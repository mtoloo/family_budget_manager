package ir.toloo.family_budget_manager;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ir.toloo.family_budget_manager.models.Transaction;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionAdapter extends BaseAdapter {
    private final ArrayList<Transaction> transactions;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public TransactionAdapter(Context c, ArrayList<Transaction> transactions) {
        this.mContext = c;
        this.layoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.transactions = transactions;
    }

    public int getCount() {
        return this.transactions.size();
    }

    public Object getItem(int position) {
        return this.transactions.get(position);
    }

    public long getItemId(int position) {
        return this.transactions.get(position).getId();
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        Transaction transaction = this.transactions.get(position);
        if (convertView == null)
            vi = this.layoutInflater.inflate(R.layout.transaction_list_item, null);
        TextView itemDescriptionText = (TextView) vi.findViewById(R.id.transactionItemDescription);
        itemDescriptionText.setText(transaction.itemName);
        TextView dateText = (TextView) vi.findViewById(R.id.transactionDate);
        CharSequence relativeTimeSpanString = DateUtils.getRelativeTimeSpanString(transaction.date, System.currentTimeMillis(),
                DateUtils.DAY_IN_MILLIS);
        dateText.setText(relativeTimeSpanString);

        TextView valueText = (TextView) vi.findViewById(R.id.transactionValue);
        valueText.setText(String.valueOf(transaction.value));
        return vi;
    }

    private int getBudgetResourceId(String budgetIcon) {
        Map<String, Integer> resourceMap = new Hashtable<String, Integer>();
        resourceMap.put("car.png", R.drawable.car);
        resourceMap.put("man.png", R.drawable.man);
        resourceMap.put("woman.png", R.drawable.woman);
        resourceMap.put("consumable.png", R.drawable.consumable);
        resourceMap.put("child.png", R.drawable.child);
        resourceMap.put("home.png", R.drawable.home);
        resourceMap.put("angel.png", R.drawable.angel);
        Integer result = resourceMap.get(budgetIcon);
        if (result == null)
            result = R.drawable.save;
        return result;
    }
}
