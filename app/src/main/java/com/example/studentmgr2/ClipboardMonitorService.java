package com.example.studentmgr2;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import java.security.Provider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClipboardMonitorService extends Service {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final static String DIVIDE_RESULT = "com.example.studentmgr2.Clip";
    private int count = 0;
    private boolean stop_state = false;

    @Override
    public void onCreate() {
        super.onCreate();
        final ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (clipboardManager.hasPrimaryClip()) {
                        Intent intent = new Intent(DIVIDE_RESULT);
                        intent.setComponent(new ComponentName("com.example.studentmgr2","com.example.studentmgr2.StudentRecordBroadcastReceiver"));
//                        String message = clipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
                        String message = clipboardManager.getText().toString();
                        //  判断是否包含SE123456格式的字符串
                        if (contain(message) != null) {
                            //  发送字符串
                            intent.putExtra("HAS_STUDENT_RECORD", contain(message));
                            sendBroadcast(intent);
                        }
                        clipboardManager.setText("");
                    }
                }
            }
        }).start();
    }

    private String contain(String message) {
        //  利用正则表达式匹配
        Pattern pattern = Pattern.compile("SE[0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(0);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
