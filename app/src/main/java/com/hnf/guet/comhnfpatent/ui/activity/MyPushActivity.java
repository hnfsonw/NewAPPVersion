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
import com.hnf.guet.comhnfpatent.model.bean.IdeaEntity;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.MyPushPresenter;
import com.hnf.guet.comhnfpatent.ui.adapter.MyPushAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyPushActivity extends BaseActivity {
    private static final String TAG = "MyPushActivity";

    @BindView(R.id.listview_my_push)
    ListView mListview;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    private SharedPreferences mGlobalvariable;
    private String acountId;
    private MyPushPresenter mPresenter;
    private MyPushAdapter myPushAdapter;
    private List<ResultBean> resultList;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_my_push;
    }

    @Override
    protected void init() {
        if (mPresenter == null){
            mPresenter = new MyPushPresenter(this,this);
        }
        mGlobalvariable = this.getSharedPreferences("globalvariable", Context.MODE_PRIVATE);
        acountId = String.valueOf(mGlobalvariable.getLong("acountId",14003));
        initView();
        initData();
    }

    private void initData() {
        if (myPushAdapter != null){
            myPushAdapter = new MyPushAdapter(this,this,resultList);
        }
        mListview.setAdapter(myPushAdapter);
        getDataOfMyPush(acountId);
    }

    private void getDataOfMyPush(String acountId) {
        if (mPresenter != null){
            mPresenter.getMyPushData(acountId);
        }
    }

    private void initView() {
        mTvTitle.setText("我的发布");
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
//        Intent myPushDetail = new
    }

    public void dataOfMyPushLists(List<ResultBean> ideaList) {
        myPushAdapter = new MyPushAdapter(this,this,ideaList);
        resultList =ideaList;
        mListview.setAdapter(myPushAdapter);
    }
}
