package com.quickblox.sample.core.gcm;

import android.os.Bundle;
import android.util.Log;

import com.quickblox.messages.services.gcm.QBGcmPushListenerService;
import com.quickblox.sample.core.utils.ActivityLifecycle;

public abstract class CoreGcmPushListenerService extends QBGcmPushListenerService {
    private static final String TAG = CoreGcmPushListenerService.class.getSimpleName();

    @Override
    public void sendPushMessage(Bundle data, String from, String messages) {
        super.sendPushMessage(data, from, messages);
        Log.v(TAG, "From: " + from);
        Log.v(TAG, "Message: " + messages);

//        for (String key : data.keySet()) {
//            System.out.println("Prashant CoreGcmPushListenerService  sendPushMessage :: " + key + " = " + data.get(key));
//        }
        if (ActivityLifecycle.getInstance().isBackground()) {
//            showNotification(message);
            showNotification(data, messages);
        } else {
            //sendPushMessageBroadcast(messages);
            showNotification(data, messages);
        }
    }

    //    protected abstract void showNotification(String message);
    protected abstract void showNotification(Bundle data, String message);

    protected abstract void sendPushMessageBroadcast(String message);
}
/*
public abstract class CoreGcmPushListenerService extends QBGcmPushListenerService {
    private static final String TAG = CoreGcmPushListenerService.class.getSimpleName();

    @Override
    public void sendPushMessage(final Bundle data, String from, final String messages) {
        super.sendPushMessage(data, from, messages);

        if (ActivityLifecycle.getInstance().isBackground()) {
            //showNotification(data, messages);
            System.out.println("Prashant inBackground " + messages) ;
        }

       // sendPushMessageBroadcast(messages);
    }

//    protected abstract void showNotification(String message);

    protected abstract void showNotification(Bundle data, String message);

    protected abstract void sendPushMessageBroadcast(String message);
}
*/