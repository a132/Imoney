package guolei.imoney.view;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import guolei.imoney.R;
import guolei.imoney.util.EnumHelper;
import guolei.imoney.presenter.Ipresenter;
import guolei.imoney.presenter.presenterImp;


public class BudgetFragment extends Fragment {


    @BindView(R.id.month_budget)
    TextView monthBudget;
    @BindView(R.id.month_spend)
    TextView monthSpend;
    @BindView(R.id.year_spend)
    TextView yearSpend;
    @BindView(R.id.week_spend)
    TextView weekSpend;
    @BindView(R.id.day_spend)
    TextView daySpend;
    @BindView(R.id.addBudget)
    Button addBudget;
    @BindView(R.id.budget_content)
    RelativeLayout budgetContent;
    @BindView(R.id.month_spend_text)
    TextView monthSpendText;

    private Ipresenter presenter;
    private Context context;
    private SharedPreferences sp;
    private float month_spend;

    public BudgetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        ButterKnife.bind(this, view);
        this.context = getActivity();
        sp = context.getSharedPreferences("Password", Context.MODE_PRIVATE);
        presenter = new presenterImp();
        setupUI();
        return view;
    }

    void setupUI() {
        month_spend = presenter.getTotalAmount(EnumHelper.conditionEnum.MONTH);
        float year_spend = presenter.getTotalAmount(EnumHelper.conditionEnum.YEAR);
        float week_spend = presenter.getTotalAmount(EnumHelper.conditionEnum.WEEK);
        float day_spend = presenter.getTotalAmount(EnumHelper.conditionEnum.DAY);

        monthSpend.setText(month_spend + "");
        yearSpend.setText(year_spend + "");
        weekSpend.setText(week_spend + "");
        daySpend.setText(day_spend+ "");

        String budget = sp.getString("budget", "0");
        float budgetValue = Float.parseFloat(budget);
        monthBudget.setText(budget);

        if (budgetValue < month_spend) {
            monthSpendText.setTextColor(Color.parseColor("#E43F3F"));
            monthSpend.setTextColor(Color.parseColor("#E43F3F"));
        }

        addChart(budgetValue);

    }


    // 返回消费指数
    int  getAssess(float budget, float month_spend,float month_income){

        if(budget < 0 || month_income < 0 || month_spend < 0){
            return  -1;
        }

        if(budget>month_spend && month_spend>month_income)
        {
            return 4;
        }
        if(month_income>month_spend && month_spend>budget)
        {
            return 2;
        }
        if(month_spend>budget && budget>month_income)
        {
            return 6;
        }
        if(month_income>budget && budget>month_spend)
        {
            return 1;
        }
        if(budget>month_income && month_income>month_spend)
        {
            return 3;
        }
        if(month_spend>month_income && month_income>budget)
        {
            return 5;
        }
        return  0;
    }
    void addChart(float budgetValue) {
        HorizontalBarChart mChart = new HorizontalBarChart(context);
        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        mChart.setDescription("");
        mChart.setMaxVisibleValueCount(2);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
        yl.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = mChart.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinValue(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        // set data
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("本月消费");
        xVals.add("本月预算");

        yVals1.add(new BarEntry(month_spend, 0));
        yVals1.add(new BarEntry(budgetValue, 1));

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals1);
            mChart.getData().setXVals(xVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "预算");

            if (budgetValue > month_spend) {
                set1.setColor(Color.parseColor("#39b894"));
            } else {
                set1.setColor(Color.parseColor("#E43F3F"));
            }
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);


            BarData data = new BarData(xVals, dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(tf);

            mChart.setData(data);
        }
        mChart.animateY(2500);

        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp1.setMargins(2, 2, 2, 2);
        budgetContent.removeAllViews();
        System.gc();
        budgetContent.addView(mChart, lp1);
    }

    @OnClick(R.id.addBudget)
    public void onClick() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        builder.setTitle("设定本月预算").
                setView(editText);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String budget = editText.getText().toString();
                Float budget_value = Float.parseFloat(budget);
                if(budget_value < 0){
                    editText.setError("不能小于0");
                    return;
                }
                if (budget.isEmpty()) {
                    editText.setError("不能为空");
                    return;
                } else {
                    editText.setError(null);
                }
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("budget", budget);
                editor.commit();
                addChart(budget_value);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
