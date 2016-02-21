package com.shanshan.myaccountbook.database;

import android.provider.BaseColumns;

/**
 * Created by heshanshan on 2015/11/15.
 */
public final class DBTablesDefinition {
    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    public DBTablesDefinition() {
    }

    public static final int INCOME = 0;
    public static final int EXPENSES = 1;

    /* Inner class that defines the table records */
    public static abstract class RecordsDefinition implements BaseColumns {
        /*records table */
        public static final String TABLE_RECORDS_NAME = "records";
        public static final String ID = "id";
        public static final String COLUMN_RECORDS_ACCOUNT_NAME_ID = "accountNameId";
        public static final String COLUMN_RECORDS_DATE = "date";
        public static final String COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE = "incomeOrExpense";
        public static final String COLUMN_RECORDS_AMOUNT = "amount";
        public static final String COLUMN_RECORDS_REMARKS = "remarks";
        /*============================*/
    }

    /*monthly activity_statistics*/
    public static abstract class MonthlyStatisticsDefinition implements BaseColumns {
        public static final String TABLE_MONTHLY_STATISTICS_NAME = "monthlyStatistics";
        public static final String ID = "id";
        public static final String COLUMN_MONTHLY_STATISTICS_DATE = "date";
        public static final String COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE = "incomeOrExpense";
        public static final String COLUMN_MONTHLY_STATISTICS_AMOUNT = "amount";
    }

    /*weekly activity_statistics*/
    public static abstract class WeeklyStatisticsDefinition implements BaseColumns {
        public static final String TABLE_WEEKLY_STATISTICS_NAME = "weeklyStatistics";
        public static final String ID = "id";
        public static final String COLUMN_WEEKLY_STATISTICS_DATE = "date";
        public static final String COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE = "incomeOrExpense";
        public static final String COLUMN_WEEKLY_STATISTICS_AMOUNT = "amount";
    }

    /*activity_statistics for each year*/
    public static abstract class AnnualStatisticsDefinition implements BaseColumns {
        public static final String TABLE_ANNUAL_STATISTICS_NAME = "annualStatistics";
        public static final String ID = "id";
        public static final String COLUMN_ANNUAL_STATISTICS_DATE = "date";
        public static final String COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE = "incomeOrExpense";
        public static final String COLUMN_ANNUAL_STATISTICS_AMOUNT = "amount";
    }


    /* Inner class that defines the table income/expenses */
    public static abstract class IncomeOrExpensesDefinition implements BaseColumns {
        /*income or expenses table*/
        public static final String TABLE_INOREXP_NAME = "incomeOrExpenses";
        public static final String ID = "id";
        public static final String COLUMN_INOREXP_NAME = "name";
        /*0 income, 1 expenses*/
        public static final String COLUMN_INOREXP_FLAG = "flag";
        /*=========================*/
    }

    /* Inner class that defines the table account */
    public static abstract class AccountsDefinition implements BaseColumns {
        public static final String TABLE_ACCOUNT_NAME = "accounts";
        public static final String ID = "id";
        public static final String COLUMN_ACCOUNT_NAME = "name";
        /*0 unavailable, 1 available*/
        public static final String COLUMN_ACCOUNT_STATUS = "status";

        public static final int ACCOUNT_AVAILABLE = 1;
        public static final int ACCOUNT_UNAVAILABLE = 0;
    }
}