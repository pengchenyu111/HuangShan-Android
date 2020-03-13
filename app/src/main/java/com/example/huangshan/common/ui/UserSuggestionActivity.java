package com.example.huangshan.common.ui;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.common.bean.Suggestion;
import com.example.huangshan.common.httpservice.SuggestionService;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

public class UserSuggestionActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.user_suggestion_back_btn) ImageView backBtn;
    @BindView(R.id.user_suggestion_content) EditText contentEdit;
    @BindView(R.id.user_suggestion_contact_way) EditText contactWayEdit;
    @BindView(R.id.user_suggestion_submit_btn) Button submitBtn;

    private static final String TAG = "UserSuggestionActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //缓存
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_suggestion);

        ButterKnife.bind(this);

        backBtn.setOnClickListener(this);
        submitBtn.setOnClickListener(this);

        //网络初始化
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //初始化缓存
        preferences= this.getSharedPreferences("loginUser", MODE_PRIVATE);

        //填上默认的联系方式
        contactWayEdit.setText(preferences.getString("phone",null));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_suggestion_back_btn:
                finish();
                break;
            case R.id.user_suggestion_submit_btn:
                submitSuggestion();
                break;
                default:
                    break;
        }
    }

    @SuppressLint("CheckResult")
    private void submitSuggestion() {
        String content = contentEdit.getText().toString();
        String contactWay = contactWayEdit.getText().toString();
        boolean isEmpty = checkInput(content);
        if (!isEmpty){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(new Date());
            String name = preferences.getString("name",null);
            Suggestion suggestion = new Suggestion();
            suggestion.setSuggestion(content);
            suggestion.setPropounder(name);
            suggestion.setFeedbackTime(date);
            suggestion.setContactWay(contactWay);
            RequestBody requestBody = RequestBody.Companion.create(gson.toJson(suggestion), MediaType.Companion.parse("application/json; charset=utf-8"));
            SuggestionService suggestionService = retrofit.create(SuggestionService.class);
            suggestionService.sendSuggestion(requestBody)
                    .compose(RxSchedulers.io_main())
                    .subscribe(new Consumer<ResultObj>() {
                        @Override
                        public void accept(ResultObj resultObj) throws Exception {
                            if (resultObj.getCode() == ResultCode.OK){
                                new SweetAlertDialog(UserSuggestionActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("成功")
                                        .setContentText("感谢您的支持！")
                                        .setCancelClickListener(null)
                                        .setConfirmText("返回")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                finish();
                                            }
                                        }).show();
                            }else {
                                Toast.makeText(UserSuggestionActivity.this,"似乎出了一些小问题...",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Log.d(TAG,throwable.getMessage());
                            Toast.makeText(UserSuggestionActivity.this,"服务器繁忙，请稍后再试！",Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(this,"请输入内容", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkInput(String content) {
        if (content.isEmpty()){
            return true;
        }
        return false;
    }
}
