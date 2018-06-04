package com.hnf.guet.comhnfpatent.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseFragment;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.HomeFragmentPresenter;
import com.hnf.guet.comhnfpatent.ui.activity.FindProfessActivity;
import com.hnf.guet.comhnfpatent.ui.activity.PushGoodIdeaActivity;
import com.hnf.guet.comhnfpatent.ui.activity.TalentPersionActivity;
import com.hnf.guet.comhnfpatent.ui.adapter.HomeFragmrntAdapter;
import com.hnf.guet.comhnfpatent.ui.adapter.HomeFragmrntBBBAdapter;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";

    private static final String ATG = "HomeFragment";
    private ImageView circlePatentImg,ideaImg,askProfesserImg;
    private HomeFragmentPresenter homeFragmentPresenter;
    private HomeFragmrntAdapter homeFragmrntAdapter;
    private HomeFragmrntBBBAdapter homeBAdapter;
    private List<ResultBean> resultList;
    private ListView mListview;
    private SharedPreferences mGlobalvariable;
    private String macountName;
    private ResultBean mResultBean;
    public String acountType;
    private FrameLayout frameLayout;

    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,null);
        circlePatentImg = view.findViewById(R.id.aaa_img);
        ideaImg = view.findViewById(R.id.bbb_img);
        askProfesserImg = view.findViewById(R.id.ccc_img);
        mListview = view.findViewById(R.id.list_view_home_fragment);
        frameLayout = view.findViewById(R.id.hide_layout);
        return view;
    }

    @Override
    protected void init() {
        mGlobalvariable = getActivity().getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
        macountName = mGlobalvariable.getString("acountName","");
        acountType = mGlobalvariable.getString("acountType","");
        if (homeFragmentPresenter == null){
            homeFragmentPresenter = new HomeFragmentPresenter(getActivity(),this);
        }
        initView();
        initListener();
        initData();
    }

    private void initData() {
        if (acountType.equals("1")){
            if (homeFragmrntAdapter == null)
                homeFragmrntAdapter = new HomeFragmrntAdapter(getActivity(),this,resultList);
            mListview.setAdapter(homeFragmrntAdapter);
        }else {
            if (homeBAdapter == null){
                homeBAdapter = new HomeFragmrntBBBAdapter(getActivity(),this,resultList);
            }
            mListview.setAdapter(homeBAdapter);
        }


        if (mResultBean != null){
            getDataOfUserInfo(macountName);
        }

    }

    private void getDataOfUserInfo(String acountName) {
        if (homeFragmentPresenter != null){
            if (acountType.equals("1")){
                homeFragmentPresenter.getUserInfomation(MyApplication.sToken,acountName);
            }else {
                homeFragmentPresenter.getIdeasInformation(MyApplication.sToken,acountName);
            }
        }
    }


    private void initListener() {
        circlePatentImg.setOnClickListener(this);
        askProfesserImg.setOnClickListener(this);
        ideaImg.setOnClickListener(this);
    }



    private void initView() {
        if (acountType.equals("2")){
            frameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setData(List<ResultBean> data) {
        LogUtils.e(TAG,"在setOndata:"+data.size());
        resultList = data;
    }

    @Override
    protected void dismissNewok() {

    }

    public void setList(List<ResultBean> mResultLists) {
        resultList = mResultLists;
        LogUtils.e(TAG,"setList这里"+resultList.size());
        if (acountType.equals("2")){
            if (homeBAdapter != null){
                homeBAdapter.setData(resultList);
                homeBAdapter.notifyDataSetChanged();
            }
        }else {
            if (homeFragmrntAdapter != null){
                homeFragmrntAdapter.setData(resultList);
                homeFragmrntAdapter.notifyDataSetChanged();
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.aaa_img:

                LogUtils.i(TAG,"专利转让");
                break;
            case R.id.bbb_img:
                LogUtils.i(TAG,"发布需求");
                Intent pushIdae = new Intent(getActivity(), PushGoodIdeaActivity.class);
                startActivity(pushIdae);
                break;
            case R.id.ccc_img:
                LogUtils.i(TAG,"咨询专家");
                Intent findIntent = new Intent(getActivity(), FindProfessActivity.class);
                findIntent.putExtra("list",(Serializable) resultList);
                startActivity(findIntent);
                break;
        }

    }

    /**
     * 获取消息成功
     * @param result
     */
    public void onUserInfomation(ResultBean result) {
        dismissLoading();
        mResultBean = result;
    }

    /**
     * 获取信息失败
     */
    public void noUserInformation() {
        dismissLoading();
        mResultBean = new ResultBean();
        LogUtils.i(TAG,"获取信息失败");
    }


    /**
     * listView选项点击事件
     * @param position
     */
    public void ItemOnClick(int position) {
        Intent persionIntent = new Intent(getActivity(), TalentPersionActivity.class);
        persionIntent.putExtra("acountType","1");
        persionIntent.putExtra("imgUrl",resultList.get(position).getImgUrl());
        persionIntent.putExtra("nickName",resultList.get(position).getNickName());
        persionIntent.putExtra("goodAt",resultList.get(position).getGoodAt());
        persionIntent.putExtra("information",resultList.get(position).getInfomation());
        persionIntent.putExtra("acountName",resultList.get(position).getAcountName());
        startActivity(persionIntent);
    }

    /**
     * 专业用户界面listView的点击事件
     * @param position
     */
    public void ItemOnClickB(int position) {
        Intent persionIntent = new Intent(getActivity(), TalentPersionActivity.class);
        persionIntent.putExtra("acountType","2");
        persionIntent.putExtra("imgUrl",resultList.get(position).getImgUrl());
        persionIntent.putExtra("nickName",resultList.get(position).getNickName());
        persionIntent.putExtra("title",resultList.get(position).getIdeaTitle());
        persionIntent.putExtra("content",resultList.get(position).getIdeaContent());
        persionIntent.putExtra("acountName",resultList.get(position).getAcountName());
        persionIntent.putExtra("ideaImage",resultList.get(position).getIdeaImage());
        startActivity(persionIntent);
    }
}
