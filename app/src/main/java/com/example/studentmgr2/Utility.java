package com.example.studentmgr2;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Utility {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<String> handleWeatherResponse(String response){
    /*
  handleWeatherResponse()方法中先是通过jsonObject和jsonArray将天气
        数据中的主体内容解析出来
     */
        List<String> weatherList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
            JSONObject now = jsonObject1.getJSONObject("now");
            //  拿到今天的天气数据
            String cond_txt = now.get("cond_txt").toString();
            LocalDateTime now1 = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            weatherList.add(now1.format(formatter));
            weatherList.add(cond_txt);
            JSONArray daily_forecast = jsonObject1.getJSONArray("daily_forecast");
            for (int i = 0;i < 4; i++){
                //  拿到四天的数据
                JSONObject cond = daily_forecast.getJSONObject(i).getJSONObject("cond");
                String weather = cond.getString("txt_d");
                String date = daily_forecast.getJSONObject(i).getString("date");
                weatherList.add(date);
                weatherList.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return weatherList;
    }

}
