package com.example.ptmanagment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ptmanagment.R;
import com.example.ptmanagment.fragments.ChangePassword;
import com.example.ptmanagment.utils.EmailAndPassword;
import com.example.ptmanagment.utils.LoadingBar;
import com.example.ptmanagment.utils.Validations;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    //region Variables
    private boolean valid = false;
    private String userName;
    private String mail;
    private String pass;
    private EditText passwordEditText;
    private EditText emailEditText;
    private LoadingBar loadingBar;
    private Context context;
    private Validations validations;
    private FirebaseUser firebaseUser;
    private String newPassword;
    private FragmentContainerView changePassword;
    private FragmentTransaction ft;
    private final Fragment changePass = getFragmentManager().findFragmentById(R.id.change_layout);
    private Button login;
    private TextView forgotPassword;
    private static final String FileName = "login";
    private static final String UserName = "Username";
    private static final String Password = "Password";
    private SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    //endregion

    //region Getters
    public String getUserName() {
        return userName;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getMail() {
        return mail;
    }

    public String getPass() {
        return pass;
    }

    public EditText getPasswordEditText() {
        return passwordEditText;
    }

    public EditText getEmailEditText() {
        return emailEditText;
    }

    public LoadingBar getLoadingBar() {
        return loadingBar;
    }

    public Context getContext() {
        return context;
    }

    public Validations getValidations() {
        return validations;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public boolean isValid() {
        return valid;
    }

    //endregion

    //region Setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPasswordEditText(EditText passwordEditText) {
        this.passwordEditText = passwordEditText;
    }

    public void setEmailEditText(EditText emailEditText) {
        this.emailEditText = emailEditText;
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

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region Open in Full Screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //endregion

        //region Stay Loged In
        sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(UserName)) {
            mail = sharedPreferences.getString(UserName, "Not found");
            pass = sharedPreferences.getString(Password, "No Pass");
            valid = true;
            Login();
        }
        //endregion

        //region Elements Configuration
        forgotPassword = findViewById(R.id.forgot_title);
        login = findViewById(R.id.login_btn);
        validations = new Validations();
        context = this;
        changePassword = findViewById(R.id.change_layout);
        ft = getFragmentManager().beginTransaction();
        ft.hide(changePass);
        loadingBar = new LoadingBar();

        //endregion

        //region On Click Actions
        //region Login Btn
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEditText = findViewById(R.id.password_title);
                emailEditText = findViewById(R.id.email_reg);
                pass = passwordEditText.getText().toString();
                mail = emailEditText.getText().toString();
                EmailAndPassword signIn = new EmailAndPassword();
                EmailAndPassword signOut = new EmailAndPassword();
                System.out.println(emailEditText + " " + pass);
                loadingBar.LoadingBarShow(context);
                if (validations.LoginValidation(mail, pass, LoginActivity.this)) {
                    signIn.signIn(mail, pass, LoginActivity.this, loadingBar);
                } else {
                    loadingBar.EndLoadingBar();
                    valid = false;
                    signOut.signOut();
                }
            }
        });
        //endregion
        //region Forgot Password
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgotIntent = new Intent(LoginActivity.this,
                        ForgotPasswordActivity.class);
                startActivity(forgotIntent);

            }
        });
        //endregion
        //endregion
    }

    //region Methods
    public void Login() {
        if (valid) {
            Intent mainActivity = new Intent(LoginActivity.this,
                    MainActivity.class);
            mainActivity.putExtra("1", mail);
            mainActivity.putExtra("2", pass);
            startActivity(mainActivity);
        }
    }

    public void ClearEditText(EditText editText) {
        //Clear Error Inputs In EditText
        editText.getText().clear();
    }

    public void SaveCerdinals() {
        sharedPreferences = getSharedPreferences(FileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(UserName, mail);
        editor.commit();
        editor.putString(Password, pass);
        editor.commit();
    }
    //endregion
}