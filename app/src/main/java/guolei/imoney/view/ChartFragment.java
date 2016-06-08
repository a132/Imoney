package guolei.imoney.view;


import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import guolei.imoney.R;
import guolei.imoney.application.MvpApplication;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends Fragment {

    @BindView(R.id.add_button)
    Button addButton;
    private Typeface mtf;
    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_chart, container, false);
        ButterKnife.bind(this, view);
        mtf = Typeface.createFromAsset(getActivity().getAssets(),"OpenSans-Bold.ttf");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addchart(view);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher =MvpApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    public void addchart(View view) {
        RelativeLayout chart_content = (RelativeLayout) view.findViewById(R.id.chart_content);

        LineChart lineChart = (LineChart)view.findViewById(R.id.LineChart);
        if(lineChart != null){

             LineData uselessData =  lineChart.getData();
             uselessData = null;
        }
        chart_content.removeAllViews();
        lineChart = null;

        lineChart = new LineChart(getActivity());
        lineChart.setId(R.id.LineChart);
        LineData data = getLineData(12);
        setupChart(lineChart,Color.rgb(137,230,81),data);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.setMargins(2,2,2,2);
        chart_content.addView(lineChart, lp1);
    }


    LineData getLineData(int count){
        String[] mMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        ArrayList<String> Xvals  = new ArrayList<String>();

        for(int i = 0; i < count; i++){
            Xvals.add(mMonths[i%12]);
        }

        ArrayList<Entry> Yvals = new ArrayList<Entry>();
        for(int j = 0; j < count ;j++){
            float val =  (float)(Math.random()*100)+3;
            Yvals.add(new Entry(val,j));
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

    void setupChart(LineChart chart,int color,LineData data){
        //chart.setStartAtZero(true);
        chart.setDrawBorders(false);
        chart.setDescription("hi,bitch");  //设置表格的描述

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
        yAxis.setLabelCount(4,false);

        // 平均线
        LimitLine averageLine = new LimitLine(70f,"average expense amount");
        averageLine.setLineColor(Color.BLUE);
        averageLine.setLineWidth(4f);
        averageLine.setTextColor(Color.BLACK);
        averageLine.setTextSize(12f);

        yAxis.addLimitLine(averageLine);

        XAxis xAxis = chart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTypeface(mtf);
        chart.animateX(2500);

    }
}
