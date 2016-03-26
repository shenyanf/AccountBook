package com.shanshan.myaccountbook.fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.activity.AddRecordActivity;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.entity.DayRecordsEntity;
import com.shanshan.myaccountbook.entity.IncomeAndExpensesEntity;

import java.util.ArrayList;

/**
 * Created by heshanshan on 2016/3/25.
 */
public class DayRecordListFragment extends RecordsListFragment implements AbsListView.OnScrollListener {
    private int currentPage = 1; //默认在第一页
    private static final int lineSize = 10;    //每次显示数
    private int allRecorders = 0;  //全部记录数
    private int pageSize = 1;  //默认共一页
    private int lastItem;
    private LinearLayout loadLayout;
    private TextView loadInfo;
    private int alreadyLoadCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = null;
//            System.out.println("create fragment view date is " + date);
        view = inflater.inflate(R.layout.fragment_item, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);

        //创建一个角标线性布局用来显示"正在加载"
        loadLayout = new LinearLayout(getActivity());
        loadLayout.setGravity(Gravity.CENTER);
        //定义一个文本显示“正在加载”
        loadInfo = new TextView(getActivity());
        loadInfo.setText("正在加载...");
        loadInfo.setGravity(Gravity.CENTER);
        //增加组件
        loadLayout.addView(loadInfo, new ActionBar.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        //增加到listView底部
        mListView.addFooterView(loadLayout);


        showAllData();
        mListView.setOnScrollListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            }
        });

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


        registerForContextMenu(mListView);

        return view;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.records_menu);
        String menuItemName = menuItems[menuItemIndex];
        String listItemName = "记录管理";

        DayRecordsEntity record = recordList.get((int) info.id);
//        System.out.println("===++++++++++++" + record + "++++++++++++++========");


        if (menuItemName.equals("删除记录")) {
            System.out.println("删除记录" + record);
            IncomeAndExpensesEntity incomeAndExpenses = myDBHelper.getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpensesDefinition.ID + "=?", new String[]{String.valueOf(record.getIncomeOrExpenses())}).get(0);

            myDBHelper.updateWeeklyStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
            myDBHelper.updateMonthlyStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
            myDBHelper.updateAnnualStatistics(record.getDate(), -record.getAmount(), incomeAndExpenses.getFlag());
            myDBHelper.deleteRecord(String.valueOf(record.getId()));

            onResume();
        }
        if (menuItemName.equals("修改记录")) {
            Intent intent = new Intent(getActivity(), AddRecordActivity.class);

            intent.putExtra(DBTablesDefinition.RecordsDefinition.TABLE_RECORDS_NAME + DBTablesDefinition.RecordsDefinition.ID, record);
            startActivity(intent);
        }

        return true;
    }

    /**
     * 读取全部数据
     */
    public void showAllData() {
        allRecorders = myDBHelper.getDayRecordsCount();
        //计算总页数
        pageSize = (allRecorders + lineSize - 1) / lineSize;

//        System.out.println("allRecorders =  " + allRecorders);
//        System.out.println("pageSize  =  " + pageSize);

        recordList = myDBHelper.getDayRecordsAllItems(currentPage, lineSize);

        alreadyLoadCount = recordList.size();

        mAdapter = new ArrayAdapter(getActivity(),
                R.layout.list_item_layout, android.R.id.text1, recordList);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisible, int visibleCount,
                         int totalCount) {
        lastItem = firstVisible + visibleCount - 1; //统计是否到最后

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scorllState) {
//        System.out.println("进入滚动界面了");

//        System.out.println("lastItem" + lastItem + " alreadyLoadCount" + alreadyLoadCount);
//        System.out.println("currentPage" + currentPage + "pageSize" + pageSize);
//        System.out.println("scorllState" + scorllState + " " + OnScrollListener.SCROLL_STATE_IDLE);

        //是否到最底部并且数据没读完
        if (lastItem == alreadyLoadCount
                && currentPage < pageSize    //不再滚动
                && scorllState == OnScrollListener.SCROLL_STATE_IDLE) {
            currentPage++;
            //设置显示位置
            mListView.setSelection(lastItem);
            //增加数据
            appendDate();
        }

    }

    /**
     * 增加数据
     */
    private void appendDate() {
        ArrayList<DayRecordsEntity> additems = myDBHelper.getDayRecordsAllItems(currentPage, lineSize);
        alreadyLoadCount = mAdapter.getCount() + additems.size();

        //判断，如果到了最末尾则去掉“正在加载”
        if (allRecorders == alreadyLoadCount) {
            mListView.removeFooterView(loadLayout);
        }

//        System.out.println("追加数据...");

        recordList.addAll(additems);
        //通知记录改变
        mAdapter.notifyDataSetChanged();
    }

}
