package com.hnf.guet.comhnfpatent.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.security.Principal;
import java.sql.Connection;

/**
 */

public class NetUtil {
    /**
     * 没有网络连接
     */
    private static final int NETWORK_NONE = -1;

    /**
     * 移动网络
     */
    private static final int NETWORK_MOBILE = 0;

    /**
     * wifi网络
     */
    private static final int NETWORK_WIFI = 1;


    /**
     * 获取网络状态
     */
    public static int getNetWorkState(Context context) {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        } else {
            return NETWORK_NONE;
        }
        return NETWORK_NONE;
    }

}
