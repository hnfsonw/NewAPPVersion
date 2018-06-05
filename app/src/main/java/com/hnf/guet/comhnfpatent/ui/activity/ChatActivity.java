package com.hnf.guet.comhnfpatent.ui.activity;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.EaseChatMessageList;

import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置状态栏的颜色hua
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#FF4081"));
        }


        setContentView(R.layout.activity_chat);
        EaseChatFragment easeChatFragment = new EaseChatFragment();
        easeChatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container_chat, easeChatFragment).commit();

        LogUtils.e("用户名：-------->"+getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        EMMessage message = EMMessage.createTxtSendMessage("fuck",getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID));
        EMClient.getInstance().chatManager().sendMessage(message);

        EMMessageListener msgListener = new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                LogUtils.e("收到消息了！！","");
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }
}
