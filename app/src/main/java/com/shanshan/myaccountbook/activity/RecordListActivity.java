package com.shanshan.myaccountbook.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.AbstractRecord;
import com.shanshan.myaccountbook.fragment.DayRecordListFragment;
import com.shanshan.myaccountbook.fragment.FragmentFactory;
import com.shanshan.myaccountbook.fragment.RecordsListFragment;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class RecordListActivity extends AppCompatActivity implements RecordsListFragment.OnFragmentInteractionListener {
    private FragmentManager fragmentManager = getFragmentManager();
    private RecordsListFragment details = null;
    private Logger myLogger = MyLogger.getMyLogger(RecordListActivity.class.getName());
    private MyDBHelper myDBHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDBHelper = MyDBHelper.newInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        myLogger.debug("in RecordListActivity");
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (details != null) {
            details.clearList();
        }

        details = new RecordsListFragment() {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
            }


            @Override
            public void init() {
                String currentDate = getIntent().getStringExtra("currentDate");
                if (currentDate != null) {
                    recordList = myDBHelper.getDayRecordsInSpecifiedWeek(currentDate);
                    if (!recordList.isEmpty() && recordList.size() > 0) {
                        myLogger.debug("list is " + recordList + " size " + recordList.size());
                    }
                } else {
                    Toast.makeText(getBaseContext(), "不应该啊！竟然没有获取到数据！", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public List<AbstractRecord> getNextPageRecords() {
                return null;
            }
        };

        fragmentTransaction.replace(R.id.list_fragment, details);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(String id) {

    }
}
