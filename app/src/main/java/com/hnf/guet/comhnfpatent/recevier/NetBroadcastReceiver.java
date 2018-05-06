package com.hnf.guet.comhnfpatent.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.hnf.guet.comhnfpatent.util.NetEvent;
import com.hnf.guet.comhnfpatent.util.NetUtil;


/**
 * 检查手机网络状态
 */
public class NetBroadcastReceiver extends BroadcastReceiver {

    private NetEvent netEvent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            int netWorkState = NetUtil.getNetWorkState(context);
            Log.e("NetBroadcastReceiver",netWorkState + "");
            if (netEvent != null){
                netEvent.onNetChange(netWorkState);
            }
        }

    }

    public void setNetEvent(NetEvent netEvent){
        this.netEvent = netEvent;
    }
}
