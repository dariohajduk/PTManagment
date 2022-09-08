package com.example.ptmanagment.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ChangePassword extends Fragment {

    private EditText newPassword,
            confNewPassword;
    private Button change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        newPassword = view.findViewById(R.id.new_password);
        confNewPassword = view.findViewById(R.id.confirm_new_password);
        change = view.findViewById(R.id.change_btn);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newPassword.getText().toString().equals(confNewPassword.getText().toString())) {
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).updatePassword(newPassword.getText().toString());
                }
                else
                {
                    newPassword.setError("Not Valid Password");
                    confNewPassword.setError("Not Valid Password");
                    ClearEditText(newPassword);
                    ClearEditText(confNewPassword);
                }
            }
        });

        return view;
    }



    public void ClearEditText(EditText editText) {
        //Clear Error Inputs In EditText
        boolean valid = false;
        editText.getText().clear();
    }
}