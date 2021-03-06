package com.hnf.guet.comhnfpatent.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.BuildConfig;
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.factory.HttpsFactroy;
import com.hnf.guet.comhnfpatent.factory.SSLHelper;
import com.hnf.guet.comhnfpatent.http.HttpService;
import com.hnf.guet.comhnfpatent.myWedget.chatrow.Constant;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hnf.guet.comhnfpatent.config.Constants;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.LoginActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 描   述: 数据请求的业务基类
 */
public abstract class BasePresenter {

    private static final String TAG = "BasePresenter";
    private final SharedPreferences mGlobalvariable;
    protected Retrofit mRetrofit;
    protected HttpService mHttpService;
    private HttpLoggingInterceptor mInterceptor;
    private Context mContext;


    public BasePresenter(Context context) {
        mGlobalvariable = context.getSharedPreferences("globalvariable", 0);
        mContext = context;
        OkHttpClient builder = getBuilder();
        mRetrofit = new Retrofit.Builder().
                baseUrl(Constants.HOST).
                addConverterFactory(GsonConverterFactory.create()).
                client(builder).
                build();
        mHttpService = mRetrofit.create(HttpService.class);
        //把所有接口都封装到service里面
    }


    /**
     * 获取okhttp配置
     * @return
     */
    private OkHttpClient getBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // log用拦截器
        mInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d(TAG, "http日志:"+message);
            }
        });
        mInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(mInterceptor)
                .connectTimeout(60, TimeUnit.SECONDS)//设置超时
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(SSLHelper.getSSLCertifcation(mContext))
                .hostnameVerifier(new UnSafeHostnameVerifier())
                .retryOnConnectionFailure(false);//错误重连
        return builder.build();
    }



    protected Callback mCallback = new Callback<ResponeModelInfo>() {
        @Override
        public void onResponse(Call<ResponeModelInfo> call, Response<ResponeModelInfo> response) {
            ResponeModelInfo body = response.body();
            if (body == null){
                return;
            }
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg());
            Log.e(TAG, "onResponse: " + body.getResultCode());
            //成功取到数据
            if (Constants.State80000 == state ) {
                parserJson(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001){
                    return;
                }
                if (!chekToken){
                    onFaiure(body);
                }
            }
        }


        @Override
        public void onFailure(Call<ResponeModelInfo> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器
            LogUtils.i(TAG,"解析失败："+t.getMessage());
            Toast.makeText(MyApplication.sInstance, MyApplication.sInstance.getString(R.string.Network_Error), Toast.LENGTH_SHORT).show();

        }
    };


    protected Callback<ResponeModelInfo> mCallback2 = new Callback<ResponeModelInfo>() {
        @Override
        public void onResponse(Call<ResponeModelInfo> call, Response<ResponeModelInfo> response) {
            ResponeModelInfo body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse2: " + body.getResultMsg() );
            Log.e(TAG, "onResponse2: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse2: " + body.getResultMsg() );
            Log.e(TAG, "onResponse2: " + body.getResultCode());
            if (Constants.State80000 == state){
                onSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001)
                    return;
                if (!chekToken)
                    onError(body);
            }

        }

        @Override
        public void onFailure(Call<ResponeModelInfo> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器
        }
    };


    protected Callback<ResponeModelInfo> mCallback3 = new Callback<ResponeModelInfo>() {
        @Override
        public void onResponse(Call<ResponeModelInfo> call, Response<ResponeModelInfo> response) {
            ResponeModelInfo body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                memberListSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (body.getErrorCode() == Constants.ERROR92001){
                    return;
                }
                if (!chekToken){
                    onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponeModelInfo> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };




    protected Callback<ResponeModelInfo> mCallback4 = new Callback<ResponeModelInfo>() {
        @Override
        public void onResponse(Call<ResponeModelInfo> call, Response<ResponeModelInfo> response) {
            ResponeModelInfo body = response.body();
            if (body == null){
                return;
            }
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            int state = body.getResultCode();
            Log.e(TAG, "onResponse: " + body.getResultMsg() );
            Log.e(TAG, "onResponse: " + body.getResultCode());
            if (Constants.State80000 == state){
                appCMDSuccess(body);
            }else if (Constants.State80001 == state){
                //验证Token是否失效
                LogUtils.e(TAG,"错误码 "+body.getErrorCode());
                boolean chekToken = chekToken(body);
                if (!chekToken){
                    onError(body);
                }

            }

        }

        @Override
        public void onFailure(Call<ResponeModelInfo> call, Throwable t) {
            onDissms(t.getMessage());
            //没连上服务器

        }
    };


    protected void appCMDSuccess(ResponeModelInfo body) {

    }


    protected void memberListSuccess(ResponeModelInfo body) {

    }


    protected void onDissms(String s) {

    }


    protected void onError(ResponeModelInfo body) {

    }


    protected void onSuccess(ResponeModelInfo body) {

    }


    protected abstract void parserJson(ResponeModelInfo data);


    protected abstract void onFaiure(ResponeModelInfo s);


    private boolean chekToken(ResponeModelInfo body) {
        if (body.getResultMsg().equals(mContext.getString(R.string.log))){
            MyApplication.sInUpdata = true;
            EMClient.getInstance().logout(false, new EMCallBack() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "logout success");
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "logout error " + i + " - " + s);
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
            boolean aBoolean = mGlobalvariable.getBoolean("login", false);
            if (aBoolean){
                Toast.makeText(mContext,mContext.getString(R.string.login_has_expired_please_login_again),Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            mGlobalvariable.edit().putBoolean("login", false).apply();
            mContext.startActivity(intent);
            return true;
        }
        return false;
    }


    private class UnSafeHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;//自行添加判断逻辑，true->Safe，false->unsafe
        }
    }
}
