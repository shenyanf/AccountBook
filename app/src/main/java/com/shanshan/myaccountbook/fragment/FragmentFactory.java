package com.shanshan.myaccountbook.fragment;


/**
 * Created by heshanshan on 2016/3/26.
 */
public class FragmentFactory {
    public RecordsListFragment createRecordListFragment(EnumFragment fragmentEnum) {
        RecordsListFragment recordsListFragment = null;
        switch (fragmentEnum) {
            case DayRecordListFragment:
                recordsListFragment = new DayRecordListFragment();
                break;
            case WeeklyRecordListFragment:
                recordsListFragment = new WeeklyRecordListFragment();
                break;
            case MonthlyRecordListFragment:
                recordsListFragment = new MonthlyRecordListFragment();
                break;
            case AnnualRecordListFragment:
                recordsListFragment = new AnnualRecordListFragment();
                break;
        }
        return recordsListFragment;
    }
}
