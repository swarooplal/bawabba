package com.bawaaba.rninja4.rookie;

import com.bawaaba.rninja4.rookie.models.SampleConfigs;
import com.bawaaba.rninja4.rookie.utils.configs.ConfigUtils;
import com.bawaaba.rninja4.rookie.utils.qb.Consts;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.ActivityLifecycle;

import java.io.IOException;

/**
 * Created by rninja4 on 12/11/17.
 */

public class Appquick extends CoreApp {

    private static final String TAG = Appquick.class.getSimpleName();
    private static SampleConfigs sampleConfigs;

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityLifecycle.init(this);
        initSampleConfigs();
    }

    private void initSampleConfigs() {
        try {
            sampleConfigs = ConfigUtils.getSampleConfigs(Consts.SAMPLE_CONFIG_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SampleConfigs getSampleConfigs() {
        return sampleConfigs;
    }
}
