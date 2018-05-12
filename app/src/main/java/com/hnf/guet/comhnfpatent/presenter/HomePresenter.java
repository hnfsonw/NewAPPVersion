package com.hnf.guet.comhnfpatent.presenter;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import retrofit2.Call;

public class HomePresenter extends BasePresenter {
    private static final  String TAG = "HomePresenter";
    private Context mContext;
    private HomeActivity homeActivity;
    public Call<ResponeModelInfo> resultData;
    private AlertDialog.Builder mBuilder;
    private NotificationManager notificationManager;


    public HomePresenter(Context context, HomeActivity activity) {
        super(context);
        homeActivity = activity;
        mContext = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }




    @Override
    protected void parserJson(ResponeModelInfo data) {

    }

    @Override
    protected void onFaiure(ResponeModelInfo data) {
        LogUtils.e(TAG,"onFaiure:"+data.getResultMsg());
        homeActivity.mIvError.setVisibility(View.VISIBLE);
        homeActivity.printn(data.getResultMsg());
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        LogUtils.e(TAG,"更新失败");
    }
}
