package com.example.ptmanagment.utils;
import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.ForgotPasswordActivity;
import com.example.ptmanagment.activities.LoginActivity;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.fragments.HomeFragment;
import com.example.ptmanagment.fragments.NewUserFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Objects;

public class EmailAndPassword extends Activity {

    private FirebaseAuth mAuth;
    private LoadingBar loadingBar;
    private LoginActivity refLogin;
    private FirebaseUser firebaseUser;

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingBar = new LoadingBar();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
    // [END on_start_check_user]

    public void createAccount(User user, NewUserFragment userFragment) {
        //Upload data to the database
        String password = "123456789";
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                          //  Toast.makeText(userFragment.getContext(), "Authentication Success.",
                            //        Toast.LENGTH_SHORT).show();
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            assert firebaseUser != null;
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(user.getFirst()+" "+user.getLast()).build();
                            firebaseUser.updateProfile(profileUpdates);
                            user.setUid(firebaseUser.getUid());
                            System.out.println(user.getUid());
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                            System.out.println(task.getException().getMessage());
                            updateUI(null);
                            userFragment.MoveToHome();

                        }
                    }
                });
    }

    public void signIn(String email, String password, LoginActivity log, LoadingBar refLoadingBar) {
        // [START sign_in_with_email]
        refLogin = log;
        loadingBar = refLoadingBar;
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            // Sign in success, update UI with the signed-in user's information
                            updateUI(user);
                            if(password.equals("111111")) {
                                refLogin.setValid(false);
                            }
                            else {
                                refLogin.setValid(true);
                            }
                            loadingBar.EndLoadingBar();
                            assert user != null;
                            refLogin.setUserName(user.getDisplayName());
                            refLogin.SaveCerdinals();
                            refLogin.Login();
                        } else {
                            Toast.makeText(refLogin, Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_LONG).show();
                            updateUI(null);
                            refLogin.ClearEditText(refLogin.getEmailEditText());
                            refLogin.ClearEditText(refLogin.getPasswordEditText());
                            refLogin.setValid(false);
                            loadingBar.EndLoadingBar();
                        }
                    }
                });
    }

    public void SendEmailReset(String email, ForgotPasswordActivity refForgot, LoadingBar loadingBar) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    loadingBar.EndLoadingBar();
                    if (task.isSuccessful()) {
                        Toast.makeText(refForgot, "The Email Was Sent",Toast.LENGTH_LONG).show();
                        refForgot.finish();
                    } else {
                        Toast.makeText(refForgot, Objects.requireNonNull(task.getException()).getMessage(),
                                Toast.LENGTH_LONG).show();
                    }

                });
    }

    private void reload() {
    }

    public void signOut()
    {
        FirebaseAuth.getInstance().signOut();
    }

    private void updateUI(FirebaseUser user) {

    }

    public void createAccount2(User user, NewUserFragment userFragment) {
        //Upload data to the database
        String password = "123456789";
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), password);
    }

}