package com.shanshan.myaccountbook.fragment;

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
}
