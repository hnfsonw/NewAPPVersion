package com.hnf.guet.comhnfpatent.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;

import butterknife.BindView;
import butterknife.OnClick;

public class AboutMeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void init() {
        mTvTitle.setText("关于我");
    }

    @Override
    protected void dismissNewok() {

    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
        }
    }
}
