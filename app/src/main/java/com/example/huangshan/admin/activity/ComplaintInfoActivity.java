package com.example.huangshan.admin.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.admin.service.ComplaintService;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.utils.StatusBarUtil;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class ComplaintInfoActivity extends BaseActivity implements View.OnClickListener{

    @BindView(R.id.complaint_info_back_btn) ImageView backBtn;
    @BindView(R.id.complaint_info_handle_btn) TextView handleBtn;
    @BindView(R.id.complaint_info_object) TextView objectView;
    @BindView(R.id.complaint_info_spot) TextView spotView;
    @BindView(R.id.complaint_info_reason) TextView reasonView;
    @BindView(R.id.complaint_info_evidence_photo) ImageView evidencePhoto;
    @BindView(R.id.complaint_info_complainant_name) TextView complainantNameView;
    @BindView(R.id.complaint_info_complainant_phone) TextView complainantPhoneView;
    @BindView(R.id.complaint_info_handle_admin_name) TextView handleAdminNameView;
    @BindView(R.id.complaint_info_handle_time) TextView handleTimeView;
    @BindView(R.id.complaint_info_handle_message_1) TextView handleMessageView1;
    @BindView(R.id.complaint_info_handle_message_0) EditText handleMessageView0;

    private static final String TAG = "ComplaintInfoActivity";
    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    private Gson gson = new Gson();
    //传过来的对象
    private Complaint complaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_info);
        //绑定控件
        ButterKnife.bind(this);
        //设置响应
        backBtn.setOnClickListener(this::onClick);
        handleBtn.setOnClickListener(this::onClick);
        //状态栏透明
        StatusBarUtil.transparentAndDark(this);
        //获取传来的对象
        Intent intent = getIntent();
        complaint = (Complaint) intent.getSerializableExtra("complaint");
        //初始化缓存
        preferences = this.getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();

        //显示详情
        showData(complaint);
    }

    /**
     * 将数据显示到界面上
     */
    private void showData(Complaint complaint) {
        objectView.setText(complaint.getComplaintObject());
        spotView.setText(complaint.getSpot());
        reasonView.setText(complaint.getComplaintReason());
        Glide.with(this)
                .load(complaint.getEvidencePhoto())
                .placeholder(R.mipmap.loading_1)
                .into(evidencePhoto);
        complainantNameView.setText(complaint.getComplainantName());
        complainantPhoneView.setText(complaint.getComplainantPhone());
        if (complaint.getIsHandle().equals("0")){
            //未处理
            handleAdminNameView.setText("未处理");
            handleTimeView.setText("未处理");
        }else if (complaint.getIsHandle().equals("1")){
            //已处理
            handleAdminNameView.setText(complaint.getHandleAdminName());
            handleTimeView.setText(complaint.getHandleTime());
            handleMessageView0.setVisibility(View.GONE);
            handleMessageView1.setVisibility(View.VISIBLE);
            handleMessageView1.setText(complaint.getHandleMessage());
            handleBtn.setVisibility(View.GONE);
        }
    }

    /**
     * 显示对话框
     */
    private void showDialog(String titleText, String contentText,int type) {
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
                    }
                }).show();
    }

    /**
     * 发送处理请求
     */
    @SuppressLint("CheckResult")
    private void handleComplaint() {
        String handleMessage = handleMessageView0.getText().toString();
        if (!handleMessage.isEmpty()){
            String handleAdminName = preferences.getString("name",null);
            ComplaintService complaintService = retrofit.create(ComplaintService.class);
            Map<String, String> map = new HashMap<>();
            map.put("handleAdminName",handleAdminName);
            map.put("handleMessage",handleMessage);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
            complaintService.handleOne(complaint.getId(),requestBody)
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                showDialog("成功！","您已成功处理这条用户投诉！", SweetAlertDialog.SUCCESS_TYPE);
                                String data = gson.toJson(resultObj.getData());
                                Complaint handledComplaint = gson.fromJson(data,Complaint.class);
                                showData(handledComplaint);
                            }else {
                                showDialog("失败！","请检查您的网络环境或输入！",SweetAlertDialog.ERROR_TYPE);
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG,throwable.getMessage());
                            Toast.makeText(ComplaintInfoActivity.this,"服务器繁忙，请稍候再试!",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this,"请输入反馈信息!",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.complaint_info_back_btn:
                finish();
                break;
            case R.id.complaint_info_handle_btn:
                handleComplaint();
                break;
                default:
                    break;
        }
    }


}
