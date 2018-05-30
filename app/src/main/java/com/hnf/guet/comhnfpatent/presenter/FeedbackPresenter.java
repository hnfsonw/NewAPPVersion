package com.hnf.guet.comhnfpatent.presenter;


import android.content.Context;


import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.FeedbackActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;

import retrofit2.Call;

public class FeedbackPresenter extends BasePresenter {
    private static final String TAG = "FeedbackActivity";
    private Context mContext;
    private FeedbackActivity mFeedbackActivity;
    public Call<ResponeModelInfo> mCall;

    public FeedbackPresenter(Context context, FeedbackActivity feedbackActivity) {
        super(context);
        mContext = context;
        mFeedbackActivity = feedbackActivity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.d(TAG,data.getResultMsg());
        mFeedbackActivity.submitSuccess();
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        LogUtils.d(TAG,s.getResultMsg());
        mFeedbackActivity.dismissLoading();
        mFeedbackActivity.printn(s.getResultMsg());
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.e(TAG,s);
        mFeedbackActivity.dismissLoading();
    }

    /**
     * 提交意见反馈
     * @param content
     * @param token
     * @param acountId
     */
    public void submit(String content, String token, long acountId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",token);
        hashMap.put("acountId",acountId);
        hashMap.put("content",content);
        mCall = mHttpService.updateFeelBackInterface(hashMap);
        mFeedbackActivity.showLoading("");
        mCall.enqueue(mCallback);

    }
}
