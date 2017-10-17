package com.example.animo.gita;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by animo on 16/10/17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();
    @Override
    public void onTokenRefresh() {
        Log.e(TAG,"inside onTokenRefreshe");
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Firebase Token "+refreshedToken);
    }
}
