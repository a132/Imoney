package guolei.imoney.model.db;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import guolei.imoney.helper.DataHelper;
import guolei.imoney.model.Expense;


/**
 * Created by guolei on 2016/5/11.
 */
public class DBClass extends Application{
    ExpenseSqliteOpenHelper sqliteOpenHelper;
    SQLiteDatabase db;

    private static String insertString = "";
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
    public ArrayList<Expense> queryExpense(String condition){
        ArrayList<Expense> returnExpense = new ArrayList<Expense>();
        db = sqliteOpenHelper.getReadableDatabase();
        String sql = "select * from expense";
        Cursor cursor2 = db.rawQuery(sql,null);
        //Cursor cursor = db.query(sqliteOpenHelper.EXPENSE_TABLE_NAME,new String[] = ,"")
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
                Expense expense = Expense.getExpense(type,amount,location,description);
                expense.setDate(date);
                returnExpense.add(expense);
            }
        }
        db.close();
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

    public void AddExpense(Expense expense){
        ContentValues contentValues = new ContentValues();
        Date date = new Date();   //默认插入时间为消费时间，
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
}
