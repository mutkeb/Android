package com.example.studentmgr2;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;

import java.util.Locale;


public class ActivityPhonePlace extends AppCompatActivity {
    //  电话号码输入框
    private EditText inputPhone;
    //  查询按钮
    private Button queryBn;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);
        initListener();
        fontChange();
    }

    private void fontChange() {
        SharedPreferences share = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (!share.getString("fontSize", "").equals("")) {
            inputPhone.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));

        }
    }
    private void initListener(){
        queryBn = (Button) findViewById(R.id.phoneBn);
        inputPhone = (EditText) findViewById(R.id.phoneInput);
        textView = (TextView)findViewById(R.id.textView);
        queryBn.setOnClickListener(v -> {
            String phone = inputPhone.getText().toString();
            String address = getGeo(phone);
            textView.setText(address);
            //  将内容放到剪贴板上
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label",address);
            cm.setPrimaryClip(mClipData);
        });
    }

    private  String getGeo(String phoneNum){
        PhoneNumberOfflineGeocoder geoCoder = PhoneNumberOfflineGeocoder.getInstance();
        long l = Long.parseLong(phoneNum);
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(86);
        pn.setNationalNumber(l);
        return geoCoder.getDescriptionForNumber(pn, Locale.CHINESE);
    }
}
