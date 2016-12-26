package com.shanshan.myaccountbook.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.shanshan.myaccountbook.activity.RecordListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heshanshan on 2016/3/25.
 */
public class WeeklyRecordListFragment extends RecordsListFragment {


    public void init() {
        allRecorders = myDBHelper.getWeeklyRecordsCount();
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

//        System.out.println("allRecorders =  " + allRecorders);
//        System.out.println("pageSize  =  " + pageSize);

        recordList = myDBHelper.getCurrentPageWeeklyRecords(currentPage, lineSize);

    }


    @Override
    public List getNextPageRecords() {
        return myDBHelper.getCurrentPageWeeklyRecords(currentPage, lineSize);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Intent recordList = new Intent(this.getActivity(), RecordListActivity.class);
        String date = myDBHelper.getWeeklyStatistics(null, null).get(position).getDate();

        myLogger.debug("WeeklyRecordList date is " + date);
        recordList.putExtra("currentDate", date);
        startActivity(recordList);
    }
}
