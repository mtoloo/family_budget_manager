package ir.toloo.family_budget_manager.models;

import android.text.format.DateUtils;
import ir.toloo.family_budget_manager.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {
    private final long id;
    public final long date;
    public final float value;
    private final int budgetId;
    private final int itemId;
    public final String itemName;
    public final String description;
    public final String sourceName;

    public Transaction(long id, long date, float value, int budgetId, int itemId, String itemName, String sourceName,
                       String description) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.budgetId = budgetId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.sourceName = sourceName;
        this.description = description;
    }

    @Override
    public String toString() {
        String result = itemName;
        if (!description.isEmpty())
            result += "(" + description + ")";
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(date, System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS);
        result = String.format("%s = %.1f at %s", result, value, relativeTimeSpanString);
        return result;
    }

    public long getId() {
        return id;
    }

    public int getBudgetId() {
        return budgetId;
    }

    public String toCSV() {
        // id,date,value,budget,item,description
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DBHelper.DATE_STRING_FORMAT, Locale.getDefault());
        String formatted_date = dateFormat.format(date);
        String budgetName = String.valueOf(budgetId);
        String result = String.format("%d,%s,%f,%s,%s,%s", id, formatted_date, value, budgetName, itemName, description);
        return result;
    }
}
