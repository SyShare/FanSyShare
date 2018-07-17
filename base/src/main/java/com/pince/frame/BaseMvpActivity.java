package com.pince.frame;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleRegistry;
import android.databinding.ViewDataBinding;
import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.pince.frame.mvp.FinalBindMvpActivity;
import com.pince.permission.PermissionHelper;

/**
 * @author cai
 * @date 2017/7/10
 */

public abstract class BaseMvpActivity<P extends BaseMvpPresenter, B extends ViewDataBinding> extends FinalBindMvpActivity<P, B> {

    protected LifecycleRegistry mLifecycleRegistry = (LifecycleRegistry) getLifecycle();

    @Override
    protected void initView(View contentView) {
        invokeTheme();
        setTitle(getTitle());
        handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
    }

    @Override
    protected boolean isTitleCenter() {
        return true;
    }

    @Override
    protected Drawable requestToolBarBackground() {
        return ContextCompat.getDrawable(this, R.drawable.app_theme_drawable);
    }

    @Override
    protected int requestNavigationIcon() {
        return R.drawable.icon_back_gray;
    }



    @Override
    protected void onStart() {
        super.onStart();
        handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    public <T extends View> T findView(@IdRes int id) {
        return findViewById(id);
    }

    public <T extends View> T findView(View view, @IdRes int id) {
        return view.findViewById(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
    }

    @Override
    protected void onPause() {
        handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        // 在onStop()方法中提交统计的信息。
        super.onPause();
    }

    @Override
    protected void onStop() {
        handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        super.onStop();
    }

    @Override
    protected void preDestroy() {

    }

    @Override
    protected void onDestroy() {
        handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }

    protected PermissionHelper permissionHelper;

    public PermissionHelper getPermissionHelper() {
        if (permissionHelper == null) {
            permissionHelper = new PermissionHelper(this);
        }
        return permissionHelper;
    }

    /**
     * 设置主题背景
     */
    protected void invokeTheme() {
    }

    public void handleLifecycleEvent(@NonNull Lifecycle.Event event) {
        mLifecycleRegistry.handleLifecycleEvent(event);
    }
}
