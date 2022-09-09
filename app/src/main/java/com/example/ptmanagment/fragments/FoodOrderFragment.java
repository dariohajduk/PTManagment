package com.example.ptmanagment.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.example.ptmanagment.component.Order;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.utils.LoadingBar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class FoodOrderFragment extends Fragment {

    //region Private Variables
    private FirebaseDatabase database;
    private DatabaseReference restDB ,mealDB,drinkDB,orderDB;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private Spinner restName, meal, drink;
    private String resturantName;
    private Button order, clear;
    private LoadingBar loadingBar;
    private Context context;
    private User connected;
    private MainActivity refMain;

    //endregion


    //region Constructors
    public FoodOrderFragment() {
    }
    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_order, container, false);

        //region Initialize Variables
        ArrayList<String> restList = new ArrayList<>();
        ArrayList<String> mealList = new ArrayList<>();
        ArrayList<String> drinkList = new ArrayList<>();
        refMain = (MainActivity) getActivity();
        connected = refMain.refUser;
        loadingBar = new LoadingBar();
        context = this.getContext();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();
        orderDB = database.getReference("Orders");
        restDB = database.getReference("Restaurants");
        drinkDB = database.getReference("Drinks");
        restName = view.findViewById(R.id.choose_rest);
        meal = view.findViewById(R.id.choose_meal);
        drink = view.findViewById(R.id.choose_drink);
        clear = view.findViewById(R.id.clear_btn);
        order = view.findViewById(R.id.order_btn);

        restList.clear();
        drinkList.clear();
        //endregion

        //region Rest Data Base
        restDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    restList.add(dataSnapshot.getKey());
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, restList);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                restName.setAdapter(areasAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        restName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                resturantName = restName.getSelectedItem().toString();
                System.out.println(resturantName);
                //region Meal Data Base
                mealList.clear();
                mealDB = database.getReference("Restaurants");
                mealDB.child(resturantName).child("Meal").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            mealList.add(dataSnapshot.getValue().toString());
                        }
                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mealList);
                        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        meal.setAdapter(areasAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            //endregion
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        //endregion

        //region Drinks Data Base
        drinkDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    drinkList.add(Objects.requireNonNull(dataSnapshot.getValue()).toString());
                }
                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, drinkList);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                drink.setAdapter(areasAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //endregion

        //region Buttons Action
        //Clear button
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restName.setSelection(0);
                drink.setSelection(0);
            }
        });
        //Order button
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.LoadingBarShow(context);
                Order order = new Order(meal.getSelectedItem().toString(),
                        restName.getSelectedItem().toString(),
                        drink.getSelectedItem().toString());
                orderDB.child(connected.getDepartment()).child(connected.getFirst()+" "+connected.getLast())
                        .setValue(order);
                loadingBar.EndLoadingBar();
            }
        });
        //endregion

        return view;

    }
}

