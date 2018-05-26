package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.PushGoodIdeaActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

public class PushGoodIdeaPresenter extends BasePresenter{

    private static final  String TAG = "PushGoodIdeaPresenter";

    private Context mContext;
    private PushGoodIdeaActivity myActivity;
    private Call<ResponeModelInfo> myCall;

    public PushGoodIdeaPresenter(Context context, PushGoodIdeaActivity activity) {
        super(context);
        mContext = context;
        myActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.e(TAG,"新需求信息插入成功");
        myActivity.succeed();
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
myActivity.inserFaiure(s.getResultMsg());
    }

    public void pushIdea(String sToken, long sAcountId, String title, String content) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("content",content);
        hashMap.put("token",sToken);
        hashMap.put("acountId",sAcountId);
        myCall = mHttpService.insertNewIdea(hashMap);
        myActivity.showLoading("");//显示加载圈
        myCall.enqueue(mCallback);
    }
}
