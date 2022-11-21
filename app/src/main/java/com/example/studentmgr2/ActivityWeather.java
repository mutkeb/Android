package com.example.studentmgr2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityWeather extends AppCompatActivity {
//    private final List<String> times = new ArrayList<>();
//    private final List<String> weather = new ArrayList<>();

    private ImageView image1,image2,image3,image4,image5;
    private TextView day1,day2,day3,day4,day5;

    private List<String> data = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);
        findUIs();
        loadBingPic();
        updateView();
    }

    private void findUIs(){
        image1 = (ImageView) findViewById(R.id.weather1);
        image2 = (ImageView) findViewById(R.id.weather2);
        image3 = (ImageView) findViewById(R.id.weather3);
        image4 = (ImageView) findViewById(R.id.weather4);
        image5 = (ImageView) findViewById(R.id.weather5);
        day1 = (TextView) findViewById(R.id.day1);
        day2 = (TextView) findViewById(R.id.day2);
        day3 = (TextView) findViewById(R.id.day3);
        day4 = (TextView) findViewById(R.id.day4);
        day5 = (TextView) findViewById(R.id.day5);

    }
    private void setImage(ImageView view,String weather){
        if (weather.equals("晴")){
            view.setImageResource(R.drawable.w1);
        }else if (weather.equals("少云")){
            view.setImageResource(R.drawable.w2);
        }else if (weather.equals("多云")){
            view.setImageResource(R.drawable.w3);
        }else if (weather.equals("阵雨")){
            view.setImageResource(R.drawable.w4);
        }else if (weather.equals("阴")){
            view.setImageResource(R.drawable.w5);
        }else if (weather.equals("小雨")){
            view.setImageResource(R.drawable.w6);
        }else{
            view.setImageResource(R.drawable.w7);
        }
    }
    private void updateView(){
//        List<String> data = new ArrayList<>();
//        data.add("晴");
//        data.add("阴");
//        data.add("小雨");
//        data.add("晴");
//        data.add("多云");
//
//        List<String> data2 = new ArrayList<>();
//        data2.add("2022-10-25");
//        data2.add("2022-10-26");
//        data2.add("2022-10-27");
//        data2.add("2022-10-28");
//        data2.add("2022-10-29");
//
//        day1.setText(data2.get(0));
//        day2.setText(data2.get(1));
//        day3.setText(data2.get(2));
//        day4.setText(data2.get(3));
//        day5.setText(data2.get(4));
//        setImage(image1,data.get(0));
//        setImage(image2,data.get(1));
//        setImage(image3,data.get(2));
//        setImage(image4,data.get(3));
//        setImage(image5,data.get(4));

        SharedPreferences shared = getSharedPreferences("weather",Context.MODE_PRIVATE);
        String weather = shared.getString("weather", "");
        Gson gson = new Gson();
        List<String> weatherResponse = gson.fromJson(weather,new TypeToken<List<String>>(){
        }.getType());
        day1.setText(weatherResponse.get(0));
        day2.setText(weatherResponse.get(2));
        day3.setText(weatherResponse.get(4));
        day4.setText(weatherResponse.get(6));
        day5.setText(weatherResponse.get(8));
        setImage(image1,weatherResponse.get(1));
        setImage(image2,weatherResponse.get(3));
        setImage(image3,weatherResponse.get(5));
        setImage(image4,weatherResponse.get(7));
        setImage(image5,weatherResponse.get(9));
    }

    private void loadBingPic(){
        String weatherId = "CN101190301";
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId +"&key=755a053d247341699ebbe941099d994f";

        HttpUtil.sendHttpRequest(weatherUrl, new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseText = response.body().string();
                final List<String> weatherResponse = Utility.handleWeatherResponse(responseText);
//                for (int i = 0; i < 5; i++) {
//                    times.add(weatherResponse.get(2 * i));
//                    weather.add(weatherResponse.get(2 * i + 1));
//                }
                SharedPreferences share = getSharedPreferences("weather", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit();
                Gson gson = new Gson();
                String strJson = gson.toJson(weatherResponse);
                edit.clear();
                edit.putString("weather",strJson);
                edit.commit();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityWeather.this, "获取天气信息成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ActivityWeather.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}
