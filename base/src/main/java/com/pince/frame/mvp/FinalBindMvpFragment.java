package com.pince.frame.mvp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author athou
 * @date 2017/7/10
 */

public abstract class FinalBindMvpFragment<P extends FinalMvpPresenter, B extends ViewDataBinding> extends FinalMvpFragment<P> {

    protected B mBinding;

    @Override
    protected View createContainerView(LayoutInflater inflater, ViewGroup container, int layoutResID) {
        View view = super.createContainerView(inflater, container, layoutResID);
        createBinding(view);
        return view;
    }

    protected B createBinding(View view) {
        if (view != null) {
            Object viewTag = view.getTag();
            if (viewTag != null && viewTag instanceof String) {
                mBinding = DataBindingUtil.bind(view);
            }
        }
        return mBinding;
    }

    @Override
    protected void initView(View rootView) {

    }
}
