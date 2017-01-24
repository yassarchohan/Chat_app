package com.example.yassarchohan.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class letschat extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letschat);


        Button btn = (Button) findViewById(R.id.messaging);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(letschat.this,messaging.class);
                startActivity(intent);
            }
        });

        fAuth = FirebaseAuth.getInstance();

        Button btn2 = (Button) findViewById(R.id.signout);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                Intent intent = new Intent(letschat.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
