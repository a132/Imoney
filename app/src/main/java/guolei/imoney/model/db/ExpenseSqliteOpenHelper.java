package guolei.imoney.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by guolei on 2016/5/11.
 */
public class ExpenseSqliteOpenHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION= 7;

    private static final String DB_NAME = "expense.db";
    public static final String EXPENSE_TABLE_NAME = "expense";
    public static final String EXPENSE_TYPE_TABLE_NAME = "expensetype";


    public ExpenseSqliteOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // create table Orders(Id integer primary key, CustomName text, OrderPrice integer, Country text);

        String createExpenseTable = "create table if not exists " + EXPENSE_TABLE_NAME
                + " (id integer primary key autoincrement,amount real,location text,description text,type integer,time integer)";
        sqLiteDatabase.execSQL(createExpenseTable);
        String createExpenseTypeTable = "create table if not exists expensetype" + " (id integer primary key autoincrement, description text)";
        sqLiteDatabase.execSQL(createExpenseTypeTable);

        String[] expenseType = {"食物","衣服","交通","教育","生活用品"};

        for(String item : expenseType){
            ContentValues contentValues = new ContentValues();
            contentValues.put("description",item);
            sqLiteDatabase.insertOrThrow(EXPENSE_TYPE_TABLE_NAME,null,contentValues);
            Log.d("sqlliteHelper",item);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + EXPENSE_TABLE_NAME;
        String sql2 = "DROP TABLE IF EXISTS "+ EXPENSE_TYPE_TABLE_NAME;
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }
}
