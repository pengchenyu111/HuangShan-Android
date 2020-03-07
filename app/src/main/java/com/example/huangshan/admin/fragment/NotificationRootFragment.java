package com.example.huangshan.admin.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huangshan.admin.adapter.NotificationPagerAdapter;
import com.example.huangshan.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *  第三个导航栏，服务  NotificationRootFragment
 */
public class NotificationRootFragment extends Fragment{

    private static final String TAG = "NotificationRootFragment";

    @BindView(R.id.notification_view_pager)ViewPager viewPager;
    @BindView(R.id.notification_tabs) TabLayout tabLayout;

    public NotificationRootFragment() {
        // Required empty public constructor
    }

    public static NotificationRootFragment newInstance() {
        Bundle args = new Bundle();
        NotificationRootFragment fragment = new NotificationRootFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        View view = inflater.inflate(R.layout.fragment_main_notification,container,false);
        ButterKnife.bind(this,view);
        //加载分页导航
        List<Fragment> fragments = new ArrayList<>();
        NotificationMessageFragment fragment1 = new NotificationMessageFragment();
        HandleFeedBackFragment fragment2 = new HandleFeedBackFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        FragmentManager manager = getChildFragmentManager();
        NotificationPagerAdapter notificationPagerAdapter = new NotificationPagerAdapter(manager,fragments);
        viewPager.setAdapter(notificationPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }
}
