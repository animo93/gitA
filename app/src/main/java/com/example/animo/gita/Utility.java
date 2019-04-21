package com.example.animo.gita;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by animo on 18/6/17.
 */

public class Utility {
    public String getAccessToken(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString(mContext.getString(R.string.access_token),null);
        return authToken;
    }

    public String getRegToken(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE);
        String regToken = sharedPreferences.getString("reg_token",null);
        return regToken;
    }

    public String getClientId(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE);
        String clientId = sharedPreferences.getString("client_id",null);
        return clientId;
    }

    public String getClientSecret(Context mContext){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE);
        String clientSecret = sharedPreferences.getString("client_secret",null);
        return clientSecret;
    }

    public void createToast(Context mContext,String text,int duration){
        Toast toast=Toast.makeText(mContext,text,duration);
        toast.show();
    }

    public boolean isInternetAvailable(Context context){
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
