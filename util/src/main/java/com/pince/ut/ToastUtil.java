package com.pince.ut;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.pince.ut.toast.ToastFactory;

/**
 * @Description:
 * @Data：2018/7/17
 * @author: SyShare
 */
public class ToastUtil {

    private ToastUtil() {
        // Disabled.
    }

    /**
     * 默认显示的Toast
     */
    public static Toast show(@NonNull Context context, CharSequence msgStr) {
        return show(context, msgStr, Toast.LENGTH_SHORT);
    }

    /**
     * 默认显示的Toast
     */
    public static Toast show(@NonNull Context context, @StringRes int msgRes) {
        return show(context, msgRes, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     */
    public static Toast showLong(@NonNull Context context, CharSequence message) {
        return show(context, message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     */
    public static Toast showLong(@NonNull Context context, @StringRes int msgRes) {
        return show(context, msgRes, Toast.LENGTH_LONG);
    }

    /**
     * 长时间显示err Toast
     */
    public static Toast showError(@NonNull Context context, String msgStr) {
        return show(context, msgStr, Toast.LENGTH_LONG);
    }

    public static Toast show(Context context, @StringRes int msgRes, int duration) {
        return show(context, context.getString(msgRes), duration);
    }

    public static Toast show(Context context, CharSequence msgStr, int duration) {
        return show(context, msgStr, duration, Gravity.CENTER, 0, 0);
    }

    public static Toast show(Context context, CharSequence msgStr, int duration, int gravity, int xOffset, int yOffset) {
        return ToastFactory.getToastCreator().show(context, msgStr, duration, gravity, xOffset, yOffset);
    }


    //==================================================================================

    /**
     * 自定义显示Toast view
     */
    public static Toast showView(Context context, View view) {
        return showView(context, view, Toast.LENGTH_LONG, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 自定义显示Toast view
     */
    public static Toast showView(Context context, View view, int duration, int gravity, int xOffset, int yOffset) {
        return ToastFactory.getToastCreator().showView(context, view, duration, gravity, xOffset, yOffset);
    }

    //==================================================================================

    /**
     * 自定义显示Toast icon
     */
    public static Toast showIcon(Context context, @DrawableRes int iconRes) {
        return showIcon(context, null, iconRes);
    }

    public static Toast showIcon(Context context, Drawable icon) {
        return showIcon(context, null, icon);
    }

    /**
     * 自定义显示Toast icon
     */
    public static Toast showIcon(Context context, String msg, @DrawableRes int iconRes) {
        return showIcon(context, msg, ContextCompat.getDrawable(context, iconRes));
    }

    /**
     * 自定义显示Toast icon
     */
    public static Toast showIcon(Context context, String msg, Drawable icon) {
        return showIcon(context, msg, icon, Gravity.TOP, Toast.LENGTH_LONG);
    }

    public static Toast showIcon(Context context, String msg, Drawable icon, int gravity, int duration) {
        return ToastFactory.getToastCreator().showIcon(context, msg, icon, gravity, duration);
    }

}
