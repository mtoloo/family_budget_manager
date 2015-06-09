package ir.toloo.family_budget_manager;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class Transaction {
    private final int id;
    private final float date;
    private final float value;
    private final int budgetId;
    private final int itemId;
    private final String description;

    public Transaction(int id, float date, float value, int budgetId, int itemId, String description) {
        this.id = id;
        this.date = date;
        this.value = value;
        this.budgetId = budgetId;
        this.itemId = itemId;
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}
