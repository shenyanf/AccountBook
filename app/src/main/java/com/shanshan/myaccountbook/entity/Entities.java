package com.shanshan.myaccountbook.entity;

import com.shanshan.myaccountbook.MyAccountUtil;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Entities implements Serializable {

    /* An array of records items.*/
    public static List<Records> RECORDS_ID = new ArrayList<Records>();

    /* A map of records items, by ID.*/
    public static Map<String, Records> RECORDS_MAP = new HashMap<String, Records>();


    /* An array of records items.*/
    public static List<WeeklyStatistics> WEEKLY_STATISTICS_ID = new ArrayList<WeeklyStatistics>();

    /* A map of records items, by ID.*/
    public static Map<String, WeeklyStatistics> WEEKLY_STATISTICS_MAP = new HashMap<String, WeeklyStatistics>();

    public static List<MonthlyStatistics> MONTHLY_STATISTICS_ID = new ArrayList<MonthlyStatistics>();

    /* A map of records items, by ID.*/
    public static Map<String, MonthlyStatistics> MONTHLY_STATISTICS_MAP = new HashMap<String, MonthlyStatistics>();

    public static List<AnnualStatistics> ANNUAL_STATISTICS_ID = new ArrayList<AnnualStatistics>();

    /* A map of records items, by ID.*/
    public static Map<String, AnnualStatistics> ANNUAL_STATISTICS_MAP = new HashMap<String, AnnualStatistics>();


    /* An array of income and expenses items.*/
    public static List<IncomeAndExpenses> INCOME_AND_EXPENSES_ID = new ArrayList<IncomeAndExpenses>();

    /* A map of  income and expenses items, by ID.*/
    public static Map<String, IncomeAndExpenses> INCOME_AND_EXPENSES_MAP = new HashMap<String, IncomeAndExpenses>();

    /* An array of income and expenses items.*/
    public static List<Accounts> ACCOUNT_ID = new ArrayList<Accounts>();

    /* A map of  income and expenses items, by ID.*/
    public static Map<String, Accounts> ACCOUNT_MAP = new HashMap<String, Accounts>();


    private static void addRecords(Records item) {
        RECORDS_ID.add(item);
        RECORDS_MAP.put(String.valueOf(item.id), item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class Records implements Comparable, Serializable {
        public int id;
        public String accountNameId;
        public String date;
        public int incomeOrExpenses;
        public float amount;
        public String remarks;

        public Records(int id, String accountNameId, String date, int incomeOrExpenses, float amount, String remarks) {
            this.id = id;
            this.accountNameId = accountNameId;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            this.amount = amount;
            this.remarks = remarks;
        }

        public Records() {

        }


        @Override
        public String toString() {
            Entities.IncomeAndExpenses incomeOrExpensesIns = MyDBHelper.newInstance(null).
                    getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpenses.ID + "=?", new String[]{String.valueOf(incomeOrExpenses)}).get(0);
            List<Entities.Accounts> list = MyDBHelper.newInstance(null).getAccount(DBTablesDefinition.Accounts.ID + "=?", new String[]{String.valueOf(accountNameId)});
            Entities.Accounts account = null;
            if (!list.isEmpty()) {
                account = list.get(0);
            }
            return date + " " + account.name + " " + (incomeOrExpensesIns.flag == DBTablesDefinition.INCOME ? "收入/" : "支出/") + incomeOrExpensesIns.name + " " + amount;
        }

        public String detail() {
            return "accountNameId is:" + accountNameId + "date is:" + date + "incomeOrExpenses is:" + incomeOrExpenses + "amount is:" + amount + "remarks is:" + remarks;
        }

        @Override
        public int compareTo(Object another) {
            Date date1 = MyAccountUtil.stringToDate(this.date);
            Date date2 = MyAccountUtil.stringToDate(((Records) another).date);

            assert date1 != null;
            assert date2 != null;

            int res = date1.compareTo(date2);
            return res == 0 ? 0 : -res;
        }
    }

    public class WeeklyStatistics implements Comparable {
        public int id;
        public String date;
        public int incomeOrExpenses;
        public float amount;

        public WeeklyStatistics(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            this.amount = amount;
        }

        public WeeklyStatistics() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((WeeklyStatistics) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class MonthlyStatistics implements Comparable {
        public int id;
        public String date;
        public int incomeOrExpenses;
        public float amount;

        public MonthlyStatistics(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            this.amount = amount;
        }

        public MonthlyStatistics() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((MonthlyStatistics) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class AnnualStatistics implements Comparable {
        public int id;
        public String date;
        public int incomeOrExpenses;
        public float amount;

        public AnnualStatistics(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            this.amount = amount;
        }

        public AnnualStatistics() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((AnnualStatistics) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class Accounts {
        public int id;
        public String name;
        public int status;

        public Accounts(int id, String name, int status) {
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public Accounts() {

        }


        @Override
        public String toString() {
            return name;
        }

    }


    public class IncomeAndExpenses {
        public int id;
        public String name;
        public int flag;

        public IncomeAndExpenses(int id, String name, int flag) {
            this.id = id;
            this.name = name;
            this.flag = flag;
        }

        public IncomeAndExpenses() {

        }


        @Override
        public String toString() {
            return (flag == DBTablesDefinition.INCOME ? "收入/" : "支出/") + name;
        }

        public String detail() {
            return "id:" + id + "name:" + name + "flag:" + (flag == DBTablesDefinition.INCOME ? "收入" : "支出");
        }
    }
}
