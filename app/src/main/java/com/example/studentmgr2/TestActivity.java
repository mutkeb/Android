//package com.example.studentmgr2;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.viewpager.widget.ViewPager;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TestActivity extends AppCompatActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//        LayoutInflater inflater = getLayoutInflater().from(this);
//        View view1 = inflater.inflate(R.layout.activity_main, null);
//        View view2 = inflater.inflate(R.layout.student_main, null);
//
//        List<View> viewList = new ArrayList<>();
//        viewList.add(view1);
//        viewList.add(view2);
//
//        ViewPager viewpager = findViewById(R.id.viewpager);
//        MyAdapter myadapter = new MyAdapter(viewList);
//        viewpager.setAdapter(myadapter);
//    }
//}
