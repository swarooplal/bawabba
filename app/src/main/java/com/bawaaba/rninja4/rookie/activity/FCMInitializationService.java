//package com.example.rninja4.rookie.activity;
//
//import android.annotation.SuppressLint;
//import android.util.Log;
//
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
//
///**
// * Created by rninja4 on 2/5/18.
// */
//
//public class FCMInitializationService extends FirebaseInstanceIdService {
//
//    private static final String TAG = "FCMInitializationService";
//
//    @SuppressLint("LongLogTag")
//    @Override
//    public void onTokenRefresh() {
//        String fcmToken = FirebaseInstanceId.getInstance().getToken();
//
//        Log.d(TAG, "FCM Device Token:" + fcmToken);
//        //Save or send FCM registration token
//    }
//}
