package com.example.huangshan.tourist.ui.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class FiveJuePageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "FiveJuePageAdapter";

    private static final String[] TAB_TITLES = new String[]{"奇松","怪石","云海","温泉","冬雪"};
    public List<Fragment> fragments = new ArrayList<>();

    public FiveJuePageAdapter(@NonNull FragmentManager fm, List<Fragment> fragments) {
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
