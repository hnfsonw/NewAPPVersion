package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.base.BaseFragment;
import com.hnf.guet.comhnfpatent.base.MyApplication;
import com.hnf.guet.comhnfpatent.factory.FragmentFactory;
import com.hnf.guet.comhnfpatent.model.ExitModel;
import com.hnf.guet.comhnfpatent.model.bean.ResultBean;
import com.hnf.guet.comhnfpatent.presenter.HomePresenter;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.LoginActivity;
import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.ui.fragment.MessageFragment;
import com.hnf.guet.comhnfpatent.ui.view.BottomTab;
import com.hnf.guet.comhnfpatent.ui.view.BottomTabLayout;
import com.hnf.guet.comhnfpatent.util.LogUtils;
import com.hnf.guet.comhnfpatent.util.SharedPreferencesUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {
    private static final String TAG = "HomeActivity";
    @BindView(R.id.home_container)
    FrameLayout mHomeContainer;

    @BindView(R.id.mBottomTabLayout)
    public BottomTabLayout mMBottomTabLayout;

    @BindView(R.id.iv_error)
    public ImageView mIvError;


    private String[] mTabNames = { "首页","消息","我的"};
    public static boolean isForeground = false;
    public boolean isOnResume;
    private int mQueryState = 0;   //用来控制当前界面不在HomeActivity 刷新UI

    //底部控件
    private List<BottomTab> mBottomTabs = new ArrayList<>();
    private Fragment fm;
    private List<ResultBean> mResultList;
    private int resultListSize;
    private HomePresenter mHomePresenter;
    private SharedPreferences mGlobalvariable;
    private String macountName;
    private String acountType;

    /**
     * 默认状态下的本地图片
     */
    private int[] mUnSelectIcons = {
            R.mipmap.home,
            R.mipmap.message,
            R.mipmap.my};

    /**
     * 默认状态下选中的本地图片
     */
    private int[] mSelectIcons = {
            R.mipmap.home_select,
            R.mipmap.message_select,
            R.mipmap.my_select};

    private Long mAcountId;
    private Gson mGson;
    public int mNotifactionId;
    private int mSelectColor = R.color.SelectColor;
    private int mUnSelectColor = R.color.unSelectColor;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
//        Intent intent = getIntent();
//        acountType = intent.getStringExtra("acountType");
        if (mHomePresenter == null){
            mHomePresenter = new HomePresenter(this,this);
        }
        if (mGson == null){
            mGson = new Gson();
        }
        mGlobalvariable = this.getSharedPreferences("globalvariable", Context.MODE_PRIVATE);
        macountName = mGlobalvariable.getString("acountName","");
        acountType = mGlobalvariable.getString("acountType","");
        LogUtils.e(TAG,"6666-------------》"+mGlobalvariable.getString("acountType",""));

        LogUtils.e(TAG,"acounName的值"+macountName);
        initView();
        initData(false);
    }


    //重写onkeydown方法
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化数据
     */
    public void initData(boolean isUrl) {
        mBottomTabs.clear();
        for (int i = 0;i<mUnSelectIcons.length;i++){
            BottomTab mBottomTab = new BottomTab(mTabNames[i],mUnSelectColor,mSelectColor,
                    mUnSelectIcons[i],mSelectIcons[i],null,null);
            mBottomTabs.add(mBottomTab);

        }
        mMBottomTabLayout.setBottomTabData(mBottomTabs);
        if (acountType.equals("1")){
            mHomePresenter.queryUserInfoList(MyApplication.sToken,macountName);
        }else {
            mHomePresenter.getIdeasInformation();
        }
    }


    public void initView() {
        mMBottomTabLayout.setOnTabChangeListener(new BottomTabLayout.OnTabChangeListener() {
            @Override
            public void onTabSelect(int position) {
                if (mResultList == null){
                    mHomePresenter.queryUserInfoList(MyApplication.sToken,macountName);
                    return;
                }
                onTabItemSelected(position);

                if (position == 0 && resultListSize >= 1){
                    HomeFragment homeFragment = (HomeFragment) fm;
                    homeFragment.setList(mResultList);
                }
            }

            @Override
            public void onTabSelected(int position) {
                LogUtils.i(TAG,"选择了:"+position);
            }
        });
    }

    public void onTabItemSelected(int position) {
        changeFragment(FragmentFactory.createFragment(position));
    }

    //切换fragment
    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fm != null){
            transaction.hide(fm);
        }
        //// isAdded:是否被添加过 被添加过 is true 反之
        if (!fragment.isAdded()){
            transaction.add(R.id.home_container,fragment);
        }else {
            transaction.show(fragment);
        }
        transaction.commit();

        fm = fragment;
    }


    @Override
    protected void dismissNewok() {
        if (mHomePresenter.resultData != null){
            mHomePresenter.resultData.cancel();
        }
    }

    public void queryUserInfoListSuccess(List<ResultBean> userInfoList) {
        dismissLoading();
        LogUtils.e(TAG,"查询用户列表成功"+userInfoList.size());
        mResultList = userInfoList;
        resultListSize = mResultList.size();
        for (int i = 0;i<resultListSize;i++){
            SharedPreferencesUtils.setParam(mContext,mResultList.get(i).getNickName(),mResultList.get(i).getImgUrl());
        }
        mQueryState = 1;
        if (isOnResume){
            mMBottomTabLayout.setCurrentTab(0);
            onTabItemSelected(0);
            HomeFragment homeFragment = (HomeFragment) fm;
            homeFragment.setData(mResultList);
            mQueryState = 0;
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        isOnResume = true;
        if (mQueryState == 1){
            mMBottomTabLayout.setCurrentTab(0);
            onTabItemSelected(0);
            BaseFragment baseFragment = (BaseFragment) fm;
            baseFragment.setData(mResultList);
            mQueryState = 0;
        }
        super.onResume();
    }


    @Override
    protected void onPause() {
        isOnResume =false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        isForeground = false;
        super.onDestroy();
    }


    private FragmentManager fManager;
    private FragmentTransaction transaction;

    public void toMessageFragment(){
        onTabItemSelected(1);
        mMBottomTabLayout.setCurrentTab(1);
    }

    public void sureLoginOut() {
        showLoading("");
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("HomeActivity", "退出成功");
                mGlobalvariable.edit().putBoolean("login",false).apply();
                exitHomeActivity();
                Intent intent = new Intent(HomeActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishActivityByAnimation(HomeActivity.this);
                dismissLoading();
            }

            @Override
            public void onError(int i, String s) {
                Log.i("LoginPresenter", "退出失败 " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
