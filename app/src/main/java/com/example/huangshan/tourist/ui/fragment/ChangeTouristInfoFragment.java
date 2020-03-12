package com.example.huangshan.tourist.ui.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.Tourist;
import com.example.huangshan.common.ui.ChangePasswordActivity;
import com.example.huangshan.common.ui.LoginActivity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.tourist.httpservice.TouristService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class ChangeTouristInfoFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tourist_change_info_back_btn) ImageView backBtn;
    @BindView(R.id.tourist_change_info_name) EditText nameEdit;
    @BindView(R.id.tourist_change_info_sex) EditText sexEdit;
    @BindView(R.id.tourist_change_info_birth) TextView birthEdit;
    @BindView(R.id.tourist_change_info_phone) EditText phoneEdit;
    @BindView(R.id.tourist_change_save_btn) Button saveChangeBtn;

    private static final String TAG = "ChangeTouristInfoFragme";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //缓存
    private SharedPreferences sharedPreferences;
    //选择的生日日期
    private String pickBirth;
    //
    private Tourist tourist = new Tourist();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //初始化缓存
        sharedPreferences = getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
        tourist.setId(sharedPreferences.getLong("id",0));
        tourist.setAccount(sharedPreferences.getString("account",null));
        tourist.setName(sharedPreferences.getString("name",null));
        tourist.setBirth(sharedPreferences.getString("birth",null));
        tourist.setSex(sharedPreferences.getString("sex",null));
        tourist.setPhone(sharedPreferences.getString("phone", null));
        tourist.setHeadIcon(sharedPreferences.getString("headIcon",null));
        pickBirth = sharedPreferences.getString("birth",null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_tourist_info, container ,false);
        ButterKnife.bind(this,view);

        backBtn.setOnClickListener(this);
        birthEdit.setOnClickListener(this);
        saveChangeBtn.setOnClickListener(this);

        setOldInfo();

        return view;
    }

    private void setOldInfo() {
        nameEdit.setText(tourist.getName());
        sexEdit.setText(tourist.getSex());
        birthEdit.setText(tourist.getBirth());
        phoneEdit.setText(tourist.getPhone());
    }

    private void showDatePicker(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.layout_date_picker_birth, null);
        final DatePicker birthPicker = (DatePicker) view.findViewById(R.id.date_picker_birth);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //开始时间
                int yearS = birthPicker.getYear();
                int realMonthS = birthPicker.getMonth()+1;
                int realDayS = birthPicker.getDayOfMonth();
                String formatMonthS = formatDate(realMonthS);
                String formatDayS = formatDate(realDayS);
                pickBirth = String.valueOf(yearS)+"-"+formatMonthS+"-"+formatDayS;
                birthEdit.setText(pickBirth);
            }
        });
        builder.setNegativeButton("取消", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        //自动弹出键盘问题解决
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    private String formatDate(int date){
        String formatedDate = String.valueOf(date);
        if (date == 1 || date == 2 || date == 3 || date == 4 || date == 5 || date == 6 || date == 7 || date == 8 || date == 9) {
            formatedDate = "0"+formatedDate;
        }
        return formatedDate;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tourist_change_info_back_btn:
                getFragmentManager().popBackStack();
                break;
            case R.id.tourist_change_info_birth:
                showDatePicker();
                break;
            case R.id.tourist_change_save_btn:
                saveChange();
                break;
                default:
                    break;
        }

    }

    @SuppressLint("CheckResult")
    private void saveChange() {
        tourist.setName(nameEdit.getText().toString());
        tourist.setSex(sexEdit.getText().toString());
        tourist.setBirth(pickBirth);
        tourist.setPhone(phoneEdit.getText().toString());
        long id = sharedPreferences.getLong("id",0);
        int age = calculateAge(pickBirth);
        RequestBody body = RequestBody.Companion.create(gson.toJson(tourist), MediaType.Companion.parse("application/json; charset=utf-8"));
        TouristService touristService = retrofit.create(TouristService.class);
        touristService.updateInfo(id,body)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            //修改缓存
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("name",tourist.getName());
                            editor.putString("sex",tourist.getSex());
                            editor.putString("birth",tourist.getBirth());
                            editor.putString("phone",tourist.getPhone());
                            editor.putInt("age",age);
                            editor.apply();
                            //显示成功对话框
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("成功！")
                                    .setContentText("您的个人信息已成功修改!")
                                    .showCancelButton(false)
                                    .setConfirmText("返回")
                                    .setCancelClickListener(null)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.dismissWithAnimation();
                                            FragmentManager fragmentManager = getFragmentManager();
                                            fragmentManager.popBackStack();
                                        }
                                    }).show();
                        }else {
                            Toast.makeText(getActivity(), "似乎除了一个小问题",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getActivity(), "服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private int calculateAge(String pickBirth) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date= null;
        try {
            date = format.parse(pickBirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int age = 0;
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        cal.setTime(date);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;
            } else {
                age--;
            }
        }
        if (age <= 0){
            age = 0;
        }
        return age;
    }
}
