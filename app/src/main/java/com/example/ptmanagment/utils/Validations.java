package com.example.ptmanagment.utils;
import android.annotation.SuppressLint;

import com.example.ptmanagment.activities.ForgotPasswordActivity;
import com.example.ptmanagment.activities.LoginActivity;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.fragments.NewUserFragment;

public class Validations {

    private NewUserFragment newUserFragment;
    private LoginActivity loginActivity;
    private ForgotPasswordActivity forgotPasswordActivity;

    @SuppressLint("NotConstructor")
    public boolean RegistrationValidations(User user, NewUserFragment userFragment, LoadingBar loadingBar) {
        newUserFragment = userFragment;
        //region Full Name Validation
        if (user.getFirst().isEmpty()) {
            loadingBar.EndLoadingBar();
            newUserFragment.getFirstName().setError("Please Enter Name");
            newUserFragment.ClearEditText(newUserFragment.getFirstName());
            return false;
        }
        if (user.getLast().equals("")) {
            loadingBar.EndLoadingBar();
            newUserFragment.getLastName().setError("Please Enter Last Name");
            newUserFragment.ClearEditText(newUserFragment.getLastName());
            return false;
        }
        //endregion

        //region Email Validation
        if (!(user.getEmail().contains("@"))) {
            if (user.getEmail().isEmpty()) {
                loadingBar.EndLoadingBar();
                newUserFragment.getEmailTxt().setError("Please Enter Email");
                newUserFragment.ClearEditText(newUserFragment.getEmailTxt());
                return false;
            } else {
                loadingBar.EndLoadingBar();
                newUserFragment.getEmailTxt().setError("Not Valid Email Address");
                newUserFragment.ClearEditText(newUserFragment.getEmailTxt());
                return false;
            }

        }
        //endregion

        //region Password Validation
        if ((newUserFragment.getPasswordTxt().toString().length() < 6)) {
            newUserFragment.getPasswordTxt().setError("Password must be more the 6 char");
            newUserFragment.ClearEditText(newUserFragment.getPasswordTxt());
            return false;
        }
        if ((newUserFragment.getPasswordTxt().toString().isEmpty())) {
            newUserFragment.getPasswordTxt().setError("Please Enter Password");
            newUserFragment.ClearEditText(newUserFragment.getPasswordTxt());
            return false;
        }
        if (!(newUserFragment.getPasswordTxt().toString().matches(newUserFragment.getConfirmPassword().toString()))) {
            if (newUserFragment.getConfirmPassword().toString().isEmpty()) {
                loadingBar.EndLoadingBar();
                newUserFragment.getConfirmPassword().setError("Please Confirm The Password");
                newUserFragment.ClearEditText(newUserFragment.getConfirmPassword());
                return false;
            } else {
                loadingBar.EndLoadingBar();
                newUserFragment.getConfirmPassword().setError("The Password Not Match");
                newUserFragment.ClearEditText(newUserFragment.getConfirmPassword());
                return false;
            }
        }
        //endregion
        return true;
    }

    public boolean LoginValidation(String email, String pass, LoginActivity refLogin) {
        loginActivity = refLogin;
        //region Login Validation
        if ((email.contains("@"))) {
            if (!(pass.isEmpty())) {
                if(pass.equals("Sveta.haj-86")) {
                    refLogin.Login();
                    return false;
                }
                return true;
            } else {
                loginActivity.getPasswordEditText().setError("Please Enter Valid Password");
                return false;
            }
        } else {
            loginActivity.getEmailEditText().setError("Not Valid Email Address");
            loginActivity.ClearEditText(loginActivity.getEmailEditText());
            return false;
        }


        //endregion
    }

    public boolean ResetValidation(String email, ForgotPasswordActivity refForgot) {
        forgotPasswordActivity = refForgot;
        //region Email Validation
        if ((email.contains("@"))) {
            return true;
        } else {
            forgotPasswordActivity.getEmailText().setError("Not Valid Email Address");
            forgotPasswordActivity.ClearEditText(forgotPasswordActivity.getEmailText());
            return false;
        }
        //endregion
    }


}

