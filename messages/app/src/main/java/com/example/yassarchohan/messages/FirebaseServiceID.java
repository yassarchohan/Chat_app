package com.example.yassarchohan.messages;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Yassar chohan on 1/24/2017.
 */
public class FirebaseServiceID extends FirebaseInstanceIdService {

    public static final String TAG = "FirebaseServiceID";

    @Override
    public void onTokenRefresh() {
        String refreshtoken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG , "Token is :" + refreshtoken);
    }
}
