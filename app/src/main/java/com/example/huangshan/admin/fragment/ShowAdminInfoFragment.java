package com.example.huangshan.admin.fragment;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.huangshan.admin.bean.ScenicManage;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 用于展示管理员的信息
 */
public class ShowAdminInfoFragment extends Fragment implements View.OnClickListener{

    private View view;

    //基本信息
    @BindView(R.id.admin_info_headicon)
    CircleImageView adminHeadIcon;
    @BindView(R.id.admin_info_title_name)
    TextView adminNameTitle;
    @BindView(R.id.admin_info_adminname)
    TextView adminName;
    @BindView(R.id.admin_info_adminworkspot)
    TextView adminWorkSpot;
    @BindView(R.id.admin_info_adminworktime)
    TextView adminWorkTime;
    @BindView(R.id.admin_info_adminphone)
    TextView adminPhone;
    @BindView(R.id.admin_info_adminsex)
    TextView adminSex;
    @BindView(R.id.admin_info_adminage)
    TextView adminAge;
    @BindView(R.id.admin_info_adminworkyear)
    TextView adminWorkYear;
    @BindView(R.id.admin_info_adminintroduction)
    TextView adminIntroduction;

    //按钮
    @BindView(R.id.admin_info_back_btn)
    ImageView back;
    @BindView(R.id.update_admin_info_btn)
    FloatingActionButton updateInfoBtn;

    //背景
    @BindView(R.id.admin_info_background)
    ImageView background;


    public ShowAdminInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_admin_info, container, false);
        ButterKnife.bind(this,view);

        //设置状态栏透明化
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        //添加响应
        back.setOnClickListener(this::onClick);
        updateInfoBtn.setOnClickListener(this::onClick);
        //添加背景图片
        addBackground();

        //获得Activity传来的数据
        Bundle bundle = getArguments();
        ScenicManage adminInfo = (ScenicManage) bundle.getSerializable("adminInfo");

        //将数据写进界面
        showDataToView(adminInfo);

        return view;
    }

    private void showDataToView(ScenicManage adminInfo) {
        Glide.with(getActivity()).load(adminInfo.getAdminHeadIcon()).into(adminHeadIcon);
        adminNameTitle.setText(adminInfo.getAdminName());
        adminName.setText(adminInfo.getAdminName());
        adminWorkSpot.setText(adminInfo.getScenicName());
        adminWorkTime.setText(adminInfo.getAdminWorkDay()+"  "+adminInfo.getAdminWorkTime());
        adminPhone.setText(adminInfo.getPhone());
        adminSex.setText(adminInfo.getSex());
        adminAge.setText(String.valueOf(adminInfo.getAge()));
        adminWorkYear.setText(String.valueOf(adminInfo.getWorkYear()));
        adminIntroduction.setText(adminInfo.getIntroduction());
    }

    /**
     * 添加随机背景
     */
    private void addBackground() {
        int random = (int)(Math.random()*4);
        int temp = random % 4;
        if (temp == 0){
            background.setImageResource(R.mipmap.huangshan_spring);
        }else if (temp == 1){
            background.setImageResource(R.mipmap.huangshan_summer);
        }else if (temp == 2){
            background.setImageResource(R.mipmap.huangshan_fall);
        }else if (temp == 3){
            background.setImageResource(R.mipmap.huangshan_winter);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.admin_info_back_btn:
                getActivity().finish();
                break;
            case R.id.update_admin_info_btn:
                //todo 这里到底怎么解决？
                Toast.makeText(getActivity(),"点击了flobtn",Toast.LENGTH_SHORT).show();
                break;
                default:break;
        }
    }
}
