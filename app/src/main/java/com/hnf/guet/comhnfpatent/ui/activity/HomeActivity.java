package com.hnf.guet.comhnfpatent.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseActivity;
import com.hnf.guet.comhnfpatent.factory.FragmentFactory;
import com.hnf.guet.comhnfpatent.model.ExitModel;
import com.hnf.guet.comhnfpatent.presenter.HomePresenter;
import com.hnf.guet.comhnfpatent.ui.activity.acountActivity.LoginActivity;
import com.hnf.guet.comhnfpatent.ui.view.BottomTab;
import com.hnf.guet.comhnfpatent.ui.view.BottomTabLayout;
import com.hnf.guet.comhnfpatent.util.LogUtils;

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
    BottomTabLayout mMBottomTabLayout;

    @BindView(R.id.iv_error)
    public ImageView mIvError;

//    @BindView(R.id.include)
//    View mView;

    private HomePresenter mPresenter;

    private String[] mTabNames = { "首页", "找专家","我有好主意","消息","我的"};
    public static boolean isForeground = false;
    public boolean isOnResume;
    private int mQueryState = 0;   //用来控制当前界面不在HomeActivity 刷新UI

    //底部控件
    private List<BottomTab> mBottomTabs = new ArrayList<>();
    private Fragment fm;

    /**
     * 默认状态下的本地图片
     */
    private int[] mUnSelectIcons = {
            R.mipmap.home,
            R.mipmap.group,
            R.mipmap.idea,
            R.mipmap.message,
            R.mipmap.my};

    /**
     * 默认状态下选中的本地图片
     */
    private int[] mSelectIcons = {
            R.mipmap.home_select,
            R.mipmap.group_select,
            R.mipmap.idea_seclect,
            R.mipmap.message_select,
            R.mipmap.my_select};

    private Long mAcountId;
    private Gson mGson;
    public int mNotifactionId;
    private int mSelectColor = R.color.SelectColor;
    private int mUnSelectColor = R.color.unSelectColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onTabItemSelected(0);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void init() {
        if (mPresenter == null){
            mPresenter = new HomePresenter(this,this);
        }
        if (mGson == null){
            mGson = new Gson();
        }

        EventBus.getDefault().register(this);
        initView();
        initData(false);
    }

    /**
     * 更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)  //表示这个方法在主线程执行
    public void onUpdate(){
        LogUtils.i(TAG,"更新设备");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExit(ExitModel exitModel){
        LogUtils.e(TAG,"退出");
        toActivity(LoginActivity.class,0);
        finishActivityByAnimation(this);
    }

    //刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(){
//        mView.setVisibility(View.GONE);
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
    private void initData(boolean isUrl) {
        mBottomTabs.clear();
        for (int i = 0;i<mUnSelectIcons.length;i++){
            BottomTab mBottomTab = new BottomTab(mTabNames[i],mUnSelectColor,mSelectColor,
                    mUnSelectIcons[i],mSelectIcons[i],null,null);
            mBottomTabs.add(mBottomTab);

        }
        mMBottomTabLayout.setBottomTabData(mBottomTabs);
    }


    private void initView() {
        mMBottomTabLayout.setOnTabChangeListener(new BottomTabLayout.OnTabChangeListener() {
            @Override
            public void onTabSelect(int position) {
//                if (position == 0){
                onTabItemSelected(position);
//                }else if (position == 1){
//                    onTabSelected(position);
//                }else {
                onTabSelected(position);
//                }
            }

            @Override
            public void onTabSelected(int position) {
                LogUtils.i(TAG,"选择了:"+position);
            }
        });
    }

    private void onTabItemSelected(int position) {
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
        if (mPresenter.resultData != null){
            mPresenter.resultData.cancel();
        }
    }

    @Override
    protected void onResume() {

        isForeground = true;
        isOnResume = true;
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


}
