package ir.toloo.family_budget_manager2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class BudgetsActivity extends Activity {
    private DBHelper db;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.db = new DBHelper(this);

        GridView gridView = (GridView) findViewById(R.id.gridView);
        final BudgetAdapter budgetAdapter = new BudgetAdapter(this, db);
        gridView.setAdapter(budgetAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.export_db:
                export_database();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void export_database() {
        String fileName = db.export();
        Toast.makeText(this, "Saved to " + fileName, Toast.LENGTH_LONG).show();
    }
}
