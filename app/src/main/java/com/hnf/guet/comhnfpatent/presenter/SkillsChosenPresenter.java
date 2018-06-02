package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.SkillsChosenActivity;
import com.hnf.guet.comhnfpatent.util.MD5Utils;

import java.util.HashMap;

import retrofit2.Call;

public class SkillsChosenPresenter extends BasePresenter {
    private static final String TAG = "SkillsChosenPresenter";


    private Context mContext;
    private SkillsChosenActivity mActivity;
    private Call<ResponeModelInfo> mRegister;
    private SharedPreferences mGlobalvariable;

    public SkillsChosenPresenter(Context context, SkillsChosenActivity activity) {
        super(context);
        mContext = context;
        mActivity = activity;
        mGlobalvariable = mActivity.getSharedPreferences("globalvariable",Context.MODE_PRIVATE);
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        mActivity.dismissLoading();
        mActivity.updateSucceed();

    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        mActivity.dismissLoading();
        mActivity.printn(s.getResultMsg());
    }

    public void commitProfessInformation(String nick, String job, String skill, String experience, String imformation) {
        if (nick.equals("") || job.equals("")|| skill.equals("") || experience.equals("")|| imformation.equals("")){
            mActivity.printn("所有的项都必须填写哦~");
            return;
        }
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("token", MyApplication.sToken);
        paramsMap.put("acountName",mGlobalvariable.getString("acountName",""));
        paramsMap.put("nick", nick);
        paramsMap.put("job", job);
        paramsMap.put("skill", skill);
        paramsMap.put("experience",experience);
        paramsMap.put("information",imformation);
        mRegister = mHttpService.updateUserInfoInterface(paramsMap);
        mActivity.showLoading(mContext.getString(R.string.dialogMessage));
        mRegister.enqueue(mCallback);

    }
}
