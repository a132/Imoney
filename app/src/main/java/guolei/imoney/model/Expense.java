package guolei.imoney.model;

import java.util.Calendar;
import java.util.Date;

/**
 * 单例模型
 * Created by guolei on 2016/5/6.
 */
public class Expense {
    private int type;
    private float amount;
    private String location;
    private String description;
    private Date date;
    private int month;
    private int id;

    private static class ExpenseHolder{
        private static final Expense Instace = new Expense();
    }

    private Expense(){
    }
    public int getId(){return this.id;}
    public float getAmount(){
        return this.amount;
    }
    public String getLocation() { return this.location; }
    public int getType() { return  this.type;}
    public String getDescription() { return this.description; }
    public Date getDate(){
        return date != null? date: null;
    }
    public int getMonth(){ return month;}

    public void setDate(Date date){
        this.date = date;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        this.month = cal.get(Calendar.MONTH);
    }
    public void setId(int id){
        this.id = id;
    }


    public static final Expense getExpense(int id,int type,float amount,String location, String description){
        Expense newone = new Expense();
        newone.id = id;
        newone.type = type;
        newone.amount = amount;
        newone.location = location;
        newone.description = description;
        return newone;
        /*
        //单例模型
        ExpenseHolder.Instace.amount= amount;
        ExpenseHolder.Instace.location = location;
        ExpenseHolder.Instace.type = type;
        ExpenseHolder.Instace.description = description;
        return ExpenseHolder.Instace;
        */
    }
}
