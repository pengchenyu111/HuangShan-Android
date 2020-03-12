package com.example.huangshan.common.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.http.RetrofitManager;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
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

    private void submitSuggestion() {
        String content = contentEdit.getText().toString();
        boolean isEmpty = checkInput(content);
        if (!isEmpty){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String date = format.format(new Date());
            String name = preferences.getString("name",null);
            //todo
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
