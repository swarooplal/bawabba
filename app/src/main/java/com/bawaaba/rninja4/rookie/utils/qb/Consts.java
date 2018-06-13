package com.bawaaba.rninja4.rookie.utils.qb;

import com.bawaaba.rninja4.rookie.R;
import com.quickblox.sample.core.utils.ResourceUtils;

/**
 * Created by rninja4 on 12/11/17.
 */

public interface Consts {

    String SAMPLE_CONFIG_FILE_NAME = "sample_config.json";

    int PREFERRED_IMAGE_SIZE_PREVIEW = ResourceUtils.getDimen(R.dimen.chat_attachment_preview_size);
    int PREFERRED_IMAGE_SIZE_FULL = ResourceUtils.dpToPx(320);
    String QB_USER_PASSWORD = "qb_user_password";
}
