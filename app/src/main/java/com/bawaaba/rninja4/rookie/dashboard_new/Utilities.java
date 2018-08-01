package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class Utilities {
    public static void  hideKeyBoard(Activity activity) {
        try {
            hideKeyBoard(activity.getCurrentFocus() != null? activity.findViewById(android.R.id.content) : activity.getCurrentFocus());
        } catch (Exception ignored) {
        }

    }

    public static void hideKeyBoard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            //}
        } catch ( Exception ignored) {
        }

    }

    public static boolean hasPermission(Context ctx, int requestCode, String [] permissions, String toastMessage, boolean showDialog) {
        int permission = 0;
        for (String permission1 : permissions) {
            permission += ContextCompat.checkSelfPermission(ctx,
                    permission1);
            if (permission != PackageManager.PERMISSION_GRANTED && ctx instanceof Activity) {
                ActivityCompat.shouldShowRequestPermissionRationale((Activity) ctx, permission1);
            }
        }
        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (showDialog && ctx instanceof Activity) {
                ActivityCompat.requestPermissions((Activity) ctx,
                        permissions,
                        requestCode);
            } else if (toastMessage != null) {
                Toast.makeText(ctx, toastMessage, Toast.LENGTH_SHORT).show();
            }
            return false;
        }
        return true;
    }
}
