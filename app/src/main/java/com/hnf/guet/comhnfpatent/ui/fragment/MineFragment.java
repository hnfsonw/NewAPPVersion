package com.hnf.guet.comhnfpatent.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.BaseFragment;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.model.ResponeModelInfo;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.SplashPresenter;
import com.hnf.guet.comhnfpatent.ui.activity.FeedbackActivity;
import com.hnf.guet.comhnfpatent.ui.activity.ModifyPasswordActivity;
import com.hnf.guet.comhnfpatent.ui.view.SelectPicturePopupWindow;
import com.hnf.guet.comhnfpatent.ui.view.VesionDialog;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.UIUtils;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends BaseFragment implements View.OnClickListener{
    private static final String TAG="MineFragment";

    //    @BindView(R.id.my_message_layout)
    LinearLayout messageLayout;

    //    @BindView(R.id.reset_pass_layout)
    LinearLayout resetPassLayout;

    //    @BindView(R.id.version_upgrade_layout)
    LinearLayout versionUpgradeLayout;

    //    @BindView(R.id.feed_back_layout)
    LinearLayout opinionLayout;

    //    @BindView(R.id.about_me_layout)
    LinearLayout aboutMeLayout;

    //    @BindView(R.id.head_img)
    CircleImageView headerImg;

    //    @BindView(R.id.my_push_layout)
    LinearLayout myPushLayout;  //我的发布

    //    @BindView(R.id.my_star_layout)
    LinearLayout starLayout;   //我的收藏

    //    @BindView(R.id.login_out_btn)
    Button loginOutBtn;

    private SelectPicturePopupWindow selectPop;
    private ResultBean mResult;
    private VesionDialog mVersionDialog;


    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment,null);
        messageLayout = view.findViewById(R.id.my_message_layout);
        resetPassLayout = view.findViewById(R.id.reset_pass_layout);
        versionUpgradeLayout = view.findViewById(R.id.version_upgrade_layout);
        opinionLayout = view.findViewById(R.id.feed_back_layout);
        aboutMeLayout = view.findViewById(R.id.about_me_layout);
        headerImg = view.findViewById(R.id.head_img);
        myPushLayout = view.findViewById(R.id.my_push_layout);
        starLayout = view.findViewById(R.id.my_star_layout);
        loginOutBtn = view.findViewById(R.id.login_out_btn);
        return view;
    }


    @Override
    protected void init() {
        initView();
        initListener();
    }

    @Override
    protected void dismissNewok() {

    }

    private void initListener() {
        messageLayout.setOnClickListener(this);
        resetPassLayout.setOnClickListener(this);
        versionUpgradeLayout.setOnClickListener(this);
        opinionLayout.setOnClickListener(this);
        aboutMeLayout.setOnClickListener(this);
        headerImg.setOnClickListener(this);
        myPushLayout.setOnClickListener(this);
        starLayout.setOnClickListener(this);
        loginOutBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_img:
                selelctPicture();
                LogUtils.e(TAG, "头像呗点击了");
                break;
            case R.id.my_push_layout:
                LogUtils.e(TAG, "发布");
                break;
            case R.id.my_star_layout:
                LogUtils.e(TAG, "收藏");
                break;
            case R.id.my_message_layout:
                LogUtils.e(TAG, "消息");
                break;
            case R.id.reset_pass_layout:
                LogUtils.e(TAG, "密码");
                Intent intent = new Intent(getActivity(),ModifyPasswordActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.version_upgrade_layout:

                break;
            case R.id.feed_back_layout:
                Intent feebIntent = new Intent(getActivity(),FeedbackActivity.class);
                getActivity().startActivity(feebIntent);
                break;
            case R.id.about_me_layout:

                break;
            case R.id.login_out_btn:
                LogUtils.e(TAG, "btn");
                break;
        }
    }

    private void selelctPicture() {
        selectPop.showPopupWindow(getActivity());
    }


    private void initView() {

    }


}
