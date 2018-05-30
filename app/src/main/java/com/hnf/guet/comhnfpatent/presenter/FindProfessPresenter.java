package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.FindProfessActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

public class FindProfessPresenter extends BasePresenter {
    private static final String TAG = "FindProfessPresenter";
    private Call<ResponeModelInfo> mResultData;
    private FindProfessActivity myActivity;

    public FindProfessPresenter(Context context, FindProfessActivity activity) {
        super(context);
        myActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        if (data.getResult() != null){
            LogUtils.e(TAG,"用户信息列表长度："+data.getResult().getUserInfoList().size());
            myActivity.onUserInfomation(data.getResult());
        }else {
            LogUtils.i(TAG,"result是空的");
            myActivity.noUserInformation();
        }
    }

    @Override
    protected void onFaiure(ResponeModelInfo data) {
        LogUtils.e(TAG,"加载数据失败："+data.getResultMsg());
        myActivity.dismissLoading();
    }

    public void getUserInfomation(String sToken, String acountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("acountName",acountName);
        hashMap.put("token",sToken);
        mResultData = mHttpService.getUserInfomation(hashMap);
        myActivity.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback);
    }

    /**
     * 根据用户的输入做查询
     * @param searchConent
     */
    public void searchFilter(String searchConent) {
        HashMap<String,Object> map = new HashMap<>();
        map.put("keyWords",searchConent);
        map.put("token", MyApplication.sToken);
        mResultData = mHttpService.queryProfessByKeywords(map);
        myActivity.showLoading("");
        mResultData.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        super.onSuccess(body);
        myActivity.sendBackResultLists(body.getResult().getUserInfoList());
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        super.onError(body);
        myActivity.dismissLoading();
        myActivity.printn(body.getResultMsg());
    }
}
