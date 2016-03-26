package com.shanshan.myaccountbook.entity;

import com.shanshan.myaccountbook.database.DBTablesDefinition;

import java.math.BigDecimal;

/**
 * Created by heshanshan on 2016/3/26.
 */
public class AnnualStatisticsEntity extends AbstractRecord {
    private int id;
    private int incomeOrExpenses;
    private float amount;
    private String date;

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
    public String toString() {
        return date + " " + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " " + amount;
    }

    public String detail() {
        return "日期:" + date + "类型:" + (incomeOrExpenses == DBTablesDefinition.INCOME ? "收入" : "支出") + " 金额:" + amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}