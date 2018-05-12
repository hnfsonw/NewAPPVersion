package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.nfc.Tag;
import android.text.TextUtils;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.myWedget.chatrow.Constant;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.ForgetActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

public class ForgetPasswordPresenter extends BasePresenter{
    private static final String TAG = "ForgetPasswordPresenter";

    private Context mContent;
    private ForgetActivity mForgetActivity;
    public Call<ResponeModelInfo> mCheckAcount;

    public ForgetPasswordPresenter(Context context, ForgetActivity forgetActivity) {
        super(context);
        mContent = context;
        mForgetActivity = forgetActivity;
    }


    /**
     * 验证手机号码是否已经注册
     * @param phone
     */
    public void checkAcount(String phone){
        if (TextUtils.isEmpty(phone)){
            mForgetActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)){
            mForgetActivity.phoneIsError();
            return;
        }

        HashMap<String,Object> params = new HashMap<>();
        params.put("mobile",phone);
        mCheckAcount = mHttpService.checkValidAcountInterface(params);
        mForgetActivity.showLoading(mContent.getString(R.string.dialogMessage));
        mCheckAcount.enqueue(mCallback);
    }

    /**
     * 验证成功的回调
     * @param data
     */
    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.d(TAG,"验证账号是否已注册返回的结果："+data.getResultMsg());
        mForgetActivity.onSuccess();
    }

    @Override
    protected void onDissms(String s) {
        LogUtils.d(TAG,"onDissms:"+s);
        mForgetActivity.dismissLoading();
        mForgetActivity.printn(mContent.getString(R.string.network_error));
    }

    @Override
    protected void onFaiure(ResponeModelInfo data) {
        mForgetActivity.onError(data.getResultMsg());
        LogUtils.d(TAG,"onFaile:"+data.getResultMsg());
    }
}
