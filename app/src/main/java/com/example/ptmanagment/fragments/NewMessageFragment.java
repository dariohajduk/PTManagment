package com.example.ptmanagment.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ptmanagment.R;
import com.example.ptmanagment.activities.MainActivity;
import com.example.ptmanagment.utils.MessageTo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class NewMessageFragment extends Fragment {
    private MessageTo message;
    private EditText msg;
    private TextView sendTo;
    private Button sendBtn;
    private Dialog dialog;
    private String msgText;
    private ArrayAdapter<String> arr;
    private MainActivity refMainActivity;
    private FirebaseDatabase database;
    private DatabaseReference messageDB, userDB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_message, container, false);
        message = new MessageTo();
        refMainActivity = (MainActivity) getActivity();
        msg = v.findViewById(R.id.multi_line_msg);
        sendTo = v.findViewById(R.id.send_to_contact);
        sendBtn = v.findViewById(R.id.send_msg_btn);
        ArrayList<String> list = refMainActivity.users;
        database = FirebaseDatabase.getInstance();
        messageDB = database.getReference("Users");

        //region Send To
        sendTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_searchable_spinner);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                System.out.println(list);
                ListView listView = dialog.findViewById(R.id.contact_list);
                arr = new ArrayAdapter<String>(getActivity(),
                        R.layout.text_view_black,R.id.text_view_list, list);
                listView.setAdapter(arr);
                EditText editText=dialog.findViewById(R.id.search_contact);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        arr.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        sendTo.setText(arr.getItem(position));

                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });
            }
        });

        //endregion

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                message.setTo(sendTo.getText().toString());
                message.setMsg(msg.getText().toString());
                message.setNewMsg(true);
                message.setSender(refMainActivity.refUser.getDisplayName());
                int text = random.nextInt();
                messageDB.child(message.getTo()).child("Message")
                        .child(String.valueOf(text)).setValue(message);
                MessageFragment messageFragment = new MessageFragment();
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.fragment_layout, messageFragment).commit();
            }
        });

        return v;
    }
}