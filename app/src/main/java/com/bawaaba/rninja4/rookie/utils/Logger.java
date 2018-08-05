package com.bawaaba.rninja4.rookie.utils;

import android.util.Log;

/**
 * Created by rninja4 on 11/1/17.
 */

public class Logger {

    public static final String TAG = "GCM";


    private static final boolean dAllowed = true;
    private static final boolean iAllowed = true;
    private static final boolean vAllowed = true;
    private static final boolean wAllowed = true;
    private static final boolean eAllowed = true;
    private static final boolean wtfAllowed = true;
    private static final boolean printlnAllowed = true;

    public static void d(String msg) {
        if (dAllowed )
            Log.d(TAG, msg);
    }

    public static void d(String msg, Exception e) {
        if (dAllowed )
            Log.d(TAG, msg, e);
        e.printStackTrace();
    }

    public static void i(String msg) {
        if (iAllowed )
            Log.i(TAG, msg);
    }

    public static void i(String msg, Exception e) {
        if (iAllowed )
            Log.i(TAG, msg, e);
        e.printStackTrace();
    }

    public static void v(String msg) {
        if (vAllowed )
            Log.v(TAG, msg);
    }

    public static void v(String msg, Exception e) {
        if (vAllowed )
            Log.v(TAG, msg, e);
        e.printStackTrace();
    }

    public static void w(String msg) {
        if (wAllowed )
            Log.w(TAG, msg);
    }

    public static void w(String msg, Exception e) {
        if (wAllowed )
            Log.w(TAG, msg, e);
        e.printStackTrace();
    }

    public static void e(String msg) {
        if (eAllowed )
            Log.e(TAG, msg);
    }

    public static void e(String msg, Exception e) {
        if (eAllowed )
            Log.e(TAG, msg, e);
        e.printStackTrace();
    }

    public static void wtf(String msg) {
        if (wtfAllowed )
            Log.wtf(TAG, msg);
    }

    public static void wtf(String msg, Exception e) {
        if (wtfAllowed )
            Log.wtf(TAG, msg, e);
        e.printStackTrace();
    }

    public static void println(String msg) {
        // Log.println(0, TAG, msg);
        if (printlnAllowed )
            System.out.println(msg);
    }

    public static void logBigString(String veryLongString) {
        int maxLogSize = 1000;

        for (int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v(TAG, veryLongString.substring(start, end));
        }
    }
}
