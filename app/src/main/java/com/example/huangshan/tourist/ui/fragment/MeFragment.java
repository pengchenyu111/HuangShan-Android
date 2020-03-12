package com.example.huangshan.tourist.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.admin.activity.AboutActivity;
import com.example.huangshan.common.ui.SystemSettingsActivity;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.tourist.ui.activity.AccountSafeActivity;
import com.example.huangshan.tourist.ui.activity.DownloadActivity;
import com.example.huangshan.tourist.ui.activity.NotificationActivity;
import com.example.huangshan.tourist.ui.activity.TouristHelpActivity;
import com.example.huangshan.tourist.ui.activity.TouristSelfInfoActivity;
import com.example.huangshan.utils.StatusBarUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Retrofit;

import static android.content.Context.MODE_PRIVATE;

public class MeFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tourist_me_headicon) CircleImageView headIconView;
    @BindView(R.id.tourist_me_talk_btn) ImageView talkBtn;
    @BindView(R.id.tourist_me_collection_btn) ImageView collectionBtn;
    @BindView(R.id.tourist_me_notification_btn) ImageView notificationBtn;
    @BindView(R.id.tourist_me_download_btn) ImageView downloadBtn;
    @BindView(R.id.tourist_me_self_ll_root) LinearLayout selfRoot;
    @BindView(R.id.tourist_me_account_safe_ll_root) LinearLayout accountSafeRoot;
    @BindView(R.id.tourist_me_setting_ll_root) LinearLayout settingRoot;
    @BindView(R.id.tourist_me_about_ll_root) LinearLayout aboutRoot;
    @BindView(R.id.tourist_me_use_help_ll_root) LinearLayout helpRoot;
    @BindView(R.id.tourist_me_feedback_ll_root) LinearLayout feedbackRoot;

    private static final String TAG = "MeFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    //缓存
    private SharedPreferences preferences;

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragment = new MeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        //初始化缓存
        preferences = getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_me,container,false);
        ButterKnife.bind(this,view);
        //设置响应
        addClick();
        StatusBarUtil.transparentAndDark(getActivity());
        showHeadIcon();

        return view;

    }

    private void showHeadIcon() {
        String headIcon = preferences.getString("headIcon",null);
        Glide.with(getActivity()).load(headIcon).placeholder(R.mipmap.loading_1).into(headIconView);
    }

    /**
     * 添加响应
     */
    private void addClick() {
        talkBtn.setOnClickListener(this);
        collectionBtn.setOnClickListener(this);
        notificationBtn.setOnClickListener(this);
        downloadBtn.setOnClickListener(this);
        selfRoot.setOnClickListener(this);
        accountSafeRoot.setOnClickListener(this);
        settingRoot.setOnClickListener(this);
        aboutRoot.setOnClickListener(this);
        helpRoot.setOnClickListener(this);
        feedbackRoot.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tourist_me_talk_btn:
                Toast.makeText(getActivity(),"功能待开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tourist_me_collection_btn:
                Toast.makeText(getActivity(),"功能待开放，敬请期待！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.tourist_me_notification_btn:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_download_btn:
                intent = new Intent(getActivity(), DownloadActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_self_ll_root:
                intent = new Intent(getActivity(), TouristSelfInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_account_safe_ll_root:
                intent = new Intent(getActivity(), AccountSafeActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_setting_ll_root:
                intent = new Intent(getActivity(), SystemSettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_about_ll_root:
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_use_help_ll_root:
                intent = new Intent(getActivity(), TouristHelpActivity.class);
                startActivity(intent);
                break;
            case R.id.tourist_me_feedback_ll_root:
                break;
                default:
                    break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        showHeadIcon();
    }
}
