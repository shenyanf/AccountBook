package com.shanshan.myaccountbook.util;

import android.os.Environment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static String getSundayDate(String currentDate) {
        String startDate = MyAccountUtil.getMondayDate(currentDate);
        Calendar cal = Calendar.getInstance();
        cal.setTime(MyAccountUtil.shortStringToDate(startDate));
        cal.add(Calendar.DATE, 6);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String endDate = sdf.format(cal.getTime());
        return endDate;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getMondayDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date time = null;
        try {
            time = sdf.parse(dateStr);
            cal.setTime(time);

            // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            if (1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }

            cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

            int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值

            return sdf.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
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

    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        return month;
    }

    public static List<Integer> range(int start, int end, int step) {
        ArrayList<Integer> res = new ArrayList<>();

        if (start < 0 || end < 0 || step <= 0) {
            return res;
        }

        if (end < start) {
            return res;
        }

        for (int i = start; i < end; i = i + step) {
            res.add(i);
        }
        return res;
    }
}
