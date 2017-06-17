package com.example.administrator.fnroad.crash;

import android.content.Context;
import android.util.Log;
import com.example.administrator.fnroad.utils.SystemUtils;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;

public class ProjectCrash implements Thread.UncaughtExceptionHandler, Serializable {

    private static final long serialVersionUID = -1L;

    private static final String TAG = "PeojectCrash";
    private static int TYPE_SAVE_SDCARD = 1; //崩溃日志保存本地SDCard
    private static int TYPE_SAVE_REMOTE = 2; //崩溃日志保存至服务器


    private int type = 1;
    private static final String CRASH_SAVE_SDCARD = ""; //保存至本地SD卡路径
    private static final String CRASH_SAVE_REMOTE = ""; //保存至服务器端地址

    private static ProjectCrash projectCrash = new ProjectCrash();//饿汉式单例模式
    private Context context;

    //私有化构造函数
    private ProjectCrash() {

    }


    public static ProjectCrash getInstance() {
        return projectCrash;
    }

    /**
     * 进行重新捕捉异常
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (type == TYPE_SAVE_REMOTE) {
            // 1,保存信息到sdcard中
            saveToSdcard(context, ex);
        } else if (type == TYPE_SAVE_SDCARD) {
            // 2,异常崩溃信息投递到服务器
            saveToServer(context, ex);
        }
    }


    /**
     * 设置自定异常处理类
     *
     * @param pContext
     */
    public void setCustomCrashInfo(Context pContext) {
        this.context = pContext;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 保存信息到sdcard中
     *
     * @param context
     * @param ex
     */
    private void saveToSdcard(Context context, Throwable ex) {

    }

    private void saveToServer(Context context, Throwable ex) {

    }

    /**
     * 获取并且转化异常信息
     * 同时可以进行投递相关的设备，用户信息
     *
     * @param ex
     * @return 异常信息的字符串形式
     */
    private String getExceptionInfo(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("---------Crash Log Begin---------\n");
        //在这边可以进行相关设备信息投递--这边就稍微设置几个吧
        //其他设备和用户信息大家可以自己去扩展收集上传投递
        stringBuffer.append("SystemVersion:" + SystemUtils.getLocalSystemVersion() + "\n");
        stringBuffer.append(sw.toString() + "\n");
        stringBuffer.append("---------Crash Log End---------\n");
        return stringBuffer.toString();
    }

    //通过复写readResolve方法
    //控制返回的实例 保证反序列的时候  单例
    private Object readResolve() {
        Log.i(TAG, "readResolve方法，确保不能通过反序列化破坏单例模式");
        return projectCrash;
    }
}
