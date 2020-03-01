package com.example.huangshan.admin.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.huangshan.R;
import com.example.huangshan.admin.activity.ComplaintInfoActivity;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.http.RetrofitManager;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Retrofit;

/**
 * 该adapter适用于用户投诉列表
 */
public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.ComplaintListViewHolder> {

    private static final String TAG = "ComplaintListAdapter";
    private Context context;
    private List<Complaint> complaints;
    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    private Gson gson = new Gson();

    public ComplaintListAdapter(Context context, List<Complaint> complaints) {
        this.context = context;
        this.complaints = complaints;
    }

    public static class ComplaintListViewHolder extends RecyclerView.ViewHolder{
        CardView rootContainer;
        TextView complaintTime;
        TextView comliantName;
        TextView complaintObject;
        TextView complaintReason;
        TextView isHandle;
        public ComplaintListViewHolder(@NonNull View itemView) {
            super(itemView);
            rootContainer = itemView.findViewById(R.id.complaint_item_info_root);
            complaintTime = itemView.findViewById(R.id.complaint_item_time);
            comliantName = itemView.findViewById(R.id.complaint_item_compliant_name);
            complaintObject = itemView.findViewById(R.id.complaint_item_object);
            complaintReason = itemView.findViewById(R.id.complaint_item_reason);
            isHandle = itemView.findViewById(R.id.complaint_item_ishandle);
        }
    }


    @Override
    public ComplaintListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_complaint_item, parent,false);
        ComplaintListViewHolder viewHolder = new ComplaintListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComplaintListViewHolder holder, int i) {
        //显示内容
        Complaint complaint = complaints.get(i);
        holder.complaintTime.setText(complaint.getComplaintTime());
        holder.comliantName.setText(complaint.getComplainantName());
        holder.complaintObject.setText(complaint.getComplaintObject());
        holder.complaintReason.setText(complaint.getComplaintReason());
        if (complaint.getIsHandle().equals("0")){
            holder.isHandle.setVisibility(View.VISIBLE);
        }

        //设置响应
        holder.rootContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("complaint",complaint);
                Intent intent = new Intent(context, ComplaintInfoActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }
}
