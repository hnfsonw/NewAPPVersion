package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.presenter.ForgetAndResetPassPresenter;
import com.hnf.guet.comhnfpatent.presenter.RegistetrPresenter;
import com.hnf.guet.comhnfpatent.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetAndResetPassActivity extends BaseActivity {
    private static final String TAG = "ForgetAndResetPassActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_code)
    LastInputEditText mEtCode;

    @BindView(R.id.tv_send_code)
    public TextView mTvSendCode;

    @BindView(R.id.et_password)
    LastInputEditText mEtPassword;

    @BindView(R.id.et_confirm_password)
    LastInputEditText mEtConfigPassword;

    @BindView(R.id.btn_register)
    Button mBtnResetpassword;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;


    private ForgetAndResetPassPresenter mPresenter;
    private String mCode;
    private String mPhone;
    private String mPassword;
    private String mConfirPassword;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget_and_reset_pass;
    }

    @Override
    protected void init() {
        mPresenter = new ForgetAndResetPassPresenter(this,this);
        mPhone = getIntent().getStringExtra(STRING);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.login_forgot_pwd_text));
    }

    @OnClick({R.id.iv_back,R.id.tv_send_code,R.id.btn_register,R.id.iv_no_view})
    public void onViewClicked(View view){
        String code = mEtCode.getText().toString().trim();
        String password= mEtPassword.getText().toString().trim();
        String confirPassword = mEtConfigPassword.getText().toString().trim();
        switch (view.getId()){
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.tv_send_code:
                mPresenter.checkCode(mPhone);
                break;
            case R.id.btn_register:
                mPresenter.findPassAction(mPhone,code,password,confirPassword);
                break;
            case R.id.iv_no_view:
                viewPassword();
                break;
        }
    }

    /**
     * 显示密码
     */
    private void viewPassword() {
        if (mIvNoView.isSelected()){
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }
    @Override
    protected void dismissNewok() {
        if (mPresenter.codeResult != null){
            mPresenter.codeResult.cancel();
        }
        if (mPresenter.findPassResult != null){
            mPresenter.findPassResult.cancel();
        }
    }

    public void CheckCode() {
        printn(getString(R.string.CheckCode_Success));
    }

    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }

    public void phoneError() {
        printn(getString(R.string.phoneError));
    }

    public void error(String resultMsg) {
        printn(resultMsg);
    }

    public void codeIsEmpty() {
        printn(getString(R.string.codeIsEmpty));
    }

    public void passWordIsEmpty() {
        printn(getString(R.string.passwordIsEmpty));
    }

    public void passwordInconformity() {
        printn(getString(R.string.password_error));
    }

    public void passwordError() {
        printn(getString(R.string.password_error));
    }

    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void failureReasion(String s) {
        printn(s);
        dismissLoading();
    }

    /**
     * 找回密码成功后的回调
     */
    public void findPassSuccess() {
        dismissLoading();
        printn(getString(R.string.deit_password_success));
        finishActivityByAnimation(this);
    }
}
