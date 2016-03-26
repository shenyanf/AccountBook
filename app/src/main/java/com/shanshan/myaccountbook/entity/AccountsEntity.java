package com.shanshan.myaccountbook.entity;

/**
 * Created by heshanshan on 2016/3/26.
 */
public class AccountsEntity {
    private int id;
    private String name;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    private int status;

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
