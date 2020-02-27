package com.example.huangshan.admin.activity;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.huangshan.admin.bean.Notification;
import com.example.huangshan.admin.service.NotificationService;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

/**
 * 管理员发布通知的activity
 */
public class SendNotificationActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.notifications_send_back_btn)
    ImageView back;
    @BindView(R.id.notification_send_btn)
    TextView sendNotification;
    @BindView(R.id.notification_input_title)
    EditText titleEditText;
    @BindView(R.id.notification_input_type_group)
    RadioGroup radioGroup;
    @BindView(R.id.notification_input_content)
    EditText contentEditText;

    private static final String TAG = "SendNotificationActivit";
    public String type = new String();

    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    private Gson gson = new Gson();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        //绑定控件
        ButterKnife.bind(this);
        //设置响应
        back.setOnClickListener(this);
        sendNotification.setOnClickListener(this);
        //初始化缓存
        preferences = this.getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();

        //设置单选框的响应
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton)findViewById(checkedId);
                type = button.getTag().toString();
            }
        });

        //创建通知栏Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String channelId = "adminNotification";
            String channelName = "adminNotification";
            int importance = NotificationManager.IMPORTANCE_MAX;
            createNotificationChannel(channelId, channelName, importance);
        }
    }

    /**
     * 获得页面上的通知数据
     */
    private Notification getData() {
        String title = titleEditText.getText().toString();
        String content = contentEditText.getText().toString();
        String sendAdminName = preferences.getString("name",null);
        String sendTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        Notification notification = new Notification();
        notification.setSendTime(sendTime);
        notification.setSendAdminName(sendAdminName);
        notification.setTitle(title);
        notification.setContent(content);
        notification.setType(type);
        notification.setIsClose("0");
        return notification;
    }


    /**
     * 创建NotificationChannel，Android8后要求的
     * @param channelId
     * @param channelName
     * @param importance
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{0,50,100,150});
        //todo  这里的铃声有点问题
        channel.setSound(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"/MIUI/.ringtone/2.mp3")),null);
        notificationManager.createNotificationChannel(channel);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.notifications_send_back_btn:
                finish();
                break;
            case R.id.notification_send_btn:
                //发布通知
                Notification notification = getData();
                sendData(notification);
                //send();
                break;
                default:break;
        }
    }

    /**
     * 发送请求
     * @param notification
     */
    @SuppressLint("CheckResult")
    private void sendData(Notification notification) {
        if (checkData(notification)){
            NotificationService notificationService = retrofit.create(NotificationService.class);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(notification));
            notificationService.addOne(requestBody)
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                String data = gson.toJson(resultObj.getData());
                                Notification myData = gson.fromJson(data,Notification.class);
                                //开启通知栏
                                showAndriodNotification(myData);
                                //显示对话框
                                changeTips("成功！","此通知发布成功！",SweetAlertDialog.SUCCESS_TYPE);
                            }else {
                                changeTips("失败！","服务器繁忙，请稍后再试！",SweetAlertDialog.ERROR_TYPE);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG,throwable.getMessage());
                        }
                    });
        }
    }

    /**
     * 显示Android的通知栏
     * @param myData
     */
    private void showAndriodNotification(Notification myData) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("notification",myData);
        Intent intent = new Intent(getBaseContext(), NotificationInfoActivity.class);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getBaseContext(),0,intent,0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        android.app.Notification notification = new NotificationCompat.Builder(getBaseContext(), "adminNotification")
                    .setContentTitle(myData.getTitle())
                    .setContentText(myData.getContent())
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.notification_icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.notification_icon))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setNumber(1)
                    .setContentIntent(pendingIntent)
                    .build();
        manager.notify(1, notification);
    }

    /**
     * 校验
     * @param notification
     * @return
     */
    private boolean checkData(Notification notification) {
        if (TextUtils.isEmpty(notification.getTitle()) || TextUtils.isEmpty(notification.getContent())){
            Toast.makeText(this,"输入值为空!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * 显示对话框
     */
    private void changeTips(String titleText, String contentText,int type) {
        new SweetAlertDialog(this,type)
                .setTitleText(titleText)
                .setContentText(contentText)
                .showCancelButton(false)
                .setConfirmText("OK")
                .setCancelClickListener(null)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                        if (type == SweetAlertDialog.SUCCESS_TYPE){
                            SendNotificationActivity.this.setResult(0);
                            finish();
                        }
                    }
                }).show();
    }
}
