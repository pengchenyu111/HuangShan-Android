package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Complaint;
import com.example.huangshan.admin.httpservice.AdminService;
import com.example.huangshan.admin.httpservice.ComplaintService;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.httpservice.FileService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.http.UploadFileResponse;
import com.example.huangshan.tourist.bean.SpinnerItemWithImg;
import com.example.huangshan.tourist.ui.adapter.SpinnerItemWithImgAdapter;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServeComplaintActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.serve_complaint_back_btn) ImageView backBtn;
    @BindView(R.id.serve_complaint_submit_btn) TextView submitBtn;

    @BindView(R.id.serve_complaint_spot) EditText spotText;
    @BindView(R.id.serve_complaint_object) EditText objectText;
    @BindView(R.id.serve_complaint_type) TextView complaintTypeView;
    @BindView(R.id.serve_complaint_spinner_btn) Spinner spinner;
    @BindView(R.id.serve_complaint_reason) EditText reasonText;
    @BindView(R.id.serve_complaint_evidence) ImageView evidenceImg;
    @BindView(R.id.serve_complaint_complainant) EditText complainantText;
    @BindView(R.id.serve_complaint_phone) EditText phoneText;

    private static final String TAG = "ServeComplaintActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //缓存
    private SharedPreferences sharedPreferences;
    //上传凭据
    public String imgPath;
    public static final int CHOOSE_IMG = 2;
    private String evidenceUrl;
    //对话框
    private SweetAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_complaint);

        ButterKnife.bind(this);

        addClick();
        init();
        showSpinner();

    }

    private void init() {
        //网络初始化
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //缓存初始化
        sharedPreferences = this.getSharedPreferences("loginUser", MODE_PRIVATE);
        complainantText.setText(sharedPreferences.getString("name",null));
        phoneText.setText(sharedPreferences.getString("phone",null));
    }

    private void addClick() {
        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        evidenceImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serve_complaint_back_btn:
                finish();
                break;
            case R.id.serve_complaint_submit_btn:
                uploadFile();
                break;
            case R.id.serve_complaint_evidence:
                showImg();
                break;
                default:
                    break;
        }
    }

    /**
     * 展示下拉选择框
     */
    private void showSpinner() {
        SpinnerItemWithImg scenic = new SpinnerItemWithImg(R.mipmap.scenic_colored,"景区");
        SpinnerItemWithImg hotel = new SpinnerItemWithImg(R.mipmap.hotel_colored,"住宿");
        SpinnerItemWithImg delicacies = new SpinnerItemWithImg(R.mipmap.delicacies_colored,"餐饮");
        SpinnerItemWithImg tourOrg = new SpinnerItemWithImg(R.mipmap.tour_org,"旅行社");
        SpinnerItemWithImg tourGuide = new SpinnerItemWithImg(R.mipmap.tour_guide,"导游");
        List<SpinnerItemWithImg> spinnerItemWithImgs = new ArrayList<>();
        spinnerItemWithImgs.add(scenic);
        spinnerItemWithImgs.add(hotel);
        spinnerItemWithImgs.add(delicacies);
        spinnerItemWithImgs.add(tourOrg);
        spinnerItemWithImgs.add(tourGuide);

        SpinnerItemWithImgAdapter adapter = new SpinnerItemWithImgAdapter(this,spinnerItemWithImgs);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                complaintTypeView.setText(spinnerItemWithImgs.get(position).getText());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                complaintTypeView.setText("景区");
            }

        });
    }

    /**
     * 打开相册
     */
    private void showImg(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CHOOSE_IMG);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CHOOSE_IMG:
                switch (resultCode){
                    case RESULT_OK:
                        Uri uri = data.getData();
                        try {
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            if(selectedImage!=null) {
                                Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imgPath = cursor.getString(columnIndex);
                                cursor.close();
                                Glide.with(this).load(imgPath).into(evidenceImg);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }
        }
    }

    /**
     * 上传到文件服务器
     */
    @SuppressLint("CheckResult")
    private void uploadFile() {

        dialog = new SweetAlertDialog(this,SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("提交中");
        dialog.setCancelable(false);
        dialog.show();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(30000, TimeUnit.MILLISECONDS)
                .readTimeout(30000, TimeUnit.MILLISECONDS)
                .writeTimeout(30000, TimeUnit.MILLISECONDS);
        OkHttpClient okHttpClient = builder.build();

        //使用该OkHttpClient创建一个Retrofit对象
        Retrofit mRetrofit = new Retrofit.Builder()
                //添加Gson数据格式转换器支持
                .addConverterFactory(GsonConverterFactory.create())
                //添加RxJava语言支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //指定网络请求client
                .client(okHttpClient)
                .baseUrl("http://101.37.173.73:9000/")
                .build();
        FileService fileService = mRetrofit.create(FileService.class);

        String account = (String)sharedPreferences.getString("account",null);
        File file = new File(imgPath);
        RequestBody requestFile = RequestBody.Companion.create( file,MediaType.Companion.parse("image/*"));
        RequestBody requestBody =new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),requestFile)
                .addFormDataPart("account",account)
                .build();

        //上传到文件服务器
        fileService
                .uploadComplaintEvidence(requestBody)
                .compose(RxSchedulers.<UploadFileResponse>io_main())
                .subscribe(new Consumer<UploadFileResponse>() {
                    @Override
                    public void accept(UploadFileResponse uploadFileResponse) throws Exception {
                        evidenceUrl = uploadFileResponse.getFileUri();
                        updateDB(account);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"出错===》"+ throwable.getMessage());
                        dialog.dismiss();
                        Toast.makeText(ServeComplaintActivity.this,"服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 更新数据库
     * @param account
     */
    @SuppressLint("CheckResult")
    private void updateDB(String account) {
        ComplaintService complaintService = retrofit.create(ComplaintService.class);
        Complaint complaint = new Complaint();
        complaint.setSpot(spotText.getText().toString());
        complaint.setComplaintObject(objectText.getText().toString());
        complaint.setComplaintType(complaintTypeView.getText().toString());
        complaint.setComplaintReason(reasonText.getText().toString());
        complaint.setEvidencePhoto(evidenceUrl);
        complaint.setComplainantName(complainantText.getText().toString());
        complaint.setComplainantPhone(phoneText.getText().toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(complaint));
        complaintService.addleOne(requestBody)
                .compose(RxSchedulers.<ResultObj>io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            dialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            dialog.setTitleText("成功")
                                    .setContentText("我们会尽快反馈给您处理结果！")
                                    .setCancelClickListener(null)
                                    .setConfirmText("OK")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            dialog.dismissWithAnimation();
                                            finish();
                                        }
                                    }).show();
                        }else {
                            dialog.dismiss();
                            Toast.makeText(ServeComplaintActivity.this,"似乎除了一些问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.getMessage());
                        dialog.dismiss();
                        Toast.makeText(ServeComplaintActivity.this,"服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
