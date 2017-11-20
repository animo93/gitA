package com.example.animo.gita;

import android.content.SharedPreferences;
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
        Log.e(TAG,"inside onTokenRefreshed");
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        SharedPreferences sharedPref = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("reg_token",refreshedToken);
        editor.commit();
        Log.d(TAG, "Firebase Token "+refreshedToken);
    }
}
