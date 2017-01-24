package com.example.yassarchohan.messages;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class messaging extends AppCompatActivity {

    FirebaseDatabase fb;
    private NotificationCompat.Builder notification;
    private DatabaseReference mDatabase;
    private ChildEventListener mchildlistener;

    public static final int RC_PHOTO_PICKER = 2;
  //  public static final String ANONYMOUS = "yassar";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    private ListView mMessageListView;
    private firview mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText edt,edt2;
    private Button btn;
    private FirebaseStorage mstorage;
    private StorageReference mreference;
    private gettermethods gm;

    private String mUsername;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        notification = new NotificationCompat.Builder(this);
        notification.setAutoCancel(true);


        btn = (Button) findViewById(R.id.send);
         edt = (EditText) findViewById(R.id.message);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);

        fb = FirebaseDatabase.getInstance();
        mDatabase = fb.getReference().child("chatmessages");



        mstorage = FirebaseStorage.getInstance();

        mreference = mstorage.getReference().child("Chat_pics");


        // Initialize message ListView and its adapter
        List<gettermethods> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new firview(this, R.layout.activity_message_adapter, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        gm = new gettermethods();

        Intent intent = new Intent(this,MainActivity.class);
        edt2 = (EditText) findViewById(R.id.forname);

        String name = edt2.getText().toString();
        mUsername = name;


//        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/jpeg");
//                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
//                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
//            }
//        });


        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: Fire an intent to show an image picker
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);

                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        // Enable Send button when there's text to send
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                     btn.setEnabled(true);
                } else {
                     btn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        edt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        Intent intent = new Intent(this,messaging.class);
        final PendingIntent pi = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               String notoficationIcon = String.valueOf(notification.setSmallIcon(R.drawable.ic_launcher));
              String nameofnotification = String.valueOf(notification.setContentTitle("Chat_Box"));
                notification.setTicker("Sir it is from your Chatbox friend");
                notification.setWhen(System.currentTimeMillis());


                String userid = "";


                String msg = edt.getText().toString();
                notification.setContentText(msg);



                if(msg == null){
                    Toast.makeText(messaging.this, "could not send null message", Toast.LENGTH_SHORT).show();
                }

                else {

                    gettermethods gmre = new gettermethods(msg,mUsername,null);
                    mDatabase.push().setValue(gmre);

                    DatabaseReference Dr = fb.getReference().child("Notifications");
                    gettermethods gm1 = new gettermethods(nameofnotification,msg,notoficationIcon,mUsername);



                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(4568977,notification.build());

                    edt.setText("");
                }
            }
        });

            mchildlistener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                   gettermethods gm =  dataSnapshot.getValue(gettermethods.class);
                    mMessageAdapter.add(gm);
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


}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK){
            Uri imguri = data.getData();
            StorageReference stref = mreference.child(imguri.getLastPathSegment());

//            java.io.File xmlFile = new java.io.File(Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    + "/Filename.xml");


            stref.putFile(imguri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                         // When the image has successfully uploaded, we get its download URL

                                         Uri downloadUrl = taskSnapshot.getDownloadUrl();

                                         // Set the download URL to the message box, so that the user can send it to the database
                                         gettermethods gm = new gettermethods(null, mUsername, downloadUrl.toString());
                                         mDatabase.push().setValue(gm);
                                            }
                                   });

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
}}
