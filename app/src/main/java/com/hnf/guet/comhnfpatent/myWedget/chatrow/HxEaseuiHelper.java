package com.hnf.guet.comhnfpatent.myWedget.chatrow;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;

import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.model.EaseNotifier;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;
import com.hnf.guet.comhnfpatent.util.ActivityManagerUtils;
import com.hnf.guet.comhnfpatent.util.FileUtils;
import com.hnf.guet.comhnfpatent.util.IOUtils;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.util.SharedPreferencesUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;



public class HxEaseuiHelper {

    private static HxEaseuiHelper sInstance = null;
    protected EMMessageListener mMessageListener = null;
    private Context mAppContext;
    private String mUsername;
    private EaseUI mEaseUI;
    private Map<String, EaseUser> mContactList;
    private Map<String, RobotUser> mRobotList;
    private DemoModel mDemoModel = null;

    private String TAG="ChatActivity";
    private boolean isVideoCalling ;

    public synchronized static HxEaseuiHelper getInstance() {
        if (sInstance == null) {
            sInstance = new HxEaseuiHelper();
        }
        return sInstance;
    }


    public void init(Context context,EMOptions options) {
        mDemoModel = new DemoModel(context);

        if (EaseUI.getInstance().init(context, options)) {
            mAppContext = context;
            //获取easeui实例
            mEaseUI = EaseUI.getInstance();
            //初始化easeui
            mEaseUI.init(mAppContext,options);
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(false);

            setEaseUIProviders();
        }
    }



    protected void setEaseUIProviders() {
        // set profile provider if you want mEaseUI to handle avatar and nickname
        mEaseUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                Log.e(TAG,"getUser.mUsername"+username);
                EaseUser userInfo = getUserInfo(username);
                if (userInfo != null){
                    LogUtils.e(TAG,"getUser.getNick"+userInfo.getNick());
                    LogUtils.e(TAG,"EaseUser  "+userInfo);
                }
                return userInfo;
            }
        });


        mEaseUI.setSettingsProvider(new EaseUI.EaseSettingsProvider() {
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return false;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return mDemoModel.getSettingMsgSound();
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return mDemoModel.getSettingMsgVibrate();
            }

            //设置扬声器
            @Override
            public boolean isSpeakerOpened() {
                LogUtils.e(TAG,"MyApplication.sHeadset " + MyApplication.sHeadset);
                return MyApplication.sHeadset;
            }
        });


        //set notification options, will use default if you don't set it
        mEaseUI.getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                String ticker = EaseCommonUtils.getMessageDigest(message, mAppContext);
                if(message.getType() == EMMessage.Type.TXT){
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                EaseUser user = getUserInfo(message.getFrom());
                if(user != null){
                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
//                        return String.format(mAppContext.getString(R.string.at_your_in_group), user.getNick());
                    }
                    return user.getNick() + ": " + ticker;
                }else{
                    if(EaseAtMessageHelper.get().isAtMeMsg(message)){
//                        return String.format(mAppContext.getString(R.string.at_your_in_group), message.getFrom());
                    }
                    return message.getFrom() + ": " + ticker;
                }
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Activity activity = ActivityManagerUtils.getAppManager().currentActivity();
//                Intent intent = new Intent(mAppContext, activity.getClass());
//                intent.putExtra("flags","flags");
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.setComponent(new ComponentName(mAppContext, activity.getClass()));//用ComponentName得到class对象
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);// 关键的一步，设置启动模式，两种情况
                return intent;
            }
        });
    }


    private EaseUser getUserInfo(String username){
        //获取 EaseUser实例, 这里从内存中读取
        //如果你是从服务器中读读取到的，最好在本地进行缓存
        EaseUser user = null;
        //如果用户是本人，就设置自己的头像
        Log.d(TAG, "如果用户是本人，getCurrentUser: " + EMClient.getInstance().getCurrentUser());
        Log.d(TAG, "如果用户是本人，mUsername: " + username);
        if(username.equals(EMClient.getInstance().getCurrentUser())){
            user = new EaseUser(username);
//            user.setAvatar(SharedPreferencesUtils.getParam(mAppContext, APPConfig.USER_HEAD_IMG,R.drawable.the_default));
            Object relation = SharedPreferencesUtils.getParam(mAppContext, "relation", "");
            Log.d(TAG, "设置自己的头像: " + relation);
            user.setNick((String) SharedPreferencesUtils.getParam(mAppContext, "relation", ""));
            return user;
        }

//        收到别人的消息，设置别人的头像
        if (mContactList != null && mContactList.containsKey(username)){
            user = mContactList.get(username);
            LogUtils.e(TAG,"收到别人的消息从内存拿 " + user);

        }else { //如果内存中没有，则将本地数据库中的取出到内存中
//            Log.d(TAG, "从本地拿设置别人的头像: mUsername" + mUsername);
//            if (mContactList != null){
//            LogUtils.e(TAG,"mContactList.size()"+mContactList.size());
//            LogUtils.e(TAG,"mContactList.get(\"17053334521\")"+mContactList.get("17053334521"));
//            }
//            mContactList = getContactList();
//            LogUtils.d("ChatActivitya",mContactList.size()+" 数据库size");
//            user = mContactList.get(mUsername);
            user = new EaseUser(username);
            LogUtils.d(TAG,username +MyApplication.sAcountId+"~~" );
            String name = loadDataFromLocal(username + MyApplication.sAcountId+"", Constant.PROTOCOL_TIMEOUT_MONTH);
            String imgUrl = loadDataFromLocal(username+MyApplication.sAcountId+"imgUrl",Constant.PROTOCOL_TIMEOUT_MONTH);

            LogUtils.d(TAG,"name   ~~~ "+name);
            LogUtils.d(TAG,"imgUrl !!! " + imgUrl);
            user.setNick(name);
            user.setAvatar(imgUrl);
        }


        return user;
    }




    public Map<String, RobotUser> getRobotList() {
        if (isLoggedIn() && mRobotList == null) {
            mRobotList = mDemoModel.getRobotList();
        }
        return mRobotList;
    }


    /**
     * get current user's id
     */
    public String getCurrentUsernName(){
        if(mUsername == null){
            mUsername = (String)SharedPreferencesUtils.getParam(mAppContext, Constant.USER_ID,"");
        }
        return mUsername;
    }


    /**
     *获取所有的联系人信息
     *
     * @return
     */
    public Map<String, EaseUser> getContactList() {
        if (isLoggedIn() && mContactList == null) {
            LogUtils.e(TAG,"从数据库取");
            mContactList = mDemoModel.getContactList();
        }
        // return a empty non-null object to avoid app crash
        if(mContactList == null){
            LogUtils.e(TAG,"Hashtable~~!!!!!");
            return new Hashtable<String, EaseUser>();
        }
        return mContactList;
    }


    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }


    public EaseNotifier getNotifier(){
        return mEaseUI.getNotifier();
    }


    /**
     * 去本地加载数据
     * @param imei 唯一标识
     */
    protected String loadDataFromLocal(String imei,long timeout) {
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


    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) mAppContext.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = mAppContext.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

}
