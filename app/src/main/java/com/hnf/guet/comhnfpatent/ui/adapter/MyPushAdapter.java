package com.hnf.guet.comhnfpatent.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.ui.activity.MyPushActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPushAdapter extends BaseAdapter{
    private static final String TAG = "MyPushAdapter";
    private List<ResultBean> resultDatas;
    private MyPushActivity myPushActivity;
    private Context mConext;
    public MyPushAdapter(Context context,MyPushActivity activity,List<ResultBean> resultList){
        myPushActivity = activity;
        resultDatas = resultList;
        mConext = context;
    }

    @Override
    public int getCount() {
        if (resultDatas == null){
            return 0;
        }
        return resultDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mConext).inflate(R.layout.item_for_my_push,null);
            viewHolder.titleText = view.findViewById(R.id.push_title_text);
            viewHolder.contentEdtext = view.findViewById(R.id.push_content_edtext);
            viewHolder.layout = view.findViewById(R.id.mypush_list_item_layout);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.titleText.setText(resultDatas.get(position).getIdeaTitle());
        viewHolder.contentEdtext.setText(resultDatas.get(position).getIdeaContent());

        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myPushActivity.ItemOnClick(position);
            }
        });
        return view;
    }

    class ViewHolder{
        TextView titleText;
        TextView contentEdtext;
        LinearLayout layout;
    }
}
