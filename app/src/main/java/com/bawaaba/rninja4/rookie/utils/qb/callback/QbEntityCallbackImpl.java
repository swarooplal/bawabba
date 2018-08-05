package com.bawaaba.rninja4.rookie.utils.qb.callback;

import android.os.Bundle;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;

/**
 * Created by rninja4 on 12/11/17.
 */

public class QbEntityCallbackImpl <T> implements QBEntityCallback<T> {
    public QbEntityCallbackImpl() {
    }

    @Override
    public void onSuccess(T result, Bundle bundle) {

    }

    @Override
    public void onError(QBResponseException e) {

    }
}
