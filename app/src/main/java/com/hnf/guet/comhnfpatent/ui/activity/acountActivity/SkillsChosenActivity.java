package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.presenter.SkillsChosenPresenter;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SkillsChosenActivity extends BaseActivity {

    private static final String TAG = "SkillsChosenActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.nick_edt)
    EditText nickEdt;

    @BindView(R.id.job_edt)
    EditText jobEdt;

    @BindView(R.id.skills_edt)
    EditText skillEdt;

    @BindView(R.id.experience_edt)
    EditText experienceEdt;

    @BindView(R.id.information_edt)
    EditText informationEdt;

    @BindView(R.id.btn_profess_commit)
    Button commitBtn;
    private SkillsChosenPresenter mPresenter;
    private String mphone,mPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_skills_chosen;
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        mphone = intent.getStringExtra("mPhone");
        mPassword = intent.getStringExtra("mPassword");
        if (mPresenter == null){
            mPresenter = new SkillsChosenPresenter(this,this);
        }
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        mTvTitle.setText("技能填写");
    }

    @OnClick({R.id.iv_back,R.id.btn_profess_commit})
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
            case R.id.btn_profess_commit:
                mPresenter.commitProfessInformation(nick,job,skill,experience,imformation);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    public void updateSucceed() {
        LogUtils.i(TAG,"登录跳转--》HomeActivity");
        Intent intent = new Intent(SkillsChosenActivity.this,HomeActivity.class);
        intent.putExtra("acountType","2");
        startActivity(intent);
//        toActivity(HomeActivity.class);
        finishActivityByAnimation(this);
    }
}
