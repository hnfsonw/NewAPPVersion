package com.hnf.guet.comhnfpatent.presenter;

import android.content.Context;
import android.content.SharedPreferences;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BasePresenter;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.ui.activity.ProfessSkillsActivity;

import java.util.HashMap;

import retrofit2.Call;

public class ProfessSkillsPresenter extends BasePresenter {
    private ProfessSkillsActivity mActivity;
    private SharedPreferences mGlobalvariable;
    private Context mConetnt;
    private String acountName;
    private Call<ResponeModelInfo> resultData;

    public ProfessSkillsPresenter(Context context, ProfessSkillsActivity activity) {
        super(context);
        mConetnt = context;
        mActivity = activity;
        mGlobalvariable = mConetnt.getSharedPreferences("globalvariable", Context.MODE_PRIVATE);
        acountName = mGlobalvariable.getString("acountName","");
    }

    @Override
    protected void parserJson(ResponeModelInfo data) {
        mActivity.dismissLoading();
        mActivity.loadSucced(data.getResult());
    }

    @Override
    protected void onFaiure(ResponeModelInfo s) {
        mActivity.dismissLoading();
        mActivity.printn(s.getResultMsg());
    }

    public void loadMySkills() {
        HashMap<String,String> paramsMap = new HashMap<>();
        paramsMap.put("token", MyApplication.sToken);
        paramsMap.put("acountName",mGlobalvariable.getString("acountName",""));
        resultData = mHttpService.querySkillsByAcountname(paramsMap);
        mActivity.showLoading(mConetnt.getString(R.string.dialogMessage));
        resultData.enqueue(mCallback);
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
        resultData = mHttpService.updateUserInfoInterface(paramsMap);
        mActivity.showLoading(mConetnt.getString(R.string.dialogMessage));
        resultData.enqueue(mCallback2);
    }

    @Override
    protected void onSuccess(ResponeModelInfo body) {
        mActivity.dismissLoading();
        mActivity.printn("保存成功~");
        mActivity.savaSucceed();
    }

    @Override
    protected void onError(ResponeModelInfo body) {
        mActivity.dismissLoading();
        mActivity.printn(body.getResultMsg());
    }
}
