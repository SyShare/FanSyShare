package com.pince.ut;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 *
 * @author cai
 * @date 16/5/8
 */
public class JsonUtil {

    private static  Gson GSON_INSTANCE;

    public synchronized static Gson getGson() {
        if (GSON_INSTANCE == null) {
            GSON_INSTANCE = new Gson();
        }
        return GSON_INSTANCE;
    }

    public static String toJson(Object src) {
        return getGson().toJson(src);
    }

    public static <T> T fromJson(String src, Class<T> classOfT) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        T t = null;
        try {
            t = getGson().fromJson(src, classOfT);
        } catch (JsonParseException e) {
            LogUtil.w("JsonParseException", e.getMessage());
        }
        return t;
    }

    public static String toJson(List<?> src) {
        return getGson().toJson(src);
    }

    public static <T> T fromJson(String src, TypeToken typeToken) {
        if (TextUtils.isEmpty(src)) {
            return null;
        }
        return getGson().fromJson(src, typeToken.getType());
    }
}
