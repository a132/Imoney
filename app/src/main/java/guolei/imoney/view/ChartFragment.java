package guolei.imoney.view;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.application.MvpApplication;
import guolei.imoney.helper.TimeHelper;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    @BindView(R.id.add_button)
    Button addButton;
    @BindView(R.id.type_spinner)
    Spinner typeSpinner;
    @BindView(R.id.time_spinner)
    Spinner timeSpinner;
    private Typeface mtf;
    private static final String TAG = "chart fragment";
    private Ipresenter presenter;

    private int type;
    private int time;
    private float averageAmount;

    public ChartFragment() {
        // Required empty public constructor
        presenter = new presenterImp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);
        mtf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("统计数据");
        setupSpinner();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseChart();
            }
        });
        return view;
    }

    public void setupSpinner(){
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = position;
                Log.d(TAG,"type"+type);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = 0;
            }
        });

        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                time = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                time = 0;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MvpApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public void remove() {
        RelativeLayout chart_content = (RelativeLayout) getView().findViewById(R.id.chart_content);
        chart_content.removeAllViews();
        Log.d(TAG, "remove has been called");
    }


    public void chooseChart(){
        if(type == 0 ){
            addchart(time);
        }
        if(type ==1 ){
            addPieChart(time);
        }
    }

    public void addPieChart(int time){
        Log.d(TAG,"add piechart");
        View view = getView();
        RelativeLayout chart_content = (RelativeLayout)view.findViewById(R.id.chart_content);
        chart_content.removeAllViews();
        System.gc();
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.setMargins(2, 2, 2, 2);

        PieChart pieChart = (PieChart)view.findViewById(R.id.PieChart);

        if(pieChart == null){
            pieChart = new PieChart(getActivity());
            PieData data = getPieData(time);
            setupPieChart(pieChart,data);
            pieChart.setId(R.id.PieChart);
            chart_content.addView(pieChart,lp1);
        }
    }
    void setupPieChart(PieChart pieChart, PieData pieData){
        pieChart.setData(pieData);
        //pieChart.setDescription("pie Chart bitch");
        pieChart.animateY(2000);
    }

    public void addchart(int time) {
        View view = getView();
        Log.d(TAG,"add line chart");
        RelativeLayout chart_content = (RelativeLayout) view.findViewById(R.id.chart_content);
        chart_content.removeAllViews();
        System.gc();
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.setMargins(2, 2, 2, 2);
       // LineChart lineChart = (LineChart) view.findViewById(R.id.LineChart);
        LineChart lineChart = new LineChart(getActivity());
        chart_content.removeAllViews();
        lineChart.setId(R.id.LineChart);
        LineData data = getLineData(time);
        setupChart(lineChart, Color.rgb(137, 230, 81), data);
        chart_content.addView(lineChart, lp1);
    }

    Float getAverage(ArrayList<Float> data){
        float sum = 0;
        for(Float item : data){
            sum += item;
        }
        return sum/data.size();
    }

    LineData getLineData(int time) {
        ArrayList<String> Xvals = new ArrayList<String>();
        ArrayList<Entry> Yvals = new ArrayList<Entry>();

        switch (time){
            case 0:
                Log.d(TAG,"getLineData 0");
                String[] mMonths = getActivity().getResources().getStringArray(R.array.month);
                int pastMonth = TimeHelper.getPastMonth()+1;
                for (int i = 0; i < pastMonth; i++) {
                    Xvals.add(mMonths[i % 12]);
                }
                ArrayList<Float> amounts = presenter.getYearData();
                averageAmount = getAverage(amounts);

                for (int j = 0; j < pastMonth; j++) {
                    float vll = amounts.get(j);
                    Yvals.add(new Entry(vll, j));
                }
                break;
            case 1:
                Log.d(TAG,"getLineData 1");
                int pastDays = TimeHelper.getPastDayOfMonth();
                for(int i =0 ; i< pastDays;i++){
                    Xvals.add(i+1+"");
                }
                ArrayList<Float> monthAmount = presenter.getMonthData();
                averageAmount = getAverage(monthAmount);
                for (int j = 0; j < pastDays; j++) {
                    float vll = monthAmount.get(j);
                    Yvals.add(new Entry(vll, j));
                }
                break;
            case 2:
                Log.d(TAG,"getLineData 2");
                String[] mWeeks = getActivity().getResources().getStringArray(R.array.week);
                int pastWeekDays = TimeHelper.getPastDaysOfWeek()-1;  // 得到的天数有周日，本项目从周一开始计算，所以减去一
                Log.d(TAG,pastWeekDays+"");
                for(int i =0 ; i< pastWeekDays;i++){
                    Xvals.add(mWeeks[i % 7]);
                }
                ArrayList<Float> weekAmount = presenter.getWeekData();
                averageAmount = getAverage(weekAmount);
                for (int j = 0; j < pastWeekDays; j++) {
                    float vll = weekAmount.get(j);
                    Yvals.add(new Entry(vll, j));
                }
                break;
        }

        LineDataSet set1 = new LineDataSet(Yvals, "DataSet 1");

        set1.setLineWidth(1.75f);
        set1.setColor(Color.WHITE);
        set1.setCircleColor(Color.WHITE);
        set1.setHighLightColor(Color.WHITE);

        List<LineDataSet> dataSets = new ArrayList<LineDataSet>();  //一个DataSet代表一条曲线
        dataSets.add(set1);

        LineData data2 = new LineData(Xvals, set1);
        //LineData data = new LineData(Xvals,dataSets);
        return data2;
    }
    PieData getPieData(int time){
        ArrayList<String> Xvals  = new ArrayList<String>();
        ArrayList<Entry> Yvals = new ArrayList<Entry>();
        switch (time){
            case 0:
                Log.d(TAG,"getPieData 0");
                String[] mMonths = getActivity().getResources().getStringArray(R.array.month);
                int pastMonth = TimeHelper.getPastMonth() + 1 ;
                for(int i = 0; i < pastMonth; i++){
                    Xvals.add(mMonths[i%12]);
                }
                ArrayList<Float> amounts = presenter.getYearData();
                for(int j = 0; j < pastMonth ;j++){
                    float val = amounts.get(j);
                    Yvals.add(new Entry(val,j));
                }
                break;
            case 1:
                Log.d(TAG,"getPieData 1");
                int pastDays= TimeHelper.getPastDayOfMonth();
                for(int i = 0; i < pastDays; i++){
                    Xvals.add(i+"" );
                }
                ArrayList<Float> Monthamount = presenter.getMonthData();
                for(int j = 0; j < pastDays ;j++){
                    float val = Monthamount.get(j);
                    Yvals.add(new Entry(val,j));
                }
                break;
            case 2:
                String[] mWeeks = getActivity().getResources().getStringArray(R.array.week);
                int pastDaysOfWeek = TimeHelper.getPastDaysOfWeek()-1;   //得到的天数有周日，本项目从周一开始计算，所以减去一
                for(int i = 0; i < pastDaysOfWeek; i++){
                    Xvals.add(mWeeks[ i % 7]);
                }
                ArrayList<Float> weeekAmounts = presenter.getWeekData();
                for(int j = 0; j < pastDaysOfWeek ;j++){
                    float val = weeekAmounts.get(j);
                    Yvals.add(new Entry(val,j));
                }
                break;
        }
        PieDataSet set = new PieDataSet(Yvals,"pie dataSet");
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData(Xvals,set);
        return pieData;
    }
    void setupChart(LineChart chart, int color, LineData data) {
        //chart.setStartAtZero(true);
        chart.setDrawBorders(false);
        //chart.setDescription("hi,bitch");  //设置表格的描述

        chart.setNoDataTextDescription("no data bitch");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        //chart.

        chart.setBackgroundColor(color);
        chart.setDescriptionTypeface(mtf);

        chart.setData(data);
        // 图例， 即 dataset1
        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.CIRCLE);
        l.setFormSize(6f);
        l.setTextColor(Color.WHITE);
        l.setTypeface(mtf);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setTextColor(Color.WHITE);
        yAxis.setTypeface(mtf);
        yAxis.setLabelCount(4, false);

        // 平均线
        LimitLine averageLine = new LimitLine(averageAmount, "平均  " + averageAmount);
        averageLine.setLineColor(Color.BLUE);
        averageLine.setLineWidth(2f);
        averageLine.setTextColor(Color.WHITE);
        averageLine.setTextSize(12f);
        yAxis.addLimitLine(averageLine);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTypeface(mtf);
        chart.animateX(2000);
    }
}
