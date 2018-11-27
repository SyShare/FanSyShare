package com.youlu.skinloader.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.youlu.skinloader.attr.concrete.DynamicAttr;
import com.youlu.skinloader.listener.IDynamicNewView;
import com.youlu.skinloader.listener.ISkinUpdate;
import com.youlu.skinloader.load.SkinInflaterFactory;
import com.youlu.skinloader.load.SkinManager;
import com.youlu.skinloader.statusbar.StatusBarBackground;

import java.util.List;

/**
 * Created by _SOLID
 * Date:2016/4/14
 * Time:10:24
 * <p>
 * 需要实现换肤功能的Activity就需要继承于这个Activity
 */
public abstract class SkinBaseActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    /**
     * 当前Activity是否需要响应皮肤更改需求
     */
    private boolean isResponseOnSkinChanging = true;
    /**
     * Factory模式工厂
     */
    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        beforeCreate();
        super.onCreate(savedInstanceState);
        changeStatusColor();

    }

    private void beforeCreate(){
        if(!enableResponseOnSkin()){
            return;
        }
        mSkinInflaterFactory = new SkinInflaterFactory();
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), mSkinInflaterFactory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!enableResponseOnSkin()){
            return;
        }
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(!enableResponseOnSkin()){
            return;
        }
        SkinManager.getInstance().detach(this);
        mSkinInflaterFactory.clean();
    }

    @Override
    public void onThemeUpdate() {
        Log.i("SkinBaseActivity", "onThemeUpdate");
        if(!enableResponseOnSkin()){
            return;
        }
        mSkinInflaterFactory.applySkin();
        changeStatusColor();

//        //设置window的背景色
//        Drawable drawable = new ColorDrawable(SkinManager.getInstance().getColorPrimaryDark());
//        getWindow().setBackgroundDrawable(drawable);
    }

    public void changeStatusColor() {
        if(!enableResponseOnSkin()){
            return;
        }
        //如果当前的Android系统版本大于4.4则更改状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("SkinBaseActivity", "changeStatus");
            int color = SkinManager.getInstance().getColorPrimaryDark();
            StatusBarBackground statusBarBackground = new StatusBarBackground(
                    this, color);
            if (color != -1)
                statusBarBackground.setStatusBarbackColor();
        }
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
        }
    }

    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
        }
    }

    protected void dynamicAddSkinEnableView(View view, List<DynamicAttr> pDAttrs) {
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
        }
    }

    abstract protected boolean enableResponseOnSkin();
}
