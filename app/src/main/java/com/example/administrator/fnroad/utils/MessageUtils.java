package com.example.administrator.fnroad.utils;

import android.os.Bundle;
import android.os.Message;

/**
 * 简单的Android.OS.Message的封装，复杂Msg的生产待逐步补充
 */
public class MessageUtils {

    /**
     * 只包含what信息的Message
     *
     * @param what
     * @return
     */
    public static Message createMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        return msg;
    }

    /**
     * 包含what,agr1,arg2常用int型信息的Message
     *
     * @param what
     * @param arg1
     * @param arg2
     * @return
     */
    public static Message createMsg(int what, int arg1, int arg2) {
        Message msg = createMsg(what);
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        return msg;
    }

    /**
     * 包含what并且需要有额外信息传递的Message
     *
     * @param what
     * @param bundleData
     * @return
     */
    public static Message createMsg(int what, Bundle bundleData) {
        Message msg = createMsg(what);
        msg.setData(bundleData);
        return msg;
    }

    /**
     * 包含what,agr1,arg2常用int型信息并且需要有额外信息传递的Message
     *
     * @param what
     * @param arg1
     * @param arg2
     * @param bundleData
     * @return
     */
    public static Message createMsg(int what, int arg1, int arg2, Bundle bundleData) {
        Message msg = createMsg(what, arg1, arg2);
        msg.setData(bundleData);
        return msg;
    }
}
