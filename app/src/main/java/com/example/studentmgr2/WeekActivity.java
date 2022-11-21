package com.example.studentmgr2;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentmgr2.IMyAidlInterface;

public class WeekActivity extends AppCompatActivity {
    private IMyAidlInterface queryWeekday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekday);
        Button queryBtn = (Button)findViewById(R.id.queryBtn);
        EditText dateText = (EditText)findViewById(R.id.dateEditView);
        queryBtn.setOnClickListener(v -> {
            String date = dateText.getText().toString();
            String weekday = null;
            try {
                weekday = queryWeekday.remoteGetWeekday(date);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,weekday,Toast.LENGTH_LONG).show();
        });
    }

    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            queryWeekday = IMyAidlInterface.Stub.asInterface(service);
//            queryWeekday = ((QueryWeekday.MyBinder)service).getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this,QueryWeekdayService.class);
        Boolean result = bindService(intent,conn,BIND_AUTO_CREATE);
        Log.i("result",result+"");
//        System.out.println(queryWeekday);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(conn);
    }
}