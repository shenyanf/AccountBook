package com.shanshan.myaccountbook;

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

import com.shanshan.myaccountbook.button.AddButton;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

public class ManageIncomeAndExpenses extends AppCompatActivity {
    private ArrayAdapter adapterIncomeAndExpenses = null;
    private MyDBHelper myDBHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDBHelper = MyDBHelper.newInstance(this);
        adapterIncomeAndExpenses = new ArrayAdapter(this, R.layout.spinner_drop_down_layout);
        /* set spinner text font size */
        adapterIncomeAndExpenses.setDropDownViewResource(R.layout.spinner_drop_down_layout);

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
                Intent intent = new Intent(getApplicationContext(), AddIncomeAndExpenses.class);
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
        String incomeOrExpensesId = String.valueOf(info.id + 1);

        if (menuItemName.equals("删除收支项")) {
            System.out.println("删除收支项" + myDBHelper.getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpenses.ID + "=?", new String[]{incomeOrExpensesId}));
            myDBHelper.deleteIncomeOrExpenses(incomeOrExpensesId);
            onResume();
        } else {
            Intent intent = new Intent(this, AddIncomeAndExpenses.class);
            intent.putExtra(DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME + DBTablesDefinition.IncomeOrExpenses.ID, incomeOrExpensesId);
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