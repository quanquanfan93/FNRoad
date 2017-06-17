package com.example.administrator.fnroad.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by hornkyin on 17/1/19.
 */
public class AssetsUtils {

    public static File copyAssets(Context context, String path, String fileName) {
        //有限判断拷贝路径的文件夹在不在，不在先创建文件夹。否则，在下面调用“new FileOutputStream(file)”会出异常
        File folder = new File(path);
        if (!folder.exists()) folder.mkdirs();

        File file = new File(path + "/" + fileName);
        InputStream is = null;
        OutputStream os = null;
        if (!file.exists()) try {
            is = context.getAssets().open(fileName);

            os = new FileOutputStream(file);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read;
            while ((read = is.read(buffer)) != -1) os.write(buffer, 0, read);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                    is = null;
                }
                if (os != null) {
                    os.flush();
                    os.close();
                    os = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}
