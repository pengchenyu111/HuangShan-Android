package com.example.huangshan.admin.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.bin.david.form.core.SmartTable;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.table.TicketPrice;
import com.example.huangshan.common.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePageCablewayActivity extends BaseActivity  implements View.OnClickListener{

    @BindView(R.id.home_page_admin_cableway_back_btn) ImageView backBtn;
    @BindView(R.id.home_page_admin_cableway_tab_name) TabLayout tabLayout;
    @BindView(R.id.home_page_admin_cableway_scroll) ScrollView scrollView;
    @BindView(R.id.home_page_admin_cableway_1) LinearLayout yunguRootView;
    @BindView(R.id.home_page_admin_cableway_yungu_price) SmartTable yunguPriceTable;
    @BindView(R.id.home_page_admin_cableway_2) LinearLayout taipingRootView;
    @BindView(R.id.home_page_admin_cableway_taiping_price) SmartTable taipingPriceTable;
    @BindView(R.id.home_page_admin_cableway_3) LinearLayout yupinRootView;
    @BindView(R.id.home_page_admin_cableway_yupin_price) SmartTable yupinPriceTable;
    @BindView(R.id.home_page_admin_cableway_4) LinearLayout xihaiRootView;
    @BindView(R.id.home_page_admin_cableway_xihai_price) SmartTable xihaiPriceTable;

    private static final String TAG = "HomePageCablewayActivit";
    private boolean IsSlide = false;//默认不是滑动操作
    private  int height1;
    private  int height2;
    private  int height3;
    private  int height4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page_cableway);

        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);

        addTab();
        initView();
        showYunguPriceTable();
        showTaipingPriceTable();
        showYupinPriceTable();
        showXiahaiPriceTable();
    }

    private void addTab() {
        tabLayout.addTab(tabLayout.newTab().setText("云谷索道"));
        tabLayout.addTab(tabLayout.newTab().setText("太平索道"));
        tabLayout.addTab(tabLayout.newTab().setText("玉屏索道"));
        tabLayout.addTab(tabLayout.newTab().setText("西海观光缆车"));
    }

    private void initView() {
        tabLayout.setSelectedTabIndicatorHeight(0);//去掉标签隐藏的下划线

        //ScrollView的滑动监听
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //第二个参数:目前的x轴坐标
                //第三个参数:目前的y轴坐标
                //第四个参数:上一个x轴坐标
                //第五个参数:上一个y轴坐标

                IsSlide = true;//表明此时正在滑动

                if (scrollY < height1) {
                    //表明此时在Item1范围内，所以第一个Item应该被选择
                    Objects.requireNonNull(tabLayout.getTabAt(0)).select();
                } else if (scrollY >= height1 && scrollY < height2) {
                    Objects.requireNonNull(tabLayout.getTabAt(1)).select();
                } else if (scrollY >= height2 && scrollY < height3) {
                    Objects.requireNonNull(tabLayout.getTabAt(2)).select();
                } else if (scrollY >= height3 && scrollY < height4) {
                    Objects.requireNonNull(tabLayout.getTabAt(3)).select();
                }
                IsSlide = false;//表明滑动结束
            }
        });

        //这个方法的作用是标签被选择调用的方法
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //当标签处于选中的状态   此时有两种情况 1.直接点击tabLayout标签会走   2.当ScrollView滑动时，因为我们在滑动里面设置了选择标签，所以此时这个方法也会走
                //现在有个问题，当滑动的时候，tabLayout标签也会被选择，这个方法也会走，所以此时会产生冲突，我们要排除滑动的操作
                if (IsSlide) {//表示此时正在滑动  不走这个方法
                    return;
                }
                int position = tab.getPosition();//表明哪个标签被选择
                if (position == 0) {
                    scrollView.smoothScrollTo(0, 0);
                } else if (position == 1) {
                    scrollView.smoothScrollTo(0, height1);
                } else if (position == 2) {
                    scrollView.smoothScrollTo(0, height2);
                } else if (position == 3) {
                    scrollView.smoothScrollTo(0, height3);//滑到对应的View顶部
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void showYunguPriceTable() {
        List<TicketPrice> ticketPrices = new ArrayList<>();
        ticketPrices.add(new TicketPrice("成人","淡季","65元/人"));
        ticketPrices.add(new TicketPrice("成人","旺季","80元/人"));
        ticketPrices.add(new TicketPrice("儿童","（1.2米至1.4米）淡季","35元/人"));
        ticketPrices.add(new TicketPrice("儿童","（1.2米至1.4米）旺季","40元/人"));
        ticketPrices.add(new TicketPrice("儿童","1.2米以下","免票"));
        yunguPriceTable.setData(ticketPrices);
        yunguPriceTable.getConfig().setShowXSequence(false);
        yunguPriceTable.getConfig().setShowYSequence(false);
    }

    private void showTaipingPriceTable() {
        List<TicketPrice> ticketPrices = new ArrayList<>();
        ticketPrices.add(new TicketPrice("成人","淡季","65元/人"));
        ticketPrices.add(new TicketPrice("成人","旺季","65元/人"));
        taipingPriceTable.setData(ticketPrices);
        taipingPriceTable.getConfig().setShowXSequence(false);
        taipingPriceTable.getConfig().setShowYSequence(false);
    }

    private void showYupinPriceTable() {
        List<TicketPrice> ticketPrices = new ArrayList<>();
        ticketPrices.add(new TicketPrice("成人","淡季","70元/人"));
        ticketPrices.add(new TicketPrice("成人","旺季","90元/人"));
        yupinPriceTable.setData(ticketPrices);
        yupinPriceTable.getConfig().setShowXSequence(false);
        yupinPriceTable.getConfig().setShowYSequence(false);
    }
    private void showXiahaiPriceTable() {
        List<TicketPrice> ticketPrices = new ArrayList<>();
        ticketPrices.add(new TicketPrice("成人","淡季","70元/人"));
        ticketPrices.add(new TicketPrice("成人","旺季","90元/人"));
        xihaiPriceTable.setData(ticketPrices);
        xihaiPriceTable.getConfig().setShowXSequence(false);
        xihaiPriceTable.getConfig().setShowYSequence(false);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        height1 = yunguRootView.getMeasuredHeight();//表示第一个item的高度,这是高度
        height2 = yunguRootView.getMeasuredHeight() + taipingRootView.getMeasuredHeight();//这个是第一个item加第二个item的高度  下面同理
        height3 = yunguRootView.getMeasuredHeight() + taipingRootView.getMeasuredHeight() + yupinRootView.getMeasuredHeight();
        height4 = yunguRootView.getMeasuredHeight() + taipingRootView.getMeasuredHeight() + yupinRootView.getMeasuredHeight() + xihaiRootView.getMeasuredHeight();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_admin_cableway_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }
}
