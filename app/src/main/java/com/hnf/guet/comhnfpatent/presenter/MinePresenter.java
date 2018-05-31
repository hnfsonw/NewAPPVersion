package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.ui.fragment.MineFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UIUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class MinePresenter extends BasePresenter {
    private static final String TAG = "MinePresenter";
    private HomeActivity homeActivity;
    private Call<ResponeModelInfo> resultData;
    private MineFragment mineFragment;

    public MinePresenter(Context context,HomeActivity activity,MineFragment fragment) {
        super(context);
        homeActivity = activity;
        mineFragment = fragment;
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

    public void uploadHeadImage(final String headUrl, final String sToken, final long sAcountId) {
        homeActivity.showLoading("");
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (headUrl != null){
                    uploadImageByAcountId(headUrl,sToken,sAcountId);
                }
            }
        }.start();
    }

    private void uploadImageByAcountId(String headUrl, String sToken, long sAcountId) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token",sToken)
                .addFormDataPart("acountId",String.valueOf(sAcountId));
        File file = new File(headUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("File",file.getName(),fileBody);

        List<MultipartBody.Part> part = builder.build().parts();
        resultData = mHttpService.uploadheadImage(part);
        resultData.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        homeActivity.dismissLoading();
        mineFragment.setCompleted();
    }


    @Override
    protected void onError(ResponeModelInfo body) {
        homeActivity.dismissLoading();
        homeActivity.printn(body.getResultMsg());
    }
}
