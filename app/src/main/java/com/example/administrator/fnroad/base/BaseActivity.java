package com.example.administrator.fnroad.base;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.administrator.fnroad.utils.ToastUtils;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int getContentViewId();
    private static boolean isExit = false;//退出应用
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    isExit = false;
                    break;
            }
        }
    };

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };
    /**
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);
        initAllMembersView(savedInstanceState);
        // Check whether we have write permission
        int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,//需要请求的所有权限，这是个数组String[]
                    REQUEST_EXTERNAL_STORAGE//请求码
            );
        }
    }

    protected abstract void initAllMembersView(Bundle savedInstanceState);


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ButterKnife.unbind(this);//解除绑定
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //双击返回键退出应用
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                finish();
                System.exit(0);
            } else {
                isExit = true;
                ToastUtils.showShort(getApplicationContext(), "再按一次后退键退出程序");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
