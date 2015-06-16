package ir.toloo.family_budget_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
        super(context, DBName, null, 10);
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
                "date long," +
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
        String sql = "select t.id, t.date, t.value, t.budgetId, t.itemId, t.description, i.name " +
                "from transactions as t left join items as i on t.itemId = i.id ";
        Cursor cursor;
        if (budgetId > 0) {
            sql += "where t.budgetId = ? order by t.id desc";
            cursor = db.rawQuery(sql, new String[] {String.valueOf(budgetId)});
        }
        else {
            sql += " order by t.id";
            cursor = db.rawQuery(sql, null);
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Transaction transaction = new Transaction(cursor.getInt(0), cursor.getLong(1),
                    cursor.getFloat(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(6),
                    cursor.getString(5));
            transactions.add(transaction);
        }
        return transactions;
    }

    public long saveTransaction(long id, long date, float price, Integer budgetId, String item, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        long itemId = this.getItemId(db, item);
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("date", date);
        transactionValues.put("value", price);
        transactionValues.put("budgetId", budgetId);
        transactionValues.put("itemId", itemId);
        transactionValues.put("description", description);
        long result;
        if (id > 0)
            result = db.update("transactions", transactionValues, "id=?", new String[] {String.valueOf(id)});
        else
            result = db.insert("transactions", null, transactionValues);
        return result;
    }

    private long getItemId(SQLiteDatabase db, String name) {
        Cursor itemCursor = db.rawQuery("select id from items where name like ?", new String[] {name});
        if (itemCursor.isAfterLast()) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("name", name);
            return db.insert("items", null, itemValues);
        }
        itemCursor.moveToFirst();
        return itemCursor.getLong(0);
    }

    public ArrayList<String> getItems(int budgetId) {
        ArrayList<String> items = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from items", null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            items.add(cursor.getString(0));
            cursor.moveToNext();
        }
        return items;
    }

    public void removeTransaction(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("transactions", "id=?", new String[] {String.valueOf(id)});
    }

    public void export() {
        String file_name = "fbm.csv";
        File root = Environment.getExternalStorageDirectory();
        File exportDir = new File(root.getAbsolutePath() + File.separator + "fbm/");
        try {
            if (!exportDir.exists())
                if (!exportDir.mkdirs())
                    throw new IOException("Error creating export dir");
            File exportFile = new File(exportDir, "transactions.csv");
            exportFile.createNewFile();
            FileWriter fileWriter = new FileWriter(exportFile);
            BufferedWriter out = new BufferedWriter(fileWriter);
            String csvHeader = "id,date,value,budget,item,description";
            String csvValues = "";
            ArrayList<Transaction> transactions = this.getTransactions(0);
            for (Transaction transaction : transactions) {
                out.write(transaction.toString());
            }
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
