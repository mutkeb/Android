package com.example.studentmgr2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Icon;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class MyAdapter extends SimpleAdapter {
    Context context ;
    List<? extends Map<String, ?>> data;
    int resource;
    String[] from ;
    int[] to;
    LayoutInflater inflater = null;
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public MyAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data = data;
        this.resource = resource;
        this.from = from;
        this.to = to;
        inflater = LayoutInflater.from(context); //实例化这个控件
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if( convertView == null )
        {
            convertView = inflater.inflate(R.layout.listview, null );//这里面的video_list就是我们要更改的ListView布局。
        }
        SharedPreferences share = context.getSharedPreferences("config",Context.MODE_PRIVATE);
        TextView t1 = (TextView) convertView.findViewById(R.id.ListName);
        TextView t2 = (TextView) convertView.findViewById(R.id.ListSubject);
        TextView t3 = (TextView) convertView.findViewById(R.id.ListSex);
        TextView t4 = (TextView) convertView.findViewById(R.id.ListNum);
        TextView t5 = (TextView) convertView.findViewById(R.id.ListInstitute);
        TextView t6 = (TextView) convertView.findViewById(R.id.ListBir);
        TextView t8 = (TextView) convertView.findViewById(R.id.ListIntro);
        ImageView t7 = (ImageView) convertView.findViewById(R.id.ListIcon);

        Map information = data.get(position);
        t1.setText(information.get("name").toString());
        t2.setText(information.get("subject").toString());
        t3.setText(information.get("sex").toString());
        t4.setText(information.get("number").toString());
        t5.setText(information.get("institute").toString());
        t6.setText(information.get("birthday").toString());
        t7.setImageResource((Integer) information.get("icon"));
        t8.setText(information.get("intro").toString());

        if (!share.getString("fontSize","").equals("")) {
            t1.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            t2.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            t3.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            t4.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            t5.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
            t6.setTextSize(TypedValue.COMPLEX_UNIT_PX, Integer.parseInt(share.getString("fontSize", "")));
        }
        return convertView;
    }


}
