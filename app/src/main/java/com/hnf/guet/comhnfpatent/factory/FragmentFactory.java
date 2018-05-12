package com.hnf.guet.comhnfpatent.factory;



import android.support.v4.app.Fragment;

import com.hnf.guet.comhnfpatent.ui.fragment.HomeFragment;
import com.hnf.guet.comhnfpatent.ui.fragment.FindProfessorFragment;
import com.hnf.guet.comhnfpatent.ui.fragment.HaveIdearFragment;
import com.hnf.guet.comhnfpatent.ui.fragment.MessageFragment;
import com.hnf.guet.comhnfpatent.ui.fragment.MineFragment;
import com.hnf.guet.comhnfpatent.util.LogUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 类    名:  FragmentFactory
 * 描    述： 封装Fragment的创建过程
 */
public class FragmentFactory {
    public static final int HOME_FRAGMENT = 0;//首页
    public static final int FIND_PROFESS_FRAGMENT = 1;//找专家
    public static final int HAVE_IDEAR_FRAGMENT = 2;//我有好主意
    public static final int MESSAGE_FRAGMENT = 3;//消息
    public static final int MINE_FRAGMENT = 4;//我的
    private static final String TAG = "FragmentFactory";

    public static Map<Integer, Fragment> mCacheFragmentMap = new HashMap<>();

    public static Fragment createFragment(int position) {
        Fragment fragment = null;
        //优先从集合中取出来
        if (mCacheFragmentMap.containsKey(position)) {
            LogUtils.d(TAG,"优先从集合中取出来");
            return mCacheFragmentMap.get(position);
        }

        switch (position) {
            case HOME_FRAGMENT:
                fragment = new HomeFragment();
                break;

            case FIND_PROFESS_FRAGMENT:
                fragment = new FindProfessorFragment();
                break;

            case HAVE_IDEAR_FRAGMENT:
                fragment = new HaveIdearFragment();
                break;

            case MESSAGE_FRAGMENT:
                fragment = new MessageFragment();
                break;

            case MINE_FRAGMENT:
                fragment = new MineFragment();
                break;
            default:
                break;
        }
        //保存fragment到集合中
        mCacheFragmentMap.put(position, fragment);

        return fragment;
    }
}
