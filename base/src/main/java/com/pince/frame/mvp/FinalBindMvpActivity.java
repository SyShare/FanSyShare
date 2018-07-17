package com.pince.frame.mvp;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

/**
 * @author athou
 * @date 2017/7/10
 */

public abstract class FinalBindMvpActivity<P extends FinalMvpPresenter, B extends ViewDataBinding> extends FinalMvpActivity<P> {
    protected B mBinding;

    @Override
    protected View createContainerView(LayoutInflater inflater, int layoutResID) {
        View view = super.createContainerView(inflater, layoutResID);
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
    protected void initView(View contentView) {

    }
}
