package com.pince.frame;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

/**
 *
 * @author cai
 * @date 2017/7/10
 */

public abstract class BaseFragment<B extends ViewDataBinding> extends BaseMvpFragment<BaseMvpPresenter, B> {

    @Override
    protected void initData(Bundle argments) {
    }
}
