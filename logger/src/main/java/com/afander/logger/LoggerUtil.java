package com.afander.logger;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.Printer;

import static com.afander.logger.Utils.checkNotNull;

/**
 * Log工具类，改写自 https://github.com/orhanobut/logger
 * {@link com.orhanobut.logger.Logger}
 * <p>
 * <p>
 * Created on 2018/6/14.
 *
 * @author ice
 */
public class LoggerUtil {

    public static abstract class LogConfig {
        /**
         * 头部tag，也是默认tag。
         */
        @Nullable
        public String headTag;

        public boolean enableLogger;

        /**
         * log日志文件夹路径(若不填写，则不保存log为本地文件）。
         * 示范 logDirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "afander_logger";
         */
        public String logDirPath;

        /**
         * 暂未提供时长管理。
         */
        public long maxSaveTime;

        /**
         * @param priority {@link #VERBOSE}...{@link #ERROR}...
         */
        public abstract boolean isLoggable(int priority, @Nullable String tag);

        public abstract boolean isSaveToFile(int priority, @Nullable String tag, @NonNull String message);
    }


    private static String sLogDirPath = null;


    /**
     * 初始化。
     * 若无法满足业务需求，可手动配置Logger.addLogAdapter(...)。
     */
    public static void init(@NonNull final LogConfig logConfig) {
        Utils.checkNotNull(logConfig);

        AndroidLogAdapter androidLogAdapter = new AndroidLogAdapter(CustomPrettyFormatStrategy
                .newBuilder()
                .tag(logConfig.headTag)
                .build()) {
            @Override
            public void log(int priority, @Nullable String tag, @NonNull String message) {
                super.log(priority, tag, message);
            }

            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
//                return super.isLoggable(priority, tag);
                return logConfig.isLoggable(priority, tag);
            }
        };
        LoggerUtil.addLogAdapter(androidLogAdapter);

        if (logConfig.logDirPath != null) {
            sLogDirPath = logConfig.logDirPath;
            DiskLogAdapter diskLogAdapter = new DiskLogAdapter(
                    CustomCsvFormatStrategy
                            .newBuilder()
                            .tag(logConfig.headTag)
                            .folderPath(sLogDirPath)
                            .build()) {
                @Override
                public void log(int priority, @Nullable String tag, @NonNull String message) {
                    if (logConfig.isSaveToFile(priority, tag, message)) {
                        super.log(priority, tag, message);
                    }
                }
            };
            LoggerUtil.addLogAdapter(diskLogAdapter);
        }

        if (logConfig.enableLogger) {
            Logger.printer(printer);
        }
    }

    /**
     * 删除log文件夹下所有log。
     */
    public static boolean deleteLocalLogs() {
        return Utils.deleteFileBySuffix(sLogDirPath, DiskLogStrategy.sFileSuffix);
    }




    /*---------------------------------上为配置，下为方法代理--------------------------------------*/


    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;


    @NonNull private static Printer printer = new CustomLoggerPrinter();

    private LoggerUtil() {
        //no instance
    }

    public static void printer(@NonNull Printer printer) {
        LoggerUtil.printer = checkNotNull(printer);
    }

    public static void addLogAdapter(@NonNull LogAdapter adapter) {
        printer.addAdapter(checkNotNull(adapter));
    }

    public static void clearLogAdapters() {
        printer.clearLogAdapters();
    }

    /**
     * Given tag will be used as tag only once for this method call regardless of the tag that's been
     * set during initialization. After this invocation, the general tag that's been set will
     * be used for the subsequent log calls
     */
    public static Printer t(@Nullable String tag) {
        return printer.t(tag);
    }

    /**
     * General log function that accepts all configurations as parameter
     */
    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        printer.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        printer.d(message, args);
    }

    public static void d(@Nullable Object object) {
        printer.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        printer.e(null, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        printer.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        printer.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        printer.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        printer.w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(@NonNull String message, @Nullable Object... args) {
        printer.wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        printer.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        printer.xml(xml);
    }


}
