package com.shanshan.myaccountbook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.RecordsDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.Entities;
import com.shanshan.myaccountbook.entity.Entities.AnnualStatisticsEntity;
import com.shanshan.myaccountbook.entity.Entities.MonthlyStatisticsEntity;
import com.shanshan.myaccountbook.entity.Entities.RecordsEntity;
import com.shanshan.myaccountbook.entity.Entities.WeeklyStatisticsEntity;

import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RecordsListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List list = null;
    private List<RecordsEntity> recordList = null;
    private MyDBHelper myDBHelper;

    private OnFragmentInteractionListener mListener = null;
    private String date = null;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView = null;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayAdapter mAdapter = null;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordsListFragment() {
    }

    public RecordsListFragment getFragmentByIndex(int tabIndex) {
        RecordsListFragment recordsListFragment = null;
        switch (tabIndex) {
            case 1:
                recordsListFragment = new RecordsListFragment();
                recordsListFragment.setdate("week");
                break;
            case 2:
                recordsListFragment = new RecordsListFragment();
                recordsListFragment.setdate("month");
                break;
            case 3:
                recordsListFragment = new RecordsListFragment();
                recordsListFragment.setdate("annual");
                break;
            default:
                recordsListFragment = new RecordsListFragment();
                recordsListFragment.setdate("day");
        }
        return recordsListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        System.out.println("create fragment date is " + date);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        date = (date == null ? "day" : date);

        View view = null;
        if (date.equals("day")) {
//            System.out.println("create fragment view date is " + date);
            view = inflater.inflate(R.layout.fragment_item, container, false);
            recordList = myDBHelper.getRecords();

            Collections.sort(recordList);

            mAdapter = new ArrayAdapter<RecordsEntity>(getActivity(),
                    R.layout.list_item_layout, android.R.id.text1, recordList);
        }
        if (date.equals("week")) {
//            System.out.println("create fragment view date is " + date);
            view = inflater.inflate(R.layout.fragment_item, container, false);
            list = myDBHelper.getWeeklyStatistics(null, null);

            Collections.sort(list);

            mAdapter = new ArrayAdapter<WeeklyStatisticsEntity>(getActivity(),
                    R.layout.list_item_layout, android.R.id.text1, list);
        }
        if (date.equals("month")) {
//            System.out.println("create fragment view date is " + date);
            view = inflater.inflate(R.layout.fragment_item, container, false);
            list = myDBHelper.getMonthlyStatistics(null, null);

            Collections.sort(list);

            mAdapter = new ArrayAdapter<MonthlyStatisticsEntity>(getActivity(),
                    R.layout.list_item_layout, android.R.id.text1, list);
        }
        if (date.equals("annual")) {
//            System.out.println("create fragment view date is " + date);
            view = inflater.inflate(R.layout.fragment_item, container, false);
            list = myDBHelper.getAnnualStatistics(null, null);

            Collections.sort(list);

            mAdapter = new ArrayAdapter<AnnualStatisticsEntity>(getActivity(),
                    R.layout.list_item_layout, android.R.id.text1, list);
        }
        /* set spinner text font size */
//        mAdapter.setDropDownViewResource(R.layout.list_item_layout);

        System.out.println(mAdapter);
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

        if (date.equals("day")) {
            registerForContextMenu(mListView);
        }
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            myDBHelper = MyDBHelper.newInstance(this.getActivity());
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mAdapter = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(String.valueOf((position)));
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }


    public ListAdapter getmAdapter() {
        return mAdapter;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void clearList() {
        if (recordList != null) {
            int size = recordList.size();
            if (size > 0) {
                recordList.removeAll(recordList);
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
            }
        }
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

        RecordsEntity record = recordList.get((int) info.id);
        System.out.println("===++++++++++++" + record + "++++++++++++++========");


        if (date.equals("day")) {
            if (menuItemName.equals("删除记录")) {
                System.out.println("删除记录" + record);
                Entities.IncomeAndExpensesEntity incomeAndExpenses = myDBHelper.getIncomeAndExpenses(IncomeOrExpensesDefinition.ID + "=?", new String[]{String.valueOf(record.incomeOrExpenses)}).get(0);

                myDBHelper.updateWeeklyStatistics(record.date, -record.amount, incomeAndExpenses.flag);
                myDBHelper.updateMonthlyStatistics(record.date, -record.amount, incomeAndExpenses.flag);
                myDBHelper.updateAnnualStatistics(record.date, -record.amount, incomeAndExpenses.flag);
                myDBHelper.deleteRecord(String.valueOf(record.id));

                onResume();
            }
            if (menuItemName.equals("修改记录")) {
                Intent intent = new Intent(getActivity(), AddRecordActivity.class);

                intent.putExtra(RecordsDefinition.TABLE_RECORDS_NAME + RecordsDefinition.ID, record);
                startActivity(intent);
            }
        }

        return true;
    }
}
