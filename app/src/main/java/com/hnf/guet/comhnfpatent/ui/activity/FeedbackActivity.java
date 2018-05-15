package com.hnf.guet.comhnfpatent.ui.activity;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.FeedbackPresenter;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;
public class FeedbackActivity extends BaseActivity {
    private static final java.lang.String TAG = "FeedbackActivity";
    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.help_feedback)
    EditText mHelpFeedback;

    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    private FeedbackPresenter mFeedbackPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void init() {
        mFeedbackPresenter = new FeedbackPresenter(this, this);

        initView();
        initListener();
    }


    private void initListener() {
        mHelpFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 10) {
                    mBtnSubmit.setEnabled(true);
                    mBtnSubmit.setBackgroundResource(R.mipmap.register);
                } else {
                    mBtnSubmit.setEnabled(false);
                    mBtnSubmit.setBackgroundResource(R.drawable.back_shape_corners_tou_black_mask_back);
                }
            }
        });
    }


    private void initView() {
        mTvTitle.setText(getString(R.string.feedback));
        mBtnSubmit.setEnabled(false);
        mBtnSubmit.setBackgroundResource(R.drawable.back_shape_corners_tou_black_mask_back);
        UserUtil.showSoftInput(this, true);
    }


    @Override
    protected void dismissNewok() {
        if (mFeedbackPresenter.mCall != null) {
            mFeedbackPresenter.mCall.cancel();
        }
    }


    @OnClick({R.id.iv_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        String trim = mHelpFeedback.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;

            case R.id.btn_submit:
                mFeedbackPresenter.submit(trim, MyApplication.sToken, MyApplication.sAcountId);
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            finishActivityByAnimation(this);
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 提交成功的通知
     */
    public void submitSuccess() {
        dismissLoading();
        printn(getString(R.string.Submit_success_thank_you_for_your_valuable_opinions));
        finishActivityByAnimation(this);
    }
}

