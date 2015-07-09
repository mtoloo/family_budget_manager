package ir.toloo.family_budget_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import ir.toloo.family_budget_manager.models.Budget;
import ir.toloo.family_budget_manager.models.Transaction;

import java.io.*;
import java.text.ParseException;
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
    public static final String DATE_STRING_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public DBHelper(Context context) {
        super(context, DBName, null, 16);
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
        db.execSQL("Create table sources " +
                "(id integer primary key," +
                "icon text, name text)");
        db.execSQL("Create table transactions " +
                "(id integer primary key," +
                "date long," +
                "value float," +
                "budgetId integer," +
                "itemId integer," +
                "sourceId integer," +
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
        if (oldVersion == 12) {
            db.execSQL("insert into budgets(id, name, icon, value) values " +
                    "(7, 'صدقه', 'angel.png', 100)");
            db.execSQL("update budgets set value = 300 where id = 3");
        }
        if (oldVersion == 13) {
            db.execSQL("insert into budgets(id, name, icon, value) values " +
                    "(8, 'متفرقه', 'misc.png', 100)");
        }
        if (oldVersion == 14) {
            db.execSQL("Create table sources " +
                    "(id integer primary key," +
                    "icon text, name text)");
            db.execSQL("alter table transactions add column source_id int");
            db.execSQL("insert into sources (id, icon, name) values (1, 'novin.png', 'نوین')");
            db.execSQL("insert into sources (id, icon, name) values (2, 'mellat.png', 'ملت')");
            db.execSQL("insert into sources (id, icon, name) values (3, 'parsian.png', 'پارسیان')");
            db.execSQL("insert into sources (id, icon, name) values (4, 'man.png', 'مرتضی')");
            db.execSQL("insert into sources (id, icon, name) values (5, 'woman.png', 'زهرا')");
        }
        if (oldVersion == 15) {
//            db.execSQL("alter table transactions remove column source_id ");
            db.execSQL("alter table transactions add column sourceId int");
        }
    }

    public ArrayList<Budget> getAllBudgets() {
        ArrayList<Budget> budgets = new ArrayList<Budget>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select b.id, b.name, b.icon, sum(case when t.value > 0 then t.value else 0 end), " +
                "sum(case when t.value < 0 then t.value else 0 end), b.value, b.percent from budgets b left join transactions t " +
                "on t.budgetId = b.id group by b.id, b.name, b.icon", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            Budget budget = new Budget(res.getInt(0), res.getString(1), res.getString(2), res.getFloat(3),
                    res.getFloat(4), res.getInt(5), res.getFloat(6));
            budgets.add(budget);
            res.moveToNext();
        }
        return budgets;
    }

    public BudgetValues getBudgetValues(int budgetId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select sum(case when value > 0 then value else 0 end), " +
                "sum(case when value < 0 then value else 0 end) from transactions";
        Cursor cursor;
        if (budgetId > 0) {
            sql += " where budgetId = ?";
            cursor = db.rawQuery(sql, new String[] {String.valueOf(budgetId)});
        }
        else {
            cursor = db.rawQuery(sql, null);
        }
        cursor.moveToFirst();

        if (!cursor.isAfterLast())
            return new BudgetValues(cursor.getFloat(0), -1 * cursor.getFloat(1));
        return new BudgetValues(0, 0);
    }

    public ArrayList<Transaction> getTransactions(int budgetId) {
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "select t.id, t.date, t.value, t.budgetId, t.itemId, i.name, s.name, t.description " +
                " from transactions as t " +
                " left join items as i on t.itemId = i.id " +
                " left join sources as s on t.sourceId = s.id ";
        Cursor cursor;
        if (budgetId > 0) {
            sql += " where t.budgetId = ? order by t.date desc";
            cursor = db.rawQuery(sql, new String[] {String.valueOf(budgetId)});
        }
        else {
            sql += " order by t.id";
            cursor = db.rawQuery(sql, null);
        }

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Transaction transaction = new Transaction(cursor.getInt(0), cursor.getLong(1),
                    cursor.getFloat(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5),
                    cursor.getString(6), cursor.getString(7));
            transactions.add(transaction);
        }
        return transactions;
    }

    public long saveTransaction(long id, long date, float price, Integer budgetId, String item, String source,
                                String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        long itemId = this.getItemId(db, item);
        long sourceId = this.getSourceId(db, source);
        ContentValues transactionValues = new ContentValues();
        transactionValues.put("date", date);
        transactionValues.put("value", price);
        transactionValues.put("budgetId", budgetId);
        transactionValues.put("itemId", itemId);
        transactionValues.put("sourceId", sourceId);
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

    private long getSourceId(SQLiteDatabase db, String name) {
        Cursor itemCursor = db.rawQuery("select id from sources where name like ?", new String[] {name});
        if (itemCursor.isAfterLast()) {
            ContentValues itemValues = new ContentValues();
            itemValues.put("name", name);
            if (name.equals(""))
                itemValues.put("id", 0);
            return db.insert("sources", null, itemValues);
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

    public ArrayList<String> getSources(int budgetId) {
        ArrayList<String> items = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from sources", null);

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

    public String export() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd", Locale.getDefault());
        String fileName = dateFormat.format(new Date()) + ".csv";
        File root = Environment.getExternalStorageDirectory();
        File exportDir = new File(root.getAbsolutePath() + File.separator + "fbm/");
        try {
            if (!exportDir.exists())
                if (!exportDir.mkdirs())
                    throw new IOException("Error creating export dir");
            File exportFile = new File(exportDir, fileName);
            exportFile.createNewFile();
            FileWriter fileWriter = new FileWriter(exportFile);
            BufferedWriter out = new BufferedWriter(fileWriter);
            out.write("id,date,value,budget,item,description\n");
            String csvValues = "";
            ArrayList<Transaction> transactions = this.getTransactions(0);
            for (Transaction transaction : transactions) {
                out.write(transaction.toCSV());
                out.write("\n");
            }
            out.close();
            return exportFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Failed!!";
    }

    public void import_csv(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.beginTransaction();
        String line = bufferedReader.readLine();
        String[] columns = line.split(",");
        while ((line = bufferedReader.readLine()) != null) {
            String data[] = line.split(",");
            Integer id = 0;
            if (!data[0].equals(""))
                id = Integer.valueOf(data[0]);

            long date_milis;
            try {
                Date date = new SimpleDateFormat(DBHelper.DATE_STRING_FORMAT, Locale.ENGLISH).parse(data[1]);
                date_milis = date.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
                date_milis = System.currentTimeMillis();
            }
            Float price = Float.parseFloat(data[2]);
            Integer budgetId = Integer.parseInt(data[3]);
            String item = data[4];
            String source = data[5];
            String description = "";
            if (data.length >= 7)
                description = data[6];
            long saveResult = this.saveTransaction(id, date_milis, price, budgetId, item, source, description);
            if (saveResult == 0)
                saveResult = saveTransaction(0, date_milis, price, budgetId, item, source, description);
        }
//        db.setTransactionSuccessful();
//        db.endTransaction();
    }
}
