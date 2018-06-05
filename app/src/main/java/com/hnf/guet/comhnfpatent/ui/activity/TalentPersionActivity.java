package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.TalentPersionPresenter;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class TalentPersionActivity extends BaseActivity {
    private static final String TAG = "TalentPersionActivity";
    private String selectedAcountName;

    @BindView(R.id.iv_back_persion)
    LinearLayout IvBack;

    @BindView(R.id.head_img_persion)
    CircleImageView headImg;

    @BindView(R.id.nick_tv_person)
    TextView nickNameText;

    @BindView(R.id.persion_skills_text)
    TextView skillsText;

    @BindView(R.id.persion_information_text)
    TextView informationText;

    @BindView(R.id.touch_hime_btn)
    Button connectHimBtn;

    @BindView(R.id.colletion_btn)
    Button collectionBtn;

    @BindView(R.id.titlt_for_persion)
    TextView titleBarText;

    @BindView(R.id.skill_labal_text)
    TextView skillLabelText;

    @BindView(R.id.skill_content_text)
    TextView skillContentText;

    @BindView(R.id.grid_view_for_idea)
    GridView gridView;

    @BindView(R.id.big_imageview)
    ImageView bigImage;

    private TalentPersionPresenter talentPersionPresenter;
    private Context mContext;
    private String nickName;
    private String tagAcount;//点击的专家的账号
    private String acountType;
    private int columnWidth;
    private String[] ideaImageUrls;
    private ArrayList<String> urlLists;
    private GridAdapter gridAdapter;
    private SharedPreferences mGlobalvariable;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_talent_persion;
    }

    @Override
    protected void init() {
        mGlobalvariable = this.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
        talentPersionPresenter = new TalentPersionPresenter(this,this);
        final Intent intent = getIntent();
        LogUtils.e(TAG,"acountName:"+intent.getStringExtra("acountName"));
        selectedAcountName = intent.getStringExtra("acountName");
        nickName = intent.getStringExtra("nickName");
        tagAcount = intent.getStringExtra("acountName");
        acountType = intent.getStringExtra("acountType");
        if (acountType == null||acountType.equals("")){
            acountType = mGlobalvariable.getString("acountType","");
        }
        String url = intent.getStringExtra("ideaImage");
        if (url == null){
            gridView.setVisibility(View.GONE);
        }else {
            ideaImageUrls = url.split(",");
            urlLists = new ArrayList<>(Arrays.asList(ideaImageUrls));
            loadAdapter(urlLists);
            for (String a:ideaImageUrls){
                LogUtils.e("url--------->"+a);
            }
        }
        initview();
        initData();
        initListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap bitmap = getBitmap(intent.getStringExtra("imgUrl"));
                    headImg.post(new Runnable() {
                        @Override
                        public void run() {
                            headImg.setImageBitmap(bitmap);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    LogUtils.e(TAG,"bitmap转换失败"+e);
                }
            }
        }).start();
        nickNameText.setText(intent.getStringExtra("nickName"));
        if (acountType.equals("1")){
            skillsText.setText(intent.getStringExtra("goodAt"));
            informationText.setText(intent.getStringExtra("information"));
        }else {
            skillsText.setText(intent.getStringExtra("title"));
            informationText.setText(intent.getStringExtra("content"));
        }
        collectionBtn.setSelected(false);
    }

    private void initListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Glide.with(TalentPersionActivity.this).load(urlLists.get(i))
                        .into(bigImage);
            }
        });
    }


    private void loadAdapter(ArrayList<String> Lists) {
//        if (urlLists == null){
//            urlLists = new ArrayList<>();
//        }
        if (gridAdapter == null){
            gridAdapter = new GridAdapter(Lists);
            gridView.setAdapter(gridAdapter);
        }else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    private void initview() {
        if (acountType.equals("2")){
            titleBarText.setText("需求详情");
            skillLabelText.setText("需求标题");
            skillContentText.setText("需求内容");
        }
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 4 ? 4 : cols;
        gridView.setNumColumns(cols);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols-1)) / cols;
    }

    private void initData() {
        talentPersionPresenter.queryCollections(MyApplication.sToken,selectedAcountName);
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




    @OnClick({R.id.touch_hime_btn,R.id.colletion_btn,R.id.iv_back_persion})
    public void OnclickListener(View view){
        switch (view.getId()){
            case R.id.touch_hime_btn:
                LogUtils.e(TAG,"联系他");
                //从这里进入与专家的聊天界面
                Intent chatIntent = new Intent(TalentPersionActivity.this,ChatActivity.class);
                chatIntent.putExtra(EaseConstant.EXTRA_USER_ID,nickName);
                chatIntent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EMMessage.ChatType.Chat);
                startActivity(chatIntent);
                break;
            case R.id.colletion_btn:
                if (collectionBtn.isSelected() == false){
                    talentPersionPresenter.collectionAction(MyApplication.sToken,selectedAcountName);
                }else {
                    talentPersionPresenter.cancleCollection(MyApplication.sToken,selectedAcountName);
                }
                break;
            case R.id.iv_back_persion:
                finishActivityByAnimation(this);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void collectionSucced() {
        printn("已收藏~");
        collectionBtn.setText("取消收藏");
        collectionBtn.setSelected(true);
    }

    /**
     * 取消收藏成功
     */
    public void cancelSuccess() {
        printn("已取消收藏");
        collectionBtn.setText("收藏");
        collectionBtn.setSelected(false);
    }

    /**
     * 已经收藏
     */
    public void hadCollected() {
        collectionBtn.setText("取消收藏");
        collectionBtn.setSelected(true);
    }

    private class GridAdapter extends BaseAdapter{
        private ArrayList<String> listUrls;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
        }

        @Override
        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.item_image, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
                // 重置ImageView宽高
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(columnWidth, columnWidth);
                imageView.setLayoutParams(params);
            }else {
                imageView = (ImageView) convertView.getTag();
            }
            //框架里自带glide
            Glide.with(TalentPersionActivity.this)
                    .load(getItem(position))
                    .placeholder(R.mipmap.default_error)
                    .error(R.mipmap.default_error)
                    .centerCrop()
                    .crossFade()
                    .into(imageView);
            return convertView;
        }
    }

}
