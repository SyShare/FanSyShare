package com.youlu.skinloader.load;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.youlu.skinloader.config.SkinConfig;
import com.youlu.skinloader.listener.ILoaderListener;
import com.youlu.skinloader.listener.ISkinLoader;
import com.youlu.skinloader.listener.ISkinUpdate;
import com.youlu.skinloader.util.L;

import java.io.File;
import java.lang.ref.SoftReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by _SOLID
 * Date:2016/4/13
 * Time:21:07
 */
public class SkinManager implements ISkinLoader {

    /**
     * 当前上下文应用
     */
    private SoftReference<Context> softReference;
    /**
     * 观察者集合
     */
    private List<ISkinUpdate> mSkinObservers;
    private Resources mResources;
    /**
     * 当前的皮肤是否是默认的
     */
    private boolean isDefaultSkin = false;
    /**
     * 皮肤的包名
     */
    private String skinPackageName;
    /**
     * 皮肤路径
     */
    private String skinPath;

    private SkinManager() {
    }

    public static SkinManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    public void init(Context ctx) {
        softReference = new SoftReference<>(ctx.getApplicationContext());
    }

    public int getColorPrimaryDark() {
        if (mResources != null) {
            int identify = mResources.getIdentifier("colorPrimaryDark", "color", skinPackageName);
            return mResources.getColor(identify);
        }
        return -1;
    }

    /**
     * 判断当前使用的皮肤是否来自外部
     *
     * @return
     */
    public boolean isExternalSkin() {
        return !isDefaultSkin && mResources != null;
    }

    /**
     * 得到当前的皮肤路径
     *
     * @return
     */
    public String getSkinPath() {
        return skinPath;
    }

    /**
     * 得到当前皮肤的包名
     *
     * @return
     */
    public String getSkinPackageName() {
        return skinPackageName;
    }

    public Resources getResources() {
        return mResources;
    }

    /**
     * 恢复到默认主题
     */
    public void restoreDefaultTheme() {
        if (softReference.get() == null) {
            return;
        }
        Context context = softReference.get();
        SkinConfig.saveSkinPath(context, SkinConfig.DEFALT_SKIN);
        isDefaultSkin = true;
        mResources = context.getResources();
        skinPackageName = context.getPackageName();
        notifySkinUpdate();
    }

    @Override
    public void attach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            mSkinObservers = new ArrayList<>();
        }
        if (!mSkinObservers.contains(observer)) {
            mSkinObservers.add(observer);
        }
    }

    @Override
    public void detach(ISkinUpdate observer) {
        if (mSkinObservers == null) {
            return;
        }
        if (mSkinObservers.contains(observer)) {
            mSkinObservers.remove(observer);
        }
    }

    @Override
    public void notifySkinUpdate() {
        if (mSkinObservers == null) return;
        for (ISkinUpdate observer : mSkinObservers) {
            observer.onThemeUpdate();
        }
    }

    public void load() {
        if (softReference.get() == null) {
            return;
        }
        Context context = softReference.get();
        String skin = SkinConfig.getCustomSkinPath(context);
        load(skin, null);
    }

    public void load(ILoaderListener callback) {
        if (softReference.get() == null) {
            return;
        }
        Context context = softReference.get();
        String skin = SkinConfig.getCustomSkinPath(context);
        if (SkinConfig.isDefaultSkin(context)) {
            return;
        }
        load(skin, callback);
    }

    public void load(final String skinPkgPath, final ILoaderListener callback) {
        Observable.create(new ObservableOnSubscribe<Resources>() {
            @Override
            public void subscribe(ObservableEmitter<Resources> emitter) throws Exception {
                try {
                    if (!TextUtils.isEmpty(skinPkgPath)) {
                        Log.i("loadSkin", skinPkgPath);
                        File file = new File(skinPkgPath);
                        if (!file.exists()) {
                            emitter.onNext(null);
                            return;
                        }
                        if (softReference.get() == null) {
                            return;
                        }
                        Context context = softReference.get();
                        PackageManager mPm = context.getPackageManager();
                        PackageInfo mInfo = mPm.getPackageArchiveInfo(skinPkgPath, PackageManager.GET_ACTIVITIES);
                        skinPackageName = mInfo.packageName;

                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);


                        Resources superRes = context.getResources();
                        Resources skinResource = new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());

                        SkinConfig.saveSkinPath(context, skinPkgPath);

                        skinPath = skinPkgPath;
                        isDefaultSkin = false;
                        emitter.onNext(skinResource);
                        emitter.onComplete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Resources>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        if (callback != null) {
                            callback.onStart();
                        }
                    }

                    @Override
                    public void onNext(@Nullable Resources resources) {
                        mResources = resources;
                    }

                    @Override
                    public void onError(Throwable e) {
                        isDefaultSkin = true;
                        if (callback != null) {
                            callback.onFailed();
                        }
                        dispose(disposable);
                    }

                    @Override
                    public void onComplete() {
                        if (mResources != null) {
                            if (callback != null) {
                                callback.onSuccess();
                            }
                            notifySkinUpdate();
                        } else {
                            isDefaultSkin = true;
                            if (callback != null) {
                                callback.onFailed();
                            }
                        }
                        dispose(disposable);
                    }
                });
    }

    private void dispose(Disposable d){
        if (d != null
                && !d.isDisposed()) {
            d.dispose();
        }
    }

    public int getColor(int resId) {
        if (softReference.get() == null) {
            return 0;
        }
        Context context = softReference.get();
        int originColor = context.getResources().getColor(resId);
        if (mResources == null || isDefaultSkin) {
            return originColor;
        }

        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
        int trueColor = 0;

        try {
            trueColor = mResources.getColor(trueResId);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueColor = originColor;
        }

        return trueColor;
    }

    public Drawable getDrawable(int resId) {
        if (softReference.get() == null) {
            return null;
        }
        Context context = softReference.get();
        Drawable originDrawable = context.getResources().getDrawable(resId);
        if (mResources == null || isDefaultSkin) {
            return originDrawable;
        }
        String resName = context.getResources().getResourceEntryName(resId);

        int trueResId = mResources.getIdentifier(resName, "drawable", skinPackageName);

        Drawable trueDrawable = null;
        try {
            L.i("SkinManager getDrawable", "SDK_INT = " + android.os.Build.VERSION.SDK_INT);
            if (android.os.Build.VERSION.SDK_INT < 22) {
                trueDrawable = mResources.getDrawable(trueResId);
            } else {
                trueDrawable = mResources.getDrawable(trueResId, null);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            trueDrawable = originDrawable;
        }

        return trueDrawable;
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。</br>
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId
     * @return
     * @author pinotao
     */
    public ColorStateList convertToColorStateList(int resId) {
        boolean isExtendSkin = true;
        if (mResources == null || isDefaultSkin) {
            isExtendSkin = false;
        }
        if (softReference.get() == null) {
            return null;
        }
        Context context = softReference.get();

        String resName = context.getResources().getResourceEntryName(resId);
        if (isExtendSkin) {
            int trueResId = mResources.getIdentifier(resName, "color", skinPackageName);
            ColorStateList trueColorList = null;
            if (trueResId == 0) { // 如果皮肤包没有复写该资源，但是需要判断是否是ColorStateList
                try {
                    ColorStateList originColorList = context.getResources().getColorStateList(resId);
                    return originColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    L.e("resName = " + resName + " NotFoundException : " + e.getMessage());
                }
            } else {
                try {
                    trueColorList = mResources.getColorStateList(trueResId);
                    return trueColorList;
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                    L.e("resName = " + resName + " NotFoundException :" + e.getMessage());
                }
            }
        } else {
            try {
                ColorStateList originColorList = context.getResources().getColorStateList(resId);
                return originColorList;
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                L.e("resName = " + resName + " NotFoundException :" + e.getMessage());
            }

        }

        int[][] states = new int[1][1];
        return new ColorStateList(states, new int[]{context.getResources().getColor(resId)});
    }

    private static final class SingleHolder {
        private static final SkinManager INSTANCE = new SkinManager();
    }
}
