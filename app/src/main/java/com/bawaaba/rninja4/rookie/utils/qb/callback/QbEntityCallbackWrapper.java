package com.bawaaba.rninja4.rookie.utils.qb.callback;

import android.os.Bundle;

import com.quickblox.core.QBEntityCallback;

/**
 * Created by rninja4 on 12/11/17.
 */

public class QbEntityCallbackWrapper<T> extends QbEntityCallbackTwoTypeWrapper<T, T> {
    public QbEntityCallbackWrapper(QBEntityCallback<T> callback) {
        super(callback);
    }
    @Override
    public void onSuccess(T t, Bundle bundle) {
        onSuccessInMainThread(t, bundle);
    }
}
