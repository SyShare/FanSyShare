package com.pince.ut;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * @Description:
 * @Data：2018/7/17
 * @author: SyShare
 */
class ToastUtil {
    public static void show(Context context, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
