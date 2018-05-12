package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.ForgetAndResetPassActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.MD5Utils;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

public class ForgetAndResetPassPresenter extends BasePresenter {
    private static final String TAG = "ForgetAndResetPassPresenter";

    private ForgetAndResetPassActivity mActivity;
    public Call<ResponeModelInfo> codeResult;
    public Call<ResponeModelInfo> findPassResult;
    private Context mContext;
    protected int time = 120;
    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    private boolean flag = true;

    public ForgetAndResetPassPresenter(Context context,ForgetAndResetPassActivity activity) {
        super(context);
        mActivity = activity;
        mContext = context;
    }

    /**
     * 重置失败返回的消息
     * @param body
     */
    @Override
    protected void onError(ResponeModelInfo body) {
        mActivity.failureReasion(body.getResultMsg());
    }

    /**
     * 重置成功
     * @param body
     */
    @Override
    protected void onSuccess(ResponeModelInfo body) {
        mActivity.findPassSuccess();
    }

    /**
     * 验证注册的帐号信息
     */
    public void findPassAction(String userName,String code,String passWord,String confrimPassword) {
        if (TextUtils.isEmpty(code)){
            mActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(passWord)){
            mActivity.passWordIsEmpty();
            return;
        }

        if (!UserUtil.judgePassword(passWord)){
            mActivity.passwordInconformity();
            return;
        }

        if (passWord.equals(confrimPassword) && UserUtil.judgePassword(passWord) ){
            String md5PassWord = MD5Utils.getInstance().getMD5String(passWord);
            LogUtils.d(TAG, "md5PassWord: " + md5PassWord);
            toPassBack(userName,code,md5PassWord);
        }else{
            mActivity.passwordError();
        }
    }

    /**
     * 请求网路重置密码
     * @param userName
     * @param code
     * @param md5PassWord
     */
    private void toPassBack(String userName, String code, String md5PassWord) {
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", userName);
        paramsMap.put("password", md5PassWord);
        paramsMap.put("code", code);
        LogUtils.i(TAG, "注册请求网络: "+String.valueOf(paramsMap) );
        findPassResult = mHttpService.findPassWordBackInterface(paramsMap);
        mActivity.showLoading(mContext.getString(R.string.dialogMessage));
        findPassResult.enqueue(mCallback2);
    }

        /**
         * 验证手机号码
         * @param phone
         */
        public void checkCode(String phone){
            if (TextUtils.isEmpty(phone)){
                mActivity.phoneIsEmpty();
                return;
            }
            if (!UserUtil.judgePhoneNums(phone)){
                mActivity.phoneError();
                return;
            }
            sendCheckCode(phone);
        }

    /**
     *请求网络发送短信验证码
     */
    private void sendCheckCode(String phone) {
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile",phone);
        LogUtils.i(TAG,"发送短信验证码"+String.valueOf(params));
        codeResult = mHttpService.sendCheckCodeInterface(params);
        mActivity.showLoading(mContext.getString(R.string.dialogMessage));
        codeResult.enqueue(mCallback);
    }



    /**
     * 验证码发送成功的回调
     * @param data
     */
    @Override
    protected void parserJson(ResponeModelInfo data) {
        uptime();
        mActivity.dismissLoading();
        mActivity.CheckCode();//通知视图层，验证码发sing成功。
    }

    /**
     * 验证码发送失败的回调
     * @param data
     */
    @Override
    protected void onFaiure(ResponeModelInfo data) {
        LogUtils.d("验证码回调失败:"+data.getResultMsg());
        mActivity.dismissLoading();
        mActivity.error(data.getResultMsg());
    }

    private void uptime() {
        mActivity.mTvSendCode.setText(time+ MyApplication.sInstance.getString(R.string.Second));
        mActivity.mTvSendCode.setEnabled(false);
        new Thread(){
            @Override
            public void run() {
                for (; time > 0; time--) {
                    SystemClock.sleep(1000);
                    mHandler.sendEmptyMessage(TIME_MINUS);
                }
                mHandler.sendEmptyMessage(TIME_IS_OUT);
            }
        }.start();
    }


    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MINUS:
                    mActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mActivity.mTvSendCode.setText(MyApplication.sInstance.getString(R.string.To_Resend));
                    time = 120;
                    mActivity.mTvSendCode.setEnabled(true);
                    flag = false;
                    break;
            }

        }
    };
}
