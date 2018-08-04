package com.shanshan.myaccountbook.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.activity.AddRecordActivity;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.entity.DayRecordsEntity;
import com.shanshan.myaccountbook.entity.IncomeAndExpensesEntity;

import java.util.ArrayList;

/**
 * Created by heshanshan on 2016/3/25.
 */
public class DayRecordListFragment extends RecordsListFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        registerForContextMenu(mListView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == android.R.id.list) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("记录管理");
            String[] menuItems = getResources().getStringArray(R.array.records_menu);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.records_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = "记录管理";

        DayRecordsEntity record = (DayRecordsEntity) recordList.get((int) info.id);
//        System.out.println("===++++++++++++" + record + "++++++++++++++========");


        if (menuItemName.equals("删除记录")) {
            IncomeAndExpensesEntity incomeAndExpenses = myDBHelper.getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpensesDefinition.ID + "=?", new String[]{String.valueOf(record.getIncomeOrExpenses())}).get(0);

            //如果删除一条收支为0的记录，则不修改周、月、年的记录
            if (Float.compare(record.getAmount(), 0.00f) != 0) {
                myDBHelper.updateWeeklyStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
                myDBHelper.updateMonthlyStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
                myDBHelper.updateAnnualStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
            }
            myDBHelper.deleteRecord(String.valueOf(record.getId()));

            //删除记录后，重新加载本页，暂时先用这种方法
            super.clearList();
            super.initAndLoadFirstPageData();
        }

        if (menuItemName.equals("修改记录")) {
            Intent intent = new Intent(getActivity(), AddRecordActivity.class);

            intent.putExtra(DBTablesDefinition.RecordsDefinition.TABLE_RECORDS_NAME + DBTablesDefinition.RecordsDefinition.ID, record);
            startActivity(intent);
        }

        return true;
    }

    @Override
    public ArrayList getNextPageRecords() {
        return myDBHelper.getCurrentPageDayRecords(currentPage, lineSize);
    }

    @Override
    public void init() {
        allRecorders = myDBHelper.getDayRecordsCount();
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

//        System.out.println("allRecorders =  " + allRecorders);
//        System.out.println("pageSize  =  " + pageSize);

        recordList = myDBHelper.getCurrentPageDayRecords(currentPage, lineSize);

    }

}
