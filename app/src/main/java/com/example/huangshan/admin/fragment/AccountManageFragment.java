package com.example.huangshan.admin.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.huangshan.R;
import com.example.huangshan.admin.activity.AboutActivity;
//import com.example.huangshan.admin.activity.AdminSelfInfoActivity;
import com.example.huangshan.admin.activity.AdminSelfInfoActivity;
import com.example.huangshan.admin.activity.AdminsMapViewActivity;
import com.example.huangshan.common.LoginActivity;
import com.example.huangshan.admin.activity.SettingsActivity;
import com.example.huangshan.admin.activity.UsersManageActivity;
import com.example.huangshan.admin.bean.Admin;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

/**
 * MainActivity中  账户 的fragment
 */
public class AccountManageFragment extends Fragment implements View.OnClickListener{

  @BindView(R.id.headicon) CircleImageView headIcon;
  @BindView(R.id.admin_self_ll_root1) LinearLayout selfLayout;
  @BindView(R.id.ll_root1) LinearLayout adminLayout;
  @BindView(R.id.ll_root2) LinearLayout userLayout;
  @BindView(R.id.ll_root3) LinearLayout settingLayout;
  @BindView(R.id.ll_root4) LinearLayout aboutLayout;

  private View view;

  private static final String TAG = "AccountManageFragment";

  public AccountManageFragment() {
    // Required empty public constructor
  }

  /**
   * 加载一次避免内存消耗
   * @return
   */
    public static AccountManageFragment newInstance() {
        Bundle args = new Bundle();
        AccountManageFragment fragment = new AccountManageFragment();
        fragment.setArguments(args);
        return fragment;
    }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        加载布局
    view = inflater.inflate(R.layout.fragment_main_accountmanage,container,false);
//        绑定控件
    ButterKnife.bind(this,view);
//        设置事件响应
    headIcon.setOnClickListener(this);
    selfLayout.setOnClickListener(this);
    adminLayout.setOnClickListener(this);
    userLayout.setOnClickListener(this);
    settingLayout.setOnClickListener(this);
    aboutLayout.setOnClickListener(this);

    //加载头像头像
    loadHeadIcon();

    return view;
  }

  /**
   * 加载头像
   */
  private void loadHeadIcon() {
    SharedPreferences preferences= getActivity().getSharedPreferences("loginUser", MODE_PRIVATE);
    String headIconUrl = (String) preferences.getString("headIcon",null);
    Glide.with(getActivity()).load(headIconUrl).into(headIcon);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()){
      case R.id.headicon:
        //todo
        Toast.makeText(getActivity(),"点击了头像",Toast.LENGTH_SHORT).show();
//        Intent intent4 = new Intent(getActivity(),LoginActivity.class);
//        startActivity(intent4);
        break;
      case R.id.admin_self_ll_root1:
        //管理员个人信息
        Intent intent5 = new Intent(getActivity(), AdminSelfInfoActivity.class);
        startActivity(intent5);
        break;
      case R.id.ll_root1:
        //管理员地图一览
        Intent intent = new Intent(getActivity(),AdminsMapViewActivity.class);
        startActivity(intent);
        break;
      case R.id.ll_root2:
        //游客账户管理
        Intent intent1 = new Intent(getActivity(),UsersManageActivity.class);
        startActivity(intent1);
        break;
      case R.id.ll_root3:
        //设置界面
        Intent intent2 = new Intent(getActivity(), SettingsActivity.class);
        startActivity(intent2);
        break;
      case R.id.ll_root4:
        //关于界面
        Intent intent3 = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent3);
        break;
      default:break;

    }
  }

}