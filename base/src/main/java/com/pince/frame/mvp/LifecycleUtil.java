package com.pince.frame.mvp;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.pince.ut.LogUtil;
import com.pince.ut.constans.AbAppData;

/**
 * Created on 2018/4/26.
 *
 * @author ice
 */
public class LifecycleUtil {

    public static Lifecycle getLifecycle(Context context) {
        if (context instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) context;
            return fragmentActivity.getLifecycle();
        }

        if (AbAppData.DEBUG) {
            throw new RuntimeException("lifecycle is null, check your Activity or Fragment");
        } else {
            LogUtil.e("lifecycle is null, check your Activity or Fragment");
            return null;
        }
    }

    public static Lifecycle getLifecycle(IBaseView view) {
        if (view instanceof FragmentActivity) {
            FragmentActivity fragmentActivity = (FragmentActivity) view;
            return fragmentActivity.getLifecycle();
        } else if (view instanceof Fragment) {
            Fragment fragment = (Fragment) view;
            return fragment.getLifecycle();
        } else if (view instanceof View) {
            View lView = (View) view;
            return getLifecycle(lView.getContext());
        }

        if (AbAppData.DEBUG) {
            throw new RuntimeException("lifecycle is null, check your Activity or Fragment");
        } else {
            LogUtil.e("lifecycle is null, check your Activity or Fragment");
            return null;
        }
    }

}
