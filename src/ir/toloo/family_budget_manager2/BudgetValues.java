package ir.toloo.family_budget_manager2;

/**
 * Created by toloo on 6/18/15.
 */
public class BudgetValues {
    public float income, expense, remain;
    public BudgetValues (float income, float expense) {
        this.income = income;
        this.expense = expense;
        this.remain = income - expense;
    }
}
