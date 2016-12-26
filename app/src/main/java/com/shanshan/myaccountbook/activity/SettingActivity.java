package com.shanshan.myaccountbook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.shanshan.myaccountbook.R;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TextView manageAccount = (TextView) findViewById(R.id.manage_account);
        final Intent manageAccountIntent = new Intent(this, ManageAccountActivity.class);
        manageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(manageAccountIntent);
            }
        });


        TextView manageIncomeAndExpenses = (TextView) findViewById(R.id.manage_income_and_expenses);
        final Intent manageIncomeAndExpensesIntent = new Intent(this, ManageIncomeAndExpensesActivity.class);
        manageIncomeAndExpenses.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(manageIncomeAndExpensesIntent);
            }
        });

        TextView abountAuthor = (TextView) findViewById(R.id.about_author);
        final Intent aboutAuthorIntent = new Intent(this, AboutAuthorActivity.class);
        abountAuthor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(aboutAuthorIntent);
            }
        });
        this.callStatistic();

    }

    private void callStatistic() {
        TextView statistics = (TextView) findViewById(R.id.statistics);
        final Intent statisticsIntent = new Intent(this, PieChartActivity.class);
        statistics.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(statisticsIntent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
