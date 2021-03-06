package guolei.imoney.presenter;

import java.util.ArrayList;

import guolei.imoney.application.MvpApplication;
import guolei.imoney.util.EnumHelper;
import guolei.imoney.model.Expense;
import guolei.imoney.model.db.DBClass;

/**
 * Created by guolei on 2016/6/8.
 */
public class presenterImp implements Ipresenter {
    private DBClass dbClass;

    public presenterImp(){
        dbClass = new DBClass(MvpApplication.getApplication());
    }

    @Override
    public ArrayList<String> getExpenseType() {
        return dbClass.getExpenseType();
    }

    @Override
    public void addExpenseType(String description) {
        dbClass.addExpenseType(description);
    }

    @Override
    public void addNewExpense(int type, float amount, String location, String description) {
        Expense expense = Expense.getExpense(1,type,amount,location,description); // 1 is useless,
        dbClass.AddExpense(expense);
    }
    @Override
    public ArrayList<Expense> getExpense(EnumHelper.conditionEnum conditionEnum) {
        return dbClass.queryExpense(conditionEnum);
    }

    @Override
    public ArrayList<Float> getMonthData() {
        return dbClass.getMonthData();
    }

    @Override
    public ArrayList<Float> getWeekData() {
        return dbClass.getWeekData();
    }

    @Override
    public ArrayList<Float> getYearData() {
        return dbClass.getYearData();
    }

    @Override
    public ArrayList<Expense> getExpenseByType(int type) {
        return dbClass.getExpenseByType(type);
    }

    @Override
    public void deleteExpense(int id) {
        dbClass.deleteItem(id);
    }
    @Override
    public void updateExpense(Expense expense) {
        dbClass.updateExpense(expense);
    }

    @Override
    public float getTotalAmount(EnumHelper.conditionEnum condition) {
        return dbClass.getFloat(condition);
    }
}
