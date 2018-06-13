package com.bawaaba.rninja4.rookie.App;

import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GoogleApiAvailability;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.quickblox.core.SubscribePushStrategy;
import com.quickblox.messages.services.QBPushManager;
import com.quickblox.messages.services.SubscribeService;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.models.QbConfigs;
import com.quickblox.sample.core.utils.ActivityLifecycle;

/**
 * Created by rninja4 on 4/4/17.
 */

public class AppController extends CoreApp {
    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";
    private QbConfigs qbConfigs;
    private static AppController instance;

    public static final String APP_ID = "63578";
    public static final String AUTH_KEY = "uBCnkCW-DhTpG2L";
    public static final String AUTH_SECRET = "qeHzBb7PwxrLzgX";
    public static final String ACCOUNT_KEY = "tEzfi5kzn3CAMPSVc41J";

//    {
//        "app_id": "66977",
//            "auth_key": "OnpJYFqqbAb4U2B",
//            "auth_secret": "X3SbOM6ypEQ3hJE",
//            "account_key": "1uhMsmEaWog82cxWnzsz",
//            "api_domain": "https://api.quickblox.com",
//            "chat_domain": "chat.quickblox.com",
//            "gcm_sender_id": "761750217637"
//    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ActivityLifecycle.init(this);
        QBSettings.getInstance().init(instance, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
        QBSettings.getInstance().setEnablePushNotification(true);
        QBSettings.getInstance().setSubscribePushStrategy(SubscribePushStrategy.ALWAYS);
//        QBSettings.getInstance().setEnablePushNotification(false);
//        QBSettings.getInstance().setSubscribePushStrategy(SubscribePushStrategy.NEVER);
        initPushManager();
        SubscribeService.subscribeToPushes(instance, true);
    }
    private void initPushManager() {
        QBPushManager.getInstance().addListener(new QBPushManager.QBSubscribeListener() {
            @Override
            public void onSubscriptionCreated() {
            }

            @Override
            public void onSubscriptionError(Exception e, int resultCode) {
                if (resultCode >= 0) {
                    String error = GoogleApiAvailability.getInstance().getErrorString(resultCode);
                }
            }

            @Override
            public void onSubscriptionDeleted(boolean success) {

            }
        });
    }
    public static synchronized AppController getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void initCredentials(){
        if (qbConfigs != null) {
            QBSettings.getInstance().init(getApplicationContext(), qbConfigs.getAppId(), qbConfigs.getAuthKey(), qbConfigs.getAuthSecret());
            QBSettings.getInstance().setAccountKey(qbConfigs.getAccountKey());

            if (!TextUtils.isEmpty(qbConfigs.getApiDomain()) && !TextUtils.isEmpty(qbConfigs.getChatDomain())) {
                QBSettings.getInstance().setEndpoints(qbConfigs.getApiDomain(), qbConfigs.getChatDomain(), ServiceZone.PRODUCTION);
                QBSettings.getInstance().setZone(ServiceZone.PRODUCTION);
            }
        }
    }

    public QbConfigs getQbConfigs(){
        return qbConfigs;
    }

    protected String getQbConfigFileName(){
        return QB_CONFIG_DEFAULT_FILE_NAME;
    }


}