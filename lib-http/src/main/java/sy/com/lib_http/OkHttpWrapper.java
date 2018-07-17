package sy.com.lib_http;

import android.content.Context;

import com.pince.network.interceptor.AddCookiesInterceptor;
import com.pince.network.interceptor.HeaderInterceptor;
import com.pince.network.interceptor.ReceivedCookiesInterceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpWrapper {

    private Builder mBuilder;

    public OkHttpWrapper(Builder builder) {
        mBuilder = builder;
    }

    public OkHttpClient getClient() {
        return mBuilder.client;
    }

    public static Builder builder(Context context){
        return new Builder(context);
    }

    public static class Builder {
        private HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        private HeaderInterceptor headerInterceptor = new HeaderInterceptor();

        private OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        private OkHttpClient client;

        Builder(Context context) {
            clientBuilder.addInterceptor(logInterceptor)
                    .addNetworkInterceptor(headerInterceptor)
                    .addInterceptor(new ReceivedCookiesInterceptor(context))
                    .addInterceptor(new AddCookiesInterceptor(context))
                    .connectTimeout(10, TimeUnit.SECONDS);
        }

        public Builder setLogEnable(boolean logEnable) {
            logInterceptor.setLevel(logEnable ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            return this;
        }

        public Builder setHeaderParams(Map<String, String> headerParams) {
            headerInterceptor.setMHeadParams(headerParams);
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            clientBuilder.addInterceptor(interceptor);
            return this;
        }

        public OkHttpWrapper build() {
            client = clientBuilder.build();
            return new OkHttpWrapper(this);
        }

        public RetrofitWrapper.Builder retrofitBuilder(String baseUrl) {
            return RetrofitWrapper.builder(baseUrl, build());
        }
    }

}
