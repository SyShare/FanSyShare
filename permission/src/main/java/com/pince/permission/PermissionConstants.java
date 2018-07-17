package com.pince.permission;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class PermissionConstants {
    private static String appName = "";
    private static Context context;

    public static void init(@NonNull Application application) {
        context = application.getApplicationContext();
        appName = getAppName();
    }

    private static String getAppName() {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    static String getTipsStr(int resId) {
        return String.format(context.getString(resId), appName);
    }
}
