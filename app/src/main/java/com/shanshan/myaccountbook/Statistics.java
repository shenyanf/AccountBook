package com.shanshan.myaccountbook;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shanshan.myaccountbook.database.MyDBHelper;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class Statistics extends AppCompatActivity {
    private Logger myLogger = MyLogger.getMyLogger(Statistics.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);


        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.income_chart, new PlaceholderFragment()).commit();
        }


        myLogger.debug("display income chart...");

    }

    public static class PlaceholderFragment extends Fragment {
        private PieChartView incomePieChart;
        private PieChartData incomePiedata;

        private boolean hasLabels = false;
        private boolean hasLabelsOutside = false;
        private boolean hasCenterCircle = false;
        private boolean hasCenterText1 = false;
        private boolean hasCenterText2 = false;
        private boolean isExploded = false;
        private boolean hasLabelForSelected = false;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            List<SliceValue> list = new ArrayList<SliceValue>();


//            incomePieChart = (PieChartView) findViewById(R.id.income_chart);
            incomePieChart.setOnValueTouchListener(new ValueTouchListener());

            MyDBHelper myDBHelper = MyDBHelper.newInstance(null);
            SQLiteDatabase db = myDBHelper.getWritableDatabase();
        /*Cursor cursor = db.rawQuery("Select " + DBTablesDefinition.Records.TABLE_RECORDS_NAME + "." + DBTablesDefinition.Records.ID + "," +
                DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + "." + DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME + "," + DBTablesDefinition.Records.TABLE_RECORDS_NAME + "." + DBTablesDefinition.Records.COLUMN_RECORDS_DATE
                + "," + DBTablesDefinition.Records.TABLE_RECORDS_NAME + "." + DBTablesDefinition.Records.COLUMN_RECORDS_AMOUNT + " from " + DBTablesDefinition.Records.TABLE_RECORDS_NAME + " left join " +
                DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + " on " + DBTablesDefinition.Records.TABLE_RECORDS_NAME + "." + DBTablesDefinition.Records.COLUMN_RECORDS_ACCOUNT_NAME_ID
                + " = " + DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + "." + DBTablesDefinition.Accounts.ID + " where " + DBTablesDefinition.Records.TABLE_RECORDS_NAME + "." + DBTablesDefinition.Records.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE
                + " = " + DBTablesDefinition.INCOME, null);
*/

            Cursor cursor = db.rawQuery("select records.id,accounts.name,records.date,SUM(records.amount),incomeOrExpenses.name from records,accounts,incomeOrExpenses where records.accountNameId=accounts.id and records.incomeOrExpense=incomeOrExpenses.id and incomeOrExpenses.flag=0 group by accounts.name", null);
            System.out.println("Cursor count is " + cursor.getCount());

            while (cursor.moveToNext()) {
                System.out.println(cursor.getInt(0) + " " + cursor.getString(1) + " " + cursor.getString(2) + " " + cursor.getDouble(3) + " " + cursor.getString(4));
                SliceValue sliceValue = new SliceValue((float) cursor.getDouble(3), ChartUtils.pickColor());
                list.add(sliceValue);
            }

            incomePiedata = new PieChartData(list);
            incomePiedata.setHasLabels(hasLabels);
            incomePiedata.setHasLabelsOnlyForSelected(hasLabelForSelected);
            incomePiedata.setHasLabelsOutside(hasLabelsOutside);
            incomePiedata.setHasCenterCircle(hasCenterCircle);


            incomePieChart.setPieChartData(incomePiedata);

            return null;
        }

        private class ValueTouchListener implements PieChartOnValueSelectListener {

            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
//                Toast.makeText(getApplicationContext(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
