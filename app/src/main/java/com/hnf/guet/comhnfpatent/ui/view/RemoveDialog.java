package com.hnf.guet.comhnfpatent.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RemoveDialog extends AlertDialog {


    @BindView(R.id.tv_cancel)
    TextView mTvCancel;

    @BindView(R.id.determine)
    TextView mDetermine;

    @BindView(R.id.tv_remove)
    public TextView mTvRemove;

    @BindView(R.id.tv_user_name)
    public TextView mTvUserName;

    @BindView(R.id.tv_factory_settings)
    TextView mTvFactorySettings;

    private HomeActivity mBaseActivity;
    private Context mContext;

    public RemoveDialog(Context context, HomeActivity baseActivity) {
        super(context, R.style.MyDialog);
        mContext = context;
        mBaseActivity = baseActivity;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_dialog);
        ButterKnife.bind(this);
        getWindow().setGravity(Gravity.CENTER); //显示在中间


    }

    public void initUserName(String string){
        mTvUserName.setVisibility(View.VISIBLE);
        mTvUserName.setText(string);
    }


    public void initTitle(String title){
        mTvRemove.setText(title);
    }


    public void initTitle(String title,boolean isShow){
        mTvRemove.setText(title);
        if (isShow){
            mTvFactorySettings.setVisibility(View.VISIBLE);
        }else {
            mTvFactorySettings.setVisibility(View.GONE);
        }
    }


    public void initTitle(String title,boolean isShow,String str){
        mTvRemove.setText(title);
        if (isShow){
            mTvFactorySettings.setVisibility(View.VISIBLE);
            mTvFactorySettings.setText(str);
        }
    }


    /**
     * 设置取消按钮
     * @param cancel
     */
    public void setTvCancel(String cancel){
        mTvCancel.setText(cancel);
    }


    public void setDetermine(String remove){
        mDetermine.setText(remove);
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
                dismiss();
                mBaseActivity.dismiss();
                break;


            case R.id.determine:
                mBaseActivity.cancel();
                mBaseActivity.sureLoginOut();
                dismiss();
                break;
        }
    }

}
