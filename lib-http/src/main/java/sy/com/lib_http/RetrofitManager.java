package sy.com.lib_http;

import android.content.Context;

import retrofit2.Converter;

public class RetrofitManager {

    public static Converter responseConverter;

    public static OkHttpWrapper.Builder okHttpBuilder(Context context) {
        return OkHttpWrapper.builder(context);
    }

    public static <T> T getService(Class<T> tClass) {
       return RetrofitServiceFactory.getService(tClass);
    }

    public static void replaceResponseConverter(Converter converter) {
        responseConverter = converter;
    }

}
