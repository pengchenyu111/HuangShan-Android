package com.example.huangshan.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.admin.bean.Notification;
import com.gongwen.marqueen.MarqueeFactory;

/**
 * 管理员里轮播用户投诉的banner
 */
public class AdminFeedbackBanner extends MarqueeFactory<LinearLayout, Complaint> {

    private LayoutInflater inflater;

    public AdminFeedbackBanner(Context mContext) {
        super(mContext);
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    protected LinearLayout generateMarqueeItemView(Complaint data) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.myview_admin_feedback_banner,null);
        ((TextView)view.findViewById(R.id.notification_feedback_banner_object)).setText(data.getComplaintObject());
        ((TextView)view.findViewById(R.id.notification_feedback_banner_type)).setText(data.getComplaintType());
        ((TextView)view.findViewById(R.id.notification_feedback_banner_spot)).setText(data.getSpot());
        ((TextView)view.findViewById(R.id.notification_feedback_banner_others)).setText(data.getComplainantName() + " 投诉于 " + data.getComplaintTime());
        return view;
    }
}
