package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.content.Intent;
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
import com.hnf.guet.comhnfpatent.presenter.RegistetrPresenter;
import com.hnf.guet.comhnfpatent.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {
    private static final String TAG = "RegisterActivity";

    @BindView(R.id.et_phone)
    LastInputEditText mEtPhone;

    @BindView(R.id.et_code)
    LastInputEditText mEtCode;

    @BindView(R.id.tv_send_code)
    public TextView mTvSendCode;

    @BindView(R.id.et_password)
    LastInputEditText mEtPassword;


    @BindView(R.id.btn_register)
    Button mBtnRegister;

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;


    @BindView(R.id.et_confirm_password)
    LastInputEditText mEtConfirmPassword;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    private RegistetrPresenter mRegisterPresenter;
    private String mPhone;
    private String mPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_register;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mRegisterPresenter = new RegistetrPresenter(this,this);
        initView();
//        initListener();
    }

    private void initListener() {

    }

    @OnClick({R.id.btn_register,R.id.tv_send_code,R.id.iv_back,R.id.iv_no_view})
    public void onViewClicked(View view){
        mPhone = mEtPhone.getText().toString().trim();
        String code = mEtCode.getText().toString().trim();
        mPassword = mEtPassword.getText().toString().trim();
        String confirmPassword = mEtConfirmPassword.getText().toString().trim();
        switch (view.getId()){
            case R.id.btn_register:
                //presneter发起注册请求
                mRegisterPresenter.registerNumber(mPhone,code,mPassword,confirmPassword);
                break;
            case R.id.tv_send_code:
                //发送短信验证码
                mRegisterPresenter.checkCode(mPhone);
                break;
            case R.id.iv_back:
                finishActivityByAnimation(this);
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


    private void initView() {
        mTvTitle.setText(getString(R.string.Register_Title));
        mIvBack.setVisibility(View.VISIBLE);
    }


    /**
     * 重写onkeydown方法
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 加载网络，清除缓存队列
     */
    @Override
    protected void dismissNewok() {
        if (mRegisterPresenter.mRegister != null){
            mRegisterPresenter.mRegister.cancel();
        }
        if (mRegisterPresenter.mSendCheckCode != null){
            mRegisterPresenter.mSendCheckCode.cancel();
        }
    }

    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }

    public void phoneError() {
        printn(getString(R.string.phoneError));
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

    public void CheckCode() {
        printn(getString(R.string.CheckCode_Success));
    }

    public void error(String resultMsg) {
        printn(resultMsg);
    }

    public void succeed() {
        printn(getString(R.string.Register_Success));
        dismissLoading();
        Intent intent = getIntent();
        intent.putExtra("phone",mPhone);
        intent.putExtra("password",mPassword);
        setResult(100,intent);
        finishActivityByAnimation(this);
    }

    public void onError(String resultMsg) {
        printn(resultMsg);
        dismissLoading();
    }
}
