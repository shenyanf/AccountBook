package com.shanshan.myaccountbook;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.Entities;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecordsListFragment.OnFragmentInteractionListener {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";

    private Logger myLogger = MyLogger.getMyLogger(MainActivity.class.getName());
    private RecordsListFragment details = null;
    private MyDBHelper myDBHelper = null;
    private FragmentManager fragmentManager = getFragmentManager();
    private volatile int tabPosition;

    public List getRecordListfromTabPosition() {
        List list = null;
        String date = null;
        switch (tabPosition) {
            case 1:
                date = "week";
                list = myDBHelper.getWeeklyStatistics(null, null);
                break;
            case 2:
                date = "month";
                list = myDBHelper.getMonthlyStatistics(null, null);
                break;
            case 3:
                date = "annual";
                list = myDBHelper.getAnnualStatistics(null, null);
                break;
            default:
                date = "day";
                list = myDBHelper.getRecords();
        }
        Collections.sort(list, new Comparator<Entities.AbstractRecord>() {
            @Override
            public int compare(Entities.AbstractRecord lhs, Entities.AbstractRecord rhs) {
                return -lhs.date.compareTo(rhs.date);
            }
        });
        return list;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablyout);
        tabLayout.addTab(tabLayout.newTab().setText("日"));
        tabLayout.addTab(tabLayout.newTab().setText("周"));
        tabLayout.addTab(tabLayout.newTab().setText("月"));
        tabLayout.addTab(tabLayout.newTab().setText("年"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (details != null) {
                    details.clearList();
                }

                details = new RecordsListFragment().getFragmentByIndex(tabPosition);

                fragmentTransaction.replace(R.id.list_fragment, details);
                fragmentTransaction.commit();

                System.out.println("change tab " + tab.getPosition());
                myLogger.debug("change tab " + tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        myDBHelper = MyDBHelper.newInstance(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences("appInfo", MODE_PRIVATE);
        boolean firstRunFlag = sharedPreferences.getBoolean("firstRun", true);

        if (firstRunFlag) {
            initDB();

            Toast.makeText(this, "首次运行！", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        } else {
            Toast.makeText(this, "Have a good day！Mr. Shen", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDB() {
        myDBHelper.addAccount("现金账户");
        myDBHelper.addAccount("招行储蓄卡");
        myDBHelper.addAccount("招行信用卡");
        myDBHelper.addAccount("北京银行储蓄卡");
        myDBHelper.addAccount("工行储蓄卡");
        myDBHelper.addAccount("支付宝");
        myDBHelper.addAccount("QQ红包");
        myDBHelper.addAccount("百度钱包");
        myDBHelper.addAccount("理财账户");
        myDBHelper.addAccount("微信");

        myDBHelper.addIncomeAndExpenses("吃饭", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("外卖", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("超市", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("购物", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("房租", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("水电煤、公交、话费等", DBTablesDefinition.EXPENSES);
        myDBHelper.addIncomeAndExpenses("杂项", DBTablesDefinition.EXPENSES);

        myDBHelper.addIncomeAndExpenses("工资", DBTablesDefinition.INCOME);
        myDBHelper.addIncomeAndExpenses("利息", DBTablesDefinition.INCOME);
        myDBHelper.addIncomeAndExpenses("理财收益", DBTablesDefinition.INCOME);
        myLogger.debug("init database...");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        System.out.println("on create menu ....");
        myLogger.debug("on create menu ....");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*public void sendMessage(View view) {
        Intent intent = new Intent(this, DetailActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        System.out.println("++++++++++++++++++++++++" + message + "++++++++++++++");
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("USERPASS", PreferenceActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", "shenyanfang");
        editor.putString("password", "guess");
        editor.commit();
    }

    @Override
    public void onFragmentInteraction(String id) {
        List<Entities.AbstractRecord> list = getRecordListfromTabPosition();
        Toast.makeText(getApplicationContext(),
                list.get(Integer.valueOf(id)).detail(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("USERPASS", PreferenceActivity.MODE_PRIVATE);
//        System.out.println("name is " + sharedPreferences.getString("username", "whoami"));
//        System.out.println("password is " + sharedPreferences.getString("password", "password"));

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (details != null) {
            details.clearList();
        }

        details = new RecordsListFragment().getFragmentByIndex(tabPosition);

        fragmentTransaction.replace(R.id.list_fragment, details);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Main activity starting...");
        myLogger.debug("Main activity starting...");
    }

    public void onClickAdd(View view) {

        System.out.print("init account and income or expenses tables");
        myLogger.debug("init account and income or expenses tables");
        Intent intent;
        intent = new Intent(this, AddRecordActivity.class);
        startActivity(intent);

//        ((ArrayAdapter) details.getmAdapter()).notifyDataSetChanged();
    }

}
