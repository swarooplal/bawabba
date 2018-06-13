package com.bawaaba.rninja4.rookie.utils;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by rninja4 on 11/1/17.
 */

public class BaseFragment  extends Fragment{


    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;

    @Override
    public void onResume() {
        super.onResume();
        registerReceivers();
    }

    @Override
    public void onPause() {
        unregisterReceivers();
        super.onPause();
    }

    public void registerReceivers() {
        if (broadcastReceiver != null && intentFilter != null && getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(broadcastReceiver, intentFilter);
        }
    }

    public void unregisterReceivers() {
        if (broadcastReceiver != null && intentFilter != null && getActivity() != null) {
            LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(broadcastReceiver);
        }
    }

    public void addBroadcastListener(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        this.broadcastReceiver = broadcastReceiver;
        this.intentFilter = intentFilter;
    }

}

