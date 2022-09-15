package com.example.ptmanagment.fragments;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.example.ptmanagment.component.Order;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.utils.ExcelToObject;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private MainActivity refMainActivity;
    private User user, selecteduser;
    private Button sendFoodOrder, usersListBtn;
    private EditText inputSearch;
    private ListView usersList;
    private FirebaseDatabase database;
    private DatabaseReference logedUserDetails, userDB, orderDB;
    private ArrayAdapter<String> arr;

    private ArrayList<String> employee;
    ArrayList<Order> orders = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        database = FirebaseDatabase.getInstance();
        userDB = database.getReference("Users");
        orderDB = database.getReference("Orders");
        usersListBtn = view.findViewById(R.id.user_list);
        usersList = view.findViewById(R.id.users_list);
        sendFoodOrder = view.findViewById(R.id.send_food_order);
        sendFoodOrder.setVisibility(View.INVISIBLE);
        refMainActivity = (MainActivity) this.getActivity();
        assert refMainActivity != null;
        user = refMainActivity.refUser;
        inputSearch = view.findViewById(R.id.search_user);
        inputSearch.setVisibility(View.INVISIBLE);
        employee = new ArrayList<>();
        //region User Is Manager
        if (user.isManager()) {
            ManagerUser();
        }

        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String emp = ds.getKey();
                    Order order = ds.getValue(Order.class);
                    orders.add(order);
                    employee.add(emp);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        //endregion
        return view;
    }

    private void ManagerUser() {
        sendFoodOrder.setVisibility(View.VISIBLE);
        sendFoodOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        usersListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSearch.setVisibility(View.VISIBLE);
                usersList.setAdapter(null);
                ArrayList<String> list = refMainActivity.users;
                arr = new ArrayAdapter<String>(getActivity(), com.google.android.material.R.layout.support_simple_spinner_dropdown_item, list);
                usersList.setAdapter(arr);

                //TODO Configure how to use On Item Click Selector
                usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String selectedUser = adapterView.getSelectedView().toString();
                        NewUserFragment updatUser = new NewUserFragment();
                        userDB.child(selectedUser).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    User user1 = new User(ds.child("first").getValue().toString(), ds.child("last").getValue().toString(), ds.child("email").getValue().toString(), ds.child("department").getValue().toString(), Boolean.parseBoolean(ds.child("admin").getValue().toString()), Boolean.parseBoolean(ds.child("manager").getValue().toString()), ds.child("shift").getValue().toString());
                                    selecteduser = user1;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                        updatUser.setUser(selecteduser);
                        getFragmentManager().beginTransaction().replace(R.id.fragment_layout, updatUser).commit();
                    }
                });
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                arr.getFilter().filter(cs);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

        });

        sendFoodOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ExcelToObject createOrderFile = new ExcelToObject();
                createOrderFile.ExportOrdersToExcelFile(orders,employee);
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{refMainActivity.refUser.getEmail()});
                i.putExtra(Intent.EXTRA_SUBJECT, " test email sender");
                i.putExtra(Intent.EXTRA_TEXT, "i WANT TO TEST EMAIL SEND");
                try {
                    startActivity(Intent.createChooser(i, "send mail..."));
                } catch (android.content.ActivityNotFoundException exception) {
                    Toast.makeText(getContext(), "No Email", Toast.LENGTH_LONG).show();
                }
            }


        });



    }
}

