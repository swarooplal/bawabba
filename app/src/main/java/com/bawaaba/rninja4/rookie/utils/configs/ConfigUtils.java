package com.bawaaba.rninja4.rookie.utils.configs;

import com.bawaaba.rninja4.rookie.models.SampleConfigs;
import com.google.gson.Gson;
import com.quickblox.sample.core.utils.configs.ConfigParser;
import com.quickblox.sample.core.utils.configs.CoreConfigUtils;

import java.io.IOException;

/**
 * Created by rninja4 on 12/11/17.
 */

public class ConfigUtils  extends CoreConfigUtils {

    public static SampleConfigs getSampleConfigs(String fileName) throws IOException {
        ConfigParser configParser = new ConfigParser();
        Gson gson = new Gson();
        return gson.fromJson(configParser.getConfigsAsJsonString(fileName), SampleConfigs.class);
    }
}
