package ir.toloo.family_budget_manager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import ir.toloo.family_budget_manager.models.Budget;

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
public class BudgetAdapter extends BaseAdapter {
    private final ArrayList<Budget> budgets;
    private Context mContext;
    public BudgetAdapter(Context c, DBHelper db) {
        this.mContext = c;
        this.budgets = db.getAllBudgets();
    }

    public int getCount() {
        return this.budgets.size();
    }

    public Object getItem(int position) {
        return this.budgets.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Budget budget = this.budgets.get(position);
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        Integer budgetResourceId = this.getBudgetResourceId(budget.getIcon());
        Bitmap image = BitmapFactory.decodeResource(this.mContext.getResources(), budgetResourceId);
        imageView.setImageBitmap(image);
        final Context context = mContext;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent singleBudgetIntent = new Intent(context, SingleBudgetActivity.class);
                singleBudgetIntent.putExtra("id", budgets.get(position).getId());
                context.startActivity(singleBudgetIntent);
            }
        });
        return imageView;
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
