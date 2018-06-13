package com.bawaaba.rninja4.rookie.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rninja4 on 11/1/17.
 */

public class NetworkManager {

    public static final String BROADCAST_DATA_AVAILABILITY_UPDATED = "BROADCAST_DATA_AVAILABILITY_UPDATED";
    private Context context;
    private boolean isDataAvailable = true;
    private String connectedWifi = "";
    private List<String> checkedItems = new ArrayList<>();
    private List<String> checkedLangauges = new ArrayList<>();

    public NetworkManager(Context context) {
        if (context != null) {
            this.context = context.getApplicationContext();
        }
    }

    private static boolean isAvailable() {
        // strictly in background thread
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("http://www.google.com/").openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            return (200 <= responseCode && responseCode <= 399);
        } catch (IOException exception) {
            return false;
        }
    }

    private String getWiFiName(Context context) {
        String name = null;
        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiMgr != null) {
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if (wifiInfo != null) {
                name = wifiInfo.getSSID();
                name = removeQuotationsInCurrentSSIDForJellyBean(name);
            }
        }

        return name;
    }

    private String removeQuotationsInCurrentSSIDForJellyBean(String ssid) {
        int JELLY_BEAN = 17;
        if (android.os.Build.VERSION.SDK_INT >= JELLY_BEAN) {
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
        }
        return ssid;
    }

    public void updateContext(Context context) {
        if (context != null)
            this.context = context.getApplicationContext();
    }

    public boolean isValidWifi(List<String> validWifiNames) {

        if (validWifiNames == null || validWifiNames.isEmpty()) {
            return true;
        }

        for (String wifi : validWifiNames) {
            if (wifi.equalsIgnoreCase(connectedWifi)) {
                return true;
            }
        }

        return false;
    }

    public boolean isDataAvailable() {
        return isDataAvailable;
    }

    public void checkDataAvailability() {

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                connectedWifi = getWiFiName(context);
                return isAvailable();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                boolean notify = isDataAvailable != result;
                isDataAvailable = result;
                if (notify) {
                    Intent intent = new Intent(BROADCAST_DATA_AVAILABILITY_UPDATED);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    if (isDataAvailable) {

                    }
                }
            }

        }.execute();
    }

    public List<String> getCheckedItems() {
        return checkedItems;
    }

    public void setCheckedItems(List<String> checkedItems) {
        this.checkedItems = new ArrayList<>();
        this.checkedItems = checkedItems;
    }

    public List<String> getCheckedLangauges() {
        return checkedLangauges;
    }

    public void setCheckedLangauges(List<String> checkedItems) {
        this.checkedLangauges = new ArrayList<>();
        this.checkedLangauges = checkedItems;
    }

    public static class NetworkNotifier extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ObjectFactory.getInstance().getNetworkManager(context).checkDataAvailability();
        }
    }
}
