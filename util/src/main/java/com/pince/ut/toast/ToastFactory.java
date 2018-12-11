package com.pince.ut.toast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

/**
 * Author by Administrator , Date on 2018/10/17.
 * PS: Not easy to write code, please indicate.
 */
public final class ToastFactory {


    private static ToastCreator toastCreator;


    private ToastFactory() {

    }

    public static ToastCreator getToastCreator() {
        if (toastCreator == null) {
            toastCreator = new NormalToastImpl();
        }
        return toastCreator;
    }


    public static void setToastCreator(ToastCreator toastCreator) {
        ToastFactory.toastCreator = toastCreator;
    }

    public interface ToastCreator {


        /**
         * 弹出普通消息
         *
         * @param context
         * @param msgStr   消息文本
         * @param duration 时长
         * @param gravity  位置
         * @param xOffset
         * @param yOffset
         * @return
         */
        Toast show(Context context, CharSequence msgStr, int duration, int gravity, int xOffset, int yOffset);

        /**
         * 弹出自定义view
         *
         * @param context
         * @param view     需要展示的view
         * @param duration 时长
         * @param gravity  位置
         * @param xOffset
         * @param yOffset
         * @return
         */
        Toast showView(Context context, View view, int duration, int gravity, int xOffset, int yOffset);

        /**
         * 弹出带图标的文本消息
         *
         * @param context
         * @param msg     消息文本
         * @param icon    提示的图标
         * @param gravity 图标的位置（left, top, right, bottom）
         * @return
         */
        Toast showIcon(Context context, String msg, Drawable icon, int gravity, int duration);

    }


}
