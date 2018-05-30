package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.FindProfessPresenter;
import com.hnf.guet.comhnfpatent.ui.adapter.FindProfessAdapter;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FindProfessActivity extends BaseActivity {

    private static final String TAG = "FindProfessActivity";

    @BindView(R.id.listview_find_profess)
    ListView mListview;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.search_btn_findProfess)
    Button searchBtn;

    @BindView(R.id.search_editText)
    EditText searchText;

    private FindProfessAdapter findProfessAdapter;
    private List<ResultBean> resultList;
    private SharedPreferences mGlobalvariable;
    private String macountName;
    private ResultBean mResultBean;
    private FindProfessPresenter mpresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_find_profess;
    }

    @Override
    protected void init() {
        resultList = (List<ResultBean>) getIntent().getSerializableExtra("list");
        if (mpresenter == null){
            mpresenter = new FindProfessPresenter(this,this);
        }
        mGlobalvariable = this.getSharedPreferences("globalvariable", Context.MODE_PRIVATE);
        macountName = mGlobalvariable.getString("acountName","");
        initView();
        initData();
    }

    private void initData() {
        if (findProfessAdapter == null){
            findProfessAdapter = new FindProfessAdapter(this,this,resultList);
        }
        mListview.setAdapter(findProfessAdapter);
        if (mResultBean != null){
            getDataOfUserInfo(macountName);
        }

    }

    private void getDataOfUserInfo(String acountName) {
        if (mpresenter != null){
            mpresenter.getUserInfomation(MyApplication.sToken,acountName);
        }
    }

    private void initView() {
        mTvTitle.setText("咨询专家");
    }

    @OnClick({R.id.iv_back,R.id.search_btn_findProfess})
    public void onViewClicked(View view){
        String searchConent = searchText.getText().toString().trim();
        switch (view.getId()){
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.search_btn_findProfess:
                if (searchConent.length() == 0){
                    printn("请输入搜索内容");
                    return;
                }
                mpresenter.searchFilter(searchConent);
                break;
        }

    }



    @Override
    protected void dismissNewok() {

    }

    public void ItemOnClick(int position) {
        Intent persionIntent = new Intent(FindProfessActivity.this, TalentPersionActivity.class);
        persionIntent.putExtra("imgUrl",resultList.get(position).getImgUrl());
        persionIntent.putExtra("nickName",resultList.get(position).getNickName());
        persionIntent.putExtra("goodAt",resultList.get(position).getGoodAt());
        persionIntent.putExtra("information",resultList.get(position).getInfomation());
        persionIntent.putExtra("acountName",resultList.get(position).getAcountName());
        startActivity(persionIntent);
    }

    public void onUserInfomation(ResultBean result) {
        dismissLoading();
        mResultBean = result;
        LogUtils.i(TAG,"职业2："+mResultBean.getUserInfoList().get(0).getJob());
    }

    public void noUserInformation() {
        dismissLoading();
        mResultBean = new ResultBean();
        LogUtils.i(TAG,"获取信息失败");
    }

    /**
     * 返回查询数组
     * @param userInfoList
     */
    public void sendBackResultLists(List<ResultBean> userInfoList) {
        dismissLoading();
        findProfessAdapter = new FindProfessAdapter(this,this,userInfoList);
        mListview.setAdapter(findProfessAdapter);
    }
}
