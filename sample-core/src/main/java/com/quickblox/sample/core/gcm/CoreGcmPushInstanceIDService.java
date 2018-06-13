package com.quickblox.sample.core.gcm;

import com.google.android.gms.iid.InstanceIDListenerService;


public abstract class CoreGcmPushInstanceIDService extends InstanceIDListenerService {
    public static final String TAG = CoreGcmPushInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        GooglePlayServicesHelper playServicesHelper = new GooglePlayServicesHelper();
        if (playServicesHelper.checkPlayServicesAvailable()) {
            playServicesHelper.registerForGcm(getSenderId());
        }
    }
    protected abstract String getSenderId();
}
