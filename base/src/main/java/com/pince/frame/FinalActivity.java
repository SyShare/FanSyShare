package com.pince.frame;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pince.core.IActivityHandler;
import com.pince.ut.LogUtil;
import com.pince.ut.SoftInputUtil;

/**
 * 作者：CaiCai on 2016/6/17 11:44
 *
 * @author athoucai
 */
public abstract class FinalActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener
        , IActivityHandler {

    /**
     * 全局的LayoutInflater对象，已经完成初始化.
     */
    public LayoutInflater mInflater;
    /**
     * 根布局
     */
    protected View mRootView;
    private FrameLayout mToolbarContainer;
    protected Toolbar mToolbar;
    private TextView centerTitle;
    protected FrameLayout mContainer;
    protected View mContentView;

    protected Handler baseHandler = new Handler(Looper.getMainLooper());

    protected void showCheckDataToast() {
        Toast.makeText(FinalActivity.this, "初始化数据出错", Toast.LENGTH_LONG).show();
    }

    protected void changeTheme() {

    }

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        changeTheme();
        super.onCreate(savedInstanceState);
        LogUtil.i("onCreate ==>>" + getClass().getSimpleName());
        //初始化数据失败的话 直接finish
        if (!checkData(savedInstanceState)) {
            showCheckDataToast();
            finish();
            return;
        }
        int layoutId = requestLayoutId();
        if (layoutId <= 0) {
            LogUtil.e("the activity layoutid is illegal");
            finish();
            return;
        }
        mInflater = getLayInflater();
        mContentView = createContainerView(mInflater, layoutId);
        if (mContentView == null) {
            LogUtil.e("the activity view inflater error");
            finish();
            return;
        }
        onActivityCreateStart();

        setBaseContentView(R.layout.frame_base_container);
        mRootView = findViewById(R.id.frame_base_root);
        mToolbarContainer = findViewById(R.id.frame_base_container_toolbar);
        if (isToolBarEnable()) {
            mToolbarContainer.setVisibility(View.VISIBLE);
            mToolbarContainer.addView(mToolbar = inflateToolbar());
        } else {
            mToolbarContainer.setVisibility(View.GONE);
        }
        mContainer = findViewById(R.id.frame_base_container);

        setupToolbar();

        // 设置ContentView
        mContainer.addView(mContentView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        initView(mContentView);
        setViewData(savedInstanceState);

        onActivityCreateEnd();
    }

    /**
     * 创建containerview
     *
     * @param inflater
     * @param layoutResID 你自己的布局id
     * @return
     */
    protected View createContainerView(LayoutInflater inflater, @LayoutRes int layoutResID) {
        return inflater.inflate(layoutResID, null);
    }

    /**
     * 设置contentview<br>
     * 供子类进行重写
     *
     * @param layoutResID 根布局id
     */
    protected void setBaseContentView(@LayoutRes int layoutResID) {
        setContentView(layoutResID);
    }

    /**
     * 初始化数据，若失败，则finish
     *
     * @param savedInstanceState
     * @return
     */
    protected abstract boolean checkData(Bundle savedInstanceState);

    /**
     * 开始创建activity界面
     */
    protected void onActivityCreateStart() {
    }

    /**
     * 设置contentview的layout
     */
    protected abstract
    @LayoutRes
    int requestLayoutId();

    private Toolbar inflateToolbar() {
        Toolbar toolbar = null;
        if (isTitleCenter()) {
            toolbar = (Toolbar) mInflater.inflate(R.layout.frame_base_toolbar_center, null);
        } else {
            toolbar = (Toolbar) mInflater.inflate(R.layout.frame_base_toolbar_normal, null);
        }
        return toolbar;
    }

    /**
     * 是否展示toolbar
     *
     * @return
     */
    protected boolean isToolBarEnable() {
        return true;
    }

    /**
     * 设置是否toolbar标题居中
     *
     * @return
     */
    protected boolean isTitleCenter() {
        return false;
    }

    /**
     * 初始化view
     */
    protected abstract void initView(View contentView);

    /**
     * 初始化view相关联的数据
     */
    protected abstract void setViewData(Bundle savedInstanceState);

    /**
     * 结束activity创建
     */
    protected void onActivityCreateEnd() {
    }

    protected final View getRootView() {
        return mRootView;
    }

    protected final FrameLayout getContainer() {
        return mContainer;
    }

    protected final View getContentView() {
        return mContentView;
    }

    protected final Toolbar getToolbar() {
        return mToolbar;
    }

    /* replace the inner toolbar, if the new toolbar is same as inner's , it will do nothing*/
    protected final void setToolbar(Toolbar toolbar) {
        if (mToolbar != toolbar) {
            if (mToolbar != null) {
                mToolbar.setVisibility(View.GONE);
            }
            this.mToolbar = toolbar;
            setupToolbar();
        }
    }

    /**
     * setup  toolbar
     */
    private void setupToolbar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            //显示返回键
            getSupportActionBar().setDisplayHomeAsUpEnabled(homeAsUpEnable());
            //可响应返回键点击事件
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            //返回键
            getSupportActionBar().setHomeAsUpIndicator(requestNavigationIcon());

            Drawable toolbarBg = requestToolBarBackground();
            if (toolbarBg != null) {
                mToolbar.setBackground(toolbarBg);
            }
            mToolbar.setOnTouchListener(toolbarTouch);
            mToolbar.setOnMenuItemClickListener(this);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickAvalible()) {
                        onBackPressed();
                    }
                }
            });
            if (isTitleCenter()) {
                getSupportActionBar().setDisplayShowTitleEnabled(false);
            }
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (isTitleCenter()) {
            if (mToolbar != null) {
                if (centerTitle == null) {
                    centerTitle = mToolbar.findViewById(R.id.toolbar_title_tv);
                }
                centerTitle.setText(title);
            }
        } else {
            if (mToolbar != null) {
                mToolbar.setTitle(title);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void setStatusBarColor(@ColorRes int colorRes) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(colorRes));
        }
    }

    protected void setBackground(@DrawableRes int resId) {
        getWindow().getDecorView().setBackgroundResource(resId);
    }

    /**
     * 是否显示返回键。默认显示
     *
     * @return
     */
    protected boolean homeAsUpEnable() {
        return true;
    }

    /**
     * 设置toolbar 背景样式
     */
    protected abstract Drawable requestToolBarBackground();

    /**
     * 返回键
     *
     * @return
     */
    protected abstract
    @DrawableRes
    int requestNavigationIcon();

    /* 监听toolbar的touch事件，隐藏键盘 */
    private View.OnTouchListener toolbarTouch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideSoftInputView();
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuId = requestMenuId();
        if (-1 != menuId) {
            getMenuInflater().inflate(menuId, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public int requestMenuId() {
        return -1;
    }

    /* 监听toolbar的点击事件 */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            preDestroy();
        }
    }

    /**
     * 用户主动销毁界面的回调，可以在这里做一些资源的释放动作
     */
    protected abstract void preDestroy();

    @Override
    protected void onDestroy() {
        hideSoftInputView();
        if (baseHandler != null) {
            baseHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();

        LogUtil.i("onDestroy ==>>" + getClass().getSimpleName());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.i("onSaveInstanceState ==>>" + getClass().getSimpleName());
        //onStateNotSaved();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtil.i("onRestoreInstanceState ==>>" + getClass().getSimpleName());
    }

    @Override
    public Activity getActivityContext() {
        return this;
    }

    @Override
    public LayoutInflater getLayInflater() {
        return LayoutInflater.from(this);
    }

    @Override
    public boolean isDestroyed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return super.isDestroyed();
        } else {
            if (getSupportFragmentManager().isDestroyed()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public android.app.FragmentManager getFM() {
        return getFragmentManager();
    }

    @Override
    public FragmentManager getSupportFM() {
        return getSupportFragmentManager();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                hideSoftInputView();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftInputView() {
        SoftInputUtil.hideSoftInputView(this);
    }

    // 双击事件记录最近一次点击的时间
    private long lastClickTime = 0;

    /**
     * 按钮点击去重实现方法一 ，需要在onclick回调内调用
     */
    protected boolean isClickAvalible() {
        return isClickAvalible(500);
    }

    /**
     * 按钮点击去重实现方法一 ，需要在onclick回调内调用
     *
     * @param miniIntervalMills 最小间隔。
     */
    protected boolean isClickAvalible(long miniIntervalMills) {
        if (System.currentTimeMillis() - lastClickTime > miniIntervalMills) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}