package com.example.huangshan.tourist.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.huangshan.R;
import com.example.huangshan.admin.activity.NotificationInfoActivity;
import com.example.huangshan.admin.adapter.NotificationListAdapter;
import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.admin.httpservice.NotificationService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.gson.Gson;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;

public class NotificationTouristListAdapter extends RecyclerView.Adapter<NotificationTouristListAdapter.NotificationViewHolder> {

    private static final String TAG = "NotificationListAdapter";

    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    private Gson gson = new Gson();


    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        CardView infoRoot;
        ImageButton typeBtn;
        TextView sendTime;
        TextView title;
        TextView content;
        TextView sendAdminName;
        TextView isClose;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            infoRoot = itemView.findViewById(R.id.tourist_notification_item_info_root);
            typeBtn = itemView.findViewById(R.id.tourist_notification_type_btn);
            sendTime = itemView.findViewById(R.id.tourist_notification_item_sendTime);
            title = itemView.findViewById(R.id.tourist_notification_item_title);
            content = itemView.findViewById(R.id.tourist_notification_item_content);
            sendAdminName = itemView.findViewById(R.id.tourist_notification_send_admin_name);
            isClose = itemView.findViewById(R.id.tourist_notification_item_isclose);
        }
    }

    private Context mContext;
    private List<Notification> notifications;

    public NotificationTouristListAdapter(Context context,List<Notification> objects){
        this.mContext = context;
        this.notifications = objects;
    }
    @Override
    public NotificationTouristListAdapter.NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notification_tourist_list,parent,false);
        NotificationTouristListAdapter.NotificationViewHolder holder = new NotificationTouristListAdapter.NotificationViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotificationTouristListAdapter.NotificationViewHolder holder, int i) {

        //显示信息
        holder.sendTime.setText(notifications.get(i).getSendTime());
        holder.title.setText(notifications.get(i).getTitle());
        holder.content.setText(notifications.get(i).getContent());
        holder.sendAdminName.setText(notifications.get(i).getSendAdminName());

        //设置type
        String type = notifications.get(i).getType();
        Resources resources = mContext.getResources();
        if ("0".equals(type)){
            //紧急通知
            Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.btn_circle_reds,null);
            holder.typeBtn.setBackground(drawable);
        }else if ("1".equals(type)){
            //客流预警
            Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.btn_circle_yellow,null);
            holder.typeBtn.setBackground(drawable);
        }else if ("2".equals(type)){
            //天气预警
            Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.btn_circle_blue,null);
            holder.typeBtn.setBackground(drawable);
        }else if ("3".equals(type)){
            //优惠通知
            Drawable drawable = ResourcesCompat.getDrawable(resources,R.drawable.btn_circle_green,null);
            holder.typeBtn.setBackground(drawable);
        }
        //设置过期显示
        String isClose = notifications.get(i).getIsClose();
        if (isClose.equals("1")){
            holder.isClose.setVisibility(VISIBLE);
        }


        //设置通知详情响应
        holder.infoRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDetails(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    /**
     * 跳转到通知详情
     * @param i
     */
    private void goToDetails(int i) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("notification", notifications.get(i));
        Intent intent = new Intent(mContext, NotificationInfoActivity.class);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

}
