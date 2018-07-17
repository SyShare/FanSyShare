package com.pince.frame;

import android.databinding.ViewDataBinding;

import com.pince.frame.mvp.FinalBindMvpFragment;

/**
 * @author cai
 * @date 2017/7/10
 */

public abstract class BaseMvpFragment<P extends BaseMvpPresenter, B extends ViewDataBinding> extends FinalBindMvpFragment<P, B> {

}
