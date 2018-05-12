package com.hnf.guet.comhnfpatent.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.hnf.guet.comhnfpatent.R;
import com.hnf.guet.comhnfpatent.base.BaseFragment;


public class HomeFragment extends BaseFragment {

    @Override
    protected View getLayoutRes(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment,null);
        return view;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void dismissNewok() {

    }
}
