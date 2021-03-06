package ir.toloo.family_budget_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ir.toloo.family_budget_manager.models.Transaction;

import java.text.ParseException;
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
    private EditText dateText;
    private AutoCompleteTextView itemText;
    private TextView priceText;
    private TextView descriptionText;
    private EditText idText;
    private AutoCompleteTextView sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_budget_view);
        this.db = new DBHelper(this);
        Intent intent = getIntent();
        budgetId = intent.getExtras().getInt("id");
        idText = (EditText) findViewById(R.id.singleBudgetIdText);
        dateText = (EditText) findViewById(R.id.singleBudgetDateText);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DBHelper.DATE_STRING_FORMAT, Locale.getDefault());
        String formatted_date = dateFormat.format(new Date());
        dateText.setText(formatted_date);
        showTransactions(budgetId);
        ArrayList<String> itemsArrayList = db.getItems(budgetId);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                itemsArrayList);
        itemText = (AutoCompleteTextView) findViewById(R.id.singleBudgetItemText);
        itemText.setAdapter(itemsAdapter);
        itemText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemText.showDropDown();
            }
        });

        ArrayList<String> sourcesArrayList = db.getSources(budgetId);
        ArrayAdapter<String> sourcesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                sourcesArrayList);
        sourceText = (AutoCompleteTextView) findViewById(R.id.singleBudgetSourceText);
        itemText.setAdapter(sourcesAdapter);
        itemText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sourceText.showDropDown();
            }
        });

        priceText = (TextView) findViewById(R.id.singleBudgetPriceText);
        descriptionText = (TextView) findViewById(R.id.singleBudgetDescriptionText);

        BudgetValues budgetValues = db.getBudgetValues(budgetId);
        TextView incomeText = (TextView) findViewById(R.id.singleBudgetIncomeTextView);
        incomeText.setText(String.valueOf(budgetValues.income));
        TextView expenseText = (TextView) findViewById(R.id.singleBudgetExpenseTextView);
        expenseText.setText(String.valueOf(budgetValues.expense));
        TextView remainText = (TextView) findViewById(R.id.singleBudgetRemainTextView);
        remainText.setText(String.valueOf(budgetValues.remain));
    }

    private void showTransactions(final int budgetId) {
        final ArrayList<Transaction> transactions = this.db.getTransactions(budgetId);
        TransactionAdapter transactionArrayAdapter = new TransactionAdapter(this, transactions);
        ListView transactionListView = (ListView) findViewById(R.id.singleBudgetTransactions);
        transactionListView.setAdapter(transactionArrayAdapter);
        final SingleBudgetActivity singleBudget = this;
        transactionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Transaction transaction = transactions.get(position);
                singleBudget.idText.setText(String.valueOf(transaction.getId()));
                singleBudget.budgetId = transaction.getBudgetId();
                singleBudget.itemText.setText(transaction.itemName);
                singleBudget.sourceText.setText(transaction.sourceName);
                singleBudget.priceText.setText(String.valueOf(transaction.value * -1));
                singleBudget.descriptionText.setText(String.valueOf(transaction.description));
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        DBHelper.DATE_STRING_FORMAT, Locale.getDefault());
                String formatted_date = dateFormat.format(transaction.date);
                singleBudget.dateText.setText(formatted_date);
            }
        });
        transactionListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Transaction transaction = transactions.get(position);
                db.removeTransaction(transaction.getId());
                Toast.makeText(singleBudget, "Transaction removed", Toast.LENGTH_LONG).show();
//                showTransactions(budgetId);
                return false;
            }
        });
    }

    public void save(View view) {
        String dateStr = dateText.getText().toString();
        long date_milis;
        try {
            Date date = new SimpleDateFormat(DBHelper.DATE_STRING_FORMAT, Locale.ENGLISH).parse(dateStr);
            date_milis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            date_milis = System.currentTimeMillis();
        }
        long id = 0;
        if (!idText.getText().toString().equals(""))
            id = Long.parseLong(idText.getText().toString());

        this.db.saveTransaction(
                id,
                date_milis,
                Float.parseFloat(priceText.getText().toString()) * -1,
                budgetId, itemText.getText().toString(),
                sourceText.getText().toString(),
                descriptionText.getText().toString());

        if (id == 0)
            this.showTransactions(budgetId);
    }
}
