package com.pince.ut;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.pince.ut.LogUtil;

/**
 * Created by sy-caizhaowei on 2017/9/21.
 */

public class ScreenUtil {

    /**
     * 是否为横屏状态
     */
    public static boolean isHorScreen(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @TargetApi(14)
    public static boolean hasNavBar(@NonNull Activity activity) {
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        boolean hasNavbar = (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
        LogUtil.i("hasNavbar:" + hasNavbar);
        return hasNavbar;
    }

    /**
     * 获取NavigationBar的高度，分为横竖屏不同状态
     */
    public static int getNavigationBarHeight(Context context, boolean isHorScreen) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (hasNavBar((Activity) context)) {
                String key;
                if (isHorScreen) {
                    key = "navigation_bar_height_landscape";
                } else {
                    key = "navigation_bar_height";
                }
                return getInternalDimensionSize(context.getResources(), key);
            }
        }
        return result;
    }

    public static void showNavigation(Activity activity) {
        if (hasNavBar(activity)) {
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    public static void hideNavigation(Activity activity) {
        if (hasNavBar(activity)) {
            int uiOptions = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
            } else {
                uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }
            activity.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        }
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context c) {
        return getInternalDimensionSize(c.getResources(), "status_bar_height");
    }

    /**
     * 获取安卓系统内部资源定义的dimens
     */
    public static int getInternalDimensionSize(Resources res, String key) {
        int result = 0;
        int resourceId = res.getIdentifier(key, "dimen", "android");
        if (resourceId > 0) {
            result = res.getDimensionPixelSize(resourceId);
        }
        return result;
    }


}