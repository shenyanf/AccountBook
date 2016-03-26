package com.shanshan.myaccountbook.entity;

/**
 * Created by heshanshan on 2016/3/26.
 */

import com.shanshan.myaccountbook.database.DBTablesDefinition;
import com.shanshan.myaccountbook.database.MyDBHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * A dummy item representing a piece of content.
 */
public class DayRecordsEntity extends AbstractRecord {
    private int id;
    private String date;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAccountNameId() {
        return accountNameId;
    }

    public void setAccountNameId(String accountNameId) {
        this.accountNameId = accountNameId;
    }

    public int getIncomeOrExpenses() {
        return incomeOrExpenses;
    }

    public void setIncomeOrExpenses(int incomeOrExpenses) {
        this.incomeOrExpenses = incomeOrExpenses;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    private String accountNameId;
    private int incomeOrExpenses;
    private float amount;
    private String remarks;

    public DayRecordsEntity(int id, String accountNameId, String date, int incomeOrExpenses, float amount, String remarks) {
        this.id = id;
        this.accountNameId = accountNameId;
        this.date = date;
        this.incomeOrExpenses = incomeOrExpenses;

        BigDecimal bigDecimal = new BigDecimal(amount);
        this.amount = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        this.remarks = remarks;
    }

    public DayRecordsEntity() {

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
        return date + " " + account.getName() + " " + (incomeOrExpensesIns.getFlag() == DBTablesDefinition.INCOME ? "收入/" : "支出/") + incomeOrExpensesIns.getName() + " " + amount;
    }

    public String detail() {
        return toString() + " " + remarks;
    }

}

