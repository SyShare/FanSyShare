/*
 * Copyright (c) 2017  athou(cai353974361@163.com).
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pince.frame.mvp;

import android.arch.lifecycle.Lifecycle;
import android.content.Intent;

import com.pince.core.IActivityHandler;
import com.pince.ut.LogUtil;

/**
 * @author athou
 * @date 2017/4/27
 */

public abstract class FinalMvpPresenter<V extends IBaseView> implements IBasePresenter<V> {

    protected V view;

    public FinalMvpPresenter() {
    }

    public abstract boolean initData(Intent intent);

    @Override
    public IActivityHandler getActivityHandler() {
        if (isFinish()) {
            LogUtil.e("you must call attach first or ui has finish");
            return new EmptyActivityHandlerImpl();
        }
        return view.getActivityHandler();
    }

    protected Lifecycle getLifecycle() {
        return LifecycleUtil.getLifecycle(view);
    }

    public boolean isFinish() {
        return view == null;
    }

    @Override
    public void attach(V v) {
        view = v;
    }

    @Override
    public void detach() {
        view = null;
    }
}
