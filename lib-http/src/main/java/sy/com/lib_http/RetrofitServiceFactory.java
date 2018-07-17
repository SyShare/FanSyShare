package sy.com.lib_http;

import java.util.HashMap;
import java.util.Map;

public class RetrofitServiceFactory {

    private static Map<Class, Object> services = new HashMap<>();

    public static <T> T getService(Class<T> tClass) {
        T t = (T) services.get(tClass);
        if (t != null) {
            return t;
        } else {
            t = RetrofitWrapper.getRetrofit().create(tClass);
            services.put(tClass, t);
            return t;
        }
    }

}
