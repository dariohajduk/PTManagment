package com.example.ptmanagment.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.ptmanagment.R;
import com.example.ptmanagment.utils.EmailAndPassword;
import com.example.ptmanagment.utils.LoadingBar;
import com.example.ptmanagment.utils.Validations;

public class ForgotPasswordActivity extends AppCompatActivity {
    //region Variables
    protected String email;
    private EditText emailText;
    private Validations validations;
    private Context context;
    private EmailAndPassword emailAndPassword;
    //endregion

    //region Getters
    public String getEmail() {
        return email;
    }
    public EditText getEmailText() {
        return emailText;
    }
    public Context getContext() {
        return context;
    }
    public EmailAndPassword getEmailAndPassword() {
        return emailAndPassword;
    }
//endregion

    //region Setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setEmailText(EditText emailText) {
        this.emailText = emailText;
    }
    public void setContext(Context context) {
        this.context = context;
    }
    public void setEmailAndPassword(EmailAndPassword emailAndPassword) {
        this.emailAndPassword = emailAndPassword;
    }
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        //region Open in Full Screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //endregion

        context = this;
        LoadingBar loadingBar = new LoadingBar();
        emailAndPassword = new EmailAndPassword();
        validations = new Validations();
        Button reset = findViewById(R.id.reset_btn);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailText = findViewById(R.id.email_reset);
                email = emailText.getText().toString();
                if (email.isEmpty()) {
                    emailText.setError("Please Enter an E-mail");
                } else {
                    if (validations.ResetValidation(email, ForgotPasswordActivity.this)) {
                        loadingBar.LoadingBarShow(context);
                        emailAndPassword.SendEmailReset(email, ForgotPasswordActivity.this,loadingBar);
                    } else {
                        emailText.setError("Not Valid Email Address");
                    }
                }
            }
        });

    }

    public void ClearEditText(EditText editText) {
        //Clear Error Inputs In EditText
        editText.getText().clear();
    }
}