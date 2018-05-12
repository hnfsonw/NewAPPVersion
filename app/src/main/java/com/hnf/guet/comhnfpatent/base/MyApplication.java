package com.hnf.guet.comhnfpatent.base;


import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.util.ServiceUtils;
import com.hyphenate.chat.EMOptions;
import com.hnf.guet.comhnfpatent.recevier.LiveService;
import com.hnf.guet.comhnfpatent.myWedget.chatrow.HxEaseuiHelper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.hyphenate.chat.EMGCMListenerService.TAG;

/**
 * 描   述: 程序的入口 初始化信息
 */
public class MyApplication extends MultiDexApplication {
    private static Handler sHandler;
    public static MyApplication sInstance;
    public static int sListSize = 0;
    public static String sToken;
    public static long sAcountId;
    public static int sDeviceId;
    public static boolean sHeadset;
    public static ResultBean sResult;
    public static String sUserAcountId = "";
    public static String sUserdeviceId = "";
    private SharedPreferences mGlobalVariablesp;
    public static boolean sInUpdata = false;

    //进程保活
    public static final int LIVE_JOB_ID = 0;
    private JobScheduler mJS;
    public static final String LIVE_SERVICE = "com.hnf.guet.comhnfpatent.recevier.LiveService";
    // 记录是否已经初始化
    private boolean isInit = false;
    //定义缓存的存储结构
    private static Map<String, Object> mCacheMap = new HashMap<>();

    public static Handler getHandler() {
        return sHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        sInstance = this;
        sHandler = new Handler();
        sResult = new ResultBean();
        mGlobalVariablesp = sInstance.getSharedPreferences("globalvariable", MODE_PRIVATE);
        sAcountId = mGlobalVariablesp.getLong("acountId", MODE_PRIVATE);
        sToken = mGlobalVariablesp.getString("token","");

        //判断当前系统版本是否5.0以上,开启服务进程
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            if(!ServiceUtils.isServiceRunning(sInstance, LIVE_SERVICE)){
                startService(new Intent(this, LiveService.class));
                scheduleJob(getJobInfo());
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }

        // 初始化环信SDK
        initEasemob();

        //初始化友盟
//        MobclickAgent.startWithConfigure(new MobclickAgent.UMAnalyticsConfig(this,
//                "594491364ad156566a0011cf","Huawei", MobclickAgent.EScenarioType. E_UM_NORMAL));
//        MobclickAgent.openActivityDurationTrack(false);

    }



    @SuppressLint("NewApi")
    public void scheduleJob(JobInfo info) {
//		LogUtils.i("xhd", "JobSchedule schedule JobInfo");
        mJS = (JobScheduler) sInstance.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        mJS.schedule(getJobInfo());
    }


    @SuppressLint("NewApi")
    public JobInfo getJobInfo() {
        JobInfo.Builder builder = new JobInfo.Builder(LIVE_JOB_ID,
                new ComponentName(sInstance, LiveService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE);//任务不需要网络
        builder.setPersisted(true);//设备重新启动时继续执行
        builder.setPeriodic(100);//该项工作多少秒重复（不定时执行，但该毫秒之内只会被执行一次）
        return builder.build();
    }


    public static Map<String, Object> getCacheMap() {
        return mCacheMap;
    }


    /**
     * 初始化环信SDK
     */
    private void initEasemob() {
        int pid = android.os.Process.myPid();//获取当前进程的id
        String processAppName = getProcessName(pid);//通过PID获取进程名

        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次

        //App默认的进程名就是包名
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }
        if (isInit) {
            return;
        }
        HxEaseuiHelper.getInstance().init(sInstance,initOptions());
//        initOptions();
        // 设置初始化已经完成
        isInit = true;
    }


    /**
     * 环信SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
//        options.setHuaweiPushAppId("100017619");
//        options.setMipushConfig(Constants.XIAOMI_ID,Constants.XIAOMI_KEY);
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        return options;
    }



    /**
     * 根据PID获取当前进程的名字
     * @param pID
     * @return
     */
    private String getProcessName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();//获取运行进程列表
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        //遍历所有的当前运行的进程
        while (i.hasNext()) {
            //获取进程的信息
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                //判断是否是当前进程对应info
                if (info.pid == pID) {
                    //获取当前进程的进程名
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

}
