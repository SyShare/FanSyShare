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

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.Window;

import com.pince.core.IActivityHandler;

/**
 * Created by athou on 2017/5/16.
 */

public class EmptyActivityHandlerImpl implements IActivityHandler {

    @Override
    public Window getWindow() {
        return null;
    }

    @Override
    public Intent getIntent() {
        return new Intent();
    }

    @Override
    public Activity getActivityContext() {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
    }

    @Override
    public LayoutInflater getLayInflater() {
        return null;
    }

    @Override
    public FragmentManager getFM() {
        return null;
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFM() {
        return null;
    }

    @Override
    public void startActivity(Intent intent) {

    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {

    }

    @Override
    public boolean isDestroyed() {
        return false;
    }
}
