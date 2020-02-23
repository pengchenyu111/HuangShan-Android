package com.example.huangshan.admin.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.example.huangshan.R;
import com.example.huangshan.admin.adapter.OnesScenicManageAdapter;
import com.example.huangshan.admin.bean.Admin;
import com.example.huangshan.admin.bean.AdminScenicManage;
import com.example.huangshan.admin.service.AdminService;
import com.example.huangshan.admin.service.ScenicManageService;
import com.example.huangshan.common.service.FileService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.http.UploadFileResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * 管理员的个人信息
 */
public class AdminSelfInfoFragment extends Fragment implements View.OnClickListener{


    @BindView(R.id.admin_self_info_headicon) ImageView headIconView;
    @BindView(R.id.admin_self_info_acount) TextView accountView;
    @BindView(R.id.admin_self_info_name) TextView nameView;
    @BindView(R.id.admin_self_info_sex) TextView sexView;
    @BindView(R.id.admin_self_info_age) TextView ageView;
    @BindView(R.id.admin_self_info_workyear) TextView workYearView;
    @BindView(R.id.admin_self_info_role) TextView roleNameView;
    @BindView(R.id.admin_self_info_phone) TextView phoneView;
    @BindView(R.id.admin_self_info_introduction) TextView introductionView;
    @BindView(R.id.admin_self_info_null_spot) TextView nullSpot;

    //按钮
    @BindView(R.id.admin_slef_info_back_btn) ImageView backBtn;
    @BindView(R.id.admin_self_info_phone_root1) LinearLayout phoneRootView;
    @BindView(R.id.admin_self_info_introduction_root1) LinearLayout introductionRootView;
    //工作地点
    @BindView(R.id.admin_manage_spots_recycler_view) RecyclerView recyclerView;

    private View view;
    private OnesScenicManageAdapter adapter;

    private static final String TAG = "AdminSelfInfoFragment";
    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    public Retrofit retrofit;
    //更新头像
    public String imgPath;
    public static final int CHOOSE_IMG = 2;
    private String dbHeadIconUrl;

    public AdminSelfInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_admin_self_info, container, false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        backBtn.setOnClickListener(this);
        headIconView.setOnClickListener(this);
        phoneRootView.setOnClickListener(this);
        introductionRootView.setOnClickListener(this);
        //初始化缓存
        preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //设置基础数据
        setAdminBaseData();
        //获取工作地点
        getManageSpots();

        return view;
    }

    /**
     * 获取工作地点
     */
    @SuppressLint("CheckResult")
    private void getManageSpots() {
        int adminId = (int)preferences.getInt("id",1);
        ScenicManageService scenicManageService = retrofit.create(ScenicManageService.class);
        scenicManageService.onesManages(adminId)
                .compose(RxSchedulers.<ResultObj>io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @SuppressLint("WrongConstant")
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.QUERY_FAIL){
                            nullSpot.setText("暂未设置");
                        }else if (resultObj.getCode() == ResultCode.OK){
                            recyclerView.setHasFixedSize(true);
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(layoutManager);
                            Gson gson = new Gson();
                            String data = gson.toJson(resultObj.getData());
                            List<AdminScenicManage> adminScenicManages = gson.fromJson(data, new TypeToken<List<AdminScenicManage>>(){}.getType());
                            adapter = new OnesScenicManageAdapter(getContext(),adminScenicManages);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                    }
                });
    }

    /**
     * 设置管理员的基础管理数据
     */
    private void setAdminBaseData() {
        //从缓存中拿到数据
        String headIcon = (String) preferences.getString("headIcon",null);
        String account = (String)preferences.getString("account",null);
        String name = (String) preferences.getString("name",null);
        String sex = (String)preferences.getString("sex",null);
        int age = (int) preferences.getInt("age",0);
        int workYear = (int)preferences.getInt("workYear",0);
        String roleName = (String) preferences.getString("roleName",null);
        String phone = (String)preferences.getString("phone",null);
        String introduction = (String) preferences.getString("introduction",null);

        //设置数据
        Glide.with(getActivity()).load(headIcon).into(headIconView);
        accountView.setText(account);
        nameView.setText(name);
        sexView.setText(sex);
        ageView.setText(String.valueOf(age));
        workYearView.setText(workYear + "年");
        roleNameView.setText(roleName);
        phoneView.setText(phone);
        introductionView.setText(introduction);
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
                                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                imgPath = cursor.getString(columnIndex);
                                cursor.close();
                                Glide.with(this).load(imgPath).into(headIconView);
                                //上传到文件服务器
                                uploadFile();
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

        String account = (String)preferences.getString("account",null);
        File file = new File(imgPath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody =new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file",file.getName(),requestFile)
                .addFormDataPart("account",account)
                .build();

        //上传到文件服务器
        fileService
                .upload(requestBody)
                .compose(RxSchedulers.<UploadFileResponse>io_main())
                .subscribe(new Consumer<UploadFileResponse>() {
                    @Override
                    public void accept(UploadFileResponse uploadFileResponse) throws Exception {
                        dbHeadIconUrl = uploadFileResponse.getFileUri();
                        updateDB(account);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,"出错===》"+ throwable.getMessage());
                    }
                });
    }

    /**
     * 更新数据库
     * @param account
     */
    @SuppressLint("CheckResult")
    private void updateDB(String account) {
        AdminService adminService = retrofit.create(AdminService.class);
        Gson gson = new Gson();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("headIconUrl",dbHeadIconUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
        adminService.changeHeadIcon(account,requestBody)
                .compose(RxSchedulers.<ResultObj>io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            Toast.makeText(getContext(),"头像修改成功",Toast.LENGTH_SHORT).show();
                            //及时修改缓存
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("headIcon",dbHeadIconUrl);
                            editor.apply();
                        }else {
                            Toast.makeText(getContext(),"头像修改失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, throwable.getMessage());
                    }
                });
    }

    /**
     * 更新联系电话
     */
    private void updatePhone() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        AdminPhoneUpdateFragment fragment = new AdminPhoneUpdateFragment();
        transaction.replace(R.id.admin_self_info_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_slef_info_back_btn:
                //返回
                getActivity().finish();
                break;
            case R.id.admin_self_info_headicon:
                //更新头像
                showImg();
            case R.id.admin_self_info_phone_root1:
                //更新联系电话
                updatePhone();
                break;
            case R.id.admin_self_info_introduction_root1:
                //更新个人简介
                break;
            default:break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //修改后及时更新界面信息
        //更新电话
        String phone = preferences.getString("phone", null);
        phoneView.setText(phone);
        //更新个人简介
    }
}
