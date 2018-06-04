package com.hnf.guet.comhnfpatent.presenter;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

public class HomePresenter extends BasePresenter {
    private static final  String TAG = "HomePresenter";
    private Context mContext;
    private HomeActivity homeActivity;
    public Call<ResponeModelInfo> resultData;
    private AlertDialog.Builder mBuilder;
    private NotificationManager notificationManager;



    public HomePresenter(Context context, HomeActivity activity) {
        super(context);
        homeActivity = activity;
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    public void queryUserInfoList(String sToken,String acountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("acountName",acountName);
        hashMap.put("token",sToken);
        resultData = mHttpService.getUserInfomation(hashMap);
        homeActivity.showLoading("正在加载数据……");
        resultData.enqueue(mCallback);

    }


    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.e(TAG,"在homepresenter查询成功:"+data.getResult().getUserInfoList().size());
        homeActivity.queryUserInfoListSuccess(data.getResult().getUserInfoList());
    }

    @Override
    protected void onFaiure(ResponeModelInfo data) {
        homeActivity.dismissLoading();
        LogUtils.e(TAG,"homePresenter查询失败:"+data.getResultMsg());
        homeActivity.mIvError.setVisibility(View.VISIBLE);
        homeActivity.printn(data.getResultMsg());
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        LogUtils.e(TAG,"更新失败");
    }


    public void getIdeasInformation() {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token", MyApplication.sToken);
        resultData = mHttpService.queryAllIdeaInfomation(hashMap);
        homeActivity.showLoading("正在加载数据……");
        resultData.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        if (body.getResult() != null){
            LogUtils.e(TAG,"需求列表长度："+body.getResult().getUserInfoList().size());
            homeActivity.queryUserInfoListSuccess(body.getResult().getUserInfoList());
        }

    }

}
