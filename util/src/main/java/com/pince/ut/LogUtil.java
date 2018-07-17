/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pince.ut;

import android.renderscript.RenderScript;
import android.util.Log;

import com.pince.ut.constans.AbAppData;

/**
 * Log统一管理类
 *
 * @author athou
 */
public class LogUtil {
    private static final String TAG = "Frame";

    // 下面四个是默认tag的函数
    public static void i(String msg) {
        if (AbAppData.DEBUG)
            Log.i(tag(), checkNullLog(msg));
    }

    public static void d(String msg) {
        if (AbAppData.DEBUG)
            Log.d(tag(), checkNullLog(msg));
    }

    public static void e(String msg) {
        if (AbAppData.DEBUG)
            Log.e(tag(), checkNullLog(msg));
    }

    public static void v(String msg) {
        if (AbAppData.DEBUG)
            Log.v(tag(), checkNullLog(msg));
    }

    public static void w(String msg) {
        if (AbAppData.DEBUG)
            Log.w(tag(), checkNullLog(msg));
    }

    // 下面是传入自定义tag的函数
    public static void i(String tag, String msg) {
        if (AbAppData.DEBUG)
            Log.i(tag, checkNullLog(msg));
    }

    public static void d(String tag, String msg) {
        if (AbAppData.DEBUG)
            Log.d(tag, checkNullLog(msg));
    }

    public static void e(String tag, String msg) {
        if (AbAppData.DEBUG)
            Log.e(tag, checkNullLog(msg));
    }

    public static void v(String tag, String msg) {
        if (AbAppData.DEBUG)
            Log.v(tag, checkNullLog(msg));
    }

    public static void w(String tag, String msg) {
        if (AbAppData.DEBUG)
            Log.w(tag, checkNullLog(msg));
    }

    private static String tag() {
        StackTraceElement stackTraceElement = new Exception().getStackTrace()[2];
        String str = stackTraceElement.getClassName();
        return TAG + " [" + str.substring(1 + str.lastIndexOf(".")) + " " + stackTraceElement.getMethodName()
                + ":" + stackTraceElement.getLineNumber() + "]";
    }

    private static String checkNullLog(String logMsg) {  //对log进行判空  防止NP
        return logMsg == null ? "" : logMsg;
    }
}
