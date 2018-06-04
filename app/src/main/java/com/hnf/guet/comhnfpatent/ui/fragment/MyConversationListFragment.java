package com.hnf.guet.comhnfpatent.ui.fragment;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.ui.activity.ChatActivity;
import com.hnf.guet.comhnfpatent.util.NetUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.model.EaseDingMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import retrofit2.http.GET;

public class MyConversationListFragment extends EaseConversationListFragment {




    @Override
    protected void setUpView() {
        super.setUpView();

        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();//获取用户名
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID,username);
//                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        Toast.makeText(getActivity(),"网络错误，请检查网络连接~",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean delateMessage = false;
        if (item.getItemId() == R.id.delete_conversation){
            delateMessage = true;
        }else if (item.getItemId() == R.id.delete_conversation){
            delateMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(),delateMessage);
        if (delateMessage){
            EaseDingMessageHelper.get().delete(tobeDeleteCons);
        }
        refresh();
        return true;
    }
}
