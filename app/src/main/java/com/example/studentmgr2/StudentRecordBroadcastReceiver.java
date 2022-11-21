package com.example.studentmgr2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StudentRecordBroadcastReceiver extends BroadcastReceiver {
    private static final String Action_Name = "com.example.studentmgr2.Clip";
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Action_Name.equals(action)){
            String content = intent.getStringExtra("HAS_STUDENT_RECORD");
            Intent intent1 = new Intent();
            intent1.putExtra("number",content);
            intent1.setClass(context,StudentActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
