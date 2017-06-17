package com.example.administrator.fnroad.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.InputStream;

/**
 * Created by YXY on 2016/12/21.
 */
public class LoadXMLUtils {
    public InputStream getFromAssets(Context context, String fileName) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
