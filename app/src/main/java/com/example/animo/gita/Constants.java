package com.example.animo.gita;

import android.content.res.Resources;

/**
 * Created by animo on 10/5/17.
 */

public class Constants {

    public static final String AUTH_TOKEN= Resources.getSystem().getString(R.string.auth_token);

    public static final String ROOT_URL="https://api.github.com";

    public static final String USER="/user";

    public static final String REPOSITORIES = USER+"/repos";
}
