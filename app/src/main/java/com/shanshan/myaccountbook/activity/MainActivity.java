package com.shanshan.myaccountbook.activity;

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

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.AbstractRecord;
import com.shanshan.myaccountbook.fragment.EnumFragment;
import com.shanshan.myaccountbook.fragment.FragmentFactory;
import com.shanshan.myaccountbook.fragment.RecordsListFragment;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecordsListFragment.OnFragmentInteractionListener {
    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    private FragmentFactory fragmentFactory = null;
    private Logger myLogger = MyLogger.getMyLogger(MainActivity.class.getName());

    private RecordsListFragment details = null;
    private MyDBHelper myDBHelper = null;
    private FragmentManager fragmentManager = getFragmentManager();
    private volatile int tabPosition;

    public List getRecordListfromTabPosition() {
        List list = null;
        switch (tabPosition) {
            case 1:
                list = myDBHelper.getWeeklyStatistics(null, null);
                break;
            case 2:
                list = myDBHelper.getMonthlyStatistics(null, null);
                break;
            case 3:
                list = myDBHelper.getAnnualStatistics(null, null);
                break;
            default:
                list = myDBHelper.getRecords();
        }

        //it's no need to sort records
//        Collections.sort(list);

        return list;
    }

    private EnumFragment tabPosition2Enum(int tabPosition) {
        switch (tabPosition) {
            case 1:
                return EnumFragment.WeeklyRecordListFragment;
            case 2:
                return EnumFragment.MonthlyRecordListFragment;
            case 3:
                return EnumFragment.AnnualRecordListFragment;
            default:
                return EnumFragment.DayRecordListFragment;

        }
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
        fragmentFactory = new FragmentFactory();

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabPosition = tab.getPosition();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (details != null) {
                    details.clearList();
                }

                details = fragmentFactory.createRecordListFragment(tabPosition2Enum(tabPosition));

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
            myDBHelper.initDB();

            Toast.makeText(this, "首次运行！", Toast.LENGTH_SHORT).show();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        } else {
            Toast.makeText(this, "Have a good day！Mr. Shen", Toast.LENGTH_SHORT).show();

//            myDBHelper.getWritableDatabase().execSQL("delete  from " + DBTablesDefinition.WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME);
//            myDBHelper.getWritableDatabase().execSQL("delete  from " + DBTablesDefinition.MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME);
//            myDBHelper.getWritableDatabase().execSQL("delete  from " + DBTablesDefinition.AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME);

        }
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
        List<AbstractRecord> list = getRecordListfromTabPosition();
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

        details = fragmentFactory.createRecordListFragment(tabPosition2Enum(tabPosition));

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
