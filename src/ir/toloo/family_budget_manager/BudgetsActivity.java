package ir.toloo.family_budget_manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

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
        gridView.setAdapter(new BudgetAdapter(this, db));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent singleBudgetIntent = new Intent(getApplicationContext(), SingleBudgetActivity.class);
                singleBudgetIntent.putExtra("id", position);
                startActivity(singleBudgetIntent);
            }
        });
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
}
