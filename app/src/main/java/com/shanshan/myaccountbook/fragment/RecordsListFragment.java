package com.shanshan.myaccountbook.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.shanshan.myaccountbook.R;
import com.shanshan.myaccountbook.database.MyDBHelper;
import com.shanshan.myaccountbook.entity.AbstractRecord;

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
public abstract class RecordsListFragment extends Fragment implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener {

    protected List recordList = null;
    protected MyDBHelper myDBHelper;

    protected OnFragmentInteractionListener mListener = null;

    /**
     * The fragment's ListView/GridView.
     */
    protected ListView mListView = null;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    protected ArrayAdapter mAdapter = null;

    protected int currentPage = 1; //默认在第一页
    protected static final int lineSize = 10;    //每次显示数
    protected int allRecorders = 0;  //全部记录数
    protected int pageSize = 1;  //默认共一页
    protected int lastItem; //最后一行的行号
    protected int alreadyLoadCount = 0; //已经加载了多少记录

    protected LinearLayout loadLayout;
    protected TextView loadInfo;

    /**
     * 初始化变量
     */
    public abstract void init();

    /**
     * 获得下一页的数据
     *
     * @return
     */
    public abstract List<AbstractRecord> getNextPageRecords();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecordsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        System.out.println("create fragment date is " + date);

    }

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


        initAndLoadFirstPageData();
        mListView.setOnScrollListener(this);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(20, TypedValue.COMPLEX_UNIT_SP);
            }
        });
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
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

    /**
     * 清空数据
     */
    public void clearList() {
        if (recordList != null) {
            int size = recordList.size();
            if (size > 0) {
                recordList.removeAll(recordList);
                ((ArrayAdapter) mAdapter).notifyDataSetChanged();
            }
        }
    }


    /**
     * 追加数据
     */
    public void appendDate() {
        List<AbstractRecord> additems = getNextPageRecords();

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

    /**
     * 判断是否到最后
     *
     * @param view
     * @param firstVisible
     * @param visibleCount
     * @param totalCount
     */
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
                && scorllState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            currentPage++;
            //设置显示位置
            mListView.setSelection(lastItem);
            //增加数据
            appendDate();
        }

    }

    /**
     * 初始化变量，并读取第一页显示的数据
     */
    public void initAndLoadFirstPageData() {
        init();
        alreadyLoadCount = recordList.size();

        //判断，如果到了最末尾则去掉“正在加载”
        if (allRecorders <= lineSize) {
            mListView.removeFooterView(loadLayout);
        }

        mAdapter = new ArrayAdapter(getActivity(),
                R.layout.list_item_layout, android.R.id.text1, recordList);
        mListView.setAdapter(mAdapter);
    }

}
