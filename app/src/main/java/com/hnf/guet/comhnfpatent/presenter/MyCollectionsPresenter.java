package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.MyCollectionsActivity;

import java.util.HashMap;

import retrofit2.Call;

public class MyCollectionsPresenter extends BasePresenter{
    private static final String TAG = "MyCollectionsPresenter";

    private MyCollectionsActivity mActivity;
    private Context mContext;
    private Call<ResponeModelInfo> mResultData;



    public MyCollectionsPresenter(Context context, MyCollectionsActivity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        mActivity.dismissLoading();
        mActivity.sendBackResultLists(data.getResult().getUserInfoList());
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        mActivity.dismissLoading();
        mActivity.printn(s.getResultMsg());
    }

    public void getUserInfomation(String sToken, String macountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("acountName",macountName);
        hashMap.put("token",sToken);
        mResultData = mHttpService.queryMyAllCollections(hashMap);
        mActivity.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback);
    }
}
