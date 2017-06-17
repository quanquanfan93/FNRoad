package com.example.administrator.fnroad.spreference;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 当前注释类：当前为SharedPerferences进行封装基本的方法,SharedPerferences已经封装成单例模式
 * 可以通过SharedPreferences sp=SharedPreferencesHelper.getInstances(ProjectApplication.getInstance())进行获取当前对象
 * sp.putStringValue(key,value)进行使用
 */
public class SharePrefrenceHelper {
    private static final String SHARED_PATH = "base_shared";
    private static SharePrefrenceHelper instance = null;
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    private SharePrefrenceHelper(Context context) {
        sp = context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
        editor = sp.edit();

    }

    public synchronized static SharePrefrenceHelper getInstance(Context context) {
        if (null == instance && context != null) {
            instance = new SharePrefrenceHelper(context);
        }
        return instance;
    }

    public long getLongValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getLong(key, 0);
        }
        return 0;
    }

    public String getStringValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getString(key, null);
        }
        return null;
    }

    public int getIntValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public int getIntValueByDefault(String key) {
        if (key != null && !key.equals("")) {
            return sp.getInt(key, 0);
        }
        return 0;
    }

    public boolean getBooleanValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getBoolean(key, false);
        }
        return true;
    }

    public float getFloatValue(String key) {
        if (key != null && !key.equals("")) {
            return sp.getFloat(key, 0);
        }
        return 0;
    }

    public void putStringValue(String key, String value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public void putIntValue(String key, int value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public void putBooleanValue(String key, boolean value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public void putLongValue(String key, long value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putLong(key, value);
            editor.apply();
        }
    }

    public void putFloatValue(String key, Float value) {
        if (key != null && !key.equals("")) {
            editor = sp.edit();
            editor.putFloat(key, value);
            editor.apply();
        }
    }

}
