package com.pince.ut.toast;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Author by Administrator , Date on 2018/10/17.
 * PS: Not easy to write code, please indicate.
 */

public class NormalToastImpl implements ToastFactory.ToastCreator {

    @Override
    public Toast show(Context context, CharSequence msgStr, int duration, int gravity, int xOffset, int yOffset) {
        Toast toast = Toast.makeText(context, msgStr, duration);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
        return toast;
    }

    @Override
    public Toast showView(Context context, View view, int duration, int gravity, int xOffset, int yOffset) {
        Toast viewToast = Toast.makeText(context, "", duration);
        viewToast.setView(view);
        viewToast.setGravity(gravity, xOffset, yOffset);
        viewToast.setDuration(duration);
        viewToast.show();
        return viewToast;
    }

    @Override
    public Toast showIcon(Context context, String msg, Drawable icon, int gravity, int duration) {
        Toast iconToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        iconToast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) iconToast.getView();
        TextView tv = (TextView) toastView.getChildAt(0);
        tv.setText(msg);
        if ((gravity & Gravity.LEFT) != 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
        } else if ((gravity & Gravity.TOP) != 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
        } else if ((gravity & Gravity.RIGHT) != 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
        } else if ((gravity & Gravity.BOTTOM) != 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, icon);
        }
        tv.setGravity(Gravity.CENTER);
        iconToast.setDuration(duration);
        iconToast.show();
        return iconToast;
    }
}