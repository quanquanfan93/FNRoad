package com.example.administrator.fnroad.user.presenter;

import android.view.View;
import android.widget.Toast;

import com.example.administrator.fnroad.ProjectApplication;
import com.example.administrator.fnroad.R;
import com.example.administrator.fnroad.main.model.Project;
import com.example.administrator.fnroad.main.model.ProjectType;
import com.example.administrator.fnroad.user.model.Message;
import com.example.administrator.fnroad.user.view.IUserView;
import com.example.administrator.fnroad.utils.OkHttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import jcifs.dcerpc.msrpc.MsrpcDfsRootEnum;
import okhttp3.Request;

/**
 * Created by Administrator on 2017/6/24 0024.
 */

public class UserPresenterImpl implements IUserPresenter {
    private IUserView mUserView;
    private List<Message> mMessageList = new ArrayList<>();
    private String messageString;
    private int currentMessageId = 0;
    StringBuilder readMessageId = new StringBuilder("");


    public UserPresenterImpl(IUserView userView) {
        this.mUserView = userView;
    }

    @Override
    public void initUnReadMessage() {
        String url = mUserView.getActivity().getString(R.string.php_service_url) + "Message/all_message";
        OkHttpUtils.getAsyn(url, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ProjectApplication.getInstance().onNetError();
                mUserView.getActivity().finish();
            }

            @Override
            public void onResponse(String response) {
                if (response.equals("null")) {
                    mMessageList.clear();
                } else {
                    Gson gson = new Gson();
                    mMessageList = gson.fromJson(response, new TypeToken<List<Message>>() {
                    }.getType());
                }
                if ( mMessageList.size() > 0) {
                    int i=0;
                    for(Message message:mMessageList){
                        if(message.getReadStatus()==0){
                            i++;
                        }
                    }
                    Message message=mMessageList.get(0);
                    mUserView.setMessageTV(createMessageText(message));
                    if(message.getReadStatus()==0){
                            readMessageId.append(message.getId()).append(",");
                    }
                    mUserView.setMessageTile("消息（总共" + mMessageList.size() + "条，未读"+i+"条）");
                } else mUserView.setMessageTV("无任何消息");
                currentMessageId = 0;
            }
        });
    }

    @Override
    public void onWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.iv_user_back:
                updateReadStatus();
                mUserView.getActivity().finish();
                break;
            case R.id.btn_user_logout:
                updateReadStatus();
                mUserView.logout();
                break;
            case R.id.btn_user_message_last:
                showLastMessage();
                break;
            case R.id.btn_user_message_next:
                showNextMessage();
                break;
            default:
                break;
        }
    }

    /**
     * 生成消息文字
     *
     * @param message
     * @return
     */
    private String createMessageText(Message message) {
        String messageText = null;
        if (message.getPassStatus() == 1) {
            messageText = message.getReadStatus()+";"+"管理员通过了" + message.getProjectName() + "的更新，工作建议为："
                    + message.getDescription();
        } else if (message.getPassStatus() == 0) {
            messageText = message.getReadStatus()+";"+"管理员拒绝了" + message.getProjectName() + "的更新，理由为："
                    + message.getDescription();
        }
        return messageText;
    }

    private void showLastMessage() {
        if(mMessageList.size()==0)
            return;
        if (currentMessageId > 0) {
            currentMessageId = currentMessageId - 1;
        }
        mUserView.setMessageTV(createMessageText(mMessageList.get(currentMessageId)));
    }

    private void showNextMessage() {
        if(mMessageList.size()==0)
            return;
        if (currentMessageId < mMessageList.size() - 1) {
            currentMessageId = currentMessageId + 1;
        }
        Message message=mMessageList.get(currentMessageId);
        mUserView.setMessageTV(createMessageText(message));
        if(message.getReadStatus()==0)
        readMessageId.append(message.getId()).append(",");
    }

    private void updateReadStatus() {
//        for (Message message : mMessageList) {
//            readMessageId.append(message.getId()).append(",");
//        }
        String readMessage = readMessageId.toString();
        if(readMessage.equals(""))
            return;
        OkHttpUtils.Param[] param = new OkHttpUtils.Param[1];
        param[0] = new OkHttpUtils.Param("READ_MESSAGE", readMessage);
        String url = mUserView.getActivity().getString(R.string.php_service_url) + "Message/update_selected_status";
        OkHttpUtils.postAsyn(url, param, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ProjectApplication.getInstance().onNetError();
            }

            @Override
            public void onResponse(String response) {
                Toast.makeText(mUserView.getActivity(), "已读更新成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
