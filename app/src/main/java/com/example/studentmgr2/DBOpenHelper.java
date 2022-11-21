package com.example.studentmgr2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;

    String CREATE_TABLE_STUDENT = "create table student(" +
            "id integer primary key autoincrement," +
            "name varchar(20)," +
            "number varchar(20)," +
            "birthday varchar(30)," +
            "sex varchar(2)," +
            "institute varchar(20)," +
            "subject varchar(20)," +
            "hobby varchar(40), " +
            "intro varchar(200))";
    String CREATE_TABLE_USER = "create table user("+
            "id integer primary key autoincrement,"+
            "name varchar(20),"+
            "pwd varchar(20))";


    private DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库sql语句并执行
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    private static DBOpenHelper mInstance;
    public static synchronized DBOpenHelper getInstance(Context context){
        if (mInstance == null){
            mInstance = new DBOpenHelper(context,"student.db",null,VERSION);
        }
        return mInstance;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL("alter table user add column sex varchar(2)");
            case 2:
                //  删除列或者更改一个已经存在的字段的名称、数据类型、限定符等等，要建立新表，将原表的数据复制到新表中
                upgradeTo3(db);
            default:
                break;
        }
    }
//    删除列或者更改一个已经存在的字段的名称、数据类型、限定符等等，要建立新表，将原表的数据复制到新表中
    private void upgradeTo3(SQLiteDatabase db){
        //  将原表改名
        String CREATE_TEMP_TABLE ="alter table student rename to temp_student";
        //  创建一个新的表，将原有表的hobby删除掉
        String CREATE_TABLE = "create table student(" +
                "id integer primary key autoincrement," +
                "name varchar(20)," +
                "number varchar(20)," +
                "birthday varchar(30)," +
                "sex varchar(2)," +
                "institute varchar(20)," +
                "subject varchar(20)," +
                "intro varchar(200))";
        //  将旧表数据插入到新表
        String INSERT_DATA = "insert into student select id,name,number,birthday,sex,institute,subject,intro from temp_student";
        //  将旧表删除
        String DROP_STUDENT = "drop table temp_student";
        db.execSQL(CREATE_TEMP_TABLE);
        db.execSQL(CREATE_TABLE);
        db.execSQL(INSERT_DATA);
        db.execSQL(DROP_STUDENT);
    }
}
