package com.example.yassarchohan.messages;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ForGroup extends AppCompatActivity {

    private EditText edt;
    private ListView dispalyusers,listview2;
    private Custom_adapter2 custom_adapter;
    private Button btn,btn3;
    FirebaseDatabase fb;
    private DatabaseReference mDatabase,mDatabase2,mDatabase3;
    private ChildEventListener mchildlistener;
    private gettermethods gm;
    private Custom_adapter custom;


    String id;
    String name;
    String id1;
    String email;
    FirebaseUser user;


    List<gettermethods> friendlyMessages,list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_group);


        user = FirebaseAuth.getInstance().getCurrentUser();

        fb = FirebaseDatabase.getInstance();
        mDatabase = fb.getReference().child("Users").child("Groupusers");
        mDatabase2 = fb.getReference().child("NewGroups").child(user.getUid());
        mDatabase3 = mDatabase2.child("GroupMembers");

        dispalyusers = (ListView) findViewById(R.id.toshowgroup);
        listview2 = (ListView) findViewById(R.id.finalgroup);


        btn = (Button) findViewById(R.id.forcreatinggroup);
        edt = (EditText) findViewById(R.id.forgroupname);

        //this listview displays the current users of the app
        friendlyMessages = new ArrayList<>();
        custom_adapter = new Custom_adapter2(this, R.layout.activity_custom_view2, friendlyMessages);
        dispalyusers.setAdapter(custom_adapter);


        //this listview is used to display the selected users of the group
        list2 = new ArrayList<>();
        custom = new Custom_adapter(this, R.layout.activity_custom__view, list2);


        mchildlistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gm = dataSnapshot.getValue(gettermethods.class);
                custom_adapter.add(gm);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addChildEventListener(mchildlistener);


        dispalyusers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gm = new gettermethods(edt.getText().toString(),user.getUid());
                mDatabase2.setValue(gm);



                    listview2.setAdapter(custom);
                     name = friendlyMessages.get(position).getMessage();
                     id1 = friendlyMessages.get(position).getUsername();
                    email = friendlyMessages.get(position).getPhotourl();
                    gm = new gettermethods(name,id1,email);
                    custom.add(gm);


            }
        });

        listview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                custom.remove(gm);
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               gettermethods gm2 = new gettermethods(edt.getText().toString(),user.getUid());
                mDatabase2.setValue(gm2);

                for (int i = 0;i<listview2.getCount();i++) {
                   if(!friendlyMessages.get(i).equals(gm)) {
                       mDatabase3.push().setValue(gm);

                   }
                   }


                Toast.makeText(ForGroup.this, "Group created Successfully", Toast.LENGTH_SHORT).show();

            }
        });

    }

}
