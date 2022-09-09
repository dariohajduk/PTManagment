package com.example.ptmanagment.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ptmanagment.R;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.utils.EmailAndPassword;
import com.example.ptmanagment.utils.ExcelToObject;
import com.example.ptmanagment.utils.LoadingBar;
import com.example.ptmanagment.utils.Validations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewUserFragment extends Fragment {

    //region Private Variables
    private EditText firstName;
    private EditText lastName;
    private EditText emailTxt;
    private EditText passwordTxt;
    private EditText confirmPassword;
    private EditText shift;
    private Spinner department;
    private EmailAndPassword emailAuth;
    private User user;
    private LoadingBar loadingBar;
    private Context context;
    private Validations validations;
    private CheckBox isManager, isAdmin;
    private FirebaseDatabase database;
    private DatabaseReference departDB, userDB;
    private StorageReference storageReference;
    private Uri filePath;
    private FirebaseStorage storage;
    private NewUserFragment refUserFragment;


    //endregion

    //region Getters

    public EditText getFirstName() {
        return firstName;
    }

    public EditText getLastName() {
        return lastName;
    }

    public EditText getEmailTxt() {
        return emailTxt;
    }

    public EditText getPasswordTxt() {
        return passwordTxt;
    }

    public EditText getConfirmPassword() {
        return confirmPassword;
    }

    public EditText getShift() {
        return shift;
    }

    public Spinner getDepartment() {
        return department;
    }

    public EmailAndPassword getEmailAuth() {
        return emailAuth;
    }

    public User getUser() {
        return user;
    }

    public LoadingBar getLoadingBar() {
        return loadingBar;
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public Validations getValidations() {
        return validations;
    }

    public CheckBox getIsManager() {
        return isManager;
    }

    public CheckBox getIsAdmin() {
        return isAdmin;
    }


    //endregion

    //region Setters

    public void setFirstName(EditText firstName) {
        this.firstName = firstName;
    }

    public void setLastName(EditText lastName) {
        this.lastName = lastName;
    }

    public void setEmailTxt(EditText emailTxt) {
        this.emailTxt = emailTxt;
    }

    public void setPasswordTxt(EditText passwordTxt) {
        this.passwordTxt = passwordTxt;
    }

    public void setConfirmPassword(EditText confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setShift(EditText shift) {
        this.shift = shift;
    }

    public void setDepartment(Spinner department) {
        this.department = department;
    }

    public void setEmailAuth(EmailAndPassword emailAuth) {
        this.emailAuth = emailAuth;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setLoadingBar(LoadingBar loadingBar) {
        this.loadingBar = loadingBar;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setValidations(Validations validations) {
        this.validations = validations;
    }

    public void setIsManager(CheckBox isManager) {
        this.isManager = isManager;
    }

    public void setIsAdmin(CheckBox isAdmin) {
        this.isAdmin = isAdmin;
    }
//endregion


    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //region Initialize Variables
        View view = inflater.inflate(R.layout.fragment_new_user, container, false);
        ArrayList<String> departList = new ArrayList<>();
        refUserFragment = this;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        departDB = database.getReference("Departments");
        userDB = database.getReference("Users");
        //endregion


        if(user!=null){
            firstName.setText(user.getFirst());
            lastName.setText(user.getLast());
            shift.setText(user.getShift());
            emailTxt.setText(user.getEmail());
        }

        //region Elements Configuration
        context = this.getContext();
        loadingBar = new LoadingBar();
        emailAuth = new EmailAndPassword();
        validations = new Validations();
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        passwordTxt = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirm_password);
        department = view.findViewById(R.id.department);
        emailTxt = view.findViewById(R.id.email_reg);
        shift = view.findViewById(R.id.shift);
        isAdmin = view.findViewById(R.id.is_admin);
        isManager = view.findViewById(R.id.is_manager);
        //endregion

        //region Department Data Base Configuration
        departDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    departList.add(dataSnapshot.getValue().toString());
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, departList);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                department.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region Load Button
        Button load = view.findViewById(R.id.load_btn);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String fileName = "Users.xlsx";
                    storageReference = storage.getReference("excel/" + fileName);
                    File local = File.createTempFile("temp", ".xlsx");
                    storageReference.getFile(local).
                            addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    File file;
                                    System.out.println("File is OK");
                                    file = new File(local.getAbsolutePath());
                                    ExcelToObject excelToObject = new ExcelToObject();
                                    excelToObject.UploadeUserWithExcel(file, refUserFragment);
                                    Toast.makeText(getActivity(), "You Uploaded all the new users: "
                                            +excelToObject.getEmployeeList().size()+" users", Toast.LENGTH_LONG).show();

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        });

        //endregion

        //region Registration Button
        Button registration = view.findViewById(R.id.register);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.LoadingBarShow(context);
                //todo configuer user class
                user = new User(firstName.getText().toString(), lastName.getText().toString(),
                        emailTxt.getText().toString(), department.toString(), isAdmin.isChecked(),
                        isManager.isChecked(), shift.getText().toString());
                if (validations.RegistrationValidations(user, NewUserFragment.this, loadingBar)) {
                    loadingBar.EndLoadingBar();
                    emailAuth.createAccount(user, NewUserFragment.this);
                    Map<String, User> users = new HashMap<>();
                    users.put(user.getFirst() + " " + user.getLast(), user);
                    userDB.setValue(users);
                    FirebaseAuth.getInstance().signOut();

                } else
                    loadingBar.EndLoadingBar();

            }

        });

        //endregion

        return view;

    }

    //region Methods
    public void ClearEditText(EditText editText) {
        //Clear Error Inputs In EditText
        boolean valid = false;
        editText.getText().clear();
    }

    public void MoveToHome() {
        HomeFragment homeFragment = new HomeFragment();
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().replace(R.id.fragment_layout, homeFragment).commit();
    }
    //endregion
}
