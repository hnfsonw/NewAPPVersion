package com.hnf.guet.comhnfpatent.http;

import com.hnf.guet.comhnfpatent.config.Constants;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

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

    /**
     * 获取专业用户的信息
     * @param fields
     */
    @FormUrlEncoded
    @POST(Constants.GET_USER_INFOMATION)
    Call<ResponeModelInfo> getUserInfomation(@FieldMap HashMap<String, Object> fields);

    /**
     * 添加收藏
     * @param fields
     * @return
     */
    @FormUrlEncoded
    @POST(Constants.ADD_COLLECTION)
    Call<ResponeModelInfo> addCollection(@FieldMap HashMap<String, Object> fields);


    /**
     * 取消收藏
     */
    @FormUrlEncoded
    @POST(Constants.CANCLE_COLLECTION)
    Call<ResponeModelInfo> canclecollection(@FieldMap HashMap<String, Object> fields);

    /**
     * 查询是否已经收藏
     */
    @FormUrlEncoded
    @POST(Constants.COLLECTION_OR_NOR)
    Call<ResponeModelInfo> queryCollectionOne(@FieldMap HashMap<String, Object> fields);

    /**
     * 验证token是否有效
     * @param fields
     */
//    @FormUrlEncoded
//    @POST(Constants.TOKEN_IS_STILL_WORK)
//    Call<ResponeModelInfo>VerificationToken(@FieldMap HashMap<String, Object> fields);

    /**
     * 插入新的需求信息
     */
    @FormUrlEncoded
    @POST(Constants.INSERT_NEW_IDEA)
    Call<ResponeModelInfo> insertNewIdea(@FieldMap HashMap<String, Object> fields);

    @Multipart
    @POST(Constants.UPLOAD_IMAGES)
    Call<ResponeModelInfo> uploadImages(@Part List<MultipartBody.Part> partList);


    /**
     * 根据关键词查询专家
     */
    @FormUrlEncoded
    @POST(Constants.QUERY_FROFESS_BY_KEY_WORDS)
    Call<ResponeModelInfo> queryProfessByKeywords(@FieldMap HashMap<String, Object> fields);
}
