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
import com.hnf.guet.comhnfpatent.ui.activity.ModifyPasswordActivity;
import com.hnf.guet.comhnfpatent.util.MD5Utils;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

public class EditPasswordPresenter extends BasePresenter {

    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    private static final String TAG = "EditPasswordPresenter";
    private boolean flag = true;

    private ModifyPasswordActivity mModifyPasswordActivity;

    private Context mContext;
    public Call<ResponeModelInfo> mSendCheckCode;
    public Call<ResponeModelInfo> mEdit;

    public EditPasswordPresenter(Context context, ModifyPasswordActivity modifyPasswordActivity) {
        super(context);
        mContext = context;
        mModifyPasswordActivity = modifyPasswordActivity;
    }


    /**
     * 验证身份信息
     * @param phone 手机号码
     * @param code  验证码
     * @param password 密码
     * @param configPassword 确认密码
     */
    public void resetPassword(String phone, String code, String password, String configPassword) {
        if (TextUtils.isEmpty(phone)){
            mModifyPasswordActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)){
            mModifyPasswordActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)){
            mModifyPasswordActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(password)){
            mModifyPasswordActivity.passwordInEmpty();
            return;
        }

        if (!UserUtil.judgePassword(password)){
            mModifyPasswordActivity.passwordInconformity();
            return;
        }

        if (password.equals(configPassword) && UserUtil.judgePassword(password)){
            reset(phone,code,password);
        }else{
            mModifyPasswordActivity.passwordError();
        }

    }


    /**
     * 发送验证码
     */
    public void resetCode(String phone) {
        if (TextUtils.isEmpty(phone)) {
            mModifyPasswordActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.judgePhoneNums(phone)){
            mModifyPasswordActivity.phoneError();
            return;
        }

        sendCode(phone);
    }


    /**
     * 请求网络发送验证码
     * @param phone
     */
    private void sendCode(String phone) {
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile", phone);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(params));

        mSendCheckCode = mHttpService.resetPassWordInterface(params);
        mModifyPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage));
        mSendCheckCode.enqueue(mCallback);
    }


    /**
     * 请求网络 修改密码
     * @param phone
     * @param code
     * @param password
     */
    private void reset(String phone, String code, String password) {
        String md5Password = MD5Utils.getInstance().getMD5String(password);
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile", phone);
        params.put("password", md5Password);
        params.put("code", code);
        Log.d("BasePresenter", "sendCode: " + String.valueOf(params));

        mEdit = mHttpService.resetPassWordInterface(params);
        mModifyPasswordActivity.showLoading(mContext.getString(R.string.dialogMessage));
        mEdit.enqueue(mCallback2);

    }


    /**
     * 验证码发送成功的回调
     * @param data
     */
    @Override
    protected void parserJson(ResponeModelInfo data) {
        upTime();
        mModifyPasswordActivity.dismissLoading();

        //通知v层验证码发送成功
        mModifyPasswordActivity.CheckCode();
    }


    /**
     * 验证码80001失败的回调
     * @param data
     */
    @Override
    protected void onFaiure(ResponeModelInfo data) {
        Log.d(TAG, "onFaiure: " + data.getResultMsg());
        mModifyPasswordActivity.dismissLoading();
        mModifyPasswordActivity.error(data.getResultMsg());
    }


    /**
     * 找回密码成功的回调
     * @param body
     */
    @Override
    protected void onSuccess(ResponeModelInfo body) {
        Log.d(TAG, "onSuccess: " + body.getResultMsg());
        mModifyPasswordActivity.dismissLoading();
        mModifyPasswordActivity.editSuccess();

    }


    /**
     * 找回密码失败的回调
     * @param body
     */
    @Override
    protected void onError(ResponeModelInfo body) {
        Log.d(TAG, "onError: " + body.getResultMsg());
        mModifyPasswordActivity.editError(body.getResultMsg());
    }


    /**
     * 没连接服务器的回掉
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        mModifyPasswordActivity.dismissLoading();
        mModifyPasswordActivity.printn(mContext.getString(R.string.Network_Error));
    }


    /**
     * 验证码发送成功倒计时60秒
     */
    protected void upTime() {
        mModifyPasswordActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
        mModifyPasswordActivity.mTvSendCode.setEnabled(false);
        new Thread() {
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
                    mModifyPasswordActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mModifyPasswordActivity.mTvSendCode.setText(MyApplication.sInstance.getString(R.string.To_Resend));
                    time = 120;
                    mModifyPasswordActivity.mTvSendCode.setEnabled(true);
                    flag = false;
                    break;
            }

        }
    };
    protected int time = 120;


}
