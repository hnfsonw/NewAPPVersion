package com.hnf.guet.comhnfpatent.ui.view;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.ui.activity.SettingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描   述:  移除提示框
 */

public class VesionDialog extends AlertDialog {


    @BindView(R.id.tv_cancel)
    public TextView mTvCancel;

    @BindView(R.id.determine)
    TextView mDetermine;

    @BindView(R.id.tv_remove)
    public TextView mTvRemove;

    @BindView(R.id.tv_user_name)
    public TextView mTvUserName;

    @BindView(R.id.ll_lin)
    public LinearLayout mLlLin;

    private BaseActivity mSettingActivity;
    private Context mContext;

    public VesionDialog(Context context, BaseActivity settingActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mSettingActivity = settingActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vesion);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        getWindow().setGravity(Gravity.CENTER); //显示在中间


    }

    public void initUserName(String string){
        mTvUserName.setVisibility(View.VISIBLE);
        mTvUserName.setText(string);
    }


    public void initTitle(String title){
        mTvRemove.setText(title);
    }



    /**
     * 设置宽度全屏，要设置在show的后面
     */
    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().getDecorView().setPadding(120, 0, 120, 0);

        getWindow().setAttributes(layoutParams);

    }


    @OnClick({R.id.tv_cancel, R.id.determine})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                mSettingActivity.dismissVesion();
                dismiss();
                break;


            case R.id.determine:
                mSettingActivity.upDate();
                dismiss();
                break;
        }
    }


}
