package com.example.ptmanagment.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ptmanagment.R;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.fragments.ChangePassword;
import com.example.ptmanagment.fragments.DepartmentConfiguration;
import com.example.ptmanagment.fragments.FoodOrderFragment;
import com.example.ptmanagment.fragments.HomeFragment;
import com.example.ptmanagment.fragments.MessageFragment;
import com.example.ptmanagment.fragments.NewRestFragment;
import com.example.ptmanagment.fragments.NewUserFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    private TextView connectedUser;
    private String password;
    public User refUser,selectedUser;
    public ArrayList<String> users;
    private String emailUser;
    private FirebaseDatabase database;
    private DatabaseReference logedUserDetails,userDB;
    public int weekOfYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //region Open in Full Screen
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        //endregion

        //TODO Create Date And Time Class
        users = new ArrayList<>();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        refUser = new User();
        connectedUser = findViewById(R.id.loggedIn);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_app, R.string.close_app);
        NavigationView navigationView = findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        actionBarDrawerToggle.syncState();
        database = FirebaseDatabase.getInstance();
        logedUserDetails = database.getReference("Users");
        userDB = database.getReference("Users");
        try {
            Bundle bundle = getIntent().getExtras();
            emailUser = bundle.getString("1");
            password = bundle.getString("2");
            if (password.equals("123456789")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new ChangePassword()).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new HomeFragment()).commit();
            }
        } catch (Exception e) {

        }
        logedUserDetails.orderByChild("email").equalTo(emailUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    User user = new User(ds.child("first").getValue().toString(),ds.child("last").getValue().toString(),
                            ds.child("email").getValue().toString(),
                            ds.child("department").getValue().toString(),
                            Boolean.parseBoolean(ds.child("admin").getValue().toString()),
                            Boolean.parseBoolean(ds.child("manager").getValue().toString()),
                            ds.child("shift").getValue().toString());
                    refUser = user;
                    connectedUser.setText(String.format("Hello %s ", user.getDisplayName()));
                    //region User Validation
                    //Check if the user is Admin
                    if(!user.isAdmin()) {
                        navigationView.getMenu().setGroupVisible(R.id.admins,false);
                    }
                    //endregion

                    }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

       userDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    users.add(ds.getKey());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new HomeFragment()).commit();
                break;
            case R.id.food_order:

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                            new FoodOrderFragment()).commit();
                break;
            case R.id.message:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new MessageFragment()).commit();
                break;
            case R.id.add_user:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new NewUserFragment()).commit();
                break;
            case R.id.add_rest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new NewRestFragment()).commit();
                break;
            case R.id.add_dep:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new DepartmentConfiguration()).commit();
                break;
            case R.id.new_password_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new ChangePassword()).commit();
                break;
            case R.id.nav_logout:
                this.finish();
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}