package com.example.huangshan.tourist.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.admin.httpservice.AdminService;
import com.example.huangshan.common.service.FileService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.http.UploadFileResponse;
import com.example.huangshan.tourist.httpservice.TouristService;
import com.google.gson.Gson;

import java.io.File;
import java.util.LinkedHashMap;
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

public class TouristSelfInfoFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.tourist_self_info_headicon) CircleImageView headIconView;
    @BindView(R.id.tourist_self_info_acount) TextView accountView;
    @BindView(R.id.tourist_self_info_name) TextView nameView;
    @BindView(R.id.tourist_self_info_sex) TextView sexView;
    @BindView(R.id.tourist_self_info_birth) TextView birthView;
    @BindView(R.id.tourist_self_info_age) TextView ageView;
    @BindView(R.id.tourist_self_info_phone) TextView phoneView;

    //按钮
    @BindView(R.id.tourist_self_info_back_btn) ImageView backBtn;
    @BindView(R.id.tourist_self_info_headicon_root1) LinearLayout headIconRoot;
    @BindView(R.id.tourist_self_info_change_btn) TextView changeInfoBtn;

    private static final String TAG = "TouristSelfInfoFragment";
    //缓存
    private SharedPreferences preferences;
    //网络请求
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //更新头像
    public String imgPath;
    public static final int CHOOSE_IMG = 2;
    private String dbHeadIconUrl;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化缓存
        preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tourist_self_info, container, false);

        ButterKnife.bind(this,view);

        backBtn.setOnClickListener(this::onClick);
        headIconRoot.setOnClickListener(this::onClick);
        changeInfoBtn.setOnClickListener(this);

        setAdminBaseData();


        return view;
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
        String birth = (String) preferences.getString("birth","暂未设置");
        int age = (int) preferences.getInt("age",0);
        String phone = (String)preferences.getString("phone",null);

        //设置数据
        Glide.with(getActivity()).load(headIcon).into(headIconView);
        accountView.setText(account);
        nameView.setText(name);
        sexView.setText(sex);
        birthView.setText(birth);
        ageView.setText(String.valueOf(age));
        phoneView.setText(phone);

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
        RequestBody requestFile = RequestBody.Companion.create(file, MediaType.parse("image/*"));
        RequestBody requestBody = new MultipartBody.Builder()
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
        long id = preferences.getLong("id",0);
        TouristService touristService = retrofit.create(TouristService.class);
        Gson gson = new Gson();
        Map<String,String> map = new LinkedHashMap<>();
        map.put("headIcon",dbHeadIconUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),gson.toJson(map));
        touristService.changeHeadIcon(id,requestBody)
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

    private void changeInfo() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ChangeTouristInfoFragment fragment = new ChangeTouristInfoFragment();
        transaction.replace(R.id.tourist_self_info_container,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tourist_self_info_back_btn:
                getActivity().finish();
                break;
            case R.id.tourist_self_info_headicon_root1:
                showImg();
                break;
            case R.id.tourist_self_info_change_btn:
                changeInfo();
                break;
                default:
                    break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        setAdminBaseData();
    }
}
