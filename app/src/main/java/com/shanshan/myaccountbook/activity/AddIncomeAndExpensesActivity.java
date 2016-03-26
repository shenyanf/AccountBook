package com.shanshan.myaccountbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shanshan.myaccountbook.util.MyLogger;
import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.IncomeAndExpensesEntity;

import org.apache.log4j.Logger;

public class AddIncomeAndExpensesActivity extends Activity {
    private Logger myLogger = MyLogger.getMyLogger(AddIncomeAndExpensesActivity.class.getName());

    private MyDBHelper myDBHelper = null;
    private Spinner spinnerIncomeOrExpenses = null;
    private String incomeOrExpensesId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*not display title*/
        setTitle("添加收支项");
        setContentView(R.layout.activity_add_income_and_expenses);
        /*get the SqliteDatabasehelper*/
        myDBHelper = MyDBHelper.newInstance(this);

        spinnerIncomeOrExpenses = (Spinner) findViewById(R.id.spinner_income_expenses_type_income_expenses);
        ArrayAdapter<String> spinnerIncomeAndExpensesArrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_drop_down_layout, getResources().getStringArray(R.array.income_expenses_item));
        /* set spinner text font size */
        spinnerIncomeAndExpensesArrayAdapter.setDropDownViewResource(R.layout.spinner_drop_down_layout);

        spinnerIncomeOrExpenses.setAdapter(spinnerIncomeAndExpensesArrayAdapter);

        Intent intent = getIntent();
        incomeOrExpensesId = intent.getStringExtra(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME + IncomeOrExpensesDefinition.ID);
        if (incomeOrExpensesId != null) {
            TextView textView = (TextView) findViewById(R.id.add_income_expenses_name);
            IncomeAndExpensesEntity incomeAndExpenses = myDBHelper.getIncomeAndExpenses(IncomeOrExpensesDefinition.ID + "=?", new String[]{incomeOrExpensesId}).get(0);

            textView.setText(incomeAndExpenses.getName());
            Spinner spinner = (Spinner) findViewById(R.id.spinner_income_expenses_type_income_expenses);

            int i = 0;
            for (i = 0; i <= spinnerIncomeAndExpensesArrayAdapter.getCount(); i++) {
                if (spinnerIncomeAndExpensesArrayAdapter.getItemId(i) == Long.valueOf(incomeAndExpenses.getFlag())) {
                    break;
                }
            }
            spinner.setSelection(i);
            spinner.setClickable(false);
            spinner.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    public void AddIncomeAndExpenses(View view) {
        TextView textView = (TextView) findViewById(R.id.add_income_expenses_name);
        String incomeOrExpensesName = textView.getText().toString();
        if (incomeOrExpensesName.length() == 0) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();    //弹出一个自动消失的提示框
            return;
        }
        String incomeOrexpensesType = (String) spinnerIncomeOrExpenses.getSelectedItem();

//        System.out.println("===================" + incomeOrexpensesType + "=================");
        myLogger.debug("income or expenses type is " + incomeOrexpensesType);

        if (incomeOrExpensesId != null) {
            myDBHelper.updateIncomeAndExpenses(incomeOrExpensesId, incomeOrExpensesName);
        } else {
            myDBHelper.addIncomeAndExpenses(incomeOrExpensesName, incomeOrexpensesType.equals("收入") ? DBTablesDefinition.INCOME : DBTablesDefinition.EXPENSES);
        }
        finish();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        finish();
//        return true;
//    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
