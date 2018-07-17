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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 设备信息工具类
 *
 * @author cai
 */
@SuppressLint("MissingPermission")
public class DeviceUtil {
    public static final String DeviceId = "DeviceId";
    public static final String DeviceSoftwareVersion = "DeviceSoftwareVersion";
    public static final String Line1Number = "Line1Number";
    public static final String NetworkCountryIso = "NetworkCountryIso";
    public static final String NetworkOperator = "NetworkOperator";
    public static final String NetworkOperatorName = "NetworkOperatorName";
    public static final String NetworkType = "NetworkType";
    public static final String PhoneType = "PhoneType";
    public static final String SimCountryIso = "SimCountryIso";
    public static final String SimOperator = "SimOperator";
    public static final String SimOperatorName = "SimOperatorName";
    public static final String SimSerialNumber = "SimSerialNumber";
    public static final String SimState = "SimState";
    // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
    public static final String SubscriberId = "SubscriberId";
    public static final String VoiceMailNumber = "VoiceMailNumber";

    public static final int MinSDCardSpace = 100; //最低内存剩余空间，100M

    /**
     * 判断当前手机是否有ROOT权限
     *
     * @return
     */
    public boolean isRoot() {
        boolean bool = false;

        try {
            if ((!new File("/system/bin/su").exists())
                    && (!new File("/system/xbin/su").exists())) {
                bool = false;
            } else {
                bool = true;
            }
        } catch (Exception e) {

        }
        return bool;
    }

    /**
     * 判断SDCard剩余内存是否够，（是否大于100M）
     *
     * @return
     */
    public static boolean isSDCardSpaceEnough() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = 0;
        if (Build.VERSION.SDK_INT >= 18) {
            sdFreeMB = ((double) stat.getAvailableBlocksLong() * (double) stat.getBlockSizeLong()) / (1024 * 1024);
        } else {
            sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / (1024 * 1024);
        }
        if (sdFreeMB >= MinSDCardSpace) {
            return true;
        }
        return false;
    }

    public static int getWindowWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getWindowHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void getWindowSize(Activity activity, int[] size) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        size[0] = dm.widthPixels;
        size[1] = dm.heightPixels;
    }

    public static Map<String, Object> readDevice(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(DeviceId, tm.getDeviceId());
        map.put(DeviceSoftwareVersion, tm.getDeviceSoftwareVersion());
        map.put(Line1Number, tm.getLine1Number());
        map.put(NetworkCountryIso, tm.getNetworkCountryIso());
        map.put(NetworkOperator, tm.getNetworkOperator());
        map.put(NetworkOperatorName, tm.getNetworkOperatorName());
        map.put(NetworkType, tm.getNetworkType());
        map.put(PhoneType, tm.getPhoneType());
        map.put(SimCountryIso, tm.getSimCountryIso());
        map.put(SimOperator, tm.getSimOperator());
        map.put(SimOperatorName, tm.getSimOperatorName());
        map.put(SimSerialNumber, tm.getSimSerialNumber());
        map.put(SimState, tm.getSimState());
        map.put(SubscriberId, tm.getSubscriberId());
        map.put(VoiceMailNumber, tm.getVoiceMailNumber());
        return map;
    }

    public static String readSIMCard(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);// 取得相关系统服务
        StringBuffer sb = new StringBuffer();
        switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT:
                sb.append("无卡");
                break;
            case TelephonyManager.SIM_STATE_UNKNOWN:
                sb.append("未知状态");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                sb.append("需要NetworkPIN解锁");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                sb.append("需要PIN解锁");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                sb.append("需要PUK解锁");
                break;
            case TelephonyManager.SIM_STATE_READY:
                sb.append("良好");
                break;
        }

        if (tm.getSimSerialNumber() != null) {
            sb.append("@" + tm.getSimSerialNumber().toString());
        } else {
            sb.append("@无法取得SIM卡号");
        }

        if (tm.getSimOperator().equals("")) {
            sb.append("@无法取得供货商代码");
        } else {
            sb.append("@" + tm.getSimOperator().toString());
        }

        if (tm.getSimOperatorName().equals("")) {
            sb.append("@无法取得供货商");
        } else {
            sb.append("@" + tm.getSimOperatorName().toString());
        }

        if (tm.getSimCountryIso().equals("")) {
            sb.append("@无法取得国籍");
        } else {
            sb.append("@" + tm.getSimCountryIso().toString());
        }

        if (tm.getNetworkOperator().equals("")) {
            sb.append("@无法取得网络运营商");
        } else {
            sb.append("@" + tm.getNetworkOperator());
        }
        if (tm.getNetworkOperatorName().equals("")) {
            sb.append("@无法取得网络运营商名称");
        } else {
            sb.append("@" + tm.getNetworkOperatorName());
        }
        if (tm.getNetworkType() == 0) {
            sb.append("@无法取得网络类型");
        } else {
            sb.append("@" + tm.getNetworkType());
        }
        return sb.toString();
    }

    public static String getPhoneNo(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        switch (tm.getSimState()) { // getSimState()取得sim的状态 有下面6中状态
            case TelephonyManager.SIM_STATE_READY:
                return tm.getLine1Number();
            case TelephonyManager.SIM_STATE_ABSENT:
            case TelephonyManager.SIM_STATE_UNKNOWN:
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
            default:
                return null;
        }
    }

    /**
     * 获取手机信息
     *
     * @return
     */
    public static String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name + "=" + value);
                sb.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    @Deprecated
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 返回正确的UserAgent
     *
     * @return
     */
    public static String getUserAgent() {
        //Dalvik/2.1.0 (Linux; U; Android 6.0.1; vivo X9L Build/MMB29M)
        String userAgent = System.getProperty("http.agent");
        if (TextUtils.isEmpty(userAgent)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0, length = userAgent.length(); i < length; i++) {
            char c = userAgent.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", (int) c));
            } else {
                sb.append(c);
            }
        }
        LogUtil.v("User-Agent", "User-Agent: " + sb.toString());
        return sb.toString();
    }

    public static void fixHWInputMethodManagerLeak(Context destContext) {
        if (destContext != null) {
            InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                String lastSrvViewString = "mLastSrvView";
                Object lastSrvView = null;
                try {
                    Field field = imm.getClass().getDeclaredField(lastSrvViewString);
                    field.setAccessible(true);
                    lastSrvView = field.get(imm);
                    if (lastSrvView != null && lastSrvView instanceof View) {
                        View srvView = (View) lastSrvView;
                        if (srvView.getContext() == destContext) {
                            field.set(imm, null);
                            LogUtil.e("fixHWInputMethodManagerLeak success!");
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e("fixHWInputMethodManagerLeak fail, not find mLastSrvView!");
                }
            }
        }
    }
}
