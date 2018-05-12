package com.hnf.guet.comhnfpatent.http;

import com.hnf.guet.comhnfpatent.config.Constants;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Administrator on 2018/3/24.
 * 所有网络请求的接口
 */

public interface HttpService {

    /**
     * 注册的接口
     */
    @FormUrlEncoded
    @POST(Constants.REGISTER)
    Call<ResponeModelInfo> registerInterface(@FieldMap Map<String,String> fields);

    /**
     * 登录接口
     */
    @FormUrlEncoded
    @POST(Constants.ACCOUNTLOGIN)
    Call<ResponeModelInfo> loginInterface(@FieldMap Map<String,String> fields);

    /**
     * 短信验证码的接口
     */
    @FormUrlEncoded
    @POST(Constants.SEND_CODE)
    Call<ResponeModelInfo> sendCheckCodeInterface(@FieldMap Map<String,String> fields);


    /**
     * 忘记（重置）密码的接口
     */
    @FormUrlEncoded
    @POST(Constants.RESET_PASS_WORD)
    Call<ResponeModelInfo> resetPassWordInterface(@FieldMap Map<String,String> fields);

    /**
     * 找回密码的接口
     */
    @FormUrlEncoded
    @POST(Constants.FIND_PASSWORD_BACK)
    Call<ResponeModelInfo> findPassWordBackInterface(@FieldMap Map<String,String> fields);


    /**
     * 上传附件
     */
    @FormUrlEncoded
    @POST(Constants.UPLOAD)
    Call<ResponeModelInfo> uploadFilesInterface(@FieldMap Map<String,String> fields);

    /**
     * 验证手机号码有没有注册
     * @param fields
     */
    @FormUrlEncoded
    @POST(Constants.CHECKACOUNT)
    Call<ResponeModelInfo> checkValidAcountInterface(@FieldMap HashMap<String, Object> fields);

    /**
     * 修改账户信息
     */
    @FormUrlEncoded
    @POST(Constants.UPDATE_USER_ACOUNT_INFO)
    Call<ResponeModelInfo> updateUserInfoInterface(@FieldMap Map<String,String> fields);

    /**
     * 检查app版本是否为最新
     * @param fields
     */
    @FormUrlEncoded
    @POST(Constants.CHECK_VERSION)
    Call<ResponeModelInfo> checkVersionInterface(@FieldMap HashMap<String, Object> fields);

    /**
     * 更新反馈的意见
     * @param fields
     */
    @FormUrlEncoded
    @POST(Constants.UPDATE_FEEL_BACK)
    Call<ResponeModelInfo> updateFeelBackInterface(@FieldMap HashMap<String, Object> fields);


}
