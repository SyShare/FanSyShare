package com.pince.frame;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.pince.ut.LogUtil;
import com.youlu.skinloader.base.SkinBaseApplication;

/**
 * Author by Administrator , Date on 2018/12/7.
 * PS: Not easy to write code, please indicate.
 */
public abstract class AbstractBaseApplication extends SkinBaseApplication {

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

    @Override
    public void onCreate() {
        super.onCreate();

        initARouter();
    }

    public void initARouter() {
        if (isDebug(this)) {
            ARouter.openLog();
            ARouter.openDebug();
        }
        ARouter.init(this);
    }
}
