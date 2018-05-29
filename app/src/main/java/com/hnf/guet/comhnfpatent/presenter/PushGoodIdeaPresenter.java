package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.PushGoodIdeaActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class PushGoodIdeaPresenter extends BasePresenter{

    private static final  String TAG = "PushGoodIdeaPresenter";

    private Context mContext;
    private PushGoodIdeaActivity myActivity;
    private Call<ResponeModelInfo> myCall;

    public PushGoodIdeaPresenter(Context context, PushGoodIdeaActivity activity) {
        super(context);
        mContext = context;
        myActivity = activity;
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        LogUtils.e(TAG,"新需求信息插入成功");
        myActivity.dismissLoading();
        myActivity.printn("发布成功");
    }



    public void pushIdea(String sToken, long sAcountId, String title, String content) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("title",title);
        hashMap.put("content",content);
        hashMap.put("token",sToken);
        hashMap.put("acountId",sAcountId);
        myCall = mHttpService.insertNewIdea(hashMap);
        myActivity.showLoading("");//显示加载圈
        myCall.enqueue(mCallback);
    }

    /**
     * 提交图片文件
     * @param imagePaths
     */
    public void pushImages(final ArrayList<String> imagePaths,final ArrayList<String> filePaths,final String sToken, final long sAcountId, final String title, final String content) {
        myActivity.showLoading("");//显示加载圈
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (imagePaths == null){
                    upLoadFiles(filePaths,sToken,sAcountId,title,content);
                }else if (filePaths == null){
                    upLoadImages(imagePaths,sToken,sAcountId,title,content);
                }else {
                    upLoadImagesAndFiles(imagePaths,filePaths,sToken,sAcountId,title,content);
                }

            }
        }.start();//一定要记得启动新线程
    }

    /**
     * 只发布文字和文件
     * @param filePaths
     * @param sToken
     * @param sAcountId
     * @param title
     * @param content
     */
    private void upLoadFiles(ArrayList<String> filePaths, String sToken, long sAcountId, String title, String content) {
        LogUtils.e(TAG,"只传文件");

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token",MyApplication.sToken)
                .addFormDataPart("title",title)
                .addFormDataPart("content",content)
                .addFormDataPart("acountId",String.valueOf(sAcountId))
                .addFormDataPart("all","2");

        for (int i = 0;i<filePaths.size();i++){
            File file = new File(filePaths.get(i));
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("File",file.getName(),fileBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        myCall = mHttpService.uploadImages(parts);
        myCall.enqueue(mCallback2);
    }

    /**
     * 只发布文字和图片
     * @param imagePaths
     * @param sToken
     * @param sAcountId
     * @param title
     * @param content
     */
    private void upLoadImages(ArrayList<String> imagePaths, String sToken, long sAcountId, String title, String content) {
        LogUtils.e(TAG,"只传图片");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token",sToken)
                .addFormDataPart("title",title)
                .addFormDataPart("content",content)
                .addFormDataPart("acountId",String.valueOf(sAcountId))
                .addFormDataPart("all","2");

        for (int i = 0;i<imagePaths.size();i++){
            File file = new File(imagePaths.get(i));
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("File",file.getName(),imageBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        myCall = mHttpService.uploadImages(parts);
        myCall.enqueue(mCallback2);
    }

    /**
     * 图片和文字一起传送
     * @param imagePaths
     * @param sToken
     * @param sAcountId
     * @param title
     * @param content
     */
    private void upLoadImagesAndFiles(ArrayList<String> imagePaths,ArrayList<String> filePaths,String sToken, long sAcountId, String title, String content) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token",sToken)
                .addFormDataPart("title",title)
                .addFormDataPart("content",content)
                .addFormDataPart("acountId",String.valueOf(sAcountId))
                .addFormDataPart("imageSize",String.valueOf(imagePaths.size()))
                .addFormDataPart("all","1");//表示图片和文件一起传

        int size = imagePaths.size()+filePaths.size();
        LogUtils.e(TAG,"size----->"+size);
        for (int i = 0;i<size;i++){
            File file;
            if (i >= imagePaths.size()){
                file = new File(filePaths.get(size-i-1));
            }else {
                file = new File(imagePaths.get(i));
            }
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("File",file.getName(),imageBody);
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        myCall = mHttpService.uploadImages(parts);
        myCall.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        LogUtils.e(TAG,"图片上传成功");
        myActivity.succeed();
    }


    @Override
    protected void onFaiure(ResponeModelInfo s) {
        myActivity.inserFaiure(s.getResultMsg());
    }

}
