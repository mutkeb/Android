package com.example.studentmgr2;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private Context context;
    //  用户名编辑框
    private EditText m_userName = null;
    //  密码编辑框
    private EditText m_pwd = null;
    //  登录按钮
    private Button m_butLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findUIs();
        setListeners();
        autoInput();
        insert();
//        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//        ClipData clipData = ClipData.newPlainText("label","1234");
//        clipboardManager.setPrimaryClip(clipData);
//        ClipData primaryClip = clipboardManager.getPrimaryClip();
//        String s = primaryClip.getItemAt(0).getText().toString();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(LoginActivity.this, NetworkMonitorService.class));
        startService(new Intent(LoginActivity.this,ClipboardMonitorService.class));
    }


    @Override
    public void onWindowFocusChanged(boolean hasFoucus){
        super.onWindowFocusChanged(hasFoucus);
        if (hasFoucus){
            ClipboardManager clipboardManager = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
//            ClipData clipData = ClipData.newPlainText("label","ABCSE1234567");
//            clipboardManager.setText("ABCSE123456");
        }
    }


    public void insert(){
        StudentDAL dal = new StudentDAL(this);
        //  获得用户名和密码
        String name = m_userName.getText().toString();
        String pwd = m_pwd.getText().toString();
        //  查找数据库是否存在
        if (dal.queryLogin(name)){
            ContentValues values = new ContentValues();
            values.put("name",name);;
            values.put("pwd",pwd);
            dal.insertLogin(values);
        }
    }

    private void fontChange(){
        SharedPreferences share = getSharedPreferences("config",Context.MODE_PRIVATE);
        if (!share.getString("fontSize","").equals("")) {
            m_userName.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            m_pwd.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
        }
    }

    //  找到各类控件
    private void findUIs() {
        m_userName = (EditText) findViewById(R.id.nameInput);
        m_pwd = (EditText) findViewById(R.id.pwdInput);
        m_butLogin = (Button) findViewById(R.id.submit);
        fontChange();
    }

    //  对按钮设置监听器
    private void setListeners() {
        m_butLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_Check(v);
            }
        });
    }
    //  自动加载账号密码
    private void autoInput(){
        SharedPreferences share = getSharedPreferences("config",Context.MODE_PRIVATE);
        m_userName.setText(share.getString("name","").toString());
        m_pwd.setText(share.getString("pwd","").toString());
    }

    //  登录逻辑处理
    private void login_Check(View v) {
        //  利用SharePreferences保存
        SharedPreferences share =getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        //  登录密码
        String loginPwd_value = share.getString("loginpwd_value", "");
        String loginUserName_value = share.getString("loginUserName_value", "");
        //  判断上一次登录时输入的信息是否是正确的，可以免输入登录
        if (loginPwd_value.equals(getResources().getString(R.string.pwd_init)) && loginUserName_value.equals(getResources().getString(R.string.userName_init))){
            m_userName.setText(loginUserName_value);
            m_pwd.setText(loginPwd_value);
            openDialog("成功登录","消息");
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        //  设置默认值
        if (loginPwd_value.equals("")) {
            loginPwd_value = getResources().getString(R.string.pwd_init);
        }
        if (loginUserName_value.equals("")) {
            loginUserName_value = getResources().getString(R.string.userName_init);
        }

        //  获得编辑框输入的密码和用户名
        String loginPwd = m_pwd.getText().toString();
        String loginUserName = m_userName.getText().toString();
        edit.putString("loginpwd_value",loginPwd_value);
        edit.putString("loginUserName_value",loginUserName_value);
        edit.commit();
        //  登录逻辑处理
        if (!loginUserName.equals(loginUserName_value)){
            openDialog("用户名错误","消息");
        }else if (!loginPwd.equals(loginPwd_value)){
            openDialog("密码错误","消息");
        }else {
            openDialog("成功登录","消息");
            SharedPreferences share2 =getSharedPreferences("student", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit2 = share2.edit();
            edit2.clear().commit();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this,AdvertisementActivity.class);
            startActivity(intent);
            finish();
        }
    }

    //弹出对话框--------------------------------------------------
    private void openDialog(String strMsg, String strTitle) {
        new AlertDialog.Builder(this)
                .setTitle(strTitle)
                .setMessage(strMsg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        })
                .show();
    }
}

