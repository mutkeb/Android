package com.example.studentmgr2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentDAL {

    private SQLiteDatabase db;

    StudentDAL(Context context){
        SQLiteOpenHelper instance = DBOpenHelper.getInstance(context);
        db= instance.getWritableDatabase();
    }

    public void insertStudent(ContentValues values){
        db.insert("student",null,values);
    }
    public void updateStudentByName(ContentValues values,String[] value2){
        db.update("student",values,"name = ?",value2);
    }
    @SuppressLint("Range")
    public List<Map<String,Object>> queryAll(){
        List<Map<String,Object>> data = new ArrayList<>();
        Cursor cursor = db.query("student",new String[]{"id","name","number","birthday","sex","institute","subject","intro"}, null,null,null,null,null);
        if (cursor.getCount() == 0){
            return null;
        }else{
            while(cursor.moveToNext()){
                Map<String,Object> map = new HashMap<>();
                map.put("name",cursor.getString(cursor.getColumnIndex("name")));
                map.put("number",cursor.getString(cursor.getColumnIndex("number")));
                map.put("birthday",cursor.getString(cursor.getColumnIndex("birthday")));
                map.put("sex",cursor.getString(cursor.getColumnIndex("sex")));
                map.put("institute",cursor.getString(cursor.getColumnIndex("institute")));
                map.put("subject",cursor.getString(cursor.getColumnIndex("subject")));
                map.put("icon",R.drawable.p2);
                map.put("intro",cursor.getString(cursor.getColumnIndex("intro")));
                data.add(map);
            }
            cursor.close();
            return data;
        }
    }

    @SuppressLint("Range")
    public List<Map<String,Object>> search(String keyName,String keyIns,String keySub){
        List<Map<String,Object>> data = new ArrayList<>();
        Cursor cursor = db.query("student",new String[]{"name","number","birthday","sex","institute","subject","hobby","intro"},"" +
                "name=? and institute=? and subject=?",new String[]{keyName,keyIns,keySub},null,null,null);
        if (cursor.getCount() == 0){
            return null;
        }else{
            while(cursor.moveToNext()){
                Map<String,Object> map = new HashMap<>();
                map.put("name",cursor.getString(cursor.getColumnIndex("name")));
                map.put("number",cursor.getString(cursor.getColumnIndex("number")));
                map.put("birthday",cursor.getString(cursor.getColumnIndex("birthday")));
                map.put("sex",cursor.getString(cursor.getColumnIndex("sex")));
                map.put("institute",cursor.getString(cursor.getColumnIndex("institute")));
                map.put("subject",cursor.getString(cursor.getColumnIndex("subject")));
                map.put("icon",R.drawable.p2);
                map.put("intro",cursor.getString(cursor.getColumnIndex("intro")));
                data.add(map);
            }
            cursor.close();
            return data;
        }
    }

    public void deleteStudentByName(String[] value){
        db.delete("student","name = ? and number = ?",value);
    }

    public void insertLogin(ContentValues values){
        db.insert("user",null,values);
    }

    public boolean queryLogin(String name){
        Cursor cursor = db.query("user",new String[]{"name","pwd"},"name = ?",new String[]{name},null,null,null,null);
        if (cursor.getCount() == 0){
            return true;
        }else{
            return false;
        }
    }
}
