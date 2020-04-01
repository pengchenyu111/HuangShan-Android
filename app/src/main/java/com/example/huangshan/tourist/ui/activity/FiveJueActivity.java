package com.example.huangshan.tourist.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.example.huangshan.R;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.tourist.ui.adapter.FiveJuePageAdapter;
import com.example.huangshan.tourist.ui.fragment.FiveJueCloudFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueHotSpringFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueRockFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueSnowFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueSongFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiveJueActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fivejue_back_btn) ImageView backBtn;
    @BindView(R.id.fivejue_tab_name) TabLayout tabLayout;
    @BindView(R.id.fivejue_view_pager) ViewPager viewPager;

    private static final String TAG = "FiveJueActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fivejue);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(this::onClick);

        addViewPager();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fivejue_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    private void addViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        FiveJueSongFragment fragment1 = new FiveJueSongFragment();
        FiveJueRockFragment fragment2 = new FiveJueRockFragment();
        FiveJueCloudFragment fragment3 = new FiveJueCloudFragment();
        FiveJueHotSpringFragment fragment4 = new FiveJueHotSpringFragment();
        FiveJueSnowFragment fragment5 = new FiveJueSnowFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        FragmentManager manager = getSupportFragmentManager();
        FiveJuePageAdapter adapter = new FiveJuePageAdapter(manager,fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
