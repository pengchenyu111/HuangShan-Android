package com.example.huangshan.admin.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.huangshan.admin.httpservice.DailyNumService;
import com.example.huangshan.admin.httpservice.HourlyNumService;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.R;
import com.example.huangshan.admin.bean.DailyNum;
import com.example.huangshan.admin.bean.HourlyNum;
import com.example.huangshan.admin.bean.OneDayHourlyNum;
import com.example.huangshan.builder.BuilderManager;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.utils.HttpUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import com.github.mikephil.charting.model.GradientColor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.BoomButton;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.OnBoomListenerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * MainActivity中首页的fragment
 */
public class ShowDataFragment extends Fragment implements View.OnClickListener{

//    @BindView(R.id.showdata_boommenubtn) BoomMenuButton showdataBoomMenuBtn;

    private static final String TAG = "ShowDataFragment";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();



    public ShowDataFragment() {
        // Required empty public constructor
    }

    /**
     * 加载一次，避免内存消耗
     * @return
     */
    public static ShowDataFragment newInstance() {
        Bundle args = new Bundle();
        ShowDataFragment fragment = new ShowDataFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载HomeFragment布局
        View view = inflater.inflate(R.layout.fragment_main_showdata,container,false);
        //绑定控件
        ButterKnife.bind(this,view);

        return view;
    }

//    private void drawFloatButton() {
//        assert showdataBoomMenuBtn != null;
//        showdataBoomMenuBtn.addBuilder(BuilderManager.getHamButtonBuilder("点击日期区间", "").normalImageRes(R.mipmap.calendar));
//        showdataBoomMenuBtn.setOnBoomListener(new OnBoomListenerAdapter() {
//            @Override
//            public void onClicked(int index, BoomButton boomButton) {
//                super.onClicked(index, boomButton);
//                showDatePicker();
//                changeBoomButton(index);
//            }
//        });
//    }
//
//    private void showDatePicker(){
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.datepicker, null);
//        final DatePicker startTimePicker = (DatePicker) view.findViewById(R.id.starttime);
//        final DatePicker endTimePicker = (DatePicker) view.findViewById(R.id.endtime);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(view);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                //开始时间
//                int yearS = startTimePicker.getYear();
//                int realMonthS = startTimePicker.getMonth()+1;
//                int realDayS = startTimePicker.getDayOfMonth();
//                String formatMonthS = formatDate(realMonthS);
//                String formatDayS = formatDate(realDayS);
//                startTime = String.valueOf(yearS)+"-"+formatMonthS+"-"+formatDayS;
//                Log.d(TAG,"startTime：-------------"+startTime+"----------------结束");
//
//                //结束时间
//                int yearE = endTimePicker.getYear();
//                int realMonthE = endTimePicker.getMonth()+1;
//                int realDayE = endTimePicker.getDayOfMonth();
//                String formatMonthE = formatDate(realMonthE);
//                String formatDayE = formatDate(realDayE);
//                endTime = String.valueOf(yearE)+"-"+formatMonthE+"-"+formatDayE;
//                Log.d(TAG,"endTime：-------------"+endTime+"----------------结束");
//
//                //更新图表
//                //initData(startTime,endTime);
//                //drawCharts();
//            }
//        });
//        builder.setNegativeButton("取消", null);
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        //自动弹出键盘问题解决
//        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//    }
//
//    private String formatDate(int date){
//        String formatedDate = String.valueOf(date);
//        if (date == 1 || date == 2 || date == 3 || date == 4 || date == 5 || date == 6 || date == 7 || date == 8 || date == 9) {
//            formatedDate = "0"+formatedDate;
//        }
//        return formatedDate;
//    }
//
//    private void changeBoomButton(int index) {
//        HamButton.Builder builder = (HamButton.Builder) showdataBoomMenuBtn.getBuilder(index);
//        if (index == 0) {
//            String tip = dailyNums.get(0).getDateName()+"----"+dailyNums.get(dailyNums.size()-1).getDateName();
//            builder.normalText(tip);
////            builder.highlightedText("Highlighted, changed!");
//            builder.subNormalText("点击选取日期区间");
//            builder.normalTextColor(Color.YELLOW);
//            builder.highlightedTextColorRes(R.color.colorPrimary);
//            builder.subNormalTextColor(Color.BLACK);
//        }
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default:
                break;
        }
    }

}