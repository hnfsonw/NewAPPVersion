package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.LoginPresenter;
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.util.LogUtils;

public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";


    @BindView(R.id.et_login_username)
    LastInputEditText mEtLoginUsername;

    @BindView(R.id.et_login_password)
    LastInputEditText mEtLoginPassword;

    @BindView(R.id.tv_login_forget)
    TextView mTvLoginForget;

    @BindView(R.id.btn_login)
    Button mBtnLogin;

    @BindView(R.id.tv_login_register)
    TextView mTvLoginRegister;

    @BindView(R.id.iv_no_view)
    ImageView mIvNoView;

    @BindView(R.id.ll_eyes)
    LinearLayout mLlEyes;

    private SharedPreferences mGlobalvariable;
    private String mMd5Password;
    private String mAppAccount;
    private LoginPresenter mLoginPresenter;
    private boolean isLogin = true;
    private String mPhone;
    private String mPassword;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }



    @Override
    protected void init() {
        ButterKnife.bind(this);
        toSDmissions();
        mLoginPresenter = new LoginPresenter(this,this);
        mGlobalvariable = getSharedPreferences("globalvariable", MODE_PRIVATE);
        mMd5Password = mGlobalvariable.getString("password", "");
        mAppAccount = mGlobalvariable.getString("appAccount", "");
        initView();
    }

    private void initView() {
        mEtLoginPassword.setText(mMd5Password);
        mEtLoginUsername.setText(mAppAccount);
    }

    @OnClick({R.id.btn_login,R.id.tv_login_register,R.id.tv_login_forget,R.id.iv_no_view,R.id.ll_eyes})
    public void onViewClicked(View view){
        if (!isLogin){
            printn(getString(R.string.no_network));
            return;
        }
        mPhone = mEtLoginUsername.getText().toString().trim();
        mPassword = mEtLoginPassword.getText().toString().trim();
        switch (view.getId()){
            case R.id.tv_login_register:
                toActivity(100,RegisterActivity.class);
                break;
            case R.id.tv_login_forget:
                toActivity(ForgetActivity.class);
                break;
            case R.id.btn_login:
                mLoginPresenter.login(mPhone,mPassword);
                break;
            case R.id.ll_eyes:
                viewPassword();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100){
            if (data != null){
                LogUtils.d(TAG,"注册返回来的数据");
                mPhone = data.getStringExtra("phone");
                mPassword = data.getStringExtra("password");
                mEtLoginUsername.setText(mPhone);
                mEtLoginPassword.setText(mPassword);
                toActivity(HomeFragment.class);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 显示密码
     */
    private void viewPassword() {
        if (mIvNoView.isSelected()){
            mIvNoView.setImageResource(R.mipmap.no_view_register);
            mEtLoginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }else {
            mIvNoView.setImageResource(R.mipmap.show_login);
            mEtLoginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        mIvNoView.setSelected(!mIvNoView.isSelected());
    }


    /**
     * 存储权限被拒绝直接退出
     */
    @Override
    protected void grantedError() {
        finishActivityByAnimation(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            //返回桌面
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_APP_GALLERY);
            startActivity(intent);
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void phoneIsEmpty(){
        printn(getString(R.string.phoneIsEmpty));
    }

    public void phoneError(){
        printn(getString(R.string.phoneError));
    }


    public void passwordIsEmpty(){
        printn(getString(R.string.passwordIsEmpty));
    }

    @Override
    protected void dismissNewok() {
        if (mLoginPresenter.mLogin != null){
            mLoginPresenter.mLogin.cancel();
        }
    }

    @Override
    protected void noNetwork() {
        MyApplication.sInUpdata = true;
    }

    @Override
    protected void theNetwork() {

        isLogin = true;
    }
}
