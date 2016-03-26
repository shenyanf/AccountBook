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
                recordsListFragment.setdate("day");
                break;
            case WeeklyRecordListFragment:
                recordsListFragment = new WeeklyRecordListFragment();
                recordsListFragment.setdate("week");
                break;
            case MonthlyRecordListFragment:
                recordsListFragment = new MonthlyRecordListFragment();
                recordsListFragment.setdate("month");
                break;
            case AnnualRecordListFragment:
                recordsListFragment = new AnnualRecordListFragment();
                recordsListFragment.setdate("annual");
                break;
        }
        return recordsListFragment;
    }
}
