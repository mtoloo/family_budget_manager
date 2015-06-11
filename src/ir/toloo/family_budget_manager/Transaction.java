package ir.toloo.family_budget_manager;

import android.text.format.DateFormat;
import android.text.format.DateUtils;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {
    private final int id;
    private final long date;
    private final float value;
    private final int budgetId;
    private final int itemId;
    private final String itemName;
    private final String description;

    public Transaction(int id, long date, float value, int budgetId, int itemId, String itemName, String description) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.budgetId = budgetId;
        this.itemId = itemId;
        this.itemName = itemName;
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

    public int getId() {
        return id;
    }
}
