package com.android.nike.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.nike.UrbanApplication;

import java.io.File;

import okhttp3.Cache;

public class AppUtils {



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static Cache provideCache () {
        Cache cache = null;
        try {
            cache = new Cache( new File(UrbanApplication.getAppContext().getCacheDir(), "http-cache" ),
                    10 * 1024 * 1024 ); // 10 MB
        }
        catch (Exception e) {
            LogUtil.log( "Error", e.toString() );
        }
        return cache;
    }
}
