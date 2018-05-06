package com.hnf.guet.comhnfpatent.util;

/**
 * Created by Administrator on 2018/3/24.
 * 回传当前改变的网络状态
 */

public interface NetEvent {
    void onNetChange(int netMobile);
}
