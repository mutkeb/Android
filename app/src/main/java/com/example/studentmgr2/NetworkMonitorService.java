package com.example.studentmgr2;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.view.Window;

import androidx.annotation.Nullable;

import java.security.PublicKey;

public class NetworkMonitorService extends Service {

    NetWorkStateReceiver myReceiver;
    CharSequence title;
    Activity previous = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public  void onCreate(){
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        NetWorkStateReceiver mReceiver = new NetWorkStateReceiver();
        this.registerReceiver(mReceiver,filter);
        mReceiver.setOnStateChangedListener(new NetWorkStateReceiver.OnStateChangedListener() {
            @Override
            public void onStateChanged(boolean state) {
                // 每次记录第一次活动的标题
                Activity activity = MyActivityManager.getActivity();
                if (activity != previous){
                    title = activity.getTitle();
                }
                previous = activity;
                if (!state){
                    // 有网络连接 进行什么操作
                    //在Service服务类中发送广播消息给Activity活动界面
                    activity.setTitle(title + "[网络已断开]");
                } else {
                    // 无网络连接 进行什么操作
                    //在Service服务类中发送广播消息给Activity活动界面
                    activity.setTitle(title);
                }
            }
        });
    }
    private String getRunningActivityName() {
        Context ct = this;
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity = activityManager.getRunningTasks(Integer.MAX_VALUE).get(0).topActivity.getClassName();
        return runningActivity;
    }


    @Override
    public void onDestroy(){
        unregisterReceiver();
        super.onDestroy();
    }

    private void unregisterReceiver(){
        if (myReceiver != null){
            this.unregisterReceiver(myReceiver);
        }
    }




}
