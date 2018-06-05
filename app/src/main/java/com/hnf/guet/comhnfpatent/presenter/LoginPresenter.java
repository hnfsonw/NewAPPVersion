package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.LoginActivity;
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.MD5Utils;
import com.hnf.guet.comhnfpatent.util.SharedPreferencesUtils;
import com.hnf.guet.comhnfpatent.util.UserUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import java.util.HashMap;

import retrofit2.Call;

public class LoginPresenter extends BasePresenter {

    private static final String TAG = "LoginPresenter";

    private LoginActivity mLoginActivity;
    private Context mContext;
    private SharedPreferences mGlobalvariable;

    public Call<ResponeModelInfo> mLogin;
    private String mPhone;
    private String mMD5Password;
    private String mPassword;


    @Override
    protected void onDissms(String s) {
        mLoginActivity.dismissLoading();
        mLoginActivity.whyNotConnectd(s);
        mLoginActivity.printn(mContext.getString(R.string.network_error));
    }

    public LoginPresenter(Context context, LoginActivity loginActivity) {
        super(context);
        mContext = context;
        mLoginActivity = loginActivity;
        mGlobalvariable = mLoginActivity.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);

    }

    public void login(String phone,String password){
        if (TextUtils.isEmpty(phone)){
            mLoginActivity.phoneIsEmpty();
            return;
        }

        if (!UserUtil.checkPhone(phone)){
            mLoginActivity.phoneError();
            return;
        }

        if (!TextUtils.isEmpty(password)){
            startToLogin(phone,password);
        }else {
            mLoginActivity.passwordIsEmpty();
            return;
        }
    }

    /**
     * APP服务器登陆
     * @param phone
     * @param password
     */
    private void startToLogin(String phone, String password) {
        mMD5Password = MD5Utils.getInstance().getMD5String(password);
        mPassword = password;
        EMLogin(phone,mMD5Password,MyEMCallBack);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("mobile",phone);
        hashMap.put("password",mMD5Password);
        LogUtils.d("loginPresenter","请求网络接入："+String.valueOf(hashMap));
        mLogin = mHttpService.loginInterface(hashMap);
        mLoginActivity.showLoading(mContext.getString(R.string.Login_Loding));
        mLogin.enqueue(mCallback);//请求入队
    }


    /**
     * 环信服务器登录
     * @param Phone
     * @param md5Password
     * @param callBack
     */
    private void EMLogin(String Phone, String md5Password, EMCallBack callBack){
        mPhone = Phone;
        mMD5Password = md5Password;
        EMClient.getInstance().login(Phone,md5Password,callBack);
    }

    /**
     * 环信登录成功/失败的回调
     */
    EMCallBack MyEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            mLoginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //加载会话到内存
                    EMClient.getInstance().chatManager().loadAllConversations();
                    //登录成功后跳转
                    LogUtils.e("loginPresenter","环信已经登录成功了"+Thread.currentThread().getName());
                    mGlobalvariable.edit()
                            .putBoolean("login",true)
                            .apply();
                }
            });
        }

        @Override
        public void onError(final int i, final String s) {
            mLoginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoginActivity.dismissLoading();
                    LogUtils.e("loginPresenter","环信错误码error code:"+i+",meaasge:"+s);
                    /**
                     * 关于错误码可以参考官方api详细说明
                     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                     */
                    switch (i) {
                        // 网络异常 2
                        case EMError.NETWORK_ERROR:
                            Log.d(TAG, "run: " + "网络异常 2");
                            break;
                        // 用户认证失败，用户名或密码错误 202
                        case EMError.USER_AUTHENTICATION_FAILED:
                            Log.d(TAG, "run: " + "用户认证失败，用户名或密码错误 202");
                            break;
                        // 用户不存在 204
                        case EMError.USER_NOT_FOUND:
                            Log.d(TAG, "run: " + "用户不存在 204");
                            break;
                        // 无法访问到服务器 300
                        case EMError.SERVER_NOT_REACHABLE:
                            Log.d(TAG, "run: " + "无法访问到服务器");
                            // 调用退出成功，结束app
                            EMLogin(mPhone,mMD5Password,MyEMCallBack);
                            break;
                        // 等待服务器响应超时 301
                        case EMError.SERVER_TIMEOUT:
                            Log.d(TAG, "run: " + "等待服务器响应超时");
                            // 调用退出成功，结束app
                            EMLogin(mPhone,mMD5Password,MyEMCallBack);
                            break;
                        // 服务器繁忙 302
                        case EMError.SERVER_BUSY:
                            Log.d(TAG, "run: " + "服务器繁忙");
                            // 调用退出成功，结束app
                            EMLogin(mPhone,mMD5Password,MyEMCallBack);
                            break;
                        // 未知 Server 异常 303 一般断网会出现这个错误
                        case EMError.SERVER_UNKNOWN_ERROR:
                            Log.d(TAG, "run: " + "未知 Server 异常 303 一般断网会出现这个错误");
                            EMLogin(mPhone,mMD5Password,MyEMCallBack);
                            break;

                        case EMError.USER_ALREADY_LOGIN:
                            Log.d(TAG, "200: " + "账号已登陆状态");
                            onExit();
                            break;
                        default:
                            //200 是已经登入 那么就让环信退出登录 在重新登录
                            LogUtils.e(TAG,"run " + "default" );
                            onExit();
                            break;
                    }
                }
            });
        }

        @Override
        public void onProgress(int i, String s) {
            LogUtils.e(TAG,"环信登录中……………………");
        }
    };

    /**
     * 退出环信sdk
     */
    private void onExit() {
        // 调用sdk的退出登录方法，第一个参数表示是否解绑推送的token，没有使用推送或者被踢都要传false
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtils.i(TAG,"环信logout成功");
                EMLogin(mPhone,mMD5Password,MyEMCallBack);
            }

            @Override
            public void onError(int i, String s) {
                Log.i("LoginPresenter", "环信logout error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }



    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.i(TAG,"登录成功："+"token:"+data.getResult().getToken()
                +
                ",id:"+data.getResult().getAcountId()
                +
                ",acount: "+data.getResult().getAcountName()

                +",nick: "+data.getResult().getNickName()

                +",imgurl:"+data.getResult().getImgUrl()

                + ",phone: "+data.getResult().getPhone()
        );

        mGlobalvariable.edit()
                .putLong("acountId",data.getResult().getAcountId())
                .putString("token",data.getResult().getToken())
                .putString("acountName",data.getResult().getAcountName())
                .putString("nickName",data.getResult().getNickName())
                .putString("imgUrl",data.getResult().getImgUrl())
                .putString("phone",data.getResult().getPhone())
                .putString("job",data.getResult().getJob())
                .putString("acountType",String.valueOf(data.getResult().getAcountType()))
                .putString("workExprience",data.getResult().getWorkExprience())
                .putString("goodAt",data.getResult().getGoodAt())
                .putString("infomation",data.getResult().getInfomation())
                .apply();

        if (data.getResult().getImgUrl() != null){
            SharedPreferencesUtils.setParam(mContext,"imgUrl",data.getResult().getImgUrl());
        }
        SharedPreferencesUtils.setParam(mContext,"nickName",data.getResult().getNickName());


        //保存token
        MyApplication.sToken = data.getResult().getToken();
        MyApplication.sAcountId = data.getResult().getAcountId();
        mLoginActivity.loginSuccess();
    }


    /**
     * 登录失败
     * @param s
     */
    @Override
    protected void onFaiure(ResponeModelInfo s) {

        LogUtils.e(TAG,"登录失败："+s.getResultMsg());
        mLoginActivity.resultMsg(s.getResultMsg());
    }
}
