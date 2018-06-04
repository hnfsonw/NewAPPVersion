package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.MyPushDetailPresenter;
import com.hnf.guet.comhnfpatent.ui.activity.ChatActivity;
import com.hnf.guet.comhnfpatent.ui.activity.TalentPersionActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class MyPushDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.persion_skills_text)
    TextView skillsText;

    @BindView(R.id.persion_information_text)
    TextView informationText;

    @BindView(R.id.touch_hime_btn)
    Button connectHimBtn;

    @BindView(R.id.grid_view_for_idea)
    GridView gridView;

    @BindView(R.id.big_imageview)
    ImageView bigImage;

    private MyPushDetailPresenter mPresenter;
    private int columnWidth;
    private String[] ideaImageUrls;
    private ArrayList<String> urlLists;
    private GridAdapter gridAdapter;
    private String tagAcount;//点击的专家的账号
    private String ideaId;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_push_detail;
    }

    @Override
    protected void init() {
        mPresenter = new MyPushDetailPresenter(this,this);
        final Intent intent = getIntent();
        tagAcount = intent.getStringExtra("acountName");
        String url = intent.getStringExtra("ideaImage");
        ideaId = intent.getStringExtra("ideaId");
        LogUtils.e("MyPushDetailActivity","需求的id-------->"+ideaId);
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

        skillsText.setText(intent.getStringExtra("title"));
        informationText.setText(intent.getStringExtra("content"));
    }

    private void initListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Glide.with(MyPushDetailActivity.this).load(urlLists.get(i))
                        .into(bigImage);
            }
        });
    }

    private void initData() {

    }

    private void initview() {
        mTvTitle.setText("需求详情");
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 4 ? 4 : cols;
        gridView.setNumColumns(cols);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        columnWidth = (screenWidth - columnSpace * (cols-1)) / cols;
    }

    private void loadAdapter(ArrayList<String> Lists) {
        if (gridAdapter == null){
            gridAdapter = new GridAdapter(Lists);
            gridView.setAdapter(gridAdapter);
        }else {
            gridAdapter.notifyDataSetChanged();
        }
    }

    @OnClick({R.id.touch_hime_btn,R.id.iv_back})
    public void OnclickListener(View view){
        switch (view.getId()){
            case R.id.touch_hime_btn:
                LogUtils.e("MyPushDetailActivity","myapplication acountid--------->"+MyApplication.sAcountId);
                mPresenter.delectIdeasByAcountId(MyApplication.sAcountId,ideaId);
                break;
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void delectDone() {
        printn("删除成功");
        finishActivityByAnimation(this);
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
            Glide.with(MyPushDetailActivity.this)
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
