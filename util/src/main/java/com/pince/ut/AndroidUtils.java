package com.pince.ut;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class AndroidUtils {
    public static final String QUANMIN_PACKAGENAME = "com.maimiao.live.tv";
    private static final String TAG = "AndroidUtils";
    private final static long ONE_G = 1024 * 1024 * 1024;
    private static final String QUANMIN_NEXT_VERSIION = "3.4";
    private static boolean sIsMainProcessGot = false;
    private static boolean sIsMainProcess = false;
    private static long lastClickTime;

    /**
     * Determine is this the main process.
     *
     * @param ctx The context.
     * @return true is the main process, otherwise, false.
     */
    public static boolean isMainProcess(Context ctx) {
        if (sIsMainProcessGot) {
            return sIsMainProcess;
        }
        final String packageName = ctx.getApplicationContext().getPackageName();
        String processName = getProcessName(android.os.Process.myPid(), ctx);
        if (TextUtils.isEmpty(processName)) {
            processName = getProcessName(ctx);
        }
        if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(processName)) {
            sIsMainProcessGot = true;
        }
        sIsMainProcess = packageName.equals(processName);
        return sIsMainProcess;
    }

    /**
     * Get the process name of a specified process id.
     *
     * @param pid The specified process id.
     * @return The process name of the specified process id.
     */
    public static String getProcessName(int pid, Context ctx) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String name = reader.readLine();
            if (!TextUtils.isEmpty(name)) {
                name = name.trim();
            }
            return name;
        } catch (Throwable ignored) {
            ignored.printStackTrace(System.err);
            return getProcessName(ctx);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ignored) {
                ignored.printStackTrace(System.err);
            }
        }
    }

    private static String getProcessName(Context context) {
        ActivityManager am = getActivityManager(context);
        if (null == am) {
            return "";
        }

        final long processSleepDuration = 50;
        final int processRetryLimit = 90; // avoid ANR, less then 5 seconds

        int processRetryCount = 0;
        String processName = null;

        while (true) {
            final List<ActivityManager.RunningAppProcessInfo> processes =
                    am.getRunningAppProcesses();
            if (null != processes) {
                for (ActivityManager.RunningAppProcessInfo info : processes) {
                    if (android.os.Process.myPid() == info.pid) {
                        processName = info.processName;
                        break;
                    }
                }
            }

            if (TextUtils.isEmpty(processName)) { // Take a rest and retry it.
                if (++processRetryCount > processRetryLimit) {
                    return "";
                } else {
                    try {
                        Thread.sleep(processSleepDuration);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            } else {
                // Got it.
                return processName;
            }
        }
    }

    private static ActivityManager getActivityManager(Context ctx) {
        final long amSleepDuration = 25;
        final int amRetryAmLimit = 180; // avoid ANR, less then 5 seconds
        int amRetryCount = 0;

        ActivityManager am;
        while (true) {
            am = ((ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE));
            if (null == am) { // Take a rest and retry it.
                if (++amRetryCount > amRetryAmLimit) {
                    return null;
                } else {
                    try {
                        Thread.sleep(amSleepDuration);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace(System.err);
                    }
                }
            } else {
                return am;
            }
        }
    }


    // 获取android当前可用内存大小
    private static long getAvailMemory(Context ctx) {
        ActivityManager am = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    //1g 以上不压缩图片
    public static boolean isMemoryAvail(Context ctx) {
        long memory = getAvailMemory(ctx);
        return memory > ONE_G;
    }

    public synchronized static boolean checkQuickClick() {
        long curTime = System.currentTimeMillis();
        long timeD = curTime - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = curTime;
        return false;
    }

    /**
     * 判断app是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        return packageInfo != null;
    }


    /**
     * 版本号比较
     * 0代表相等，1代表version1大于version2，-1代表version1小于version2
     *
     * @param versionLocal  获取的本地版本号
     * @param versionServer 线上版本号
     * @return 状态值
     */
    public static int compareVersion(@NonNull String versionLocal, @NonNull String versionServer) {
        if (versionLocal.equals(versionServer)) {
            return 0;
        }
        String[] version1Array = versionLocal.split("\\.");
        String[] version2Array = versionServer.split("\\.");
        LogUtil.d(TAG, "version1Array==" + version1Array.length);
        LogUtil.d(TAG, "version2Array==" + version2Array.length);
        int index = 0;
        // 获取最小长度值
        int minLen = Math.min(version1Array.length, version2Array.length);
        int diff = 0;
        // 循环判断每位的大小
        while (index < minLen
                && (diff = Integer.parseInt(version1Array[index])
                - Integer.parseInt(version2Array[index])) == 0) {
            index++;
        }
        if (diff == 0) {
            // 如果位数不一致，比较多余位数
            for (int i = index; i < version1Array.length; i++) {
                if (Integer.parseInt(version1Array[i]) > 0) {
                    return 1;
                }
            }
            for (int i = index; i < version2Array.length; i++) {
                if (Integer.parseInt(version2Array[i]) > 0) {
                    return -1;
                }
            }
            return 0;
        } else {
            return diff > 0 ? 1 : -1;
        }
    }

    /**
     * 是否为老版本手印授权登陆
     *
     * @param context
     * @return
     */
    public static boolean isVersionLow(Context context) {
        boolean isLow;
        try {
            String versionLocal = getVersion(context);
            isLow = !TextUtils.isEmpty(versionLocal) &&
                    isAppInstalled(context, QUANMIN_PACKAGENAME) &&
                    compareVersion(versionLocal, QUANMIN_NEXT_VERSIION) < 0;
        } catch (Exception e) {
            isLow = false;
        }
        return isLow;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    private static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(QUANMIN_PACKAGENAME, 0);
            return info.versionName;
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 检测设备是否有陀螺仪属性
     *
     * @return
     */
    public static boolean checkDeviceGyroscope(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm != null && pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE);
    }

    /*设置宽高*/
    public static void setLayoutParams(View rootView, int height) {
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = height * (int) (AppUtil.getDisplayMetrics().density / 3);
        rootView.setLayoutParams(params);
    }

    /*设置间距*/
    public static void setMarginLayoutParams(View rootView, int left, int top, int right, int bottom) {
        left = ViewUtil.dip2px(left);
        top = ViewUtil.dip2px(top);
        right = ViewUtil.dip2px(right);
        bottom = ViewUtil.dip2px(bottom);
        ViewGroup.LayoutParams params = rootView.getLayoutParams();
        ViewGroup.MarginLayoutParams marginParams = null;
        if (params instanceof ViewGroup.MarginLayoutParams) {
            marginParams = (ViewGroup.MarginLayoutParams) params;
        } else {
            marginParams = new ViewGroup.MarginLayoutParams(params);
        }
        marginParams.setMargins(left, top, right, bottom);
        rootView.setLayoutParams(marginParams);
    }

    /**
     * 用来解决InputMethodManager 内存泄露问题
     * {@link # https://blog.csdn.net/qq402164452/article/details/54378688}
     *
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f = null;
        Object objGet = null;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                } // author: sodino mail:sodino@qq.com
                objGet = f.get(imm);
                if (objGet != null && objGet instanceof View) {
                    View vGet = (View) objGet;
                    //// 被InputMethodManager持有引用的context是想要目标销毁的
                    if (vGet.getContext() == destContext) {
                        // 置空，破坏掉path to gc节点
                        f.set(imm, null);
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        LogUtil.d("fixInputMethodManagerLeak break, context is not suitable, get_context=" + vGet.getContext() + " dest_context=" + destContext);
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
