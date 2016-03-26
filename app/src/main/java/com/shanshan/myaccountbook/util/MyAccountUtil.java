package com.shanshan.myaccountbook.util;

import android.os.Environment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by heshanshan on 2015/12/25.
 */
public class MyAccountUtil {
    public static String dateToString(Date date) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format1.format(date);
    }

    public static Date stringToDate(String str) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format1.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String StringToShortDateString(String str) {
        return str.split(" ")[0];
    }

    public static String dateToMonthlyString(Date date) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM");
        return format1.format(date);
    }

    public static String dateToYearString(Date date) {
        DateFormat format1 = new SimpleDateFormat("yyyy");
        return format1.format(date);
    }

    public static String dateToShortString(Date date) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        return format1.format(date);
    }

    public static Date shortStringToDate(String date) {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format1.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
