package com.hnf.guet.comhnfpatent.base;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.recevier.NetBroadcastReceiver;
import com.hnf.guet.comhnfpatent.util.ActivityManagerUtils;
import com.hnf.guet.comhnfpatent.util.FileUtils;
import com.hnf.guet.comhnfpatent.util.IOUtils;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.NetEvent;
import com.hnf.guet.comhnfpatent.myWedget.progress.HnfProgress;
import com.hnf.guet.comhnfpatent.util.StatusBarCompat;
import com.hnf.guet.comhnfpatent.util.ThreadUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import butterknife.ButterKnife;
/**
 * 描   述: 所以activity的基类,这里用来放置共同的方法
 */
public abstract class BaseActivity extends AppCompatActivity implements NetEvent {

    private static final String TAG = "BaseActivity";
    /**
     * 网络状态
     */
    private int netMobile;

    /**
     * 这里保存一个值用来判断网络是否经历了由断开到连接
     */
    private boolean isNetChanges;
    /**
     * 监控网络的广播
     */
    private NetBroadcastReceiver netBroadcastReceiver;


    protected static final String BABY = "baby";
    private ProgressDialog dialog;
    protected Toast mToast;
    public static final int REQUEST_CODE = 1;
    public static final String SERIALIZABLE = "Serializable";
    public static final String STRING = "String";
    public static final String MODE = "Mode";
    public static final int PHOTO_REQUEST_CAMERA = 1;// 拍照

    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择

    public static final int PHOTO_REQUEST_CUT = 3;// 结果

    public static final String PHOTO_FILE_NAME = "temp_photo.jpg";

    protected static final int RELATION = 4;//选择关系

    protected static final int EXIT = 5;//退出

    protected static final int DECI_CODE = 100;

    protected long time = 0;

    public static BaseActivity mContext;

    protected MyBaseActiviy_Broad oBaseActiviy_Broad;
    /** 记录当前时间 **/
    private long exitTime = 0;





    private IntentFilter mIntentFilter;
    private HnfProgress mProfress;
    private boolean mIsEMCLogin;
    private IntentFilter mFilter;

    private boolean hideBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setWindow();
        setContentView(getLayoutRes());
        //将Activity实例添加到AppManager的堆栈
        ActivityManagerUtils.getAppManager().addActivity(this);
        mContext = this;
        ButterKnife.bind(this);
        StatusBarCompat.compat(this);
        init();
        //动态注册广播
        if (oBaseActiviy_Broad == null){
            oBaseActiviy_Broad = new MyBaseActiviy_Broad();
        }
        if (mIntentFilter == null){
            mIntentFilter = new IntentFilter("drc.xxx.yyy.baseActivity");
        }
        registerReceiver(oBaseActiviy_Broad, mIntentFilter);

    }



    /**
     * 设置状态栏透明
     */
    private void setWindow() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(Color.TRANSPARENT);

//            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }


    /**
     * 对话框取消
     */
    public void dismiss() {

    }


    /**
     * 选择设备
     */
    public void selectEquipment() {

    }


    /**
     * 更新
     */
    public void upDate() {

    }


    /**
     * 取消更新
     */
    public void dismissVesion() {

    }


    public void watchOff() {

    }


    /**
     * 联系人拒绝
     */
    public void refused() {
        LogUtils.d(TAG,"联系人拒绝");
    }


    /**
     * 联系人同意
     */
    public void agreed() {
        LogUtils.d(TAG,"联系人同意");
    }




    /**
     * 设置底部dialog
     * @param i
     */
    public void setOptions(int i) {

    }


    //定义一个广播
    public class MyBaseActiviy_Broad extends BroadcastReceiver {
        public void onReceive(Context arg0, Intent intent) {
            //接收发送过来的广播内容
            int closeAll = intent.getIntExtra("closeAll", 0);
            if (closeAll == 1) {
                //销毁BaseActivity
                finish();
            }
        }
    }


    public void exitHomeActivity(){
        Intent intent = new Intent("jason.broadcast.action");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);//发送广播
    }


    //退出方法
    protected void exit() {
        if (System.currentTimeMillis() - time > 2000) {
            time = System.currentTimeMillis();
            printn(getString(R.string.exit_the_program));
        } else {
            Intent intent = new Intent("drc.xxx.yyy.baseActivity");
            intent.putExtra("closeAll", 1);
            sendBroadcast(intent);//发送广播
        }
    }


    protected void sendBroadcast(){
        Intent intent = new Intent("drc.xxx.yyy.baseActivity");
        intent.putExtra("closeAll", 1);
        sendBroadcast(intent);//发送广播
    }


    //申请定位权限
    public void toLocationPermission() {
        int checkSelfPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        //拒绝
        if (checkSelfPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.ACCESS_COARSE_LOCATION},100);

        }else if (checkSelfPermission == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经定位开启");
            locationSuccess();
        }
    }



    //申请语音权限
    public void toVoicepermissions() {
        int checkVoicepermissions= ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        //拒绝
        if (checkVoicepermissions == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.RECORD_AUDIO},101);

        }else if (checkVoicepermissions == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启语音权限");
            voiceSuccess();
        }
    }


    //申请相机权限
    public void toCanmeraPermissions(){
        int checkCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        //拒绝
        if (checkCameraPermission == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.CAMERA},102);

        }else if (checkCameraPermission == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启相机权限");
            cameraSuccess();
        }
    }


    //申请存储权限
    public void toSDmissions() {
        int checkSad= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        //拒绝
        if (checkSad == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE},103);
        }else if (checkSad == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启存储权限");
        }
    }

    public void toSDStore() {
        int checkSad= ContextCompat.checkSelfPermission(this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);
        //拒绝
        if (checkSad == PackageManager.PERMISSION_DENIED){
            //申请权限
            ActivityCompat.requestPermissions(this,
                    new String []{Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS},104);
        }else if (checkSad == PackageManager.PERMISSION_GRANTED){
            LogUtils.e(TAG,"已经开启存储权限");
        }
    }


    /**
     * 申请权限返回值
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.e(TAG,grantResults.length + "~~~~~~~");
        try {
            switch (requestCode){
                case 100:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"定位权限设置完毕");
                        locationSuccess();
                    }else {
                        LogUtils.e(TAG,"用户拒绝了定位权限");
                        locationError();
                    }
                    break;

                case 101:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"语音权限设置完毕");
                        voiceSuccess();
                    }else {
                        LogUtils.e(TAG,"语音权限被拒绝");
                        voiceError();
                    }
                    break;

                case 102:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"相机权限设置完毕");
                        cameraSuccess();
                    }else {
                        LogUtils.e(TAG,"相机权限被拒绝");
                        cameraError();
                    }
                    break;

                case 103:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"存储权限设置完毕");
                    }else {
                        LogUtils.e(TAG,"存储权限被拒绝");
                        grantedError();
                    }
                    break;

                case 104:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        LogUtils.e(TAG,"sd存储设置完毕");
                    }else {
                        LogUtils.e(TAG,"sd存储权限被拒绝");
                        grantedError();
                    }
                    break;
            }
        }catch (Exception e){
            LogUtils.d(TAG,"权限打开异常");
        }

    }


    /**
     * 定位权限被拒绝的通知
     */
    protected void locationError() {

    }


    /**
     * SD存储权限被拒绝
     */
    protected void grantedError() {

    }


    /**
     * 语音权限拒绝的通知
     */
    protected void voiceError() {

    }


    /**
     * 语音权限打开的通知
     */
    protected void voiceSuccess() {

    }


    /**
     * 定位权限打开
     */
    protected void locationSuccess() {

    }

    /**
     * 相机权限被拒绝
     */
    protected void cameraError() {

    }


    /**
     * 相机权限打开
     */
    protected void cameraSuccess() {

    }


    /**
     * 触发loading
     * @param message
     */
    public void showLoading(String message) {
        try {
            if (mProfress == null){
                mProfress = HnfProgress.create(this,mOnKeyListener)
                        .setStyle(HnfProgress.Style.SPIN_INDETERMINATE);
            }
            mProfress.show();
        }catch (Exception e){
            LogUtils.e(TAG,e.getMessage());
        }
    }


    /**
     * 取消loading
     */
    public void dismissLoading() {
        LogUtils.i(TAG,"取消loading");
        if (mProfress != null && mProfress.isShowing()) {
            mProfress.dismiss();
        }
    }


    /**
     * dialog监听
     */
    DialogInterface.OnKeyListener mOnKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                if ((System.currentTimeMillis() - exitTime) > 3000) {
                    printn(getString(R.string.dismiss));
                    exitTime = System.currentTimeMillis();
                } else {
                    dismissLoading();
                    dismissNewok();
                }
                return true;
            }
            return true;
        }
    };


    /**
     * 吐司show
     * @param message
     */
    public void printn(String message) {
        if (null == mToast) {
            mToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            // mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(message);
        }
        mToast.show();
    }


    public void toActivity(Class<?> toClass) {
        toActivity(0,toClass);
    }


    public void toActivity(int code,Class<?> toClass) {
        toActivity(code,toClass,null);
    }


    public void toActivity(int code,Class<?> toClass,String string){
        toActivity(code,toClass,string,null);
    }


    public void toActivity(int code ,Class<?> toClass, String string,String baby) {
        toActivity(code,toClass, string,baby, 0);
    }


    public void toActivity(Class<?> toClass, String string) {
        toActivity(toClass, string,null, 0,0);
    }


    protected void toActivity(Class<?> toClass,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    public void toActivity(Class<?> toClass, String string,String baby) {
        toActivity(toClass, string,baby, 0,0);
    }


    protected void toActivity(Class<?> toClass, String string, String baby,int mode,int flag) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);
        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    protected void toActivity(int REQUEST_CODE, Class<?> toClass,
                              String string,String baby, int mode) {
        Intent intent = new Intent(this, toClass);
        intent.putExtra(MODE, mode);

        if (string != null) {
            intent.putExtra(STRING, string);
        }

        if (baby != null){
            intent.putExtra(BABY,baby);
        }
        startActivityForResult(intent, REQUEST_CODE);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


    /**
     * 监听网络状态的回调
     * @param netMobile
     */
    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        isNetConnect();
    }


    /**
     * 当前的网络状态
     */
    private void isNetConnect() {
        switch (netMobile) {
            case 1://wifi
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"wifi");
                theNetwork();
                break;
            case 0://移动数据
                isNetChanges = true;
                Log.d(TAG, "isNetConnect: "+"移动数据");
                netWork4G();
                theNetwork();
                break;

            case -1://没有网络
                isNetChanges = false;
                Log.d(TAG, "isNetConnect: " +"没有网络");
                noNetwork();
                break;
        }

    }


    /**
     * 保存数据到本地
     *
     * @param imei
     * @param resJson
     */
    protected void saveData2Local(String imei, String resJson) {
        BufferedWriter writer = null;
        try {
            File cacheFile = getCacheFile(imei);

            writer = new BufferedWriter(new FileWriter(cacheFile));
            //写入第一行,当前时间,缓存的生成时间
            writer.write(System.currentTimeMillis() + "");

            //写入第二行
            writer.newLine();//换行
            writer.write(resJson);
            //打印一个日志
            LogUtils.e(TAG, "缓存数据到本地-->: "+ cacheFile.getAbsolutePath()+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(writer);
        }
    }


    /**
     * 保存数据到本地
     *
     * @param imei
     * @param resJson
     */
    protected void saveData2Local(String imei, ResponeModelInfo resJson) {
        LogUtils.e(TAG,"保存数据到本地");
        ObjectOutputStream oos = null;
        try {
            File cacheFile = getCacheFile(imei);

            oos = new ObjectOutputStream(new FileOutputStream(cacheFile));
            //写入当前时间
            oos.writeLong(System.currentTimeMillis());
            oos.writeObject(resJson);
            //打印一个日志
            LogUtils.e(TAG, "缓存数据到本地-->: "+ cacheFile.getAbsolutePath()+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG,e.getMessage());
        } finally {
            IOUtils.close(oos);
        }
    }


    /**
     * 得到缓存文件
     *
     * @param imei
     * @return
     */
    protected File getCacheFile(String imei) {
        String dir = FileUtils.getDir("cache");//sdcard/Android/data/包目录/bean
        File cacheFile = new File(dir, imei);
        return cacheFile;
    }


    /**
     * 保存数据到内存中
     *
     * @param imei
     * @param cacheData
     */
    protected void saveData2Mem(String imei, Object cacheData) {
        //缓存的key

        //存储结构
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();

        //开始存
        cacheMap.put(imei, cacheData);

        LogUtils.e(TAG, "缓存数据到内存了"+imei);
    }


    /**
     * 根据KEY 内存中删除
     * @param imei
     */
    protected void deleteData2Mem(String imei){
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();
        if (cacheMap.containsKey(imei)){
            cacheMap.remove(imei);
            LogUtils.e(TAG,"从内存清除");
        }
    }


    /**
     * 生成缓存的Key
     *
     * @param index
     * @return
     */
    public String generateCacheKey(int index) {
        return getInterfaceKey() + "." + index;
    }


    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    public String loadDataFromLocal(String imei,long timeout) {
        LogUtils.e(TAG,timeout+"");
        BufferedReader reader = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                reader = new BufferedReader(new FileReader(cacheFile));

                //缓存文件的第一行
                String firstLine = reader.readLine();

                long cacheInsertTime = Long.parseLong(firstLine);
                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

                if ((System.currentTimeMillis() - cacheInsertTime) < timeout) {
                    //有效缓存
                    String cacheData = reader.readLine();


                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(reader);
        }
        return null;
    }


    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    protected ResponeModelInfo loadDataFromLocalBean(String imei,long timeout) {
        ObjectInputStream ois = null;
        try {
            File cacheFile = getCacheFile(imei);
            LogUtils.e(TAG,cacheFile.exists()+"");

            if (cacheFile.exists()) {//存在
                //是否有效
                //读取缓存的生成时间-->缓存文件的第一行
                ois = new ObjectInputStream(new FileInputStream(cacheFile));

                //缓存文件的第一行
                Long cacheInsertTime = ois.readLong();

                LogUtils.e(TAG,System.currentTimeMillis()+"~~~" + cacheInsertTime+"~~~"+timeout);

                if ((System.currentTimeMillis() - cacheInsertTime) < timeout) {
                    //有效缓存
                    ResponeModelInfo cacheData = (ResponeModelInfo) ois.readObject();


                    //保存数据到内存中
                    saveData2Mem(imei, cacheData);

                    //解析
                    return cacheData ;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭流
            IOUtils.close(ois);
        }
        return null;
    }


    /**
     * 从内存加载数据
     * @param imei 缓存唯一标识
     * @return
     */
    protected Object loadDataFromMem(String imei) {
        Map<String, Object> cacheMap = MyApplication.sInstance.getCacheMap();
        if (cacheMap.containsKey(imei)){
            Object memData = cacheMap.get(imei);
            return memData;
        }
        return null;
    }



    protected String getInterfaceKey() {
        return null;
    }


    /**
     * 当前是移动网的通知
     */
    protected void netWork4G() {


    }


    /**
     * 有网络的通知
     */
    protected void theNetwork() {

    }


    /**
     * 没有网络的通知
     */
    protected void noNetwork() {

    }


    /**
     * dialog确定,让子类去实现
     */
    public void cancel() {

    }


    /**
     * 初始化layout
     * @return
     */
    protected abstract int getLayoutRes();


    /**
     * 初始化方法
     */
    protected abstract void init();


    /**
     * 强制取消网络加载,把网络队列清除
     */
    protected abstract void dismissNewok();


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //将Activity实例从AppManager的堆栈中移除
        ActivityManagerUtils.getAppManager().finishActivity(this);
        if (netBroadcastReceiver != null) {
            unregisterReceiver(netBroadcastReceiver);
        }
        if (oBaseActiviy_Broad != null){
            unregisterReceiver(oBaseActiviy_Broad);//注销广播
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (netBroadcastReceiver == null) {
            netBroadcastReceiver = new NetBroadcastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(netBroadcastReceiver, filter);
            /**
             * 设置监听
             */
            netBroadcastReceiver.setNetEvent(this);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
//        LogUtils.e(TAG,"base onResume  "+getRunningActivityName());
//        MobclickAgent.onPageStart(getRunningActivityName());
//        MobclickAgent.onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        LogUtils.e(TAG,"base onPause  "+getRunningActivityName());
//        MobclickAgent.onPageEnd(getRunningActivityName());
//        MobclickAgent.onPause(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.e(TAG,"onRestart");
    }


    public void chekEMCLogin() {
        //用来判断环信是否登入的状态
        if (EMClient.getInstance().isLoggedInBefore() == true && EMClient.getInstance().isConnected() == true){
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    mIsEMCLogin = true;
                    LogUtils.e(TAG,"环信已登入 "+" 环信已连接服务器" );
                }
            });
            return;
        }

        LogUtils.e(TAG,"mIsEMCLogin " + mIsEMCLogin);
        if (!mIsEMCLogin){
            if (MyApplication.getHandler() != null){
                MyApplication.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (!EMClient.getInstance().isLoggedInBefore()){
                            LogUtils.e(TAG,"环信还没登入 去登入 " + Thread.currentThread().getName());
                            //环信登入
                            emcLogin();
                            mIsEMCLogin = true;
                        }else {
                            if (!EMClient.getInstance().isConnected()){
                                LogUtils.e(TAG,"环信登入了,环信服务器没连接上");
                                emcLogin();
                                mIsEMCLogin = true;
                            }
                        }
                    }
                });
            }
        }

    }



    /**
     * 环信登入
     */
    private void emcLogin() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i(TAG, "退出成功");
                //环信登入
//                EMClient.getInstance().login(DevicesHomeActivity.sPhone, DevicesHomeActivity.sMd5Password, mEMCallBack);
            }

            @Override
            public void onError(int i, String s) {
                Log.i(TAG, "退出失败 " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
            }
        });
    }




    EMCallBack mEMCallBack = new EMCallBack() {
        @Override
        public void onSuccess() {
            // 加载所有会话到内存
            EMClient.getInstance().chatManager().loadAllConversations();
            // 加载所有群组到内存，如果使用了群组的话
            EMClient.getInstance().groupManager().loadAllGroups();
            // 登录成功跳转界面
            mIsEMCLogin = false;
            Log.d(TAG, "run: " + "环信已经登入了" + Thread.currentThread().getName());
        }

        @Override
        public void onError(int i, String s) {
            mIsEMCLogin = false;
        }

        @Override
        public void onProgress(int i, String s) {

        }
    };


    /**
     * finish 动画
     * @param activity
     */
    public void finishActivityByAnimation(Activity activity){
        activity.finish();
        activity.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
    }


//    private String getRunningActivityName(){
//        ActivityManagerUtils activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        String runningActivity = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
//        return runningActivity.substring(runningActivity.lastIndexOf(".")+1);
//    }

}