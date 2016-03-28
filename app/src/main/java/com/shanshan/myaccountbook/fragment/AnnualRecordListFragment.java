package com.shanshan.myaccountbook.fragment;

import java.util.List;

/**
 * Created by heshanshan on 2016/3/25.
 */
public class AnnualRecordListFragment extends RecordsListFragment {

    public void init() {
        allRecorders = myDBHelper.getAnnualRecordsCount();
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

//        System.out.println("allRecorders =  " + allRecorders);
//        System.out.println("pageSize  =  " + pageSize);

        recordList = myDBHelper.getCurrentPageAnnualRecords(currentPage, lineSize);

    }


    @Override
    public List getNextPageRecords() {
        return myDBHelper.getCurrentPageAnnualRecords(currentPage, lineSize);
    }
}
