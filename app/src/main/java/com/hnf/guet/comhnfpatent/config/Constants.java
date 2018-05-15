package com.hnf.guet.comhnfpatent.config;

import android.os.Environment;

import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.util.LogUtils;

/**
 * Created by Administrator on 2018/3/21.
 * 一切网络请求的地址的常量设置
 */

public class Constants {

    //服务器请求地址
    public static final String HOST = "https://192.168.1.102:8443/guet.patent/api/";

    //注册
    public static final String REGISTER = "acountRegister";

    //登录
    public static final String ACCOUNTLOGIN = "acountLogin";

    //短信验证码
    public static final String SEND_CODE = "sendCheackCode";

    //忘记密码
    public static final String RESET_PASS_WORD = "resetPassword";

    //上传附件
    public static final String UPLOAD = "uploadFile";

    //验证手机号码有没有注册
    public static final String CHECKACOUNT = "checkAcount";

    //修改账户信息
    public static final String UPDATE_USER_ACOUNT_INFO = "updateAcount";

    //检查版本是否为最新版本
    public static final String CHECK_VERSION = "cheakVersion";

    //更新反馈意见
    public static final String UPDATE_FEEL_BACK = "writeOrUpdateOption";

    //找回密码
    public static final String FIND_PASSWORD_BACK = "findPwdBack";

    //获取专业用户的信息
    public static final String GET_USER_INFOMATION = "insertOrUpdateUserSkills";

    //验证通信令牌是否有效
    public static final String TOKEN_IS_STILL_WORK = "verificationToken";

    //天机我的收藏
    public static final String ADD_COLLECTION = "insertCollection";

    //取消我的收藏
    public static final String CANCLE_COLLECTION = "cancleCollection";

    //查询收藏
    public static final String COLLECTION_OR_NOR = "queryCollections";


    public static final int DEBUGLEVEL = LogUtils.LEVEL_ALL;
    public static final int StateSuccess = 0;//成功
    public static final int State20002 = 20002;//账户信息失效，请重新登录
    public final static String APP_ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + MyApplication.sInstance.getPackageName();
    public final static String DOWNLOAD_DIR = "/downlaod/";
    public static final int State80000 = 80000;//成功
    public static final int State80001 = 80001;//成功
    //Error 码
    public static final int ERROR92001 = 92001;//号码短信发送次数已达最大上限
}
