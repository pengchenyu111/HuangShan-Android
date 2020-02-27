package com.example.huangshan.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;

public class StatusBarUtil {

    public static void transparentAndDark(Activity activity){
        if (Build.VERSION.SDK_INT >= 21) {
            //状态栏透明
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            //字体黑色
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
