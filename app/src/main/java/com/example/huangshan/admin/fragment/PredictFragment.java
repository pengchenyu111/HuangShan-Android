package com.example.huangshan.admin.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.huangshan.admin.bean.DailyNum;
import com.example.huangshan.admin.bean.HourlyNum;
import com.example.huangshan.admin.httpservice.DailyNumService;
import com.example.huangshan.admin.httpservice.HourlyNumService;
import com.example.huangshan.constans.Constant;
import com.example.huangshan.R;
import com.example.huangshan.admin.activity.WeatherH5Activity;
import com.example.huangshan.constans.ResultCode;
import com.example.huangshan.http.ResultObj;
import com.example.huangshan.http.RetrofitManager;
import com.example.huangshan.http.RxSchedulers;
import com.example.huangshan.view.TextCircleView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.model.GradientColor;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heweather.plugin.view.HeContent;
import com.heweather.plugin.view.HeWeatherConfig;
import com.heweather.plugin.view.LeftLargeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class PredictFragment extends Fragment implements View.OnClickListener, OnChartValueSelectedListener {

    @BindView(R.id.heweather_plugin_ll_view) LeftLargeView leftLargeView;
    @BindView(R.id.passenger_flow_predict_num) TextCircleView predictNumView;
    @BindView(R.id.passenger_flow_predict_level) TextCircleView predictLevelView;
    @BindView(R.id.passenger_flow_bar_chart) BarChart barChart;
    @BindView(R.id.passenger_flow_line_chart) LineChart lineChart;
    @BindView(R.id.passenger_flow_diviation_chart) BarChart deviationBarChart;
    @BindView(R.id.passenger_flow_pie_chart) PieChart pieChart;

    private static final String TAG = "PredictFragment";
    private View view;
    //网络
    private RetrofitManager retrofitManager = new RetrofitManager();
    private Retrofit retrofit;
    private Gson gson = new Gson();
    private DailyNumService dailyNumService;
    private HourlyNumService hourlyNumService;
    //
    private List<HourlyNum> oneDayHourlyNums = new ArrayList<>();
    private List<DailyNum> dailyNums = new ArrayList<>();
    private List<HourlyNum> hourlyNums = new ArrayList<>();

    public PredictFragment() {
        // Required empty public constructor
    }

    /**
     * 加载一次，避免内存消耗
     * @return
     */
    public static PredictFragment newInstance() {
        Bundle args = new Bundle();
        PredictFragment fragment = new PredictFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化网络请求
        retrofitManager.init();
        retrofit = retrofitManager.getRetrofit();
        dailyNumService = retrofit.create(DailyNumService.class);
        hourlyNumService = retrofit.create(HourlyNumService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //加载布局
        view = inflater.inflate(R.layout.fragment_main_predict,container,false);
        //绑定控件
        ButterKnife.bind(this,view);
        //设置响应
        barChart.setOnChartValueSelectedListener(this);
        //显示heweather插件
        showHeWeatherPlugin();
        //获取今日预测
        getTodayPredict();

        initDailyNumData("2019-9-13","2019-9-20");
        initHourlyNumData("2019-9-13","2019-9-20");


        return view;
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
                            initDeviationChart(dailyNums);
                            initPieChart(dailyNums.get(dailyNums.size()-1));
                        }else {
                            Toast.makeText(getActivity(),"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getActivity(),"服务器繁忙，请稍候再试!！",Toast.LENGTH_SHORT).show();
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
                values.add(new BarEntry(i, val, ContextCompat.getDrawable(getActivity(),R.drawable.star)));
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
                    ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_blue_light),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_red_light)
            };
            int[] endColors = {
                    ContextCompat.getColor(getActivity(), android.R.color.holo_blue_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_purple),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_green_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_red_dark),
                    ContextCompat.getColor(getActivity(), android.R.color.holo_orange_dark)
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

    /**
     * 初始化偏差图
     * @param dailyNums
     */
    private void initDeviationChart(List<DailyNum> dailyNums){
        deviationBarChart.setExtraTopOffset(-20f);
        deviationBarChart.setExtraBottomOffset(10f);
        deviationBarChart.setExtraLeftOffset(20f);
        deviationBarChart.setExtraRightOffset(20f);

        deviationBarChart.setDrawBarShadow(false);
        deviationBarChart.setDrawValueAboveBar(true);

        deviationBarChart.getDescription().setEnabled(false);

        // scaling can now only be done on x- and y-axis separately
        deviationBarChart.setPinchZoom(false);

        deviationBarChart.setDrawGridBackground(false);

        XAxis xAxis = deviationBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf"));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setTextColor(Color.LTGRAY);
        xAxis.setTextSize(13f);
        xAxis.setLabelCount(dailyNums.size());
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1f);

        YAxis left = deviationBarChart.getAxisLeft();
        left.setDrawLabels(false);
        left.setSpaceTop(20f);
        left.setSpaceBottom(20f);
        left.setDrawAxisLine(false);
        left.setDrawGridLines(false);
        left.setDrawZeroLine(true); // draw a zero line
        left.setZeroLineColor(Color.GRAY);
        left.setZeroLineWidth(0.7f);
        deviationBarChart.getAxisRight().setEnabled(false);
        deviationBarChart.getLegend().setEnabled(false);

        final String[] labels = new String[dailyNums.size()];
        for (int i = 0; i <dailyNums.size() ; i++) {
            labels[i] = dailyNums.get(i).getDateName().substring(5,10);
        }
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                if ((int)value>=labels.length || (int)value < 0){
                    return "";
                }else{
                    return labels[(int) value];
                }
            }
        });

        setDeviationData(dailyNums);
    }

    /**
     * 设置偏差图数据
     * @param dailyNums
     */
    private void setDeviationData(List<DailyNum> dailyNums) {

        ArrayList<BarEntry> values = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        int green = Color.rgb(110, 190, 102);
        int red = Color.rgb(211, 74, 88);

        for (int i = 0; i < dailyNums.size(); i++) {
            DailyNum d = dailyNums.get(i);
            BarEntry entry = new BarEntry(i, (float) d.getDeviationRate());
            values.add(entry);

            // specific colors
            if (d.getDeviationRate() >= 0)
                colors.add(green);
            else
                colors.add(red);
        }

        BarDataSet set;

        if (deviationBarChart.getData() != null && deviationBarChart.getData().getDataSetCount() > 0) {
            set = (BarDataSet) deviationBarChart.getData().getDataSetByIndex(0);
            set.setValues(values);
            deviationBarChart.getData().notifyDataChanged();
            deviationBarChart.notifyDataSetChanged();
        } else {
            set = new BarDataSet(values, "近日预测偏差率");
            set.setColors(colors);
            set.setValueTextColors(colors);

            BarData data = new BarData(set);
            data.setValueTextSize(13f);
            //data.setValueTypeface(tfRegular);
            //data.setValueFormatter(new Formatter());
            data.setBarWidth(0.8f);

            deviationBarChart.setData(data);
            deviationBarChart.invalidate();
        }
    }


    /**
     * 获取今天前几天的日期
     * @param preNum 往前数几天
     * @return
     */
    private String getPreDate(int preNum) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        // 后一天为 +1 ,前一天 为-1
        calendar.set(Calendar.DATE,day - preNum);
        String preDate = format.format(calendar.getTime());
        return preDate;
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
                            Toast.makeText(getActivity(),"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getActivity(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
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
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.free_count));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.free_count));
        }else if (num > 8000 && num <= 15000){
            predictLevelView.setText(levels[1]);
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.tips));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.tips));
        }else {
            predictLevelView.setText(levels[2]);
            predictLevelView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
            predictNumView.setTextColor(ContextCompat.getColor(getActivity(),R.color.red));
        }
    }

    /**
     * 获取小时数据
     * @param startTime
     * @param endTime
     */
    @SuppressLint("CheckResult")
    private void initHourlyNumData(String startTime, String endTime) {
        hourlyNumService.getPeriodHourlyNum(startTime,endTime)
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResultObj>() {
                    @Override
                    public void accept(ResultObj resultObj) throws Exception {
                        if (resultObj.getCode() == ResultCode.OK){
                            String data = gson.toJson(resultObj.getData());
                            hourlyNums = gson.fromJson(data,new TypeToken<List<HourlyNum>>(){}.getType());
                            //设置初始的折线图，以免没数据
                            String date = hourlyNums.get(hourlyNums.size()-1).getDateName();
                            handleHourlyData(date);
                            initLineChart(date);
                        }else {
                            Toast.makeText(getActivity(),"似乎出了一点小问题...",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG,throwable.getMessage());
                        Toast.makeText(getActivity(),"服务器繁忙，请稍候再试！",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 找出某一天的小时数据
     * @param dateName
     */
    private void handleHourlyData(String dateName) {
        oneDayHourlyNums.clear();
        for (int i = 0; i < hourlyNums.size(); i++) {
            if (hourlyNums.get(i).getDateName().equals(dateName)){
                oneDayHourlyNums.add(new HourlyNum(hourlyNums.get(i).getDateName(),hourlyNums.get(i).getHour(),hourlyNums.get(i).getHourNum()));
            }
//            if (oneDayHourlyNums.size()>0 && !hourlyNums.get(i).getDateName().equals(dateName)){
//                break;
//            }
        }
    }

    /**
     * 一天的小时折线图
     * @param showDateName
     */
    private void initLineChart(String showDateName) {
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragDecelerationFrictionCoef(0.9f);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.animateX(1000);
        //底部标签设置
        final String[] labels = new String[oneDayHourlyNums.size()];
        for (int i = 0; i <oneDayHourlyNums.size() ; i++) {
            labels[i] = oneDayHourlyNums.get(i).getHour();
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
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(oneDayHourlyNums.size());//横坐标显示几个;
        xAxis.setValueFormatter(formatter);

        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < oneDayHourlyNums.size(); i++) {
            entries.add(new Entry(i,oneDayHourlyNums.get(i).getHourNum()));
        }

        LineDataSet lineDataSet;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            lineDataSet = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            lineDataSet.setValues(entries);
            lineDataSet.setLabel(showDateName+"日小时客流");
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();

        } else {
            lineDataSet = new LineDataSet(entries, showDateName+"日小时客流");
            lineDataSet.setDrawIcons(false);
            lineDataSet.setLineWidth(2.5f);
            lineDataSet.setCircleRadius(4.5f);
            lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
            lineDataSet.setDrawValues(true);//设置显示数据

            List<ILineDataSet> lineDataSets = new ArrayList<>();
            lineDataSets.add(lineDataSet);

            LineData data = new LineData(lineDataSets);
            data.setValueTextColor(Color.BLACK);
            data.setValueTextSize(11f);
            data.setDrawValues(true);

            lineChart.setData(data);
            lineChart.invalidate();
        }
    }

    /**
     * 初始化8/9点客流占比半饼图
     * @param dailyNum
     */
    private void initPieChart(DailyNum dailyNum){
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);

        //pieChart.setCenterTextTypeface(tfLight);
        pieChart.setCenterText("8/9点客流占比");

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setMaxAngle(180f); // HALF CHART
        pieChart.setRotationAngle(180f);
        pieChart.setCenterTextOffset(0, -20);

        setData(dailyNum);

        pieChart.animateY(1400, Easing.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        pieChart.setEntryLabelColor(Color.WHITE);
        //pieChart.setEntryLabelTypeface(tfRegular);
        pieChart.setEntryLabelTextSize(12f);
    }

    /**
     * 设置8/9点客流占比数据
     * @param dailyNum
     */
    private void setData(DailyNum dailyNum) {

        ArrayList<PieEntry> values = new ArrayList<>();

        int todayEightNum = dailyNum.getTodayEightNum();
        int todayNineNum = dailyNum.getTodayNineNum();
        int todayTotalNum = dailyNum.getTodayTotalNum();
        values.add(new PieEntry( todayEightNum/(float)todayTotalNum,"8点前"+todayEightNum+"人"));
        values.add(new PieEntry( todayNineNum/(float)todayTotalNum,"9点前"+todayNineNum+"人"));
        values.add(new PieEntry( (todayTotalNum-todayEightNum-todayNineNum)/(float)todayTotalNum,"其他时间"+(todayTotalNum-todayEightNum-todayNineNum)+"人"));


        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(tfLight);
        pieChart.setData(data);

        pieChart.invalidate();
    }

    /**
     * 设置半饼形下的文字位置
     * @return
     */
    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("8/9点客流占比");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 14, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 14, s.length() - 15, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 14, s.length() - 15, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 14, s.length() - 15, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 14, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 14, s.length(), 0);
        return s;
    }

    /**
     * 显示和风天气的Android插件
     */
    private void showHeWeatherPlugin() {
        //初始化
        HeWeatherConfig.init(Constant.HEFENGWEATHER_PLUGIN_kEY,Constant.HEFENGWEATHER_LOCATION_HUANGSHAN_GUANGMINGDING);

        //显示
        //获取左侧大布局
        LinearLayout leftLayout = leftLargeView.getLeftLayout();
        //获取右上布局
        LinearLayout rightTopLayout = leftLargeView.getRightTopLayout();
        //获取右下布局
        LinearLayout rightBottomLayout = leftLargeView.getRightBottomLayout();

        //取消默认背景
        leftLargeView.setDefaultBack(false);

        //设置布局的背景圆角角度（单位：dp），颜色，边框宽度（单位：px），边框颜色
        leftLargeView.setStroke(30, Color.parseColor("#313a44"), 1, Color.BLACK);

        //添加天气图标到左布局，第2参数为图标宽高（宽高1：1，单位：dp）,3-6参数位左上右下的边距
        leftLargeView.addWeatherIcon(leftLayout, 50,10,5,10,5);

        //添加地址信息到右上布局
        leftLargeView.addLocation(rightTopLayout, 14, Color.WHITE,0,5,10,5);
        //添加文字AQI到右上布局
        leftLargeView.addAqiText(rightTopLayout, 14);
        //添加空气质量到右上布局
        leftLargeView.addAqiQlty(rightTopLayout, 14);
        //添加空气质量数值到右上布局
        leftLargeView.addAqiNum(rightTopLayout, 14);

        //添加天气描述到右下布局
        leftLargeView.addCond(rightBottomLayout, 14, Color.WHITE);
        //添加温度描述到右下布局
        leftLargeView.addTemp(rightBottomLayout, 14, Color.WHITE);
        //添加风向图标到右下布局
        leftLargeView.addWindIcon(rightBottomLayout, 14);
        //添加风力描述到右下布局
        leftLargeView.addWind(rightBottomLayout, 14, Color.WHITE);
        //添加降雨图标到右下布局
        leftLargeView.addRainIcon(rightBottomLayout, 14);
        //添加降雨描述到右下布局
        leftLargeView.addRainDetail(rightBottomLayout, 14, Color.WHITE);
        //添加预警图标到右下布局
        leftLargeView.addAlarmIcon(rightBottomLayout, 14);
        //添加预警描述到右下布局
        leftLargeView.addAlarmTxt(rightBottomLayout, 14);

        //设置控件的对齐方式，默认居中
        leftLargeView.setViewGravity(HeContent.GRAVITY_LEFT);
        //显示布局
        leftLargeView.show();

        leftLargeView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.heweather_plugin_ll_view:
                Intent intent = new Intent(getActivity(), WeatherH5Activity.class);
                startActivity(intent);
                break;
                default:break;
        }
    }

    //接下来是  OnChartValueSelectedListener  需要实现的两个方法
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null){
            return;
        }
        int getX = (int) e.getX();
        String oneDayDataName = dailyNums.get(getX).getDateName();

        handleHourlyData(oneDayDataName);
        initLineChart(oneDayDataName.substring(5,10));
        pieChart.clear();
        initPieChart(dailyNums.get(getX));
    }

    @Override
    public void onNothingSelected() {

    }
}