package com.hnf.guet.comhnfpatent.presenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.download.DownloadApi;
import com.hnf.guet.comhnfpatent.download.DownloadProgressHandler;
import com.hnf.guet.comhnfpatent.download.ProgressHelper;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.SplashActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * Created by Administrator on 2018/3/26.
 * 欢迎页面的业务逻辑，用来做初始化
 */

public class SplashPresenter extends BasePresenter{
    private static final String TAG = "SplashPresenter";
    private SplashActivity splashActivity;
    private boolean mBoolean;
    private SharedPreferences mGlobalvariable;
    private Context mContext;
    public Call<ResponseBody> mDownloadCall;
    private File mFile;
    public Call<ResponeModelInfo> resutData;

    public SplashPresenter(Context context, SplashActivity splashActivity){
        super(context);
        mContext = context;
        this.splashActivity = splashActivity;
        mGlobalvariable = this.splashActivity.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
    }


    /**
     * 检查app是否为最新版本
     * @param data
     */
    @Override
    protected void parserJson(ResponeModelInfo data) {
        Log.d(TAG,"paresjson:"+data.getResultMsg());
        splashActivity.chekVersionSuccess(data);
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        Log.d(TAG, "onFaiure: " + s.getResultMsg());
        splashActivity.initView();
    }

    /**
     * 没连接上服务器
     * @param s
     */
    @Override
    protected void onDissms(String s) {
        Log.d(TAG, "onDissms: " + s);
        splashActivity.initView();
    }

    
    /**
     * 检查当前的版本号
     * @param token
     * @param s
     * @param versionCode
     */
    public void checkVersion(String token,String s,int versionCode){
        HashMap<String,Object> paramMap = new HashMap<>();
        paramMap.put("token",token);
        paramMap.put("version",s);
        paramMap.put("versionCode",versionCode);

        Log.e(TAG, "判断当前版本号: "+String.valueOf(paramMap) );
        Call<ResponeModelInfo> call = mHttpService.checkVersionInterface(paramMap);
        call.enqueue(mCallback);
    }


    /**
     * 下载APK;
     * @param url
     */
    public void download(String url) {
        //监听下载进度
        final ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setProgressNumberFormat("%1d KB/%2d KB");
        dialog.setMessage("正在下载，请稍后...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setCancelable(false);
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mDownloadCall != null) {
                    if (splashActivity.chekMandatory()){
                        mDownloadCall.cancel();
                        splashActivity.finishActivityByAnimation(splashActivity);
                    }else {
                        mDownloadCall.cancel();
                        splashActivity.initView();
                    }

                }
            }
        });
        dialog.show();

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://msoftdl.360.cn");
        OkHttpClient.Builder builder = ProgressHelper.addProgress(null);
        DownloadApi retrofit = retrofitBuilder
                .client(builder.build())
                .build().create(DownloadApi.class);

        ProgressHelper.setProgressHandler(new DownloadProgressHandler() {
            @Override
            protected void onProgress(long bytesRead, long contentLength, boolean done) {
                Log.e("是否在主线程中运行", String.valueOf(Looper.getMainLooper() == Looper.myLooper()));
                Log.e("onProgress",String.format("%d%% done\n",(100 * bytesRead) / contentLength));
                Log.e("done","--->" + String.valueOf(done));
                dialog.setMax((int) (contentLength/1024));
                dialog.setProgress((int) (bytesRead/1024));
                splashActivity.isDownload = true;
                if(done){
                    Log.d(TAG, "onProgress: " + "下载完成");
                    dialog.dismiss();
                    splashActivity.downloadComplete();
                }
            }
        });

        mDownloadCall = retrofit.downloadFileWithDynamicUrlSync(url);
        mDownloadCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    InputStream is = response.body().byteStream();
                    String dir = Environment.getExternalStorageDirectory() +"/my/"+
                            mContext.getPackageName()+"/apk/";
                    String path = dir + "updata.apk";
                    File fileDir = new File(dir);
                    mFile = new File(path);
                    if (fileDir.exists()){
                        fileDir.delete();
                    }
                    fileDir.mkdirs();
                    if (mFile.exists() && mFile.canWrite()){
                        mFile.delete();
                    }

                    Log.d(TAG, "111Environment: "
                            +Environment.getExternalStorageDirectory().getAbsolutePath());
                    Log.d(TAG, "222mFile.getAbsolutePath(): " + mFile.getAbsolutePath());
                    FileOutputStream fos = new FileOutputStream(mFile);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        fos.flush();
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (IOException e) {
                    LogUtils.e(TAG,"文件写入异常"+e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    //验证token是否还有效
//    public void verificationToken(String sToken) {
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("token",sToken);
//        resutData = mHttpService.VerificationToken(hashMap);
//        resutData.enqueue(mCallback2);
//    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        if (body.getResult().isTokenIsWord()){
            MyApplication.tokenIsWork = true;
        }else {
            MyApplication.tokenIsWork = false;
        }
    }
}
