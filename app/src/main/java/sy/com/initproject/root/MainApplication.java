package sy.com.initproject.root;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.pince.frame.AbstractBaseApplication;
import com.pince.frame.helper.ScreenManager;
import com.pince.ut.constans.FileConstants;
import com.youlu.http.interceptor.CacheNetworkInterceptor;
import sy.com.initproject.root.interceptor.LoggingInterceptor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Cache;
import sy.com.initproject.BuildConfig;
import sy.com.initproject.R;
import sy.com.initproject.root.interceptor.CacheInterceptor;
import sy.com.lib_http.ApiException;
import sy.com.lib_http.RetrofitManager;

public class MainApplication extends AbstractBaseApplication {


    public static List<String> bannerList;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().setContext(this);
        initNetwork();
        FileConstants.initFileConfig(this);
        ScreenManager.getInstance().init(this);
        String[] strings = getResources().getStringArray(R.array.BannerList);
        bannerList = new ArrayList<>(Arrays.asList(strings));
    }

    @Override
    public void initARouter() {
        super.initARouter();
    }

    private boolean isDebug() {
        return !BuildConfig.BUILD_TYPE.equals("release");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 初始化网络库
     */
    private void initNetwork() {
        RetrofitManager.okHttpBuilder(this)
                .setLogEnable(!BuildConfig.BUILD_TYPE.equals("release"))
                .setSuccessCode(200)
                .enableCache(true)
                .cacheSize(new Cache(new File(getExternalCacheDir(), "ok-cache"), 1024 * 1024 * 30L))
                .addNormalInterceptor(new CacheInterceptor())
                .addInterceptor(new CacheNetworkInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .addErrorHandler(throwable -> {
                    if (throwable instanceof ApiException) {
                        ApiException apiException = (ApiException) throwable;

                    }

                })
                .retrofitBuilder("https://www.apiopen.top")
                .build();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(AppContext.getInstance().getContext()).clearDiskCache();
        Glide.get(AppContext.getInstance().getContext()).clearMemory();
    }
}
