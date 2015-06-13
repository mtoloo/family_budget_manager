package ir.toloo.family_budget_manager;

import android.text.format.DateUtils;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {
    private final long id;
    private final long date;
    public final float value;
    private final int budgetId;
    private final int itemId;
    public final String itemName;
    public final String description;

    public Transaction(long id, long date, float value, int budgetId, int itemId, String itemName, String description) {
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

    public long getId() {
        return id;
    }

    public int getBudgetId() {
        return budgetId;
    }
}
