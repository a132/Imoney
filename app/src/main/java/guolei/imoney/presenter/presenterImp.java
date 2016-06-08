package guolei.imoney.presenter;

import java.util.ArrayList;

import guolei.imoney.application.MvpApplication;
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
        Expense expense = Expense.getExpense(type,amount,location,description);
        dbClass.AddExpense(expense);
    }
}
