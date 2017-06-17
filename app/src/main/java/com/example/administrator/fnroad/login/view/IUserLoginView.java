package com.example.administrator.fnroad.login.view;

import android.app.Activity;

import com.example.administrator.fnroad.login.model.UserBean;


/**
 * Created by Administrator on 2016/10/10 0010.
 */
public interface IUserLoginView {     //根据效果图分析应该有哪些方法
    public Activity getActivity();

    public String getUsername();//Login要有获取用户名和密码的方法。

    public String getPassword();

    public void setUsername(String string);

    public void setPassword(String string);

    public boolean isRememberPasswordCbChecked();//判断记住密码复选框是否选中

    public boolean isAutoLoginCbChecked();//判断自动登录复选框是否选中

    public void rememberPasswordCbSetChecked();//设置记住密码复选框

    public void autoLoginCbSetChecked();//设置自动登录复选框

 //   public void autologin();//自动登录

    public void loginSuccess(UserBean user);//成功则跳转

    public void loginFailed();//失败则提示

    public void onError();//联网失败

    public void onEmptyAccount();

    public void loginFinish();

    public void startAPButtonAnimation();

    public void setWidgetsEnable(boolean isUnabled);
}
