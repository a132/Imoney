package guolei.imoney.presenter;

import java.util.ArrayList;

/**
 * Created by guolei on 2016/6/8.
 */
public interface Ipresenter {
    ArrayList<String> getExpenseType();
    void addExpenseType(String description);
    void addNewExpense(int type,float amount,String location, String description);

}
