package com.example.huangshan.admin.activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.huangshan.R;
import com.example.huangshan.admin.fragment.PredictFragment;
import com.example.huangshan.admin.fragment.NotificationRootFragment;
import com.example.huangshan.admin.fragment.ShowDataFragment;
import com.example.huangshan.admin.fragment.AccountManageFragment;
import com.example.huangshan.common.base.ActivityCollector;
import com.example.huangshan.common.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 管理员首页
 */

public class AdminMainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.admin_main_container) FrameLayout frameLayout;
    @BindView(R.id.bottom_navigation_admin) BottomNavigationView bottomNavigationView;

    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3;
    Fragment fragment4;
    private Fragment fragmentNow = null;

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    private static final String TAG = "AdminMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        //绑定控件
        ButterKnife.bind(this);
        //设置响应
        bottomNavigationView.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    /**
     * 监听事件选择
     * @param menuItem
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        changePageFragment(menuItem.getItemId());
        return true;
    }

    /**
     * 当点击导航栏时改变fragment
     * @param id
     */
    public void changePageFragment(int id) {
        switch (id) {
            case R.id.admin_tab_showdata:
                if (fragment1 == null) {
                    fragment1 = ShowDataFragment.newInstance();
                }
                switchFragment(fragmentNow, fragment1);
                break;
            case R.id.admin_tab_predict:
                if (fragment2 == null) {
                    fragment2 = PredictFragment.newInstance();
                }
                switchFragment(fragmentNow, fragment2);
                break;
            case R.id.admin_tab_notification:
                if (fragment3 == null) {
                    fragment3 = NotificationRootFragment.newInstance();
                }
                switchFragment(fragmentNow, fragment3);
                break;
            case R.id.admin_tab_accountmanage:
                if (fragment4 == null) {
                    fragment4 = AccountManageFragment.newInstance();
                }
                switchFragment(fragmentNow, fragment4);
                break;
                default:
                    break;
        }
    }

    /**
     * 隐藏显示fragment
     *
     * @param from 需要隐藏的fragment
     * @param to   需要显示的fragment
     */
    public void switchFragment(Fragment from, Fragment to) {
        if (to == null)
            return;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!to.isAdded()) {
            if (from == null) {
                transaction.add(R.id.admin_main_container, to).show(to).commit();
            } else {
                // 隐藏当前的fragment，add下一个fragment到Activity中
                transaction.hide(from).add(R.id.admin_main_container, to).commitAllowingStateLoss();
            }
        } else {
            // 隐藏当前的fragment，显示下一个
            transaction.hide(from).show(to).commit();
        }
        fragmentNow = to;
    }

    /**
     * 双击退出应用
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(AdminMainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                ActivityCollector.finishAll();
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}
