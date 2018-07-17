package com.pince.ut;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Debug;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import com.pince.ut.constans.AbAppData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;

/**
 * The Class AppUtil.
 *
 * @author cai
 */
public class AppUtil {

    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 获取屏幕尺寸与密度.
     *
     * @return mDisplayMetrics
     */
    public static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    /**
     * for handle exception "Bitmap too large to be uploaded into a texture".
     *
     * @param w the pic's width
     * @param h the pic's height
     */
    public static boolean isNeedCloseHardwareAcceleration(int w, int h) {
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        if (maxSize[0] < h || maxSize[0] < w) {
            return true;
        }
        return false;
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取程序 图标
     */
    public static int getAppIconId(Context context) {
        return context.getApplicationInfo().icon;
    }

    /**
     * 获取程序的名字
     */
    public static String getAppName(Context context) {
        return context.getString(context.getApplicationInfo().labelRes);
    }

    /**
     * 获取程序入口activity
     */
    public static Class getEnterActivityClass(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return Class.forName(packageInfo.activities[0].name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 是否为debug模式
     *
     * @param context
     * @author cai
     */
    public static boolean isDebug(Context context) {
        boolean isDebug = false;
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            int flags = info.applicationInfo.flags;
            if ((flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE) {
                isDebug = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            LogUtil.e(e.getMessage());
        } catch (Throwable var6) {
            return false;
        }
        return isDebug;
    }

    /**
     * 是否为锁屏界面
     */
    public static boolean isInKeyguardRestricted(Context context) {
        KeyguardManager mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if (mKeyguardManager.inKeyguardRestrictedInputMode()) {
            return true;
        }
        return false;
    }

    /**
     * Returns true if this is a low-RAM device. Exactly whether a device is low-RAM is ultimately
     * up to the device configuration, but currently it generally means something
     * in the class of a 512MB device with about a 800x480 or less screen.
     * This is mostly intended to be used by apps to determine whether they
     * should turn off certain features that require more RAM.
     *
     * @return
     */
    public static boolean isLowRamDevice(Context context) {
        if (Build.VERSION.SDK_INT >= 19) {
            return ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).isLowRamDevice();
        } else {
            return false;
        }
    }

    /**
     * 跳转到应用详情界面
     *
     * @param context
     * @author 菜菜
     */
    public static void showInstalledAppDetails(Context context) {
        PackageInfo info = null;
        String packageName = context.getPackageName();
        try {
            info = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (info != null) {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到应用管理列表界面
     *
     * @param context
     * @author 菜菜
     */
    public static void showInstalledAppManage(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 描述：打开并安装文件.
     *
     * @param context the context
     * @param file    apk文件路径
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 描述：卸载程序.
     *
     * @param context     the context
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    /**
     * 判断activity正在运行
     *
     * @param mContext
     * @return
     */
    public static boolean isActivityRunning(Context mContext, Class activityClass) {
        return isActivityRunning(mContext, activityClass.getName());
    }

    /**
     * 判断activity正在运行(此方法是判断activity是否处于栈顶来判断运行情况的)
     *
     * @param mContext
     * @return
     */
    public static boolean isActivityRunning(Context mContext, String activityClassName) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> info = activityManager.getRunningTasks(1);
        if (info != null && info.size() > 0) {
            ComponentName component = info.get(0).topActivity;
            if (activityClassName.equals(component.getClassName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 用来判断服务是否运行.
     *
     * @param ctx       the ctx
     * @param className 判断的服务名字 "com.xxx.xx..XXXService"
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context ctx, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(Integer.MAX_VALUE);
        Iterator<ActivityManager.RunningServiceInfo> l = servicesList.iterator();
        while (l.hasNext()) {
            ActivityManager.RunningServiceInfo si = (ActivityManager.RunningServiceInfo) l.next();
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    /**
     * 停止服务.
     *
     * @param ctx       the ctx
     * @param className the class name
     * @return true, if successful
     */
    public static boolean stopRunningService(Context ctx, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(ctx, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = ctx.stopService(intent_service);
        }
        return ret;
    }

    /**
     * 在应用市场打开当前应用
     */
    public static boolean goMarket(Context context) {
        String mAddress = "market://details?id=" + context.getPackageName();
        Intent marketIntent = new Intent("android.intent.action.VIEW");
        marketIntent.setData(Uri.parse(mAddress));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(marketIntent);
            return true;
        } else {
            LogUtil.e("Action 'market://details?id=' not available.");
            ToastUtil.show(context, "无法启动应用市场");
        }
        return false;
    }

    /**
     * 在应用市场打开指定应用
     */
    public static void goMarket(Context context, String appId) {
        String mAddress = "market://details?id=" + appId;
        Intent marketIntent = new Intent("android.intent.action.VIEW");
        marketIntent.setData(Uri.parse(mAddress));
        marketIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(marketIntent);
        } else {
            LogUtil.e("Action 'market://details?id=' not available.");
            ToastUtil.show(context, "无法启动应用市场");
        }
    }

    /***
     * 适配乐视手机, 跳转到应用权限管理
     *
     * @param ctx
     */
    public static void startPermissionAppPermission(Context ctx) {
        boolean hasLetvsafe = false;
        try {
            ApplicationInfo info = ctx.getPackageManager().getApplicationInfo("com.letv.android.letvsafe", PackageManager.GET_UNINSTALLED_PACKAGES);
            hasLetvsafe = info != null;
        } catch (PackageManager.NameNotFoundException e) {
            hasLetvsafe = false;
        }
        if (hasLetvsafe) {
            String ACTION_PERMISSION_AUTOBOOT = "com.letv.android.permissionandapps";
            Intent intent = new Intent(ACTION_PERMISSION_AUTOBOOT);
            // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
            // Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    /**
     * 获取cpu内核个数
     *
     * @return The number of cores, or 1 if failed to get result
     */
    public static int getNumCores() {
        try {
            // Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            // Filter to only list the devices we care about
            File[] files = dir.listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    // Check if filename is "cpu", followed by a single digit
                    // number
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }

            });
            // Return the number of cores (virtual CPU devices)
            return files.length;
        } catch (Exception e) {
            // Default to return 1 core
            return 1;
        }
    }

    /**
     * 获取系统总内存
     *
     * @return 总内存大单位为B。
     */
    public static long getTotalMemorySize() {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            return Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) * 1024L;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 通过pid获取应用占用的内存
     */
    public static int getPidMemorySize(int pid, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        int[] myMempid = new int[]{pid};
        Debug.MemoryInfo[] memoryInfo = am.getProcessMemoryInfo(myMempid);
        memoryInfo[0].getTotalSharedDirty();
        int memSize = memoryInfo[0].getTotalPss();
        return memSize;
    }

    public static float getProcessCpuRate() {
        float totalCpuTime1 = getTotalCpuTime();
        float processCpuTime1 = getAppCpuTime();
        try {
            Thread.sleep(360);
        } catch (Exception e) {
        }

        float totalCpuTime2 = getTotalCpuTime();
        float processCpuTime2 = getAppCpuTime();

        float cpuRate = 100 * (processCpuTime2 - processCpuTime1)
                / (totalCpuTime2 - totalCpuTime1);

        return cpuRate;
    }

    /**
     * 获取系统总CPU使用时间
     *
     * @return
     */
    public static long getTotalCpuTime() {
        String[] cpuInfos = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long totalCpu = Long.parseLong(cpuInfos[2])
                + Long.parseLong(cpuInfos[3]) + Long.parseLong(cpuInfos[4])
                + Long.parseLong(cpuInfos[6]) + Long.parseLong(cpuInfos[5])
                + Long.parseLong(cpuInfos[7]) + Long.parseLong(cpuInfos[8]);
        return totalCpu;
    }

    /**
     * 获取应用占用的CPU时间
     *
     * @return
     */
    public static long getAppCpuTime() {
        String[] cpuInfos = null;
        try {
            int pid = android.os.Process.myPid();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream("/proc/" + pid + "/stat")), 1000);
            String load = reader.readLine();
            reader.close();
            cpuInfos = load.split(" ");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        long appCpuTime = Long.parseLong(cpuInfos[13])
                + Long.parseLong(cpuInfos[14]) + Long.parseLong(cpuInfos[15])
                + Long.parseLong(cpuInfos[16]);
        return appCpuTime;
    }

    /**
     * Gps是否打开 需要<uses-permission
     * android:name="android.permission.ACCESS_FINE_LOCATION" />权限
     *
     * @param context the context
     * @return true, if is gps enabled
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * 描述：记录当前时间毫秒
     *
     * @throws
     */
    public static void logStartTime() {
        Calendar current = Calendar.getInstance();
        AbAppData.startLogTimeInMillis = current.getTimeInMillis();
    }

    /**
     * 描述：打印这次的执行时间毫秒，需要首先调用logStartTime()
     *
     * @param tag 标记
     * @param msg 描述
     */
    public static void logEndTime(String tag, String msg) {
        Calendar current = Calendar.getInstance();
        long endLogTimeInMillis = current.getTimeInMillis();
        LogUtil.d(tag, msg + ":" + (endLogTimeInMillis - AbAppData.startLogTimeInMillis) + "ms");
    }

    /**
     * 判断是否为全屏
     *
     * @param activity
     * @return
     */
    public static boolean isFullScreen(Activity activity) {
        int flags = activity.getWindow().getAttributes().flags;
        if ((flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 全屏切换
     */
    public static void fullScreenChange(Activity activity) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(activity);
        boolean fullScreen = mPreferences.getBoolean("fullScreen", false);
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if (fullScreen) {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            // 取消全屏设置
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mPreferences.edit().putBoolean("fullScreen", false).commit();
        } else {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            mPreferences.edit().putBoolean("fullScreen", true).commit();
        }
    }

    /**
     * 全屏切换
     */
    public static void fullScreenChange(Activity activity, boolean fullScreen, boolean inScreen) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        if (fullScreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(attrs);
//            activity.getWindow().addFlags(inScreen ?
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                    : WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(attrs);
            // 取消全屏设置
//            activity.getWindow().clearFlags(inScreen ?
//                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
//                    : WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 拨打电话<br>
     * 需要权限：android.permission.CALL_PHONE
     *
     * @param context
     * @param phone
     * @author 菜菜
     */
    public static void callPhone(Activity context, String phone) {
        if (StrUtil.isEmpty(phone)) {
            ToastUtil.show(context, "您要拨打的号码不能为空");
            return;
        }
        try {
            //三星手机，禁止拨号权限后，无反应（版本低于23时）
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转网络设置界面
     *
     * @param context
     * @author 菜菜
     */
    public static void startNetSetting(Context context) {
        try {
            Intent wirelessSettingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
            context.startActivity(wirelessSettingsIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开PDF文件
     *
     * @param context
     * @param pdfFile
     */
    public static void openPdf(Context context, File pdfFile) {
        if (pdfFile == null || !pdfFile.exists()) {
            ToastUtil.show(context, "此文件不存在！");
            return;
        }
        Intent it = new Intent();
        it.setAction(Intent.ACTION_VIEW);
        it.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        List<ResolveInfo> resInfo = context.getPackageManager().queryIntentActivities(it, 0);
        if (!resInfo.isEmpty()) {
            List<Intent> targetedIntents = new ArrayList<Intent>();
            for (int n = 0; n < resInfo.size(); n++) {
                ResolveInfo info = resInfo.get(n);
                String pkg = info.activityInfo.packageName.toLowerCase();
                //特殊处理,过滤qq等应用不可用选项，com.tencent.mm /mobileqq
                if (!pkg.contains("com.tencent")) {
                    Intent chit = new Intent();
                    chit.setPackage(pkg);
                    chit.setAction(Intent.ACTION_VIEW);
                    chit.setDataAndType(Uri.fromFile(pdfFile), "application/pdf");
                    targetedIntents.add(chit);
                }
            }
            if (targetedIntents.isEmpty()) {
                ToastUtil.show(context, "打开pdf失败，请安装pdf查看器后在进行查看！");

                goMarket(context, "pdf");
                return;
            }
            Intent chooserIntent = Intent.createChooser(targetedIntents.get(0), "选择浏览方式");
            if (chooserIntent == null) {
                ToastUtil.show(context, "打开pdf失败");
                return;
            }
            // A Parcelable[] of Intent or LabeledIntent objects as set with
            // putExtra(String, Parcelable[]) of additional activities to place
            // a the front of the list of choices, when shown to the user with a
            // ACTION_CHOOSER.
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntents.toArray(new Parcelable[]{}));
            chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            try {
                context.startActivity(chooserIntent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                LogUtil.e("open pdf choose not found");
                ToastUtil.show(context, "打开pdf失败，请安装pdf查看器后在进行查看！");
                goMarket(context, "pdf");
            }
        } else {
            ToastUtil.show(context, "打开pdf失败，请安装pdf查看器后在进行查看！");
            goMarket(context, "pdf");
            return;
        }
    }

    /**
     * 打开web浏览器<br>
     * 调用系统所有公开的浏览器
     *
     * @param context
     * @param url
     * @author 菜菜
     */
    public static void openBrowser(Context context, String url) {
        if (StrUtil.isEmpty(url)) {
            ToastUtil.show(context, "无效地址");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(context, "网页打开失败！");
        }
    }

    /**
     * 打开系统浏览器
     *
     * @param context
     * @param url
     * @author 菜菜
     */
    public static void openBrowserSystem(Context context, String url) {
        if (StrUtil.isEmpty(url)) {
            ToastUtil.show(context, "无效地址");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(context, "网页打开失败！");
        }
    }

    /**
     * 调用指定的浏览器
     *
     * @param context
     * @param url
     * @param packageName 指定浏览器的报名packagename
     * @param className   指定浏览器的主启动activity
     * @author 菜菜
     */
    public static void openBrowserOther(Context context, String url, String packageName, String className) {
        if (StrUtil.isEmpty(url)) {
            ToastUtil.show(context, "无效地址");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.setClassName(packageName, className);
            context.startActivity(intent);
        } catch (Exception e) {
            ToastUtil.show(context, "网页打开失败！");
        }
    }

    /**
     * 保存图片，并通知系统相册更新
     */
    public static void insertImageToSystem(Context context, String imagePath, String imageName) {
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, imageName, "");

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(imagePath));
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存图片，并通知系统相册更新
     */
    public static void insertImageToSystem(Context context, File file) {
        if (file == null) {
            return;
        }
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver()
                    , file.getAbsolutePath(), file.getName(), "");

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(file.getPath()));
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存视频缩略图，并通知系统相册更新
     *
     * @param context
     * @param videoPath
     */
    public static void insertVideoToSystem(Context context, String videoPath) {
        try {
            Bitmap bmp = VideoUtil.createVideoThumbnail(videoPath);
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bmp, "", "");

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File("/sdcard/image.jpg"));
            intent.setData(uri);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用系统下载
     *
     * @param context
     * @param url
     * @return
     */
    public static long LoadFile(Context context, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri resource = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(resource);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton(); // 获取文件类型实例
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url)); // 获取文件类型
        request.setMimeType(mimeString); // 制定下载文件类型
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        // 是否发出通知，既后台下载，如果要使用这一句必须声明一个权限：
        // android.permission.DOWNLOAD_WITHOUT_NOTIFICATION
        request.setShowRunningNotification(true);
        // 是否显示下载界面
        request.setVisibleInDownloadsUi(true);

        int index = url.lastIndexOf("/");
        String fname = url.substring(index + 1); // 获取文件名

        request.setDestinationInExternalPublicDir("/download/", fname); // 制定下载的目录里
        return downloadManager.enqueue(request);
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     *
     * @param context
     * @return
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);

        int contentHeight = getScreenHeight();

        return totalHeight - contentHeight;
    }
}