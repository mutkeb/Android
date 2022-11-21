package com.example.studentmgr2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class QueryWeekdayService extends Service {
    private final IBinder mBinder = new IMyAidlInterface.Stub(){
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
        }
        @Override
        public String remoteGetWeekday(String date) throws RemoteException {
            return getWeekday(date); //调用具体的业务
        }
    };
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public String getWeekday(String date){
        String[] dates = date.split("-");
        String[] weekdays = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六",};
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR,Integer.parseInt(dates[0]));
        cal.set(Calendar.MONTH,Integer.parseInt(dates[1])-1);
        cal.set(Calendar.DATE,Integer.parseInt(dates[2]));
        int weekday = cal.get(Calendar.DAY_OF_WEEK)-1;
        return date+"的那天是"+weekdays[weekday];
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
