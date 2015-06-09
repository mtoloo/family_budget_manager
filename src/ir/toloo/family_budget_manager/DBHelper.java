package ir.toloo.family_budget_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DBName = "budget_acc";

    public DBHelper(Context context) {
        super(context, DBName, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table budgets " +
                "(id integer primary key," +
                "name text," +
                "icon text," +
                "value integer," +
                "percent integer)");
        db.execSQL("Create table items " +
                "(id integer primary key," +
                "name text)");
        db.execSQL("Create table transactions " +
                "(id integer primary key," +
                "date date," +
                "value float," +
                "budgetId integer," +
                "itemId integer," +
                "description text)");

        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(1, 'مصرفی', 'consumable.png', 500)");
        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(2, 'ماشین', 'car.png', 100)");
        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(3, 'خونه', 'home.png', 100)");
        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(4, 'مرتضی', 'man.png', 100)");
        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(5, 'زهرا', 'woman.png', 100)");
        db.execSQL("insert into budgets(id, name, icon, value) values " +
                "(6, 'علیرضا', 'child.png', 100)");

//        db.execSQL("Insert into transactions(id, date, value, budgetId, itemId, description) " +
//                "values (1, '2015-01-01', 22, 6, 5, 'پوشک')");
//        db.execSQL("Insert into transactions(id, date, value, budgetId, itemId, description) " +
//                "values (2, '2015-01-02', 22, 6, 6, 'شیشه')");
//        db.execSQL("Insert into transactions(id, date, value, budgetId, itemId, description) " +
//                "values (3, '2015-02-02', 22, 5, 4, 'کفش')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists budgets");
        db.execSQL("drop table if exists items");
        db.execSQL("drop table if exists transactions");
        onCreate(db);
    }

    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id, name, icon from budgets", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Budget budget = new Budget(res.getInt(0), res.getString(1), res.getString(2));
            budgets.add(budget);
            res.moveToNext();
        }
        return budgets;
    }

    public ArrayList<Transaction> getTransactions(int budgetId) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select id, date, value, budgetId, itemId, description from transactions order by id desc", null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Transaction transaction = new Transaction(cursor.getInt(0), cursor.getFloat(1),
                    cursor.getFloat(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
            transactions.add(transaction);
            cursor.moveToNext();
        }
        return transactions;
    }

    public void saveTransaction(Date date, float price, Integer budgetId, int itemId, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues transactionValues = new ContentValues();
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formatted_date = dateFormat.format(date);
        transactionValues.put("date", "2015-01-01");
        transactionValues.put("value", price);
        transactionValues.put("budgetId", budgetId);
        transactionValues.put("itemId", itemId);
        transactionValues.put("description", description);
        long result = db.insert("transactions", null, transactionValues);
        result += 1;
    }
}
