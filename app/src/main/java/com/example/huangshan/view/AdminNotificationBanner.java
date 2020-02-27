package com.example.huangshan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Notification;
import com.gongwen.marqueen.MarqueeFactory;

/**
 * 用于管理员通知界面上的通知跑马灯
 */
public class AdminNotificationBanner extends MarqueeFactory<LinearLayout, Notification> {

    private LayoutInflater inflater;

    public AdminNotificationBanner(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    protected LinearLayout generateMarqueeItemView(Notification data) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.myview_admin_notification_banner,null);
        ((TextView)view.findViewById(R.id.notification_admin_banner_title)).setText(data.getTitle());
        if (data.getType().equals("0")){
            ((TextView)view.findViewById(R.id.notification_admin_banner_type)).setText("紧急事件");
        }else if (data.getType().equals("1")){
            ((TextView)view.findViewById(R.id.notification_admin_banner_type)).setText("客流预警");
        }else if (data.getType().equals("2")){
            ((TextView)view.findViewById(R.id.notification_admin_banner_type)).setText("天气预警");
        }else if (data.getType().equals("3")){
            ((TextView)view.findViewById(R.id.notification_admin_banner_type)).setText("优惠通知");
        }
        ((TextView)view.findViewById(R.id.notification_admin_banner_others)).setText(data.getSendAdminName() + " " + data.getSendTime());
        return view;
    }
}
