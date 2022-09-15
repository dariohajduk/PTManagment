package com.example.ptmanagment.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.example.ptmanagment.component.User;
import com.example.ptmanagment.utils.MessageTo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MessageFragment extends Fragment {

    //region Private Variables
    private ArrayList<MessageTo> messageList;
    private FirebaseDatabase database;
    private DatabaseReference messageDB, userDB;
    private ListView msgList;
    private User user;
    private MainActivity refMainActivity;
    private FloatingActionButton newMessage;
    private ArrayAdapter<String> arr;

    private Dialog dialog;


    //endregion

    //region Getters and Setters

    //endregion

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        newMessage = view.findViewById(R.id.floatingActionButton);
        msgList = view.findViewById(R.id.message_list);
        refMainActivity = (MainActivity) getActivity();
        user = refMainActivity.refUser;
        database = FirebaseDatabase.getInstance();
        messageDB = database.getReference("Users/" + user.getDisplayName() + "/Message");
        ArrayList<String> from = new ArrayList<>();
        arr = new ArrayAdapter<String>(getActivity(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, from);
        msgList.setAdapter(arr);
        messageDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String sender = null;
                    messageList = new ArrayList<>();
                    String msgValue = null;
                    MessageTo messageRef = null;
                    sender = ds.child("sender").getValue().toString();
                    MessageTo message = new MessageTo(ds.child("sender").getValue().toString(),
                            ds.child("to").getValue().toString(),
                            ds.child("msg").getValue().toString(),
                            Boolean.parseBoolean(ds.child("sender").getValue().toString()));
                    msgValue = ds.getKey();
                    messageList.add(message);
                    from.add(sender+" Massage Id: "+msgValue);
                    arr.notifyDataSetChanged();

                    msgList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            Dialog msgDialog = new Dialog(getContext());
                            msgDialog.setContentView(R.layout.read_message_dialog);
                            msgDialog.getWindow().setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                            msgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            msgDialog.show();
                            TextView fromWho = msgDialog.findViewById(R.id.message_sender);
                            TextView msgTxt = msgDialog.findViewById(R.id.message_text);
                            TextView selectedText = view.findViewById(R.id.text_view_list);

                            for(int j =0;j<from.size();i++)
                            {
                                String text = String.valueOf(msgList.getSelectedItemPosition());
                                String selectedFromList = (String) (msgList.getItemAtPosition(i));
                                System.out.println(selectedFromList);
                                System.out.println(text);

                                if(from.get(j).equals(text))
                                {
                                    fromWho.setText(messageList.get(j).getSender());
                                    msgTxt.setText(messageList.get(j).getMsg());
                                }
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }

                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        arr = new ArrayAdapter<String>(getActivity(),
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item, from);
        msgList.setAdapter(arr);

        newMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewMessageFragment newMessageFragment = new NewMessageFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, newMessageFragment).commit();
            }
        });
        messageDB.child("Messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}