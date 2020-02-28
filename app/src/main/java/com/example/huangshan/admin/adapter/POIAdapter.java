package com.example.huangshan.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.MyPOI;

import java.util.ArrayList;
import java.util.List;

public class POIAdapter extends BaseAdapter {

    private Context context;
    private List<MyPOI> poiList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public POIAdapter(Context context,List<MyPOI> poiList) {
        this.context = context;
        this.poiList = poiList;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return poiList.size();
    }

    @Override
    public MyPOI getItem(int position) {
        return poiList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_notification_poi_item, null);
            new POIViewHolder(convertView);
        }
        POIViewHolder viewHolder = (POIViewHolder) convertView.getTag();
        viewHolder.poiImg.setImageResource(getItem(position).getPoiImg());
        viewHolder.poiText.setText(getItem(position).getPoiText());
        return convertView;
    }

    public static class POIViewHolder{
        ImageView poiImg;
        TextView poiText;
        public POIViewHolder(View convertView){
            poiImg = (ImageView)convertView.findViewById(R.id.notification_poi_img);
            poiText = (TextView)convertView.findViewById(R.id.notification_poi_text);
            convertView.setTag(this);
        }
    }
}
