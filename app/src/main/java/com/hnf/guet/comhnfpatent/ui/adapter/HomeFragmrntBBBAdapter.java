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
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragmrntBBBAdapter extends BaseAdapter {
    private static final String TAG = "HomeFragmrntBBBAdapter";
    private Context mContext;
    private HomeFragment mHomeFragment;
    private List<ResultBean> resultDatas;

    public HomeFragmrntBBBAdapter(Context context,HomeFragment homeFragment,List<ResultBean> resutList){
        mContext  = context;
        mHomeFragment = homeFragment;
        resultDatas = resutList;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_for_homefragment_b,null);
            viewHolder.headerImg = view.findViewById(R.id.home_header_img_homefragment_b);
            viewHolder.titleTx = view.findViewById(R.id.home_nick_name_text_b);
            viewHolder.pusher = view.findViewById(R.id.home_job_text_b);
            viewHolder.pushContent = view.findViewById(R.id.push_content_edtext_of_homefragment);
            viewHolder.listLayout = view.findViewById(R.id.home_list_item_layout_b);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final ResultBean resultBeanB =  resultDatas.get(position);
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

        viewHolder.titleTx.setText(resultDatas.get(position).getIdeaTitle());
        viewHolder.pusher.setText(resultDatas.get(position).getNickName());
        viewHolder.pushContent.setText(resultDatas.get(position).getIdeaContent());
        LogUtils.e(TAG,"适配器位置："+position);

        viewHolder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mHomeFragment.ItemOnClickB(position);
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

    public void setData(List<ResultBean> responeList){
        resultDatas = responeList;
    }

    class ViewHolder{
        CircleImageView headerImg;
        TextView titleTx;//需求的标题
        TextView pusher;//发布者
        TextView pushContent;//需求的内容
        LinearLayout listLayout;
    }
}