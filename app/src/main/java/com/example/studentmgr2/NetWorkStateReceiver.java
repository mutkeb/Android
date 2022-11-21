package com.example.studentmgr2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkStateReceiver extends BroadcastReceiver {
    private OnStateChangedListener mOnStateChangedListener;
    private static final String Action_Name = "android.net.conn.CONNECTIVITY_CHANGE";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(Action_Name)){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null){
                // 手机信号/3G/4G
                NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
                // Wifi
                NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    mOnStateChangedListener.onStateChanged(false);
                } else {
                    mOnStateChangedListener.onStateChanged(true);
                }
            } else {
                mOnStateChangedListener.onStateChanged(false);
            }
        }
    }
    public void setOnStateChangedListener(OnStateChangedListener mOnStateChangedListener) {
        this.mOnStateChangedListener = mOnStateChangedListener;
    }

    /**
     * 状态改变的监听器
     */
    public interface OnStateChangedListener {
        /**
         * 状态改变时，回调方法
         *
         * @param state
         */
        void onStateChanged(boolean state);
    }
}
