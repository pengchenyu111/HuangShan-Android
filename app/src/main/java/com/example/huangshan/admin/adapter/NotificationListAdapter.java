package com.example.huangshan.admin.adapter;

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
import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.admin.service.NotificationService;
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

/**
 * 该Adapter适用于通知列表
 */
public class NotificationListAdapter extends RecyclerSwipeAdapter<NotificationListAdapter.NotificationViewHolder>{

    private static final String TAG = "NotificationListAdapter";

    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    private Gson gson = new Gson();


    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        CardView infoRoot;
        ImageButton typeBtn;
        TextView sendTime;
        TextView title;
        TextView content;
        TextView sendAdminName;
        TextView isClose;
        ImageView details;
        ImageView outDateBtn;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.notification_list_root);
            infoRoot = itemView.findViewById(R.id.notification_item_info_root);
            typeBtn = itemView.findViewById(R.id.notification_type_btn);
            sendTime = itemView.findViewById(R.id.notification_item_sendTime);
            title = itemView.findViewById(R.id.notification_item_title);
            content = itemView.findViewById(R.id.notification_item_content);
            sendAdminName = itemView.findViewById(R.id.notification_send_admin_name);
            isClose = itemView.findViewById(R.id.notification_item_isclose);
            details = itemView.findViewById(R.id.notification_item_info);
            outDateBtn = itemView.findViewById(R.id.notification_item_outdate_btn);
        }
    }

    private Context mContext;
    private List<Notification> notifications;

    public NotificationListAdapter(Context context,List<Notification> objects){
        this.mContext = context;
        this.notifications = objects;
    }
    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_notificationlist_item,parent,false);
        NotificationViewHolder holder = new NotificationViewHolder(view);
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int i) {

        //动画
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewWithTag("swipe_menu"));

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

        //设置通知详情响应
        holder.details.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               goToDetails(i);
            }
        });
        //设置过期
        holder.outDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //初始化缓存
                preferences= mContext.getSharedPreferences("loginUser", MODE_PRIVATE);
                String myName = preferences.getString("name",null);
                if (!myName.equals(notifications.get(i).getSendAdminName())){
                    changeTips("失败！", "您不是该通知的创建人！",SweetAlertDialog.ERROR_TYPE);
                }else {
                    setClose(notifications.get(i),holder);
                }
            }
        });
    }

    /**
     * 向服务器请求关闭
     * @param notification
     * @param holder
     */
    @SuppressLint("CheckResult")
    private void setClose(Notification notification, NotificationViewHolder holder) {
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        NotificationService notificationService = retrofit.create(NotificationService.class);
        notificationService.closeOne(notification.getId())
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            changeTips("成功！", "该通知已成功关闭！",SweetAlertDialog.SUCCESS_TYPE);
                            holder.isClose.setVisibility(View.VISIBLE);
                        }else {
                            Toast.makeText(mContext,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(mContext,"服务器繁忙，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 显示对话框
     */
    private void changeTips(String titleText, String contentText,int type) {
        new SweetAlertDialog(mContext,type)
                .setTitleText(titleText)
                .setContentText(contentText)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }).show();
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

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.notification_list_root;
    }

}
