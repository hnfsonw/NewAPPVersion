package com.hnf.guet.comhnfpatent.ui.activity.acountActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.presenter.ForgetPasswordPresenter;
import com.hnf.guet.comhnfpatent.ui.view.LastInputEditText;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetActivity extends BaseActivity {
    private static final  String TAG="ForgetActivity";

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.et_phone)
    LastInputEditText mEtPhone;

    @BindView(R.id.btn_register)
    Button mBtnRegister;

    private ForgetPasswordPresenter mPresenterr;
    private String mPhone;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_forget;
    }

    @Override
    protected void init() {
        mPresenterr = new ForgetPasswordPresenter(this,this);
        initView();
    }

    private void initView() {
        mTvTitle.setText(getString(R.string.forget_password));
    }

    @OnClick({R.id.iv_back,R.id.btn_register})
    public void Onclick(View view){
        mPhone = mEtPhone.getText().toString().trim();
        switch (view.getId()){
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.btn_register:
                mPresenterr.checkAcount(mPhone);
                break;
        }
    }

    //重写onkeydown方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK){
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void dismissNewok() {
        if (mPresenterr.mCheckAcount != null){
            mPresenterr.mCheckAcount.cancel();
        }
    }

    public void phoneIsEmpty() {
        printn(getString(R.string.phoneIsEmpty));
    }

    public void phoneIsError() {
        printn(getString(R.string.phoneError));
    }

    /**
     * 验证成功后跳转到界面
     */
    public void onSuccess() {
        dismissLoading();
        toActivity(ForgetAndResetPassActivity.class,mPhone);
        finishActivityByAnimation(this);
    }

    public void onError(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }
}
