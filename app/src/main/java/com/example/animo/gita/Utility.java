package com.example.animo.gita;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
}
