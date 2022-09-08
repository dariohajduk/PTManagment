package com.example.ptmanagment.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.example.ptmanagment.component.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private MainActivity refMainActivity;
    private User user, selecteduser;
    private Button sendFoodOrder, usersListBtn;

    private ListView usersList;
    private FirebaseDatabase database;
    private DatabaseReference logedUserDetails, userDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        database = FirebaseDatabase.getInstance();
        userDB = database.getReference("Users");
        usersListBtn = view.findViewById(R.id.user_list);
        usersList = view.findViewById(R.id.users_list);
        sendFoodOrder = view.findViewById(R.id.send_food_order);
        sendFoodOrder.setVisibility(View.INVISIBLE);
        refMainActivity = (MainActivity) this.getActivity();
        assert refMainActivity != null;
        user = refMainActivity.refUser;
        //region User Is Manager
        if (user.isManager()) {
            ManagerUser();
        }
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
                ArrayList<String> list = refMainActivity.users;
                ArrayAdapter<String> arr;
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
    }
}