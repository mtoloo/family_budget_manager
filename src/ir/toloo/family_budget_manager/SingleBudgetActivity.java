package ir.toloo.family_budget_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleBudgetActivity extends Activity{
    private DBHelper db;
    private int budgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_budget_view);
        this.db = new DBHelper(this);
        Intent intent = getIntent();
        budgetId = intent.getExtras().getInt("id");
        EditText dateText = (EditText) findViewById(R.id.singleBudgetDateText);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formatted_date = dateFormat.format(new Date());
        dateText.setText(formatted_date);
        showTransactions(budgetId);
        ArrayList<String> itemsArrayList = db.getItems(budgetId);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                itemsArrayList);
        final AutoCompleteTextView itemTextView = (AutoCompleteTextView) findViewById(R.id.singleBudgetItemText);
        itemTextView.setAdapter(itemsAdapter);
        itemTextView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) {
                itemTextView.showDropDown();
            }
        });
    }

    private void showTransactions(final int budgetId) {
        final ArrayList<Transaction> transactions = this.db.getTransactions(budgetId);
        ArrayAdapter<Transaction> transactionArrayAdapter = new ArrayAdapter<Transaction>(this, android.R.layout.simple_list_item_1, transactions);
        ListView transactionListView = (ListView) findViewById(R.id.singleBudgetTransactions);
        transactionListView.setAdapter(transactionArrayAdapter);
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        transactionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction transaction = transactions.get(position);
                db.removeTransaction(transaction.getId());
                showTransactions(budgetId);
//                transactions.remove(position);
                return false;
            }
        });
    }

    public void save(View view) {
        Bundle extras = getIntent().getExtras();
        EditText priceText = (EditText) findViewById(R.id.singleBudgetPriceText);
        EditText itemText = (EditText) findViewById(R.id.singleBudgetItemText);
        EditText dateText = (EditText) findViewById(R.id.singleBudgetDateText);
        EditText descriptionText = (EditText) findViewById(R.id.singleBudgetDescriptionText);
        this.db.saveTransaction(dateText.getText().toString(),
                Float.parseFloat(priceText.getText().toString()),
                budgetId, itemText.getText().toString(),
                descriptionText.getText().toString());
        this.showTransactions(budgetId);
    }
}
