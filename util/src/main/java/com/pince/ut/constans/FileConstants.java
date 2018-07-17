package com.pince.ut.constans;

import android.app.Application;
import android.os.Environment;

import java.io.File;

import com.pince.ut.FileUtil;

/**
 * Created by czwathou on 2017/7/17.
 */

public class FileConstants {
    public static final int MAX_DISK_CACHE_SIZE = 250 * 1024 * 1024; // 250MB

    // disk缓存路径==================================================================================
    public static String CACHE_PATH = null;// 缓存根目录
    public static String CACHE_IMAGE_DIR = null; //图片缓存目录
    public static String CACHE_AUDIO_DIR = null;// 录音缓存目录
    public static String CACHE_VIDEO_DIR = null;// 视频缓存目录
    public static String CACHE_SHOT_DIR = null;// 截屏缓存目录

    public static String ROOT_PATH = null;// 文件存储根目录
    public static String DIR_DOWNLOAD = null;// 下载文件存储路径
    public static String DIR_SAVE_FILE = null;// 保存文件存储路径
    public static String DIR_CRASH_LOG_FILE = null;// 崩溃日志路径
    public static String DIR_PDF = null;// pdf路径

    /**
     * 必须在applocation.oncreate里进行初始化
     *
     * @param context
     */
    public static void initFileConfig(Application context) {
        if (FileUtil.isCanUseSD()) {
            ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + context.getPackageName();
            CACHE_PATH = ROOT_PATH + "/cache";
        } else {
            CACHE_PATH = context.getCacheDir().getPath();
            ROOT_PATH = context.getFilesDir().getPath();
        }

        CACHE_IMAGE_DIR = CACHE_PATH + "/images/";
        CACHE_AUDIO_DIR = CACHE_PATH + "/audio/";
        CACHE_VIDEO_DIR = CACHE_PATH + "/video/";
        CACHE_SHOT_DIR = CACHE_PATH + "/screenShot/";

        DIR_DOWNLOAD = ROOT_PATH + "/downloadFile/";
        DIR_SAVE_FILE = ROOT_PATH + "/save/";
        DIR_CRASH_LOG_FILE = ROOT_PATH + "/log/";
        DIR_PDF = ROOT_PATH + "/pdf/";
    }
}
