package com.bawaaba.rninja4.rookie.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ashokvarma.bottomnavigation.TextBadgeItem;
import com.bawaaba.rninja4.rookie.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by rninja4 on 11/1/17.
 */

public class Utils {


    private Context context;

    public Utils(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    public static String getTagForFragment(Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    public static boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    public static String toUtc(String givenDate) {
        String formattedDate = "";
        try {
            DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
            Date date = inputFormat.parse(givenDate);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            formattedDate = dateFormat.format(date);
        } catch (Exception e) {
            Log.d("Utils", "Could not convert date to utc", e);
        }
        return formattedDate;
    }
    public static long dateToTimeInMillisecond(String givenDate) {
        long timeinmillis = 0;
        try {
//            String formattedDate = toUtc(givenDate);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");
            Date date = sdf.parse(givenDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            timeinmillis = calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeinmillis;
    }

    public static String toFormattedDate(String givenDate) {
        try {
            long milliSeconds = Long.parseLong(givenDate);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy, hh:ss aaa");
            // Create a calendar object that will convert the date and time value in milliseconds to date.
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds * 1000L);
            return formatter.format(calendar.getTime());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return givenDate;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }
    public static void hideSoftKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static Dialog showLoadingDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progressDialog.setContentView(R.layout.progress_view);
        return progressDialog;
    }
    public void updateContext(Context context) {
        if (context != null)
            this.context = context.getApplicationContext();
    }
    public static TextBadgeItem getTextBadge() {
        TextBadgeItem textBadgeItem = new TextBadgeItem();
        textBadgeItem.setGravity(Gravity.TOP | Gravity.RIGHT);
        return textBadgeItem;
    }
}
