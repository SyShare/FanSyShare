package com.pince.frame.mvp;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.pince.core.IActivityHandler;
import com.pince.frame.FinalActivity;
import com.pince.ut.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by athou on 2017/7/10.
 */

public abstract class FinalMvpActivity<P extends FinalMvpPresenter> extends FinalActivity implements IBaseView {
    protected P presenter;

    @Override
    protected boolean checkData(Bundle bundle) {
        presenter = createPresenter();
        if (presenter == null) {
            return true;
        }
        boolean pCheck = false;
        if (bundle != null) {
            pCheck = presenter.initData(getIntent().putExtras(bundle));
        } else {
            pCheck = presenter.initData(getIntent());
        }
        return pCheck;
    }

    @Override
    @CallSuper
    protected void onActivityCreateStart() {
        super.onActivityCreateStart();
        if (presenter != null) {
            presenter.attach(this);
        }
    }

    protected P createPresenter() {
        if (null == presenter) {
            Class cls = MvpTypeUtil.findParamsTypeClass(getClass());
            LogUtil.i("createPresenter", "cls:" + cls);
            if (cls != null) {
                try {
                    Constructor<P> constructor = cls.getConstructor();
                    presenter = constructor.newInstance();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return presenter;
    }

    public void resetPresenter(P presenter) {
        resetPresenter(presenter, false);
    }

    public void resetPresenter(P presenter, boolean attach) {
        this.presenter = presenter;
        if (attach && presenter != null) {
            this.presenter.attach(this);
        }
    }

    public P getPresenter() {
        return presenter;
    }

    @Override
    public IActivityHandler getActivityHandler() {
        return this;
    }

    @Override
    @CallSuper
    protected void onDestroy() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroy();
    }
}
