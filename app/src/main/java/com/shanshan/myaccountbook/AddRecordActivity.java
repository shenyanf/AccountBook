package com.shanshan.myaccountbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.RecordsDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.Entities.IncomeAndExpensesEntity;
import com.shanshan.myaccountbook.entity.Entities.RecordsEntity;

import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class AddRecordActivity extends Activity {
    private Logger myLogger = MyLogger.getMyLogger(AddRecordActivity.class.getName());

    private Spinner spinnerAccount = null;
    private ArrayAdapter adapterAccount = null;
    private Spinner spinnerIncomeOrExpenses = null;
    private ArrayAdapter adapterIncomeOrExpenses = null;
    private MyDBHelper myDBHelper = null;
    private TextView textView = null;
    private Boolean editRecord = Boolean.FALSE;
    private float previousAmount = 0.0f;
    RecordsEntity previousRecord = null;

    DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Date getCurrDate() {
        return new Date(System.currentTimeMillis());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*not display title*/
        setContentView(R.layout.activity_add_record);
        /*get the SqliteDatabasehelper*/
        myDBHelper = MyDBHelper.newInstance(this);

        Intent intent = getIntent();
        previousRecord = (RecordsEntity) intent.getSerializableExtra(RecordsDefinition.TABLE_RECORDS_NAME + RecordsDefinition.ID);
        /*check account and incomeOrexpenses exist or not*/
        if (myDBHelper.getAccount().isEmpty() || myDBHelper.getIncomeAndExpenses().isEmpty()) {
            Toast.makeText(this, "请先添加账户和收支项！", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(this, SettingActivity.class);
            startActivity(intent1);
        }


        /*set date, default today*/
        textView = (TextView) findViewById(R.id.add_record_date);

        if (previousRecord != null) {
            editRecord = Boolean.TRUE;
            textView.setText(previousRecord.date);
            textView.setInputType(InputType.TYPE_NULL);
        } else {
            textView.setText(format1.format(getCurrDate()));

            textView.setInputType(InputType.TYPE_NULL);
//        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    Calendar c = Calendar.getInstance();
//                    new DatePickerDialog(AddRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
//
//                        @Override
//                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                            textView.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
//                        }
//                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
//
//                }
//            }
//        });

           /* textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final Calendar c = Calendar.getInstance();
                    new DatePickerDialog(AddRecordActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            textView.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth + " " + c.get(Calendar.HOUR_OF_DAY) +
                                    ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
                        }
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

                }
            });*/


            final View dialogView = View.inflate(AddRecordActivity.this, R.layout.date_time_picker, null);
            final AlertDialog alertDialog = new AlertDialog.Builder(AddRecordActivity.this).create();
            final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
            final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
            timePicker.setIs24HourView(true);

            dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                            datePicker.getMonth(),
                            datePicker.getDayOfMonth(),
                            timePicker.getCurrentHour(),
                            timePicker.getCurrentMinute());

                    textView.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY) +
                            ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND));

                    alertDialog.dismiss();
                }
            });
            textView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    alertDialog.setView(dialogView);
                    alertDialog.show();
                }
            });
        }

        /*get account from table*/
        spinnerAccount = (Spinner) findViewById(R.id.spinner_account);
        adapterAccount = new ArrayAdapter(this, R.layout.spinner_drop_down_layout, myDBHelper.getAccount());
        /* set spinner text font size */
        adapterAccount.setDropDownViewResource(R.layout.spinner_drop_down_layout);

        spinnerAccount.setAdapter(adapterAccount);

        if (previousRecord != null) {
            int i = 0;
            for (i = 0; i <= adapterAccount.getCount(); i++) {
                if (adapterAccount.getItemId(i) == Long.valueOf(previousRecord.accountNameId)) {
                    break;
                }
            }
            spinnerAccount.setSelection(i == 0 ? 0 : i - 1);
            spinnerAccount.setClickable(false);
            spinnerAccount.setBackgroundColor(Color.TRANSPARENT);

        } else {
            spinnerAccount.setVisibility(View.VISIBLE);

            spinnerAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
                    spinnerAccount.setSelection(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        /*get income or expenses from table*/
        spinnerIncomeOrExpenses = (Spinner) findViewById(R.id.spinner_income_expenses_type);

        adapterIncomeOrExpenses = new ArrayAdapter(this, R.layout.spinner_drop_down_layout, myDBHelper.getIncomeAndExpenses());

        /* set spinner text font size */
        adapterIncomeOrExpenses.setDropDownViewResource(R.layout.spinner_drop_down_layout);

        spinnerIncomeOrExpenses.setAdapter(adapterIncomeOrExpenses);
        if (previousRecord != null) {
            int i = 0;
            for (i = 0; i <= adapterIncomeOrExpenses.getCount(); i++) {
                if (adapterIncomeOrExpenses.getItemId(i) == Long.valueOf(previousRecord.incomeOrExpenses)) {
                    break;
                }
            }
            spinnerIncomeOrExpenses.setSelection(i == 0 ? 0 : i - 1);
            spinnerIncomeOrExpenses.setClickable(false);
            spinnerIncomeOrExpenses.setBackgroundColor(Color.TRANSPARENT);

        } else {

            spinnerIncomeOrExpenses.setVisibility(View.VISIBLE);
            spinnerIncomeOrExpenses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        if (previousRecord != null) {
            previousAmount = previousRecord.amount;

            TextView textView = (TextView) findViewById(R.id.add_record_amount);
            textView.setText(String.valueOf(previousRecord.amount));

            TextView remarkView = (TextView) findViewById(R.id.add_record_remarks);
            remarkView.setText(previousRecord.remarks);
        }
    }

    public void addRecord(View view) {
        String dateStr = MyAccountUtil.dateToString(MyAccountUtil.stringToDate(textView.getText().toString()));

        String accountId = String.valueOf(spinnerAccount.getSelectedItemId() + 1);
        String incomeOrexpensesId = String.valueOf(spinnerIncomeOrExpenses.getSelectedItemId() + 1);
        TextView textView = (TextView) findViewById(R.id.add_record_amount);
        String txt = textView.getText().toString();
        if (txt.length() == 0) {
            Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show();    //弹出一个自动消失的提示框
            return;
        }
        float value = Float.valueOf(txt.toString());
        BigDecimal bigDecimal = new BigDecimal(value);

        float amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        TextView remarkView = (TextView) findViewById(R.id.add_record_remarks);
        String remarks = remarkView.getText().toString();

        IncomeAndExpensesEntity incomeOrExpenses = null;

        List<IncomeAndExpensesEntity> list = myDBHelper.getIncomeAndExpenses(IncomeOrExpensesDefinition.ID + "=?", new String[]{incomeOrexpensesId});
        if (list != null && !list.isEmpty()) {
            incomeOrExpenses = list.get(0);
        }
        if (editRecord) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(RecordsDefinition.COLUMN_RECORDS_AMOUNT, amount);
            contentValues.put(RecordsDefinition.COLUMN_RECORDS_REMARKS, remarks);
            myDBHelper.updateRecord(contentValues, String.valueOf(previousRecord.id));

        } else {
            myDBHelper.addRecord(accountId, dateStr, Integer.valueOf(incomeOrexpensesId), amount, remarks);
        }

        System.out.println("incomeOrExpenses is " + incomeOrExpenses);
        myLogger.debug("incomeOrExpenses is " + incomeOrExpenses);

        System.out.println("date is" + dateStr);
        System.out.println(" accountId is " + accountId);
        System.out.println(" incomeOrexpenses is " + incomeOrExpenses);
        System.out.println(" amount is " + amount);
        myLogger.debug("date is" + dateStr + " accountId is " + accountId +
                " incomeOrexpenses is " + incomeOrExpenses + " amount is " + amount);

        if (editRecord) {
            amount = amount - previousAmount;
        }
        myDBHelper.updateWeeklyStatistics(dateStr, amount, incomeOrExpenses.flag);
        myDBHelper.updateMonthlyStatistics(dateStr, amount, incomeOrExpenses.flag);
        myDBHelper.updateAnnualStatistics(dateStr, amount, incomeOrExpenses.flag);

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
