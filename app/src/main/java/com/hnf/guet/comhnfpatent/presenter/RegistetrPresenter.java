package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.RegisterActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.MD5Utils;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import java.util.HashMap;

import retrofit2.Call;

public class RegistetrPresenter extends BasePresenter{

    private static final String TAG = "RgisterPresenter";
    private RegisterActivity mRegisterActivity;
    private Context mContext;
    public Call<ResponeModelInfo> mSendCheckCode;
    public Call<ResponeModelInfo> mRegister;
    protected int time = 120;
    private static final int TIME_MINUS = -1;
    private static final int TIME_IS_OUT = 0;
    private boolean flag = true;
    private SharedPreferences mGlobalvariable;
    private String globalAcountName;


    public RegistetrPresenter(Context context, RegisterActivity registerActivity) {
        super(context);
        mContext = context;
        mRegisterActivity = registerActivity;
        mGlobalvariable = mRegisterActivity.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
    }

    /**
     * 验证手机号码
     * @param phone
     */
    public void checkCode(String phone){
        if (TextUtils.isEmpty(phone)){
            mRegisterActivity.phoneIsEmpty();
            return;
        }
        if (!UserUtil.judgePhoneNums(phone)){
            mRegisterActivity.phoneError();
            return;
        }
        sendCheckCode(phone);
    }

    /**
     * 验证注册的帐号信息
     */
    public void registerNumber(String userName,String code,String passWord,String confrimPassword) {
        if (TextUtils.isEmpty(userName)){
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(userName)){
            mRegisterActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)){
            mRegisterActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(passWord)){
            mRegisterActivity.passWordIsEmpty();
            return;
        }

        if (!UserUtil.judgePassword(passWord)){
            mRegisterActivity.passwordInconformity();
            return;
        }

        if (passWord.equals(confrimPassword) && UserUtil.judgePassword(passWord) ){
            String md5PassWord = MD5Utils.getInstance().getMD5String(passWord);
            Log.d(TAG, "md5PassWord: " + md5PassWord);
            toRegister(userName,code,md5PassWord);
        }else{
            mRegisterActivity.passwordError();
        }
    }

    /**
     *请求网络发送短信验证码
     */
    private void sendCheckCode(String phone) {
        HashMap<String,String> params = new HashMap<>();
        params.put("mobile",phone);
        LogUtils.i(TAG,"发送短信验证码"+String.valueOf(params));
        mSendCheckCode = mHttpService.sendCheckCodeInterface(params);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage));
        mSendCheckCode.enqueue(mCallback);
    }


    /**
     * 请求网路注册
     * @param userName
     * @param code
     * @param md5PassWord
     */
    private void toRegister(String userName, String code, String md5PassWord) {
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("mobile", userName);
        paramsMap.put("password", md5PassWord);
        paramsMap.put("code", code);
        paramsMap.put("acountType","1");
        Log.i(TAG, "注册请求网络: "+String.valueOf(paramsMap) );
        Log.d(TAG, "md5密码 toRegister: " + md5PassWord);
        mRegister = mHttpService.registerInterface(paramsMap);
        mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage));
        mRegister.enqueue(mCallback2);
    }

    /**
     * 验证码发送成功的回调
     * @param data
     */
    @Override
    protected void parserJson(ResponeModelInfo data) {
        uptime();
        mRegisterActivity.dismissLoading();
        mRegisterActivity.CheckCode();//通知视图层，验证码发sing成功。
    }

    /**
     * 验证码发送失败的回调
     * @param data
     */
    @Override
    protected void onFaiure(ResponeModelInfo data) {
        LogUtils.d("验证码回调失败:"+data.getResultMsg());
        mRegisterActivity.dismissLoading();
        mRegisterActivity.error(data.getResultMsg());
    }

    /**
     * 注册成功回调
     * @param body
     */
    @Override
    protected void onSuccess(ResponeModelInfo body) {
        ResultBean resultBean = body.getResult();
        LogUtils.d(TAG,"注册成功"+body.getResultMsg());
        if (body.getResult().getAcountType() == 1){
            mRegisterActivity.succeed();
        }else {
            mGlobalvariable.edit()
                    .putString("acountType","2")
                    .putString("acountName",globalAcountName)
                    .apply();
            mRegisterActivity.registerProfessSucceed();
        }

    }

    @Override
    protected void onError(ResponeModelInfo body) {
        LogUtils.d(TAG,"请求失败："+body.getResultMsg());
        mRegisterActivity.onError(body.getResultMsg());
    }

    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "网络错误onDissms: " + s);
        mRegisterActivity.dismissLoading();
        mRegisterActivity.printn(mContext.getString(R.string.Network_Error));
    }

    private void uptime() {
        mRegisterActivity.mTvSendCode.setText(time+ MyApplication.sInstance.getString(R.string.Second));
        mRegisterActivity.mTvSendCode.setEnabled(false);
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
                    mRegisterActivity.mTvSendCode.setText(time + MyApplication.sInstance.getString(R.string.Second));
                    break;

                case TIME_IS_OUT:
                    mRegisterActivity.mTvSendCode.setText(MyApplication.sInstance.getString(R.string.To_Resend));
                    time = 120;
                    mRegisterActivity.mTvSendCode.setEnabled(true);
                    flag = false;
                    break;
            }

        }
    };


    /**
     * 注册成为一个专业用户
     * @param mPhone
     * @param code
     * @param mPassword
     * @param confirmPassword
     */
    public void registerToBeProfess(String mPhone, String code, String mPassword, String confirmPassword) {
        if (TextUtils.isEmpty(mPhone)){
            mRegisterActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(mPhone)){
            mRegisterActivity.phoneError();
            return;
        }

        if (TextUtils.isEmpty(code)){
            mRegisterActivity.codeIsEmpty();
            return;
        }

        if (TextUtils.isEmpty(mPassword)){
            mRegisterActivity.passWordIsEmpty();
            return;
        }

        if (!UserUtil.judgePassword(mPassword)){
            mRegisterActivity.passwordInconformity();
            return;
        }

        if (!(mPassword.equals(confirmPassword) && UserUtil.judgePassword(mPassword)) ){
            mRegisterActivity.passwordError();
        }else{
            globalAcountName = mPhone;
            String md5PassWord = MD5Utils.getInstance().getMD5String(mPassword);
            HashMap<String,String> paramsMap = new HashMap<>();
            paramsMap.put("mobile", mPhone);
            paramsMap.put("password", md5PassWord);
            paramsMap.put("code", code);
            paramsMap.put("acountType","2");//1是普通用户，2是专业用户
            Log.d(TAG, "md5密码 toRegister: " + mPassword);
            mRegister = mHttpService.registerInterface(paramsMap);
            mRegisterActivity.showLoading(mContext.getString(R.string.dialogMessage));
            mRegister.enqueue(mCallback2);
        }
    }
}
