package com.bawaaba.rninja4.rookie.gcm;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;

import com.bawaaba.rninja4.rookie.MainActivity;
import com.bawaaba.rninja4.rookie.R;
import com.bawaaba.rninja4.rookie.database.BlockUserDataBaseHelper;
import com.bawaaba.rninja4.rookie.manager.ObjectFactory;
import com.bawaaba.rninja4.rookie.model.MessageEvent;
import com.bawaaba.rninja4.rookie.utils.chat.ChatHelper;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.sample.core.gcm.CoreGcmPushListenerService;
import com.quickblox.sample.core.utils.NotificationUtils;
import com.quickblox.sample.core.utils.constant.GcmConsts;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by rninja4 on 12/14/17.
 */

public class GcmPushListenerService extends CoreGcmPushListenerService {
    private static final int NOTIFICATION_ID = 1;
    BlockUserDataBaseHelper blockUserDataBaseHelper;
    ArrayList<Integer> allBlockedUsers;
    @Override
    public void onMessageReceived(String from, Bundle data) {
        blockUserDataBaseHelper=new BlockUserDataBaseHelper(this);
        allBlockedUsers = blockUserDataBaseHelper.getAllBlockedUsers();

        super.onMessageReceived(from, data);
    }

    @Override
    protected void showNotification(final Bundle data, final String messages) {
        String recipientid=data.getString("user_id");
        if (recipientid!=null)
        if (allBlockedUsers.contains(Integer.parseInt(recipientid))) {
            return;
        }
        ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveNewMessageArrived(true);
        final String personName = "New Message from ";
        Bitmap profileImg = null;
        try {
            profileImg = getBitmapFromURL(data.getString("profileimg"));
        } catch (Exception e) {
            profileImg =  BitmapFactory.decodeResource(getResources(), R.drawable.user);
        }
        showUnreadMessages();
        NotificationUtils.showNotification(this, MainActivity.class, personName, messages, profileImg, R.mipmap.ic_notif_b, personName.hashCode());
    }
    private void showUnreadMessages() {
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                try {
                    ChatHelper.getInstance().unReadMessageCount(new QBEntityCallback<Integer>() {
                        @Override
                        public void onSuccess(Integer total, Bundle bundle) {
                            ObjectFactory.getInstance().getAppPreference(getApplicationContext()).saveUnreadMessage(total);
                            EventBus.getDefault().post(new MessageEvent("Comes from showUnreadMessages!"));
                        }
                        @Override
                        public void onError(QBResponseException e) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mHandler.sendEmptyMessage(0);
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void sendPushMessageBroadcast(String message) {
        Intent gcmBroadcastIntent = new Intent(GcmConsts.ACTION_NEW_GCM_EVENT);
        gcmBroadcastIntent.putExtra(GcmConsts.EXTRA_GCM_MESSAGE, message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(gcmBroadcastIntent);
    }
}

