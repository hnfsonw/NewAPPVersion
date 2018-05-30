package com.hnf.guet.comhnfpatent.ui.fragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseFragment;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.ui.activity.ChatActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

public class MessageFragment extends BaseFragment {
    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_fragment,null);
        return view;
    }

    @Override
    protected void init() {
        MyConversationListFragment conversationListFragment = new MyConversationListFragment();
        getFragmentManager().beginTransaction().add(R.id.fragment_container,conversationListFragment)
                .show(conversationListFragment)
                .commit();


    }

    @Override
    protected void dismissNewok() {

    }

    @Override
    public void setData(List<ResultBean> data) {

    }
}
