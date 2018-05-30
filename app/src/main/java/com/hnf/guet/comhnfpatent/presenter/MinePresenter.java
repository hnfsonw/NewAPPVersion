package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.ui.fragment.MineFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UIUtils;

import java.util.HashMap;

import retrofit2.Call;

public class MinePresenter extends BasePresenter {
    private static final String TAG = "MinePresenter";
    private HomeActivity homeActivity;
    private Call<ResponeModelInfo> resultData;

    public MinePresenter(Context context,HomeActivity activity) {
        super(context);
        homeActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        homeActivity.dismissLoading();
       boolean result = data.getResult().isHasNewVersion();
       if (!result){
           homeActivity.printn("已经是最新版本");
       }
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        homeActivity.dismissLoading();
        homeActivity.printn(s.getResultMsg());
    }

    public void checkNewVersion() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("versionCode", UIUtils.getVersionCode());
        map.put("token", MyApplication.sToken);
        LogUtils.e(TAG,"versioncode------>"+UIUtils.getVersionCode());
        resultData = mHttpService.checkVersionInterface(map);
        homeActivity.showLoading("");
        resultData.enqueue(mCallback);
    }
}
