package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.MyPushDetailActivity;

import java.util.HashMap;

import retrofit2.Call;

public class MyPushDetailPresenter extends BasePresenter {
    private MyPushDetailActivity mActivity;
    private Call<ResponeModelInfo> mResultData;

    public MyPushDetailPresenter(Context context, MyPushDetailActivity activity) {
        super(context);
        mActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        mActivity.dismissLoading();
        mActivity.delectDone();
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        mActivity.dismissLoading();
        mActivity.printn(s.getResultMsg());
    }

    public void delectIdeasByAcountId(long sAcountId,String ideaId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("ideaId",ideaId);
        hashMap.put("token", MyApplication.sToken);
        mResultData = mHttpService.delectIdeaByAcountId(hashMap);
        mActivity.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback);
    }
}
