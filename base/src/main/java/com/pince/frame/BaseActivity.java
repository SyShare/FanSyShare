package com.pince.frame;

import android.databinding.ViewDataBinding;
import android.os.Bundle;

/**
 * Created by admin on 2017/7/10.
 */

public abstract class BaseActivity<B extends ViewDataBinding> extends BaseMvpActivity<BaseMvpPresenter, B> {

    @Override
    protected boolean checkData(Bundle bundle) {
        return false;
    }
}
