/*
 * Copyright (c) 2016  athou（cai353974361@163.com）.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pince.ut;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SharefUtil {
    private SharedPreferences shref = null;
    private String shrefName = null;
    private String shrefDir = null;

    public SharefUtil(Context context, String shrefName) {
        super();
        this.shrefName = shrefName;
        this.shref = context.getSharedPreferences(shrefName, Context.MODE_PRIVATE);
        shrefDir = getShrefDir(context);
    }

    /**
     * 获取SharedPreferences文件的目录路径
     *
     * @param context
     * @return
     */
    private static String getShrefDir(Context context) {
        return File.separator + "data" + File.separator + "data" + File.separator + context.getPackageName()
                + File.separator + "shared_prefs";
    }

    public SharedPreferences getShref() {
        return shref;
    }

    public void setShref(SharedPreferences shref) {
        this.shref = shref;
    }

    /**
     * 是否第一次运行APP
     *
     * @return
     */
    public final boolean isFirstRun() {
        return readBoolean("firstrun");
    }

    /**
     * 设置第一次运行
     */
    public final void setFirstRun(boolean isFirstRun) {
        saveData("firstrun", isFirstRun);
    }

    public final void setLastAppVersionCode(int lastCode) {
        saveData("last_app_code", lastCode);
    }

    public final int getLastAppVersionCode() {
        return readInt("last_app_code", 0);
    }

    public static boolean isShrefFileExists(Context context, String shrefName) {
        File file = new File(getShrefDir(context), shrefName + ".xml");
        return file.exists();
    }

    /**
     * 获取配置文件上次修改时间
     *
     * @return
     */
    public final long lastModified() {
        File file = new File(shrefDir, shrefName + ".xml");
        if (file != null && file.exists()) {
            return file.lastModified();
        }
        return 0;
    }

    public final void saveData(String key, String value) {
        Editor editor = shref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public final void saveData(String key, int value) {
        Editor editor = shref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public final void saveData(String key, boolean value) {
        Editor editor = shref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public final void saveData(String key, long value) {
        Editor editor = shref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public final void saveData(String key, Set<String> value) {
        Editor editor = shref.edit();
        editor.putStringSet(key, value);
        editor.apply();
    }

    public void saveObject(String k, Object v) {
        String json = JsonUtil.toJson(v);
        saveData("Object_" + k, json);
    }

    public void saveObject(String k, List<?> src) {
        String json = JsonUtil.toJson(src);
        saveData("List_" + k, json);
    }

    public final String readString(String key) {
        return shref.getString(key, null);
    }

    public final String readString(String key, String defaultValue) {
        return shref.getString(key, defaultValue);
    }

    public final int readInt(String key) {
        return shref.getInt(key, 0);
    }

    public final int readInt(String key, int defalValue) {
        return shref.getInt(key, defalValue);
    }

    public final boolean readBoolean(String key) {
        return shref.getBoolean(key, false);
    }

    public final boolean readBoolean(String key, boolean defaultValue) {
        return shref.getBoolean(key, defaultValue);
    }

    public final long readLong(String key) {
        return shref.getLong(key, 0);
    }

    public final long readLong(String key, long defaultValue) {
        return shref.getLong(key, defaultValue);
    }

    public final HashSet<String> readSetString(String key) {
        return new HashSet<String>(shref.getStringSet(key, new HashSet<String>()));
    }

    public final HashSet<String> readSetString(String key, Set<String> defaultValue) {
        return new HashSet<String>(shref.getStringSet(key, defaultValue));
    }

    public <T> T readObject(String k, Class<T> classOfT) {
        String json = readString("Object_" + k);
        if (TextUtils.isEmpty(json)) return null;
        T output = null;
        try {
            output = JsonUtil.fromJson(json, classOfT);
        } catch (JsonParseException ignored) {
        }
        return output;
    }

    public <T> T readList(String k, TypeToken typeToken) {
        String json = readString("List_" + k);
        if (TextUtils.isEmpty(json)) return null;
        T output = null;
        try {
            output = JsonUtil.fromJson(json, typeToken);
        } catch (JsonParseException ignored) {
        }
        return output;
    }

    public final boolean contains(String key) {
        return shref.contains(key);
    }

    public final void remove(String key) {
        Editor editor = shref.edit();
        editor.remove(key);
        editor.apply();
    }

    public final void clear() {
        Editor editor = shref.edit();
        editor.clear();
        editor.apply();
    }

}
