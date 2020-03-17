package com.example.huangshan.tourist.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.route.WalkStep;
import com.example.huangshan.R;
import com.example.huangshan.utils.AmapUtil;

import java.util.List;

public class RouteDetailAdapter extends BaseAdapter {

    private Context mContext;
    private List<WalkStep> walkSteps;

    public RouteDetailAdapter(Context mContext, List<WalkStep> walkSteps) {
        this.mContext = mContext;
        this.walkSteps = walkSteps;
    }


    @Override
    public int getCount() {
        return walkSteps.size()+2;
    }

    @Override
    public WalkStep getItem(int position) {
        return walkSteps.get(position-1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RouteDetailViewHolder holder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_route_item, parent,false);
            holder = new RouteDetailViewHolder();
            holder.normal = convertView.findViewById(R.id.route_detail_item_normal);
            holder.instructionImg = convertView.findViewById(R.id.route_detail_dir_icon);
            holder.descriptionText = convertView.findViewById(R.id.route_detail_line_name);
            holder.startEnd = convertView.findViewById(R.id.route_detail_item_start_end);
            holder.instructionStartEndImg = convertView.findViewById(R.id.route_detail_start_end_dir_icon);
            holder.descriptionStartEndText = convertView.findViewById(R.id.route_detail_start_end_line_name);
            convertView.setTag(holder);
        }else {
            holder = (RouteDetailViewHolder)convertView.getTag();
        }
        if (position == 0){
            holder.normal.setVisibility(View.GONE);
            holder.startEnd.setVisibility(View.VISIBLE);
            holder.instructionStartEndImg.setImageResource(R.mipmap.navi_dir_start);
            holder.descriptionStartEndText.setText("出发");
        }else if (position == getCount() -1){
            holder.normal.setVisibility(View.GONE);
            holder.startEnd.setVisibility(View.VISIBLE);
            holder.instructionStartEndImg.setImageResource(R.mipmap.navi_dir_end);
            holder.descriptionStartEndText.setText("到达终点");
        }else {
            holder.normal.setVisibility(View.VISIBLE);
            holder.startEnd.setVisibility(View.GONE);
            holder.instructionImg.setImageResource(AmapUtil.getWalkActionID(getItem(position).getAction()));
            holder.descriptionText.setText(getItem(position).getInstruction());
        }
        return convertView;
    }

    public static class RouteDetailViewHolder {
        public RelativeLayout normal;
        public ImageView instructionImg;
        public TextView descriptionText;
        public RelativeLayout startEnd;
        public ImageView instructionStartEndImg;
        public TextView descriptionStartEndText;
    }
}
