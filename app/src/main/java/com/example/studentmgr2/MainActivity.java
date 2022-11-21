
package com.example.studentmgr2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ListView view2;
    Button button,button2;
    private int pos;
    List<Map<String, Object>> data = new ArrayList<>();
    //  手势检测器
    private GestureDetector mGestureDetector;


    //  适配器
    private ArrayAdapter<String> insAdapter;
    private ArrayAdapter<String> subAdapter;
    private String institute = "请选择学院", subject = "请选择专业";
    private String[] insList = new String[]{"请选择学院","计算机学院","电气学院"};
    private String[] subject1 = new String[]{"请选择专业","软件工程","信息安全","物联网"};
    private String[] subject2 = new String[]{"请选择专业","电气工程","电机工程"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StudentDAL dal = new StudentDAL(this);
//        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this,"student.db",null,1);
//        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
//        initData();
        //  设置滑动页面
//        LayoutInflater inflater = getLayoutInflater().from(this);
//        View view1 = inflater.inflate(R.layout.activity_main,null);
//        View view3 = inflater.inflate(R.layout.student_main,null);
//
//        List<View> viewList = new ArrayList<>();
//        viewList.add(view1);
//        viewList.add(view3);
//
//        ViewPager viewpager = findViewById(R.id.viewpager);
//        MyAdapter myadapter = new MyAdapter(viewList);
//        viewpager.setAdapter(myadapter);

        //  开始时显示全部的数据
        view2 = (ListView)findViewById(R.id.show2);
        data = dal.queryAll();
        updateListView();
        //  实现手势页面转换
        mGestureDetector = new GestureDetector(this,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                // e1: 第一次按下的位置 e2 当手离开屏幕 时的位置 velocityX 沿x 轴的速度 velocityY： 沿Y轴方向的速度
                //判断竖直方向移动的大小
                if((e1.getRawX() - e2.getRawX()) >80){// 表示 向右滑动表示下一页
                    startActivity(new Intent(MainActivity.this,StudentActivity.class));
                    finish();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        setListeners();
        //  查询对话框
        button2 = (Button)findViewById(R.id.select);
        button2.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           LayoutInflater factory = LayoutInflater.from(MainActivity.this);
                                           final View textEntryView = factory.inflate(R.layout.select, null);
                                           final EditText editTextName = (EditText) textEntryView.findViewById(R.id.editTextName);
                                           final Spinner editTextIns = (Spinner) textEntryView.findViewById(R.id.editTextIns);
                                           final Spinner editTextSub = (Spinner) textEntryView.findViewById(R.id.editTextSub);
                                            setSpinner(editTextIns,editTextSub);

                                           AlertDialog.Builder ad1 = new AlertDialog.Builder(MainActivity.this);
                                           ad1.setTitle("输入查询条件:");
                                           ad1.setIcon(android.R.drawable.ic_dialog_info);
                                           ad1.setView(textEntryView);
                                           ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {//设置确认键，且对确认键进行监听
                                               @SuppressLint("Range")
                                               public void onClick(DialogInterface dialog, int which) {
                                                   //   开启查询
                                                   //   获取关键字信息
                                                   String keyName = editTextName.getText().toString();
                                                   String keyIns = editTextIns.getSelectedItem().toString();
                                                   String keySub = editTextSub.getSelectedItem().toString();
                                                   StudentDAL dal = new StudentDAL(MainActivity.this);
                                                   data = dal.search(keyName,keyIns,keySub);
                                                   updateListView();
                                               }
                                           });
                                           ad1.show();
                                       }
                                   });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        super.dispatchTouchEvent(ev);
        return mGestureDetector.onTouchEvent(ev);
    }


    public void setSpinner(Spinner institutes,Spinner subjects){
        //  绑定适配器对应的值
        insAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,insList);
        institutes.setAdapter(insAdapter);
        institutes.setSelection(0,true);
        //  绑定适配器对应的值
        subAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,subject1);
        subjects.setAdapter(subAdapter);
        //  设置列表项的监听
        institutes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //  获取选项中的值
                institute = adapterView.getItemAtPosition(position).toString();
                //  根据选项值的不同绑定不同的适配器
                if (institute.equals("计算机学院")){
                    subAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,subject1);
                    subjects.setAdapter(subAdapter);
                }else if (institute.equals("电气学院")) {
                    subAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, subject2);
                    subjects.setAdapter(subAdapter);
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

    //  初始化数据
    private void initData(){
        //  把初始的数据插入到数据库中
        DBOpenHelper dbOpenHelper = DBOpenHelper.getInstance(this);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        //  创建存放数据的ContentValues对象
        ContentValues values = new ContentValues();
        values.put("name","zhangsan");
        values.put("number","3200102033");
        values.put("birthday","2002-1-1");
        values.put("sex","男");
        values.put("institute","计算机学院");
        values.put("subject","软件工程");
        values.put("hobby","游泳");
        values.put("intro","马思清做事认真负责。她是我们班的准备用具的负责人，同时也是纪律委员。当上课铃响时，她会提醒我们坐好，保持安静。如果有人说话，她会提醒他一次，如果他还说，它就会坚决果断地把他的大名写在黑板的一角或直接报告老师。下课了，她又站在了讲台前，提醒大家把下节课的用具准备好，如果有人没有准备好，她就会警告他。下节课室体育的话，她就会站在体育委员的旁边协助她整顿纪律。");
        db.insert("student",null,values);
        values.put("name","wangwu");
        values.put("number","3200102023");
        values.put("birthday","2002-3-3");
        values.put("sex","女");
        values.put("institute","电气学院");
        values.put("subject","电机工程");
        values.put("hobby","跑步");
        values.put("intro","徐浙峰是个爱哭鬼，下课了，我和徐浙峰开了个玩笑，我把徐浙峰的水彩笔藏起来了，到上美术课的时间了，徐浙峰把书包翻了个底朝天，还是找不到，他急得像热锅上的蚂蚁，他很伤心，急得哇哇大哭，怎么也劝不住。");
        db.insert("student",null,values);
    }

    //  写一个方法用来更新ListView
    @SuppressLint("Range")
    private void  updateListView(){
        if (data == null){
            Toast.makeText(this, "没有查询到结果", Toast.LENGTH_LONG).show();
        }else{
            MyAdapter adapter = new MyAdapter(this, data,R.layout.listview,new String[]{"name","number","birthday","sex","icon","institute","subject","intro"}, new int[]{R.id.ListName,R.id.ListNum,R.id.ListBir,R.id.ListSex,R.id.ListIcon,R.id.ListInstitute,R.id.ListSubject,R.id.ListIntro});
            view2.setAdapter(adapter);
        }
//        if (cursor.getCount() == 0){
//            Toast.makeText(this, "没有查询到结果", Toast.LENGTH_LONG).show();
//        }else{
//            while(cursor.moveToNext()){
//                Map<String,Object> map = new HashMap<>();
//                map.put("name",cursor.getString(cursor.getColumnIndex("name")));
//                map.put("number",cursor.getString(cursor.getColumnIndex("number")));
//                map.put("birthday",cursor.getString(cursor.getColumnIndex("birthday")));
//                map.put("sex",cursor.getString(cursor.getColumnIndex("sex")));
//                map.put("institute",cursor.getString(cursor.getColumnIndex("institute")));
//                map.put("subject",cursor.getString(cursor.getColumnIndex("subject")));
//                map.put("icon",R.drawable.p2);
//                map.put("intro",cursor.getString(cursor.getColumnIndex("intro")));
//                data.add(map);
//            }
//        }
    }
    @SuppressLint("NewApi")
    public static void closeStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().build());

    }
    //该方法用于创建显示Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1,1,1,"增加");
        menu.add(2,2,2,"刷新");
        menu.add(3,3,3,"启动电话Activity");
        menu.add(4,4,4,"配置信息");
        menu.add(5,5,5,"查看星期");
        menu.add(6,6,6,"查看天气");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "增加菜单被点击了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,StudentActivity.class);
                startActivity(intent);
                break;
            case 2:
                Toast.makeText(this, "刷新菜单被点击了", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(this, "启动菜单被点击了", Toast.LENGTH_LONG).show();
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this,ActivityPhonePlace.class);
                startActivity(intent1);
                break;
            case 4:
                Toast.makeText(this, "配置菜单被点击了", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this,ActivityConfig.class);
                startActivity(intent2);
                break;
            case 5:
                Toast.makeText(this, "星期菜单被点击了", Toast.LENGTH_LONG).show();
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this,WeekActivity.class);
                startActivity(intent3);
            case 6:
                Toast.makeText(this,"天气菜单被点击了",Toast.LENGTH_LONG).show();;
                Intent intent4 = new Intent();
                intent4.setClass(MainActivity.this,ActivityWeather.class);
                startActivity(intent4);
        }
        return false;
    }


    //  设置ListView长按事件
    private void setListeners(){
        view2.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("菜单栏");
                menu.add(0,0,0,"编辑操作");
                menu.add(0,1,0,"删除操作");
            }
        });

    }
    //  给菜单栏添加事件,利用回调触发
    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case 0:
            //System.out.println("删除"+info.id);
                intent.setClass(MainActivity.this,StudentActivity.class);
                //  得到对应位置的各信息
                pos = (int) view2.getAdapter().getItemId(menuInfo.position);
                Map<String, Object> studentInf = data.get(pos);
                //  得到姓名信息
                intent.putExtra("name",studentInf.get("name").toString());
                intent.putExtra("institute",studentInf.get("institute").toString());
                intent.putExtra("sex",studentInf.get("sex").toString());
                intent.putExtra("subject",studentInf.get("subject").toString());
                intent.putExtra("number",studentInf.get("number").toString());
                intent.putExtra("birthday",studentInf.get("birthday").toString());
                intent.putExtra("intro",studentInf.get("intro").toString());
                startActivity(intent);
                return true;
            case 1:
                //  删除操作
                pos = (int) view2.getAdapter().getItemId(menuInfo.position);
                openDialog("是否要删除","消息");
                return true;
            default:
                return super.onContextItemSelected(item);
        }

    }

    private void openDialog(String msg,String title){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Map<String, Object> studentInf = data.get(pos);
                                StudentDAL dal = new StudentDAL(MainActivity.this);
                                dal.deleteStudentByName(new String[]{studentInf.get("name").toString(),studentInf.get("number").toString()});
                                data = dal.queryAll();
                                updateListView();
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .show();
    }

}