package com.example.yassarchohan.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

public class letschat extends AppCompatActivity {

    private FirebaseAuth fAuth;
    private ListView dispalyusers;
    private Custom_adapter custom_adapter;
    private Button btn,btn3;
    FirebaseDatabase fb;
    private DatabaseReference mDatabase;
    private ChildEventListener mchildlistener;
    private gettermethods gm;
    String name,name1;
    String id;
    TextView txt;

    int index;

    List<gettermethods> friendlyMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letschat);

        txt = (TextView) findViewById(R.id.welcometxt);


        fb = FirebaseDatabase.getInstance();
        mDatabase = fb.getReference().child("Users").child("CurrentUsers");

        dispalyusers = (ListView) findViewById(R.id.list123);

        friendlyMessages = new ArrayList<>();
        custom_adapter = new Custom_adapter(this, R.layout.activity_custom__view, friendlyMessages);
        dispalyusers.setAdapter(custom_adapter);

//        removecurrent();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        id = user.getUid();

        Button btn = (Button) findViewById(R.id.messaging);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(letschat.this, ForGroup.class);
                startActivity(intent);
            }
        });
        fAuth = FirebaseAuth.getInstance();

        Button btn2 = (Button) findViewById(R.id.signout);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.getCurrentUser();
                fAuth.signOut();
                Intent intent = new Intent(letschat.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn3 = (Button) findViewById(R.id.GroupChats);

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(letschat.this,User_groups.class);
                startActivity(intent);
                Toast.makeText(letschat.this, "All your groups", Toast.LENGTH_SHORT).show();
            }
        });

        mchildlistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                gm = dataSnapshot.getValue(gettermethods.class);
                 name1 = null;

                if (gm.getUsername().equals(id)) {
                    //custom_adapter.remove(gm);
                    name1 = gm.getMessage().toString();
                    txt.setText(name1);
                    txt.setTextSize(20);

                }
                else
                {
                    custom_adapter.add(gm);

                }

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


//        Intent intent =
//        startActivity(intent);


        dispalyusers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(letschat.this, messaging.class);

                    name1 = txt.getText().toString();
                    gm = new gettermethods();
                    intent.putExtra("key", friendlyMessages.get(position).getUsername());
                    intent.putExtra("Name", name1);
                    startActivity(intent);



            }

        });
    }

    public void removecurrent() {


//        if(user!= null){
//
//            for(int i =0;i < friendlyMessages.size();i++){
//                String newid = friendlyMessages.get(i).getUsername();
//                if(newid == id){
//                    friendlyMessages.remove(i);
//                    custom_adapter.notifyDataSetChanged();
//                }
//            }
//
//        }
//    }
    }
}
