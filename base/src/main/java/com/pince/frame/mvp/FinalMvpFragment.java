package com.pince.frame.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.pince.core.IActivityHandler;
import com.pince.frame.FinalFragment;
import com.pince.ut.LogUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by cai on 2017/7/10.
 */

public abstract class FinalMvpFragment<P extends FinalMvpPresenter> extends FinalFragment implements IBaseView {
    protected P presenter;

    @Override
    protected void initData(Bundle argments) {
        presenter = createPresenter();
        if (presenter != null) {
            if (argments != null) {
                presenter.initData(new Intent().putExtras(argments));
            } else {
                presenter.initData(new Intent());
            }
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
                } catch (Fragment.InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
        return presenter;
    }

    public P getPresenter() {
        return presenter;
    }

    public void resetPresenter(P presenter) {
        resetPresenter(presenter, true);
    }

    public void resetPresenter(P presenter, boolean attach) {
        this.presenter = presenter;
        if (attach && presenter != null) {
            this.presenter.attach(this);
        }
    }

    @Override
    public IActivityHandler getActivityHandler() {
        return this;
    }

    @Override
    public void onDestroy() {
        if (presenter != null) {
            presenter.detach();
        }
        super.onDestroy();
    }
}
