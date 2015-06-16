package ir.toloo.family_budget_manager;

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

    public Budget(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public int getId() {
        return id;
    }
}
