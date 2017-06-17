package com.example.administrator.fnroad.login.presenter;

import android.util.Log;
import android.view.View;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.login.model.UserBean;
import com.example.administrator.fnroad.login.view.IUserLoginView;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;
import com.example.administrator.fnroad.utils.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Request;


/**
 * Created by Administrator on 2016/10/10 0010.
 */
public class UserLoginPresenter implements IUserLoginPresenter {
    private static final String TAG = "UserLoginPresenter";
    private IUserLoginView mUserLoginView;
    private UserBean mUserBean;
    private SharePrefrenceHelper sharePrefrenceHelper;
    private final String IS_AUTO_CHECK = "IS_AUTO_CHECK";
    private final String IS_REMEMBER_CHECK = "IS_REMEMBER_CHECK";
    private final String USERNAME = "USERNAME";
    private final String PASSWORD = "PASSWORD";
    private final String GROUP_NAME = "GROUP_NAME";
    private final String GROUP_HIDE = "GROUP_HIDE";
    private final String AUTO_LOGIN = "AUTO_LOGIN";

    public UserLoginPresenter(IUserLoginView mUserLoginView) {
        this.mUserLoginView = mUserLoginView;
        mUserBean = new UserBean();
        sharePrefrenceHelper = SharePrefrenceHelper.getInstance(mUserLoginView.getActivity());
        autoLogin();
    }

    /**
     * CheckBox和Button控件点击事件，用来判断哪个控件被点击
     * 在进行控件点击时，传入该控件点击状态
     *
     * @param v
     */
    public void onClicked(View v) {
        switch (v.getId()) {
            case R.id.cb_login_remember_password:
                rememberCbOnClicked(mUserLoginView.isRememberPasswordCbChecked());
                break;
            case R.id.cb_login_auto_login:
                autoLoginCbOnClicked(mUserLoginView.isAutoLoginCbChecked());
                break;
            case R.id.apbtn_login_login:
                loginBtnOnClicked(mUserLoginView.isRememberPasswordCbChecked());
                break;
        }
    }

    /**
     * 记住密码复选框点击响应事件
     *
     * @param isRememberPasswordCbChecked
     */
    private void rememberCbOnClicked(Boolean isRememberPasswordCbChecked) {
        if (isRememberPasswordCbChecked)
            sharePrefrenceHelper.putBooleanValue(IS_REMEMBER_CHECK, true);
        else sharePrefrenceHelper.putBooleanValue(IS_REMEMBER_CHECK, false);
    }

    /**
     * 自动登录复选框点击响应事件
     *
     * @param isAutoLoginCbChecked
     */
    private void autoLoginCbOnClicked(Boolean isAutoLoginCbChecked) {
        if (isAutoLoginCbChecked) sharePrefrenceHelper.putBooleanValue(IS_AUTO_CHECK, true);
        else sharePrefrenceHelper.putBooleanValue(IS_AUTO_CHECK, false);
    }

    /**
     * 登录按钮点击响应事件
     * 登录事件中需要判断记住密码复选框的状态，用以确定登录成功后是否保存数据
     * url地址不对可能会导致点击按钮之后程序不动，界面卡住但是没有提示。
     *
     * @param isRememberPasswordCbChecked
     */
    private void loginBtnOnClicked(final Boolean isRememberPasswordCbChecked) {
        if ("".equals(mUserLoginView.getUsername()) || "".equals(mUserLoginView.getPassword())) {
            mUserLoginView.onEmptyAccount();
            return;
        }

        mUserLoginView.setWidgetsEnable(false);
        mUserLoginView.startAPButtonAnimation();

        mUserBean.setUsername(mUserLoginView.getUsername());
        mUserBean.setPassword(mUserLoginView.getPassword());

        OkHttpUtils okHttpUtils = OkHttpUtils.getInstance();
        OkHttpUtils.Param[] param = new OkHttpUtils.Param[2];
        param[0] = new OkHttpUtils.Param("USERNAME", mUserBean.getUsername());
        param[1] = new OkHttpUtils.Param("USERPSW", mUserBean.getPassword());
        String url = mUserLoginView.getActivity().getString(R.string.php_service_url) + "User/validate";

        okHttpUtils.postAsyn(url, param, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                mUserLoginView.onError();
            }

            @Override
            public void onResponse(String response) {
                try {
                    response = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                    JSONObject jsonObject = new JSONObject(response);
                    String result = jsonObject.getString("status");
                    if (result.equals("success")) {
//                        JSONObject info = jsonObject.getJSONObject("info");
//                        //改第一处（mark）
//                        sharePrefrenceHelper.putStringValue(GROUP_NAME, info.getString("name"));
//                        sharePrefrenceHelper.putStringValue(GROUP_HIDE, info.getString("hide"));
//                        sharePrefrenceHelper.putStringValue("isongoing", "");
                        mUserBean.setName(jsonObject.getString("name"));
                        mUserBean.setTelephone(jsonObject.getInt("telephone"));
                        mUserBean.setPermission(jsonObject.getInt("permission"));
                        mUserBean.setOrganization(jsonObject.getString("organization"));

                        loginSuccess(mUserBean, isRememberPasswordCbChecked);
                        mUserLoginView.loginSuccess(mUserBean);
                        ProjectApplication.getInstance().setUserBean(mUserBean);
                        //mUserLoginView.loginFinish();
                    }//点击之后跳转
                    else {
                        mUserLoginView.loginFailed();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    mUserLoginView.onError();
                }
            }
        });
    }

    /**
     * 登录成功后保存用户名和密码
     *
     * @param user
     * @param isRememberPasswordCbChecked
     */
    private void loginSuccess(UserBean user, Boolean isRememberPasswordCbChecked) {
        if (isRememberPasswordCbChecked) {
            sharePrefrenceHelper.putStringValue(USERNAME, user.getUsername());
            sharePrefrenceHelper.putStringValue(PASSWORD, user.getPassword());
            Log.e(TAG, "loginSuccess: "+sharePrefrenceHelper.getStringValue(USERNAME));
            Log.e(TAG, "loginSuccess: "+sharePrefrenceHelper.getStringValue(PASSWORD));
            sharePrefrenceHelper.putStringValue(AUTO_LOGIN, "TRUE");
        }
    }

    /**
     * 自动登录
     * 只有在之前已经记住密码且已选择自动登录复选框才会进行自动登录。
     * 判断之前是否记住密码，是则设置复选框，并往EditView中添加记录中的用户名和密码
     * 判断之前是否已自动登录，是则设置复选框，并进行界面跳转
     */
    private void autoLogin() {
        if (sharePrefrenceHelper.getBooleanValue(IS_REMEMBER_CHECK)) {
            mUserLoginView.rememberPasswordCbSetChecked();
            mUserLoginView.setUsername(sharePrefrenceHelper.getStringValue(USERNAME));
            mUserLoginView.setPassword(sharePrefrenceHelper.getStringValue(PASSWORD));
            Log.e(TAG, "autoLogin: "+sharePrefrenceHelper.getStringValue(USERNAME) );
            Log.e(TAG, "autoLogin: "+sharePrefrenceHelper.getStringValue(PASSWORD));
            mUserBean.setUsername(sharePrefrenceHelper.getStringValue(USERNAME));
            mUserBean.setPassword(sharePrefrenceHelper.getStringValue(PASSWORD));
            if (sharePrefrenceHelper.getBooleanValue(IS_AUTO_CHECK)) {
                mUserLoginView.autoLoginCbSetChecked();
                if (isAutoLogoutOrAutoLogin()) {
                    loginBtnOnClicked(true);
//                    ProjectApplication.getInstance().setUserBean(mUserBean);
                }
            }
        }
    }

    public boolean isAutoLogoutOrAutoLogin() {
        if ("TRUE".equals(sharePrefrenceHelper.getStringValue(AUTO_LOGIN)))
            return true;
        else return false;
    }
}
