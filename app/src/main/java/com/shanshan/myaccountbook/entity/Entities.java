package com.shanshan.myaccountbook.entity;

import com.shanshan.myaccountbook.MyAccountUtil;
import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class Entities implements Serializable {

    public abstract class AbstractRecord implements Serializable {
        public String date;

        public abstract String toString();

        public abstract String detail();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class RecordsEntity extends AbstractRecord implements Comparable, Serializable {
        public int id;
        public String accountNameId;
        public int incomeOrExpenses;
        public float amount;
        public String remarks;

        public RecordsEntity(int id, String accountNameId, String date, int incomeOrExpenses, float amount, String remarks) {
            this.id = id;
            this.accountNameId = accountNameId;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;

            BigDecimal bigDecimal = new BigDecimal(amount);
            this.amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

            this.remarks = remarks;
        }

        public RecordsEntity() {

        }


        @Override
        public String toString() {
            IncomeAndExpensesEntity incomeOrExpensesIns = MyDBHelper.newInstance(null).
                    getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpensesDefinition.ID + "=?", new String[]{String.valueOf(incomeOrExpenses)}).get(0);
            List<AccountsEntity> list = MyDBHelper.newInstance(null).getAccount(DBTablesDefinition.AccountsDefinition.ID + "=?", new String[]{String.valueOf(accountNameId)});
            AccountsEntity account = null;
            if (!list.isEmpty()) {
                account = list.get(0);
            }
            return date + " " + account.name + " " + (incomeOrExpensesIns.flag == DBTablesDefinition.INCOME ? "收入/" : "支出/") + incomeOrExpensesIns.name + " " + amount;
        }

        public String detail() {
            return toString() + " " + remarks;
        }

        @Override
        public int compareTo(Object another) {
            Date date1 = MyAccountUtil.stringToDate(this.date);
            Date date2 = MyAccountUtil.stringToDate(((RecordsEntity) another).date);

            assert date1 != null;
            assert date2 != null;

            int res = date1.compareTo(date2);
            return res == 0 ? 0 : -res;
        }
    }

    public class WeeklyStatisticsEntity extends AbstractRecord implements Comparable {
        public int id;
        public int incomeOrExpenses;
        public float amount;

        public WeeklyStatisticsEntity(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            BigDecimal bigDecimal = new BigDecimal(amount);
            this.amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }

        public WeeklyStatisticsEntity() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((WeeklyStatisticsEntity) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class MonthlyStatisticsEntity extends AbstractRecord implements Comparable {
        public int id;
        public int incomeOrExpenses;
        public float amount;

        public MonthlyStatisticsEntity(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            BigDecimal bigDecimal = new BigDecimal(amount);
            this.amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }

        public MonthlyStatisticsEntity() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((MonthlyStatisticsEntity) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class AnnualStatisticsEntity extends AbstractRecord implements Comparable {
        public int id;
        public int incomeOrExpenses;
        public float amount;

        public AnnualStatisticsEntity(int id, String date, int incomeOrExpenses, float amount) {
            this.id = id;
            this.date = date;
            this.incomeOrExpenses = incomeOrExpenses;
            BigDecimal bigDecimal = new BigDecimal(amount);
            this.amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }

        public AnnualStatisticsEntity() {

        }

        @Override
        public int compareTo(Object another) {
            return -this.date.compareTo(((AnnualStatisticsEntity) another).date);
        }

        @Override
        public String toString() {
            return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
        }

        public String detail() {
            return "id:" + id + "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
        }
    }

    public class AccountsEntity {
        public int id;
        public String name;
        public int status;

        public AccountsEntity(int id, String name, int status) {
            this.id = id;
            this.name = name;
            this.status = status;
        }

        public AccountsEntity() {

        }


        @Override
        public String toString() {
            return name;
        }

    }


    public class IncomeAndExpensesEntity {
        public int id;
        public String name;
        public int flag;

        public IncomeAndExpensesEntity(int id, String name, int flag) {
            this.id = id;
            this.name = name;
            this.flag = flag;
        }

        public IncomeAndExpensesEntity() {

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
