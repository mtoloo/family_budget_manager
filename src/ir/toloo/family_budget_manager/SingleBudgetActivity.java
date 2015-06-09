package ir.toloo.family_budget_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mtoloo
 * Date: 5/29/15
 * Time: 1:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class SingleBudgetActivity extends Activity{
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_budget_view);
        this.db = new DBHelper(this);
        Intent inten = getIntent();
        int budgetId = inten.getExtras().getInt("id");
        DatePicker dateText = (DatePicker) findViewById(R.id.singleBudgetDateText);
//        dateText.setv
        showTransactions(budgetId);
    }

    private void showTransactions(int budgetId) {
        ArrayList<Transaction> transactions = this.db.getTransactions(budgetId);
        ArrayAdapter<Transaction> transactionArrayAdapter = new ArrayAdapter<Transaction>(this, android.R.layout.simple_list_item_1, transactions);
        ListView transactionListView = (ListView) findViewById(R.id.singleBudgetTransactions);
        transactionListView.setAdapter(transactionArrayAdapter);
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    public void save(View view) {
        Bundle extras = getIntent().getExtras();
        Integer budgetId = extras.getInt("budgetId");
        EditText priceText = (EditText) findViewById(R.id.singleBudgetPriceText);
        DatePicker dateText = (DatePicker) findViewById(R.id.singleBudgetDateText);
        EditText descriptionText = (EditText) findViewById(R.id.singleBudgetDescriptionText);
        this.db.saveTransaction(new Date(dateText.getYear() - 1900, dateText.getMonth(), dateText.getDayOfMonth()),
                Float.parseFloat(priceText.getText().toString()),
                budgetId, 0, descriptionText.getText().toString());
        this.showTransactions(budgetId);
    }
}
