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

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionAdapter extends BaseAdapter {
    private final ArrayList<Transaction> transactions;
    private LayoutInflater layoutInflater;
    public TransactionAdapter(Context c, ArrayList<Transaction> transactions) {
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

        TextView descriptionText = (TextView) vi.findViewById(R.id.transactionDescription);
        descriptionText.setText(String.valueOf(transaction.description));

        if (transaction.value > 0)
            vi.setBackgroundColor(vi.getResources().getColor(R.color.Income));
        else
            vi.setBackgroundColor(vi.getResources().getColor(R.color.Expense));
        return vi;
    }
}
