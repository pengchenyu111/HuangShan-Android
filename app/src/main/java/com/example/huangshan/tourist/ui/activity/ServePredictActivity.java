package com.example.huangshan.tourist.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.huangshan.R;
import com.example.huangshan.admin.bean.DailyNum;
import com.example.huangshan.admin.bean.HourlyNum;
import com.example.huangshan.admin.httpservice.DailyNumService;
import com.example.huangshan.admin.httpservice.HourlyNumService;
import com.example.huangshan.common.base.BaseActivity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.view.TextCircleView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.model.GradientColor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class ServePredictActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.serve_predict_back_btn) ImageView backBtn;
    @BindView(R.id.serve_predict_num) TextCircleView predictNumView;
    @BindView(R.id.serve_predict_level) TextCircleView predictLevelView;
    @BindView(R.id.serve_predict_recent_bar_chart) BarChart barChart;

    private static final String TAG = "ServePredictActivity";
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    private DailyNumService dailyNumService;
    //
    private List<DailyNum> dailyNums = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serve_predict);
        ButterKnife.bind(this);
        backBtn.setOnClickListener(this);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        dailyNumService = retrofit.create(DailyNumService.class);

        getTodayPredict();
        initDailyNumData("2019-9-13","2019-9-20");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.serve_predict_back_btn:
                finish();
                break;
                default:
                    break;
        }
    }

    /**
     * 获取今日预测
     */
    @SuppressLint("CheckResult")
    private void getTodayPredict() {
        //todo 由于没有数据  暂时写死
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        //String date = format.format(new Date());
        dailyNumService.getOneDayNum("2019-9-8")
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            DailyNum dailyNum = gson.fromJson(data,DailyNum.class);
                            setPredictData(dailyNum);
                        }else {
                            Toast.makeText(ServePredictActivity.this,"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(ServePredictActivity.this,"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置今日预测的数据
     * @param dailyNum
     */
    private void setPredictData(DailyNum dailyNum) {
        String[] levels = {"通畅","一般","拥挤"};
        int num = dailyNum.getPredictNum();
        predictNumView.setText(String.valueOf(num));
        if (num <= 8000){
            predictLevelView.setText(levels[0]);
            predictLevelView.setTextColor(ContextCompat.getColor(this,R.color.free_count));
            predictNumView.setTextColor(ContextCompat.getColor(this,R.color.free_count));
        }else if (num > 8000 && num <= 15000){
            predictLevelView.setText(levels[1]);
            predictLevelView.setTextColor(ContextCompat.getColor(this,R.color.tips));
            predictNumView.setTextColor(ContextCompat.getColor(this,R.color.tips));
        }else {
            predictLevelView.setText(levels[2]);
            predictLevelView.setTextColor(ContextCompat.getColor(this,R.color.red));
            predictNumView.setTextColor(ContextCompat.getColor(this,R.color.red));
        }
    }

    /**
     * 获取近日的每日数据
     * @param startTime
     * @param endTime
     */
    @SuppressLint("CheckResult")
    private void initDailyNumData(String startTime, String endTime) {
        dailyNumService.getPeriodDailyNum(startTime,endTime)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            dailyNums = gson.fromJson(data,new TypeToken<List<DailyNum>>(){}.getType());
                            initBarChart(dailyNums);
                            setBarChartData(dailyNums);
                        }else {
                            Toast.makeText(ServePredictActivity.this,"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(ServePredictActivity.this,"服务器繁忙，请稍候再试!！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 往柱状图中设置数据
     * @param dailyNums
     */
    private void setBarChartData(List<DailyNum> dailyNums) {
        ArrayList<BarEntry> values = new ArrayList<>();

        for (int i = 0; i < dailyNums.size(); i++) {
            int val = dailyNums.get(i).getTodayTotalNum();

            if (val<100) {
                values.add(new BarEntry(i, val, ContextCompat.getDrawable(this,R.drawable.star)));
            } else {
                values.add(new BarEntry(i, val));
            }
        }

        BarDataSet barDataSet;
        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            barDataSet = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            barDataSet.setValues(values);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();

        } else {
            barDataSet = new BarDataSet(values, "近七日每日客流总数");
            barDataSet.setDrawIcons(false);

            //设置花里胡哨的渐变色
            int[] startColors = {
                    ContextCompat.getColor(this, android.R.color.holo_orange_light),
                    ContextCompat.getColor(this, android.R.color.holo_blue_light),
                    ContextCompat.getColor(this, android.R.color.holo_orange_light),
                    ContextCompat.getColor(this, android.R.color.holo_green_light),
                    ContextCompat.getColor(this, android.R.color.holo_red_light)
            };
            int[] endColors = {
                    ContextCompat.getColor(this, android.R.color.holo_blue_dark),
                    ContextCompat.getColor(this, android.R.color.holo_purple),
                    ContextCompat.getColor(this, android.R.color.holo_green_dark),
                    ContextCompat.getColor(this, android.R.color.holo_red_dark),
                    ContextCompat.getColor(this, android.R.color.holo_orange_dark)
            };

            List<GradientColor> gradientColors = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                gradientColors.add(new GradientColor(startColors[i],endColors[i]));
            }

            //BarDataSet设置渐变色
            barDataSet.setGradientColors(gradientColors);

            //ArrayList<IBarDataSet>添加BarDataSet
            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(barDataSet);

            //BarData设置ArrayList<IBarDataSet>
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(tfLight);
            data.setBarWidth(0.8f);//设置每个柱子的宽度

            barChart.setData(data);
        }
    }

    /**
     * 初始化柱状图
     * @param dailyNums
     */
    private void initBarChart(List<DailyNum> dailyNums) {
        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
        barChart.getDescription().setEnabled(false);
        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(1000);//动画

        //X轴设置
        //底部标签设置
        final String[] labels = new String[dailyNums.size()];
        for (int i = 0; i <dailyNums.size() ; i++) {
            labels[i] = dailyNums.get(i).getDateName().substring(5,10);
        }
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if ((int)value>=labels.length){
                    return "";
                }else{
                    return labels[(int) value];
                }
            }
        };
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(dailyNums.size());//横坐标显示几个;
        xAxis.setValueFormatter(formatter);

        //Y轴设置
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setLabelCount(8, false);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(8, false);
        rightAxis.setSpaceTop(15f);
        rightAxis.setAxisMinimum(0f);

        Legend l = barChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
    }
}
