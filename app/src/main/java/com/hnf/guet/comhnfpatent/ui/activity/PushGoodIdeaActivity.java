package com.hnf.guet.comhnfpatent.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.presenter.PushGoodIdeaPresenter;
import com.hnf.guet.comhnfpatent.util.UserUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PushGoodIdeaActivity extends BaseActivity {

    private static final String TAG = "PushGoodIdeaActivity";
    private PushGoodIdeaPresenter presenter;


    @BindView(R.id.iv_back)
    LinearLayout mIvBack;

    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.idea_commit_btn)
    Button commitBtn;

    @BindView(R.id.idea_title_editText)
    EditText titleEdText;

    @BindView(R.id.idea_content_editText)
    EditText contentEdText;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_push_good_idea;
    }

    @Override
    protected void init() {
        presenter = new PushGoodIdeaPresenter(this,this);
        initView();
    }

    private void initView() {
        mTvTitle.setText("发布需求");
    }

    @OnClick({R.id.iv_back,R.id.idea_commit_btn})
    public void onViewClicked(View view) {
        String title = titleEdText.getText().toString().trim();
        String content = contentEdText.getText().toString().trim();
        switch (view.getId()) {
            case R.id.iv_back:
                finishActivityByAnimation(this);
                break;
            case R.id.idea_commit_btn:
                presenter.pushIdea(MyApplication.sToken,MyApplication.sAcountId,title,content);
                break;
        }
    }

    @Override
    protected void dismissNewok() {

    }

    //插入成功
    public void succeed() {
        dismissLoading();
        printn("发布成功");
        finishActivityByAnimation(this);
    }

    //插入失败
    public void inserFaiure(String resultMsg) {
        dismissLoading();
        printn(resultMsg);
    }
}
