package ir.toloo.family_budget_manager.models;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Budget {
    private final int id;
    private final String name;
    private final String icon;
    private final float income;
    private final float expense;
    private final Integer value;
    private Float percent;

    public Budget(int id, String name, String icon, float income, float expense, Integer value, Float percent) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.income = income;
        this.expense = expense;
        this.value = value;
        this.percent = percent;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }

    public Integer getValue() {
        return value;
    }

    public Float getPercent() {
        return percent;
    }
}
