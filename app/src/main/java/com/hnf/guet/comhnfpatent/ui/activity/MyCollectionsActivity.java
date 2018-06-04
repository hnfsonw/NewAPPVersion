package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.MyCollectionsPresenter;
import com.hnf.guet.comhnfpatent.ui.adapter.MyCollectionsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyCollectionsActivity extends BaseActivity {

    private static final String TAG = "MyCollectionsActivity";

    @BindView(R.id.listview_my_collections)
    ListView mListview;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private List<ResultBean> resultList;
    private SharedPreferences mGlobalvariable;
    private String macountName;
    private MyCollectionsAdapter myCollectionsAdapter;
    private MyCollectionsPresenter mPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_collections;
    }

    @Override
    protected void init() {
        if (mPresenter == null){
            mPresenter = new MyCollectionsPresenter(this,this);
        }
        mGlobalvariable = this.getSharedPreferences("globalvariable", Context.MODE_PRIVATE);
        macountName = mGlobalvariable.getString("acountName","");
        initView();
        initData();
    }

    private void initData() {
        if (myCollectionsAdapter == null){
            myCollectionsAdapter = new MyCollectionsAdapter(this,this,resultList);
        }
        mListview.setAdapter(myCollectionsAdapter);
        getDataOfUserInfo(macountName);
    }

    private void getDataOfUserInfo(String macountName) {
        if (mPresenter != null){
            mPresenter.getUserInfomation(MyApplication.sToken,macountName);
        }
    }

    private void initView() {
        mTvTitle.setText("我的收藏");
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void ItemOnClick(int position) {
        Intent persionIntent = new Intent(this, TalentPersionActivity.class);
        persionIntent.putExtra("imgUrl",resultList.get(position).getImgUrl());
        persionIntent.putExtra("nickName",resultList.get(position).getNickName());
        persionIntent.putExtra("goodAt",resultList.get(position).getGoodAt());
        persionIntent.putExtra("information",resultList.get(position).getInfomation());
        persionIntent.putExtra("acountName",resultList.get(position).getAcountName());
        startActivity(persionIntent);
    }

    public void sendBackResultLists(List<ResultBean> userInfoList) {
        myCollectionsAdapter = new MyCollectionsAdapter(this,this,userInfoList);
        resultList = userInfoList;
        mListview.setAdapter(myCollectionsAdapter);
    }
}
