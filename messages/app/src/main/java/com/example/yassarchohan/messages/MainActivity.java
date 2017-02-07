package com.example.yassarchohan.messages;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    public static final int RC_Signin = 1;
    private EditText edt;
    private EditText edt2;

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String musername;
    private ChildEventListener mchildlistener;
    private firview mMessageAdapter;
    private DatabaseReference mDatabase;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInResult result;
    private gettermethods gm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = (Button) findViewById(R.id.signin);
        Button btn2 = (Button) findViewById(R.id.signup);

        //  txt = (TextView) findViewById(R.id.forerror);

        edt = (EditText) findViewById(R.id.forname);
        edt2 = (EditText) findViewById(R.id.forpassword);

        gm = new gettermethods();

        Authsetup();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        SignInButton btn3 = (SignInButton) findViewById(R.id.sign_in_button);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();

            }
        });

        Authsetup();


    }


    public void Authsetup() {

        fAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Toast.makeText(MainActivity.this, "login succesfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,letschat.class);
                    startActivity(intent);
                } else {
                    // User is signed out
                    Toast.makeText(MainActivity.this, "user is signed out", Toast.LENGTH_SHORT).show();

                }


            }

        };
    }


    @Override
    public void onStart() {
        super.onStart();
        fAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            fAuth.removeAuthStateListener(mAuthListener);

        }
    }


    public void forsignin(View v) {
        musername = edt.getText().toString();

        fAuth.signInWithEmailAndPassword(musername, edt2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(MainActivity.this, "sorry to sign in plz sign up first", Toast.LENGTH_LONG).show();

                        gm.setUsername(musername);


                        if (!task.isSuccessful()) {
                            //    txt.setText("error handling auth" + task.getException());
                        } else {
                            Intent intent = new Intent(MainActivity.this, letschat.class);
                            intent.putExtra("old value",musername);
                            startActivityForResult(intent,0);

                            // Toast.makeText(MainActivity.this, "login successfull", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    public void forsignup(View v) {
        fAuth.createUserWithEmailAndPassword(edt.getText().toString(), edt2.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        Toast.makeText(MainActivity.this, "createUserWithEmail:onComplete: " + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "you are registered",
                                    Toast.LENGTH_LONG).show();

                            if (task.isComplete()) {

                            }

                            edt2.setText("");
                            edt.setText("");

                        } else {
                            Toast.makeText(MainActivity.this, "registration inwith:failed" + task.getException(), Toast.LENGTH_SHORT).show();
                            //  Log.d("FAuth", "Registration InWithEmail:failed"+task.getException());
                            // Log.d("FAuth", "Registration Failed");
                            edt.setText("");
                            edt2.setText("");
//                            txt.setText("sorry to register you sir please enter correct email or enter strong password with at least 6 chars");
                        }

                        // ...
                    }
                });
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_Signin);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_Signin) {
            result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
                Toast.makeText(MainActivity.this, "Sign_in Successed", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(MainActivity.this, "sorry to signin from google", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        // Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //   Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            musername = acct.getEmail().toString();
                            gm.setUsername(musername);
                            Toast.makeText(MainActivity.this, "Authentication Successed", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, letschat.class);
                            startActivity(intent);
                        }
                        // ...
                    }
                });
    }


    private void handleSignInResult(GoogleSignInResult result) {
     //   Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
          //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            Toast.makeText(MainActivity.this, "signin successfull" + acct.getDisplayName(), Toast.LENGTH_SHORT).show();
        } else {
            // Signed out, show unauthenticated UI.
            onRestart();
        }
    }

}

//    private void onsignin(String username) {
//        musername = username;
//        attach();
//
//    }
//
//    private void onsignout(){
//        musername = null;
//        mMessageAdapter.clear();
//        detach();
//    }
//
//    public void attach() {
//        if (mchildlistener != null) {
//            mchildlistener = new ChildEventListener() {
//                @Override
//                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    gettermethods gm = dataSnapshot.getValue(gettermethods.class);
//                    mMessageAdapter.add(gm);
//                    Intent intent = new Intent(MainActivity.this,letschat.class);
//                    startActivity(intent);
//                }
//
//                @Override
//                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            };
//        }
//        mDatabase.addChildEventListener(mchildlistener);
//    }
//
//    public void detach(){
//        if(mchildlistener != null){
//          mDatabase.removeEventListener(mchildlistener);
//            mchildlistener = null;
//        }
//    }
//}



