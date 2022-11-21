package com.example.studentmgr2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StudentActivity extends AppCompatActivity {
    Spinner institutes, subjects;
    ArrayAdapter<String> insAdapter;
    ArrayAdapter<String> subAdapter;
    GestureDetector mGestureDetector;
    String institute = "请选择学院", subject = "请选择专业";
    String[] insList = new String[]{"请选择学院", "计算机学院", "电气学院"};
    String[] subject1 = new String[]{"请选择专业", "软件工程", "信息安全", "物联网"};
    String[] subject2 = new String[]{"请选择专业", "电气工程", "电机工程"};

    EditText nameText, numText, introText;
    RadioButton button1, button2;
    CheckBox box1, box2, box3, box4;
    Button button, copyBn, FileBn;
    DatePicker datePicker;
    boolean choice = true;

    private void findUIs() {
        button = (Button) findViewById(R.id.submit);
        nameText = (EditText) findViewById(R.id.nameInput);
        numText = (EditText) findViewById(R.id.numInput);
        button1 = (RadioButton) findViewById(R.id.radio1);
        button2 = (RadioButton) findViewById(R.id.radio2);
        box1 = (CheckBox) findViewById(R.id.checkBox1);
        box2 = (CheckBox) findViewById(R.id.checkBox2);
        box3 = (CheckBox) findViewById(R.id.checkBox3);
        box4 = (CheckBox) findViewById(R.id.checkBox4);
        institutes = (Spinner) findViewById(R.id.institutes);
        subjects = (Spinner) findViewById(R.id.subjects);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        copyBn = (Button) findViewById(R.id.copy);
        FileBn = (Button) findViewById(R.id.readFile);
        introText = (EditText) findViewById(R.id.intro);
        fontChange();
        setListeners();
    }

    private void fontChange() {
        SharedPreferences share = getSharedPreferences("config", Context.MODE_PRIVATE);
        if (!share.getString("fontSize","").equals("")) {
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            numText.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
        }
    }

    private void setListeners() {
        //  读取剪贴板内容
        copyBn.setOnClickListener(v -> {
            //  先创建剪贴板内容，往里面放一些东西
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("Label", "他人很好");
//            clipboard.setText("我们班有一个非常优秀的人，他的下巴右边有一个黑痣，长得也很帅气，人缘也不错，还有一个可笑的外号。\\n\" +\n" +
//                    "                    \"学习很用功，成绩也不错。每当月考时，都能考到前十名。他写作业的速度不是一般的快，老师刚把作业布置出来，他就写完了，这使我们非常的羡慕嫉妒恨。他也很喜欢看书，你只要给他一本书，他就能在一夜之间读完，不管书有多厚，都是如此。\\n\" +\n" +
//                    "                    \"他是一个很大方的人。每当一有东西，就与我们分享，这使别人一有东西，就和他分享。他也是一个很贪婪的人");
            clipboard.setPrimaryClip(mClipData);
            String content = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
            introText.setText(content);
        });
        FileBn.setOnClickListener(v -> {
//              writeSdcard();
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");//无类型限制
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);

        });
    }
    String path;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                Toast.makeText(this, path + "11111", Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                path = getPath(this, uri);
                Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            } else {//4.4以下下系统调用方法
                path = getRealPathFromURI(uri);
                Toast.makeText(StudentActivity.this, path + "222222", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        //  获得路径path
//        introText.setText(readFromXML(path));
        introText.setText("他很优秀!");
    }

    public static String readFromXML(String filePath) {
        FileInputStream fileInputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filePath);
        if (file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return stringBuilder.toString();
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (null != cursor && cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
            cursor.close();
        }
        return res;
    }

    /**
     * 专为Android4.4设计的从Uri获取文件绝对路径，以前的方法已不好使
     * 8
     */
    @SuppressLint("NewApi")


    public String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);

            }

        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);

        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();

        }
        return null;

    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */


    public String getDataColumn(Context context, Uri uri, String selection,
                                String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {

            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);

            }

        } finally {
            if (cursor != null)
                cursor.close();

        }
        return null;

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */


    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */


    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());

    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */


    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }



    /**
     * 在对sd卡进行读写操作之前调用这个方法
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */

    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {

            Manifest.permission.READ_EXTERNAL_STORAGE,

            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    /**

     * Checks if the app has permission to write to device storage

     *

     * If the app does not has permission then the user will be prompted to grant permissions

     *

     * @param activity

     */

    public static void verifyStoragePermissions(Activity activity) {

// Check if we have write permission

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {

// We don't have permission so prompt the user

            ActivityCompat.requestPermissions(

                    activity,

                    PERMISSIONS_STORAGE,

                    REQUEST_EXTERNAL_STORAGE

            );

        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_main);
        //读写权限
        verifyStoragePermissions(StudentActivity.this);
        //  编辑操作
        findUIs();
        //  添加按钮点击事件的监听器
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  实现页面跳转
                Intent intent = new Intent();
                intent.setClass(StudentActivity.this, MainActivity.class);
                //  利用数据库存储
                StudentDAL dal = new StudentDAL(StudentActivity.this);
                //  创建存放数据的ContentValues对象
                ContentValues values = new ContentValues();
                values.put("name", nameText.getText().toString());
                values.put("number", numText.getText().toString());
                if (button1.isChecked()) {
                    values.put("sex", "女");
                } else {
                    values.put("sex", "男");
                }
                values.put("institute", institutes.getSelectedItem().toString());
                values.put("subject", subjects.getSelectedItem().toString());
                Date date = new Date();
                date.setYear(datePicker.getYear() - 1900);
                date.setMonth(datePicker.getMonth());
                date.setDate(datePicker.getDayOfMonth());
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                values.put("birthday", formatter.format(date));
                ArrayList<String> list = new ArrayList<>();
                if (box1.isChecked()) {
                    list.add(box1.getText().toString());
                }
                if (box2.isChecked()) {
                    list.add(box2.getText().toString());
                }
                if (box3.isChecked()) {
                    list.add(box3.getText().toString());
                }
                if (box4.isChecked()) {
                    list.add(box4.getText().toString());
                }
                Gson gson = new Gson();
                String join = gson.toJson(list);
                values.put("hobby", join);
                values.put("intro", introText.getText().toString());
                //  需要判断执行编辑操作还是添加操作
                if (choice) {
                    dal.insertStudent(values);
                } else {
                    dal.updateStudentByName(values, new String[]{nameText.getText().toString()});
                }
//                intent.putStringArrayListExtra("hobby",list);
//                //  传递姓名信息
//                intent.putExtra("name",nameText.getText().toString());
//                //  传递学号信息
//                intent.putExtra("num",numText.getText().toString());
//                //  传递性别信息
//
//                //  传递学院及专业信息
//                intent.putExtra("institute",institutes.getSelectedItem().toString());
//                intent.putExtra("subject",subjects.getSelectedItem().toString());

                startActivity(intent);
            }
        });
        setGesture();
        //  获得下拉框
        setSpinner();
        //  设置默认项
        init();

    }

    //  根据共享首选项进行初始化
    public void init() {
        String name = null;
        String num = null;
        String sex = null;
        String institute2 = null;
        String birthday = null;
        String hobby = null;
        String intro = null;
        SharedPreferences share = getSharedPreferences("student", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit();
        if (getIntent().getStringExtra("number") != null) {
            choice = false;
            name = getIntent().getStringExtra("name");
            num = getIntent().getStringExtra("number");
            //  得到性别信息
            sex = getIntent().getStringExtra("sex");
            //  专业及学院下拉框设置
            institute2 = getIntent().getStringExtra("institute");
            //  生日信息
            birthday = getIntent().getStringExtra("birthday");
            //  喜好信息
            hobby = getIntent().getStringExtra("hobby");
            intro = getIntent().getStringExtra("intro");
            Edit(name, num, sex, institute2, birthday, hobby, intro);
        } else if (share.getString("flag", "").equals("1")) {
            name = share.getString("name", "");
            num = share.getString("number", "");
            //  得到性别信息
            sex = share.getString("sex", "");
            //  专业及学院下拉框设置
            institute2 = share.getString("institute", "");
            //  生日信息
            birthday = share.getString("birthday", "");
            //  喜好信息
            hobby = share.getString("hobby", "");
            intro = share.getString("intro", "");
            Edit(name, num, sex, institute2, birthday, hobby, intro);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public void setGesture() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // e1: 第一次按下的位置 e2 当手离开屏幕 时的位置 velocityX 沿x 轴的速度 velocityY： 沿Y轴方向的速度
                //判断竖直方向移动的大小
                if (Math.abs(e1.getRawY() - e2.getRawY()) > 100) {
                    return true;
                }
                if (Math.abs(velocityX) < 150) {
                    return true;
                }
                if ((e2.getRawX() - e1.getRawX()) > 80) {// 表示 向左滑动表示下一页
                    //  存入共享首选项中
                    SharedPreferences share = getSharedPreferences("student", Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit();
                    // 写入信息即可
                    edit.putString("name", nameText.getText().toString());
                    edit.putString("number", numText.getText().toString());
                    if (button1.isChecked()) {
                        edit.putString("sex", "女");
                    } else {
                        edit.putString("sex", "男");
                    }
                    edit.putString("institute", institutes.getSelectedItem().toString());
                    edit.putString("subject", subjects.getSelectedItem().toString());
                    Date date = new Date(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    edit.putString("birthday", format.format(date));
                    edit.putString("intro", introText.getText().toString());
                    ArrayList<String> list = new ArrayList<>();
                    if (box1.isChecked()) {
                        list.add(box1.getText().toString());
                    }
                    if (box2.isChecked()) {
                        list.add(box2.getText().toString());
                    }
                    if (box3.isChecked()) {
                        list.add(box3.getText().toString());
                    }
                    if (box4.isChecked()) {
                        list.add(box4.getText().toString());
                    }
                    //  转化成JSON数据
                    Gson gson = new Gson();
                    String join = gson.toJson(list);
                    edit.putString("hobby", join);
                    edit.putString("flag", "1");
                    edit.commit();
                    startActivity(new Intent(StudentActivity.this, MainActivity.class));
                    finish();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    public void setSpinner() {
        //  绑定适配器对应的值
        insAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, insList);
        institutes.setAdapter(insAdapter);
        institutes.setSelection(0, true);
        //  绑定适配器对应的值
        subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subject1);
        subjects.setAdapter(subAdapter);
        //  设置列表项的监听
        institutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //  获取选项中的值
                institute = adapterView.getItemAtPosition(position).toString();
                //  根据选项值的不同绑定不同的适配器
                if (institute.equals("计算机学院")) {
                    subAdapter = new ArrayAdapter<String>(StudentActivity.this, android.R.layout.simple_spinner_item, subject1);
                    subjects.setAdapter(subAdapter);

                } else if (institute.equals("电气学院")) {
                    subAdapter = new ArrayAdapter<String>(StudentActivity.this, android.R.layout.simple_spinner_item, subject2);
                    subjects.setAdapter(subAdapter);
                }
                SharedPreferences share = getSharedPreferences("student", Context.MODE_PRIVATE);
                String subject3 = null;
                if (getIntent().getStringExtra("name") != null && !getIntent().getStringExtra("name").equals("")) {
                    subject3 = getIntent().getStringExtra("subject");
                } else if (share.getString("flag", "").equals("1")) {
                    subject3 = share.getString("subject", "");
                }

                if (subject != null && !subject.equals("")) {
                    if (institute.equals("计算机学院")) {
                        for (int i = 0; i < subject1.length; i++) {
                            if (subject1[i].equals(subject3)) {
                                subjects.setSelection(i, true);
                                break;
                            }
                        }
                    } else if (institute.equals("电气学院")) {
                        for (int i = 0; i < subject2.length; i++) {
                            if (subject2[i].equals(subject3)) {
                                subjects.setSelection(i, true);
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //  设置列表项的监听
        subjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public static <T> T fromJson(String val, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(val, type);
    }

    private void Edit(String name, String number, String sex, String institute, String birthday, String hobby, String intro) {
        nameText.setText(name);
        numText.setText(number);
        introText.setText(intro);
        //  得到性别信息
        if (button1.getText().equals(sex)) {
            button1.setChecked(true);
        } else if (button2.getText().equals(sex)) {
            button2.setChecked(true);
        }
        //  专业及学院下拉框设置
        for (int i = 0; i < insList.length; i++) {
            if (insList[i].equals(institute)) {
                institutes.setSelection(i, true);
                break;
            }
        }
        //  设置生日日期
        if (birthday != null && !birthday.equals("")) {
            //  先获取其中年月日
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(birthday, pos);
            int year = date.getYear();
            int month = date.getMonth();
            int day = date.getDay();
            datePicker.init(year, month, day, null);
        }
        //  设置喜好
        if (hobby != null && !hobby.equals("")) {
            Gson gson = new Gson();
            List<String> hobbyList = gson.fromJson(hobby, new TypeToken<List<String>>() {
            }.getType());
            for (String h : hobbyList) {
                if (h.equals(box1.getText())) {
                    box1.setChecked(true);
                }
                if (h.equals(box2.getText())) {
                    box2.setChecked(true);
                }
                if (h.equals(box3.getText())) {
                    box3.setChecked(true);
                }
                if (h.equals(box4.getText())) {
                    box4.setChecked(true);
                }
            }
        }
    }
}
