package sy.com.lib_http;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.pince.ut.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.HttpException;
import sy.com.lib_http.util.ApiCodeUtil;

public class OkHttpWrapper {

    /**
     * 上下文
     */
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private Builder mBuilder;

    OkHttpWrapper(Builder builder) {
        mBuilder = builder;
    }

    public static Builder builder(@NonNull Context context) {
        return new Builder(context);
    }

    public static Context getContext() {
        return context;
    }

    /**
     * 对错误信息处理后的提示
     *
     * @param throwable 错误类型
     */
    public static void toastError(Context context, Throwable throwable) {
        if (context == null) {
            return;
        }
        if (throwable != null) {
            if (throwable instanceof NetworkUnAvailableException) {
                ToastUtil.show(context, R.string.http_toast_net_unavailable);
                return;
            }
            if (throwable instanceof ConnectException || throwable instanceof UnknownHostException) {
                ToastUtil.show(context, R.string.http_toast_service_connect_fail);
                return;
            }
            if (throwable instanceof SocketTimeoutException) {
                ToastUtil.show(context, R.string.http_toast_response_time_out);
                return;
            }
            if (throwable instanceof HttpException || throwable instanceof JsonParseException) {
                ToastUtil.show(context, R.string.http_toast_service_error);
                return;
            }
            if (throwable instanceof ApiException && !TextUtils.isEmpty(throwable.getMessage())) {
                ToastUtil.show(context, throwable.getMessage());
            }
        }
    }

    public OkHttpClient getClient() {
        return mBuilder.client;
    }

    public static class Builder {
        private OkHttpClient client;
        private OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        /**
         * 日志拦截器
         */
        private HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        /**
         * 拦截错误集合
         */
        private List<ApiErrorHandler> errorHandlers;
        /**
         * 链接超时时间
         */
        private long connectTimeOut = 4 * 1000L;
        /**
         * 读写超时时间
         */
        private long readTimeout = 4 * 1000L;

        Builder(Context context) {
            OkHttpWrapper.context = context;
            errorHandlers = new ArrayList<>();
            clientBuilder.addInterceptor(logInterceptor)
                    .connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS)
                    .readTimeout(readTimeout, TimeUnit.MILLISECONDS);

            RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
                @Override
                public void accept(Throwable throwable) {
                    if (errorHandlers != null && errorHandlers.size() > 0) {
                        for (ApiErrorHandler errorHandler : errorHandlers) {
                            errorHandler.handleError(throwable);
                        }
                    }
                }
            });
        }

        public Builder setLogEnable(boolean logEnable) {
            logInterceptor.setLevel(logEnable ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            clientBuilder.addNetworkInterceptor(interceptor);
            return this;
        }

        public Builder addNormalInterceptor(Interceptor interceptor) {
            clientBuilder.addInterceptor(interceptor);
            return this;
        }

        public Builder setConnectTimeOut(long connectTimeOut) {
            clientBuilder.connectTimeout(connectTimeOut, TimeUnit.MILLISECONDS);
            return this;
        }

        public Builder setReadTimeOut(long readTimeOut) {
            clientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
            return this;
        }

        public Builder cacheSize(Cache cache) {
            clientBuilder.cache(cache);
            return this;
        }

        public Builder setSuccessCode(int code) {
            ApiCodeUtil.INSTANCE.setSUCCESS_CODE(code);
            return this;
        }

        public Builder enableCache(boolean openCache) {
            ApiCodeUtil.INSTANCE.setEnableCache(openCache);
            return this;
        }

        /**
         * 添加通用Api错误处理
         *
         * @param errorHandler 错误回调
         */
        public Builder addErrorHandler(ApiErrorHandler errorHandler) {
            this.errorHandlers.add(errorHandler);
            return this;
        }

        public OkHttpWrapper build() {
            client = clientBuilder.build();
            return new OkHttpWrapper(this);
        }

        public RetrofitWrapper.Builder retrofitBuilder(String baseUrl) {
            return RetrofitWrapper.builder(baseUrl, build());
        }

        public interface ApiErrorHandler {

            /**
             * 处理错误回调实现
             *
             * @param throwable 异常
             */
            void handleError(Throwable throwable);
        }
    }

}
