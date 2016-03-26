package com.shanshan.myaccountbook.entity;

import com.shanshan.myaccountbook.database.DBTablesDefinition;

import java.io.Serializable;

public class IncomeAndExpensesEntity implements Serializable {
    private int id;
    private String name;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int flag;

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
        return "name:" + name + "flag:" + (flag == DBTablesDefinition.INCOME ? "收入" : "支出");
    }
}
