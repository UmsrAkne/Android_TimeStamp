package com.example.timestamper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater = null;
    ArrayList<TimeStamp> timeStampList;


    public MyAdapter(Context context){
        this.context = context;
        this.layoutInflater =
                (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    public void setTimeStampList(ArrayList<TimeStamp> list){
        this.timeStampList = list;
    }

    @Override
    public int getCount() {
        return timeStampList.size();
    }

    @Override
    public Object getItem(int position) {
        return timeStampList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return timeStampList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.custom_listview_row,parent,false);
        }

        TextView nameTv = (TextView) convertView.findViewById(R.id.name);
        nameTv.setText(timeStampList.get(position).getCreationDateString());

        String elTImeStr =
                String.valueOf(timeStampList.get(position).getTime());

        ((TextView) convertView.findViewById(R.id.elapsedTime)).
                setText(elTImeStr);

        if(timeStampList.get(position).IsCountDownTimer()){
            convertView.setBackgroundColor(Color.rgb(000,000,120));
        }
        else{
            convertView.setBackgroundColor(Color.rgb(0,0,0));
        }
        return convertView;
    }

    /**
     * アダプターに登録されているリストに要素を追加し、アダプターの表示を更新します。
     * @param element
     */
    public void add(TimeStamp element){
        this.timeStampList.add(element);
        notifyDataSetChanged();
    }
}
