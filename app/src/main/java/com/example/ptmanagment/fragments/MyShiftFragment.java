package com.example.ptmanagment.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class MyShiftFragment extends Fragment {

    private ImageView wwPP;
    private TextView wwNumber;
    private FirebaseStorage firebaseStore = null;
    private StorageReference storageReference = null;
    private MainActivity refMainActivity;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;
    private Button upload;


    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_shift, container, false);
        upload = view.findViewById(R.id.image_upload_btn);
        upload.setVisibility(View.INVISIBLE);
        wwPP = view.findViewById(R.id.ww_plan);
        wwNumber = view.findViewById(R.id.ww_number);
        refMainActivity = (MainActivity) getActivity();
        String date = String.valueOf(refMainActivity.weekOfYear);
        wwNumber.setText(getString(R.string.workWeek) +  date);
        firebaseStore = FirebaseStorage.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        try {
            StorageReference mImageRef =
                    FirebaseStorage.getInstance().getReference("images/WeeklyPP.jpg");
            final long ONE_MEGABYTE = 1024 * 1024;
            mImageRef.getBytes(ONE_MEGABYTE)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                            wwPP.setImageBitmap(bm);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                        }
                    });
        }
        catch (Exception e)
        {

        }
        if(refMainActivity.refUser.isAdmin() || refMainActivity.refUser.isManager()) {
            upload.setVisibility(View.VISIBLE);
            wwPP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(
                            Intent.createChooser(
                                    intent,
                                    "Select Image from here..."),
                            PICK_IMAGE_REQUEST);
                }
            });
            upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (filePath != null) {

                        // Code for showing progressDialog while uploading
                        ProgressDialog progressDialog
                                = new ProgressDialog(refMainActivity);
                        progressDialog.setTitle("Uploading...");
                        progressDialog.show();

                        // Defining the child of storageReference
                        StorageReference ref
                                = storageReference
                                .child(
                                        "images/"
                                                + "WeeklyPP.jpg");

                        // adding listeners on upload
                        // or failure of image
                        ref.putFile(filePath)
                                .addOnSuccessListener(
                                        new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                            @Override
                                            public void onSuccess(
                                                    UploadTask.TaskSnapshot taskSnapshot)
                                            {

                                                // Image uploaded successfully
                                                // Dismiss dialog
                                                progressDialog.dismiss();
                                                Toast
                                                        .makeText(refMainActivity,
                                                                "Image Uploaded!!",
                                                                Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        })

                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e)
                                    {

                                        // Error, Image not uploaded
                                        progressDialog.dismiss();
                                        Toast
                                                .makeText(refMainActivity,
                                                        "Failed " + e.getMessage(),
                                                        Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                })
                                .addOnProgressListener(
                                        new OnProgressListener<UploadTask.TaskSnapshot>() {

                                            // Progress Listener for loading
                                            // percentage on the dialog box
                                            @Override
                                            public void onProgress(
                                                    UploadTask.TaskSnapshot taskSnapshot)
                                            {
                                                double progress
                                                        = (100.0
                                                        * taskSnapshot.getBytesTransferred()
                                                        / taskSnapshot.getTotalByteCount());
                                                progressDialog.setMessage(
                                                        "Uploaded "
                                                                + (int)progress + "%");
                                            }
                                        });
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(getContext().getContentResolver() ,
                                filePath);
                wwPP.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}