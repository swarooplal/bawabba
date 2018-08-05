package com.quickblox.sample.core;

import android.app.Application;
import android.text.TextUtils;

import com.quickblox.auth.session.QBSession;
import com.quickblox.auth.session.QBSessionManager;
import com.quickblox.auth.session.QBSessionParameters;
import com.quickblox.auth.session.QBSettings;
import com.quickblox.core.ServiceZone;
import com.quickblox.sample.core.models.QbConfigs;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;

public class CoreApp extends Application {
    public static final String TAG = CoreApp.class.getSimpleName();

    private static CoreApp instance;
    private static final String QB_CONFIG_DEFAULT_FILE_NAME = "qb_config.json";
    private QbConfigs qbConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initQBSessionManager();
        initQbConfigs();
        initCredentials();
    }

    private void initQbConfigs() {
        qbConfigs = CoreConfigUtils.getCoreConfigsOrNull(getQbConfigFileName());
    }

    public static synchronized CoreApp getInstance() {
        return instance;
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

    private void initQBSessionManager() {
        QBSessionManager.getInstance().addListener(new QBSessionManager.QBSessionListener() {
            @Override
            public void onSessionCreated(QBSession qbSession) {
                //System.out.println("Prashant CoreApp Session Created");
            }

            @Override
            public void onSessionUpdated(QBSessionParameters qbSessionParameters) {
                //System.out.println("Prashant CoreApp Session Updated");
            }

            @Override
            public void onSessionDeleted() {
                //System.out.println("Prashant CoreApp Session Deleted");
            }

            @Override
            public void onSessionRestored(QBSession qbSession) {
                //System.out.println("Prashant CoreApp Session Restored");
            }

            @Override
            public void onSessionExpired() {
                //System.out.println("Prashant CoreApp Session Expired");
            }

            @Override
            public void onProviderSessionExpired(String provider) {
                //System.out.println("Prashant CoreApp Session Expired for provider:" + provider);
            }
        });
    }
}