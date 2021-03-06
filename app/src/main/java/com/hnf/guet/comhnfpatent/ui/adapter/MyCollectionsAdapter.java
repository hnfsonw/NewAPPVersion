package com.hnf.guet.comhnfpatent.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.ui.activity.MyCollectionsActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyCollectionsAdapter extends BaseAdapter{
    private static final String TAG = "MyCollectionsAdapter";
    private Context mContext;
    private MyCollectionsActivity mActivity;
    private List<ResultBean> resultDatas;

    public MyCollectionsAdapter(Context context, MyCollectionsActivity activity, List<ResultBean> resultList) {
        mContext = context;
        mActivity = activity;
        resultDatas = resultList;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_for_homefragment,null);
            viewHolder.headerImg = view.findViewById(R.id.home_header_img);
            viewHolder.nickNameTx = view.findViewById(R.id.home_nick_name_text);
            viewHolder.jobText = view.findViewById(R.id.home_job_text);
            viewHolder.workExpirenceText = view.findViewById(R.id.home_job_exprience_text);
            viewHolder.goodAtText = view.findViewById(R.id.home_good_at_text);
            viewHolder.listLayout = view.findViewById(R.id.home_list_item_layout);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.headerImg.setTag(resultDatas.get(position).getImgUrl());
        viewHolder.headerImg.setImageResource(R.drawable.loadling);

        if (resultDatas.get(position).getImgUrl() == null){
            viewHolder.headerImg.setImageResource(R.drawable.loadling); //如果url为空，这里显示默认的头像
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = getBitmap(resultDatas.get(position).getImgUrl());
                    viewHolder.headerImg.post(new Runnable() {
                        @Override
                        public void run() {
                            if (viewHolder.headerImg.getTag() != null && viewHolder.headerImg.getTag().equals(resultDatas.get(position).getImgUrl())){
                                viewHolder.headerImg.setImageBitmap(bitmap);
                            }
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG,"bitmap转换失败"+e);
                }
            }
        }).start();

        viewHolder.nickNameTx.setText(resultDatas.get(position).getNickName());
        viewHolder.jobText.setText(resultDatas.get(position).getJob());
        viewHolder.workExpirenceText.setText(resultDatas.get(position).getWorkExprience());
        viewHolder.goodAtText.setText(resultDatas.get(position).getGoodAt());

        viewHolder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.ItemOnClick(position);
            }
        });
        return view;
    }


    /**
     * 根据url地址加载网络头像
     * @param imgUrl
     * @return
     * @throws IOException
     */
    private Bitmap getBitmap(String imgUrl) throws IOException {
        LogUtils.e(TAG,"头像url:"+imgUrl);
        URL url = new URL(imgUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if (conn.getResponseCode() == 200){
            InputStream inputStream = null;
            try {
                inputStream = conn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }

        return null;
    }

    class ViewHolder{
        CircleImageView headerImg;
        TextView nickNameTx;
        TextView jobText;
        TextView workExpirenceText;
        TextView goodAtText;
        LinearLayout listLayout;
    }
}
