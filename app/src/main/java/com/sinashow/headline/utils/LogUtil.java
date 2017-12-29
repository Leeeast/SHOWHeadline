package com.sinashow.headline.utils;

import android.util.Log;

import com.sinashow.headline.base.HeadlineApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Lee
 * @ClassName: LogUtil
 * @Description: 日志打印
 * @date 2015年5月7日 下午3:12:48
 */
public final class LogUtil {

    private static final String DEFAULT_TAG = HeadlineApplication.getAppContext()
            .getPackageName();

    public static boolean DEBUG_TOGGLE = true;// Log开关

    public static final int TYPE_V = 0x01;
    public static final int TYPE_D = TYPE_V << 1;
    public static final int TYPE_I = TYPE_D << 1;
    public static final int TYPE_W = TYPE_I << 1;
    public static final int TYPE_E = TYPE_W << 1;

    private static final int LOG_SIZE = 1024 * 500;// Log日志大小
    private static final String LOG_PATH = "Droid.log";// Log日志输出

    private static SimpleDateFormat sFormat = new SimpleDateFormat(
            "[MM-dd hh:mm:ss]", Locale.CHINA);// 打印日志时间

    private LogUtil() {

    }

    // *******************Log with ERROR*******************/
    public static void e(String msg) {
        log(DEFAULT_TAG, msg, TYPE_E, false);
    }

    public static void e(Exception e) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_E, false);
    }

    public static void e(Throwable tr) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_E, false);
    }

    public static void e(String tag, String msg) {
        log(tag, msg, TYPE_E, false);
    }

    public static void e(String tag, Exception e) {
        log(tag, Log.getStackTraceString(e), TYPE_E, false);
    }

    public static void e(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), TYPE_E, false);
    }

    public static void e(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_E, false);
    }

    public static void e(Object tag, Exception e) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(e), TYPE_E,
                false);
    }

    public static void e(Object tag, Throwable tr) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(tr),
                TYPE_E, false);
    }

    public static void e(String tag, String msg, Throwable tr) {
        log(tag, msg + '\n' + Log.getStackTraceString(tr), TYPE_E, false);
    }

    // *******************Log with WARNING*******************/
    public static void w(String msg) {
        log(DEFAULT_TAG, msg, TYPE_W, false);
    }

    public static void w(Exception e) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_W, false);
    }

    public static void w(Throwable tr) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_W, false);
    }

    public static void w(String tag, String msg) {
        log(tag, msg, TYPE_W, false);
    }

    public static void w(String tag, Exception e) {
        log(tag, Log.getStackTraceString(e), TYPE_W, false);
    }

    public static void w(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), TYPE_W, false);
    }

    public static void w(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_W, false);
    }

    public static void w(Object tag, Exception e) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(e), TYPE_W,
                false);
    }

    public static void w(Object tag, Throwable tr) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(tr),
                TYPE_W, false);
    }

    public static void w(String tag, String msg, Throwable tr) {
        log(tag, msg + '\n' + Log.getStackTraceString(tr), TYPE_W, false);
    }

    // *******************Log with INFO*******************/
    public static void i(String msg) {
        log(DEFAULT_TAG, msg, TYPE_I, false);
    }

    public static void i(String tag, String msg) {
        log(tag, msg, TYPE_I, false);
    }

    public static void i(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_I, false);
    }

    // *******************Log with DEBUG*******************/
    public static void d(String msg) {
        log(DEFAULT_TAG, msg, TYPE_D, false);
    }

    public static void d(String tag, String msg) {
        log(tag, msg, TYPE_D, false);
    }

    public static void d(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_D, false);
    }

    // *******************Log with VERBOSE*******************/
    public static void v(String msg) {
        log(DEFAULT_TAG, msg, TYPE_V, false);
    }

    public static void v(String tag, String msg) {
        log(tag, msg, TYPE_V, false);
    }

    public static void v(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_V, false);
    }

    // *******************Log with FILE*******************/

    /**
     * Print msg to file and console with INFO.
     *
     * @param msg
     */
    public static void f(String msg) {
        log(DEFAULT_TAG, msg, TYPE_I, true);
    }

    /**
     * Print exception to file and console with ERROR.
     *
     * @param e
     */
    public static void f(Exception e) {
        log(DEFAULT_TAG, Log.getStackTraceString(e), TYPE_E, true);
    }

    /**
     * Print throwable to file and console with ERROR.
     *
     * @param tr
     */
    public static void f(Throwable tr) {
        log(DEFAULT_TAG, Log.getStackTraceString(tr), TYPE_E, true);
    }

    /**
     * Print msg by tag to file and console with INFO.
     *
     * @param tag
     * @param msg
     */
    public static void f(String tag, String msg) {
        log(tag, msg, TYPE_I, true);
    }

    /**
     * Print exception by tag to file and console with ERROR.
     *
     * @param tag
     * @param e
     */
    public static void f(String tag, Exception e) {
        log(tag, Log.getStackTraceString(e), TYPE_E, true);
    }

    /**
     * Print throwable by tag to file and console with ERROR.
     *
     * @param tag
     * @param tr
     */
    public static void f(String tag, Throwable tr) {
        log(tag, Log.getStackTraceString(tr), TYPE_E, true);
    }

    /**
     * Print msg by tag to file and console with INFO.
     *
     * @param tag
     * @param msg
     */
    public static void f(Object tag, String msg) {
        log(tag.getClass().getSimpleName(), msg, TYPE_I, true);
    }

    /**
     * Print exception by tag to file and console with ERROR.
     *
     * @param tag
     * @param e
     */
    public static void f(Object tag, Exception e) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(e), TYPE_E,
                true);
    }

    /**
     * Print throwable by tag to file and console with ERROR.
     *
     * @param tag
     * @param tr
     */
    public static void f(Object tag, Throwable tr) {
        log(tag.getClass().getSimpleName(), Log.getStackTraceString(tr),
                TYPE_E, true);
    }

    /**
     * Print msg and throwable by tag to file and console with ERROR.
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void f(String tag, String msg, Throwable tr) {
        log(tag, msg + '\n' + Log.getStackTraceString(tr), TYPE_E, true);
    }

    /**
     * @param tag
     * @param msg
     * @param logType
     * @param toFile  设定文件
     * @return void 返回类型
     * @throws
     * @Title: log
     * @Description: 打印日志
     */
    private static void log(String tag, String msg, int logType, boolean toFile) {
        if (!DEBUG_TOGGLE) {
            return;
        }

        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        String fileInfo = "[" + stackTrace.getFileName() + "("
                + stackTrace.getLineNumber() + ") "
                + stackTrace.getMethodName() + "] ";

        msg = fileInfo + msg;

        switch (logType) {
            case TYPE_V:
                Log.v(tag, msg);
                break;

            case TYPE_D:
                Log.d(tag, msg);
                break;

            case TYPE_I:
                Log.i(tag, msg);
                break;

            case TYPE_W:
                Log.w(tag, msg);
                break;

            case TYPE_E:
                Log.e(tag, msg);
                break;

            default:
                break;
        }

        if (toFile) {
            String message = sFormat.format(new Date());
            message += "  ";
            message += tag;
            message += "  ";
            message += msg;
            message += "\n";
            // write(LOG_PATH, LOG_SIZE, message);
        }
    }
}