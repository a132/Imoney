package guolei.imoney.presenter;

import java.util.ArrayList;

import guolei.imoney.helper.EnumHelper;
import guolei.imoney.model.Expense;

/**
 * Created by guolei on 2016/6/8.
 */
public interface Ipresenter {
    ArrayList<String> getExpenseType();
    void addExpenseType(String description);
    void addNewExpense(int type,float amount,String location, String description);
    ArrayList<Expense> getExpense(EnumHelper.conditionEnum conditionEnum);
    ArrayList<Float> getYearData();
    ArrayList<Float> getMonthData();
    ArrayList<Float> getWeekData();
    ArrayList<Expense> getExpenseByType(int type);


}
