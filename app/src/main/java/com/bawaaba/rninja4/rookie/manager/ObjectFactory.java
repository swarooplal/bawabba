package com.bawaaba.rninja4.rookie.manager;

import android.content.Context;

import com.bawaaba.rninja4.rookie.utils.AppPreference;
import com.bawaaba.rninja4.rookie.utils.Utils;

/**
 * Created by rninja4 on 11/1/17.
 */

public class ObjectFactory {
    private static ObjectFactory instance = null;
    private AppPreference appPreference = null;
    private Utils utils = null;

    private NetworkManager networkManager = null;
    private RestClient restClient = null;


    protected ObjectFactory() {

    }

    public static ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    public static boolean isDestroyed() {
        return instance == null;
    }

    public RestClient getRestClient(Context context) {
        if (restClient == null) {
            restClient = new RestClient(context);
        } else {
            restClient.updateContext(context);
        }
        return restClient;
    }

    public NetworkManager getNetworkManager(Context context) {
        if (networkManager == null) {
            networkManager = new NetworkManager(context);
        } else {
            networkManager.updateContext(context);
        }
        return networkManager;
    }

    public AppPreference getAppPreference(Context context) {
        if(appPreference == null) {
            appPreference = new AppPreference(context);
        } else {
            appPreference.updateContext(context);
        }
        return appPreference;
    }

    public Utils getUtils(Context context) {
        if(utils == null) {
            utils = new Utils(context);
        } else {
            utils.updateContext(context);
        }
        return utils;
    }


}
