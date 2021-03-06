package com.shanshan.myaccountbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.button.AddButton;
import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.IncomeAndExpensesEntity;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;


public class ManageIncomeAndExpensesActivity extends AppCompatActivity {
    private ArrayAdapter adapterIncomeAndExpenses = null;
    private MyDBHelper myDBHelper = null;
    private Logger myLogger = MyLogger.getMyLogger(ManageIncomeAndExpensesActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDBHelper = MyDBHelper.newInstance(this);
        adapterIncomeAndExpenses = new ArrayAdapter(this, R.layout.spinner_drop_down_normal);
        /* set spinner text font size */
        adapterIncomeAndExpenses.setDropDownViewResource(R.layout.spinner_drop_down_normal);

        setContentView(R.layout.activity_manage_income_and_expenses);
        ListView incomeAndExpensesList = (ListView) findViewById(R.id.list_income_and_expenses);
        incomeAndExpensesList.setAdapter(adapterIncomeAndExpenses);
        incomeAndExpensesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            }
        });
        registerForContextMenu(incomeAndExpensesList);

        AddButton addButton = (AddButton) findViewById(R.id.add_income_expenses);
        addButton.setText("添加收支项");
        addButton.setTextSize(20);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddIncomeAndExpensesActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.list_income_and_expenses) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("收支项管理");
            String[] menuItems = getResources().getStringArray(R.array.income_expenses_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.income_expenses_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = "收支项管理";
        IncomeAndExpensesEntity incomeAndExpensesEntity = (IncomeAndExpensesEntity) adapterIncomeAndExpenses.getItem(info.position);

        String incomeOrExpensesId = String.valueOf(incomeAndExpensesEntity.getId());

        if (menuItemName.equals("删除收支项")) {
            myLogger.debug("删除收支项 id:"+incomeOrExpensesId+" name:" + myDBHelper.getIncomeAndExpenses(IncomeOrExpensesDefinition.ID + "=?", new String[]{incomeOrExpensesId}));
            myDBHelper.deleteIncomeOrExpenses(incomeOrExpensesId);
            onResume();
        } else {
            Intent intent = new Intent(this, AddIncomeAndExpensesActivity.class);
            intent.putExtra(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME + IncomeOrExpensesDefinition.ID, incomeOrExpensesId);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        adapterIncomeAndExpenses.clear();
        adapterIncomeAndExpenses.addAll(myDBHelper.getIncomeAndExpenses());
        adapterIncomeAndExpenses.notifyDataSetChanged();
    }
}