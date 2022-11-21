package com.example.studentmgr2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class AdvertisementActivity extends AppCompatActivity implements Runnable {
    //  跳过按钮
    private Button skipButton;
    boolean flag = false;
    //  创建一个倒计时线程
    Handler handler=new Handler(){//处理者
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){//按时间自动逐秒递减
                case 1:
                    skipButton.setText("广告倒计时：4秒（点击可跳过）");
                    break;
                case 2:
                    skipButton.setText("广告倒计时：3秒（点击可跳过）");
                    break;
                case 3:
                    skipButton.setText("广告倒计时：2秒（点击可跳过）");
                    break;
                case 4:
                    skipButton.setText("广告倒计时：1秒（点击可跳过）");
                    break;
                case 5:
                    skipButton.setText("广告倒计时：0秒（点击可跳过）");
                    if (!flag){
                        startActivity(new Intent(AdvertisementActivity.this,MainActivity.class));
                        finish();
                    }
                    break;
            }
            super.handleMessage(msg);/* 发送消息指令 */
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.advertisement);
        skipButton = (Button)findViewById(R.id.skip);
//3秒后自动跳转到主页面
        Thread thread = new Thread(this);
        thread.start();
//点击"点击跳转"后进入主页面的点击事件：
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdvertisementActivity.this,MainActivity.class));
                finish();
                flag = true;
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5 ; i++) {
            Message message = new Message();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            message.what = i;
            handler.sendMessage(message);
        }
    }
}

