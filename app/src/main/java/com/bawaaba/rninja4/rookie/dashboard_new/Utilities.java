package com.bawaaba.rninja4.rookie.dashboard_new;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
}
