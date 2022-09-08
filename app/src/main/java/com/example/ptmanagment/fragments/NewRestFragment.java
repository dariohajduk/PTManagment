package com.example.ptmanagment.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ptmanagment.R;
import com.example.ptmanagment.component.Restaurant;
import com.example.ptmanagment.utils.ExcelToObject;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;

public class NewRestFragment extends Fragment {

    private EditText restName,
            restAddress,
            restPhone,
            rest_email;
    private Button add;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference departDB, userDB;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private NewRestFragment refNewRest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_rest, container, false);
        //region Initialize Variables
        refNewRest = this;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        restName = view.findViewById(R.id.rest_name);
        restAddress = view.findViewById(R.id.rest_address);
        restPhone = view.findViewById(R.id.rest_phone);
        add = view.findViewById(R.id.add_rest);
        rest_email = view.findViewById(R.id.rest_email);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Restaurants");
        //endregion

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant(restAddress.getText().toString(),
                        restPhone.getText().toString(), rest_email.getText().toString());
                myRef.setValue(restName.getText().toString());
                myRef.child(restName.getText().toString()).setValue("Details");
                myRef.child(restName.getText().toString()).child("Details").setValue(restaurant);
                HomeFragment home = new HomeFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, home).commit();
            }
        });

        //region Load Button
        Button load = view.findViewById(R.id.load_btn);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String fileName = "Restaurant.xlsx";
                    storageReference = storage.getReference("excel/" + fileName);
                    File local = File.createTempFile("temp", ".xlsx");
                    storageReference.getFile(local).
                            addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    File restaurant;
                                    System.out.println("File is OK");
                                    restaurant = new File(local.getAbsolutePath());
                                    ExcelToObject excelToObject = new ExcelToObject();
                                    try {
                                        excelToObject.UploadeRestWithExcel(restaurant, refNewRest);
                                    } catch (IOException | InvalidFormatException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getActivity(), "You Uploaded all the new users", Toast.LENGTH_LONG).show();

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        //endregion

        return view;
    }
}