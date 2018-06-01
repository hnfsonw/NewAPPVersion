package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.MyPushActivity;

import java.util.HashMap;

import retrofit2.Call;

public class MyPushPresenter extends BasePresenter {

    private MyPushActivity mActivity;
    private Context mContext;
    private Call<ResponeModelInfo> mResultData;

    public MyPushPresenter(Context context, MyPushActivity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        mActivity.dismissLoading();
        mActivity.dataOfMyPushLists(data.getResult().getUserInfoList());
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        mActivity.dismissLoading();
        mActivity.printn(s.getResultMsg());
    }

    public void getMyPushData(String acountId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("acountId",acountId);
        hashMap.put("token", MyApplication.sToken);
        mResultData = mHttpService.queryMyAllIdea(hashMap);
        mActivity.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback);
    }
}
