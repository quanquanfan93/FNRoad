package com.example.administrator.fnroad.login.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dd.processbutton.iml.ActionProcessButton;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.login.model.UserBean;
import com.example.administrator.fnroad.login.presenter.IUserLoginPresenter;
import com.example.administrator.fnroad.login.presenter.UserLoginPresenter;
import com.example.administrator.fnroad.main.view.MainActivity;

/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class UserLoginActivity extends AppCompatActivity implements IUserLoginView, View.OnClickListener {
    private ActionProcessButton loginAPBTN;
    private EditText usernameEdt = null;
    private EditText passwordEdt = null;
    private CheckBox rememberPasswordCb = null;
    private CheckBox autoLoginCb = null;
    private LinearLayout errorWaringLL;
    private TextView errorWarningTV;
    private IUserLoginPresenter userLoginPresenter;

    private String autoLogin = "autoLogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    public void initView() {
        //@BindView使用时初始化控件返回为空，因此不用。
        loginAPBTN = (ActionProcessButton) findViewById(R.id.apbtn_login_login);
        loginAPBTN.setMode(ActionProcessButton.Mode.ENDLESS);
        usernameEdt = (EditText) findViewById(R.id.edt_login_username);
        passwordEdt = (EditText) findViewById(R.id.edt_login_password);
        rememberPasswordCb = (CheckBox) findViewById(R.id.cb_login_remember_password);
        autoLoginCb = (CheckBox) findViewById(R.id.cb_login_auto_login);
        errorWaringLL = (LinearLayout) findViewById(R.id.ll_login_error_warning);
        errorWarningTV = (TextView) findViewById(R.id.tv_login_error_warning);

        userLoginPresenter = new UserLoginPresenter(this);
        rememberPasswordCb.setOnClickListener(this);
        autoLoginCb.setOnClickListener(this);
        loginAPBTN.setOnClickListener(this);
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    /**
     * 获取EditView控件中的用户名
     *
     * @return
     */
    @Override
    public String getUsername() {
        return usernameEdt.getText().toString();
    }

    /**
     * 获取EditView控件中的密码
     *
     * @return
     */
    @Override
    public String getPassword() {
        return passwordEdt.getText().toString();
    }

    /**
     * 设置EditView控件中的用户名
     *
     * @param string
     */
    @Override
    public void setUsername(String string) {
        usernameEdt.setText(string);
    }

    /**
     * 设置EditView控件中的密码
     *
     * @param string
     */
    @Override
    public void setPassword(String string) {
        passwordEdt.setText(string);
    }

    /**
     * 判断记住密码复选框是否选中
     *
     * @return
     */
    @Override
    public boolean isRememberPasswordCbChecked() {
        return rememberPasswordCb.isChecked();
    }

    /**
     * 判断自动登录复选框是否选中
     *
     * @return
     */
    @Override
    public boolean isAutoLoginCbChecked() {
        return autoLoginCb.isChecked();
    }

    /**
     * 设置记住密码复选框
     */
    @Override
    public void rememberPasswordCbSetChecked() {
        rememberPasswordCb.setChecked(true);
    }

    /**
     * 设置自动登录复选框
     */
    @Override
    public void autoLoginCbSetChecked() {
        autoLoginCb.setChecked(true);
    }

    /**
     * 登陆成功则跳转
     *
     * @param user
     */
    @Override
    public void loginSuccess(UserBean user) {
        setWidgetsEnable(true);
        loginAPBTN.setProgress(100);
        Intent loginIntent = new Intent(UserLoginActivity.this, MainActivity.class);
        startActivity(loginIntent);
        this.finish();
    }

    /**
     * 账号或密码有错时的登录失败
     */
    @Override
    public void loginFailed() {
        setWidgetsEnable(true);
        loginAPBTN.setProgress(-1);
        errorWaringLL.setVisibility(View.VISIBLE);
        errorWarningTV.setText("用户名或密码错误！");
    }

    /**
     * 控制CheckBox和Button控件点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        userLoginPresenter.onClicked(v);
    }

    /**
     * 连接不上网络报错
     */
    @Override
    public void onError() {
        setWidgetsEnable(true);
        loginAPBTN.setProgress(-1);
        errorWaringLL.setVisibility(View.VISIBLE);
        errorWarningTV.setText("连接服务器出错！");
    }

    @Override
    public void onEmptyAccount() {
        errorWaringLL.setVisibility(View.VISIBLE);
        errorWarningTV.setText("用户名或密码不能为空！");
    }

    @Override
    public void loginFinish() {
        finish();
    }

    @Override
    public void startAPButtonAnimation() {
        loginAPBTN.setProgress(1);
    }

    @Override
    public void setWidgetsEnable(boolean isUnabled) {
        errorWaringLL.setVisibility(View.GONE);
        loginAPBTN.setEnabled(isUnabled);
        usernameEdt.setEnabled(isUnabled);
        passwordEdt.setEnabled(isUnabled);
        rememberPasswordCb.setEnabled(isUnabled);
        autoLoginCb.setEnabled(isUnabled);
    }
}