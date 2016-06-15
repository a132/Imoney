package guolei.imoney.model.db;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import guolei.imoney.helper.DataHelper;
import guolei.imoney.helper.EnumHelper;
import guolei.imoney.helper.TimeHelper;
import guolei.imoney.model.Expense;


/**
 * Created by guolei on 2016/5/11.
 */
public class DBClass extends Application{
    ExpenseSqliteOpenHelper sqliteOpenHelper;
    SQLiteDatabase db;

    private static final long oneMinute = 60 * 1000;  //ms
    private static final long oneDay = 24 * 60 * oneMinute;
    private static final String TAG = "DBClass";
    private static final String ExpenseTableName = "expense";


    public DBClass(Context context){
       if (context !=  null) {
           sqliteOpenHelper = new ExpenseSqliteOpenHelper(context);
       }
    }
    /*
    public void addExpense(Expense expense){
        ContentValues contentValues = new ContentValues();
        contentValues.put("")
    }
    */
    public ArrayList<Expense> getExpenseByType(int type){
        ArrayList<Expense> returnExpense = new ArrayList<>();
        db = sqliteOpenHelper.getReadableDatabase();
        String sql = "select * from expense where type = ?";
        Cursor cursor2 = db.rawQuery(sql,new String[]{type+""});
        returnExpense = getExpenseFromCursor(cursor2);
        return returnExpense;
    }

    public void deleteItem(int id){
        db = sqliteOpenHelper.getWritableDatabase();
        int result = db.delete("expense","id"+"="+id,null);
        db.close();
        Log.d(TAG,"deletItem"+result);
    }

    public void updateExpense( Expense expense){
        db = sqliteOpenHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AMOUNT", expense.getAmount());
        cv.put("LOCATION", expense.getLocation());
        cv.put("TYPE", expense.getType());
        cv.put("DESCRIPTION",expense.getDescription());
        db.update(ExpenseTableName,cv,"id="+expense.getId(),null);
    }


    public ArrayList<Expense> queryExpense(EnumHelper.conditionEnum condition){
        ArrayList<Expense> returnExpense = new ArrayList<Expense>();
        db = sqliteOpenHelper.getReadableDatabase();
        long time = new Date().getTime();

        if(condition != null){
            switch (condition){
                case WEEK:
                    time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.WEEK);
                    break;
                case MONTH:
                    time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.MONTH);
                    break;
                case YEAR:
                    time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.YEAR);
                    break;
            }
            String conditionSql = "select * from expense where time > ? order by id desc";
            Log.d(TAG,"order by id");
            Cursor cursor2 = db.rawQuery(conditionSql,new String[]{time+""});
            returnExpense = getExpenseFromCursor(cursor2);
            return returnExpense;
        }

        String sql = "select * from expense order by id desc";
        Cursor cursor2 = db.rawQuery(sql,null);
        returnExpense = getExpenseFromCursor(cursor2);
        db.close();
        return returnExpense;
    }
    private ArrayList<Expense> getExpenseFromCursor(Cursor cursor2){
        ArrayList<Expense> returnExpense = new ArrayList<Expense>();
        if(cursor2.getCount() > 0)
        {
            while (cursor2.moveToNext()){
                int id = cursor2.getInt(cursor2.getColumnIndex("id"));
                int type = cursor2.getInt(cursor2.getColumnIndex("type"));
                String location = cursor2.getString(cursor2.getColumnIndex("location"));
                float amount = cursor2.getFloat(cursor2.getColumnIndex("amount"));
                String description = cursor2.getString(cursor2.getColumnIndex("description"));
                long time = cursor2.getLong(cursor2.getColumnIndex("time"));
                Date date =new  Date(time);
                //这里由于expense是单例模型，所以返回的链表可能都是相同的。
                //已经修改，取消了单例模型,
                Expense expense = Expense.getExpense(id,type,amount,location,description);
                expense.setDate(date);
                returnExpense.add(expense);
            }
        }
        return returnExpense;
    }

    public ArrayList<String> getExpenseType(){
        ArrayList<String> types = new ArrayList<String>();
        db = sqliteOpenHelper.getReadableDatabase();
        String sql = "select description from expensetype";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                String type = cursor.getString(cursor.getColumnIndex("description"));
                types.add(type);
            }
        }
        db.close();
        return types;
    }

    public void addExpenseType(String description){
        db = sqliteOpenHelper.getWritableDatabase();
        String sql = "insert into expensetype (description) values ('" +description+ "')";
        db.execSQL(sql);
        db.close();
    }

    public int queryNumber(){
        int number = 0;
        db = sqliteOpenHelper.getReadableDatabase();
        System.out.println("db null");
        String sql = "select count(id) from expense;";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            number = cursor.getInt(0);
        }
        return number;
    }

    public void AddTestData(){
        for(int i = 0; i< 100; i++){
            int amount = (int)(Math.random()*100);
            ContentValues contentValues = new ContentValues();
            Date date = new Date();   //默认插入时间为消费时间，
            contentValues.put("AMOUNT",amount);  // 0-100的随机数
            contentValues.put("LOCATION", DataHelper.ITEMS[i]);
            contentValues.put("TYPE", 1);
            contentValues.put("DESCRIPTION",DataHelper.ITEMS[i]+amount);
            contentValues.put("time",date.getTime());
            db = sqliteOpenHelper.getWritableDatabase();
            db.beginTransaction();
            db.insertOrThrow(sqliteOpenHelper.EXPENSE_TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
            db.endTransaction();
    }
    }
    public void removeAll(){
        String deleteAllSql = "delete from expense";
        db = sqliteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.execSQL(deleteAllSql);
        db.setTransactionSuccessful();
        db.endTransaction();
    }
    public void removelBeforeTwoMiniture(){
        Date date = new Date();
        long time = date.getTime();
        time = time - oneMinute * 2;
        String deletesql = "delete from expense where time > "+time;
        db = sqliteOpenHelper.getWritableDatabase();
        db.execSQL(deletesql);
        db.close();
    }


    public void AddExpense(Expense expense){
        ContentValues contentValues = new ContentValues();
        Date date = new Date();   //默认插入时间为消费时间，
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timmString = format.format(date);
        Log.d(TAG,timmString);

        contentValues.put("AMOUNT", expense.getAmount());
        contentValues.put("LOCATION", expense.getLocation());
        contentValues.put("TYPE", expense.getType());
        contentValues.put("DESCRIPTION",expense.getDescription());
        contentValues.put("time",date.getTime());
        db = sqliteOpenHelper.getWritableDatabase();
        db.beginTransaction();
        db.insertOrThrow(sqliteOpenHelper.EXPENSE_TABLE_NAME, null, contentValues);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public ArrayList<Float> getYearData(){
        ArrayList<Float> yearData = new ArrayList<Float>();
        String sql = "select sum(amount) from expense where time > ? and time < ?";
        int monthOfYear = TimeHelper.getPastMonth();
        long time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.YEAR);
        float monthAmount = 0;
        db = sqliteOpenHelper.getReadableDatabase();
        for(int i = 0; i < monthOfYear + 1; i ++){
            int days = TimeHelper.getDaysOfMonth(i);
            long timeOfEndOfMonth = time + days * oneDay;
            Cursor cursor = db.rawQuery(sql,new String[]{time+"",timeOfEndOfMonth+""});
            if (cursor.moveToFirst()) {
                monthAmount = cursor.getInt(0);
            }
            yearData.add(monthAmount);
            time = timeOfEndOfMonth;
        }
        return yearData;
    }

    public ArrayList<Float> getWeekData(){
        ArrayList<Float> weekData = new ArrayList<Float>();
        String sql = "select sum(amount) from expense where time > ? and time < ?";
        int pastDaysOfWeek = TimeHelper.getPastDaysOfWeek()-1;
        long time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.WEEK);
        float daysAmount = 0;
        db = sqliteOpenHelper.getReadableDatabase();
        for(int i = 0; i< pastDaysOfWeek;i++){
            long timeOfEndOfDay = time + oneDay;
            Cursor cursor = db.rawQuery(sql,new String[]{time+"",timeOfEndOfDay+""});
            if (cursor.moveToFirst()) {
                daysAmount = cursor.getInt(0);
            }
            weekData.add(daysAmount);
            time = timeOfEndOfDay;
        }
        return weekData;
    }

    public void testQuery(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "select * from expense where time > ? and time < ?";
        int pastDaysOfWeek = TimeHelper.getPastDaysOfWeek()-1;
        long time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.WEEK);
        String daysAmount = "";
        db = sqliteOpenHelper.getReadableDatabase();
        for(int i = 0; i< pastDaysOfWeek;i++){
            long timeOfEndOfDay = time + oneDay;
            Cursor cursor = db.rawQuery(sql,new String[]{time+"",timeOfEndOfDay+""});
            if (cursor.moveToFirst()) {
                daysAmount = cursor.getString(cursor.getColumnIndex("description"));
                long atime = cursor.getLong(cursor.getColumnIndex("time"));
                String btime = formatter.format(new Date(atime));
                Log.d(TAG,i+""+btime);
                Log.d(TAG,daysAmount);
            }
            time = timeOfEndOfDay;
        }
    }


    public ArrayList<Float> getMonthData(){
        ArrayList<Float> MonthData = new ArrayList<Float>();
        String sql = "select sum(amount) from expense where time > ? and time < ?";
        int pastDaysOfMonth = TimeHelper.getPastDayOfMonth();
        long time = TimeHelper.getFisrtDay(EnumHelper.conditionEnum.MONTH);
        float daysAmount = 0;
        db = sqliteOpenHelper.getReadableDatabase();
        for(int i = 0; i< pastDaysOfMonth;i++){
            long timeOfEndOfDay = time + oneDay;
            Cursor cursor = db.rawQuery(sql,new String[]{time+"",timeOfEndOfDay+""});
            if (cursor.moveToFirst()) {
                daysAmount = cursor.getInt(0);
            }
            MonthData.add(daysAmount);
            time = timeOfEndOfDay;
        }
        return MonthData;
    }


}
