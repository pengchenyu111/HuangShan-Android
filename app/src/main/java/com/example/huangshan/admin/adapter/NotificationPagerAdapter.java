package com.example.huangshan.admin.adapter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.huangshan.admin.fragment.HandleFeedBackFragment;
import com.example.huangshan.admin.fragment.NotificationMessageFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 通知界面中的pager 适配器
 */
public class NotificationPagerAdapter extends FragmentPagerAdapter {

    private static final String TAG = "NotificationPagerAdapte";

    private static final String[] TAB_TITLES = new String[]{"通知管理","反馈处理"};
    public List<Fragment> fragments = new ArrayList<>();


    public NotificationPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
       return fragments.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
