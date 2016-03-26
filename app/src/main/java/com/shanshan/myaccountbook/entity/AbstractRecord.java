package com.shanshan.myaccountbook.entity;

import java.io.Serializable;

/**
 * Created by heshanshan on 2016/3/26.
 */
public abstract class AbstractRecord implements Serializable {
    public abstract String toString();

    public abstract String detail();
}

