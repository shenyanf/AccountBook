package com.shanshan.myaccountbook;

import java.util.Calendar;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    //    public ApplicationTest() {
//        super(Application.class);
//    }
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = (Calendar) calendar.clone();
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);

        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        System.out.print(calendar1.getTime());
        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        System.out.print(calendar1.getTime());
    }
}