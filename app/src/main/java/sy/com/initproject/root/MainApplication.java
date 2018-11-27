package sy.com.initproject.root;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.bumptech.glide.Glide;
import com.pince.ut.constans.FileConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sy.com.initproject.BuildConfig;
import sy.com.initproject.R;
import sy.com.lib_http.RetrofitManager;

public class MainApplication extends Application {


    public static List<String> bannerList ;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.setContext(this);
        initNetwork();
        FileConstants.initFileConfig(this);

        String[] strings = getResources().getStringArray(R.array.BannerList);
        bannerList = new ArrayList<>(Arrays.asList(strings));
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
                .retrofitBuilder("https://www.apiopen.top")
                .build();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(AppContext.getContext()).clearDiskCache();
        Glide.get(AppContext.getContext()).clearMemory();
    }
}
