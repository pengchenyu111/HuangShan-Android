package com.example.huangshan.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.AdminScenicManage;
import com.example.huangshan.admin.bean.ScenicManage;

import java.util.List;

/**
 * 适配显示管理员管理的多个地点的
 */
public class OnesScenicManageAdapter extends RecyclerView.Adapter<OnesScenicManageAdapter.OnesManageViewHolder> {

    private static final String TAG = "OnesScenicManageAdapter";

    private Context mContext;
    private List<AdminScenicManage> adminManages;

    public static class OnesManageViewHolder extends RecyclerView.ViewHolder {

        TextView scenicNameView;
        TextView adminWorkDayView;
        TextView adminWorkTimeView;

        public OnesManageViewHolder(View itemView) {
            super(itemView);
            scenicNameView = itemView.findViewById(R.id.ones_manage_scenic_name);
            adminWorkDayView = itemView.findViewById(R.id.ones_manage_scenic_day);
            adminWorkTimeView = itemView.findViewById(R.id.ones_manage_scenic_time);
        }
    }

    //构造函数
    public OnesScenicManageAdapter(Context context,List<AdminScenicManage> adminManages){
        this.mContext = context;
        this.adminManages = adminManages;
    }

    @Override
    public OnesManageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ones_scenic_manage,parent,false);
        OnesScenicManageAdapter.OnesManageViewHolder holder = new OnesScenicManageAdapter.OnesManageViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(OnesManageViewHolder onesManageViewHolder, int i) {
        onesManageViewHolder.scenicNameView.setText(adminManages.get(i).getScenicName());
        onesManageViewHolder.adminWorkDayView.setText(adminManages.get(i).getAdminWorkDay());
        onesManageViewHolder.adminWorkTimeView.setText(adminManages.get(i).getAdminWorkTime());
    }

    @Override
    public int getItemCount() {
        return adminManages.size();
    }


}
