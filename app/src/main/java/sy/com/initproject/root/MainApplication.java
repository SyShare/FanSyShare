package sy.com.initproject.root;

import android.app.Application;

import sy.com.initproject.BuildConfig;
import sy.com.lib_http.RetrofitManager;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.setContext(this);
        initNetwork();
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
}
