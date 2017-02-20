package com.example.yassarchohan.messages;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class messaging extends AppCompatActivity {

    Bundle bundle;

    FirebaseDatabase fb,fb2;
    private NotificationCompat.Builder notification;

    public RemoteMessage.Notification RN;

    private DatabaseReference mDatabase,mDatabase2;
    private ChildEventListener mchildlistener;

    public static final int RC_PHOTO_PICKER = 2;
    //  public static final String ANONYMOUS = "yassar";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;

    public static final String TAG = "MainActvity";
    //public static final String url = "https://console.firebase.google.com/project/practice-bf767/storage/files/Chat_pics/93.jpg";

    private ListView mMessageListView;
    private firview mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText edt, edt2;
    private Button btn;
    private FirebaseStorage mstorage;
    private StorageReference mreference;
    private gettermethods gm;

    private String mUsername;
    private Uri downloadUrl;
    public static final int progress_bar_type = 0;
    private ProgressDialog pDialog;

    String forkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        bundle = getIntent().getExtras();

        forkey = (String) bundle.get("key");

        mUsername = (String) bundle.get("Name");

        Toast.makeText(messaging.this, "name is " + mUsername, Toast.LENGTH_SHORT).show();

        Log.d("key", "onCreate: "+forkey);

        gm = new gettermethods();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String getuser = user.getUid();


        btn = (Button) findViewById(R.id.send);
        edt = (EditText) findViewById(R.id.message);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);

        fb = FirebaseDatabase.getInstance();
        mDatabase = fb.getReference().child("Conversations").child(getuser).child(forkey);

        fb2 = FirebaseDatabase.getInstance();
        mDatabase2 = fb2.getReference().child("Conversations").child(forkey).child(getuser);


        mstorage = FirebaseStorage.getInstance();

        mreference = mstorage.getReference().child("Chat_pics");


        // Initialize message ListView and its adapter
        List<gettermethods> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new firview(this, R.layout.activity_message_adapter, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        gm = new gettermethods();

        final Intent intent = new Intent(this, messaging.class);

        final Intent intent2 = new Intent(this, messaging.class);
        notification = new NotificationCompat.Builder(this);


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

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String value = intent.getStringExtra("old value");
                //   String name = edt2.getText().toString();
                //   mUsername = value;

                String msg = edt.getText().toString();

                if (msg == null) {
                    Toast.makeText(messaging.this, "could not send null message", Toast.LENGTH_SHORT).show();
                } else {

                    gettermethods gmre = new gettermethods(msg, mUsername, null);
                    mDatabase.push().setValue(gmre);
                    mDatabase2.push().setValue(gmre);

                    String Token = FirebaseInstanceId.getInstance().getToken();
                    Log.d(TAG, "MEssage Token :" + Token);
                    Toast.makeText(messaging.this, "Token is :" + Token, Toast.LENGTH_SHORT).show();


                    edt.setText("");
                }
            }
        });

        mchildlistener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                gettermethods gm = dataSnapshot.getValue(gettermethods.class);
                mMessageAdapter.add(gm);


                notification.setAutoCancel(true);
                final PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pi);


                notification.setSmallIcon(R.drawable.ic_launcher);
                notification.setContentTitle("Chat_Box");
                notification.setTicker("Sir it is from your Chatbox friend");
                notification.setContentText(gm.getMessage());

                notification.setWhen(System.currentTimeMillis());

                NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                nm.notify(0, notification.build());

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


        mMessageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                gettermethods gm1 = mMessageAdapter.getItem(position);

                DownloadTask down = new DownloadTask();
                down.execute(gm1.getPhotourl());
                Toast.makeText(messaging.this, "url" + gm1.getPhotourl(), Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(gm1.getPhotourl()));
                startActivity(intent);

                return true;

            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri imguri = data.getData();
            StorageReference stref = mreference.child(imguri.getLastPathSegment());

//            java.io.File xmlFile = new java.io.File(Environment
//                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    + "/Filename.xml");


            stref.putFile(imguri).addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // When the image has successfully uploaded, we get its download URL

                    downloadUrl = taskSnapshot.getDownloadUrl();

                    // Set the download URL to the message box, so that the user can send it to the database
                    gettermethods gm = new gettermethods(null, mUsername, downloadUrl.toString());
                    mDatabase.push().setValue(gm);


                }
            });

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }



        class DownloadTask extends AsyncTask<String, String, String> {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showDialog(progress_bar_type);
            }

            @Override
            protected String doInBackground(String... params) {
                String path = params[0];


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String Id =  user.getUid().toString();

                if(Id != null) {

                    int count;
                    try {
                        URL url = new URL(params[0]);
                        URLConnection conection = url.openConnection();
                        conection.connect();
                        Looper.prepare();

                        // this will be useful so that you can show a tipical 0-100% progress bar
                        int lenghtOfFile = conection.getContentLength();

                        //   Toast.makeText(getApplicationContext(), "New Directory created", Toast.LENGTH_SHORT).show();


                        // download the file
                        InputStream input = new BufferedInputStream(url.openStream(), 8192);


                        File rootPath = new File(Environment.DIRECTORY_DOWNLOADS+"/Chat_Box Storage");
                        if(!rootPath.exists()){
                            rootPath.mkdirs();
                            Toast.makeText(getApplicationContext(), "directory created", Toast.LENGTH_SHORT).show();
                        }


                        String name = params[0].concat(".jpg");
                        // Output stream
                        File abc = new File(rootPath, name);

                        OutputStream output = new FileOutputStream(abc);

                        byte data[] = new byte[1024];

                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                            // writing data to file
                            output.write(data, 0, count);
                            Toast.makeText(getApplicationContext(), "downloaded image", Toast.LENGTH_SHORT).show();
                        }

                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();
                        Looper.loop();


                    } catch (Exception e) {
                        Log.e("Error: ", e.getMessage());
                    }

                }

                else {
                    Toast.makeText(getApplicationContext(), "User is not authenticated", Toast.LENGTH_SHORT).show();
                }

                return null;
            }


            @Override
            protected void onProgressUpdate(String... progress) {
                //  super.onProgressUpdate(values);
                pDialog.setProgress(Integer.parseInt(progress[0]));
            }

            @Override
            protected void onPostExecute(String file_url) {
                // dismiss the dialog after the file was downloaded
                dismissDialog(progress_bar_type);

                // Displaying downloaded image into image view
                // Reading image path from sdcard
              //  String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
                // setting downloaded into image view

            }


        }
    }















