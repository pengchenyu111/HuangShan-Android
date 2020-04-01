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
import com.example.huangshan.tourist.ui.adapter.FiveShengPageAdapter;
import com.example.huangshan.tourist.ui.fragment.FiveJueCloudFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueHotSpringFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueRockFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueSnowFragment;
import com.example.huangshan.tourist.ui.fragment.FiveJueSongFragment;
import com.example.huangshan.tourist.ui.fragment.FiveShengFamousFragment;
import com.example.huangshan.tourist.ui.fragment.FiveShengPaintFragment;
import com.example.huangshan.tourist.ui.fragment.FiveShengPeotryFragment;
import com.example.huangshan.tourist.ui.fragment.FiveShengRoadFragment;
import com.example.huangshan.tourist.ui.fragment.FiveShengRockFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiveShengActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.fivesheng_back_btn) ImageView backBtn;
    @BindView(R.id.fivesheng_tab_name) TabLayout tabLayout;
    @BindView(R.id.fivesheng_view_pager) ViewPager viewPager;

    private static final String TAG = "FiveShengActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fivesheng);
        ButterKnife.bind(this);

        backBtn.setOnClickListener(this::onClick);

        addViewPager();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fivesheng_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    private void addViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        FiveShengPaintFragment fragment1 = new FiveShengPaintFragment();
        FiveShengRockFragment fragment2 = new FiveShengRockFragment();
        FiveShengRoadFragment fragment3 = new FiveShengRoadFragment();
        FiveShengPeotryFragment fragment4 = new FiveShengPeotryFragment();
        FiveShengFamousFragment fragment5 = new FiveShengFamousFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        FragmentManager manager = getSupportFragmentManager();
        FiveShengPageAdapter adapter = new FiveShengPageAdapter(manager,fragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
