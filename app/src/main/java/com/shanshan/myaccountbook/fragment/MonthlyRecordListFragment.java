package com.shanshan.myaccountbook.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.entity.MonthlyStatisticsEntity;

/**
 * Created by heshanshan on 2016/3/25.
 */
public class MonthlyRecordListFragment extends RecordsListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = null;
//            System.out.println("create fragment view date is " + date);
        view = inflater.inflate(R.layout.fragment_item, container, false);
        list = myDBHelper.getMonthlyStatistics(null, null);


        mAdapter = new ArrayAdapter<MonthlyStatisticsEntity>(getActivity(),
                R.layout.list_item_layout, android.R.id.text1, list);
        /* set spinner text font size */
//        mAdapter.setDropDownViewResource(R.layout.list_item_layout);

//        System.out.println(mAdapter);
        // Set the adapter
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            }
        });
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

}
