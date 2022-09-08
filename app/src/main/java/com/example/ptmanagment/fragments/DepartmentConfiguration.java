package com.example.ptmanagment.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.ptmanagment.R;
import com.example.ptmanagment.component.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepartmentConfiguration extends Fragment {

    private EditText firstDep,
            secDep,
            thirdDep,
            forDep;
    private Button add;
    private FirebaseDatabase database;
    private DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_department_conf, container, false);

        firstDep = view.findViewById(R.id.first_dep);
        secDep = view.findViewById(R.id.sec_depart);
        thirdDep = view.findViewById(R.id.third_dep);
        add = view.findViewById(R.id.add_dep);
        forDep= view.findViewById(R.id.for_dep);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Departments");
        ArrayList<String> departmentList = new ArrayList<>();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    departmentList.add(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(firstDep.getText().toString().isEmpty()))
                    departmentList.add(firstDep.getText().toString());
                if(!(secDep.getText().toString().isEmpty()))
                    departmentList.add(secDep.getText().toString());
                if(!(thirdDep.getText().toString().isEmpty()))
                    departmentList.add(thirdDep.getText().toString());
                if(!(forDep.getText().toString().isEmpty()))
                    departmentList.add(forDep.getText().toString());
                myRef.setValue(departmentList);
                HomeFragment home = new HomeFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout,home ).commit();
            }
        });
        return view;
    }
}