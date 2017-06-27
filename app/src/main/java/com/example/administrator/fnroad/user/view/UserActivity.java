package com.example.administrator.fnroad.user.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.base.BaseActivity;
import com.example.administrator.fnroad.login.view.UserLoginActivity;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.spreference.SharePrefrenceHelper;
import com.example.administrator.fnroad.user.presenter.IUserPresenter;
import com.example.administrator.fnroad.user.presenter.UserPresenterImpl;

public class UserActivity extends AppCompatActivity implements IUserView, View.OnClickListener{

    private ImageView backIV;
    private Button logoutBTN;
    private TextView usernameTV;
    private TextView permissionTV;
    private TextView messageTileTV;
//    private TextView historyMessageTV;
    private TextView messageDescriptionTV;
    private Button messageLastBTN;
    private Button messageNextBTN;
    private IUserPresenter mUserPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initAllView();

    }

    private void initAllView() {
        backIV = (ImageView) findViewById(R.id.iv_user_back);
        logoutBTN = (Button) findViewById(R.id.btn_user_logout);
        usernameTV = (TextView) findViewById(R.id.tv_user_username);
        permissionTV = (TextView) findViewById(R.id.tv_user_permission);
        messageTileTV = (TextView) findViewById(R.id.tv_user_message_title);
//        historyMessageTV = (TextView) findViewById(R.id.tv_user_history_message);
        messageDescriptionTV = (TextView) findViewById(R.id.tv_user_message_description);
        messageLastBTN = (Button) findViewById(R.id.btn_user_message_last);
        messageNextBTN = (Button) findViewById(R.id.btn_user_message_next);
        mUserPresenter=new UserPresenterImpl(this);
        backIV.setOnClickListener(this);
        logoutBTN.setOnClickListener(this);
//        historyMessageTV.setOnClickListener(this);
        messageLastBTN.setOnClickListener(this);
        messageNextBTN.setOnClickListener(this);
        mUserPresenter.initUnReadMessage();
        usernameTV.setText(ProjectApplication.getInstance().getUserBean().getName());
        if(ProjectApplication.getInstance().getUserBean().getPermission()==0){
            permissionTV.setText("巡查人员");
        }else {
            permissionTV.setText("管理员");
        }
//        historyMessageTV.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void onClick(View view) {
        mUserPresenter.onWidgetClick(view);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setMessageTV(String message) {
        String[] messages=message.split(";");
        messageDescriptionTV.setText(messages[1]);
        if(messages[0].equals("0"))
            messageDescriptionTV.setTextColor(Color.RED);
        else if(messages[0].equals("1"))
            messageDescriptionTV.setTextColor(Color.BLACK);

    }

    @Override
    public void setMessageTile(String tile) {
        messageTileTV.setText(tile);
    }

    @Override
    public void logout() {
        SharePrefrenceHelper sharePrefrenceHelper = SharePrefrenceHelper.getInstance(getActivity());
        sharePrefrenceHelper.putStringValue("AUTO_LOGIN", "FALSE");
        Intent intent=new Intent(UserActivity.this, UserLoginActivity.class);
        startActivity(intent);
    }
}
