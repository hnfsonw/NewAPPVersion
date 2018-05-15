package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.TalentPersionActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

public class TalentPersionPresenter extends BasePresenter{

    private static final String TAG = "TalentPersionPresenter";
    private TalentPersionActivity talentPersionActivity;
    private Context mContext;
    private Call<ResponeModelInfo> resultData;
    private SharedPreferences mGlobalvariable;
    private String mAcountName;


    public TalentPersionPresenter(Context context, TalentPersionActivity activity) {
        super(context);
        mContext = context;
        talentPersionActivity = activity;
        mGlobalvariable = talentPersionActivity.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
        mAcountName = mGlobalvariable.getString("acountName","");
    }

    /**
     * 添加我的收藏
     * @param sToken
     * @param selectedAcountName
     */
    public void collectionAction(String sToken, String selectedAcountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",sToken);
        hashMap.put("collectible",selectedAcountName);
        hashMap.put("collector",mAcountName);
        resultData = mHttpService.addCollection(hashMap);
        talentPersionActivity.showLoading("^6^");
        resultData.enqueue(mCallback);
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        talentPersionActivity.dismissLoading();
        talentPersionActivity.collectionSucced();
        LogUtils.e(TAG,"添加收藏成功");
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        talentPersionActivity.dismissLoading();
        talentPersionActivity.printn("收藏失败");
        LogUtils.e(TAG,"添加收藏失败:"+s.getResultMsg());
    }


    /**
     * 取消收藏
     * @param selectedAcountName
     */
    public void cancleCollection(String sToken,String selectedAcountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",sToken);
        hashMap.put("collectible",selectedAcountName);
        hashMap.put("collector",mAcountName);
        resultData = mHttpService.canclecollection(hashMap);
        talentPersionActivity.showLoading("^6^");
        resultData.enqueue(mCallback2);
    }


    @Override
    protected void onSuccess(ResponeModelInfo body) {
        talentPersionActivity.dismissLoading();
        talentPersionActivity.cancelSuccess();
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        talentPersionActivity.dismissLoading();
        talentPersionActivity.printn(body.getResultMsg());
    }

    /**
     * 查询是否已经收藏
     * @param sToken
     * @param selectedAcountName
     */
    public void queryCollections(String sToken, String selectedAcountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",sToken);
        hashMap.put("collectible",selectedAcountName);
        hashMap.put("collector",mAcountName);
        resultData = mHttpService.queryCollectionOne(hashMap);
        talentPersionActivity.showLoading("^6^");
        resultData.enqueue(mCallback3);
    }

    @Override
    protected void memberListSuccess(ResponeModelInfo body) {
        talentPersionActivity.dismissLoading();
        if (body.getResult().isCollected()){
            talentPersionActivity.hadCollected();
        }
    }
}
