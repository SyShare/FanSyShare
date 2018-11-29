package com.pince.frame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.pince.core.IActivityHandler;
import com.pince.frame.helper.ViewFinder;
import com.pince.ut.LogUtil;
import com.youlu.skinloader.base.SkinBaseFragment;

/**
 * @author athou
 * @date 2016/9/25
 */

public abstract class FinalFragment extends SkinBaseFragment implements IActivityHandler {

    /*根布局*/
    protected View rootView = null;
    private LayoutInflater mInflater;

    /**
     * 合并外部数据和内部参数
     */
    public void setArgumentsData(Bundle data) {
        Bundle bundle = getArguments();
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putAll(data);
        setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutId = requestLayoutId();
        if (layoutId <= 0) {
            LogUtil.e("the fragment layoutid is illegal");
            return null;
        }
        rootView = createContainerView(inflater, container, layoutId);
        if (rootView == null) {
            LogUtil.e("the fragment view inflater error");
            return null;
        }
        this.mInflater = inflater;
        initData(getArguments());
        onFragmentCreateStart();
        initView(rootView);
        setViewData(savedInstanceState);
        onFragmentCreateEnd();
        return rootView;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 设置fragment的layout
     */
    protected abstract int requestLayoutId();

    /**
     * 创建containerview
     *
     * @param inflater
     * @param container
     * @param layoutResID 你自己的布局id
     * @return
     */
    protected View createContainerView(LayoutInflater inflater, ViewGroup container, @LayoutRes int layoutResID) {
        return inflater.inflate(layoutResID, null);
    }

    protected abstract void initData(Bundle argments);

    protected void onFragmentCreateStart() {
    }

    /**
     * 初始化view
     *
     * @param rootView
     */
    protected abstract void initView(View rootView);

    /**
     * 初始化view相关联的数据
     */
    protected abstract void setViewData(Bundle savedInstanceState);

    protected void onFragmentCreateEnd() {
    }

    /**
     * 查找view
     *
     * @param id view的ID
     * @return view
     * @author 菜菜
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(int id) {
        return ViewFinder.findViewById(rootView, id);
    }

    /**
     * 通过父View查找子View
     *
     * @param v  父View
     * @param id 子View的ID
     * @return 子View
     * @author 菜菜
     */
    @SuppressWarnings("hiding")
    protected <T extends View> T findView(View v, int id) {
        return ViewFinder.findViewById(v, id);
    }

    /**
     * 返回键处理
     *
     * @return true：表示此fragment消化了返回键 false：表示没有消化返回键，交给activity处理
     */
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public Window getWindow() {
        return getActivity().getWindow();
    }

    @Override
    public Intent getIntent() {
        return getActivity().getIntent();
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public LayoutInflater getLayInflater() {
        return mInflater;
    }

    @Override
    public android.app.FragmentManager getFM() {
        throw new RuntimeException("android.support.v4.app.Fragment donot have android.app.FragmentManager");
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFM() {
        return getFragmentManager();
    }

    @Override
    public boolean isDestroyed() {
        return isDetached();
    }
}
