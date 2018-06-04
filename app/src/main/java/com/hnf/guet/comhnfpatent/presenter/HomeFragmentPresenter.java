package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import java.util.HashMap;

import retrofit2.Call;

public class HomeFragmentPresenter extends BasePresenter{
    private static final String TAG = "HomeFragmentPresenter";
    private HomeFragment mHomeFragment;
    private Context mContext;
    private Call<ResponeModelInfo> mResultData;
    public ResultBean mResultBean;

    public HomeFragmentPresenter(Context context,HomeFragment homeFragment) {
        super(context);
        mContext = context;
        mHomeFragment = homeFragment;
    }

    public void getUserInfomation(String sToken,String acountname){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("acountName",acountname);
        hashMap.put("token",sToken);
        mResultData = mHttpService.getUserInfomation(hashMap);
        mHomeFragment.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback);
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        if (data.getResult() != null){
            LogUtils.e(TAG,"用户信息列表长度："+data.getResult().getUserInfoList().size());
            mHomeFragment.onUserInfomation(data.getResult());
        }else {
            LogUtils.i(TAG,"result是空的");
            mHomeFragment.noUserInformation();
        }
    }

    @Override
    protected void onFaiure(ResponeModelInfo data) {
        LogUtils.e(TAG,"加载数据失败："+data.getResultMsg());
        mHomeFragment.dismissLoading();
    }

    public void getIdeasInformation(String sToken, String acountName) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("token",sToken);
        mResultData = mHttpService.queryAllIdeaInfomation(hashMap);
        mHomeFragment.showLoading("正在加载数据……");
        mResultData.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        if (body.getResult() != null){
            LogUtils.e(TAG,"需求列表长度："+body.getResult().getUserInfoList().size());
            mHomeFragment.onUserInfomation(body.getResult());
        }else {
            LogUtils.i(TAG,"result是空的");
            mHomeFragment.noUserInformation();
        }
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        LogUtils.e(TAG,"--------------->"+body.getResultMsg());
    }
}
