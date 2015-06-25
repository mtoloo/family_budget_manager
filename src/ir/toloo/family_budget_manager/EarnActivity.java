package ir.toloo.family_budget_manager;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import ir.toloo.family_budget_manager.models.Budget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by toloo on 6/25/15.
 */
public class EarnActivity extends Activity {
    private DBHelper db;
    private ArrayList<Budget> budgets;
    private EditText totalEditText;
    private EditText dateText;
    private AutoCompleteTextView itemText;
    private TextView descriptionText;
    private TextView priceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_budget_view);
        this.db = new DBHelper(this);
        this.budgets = db.getAllBudgets();
        this.totalEditText = (EditText) findViewById(R.id.singleBudgetPriceText);
        this.dateText = (EditText) findViewById(R.id.singleBudgetDateText);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                DBHelper.DATE_STRING_FORMAT, Locale.getDefault());
        String formatted_date = dateFormat.format(new Date());
        dateText.setText(formatted_date);
        this.itemText = (AutoCompleteTextView) findViewById(R.id.singleBudgetItemText);

        ArrayList<String> itemsArrayList = db.getItems(0);
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
                itemsArrayList);
        itemText.setAdapter(itemsAdapter);
        itemText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                itemText.showDropDown();
            }
        });

        this.priceText = (TextView) findViewById(R.id.singleBudgetPriceText);
        this.descriptionText = (TextView) findViewById(R.id.singleBudgetDescriptionText);
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
        Integer totalPrice = Integer.valueOf(this.priceText.getText().toString());
        String item = this.itemText.getText().toString();
        String description = this.descriptionText.getText().toString();
        for (Budget budget: budgets) {
            float budgetValue = budget.getValue();
            if (budget.getPercent() > 0)
                budgetValue += totalPrice * budget.getPercent() / 100;
            db.saveTransaction(0, date_milis, budgetValue, budget.getId(), item, description);
        }
    }
}
