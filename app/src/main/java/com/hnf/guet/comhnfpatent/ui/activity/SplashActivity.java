package com.hnf.guet.comhnfpatent.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.SplashPresenter;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.LoginActivity;
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.ui.view.VesionDialog;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UIUtils;
import com.hyphenate.chat.EMClient;

import java.io.File;


public class SplashActivity extends BaseActivity {
    private static final String TAG = "SplashActivity";
    private SplashPresenter mSplashPresenter;
    private SharedPreferences mGlobalVariable;
    private ResultBean mResult;
    private VesionDialog mVersionDialog;
    //
    private static final int FORCED_TO_UPGRADE = 1;
    private AlertDialog.Builder mDialog;
    private boolean isNetWork4G;
    private Handler mHandler;
    private boolean isCancelInstall;
    private boolean mIsLogin;
    private int mType;
    public boolean isDownload;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        mSplashPresenter = new SplashPresenter(this,this);
        mGlobalVariable = getSharedPreferences("globalvariable",MODE_PRIVATE);
        mIsLogin = mGlobalVariable.getBoolean("login",false);
        LogUtils.e(TAG,"环信是否已经登录过："+mIsLogin);
        mHandler = new Handler();
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()){
            //加载所有会话到内存
            EMClient.getInstance().chatManager().loadAllConversations();
        }

        if (mVersionDialog == null){
            mVersionDialog = new VesionDialog(this,this);
        }

        if (mIsLogin){
            mSplashPresenter.checkVersion(MyApplication.sToken,"v"+ UIUtils.getVersion(),UIUtils.getVersionCode());
        }else {
            MyApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    initView();
                }
            },1000);
        }
    }

    public void initView() {
        Log.d(TAG, "login: " + mIsLogin);
        //判断sdk是否登录过，并且没有退出或者被踢，否则跳转到登录界面；
        if (!EMClient.getInstance().isLoggedInBefore()){
            toActivity(LoginActivity.class);
            finishActivityByAnimation(this);
            return;
        }
        if (mIsLogin){
            exitHomeActivity();
            toActivity(HomeActivity.class);
            finishActivityByAnimation(this);
        }else{
            exitHomeActivity();
            toActivity(LoginActivity.class);
            finishActivityByAnimation(this);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            LogUtils.e(TAG,"返回键");
            initView();
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected void dismissNewok() {

    }

    @Override
    public void dismissVesion() {
        initView();
    }


    public void chekVersionSuccess(ResponeModelInfo data) {
        dismissLoading();
        if (data == null){
            return;
        }
        mResult = data.getResult();
        Boolean hasNewVesion = mResult.isHasNewVersion();
        mType = mResult.getType();
        String url = mResult.getUrl();
        if (hasNewVesion){
            //有版本更新
            Log.d(TAG, "chekVersionSuccess: " + url);
            mVersionDialog.show();
            if (chekMandatory()){
                mVersionDialog.mTvCancel.setVisibility(View.GONE);
                mVersionDialog.mLlLin.setVisibility(View.GONE);
            }
            mVersionDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == event.KEYCODE_BACK){
                        LogUtils.d(TAG,"~~返回");
                        return true;
                    }
                    return false;
                }
            });
            mVersionDialog.initUserName(mResult.getDesc());
        }else {
            LogUtils.d(TAG,"当前是最新版本");
            //当前是最新版本
            MyApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //判断下是否有未删除的安装包
                    File file = new File(getPath());
                    if (file.exists()){
                        file.delete();
                        LogUtils.e(TAG,"删除了已安装的文件");
                    }
                    initView();
                }
            },1000);
        }
    }


    public boolean chekMandatory(){
        if (mType == FORCED_TO_UPGRADE){
            return true;
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        //取消安装
        isCancelInstall = true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (chekMandatory()){
            //强制升级
        }else {
            if(isCancelInstall){
                if (isDownload){
                    //下载中
                }else {
                    initView();
                }
            }
        }

    }


    public String getPath(){
        String dir = Environment.getExternalStorageDirectory() +"/my/"+
                getPackageName()+"/apk/";
        return dir + "updata.apk";
    }


    /**
     * 下载完成
     */
    public void downloadComplete() {
        MyApplication.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {

                try {
                    File file = new File(getPath());
                    LogUtils.e(TAG,file.getAbsolutePath());
                    //指定启动的这个activity界面，放置到一个新的任务栈里面去。
                    //系统在安装APK 结束之后， 会把当前这个安装activity所处的任务栈直接清除掉。
                    //如果没有指定新的任务栈， 那么这个安装的activity将会放置到咱们应用的早前任务栈去。
                    Intent intent = new Intent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction("android.intent.action.VIEW");
                    //指定安装的文件路径
                    intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive" );
                    startActivity(intent);
                    isDownload = false;
                }catch (Exception e){
                    LogUtils.e(TAG,"下载完成安装异常"+e.getMessage());
                }
            }
        },1000);

    }


    @Override
    public void upDate() {
        File file = new File(getPath());
        if (file.exists()){
            downloadComplete();
            return;
        }
        LogUtils.d(TAG,"isNetWork4G " + isNetWork4G);
        if (isNetWork4G){
            //提示当前是移动网
            //监听下载进度
            mDialog = new AlertDialog.Builder(this);

            mDialog.setMessage(getString(R.string.mobile_data));
            mDialog.setCancelable(false);
            mDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (chekMandatory()){
                        dialog.dismiss();
                        finishActivityByAnimation(SplashActivity.this);
                    }else{
                        dialog.dismiss();
                        initView();
                    }



                }
            });
            mDialog.setPositiveButton(getString(R.string.local_tyrants_continued),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mSplashPresenter.download(mResult.getUrl());

                        }
                    });
            mDialog.show();
        }else {
            //当前是wifi
            mSplashPresenter.download(mResult.getUrl());
        }
    }


    /**
     * 当前网络是4G的通知
     */
    @Override
    protected void netWork4G() {
        isNetWork4G = true;
        LogUtils.e(TAG,"当前网络是4G");
    }

}
