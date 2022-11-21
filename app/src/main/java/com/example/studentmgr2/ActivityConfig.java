package com.example.studentmgr2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityConfig extends AppCompatActivity {
    private EditText nameInput;
    private EditText pwdInput;
    private Spinner fontSize;
    private Button configBn;
    private Button concelBn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config);
        init();
        setListener();
    }
    //  寻找对应控件
    private void init(){
        nameInput = (EditText) findViewById(R.id.configName);
        pwdInput  = (EditText) findViewById(R.id.configPwd);
        fontSize = (Spinner) findViewById(R.id.fontSize);
        configBn = (Button) findViewById(R.id.configBn);
        concelBn = (Button) findViewById(R.id.configBn2);

        fontChange();
    }

    private void fontChange(){
        SharedPreferences share = getSharedPreferences("config",Context.MODE_PRIVATE);
        if (!share.getString("fontSize","").equals("")){
            nameInput.setTextSize(TypedValue.COMPLEX_UNIT_PX,Integer.parseInt(share.getString("fontSize","")));
            pwdInput.setTextSize(TypedValue.COMPLEX_UNIT_PX,Integer.parseInt(share.getString("fontSize","")));
        }
    }
    //  设置监听器存储对应内容
    private void setListener(){
        configBn.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            if (nameInput.getText().toString().equals("")){
                Toast.makeText(this,"用户名不能为空",Toast.LENGTH_LONG).show();
            }else if (pwdInput.getText().toString().equals("")){
                Toast.makeText(this,"密码不能为空",Toast.LENGTH_LONG).show();
            }else{
                edit.putString("name",nameInput.getText().toString());
                edit.putString("pwd",pwdInput.getText().toString());
                edit.putString("fontSize",fontSize.getSelectedItem().toString());
                edit.commit();
                Toast.makeText(this, "编辑信息成功", Toast.LENGTH_LONG).show();
            }

        });
        concelBn.setOnClickListener(v -> {
            startActivity(new Intent(ActivityConfig.this,MainActivity.class));
        });
    }
}
