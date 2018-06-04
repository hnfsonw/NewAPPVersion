package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.ProfessSkillsPresenter;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfessSkillsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.nick_edt_b)
    EditText nickEdt;

    @BindView(R.id.job_edt_b)
    EditText jobEdt;

    @BindView(R.id.skills_edt_b)
    EditText skillEdt;

    @BindView(R.id.experience_edt_b)
    EditText experienceEdt;

    @BindView(R.id.information_edt_b)
    EditText informationEdt;

    @BindView(R.id.btn_profess_commit_b)
    Button commitBtn;

    private ProfessSkillsPresenter mPresenter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_profess_skills;
    }

    @Override
    protected void init() {
        if (mPresenter == null){
            mPresenter = new ProfessSkillsPresenter(this,this);
        }
        initData();
        initView();
    }

    /**
     * 加载用户原本的技能信息
     */
    private void initData() {
        mPresenter.loadMySkills();
    }

    private void initView() {
        mTvTitle.setText("技能填写");
    }

    @OnClick({R.id.iv_back,R.id.btn_profess_commit_b})
    public void onViewClicked(View view) {
        String nick = nickEdt.getText().toString().trim();
        String job = jobEdt.getText().toString().trim();
        String skill = skillEdt.getText().toString().trim();
        String experience = experienceEdt.getText().toString().trim();
        String imformation = informationEdt.getText().toString().trim();

        switch (view.getId()){
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.btn_profess_commit_b:
                mPresenter.commitProfessInformation(nick,job,skill,experience,imformation);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void loadSucced(ResultBean result) {
        nickEdt.setText(result.getNickName());
        jobEdt.setText(result.getJob());
        skillEdt.setText(result.getGoodAt());
        experienceEdt.setText(result.getWorkExprience());
        informationEdt.setText(result.getInfomation());
        newName = result.getNickName();
    }

    public void savaSucceed() {
        LogUtils.e("HAHAHAH","妈个鸡");
        Intent intent = getIntent();
        intent.putExtra("newNick",newName);
        setResult(3,intent);
        finish();
    }

    private  String newName;
}
