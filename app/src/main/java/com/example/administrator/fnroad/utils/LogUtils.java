package com.example.administrator.fnroad.utils;

public class LogUtils {
    public static boolean isShow = true;

    /**
     * 设置打开日志开关
     *
     * @param pIsShow
     */
    public static void setShow(boolean pIsShow) {
        isShow = pIsShow;
    }

    /**
     * 根据tag打印相关v信息
     *
     * @param tag
     * @param msg
     */
    public static void v(String tag, String msg) {
        if (isShow) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.v(tag, traceInfo);
        }
    }

    /**
     * 根据tag打印v信息,包括Throwable的信息
     * * @param tag
     *
     * @param msg
     * @param tr
     */
    public static void v(String tag, String msg, Throwable tr) {
        if (isShow) {
            android.util.Log.v(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出debug信息
     *
     * @param tag
     * @param msg
     */
    public static void d(String tag, String msg) {
        if (isShow) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.d(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出debug信息 包括Throwable的信息
     * * @param tag
     *
     * @param msg
     * @param tr
     */
    public static void d(String tag, String msg, Throwable tr) {
        if (isShow) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出info的信息
     * * @param tag
     *
     * @param msg
     */
    public static void i(String tag, String msg) {
        if (isShow) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.i(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出info信息 包括Throwable的信息
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void i(String tag, String msg, Throwable tr) {
        if (isShow) {
            android.util.Log.i(tag, msg, tr);
        }
    }

    /**
     * 根据tag打印输出error信息
     *
     * @param tag
     * @param msg
     */
    public static void e(String tag, String msg) {
        if (isShow) {
            StackTraceElement ste = new Throwable().getStackTrace()[1];
            String traceInfo = ste.getClassName() + "::";
            traceInfo += ste.getMethodName();
            traceInfo += "@" + ste.getLineNumber() + ">>>";
            android.util.Log.e(tag, traceInfo + msg);
        }
    }

    /**
     * 根据tag打印输出的error信息 包括Throwable的信息
     *
     * @param tag
     * @param msg
     * @param tr
     */
    public static void e(String tag, String msg, Throwable tr) {
        if (isShow) {
            android.util.Log.e(tag, msg, tr);
        }
    }

}
