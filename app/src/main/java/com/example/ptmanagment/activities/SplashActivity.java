package com.example.ptmanagment.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ptmanagment.R;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        int delaySec = 3000;
        TextView wellcome=(TextView)findViewById(R.id.loggedIn);
        //region Open in Full Screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
                );
        //endregion

        //region Close the Activity after 3 sec and open Main Activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(splashIntent);
                finish();
            }
        },delaySec);
        //endregion

    }
}