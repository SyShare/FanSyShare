package com.pince.core.callback;

/**
 * Created by sy-caizhaowei on 2017/8/9.
 */

public abstract class IDataCallback {

    public void onStart() {
    }

    public abstract void onSuccess(Object... objects);

    public void onError(int code, Throwable e) {
    }
}
