package com.shanshan.myaccountbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.shanshan.myaccountbook.database.DBTablesDefinition.AccountsDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.AnnualStatisticsDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.IncomeOrExpensesDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.MonthlyStatisticsDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.RecordsDefinition;
import com.shanshan.myaccountbook.database.DBTablesDefinition.WeeklyStatisticsDefinition;
import com.shanshan.myaccountbook.entity.AccountsEntity;
import com.shanshan.myaccountbook.entity.AnnualStatisticsEntity;
import com.shanshan.myaccountbook.entity.DayRecordsEntity;
import com.shanshan.myaccountbook.entity.IncomeAndExpensesEntity;
import com.shanshan.myaccountbook.entity.MonthlyStatisticsEntity;
import com.shanshan.myaccountbook.entity.WeeklyStatisticsEntity;
import com.shanshan.myaccountbook.util.MyAccountUtil;
import com.shanshan.myaccountbook.util.MyLogger;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by heshanshan on 2015/11/15.
 */
public class MyDBHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " VARCHAR";
    private static final String COMMA_SEP = ",";
    private static final String INT = " INTEGER ";
    private static final String FLOAT = " FLOAT ";


    private static final String SQL_CREATE_RECORDS =
            "CREATE TABLE " + RecordsDefinition.TABLE_RECORDS_NAME + " (" +
                    RecordsDefinition.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    RecordsDefinition.COLUMN_RECORDS_ACCOUNT_NAME_ID + INT + COMMA_SEP +
                    RecordsDefinition.COLUMN_RECORDS_DATE + TEXT_TYPE + COMMA_SEP +
                    RecordsDefinition.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    RecordsDefinition.COLUMN_RECORDS_AMOUNT + FLOAT + COMMA_SEP +
                    RecordsDefinition.COLUMN_RECORDS_REMARKS + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_WEEKLY_STATISTICS =
            "CREATE TABLE " + WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME + " (" +
                    WeeklyStatisticsDefinition.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_MONTHLY_STATISTICS =
            "CREATE TABLE " + MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME + " (" +
                    MonthlyStatisticsDefinition.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_ANNUAL_STATISTICS =
            "CREATE TABLE " + AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME + " (" +
                    AnnualStatisticsDefinition.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_INCOME_OR_EXPENSES =
            "CREATE TABLE " + IncomeOrExpensesDefinition.TABLE_INOREXP_NAME + " (" +
                    IncomeOrExpensesDefinition.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME + TEXT_TYPE + COMMA_SEP +
                    IncomeOrExpensesDefinition.COLUMN_INOREXP_FLAG + INT +
                    " )";

    private static final String SQL_CREATE_ACCOUNT =
            "CREATE TABLE " + AccountsDefinition.TABLE_ACCOUNT_NAME + " (" +
                    AccountsDefinition.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    AccountsDefinition.COLUMN_ACCOUNT_NAME + TEXT_TYPE + COMMA_SEP +
                    AccountsDefinition.COLUMN_ACCOUNT_STATUS + INT +
                    " )";

    private static final String SQL_DELETE_RECORDS =
            "DROP TABLE IF EXISTS " + RecordsDefinition.TABLE_RECORDS_NAME;
    private static final String SQL_DELETE_WEEKLY_STATISTICS =
            "DROP TABLE IF EXISTS " + WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME;
    private static final String SQL_DELETE_MONTHLY_STATISTICS =
            "DROP TABLE IF EXISTS " + MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME;
    private static final String SQL_DELETE_ANNUAL_STATISTICS =
            "DROP TABLE IF EXISTS " + AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME;
    private static final String SQL_DELETE_INCOME_OR_EXPENSES =
            "DROP TABLE IF EXISTS " + IncomeOrExpensesDefinition.TABLE_INOREXP_NAME;
    private static final String SQL_DELETE_ACCOUNTS =
            "DROP TABLE IF EXISTS " + AccountsDefinition.TABLE_ACCOUNT_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyAccount.db";
    public static String dbPath = null;
    private static Logger myLogger = MyLogger.getMyLogger(MyDBHelper.class.getName());

    private static MyDBHelper myDBHelper = null;
    private static SQLiteDatabase db = null;

    private MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static MyDBHelper newInstance(Context context) {
        if (myDBHelper == null) {
            if (MyAccountUtil.isSDCardMounted()) {
                dbPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                dbPath = Environment.getDataDirectory().getAbsolutePath();
            }
            dbPath += File.separator + "myaccount" + File.separator + "databases" + File.separator + DATABASE_NAME;

            System.out.println("DataBase dir is " + dbPath);
            myLogger.debug("DataBase dir is " + dbPath);

            myDBHelper = new MyDBHelper(context, dbPath, null, MyDBHelper.DATABASE_VERSION);
        }
        return myDBHelper;
    }

    public static SQLiteDatabase getSQLiteDatabase() {
        return db == null ? myDBHelper.getWritableDatabase() : db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Start initiating databases.......");
        myLogger.debug("Start initiating databases.......");

        db.execSQL(SQL_CREATE_ACCOUNT);
        db.execSQL(SQL_CREATE_INCOME_OR_EXPENSES);
        db.execSQL(SQL_CREATE_RECORDS);
        db.execSQL(SQL_CREATE_WEEKLY_STATISTICS);
        db.execSQL(SQL_CREATE_MONTHLY_STATISTICS);
        db.execSQL(SQL_CREATE_ANNUAL_STATISTICS);

        System.out.println("Finish initiating databases...................");
        myLogger.debug("Finish initiating databases...................");
    }


    public void initDB() {
        addAccount("现金账户");
        addAccount("招行储蓄卡");
        addAccount("招行信用卡");
        addAccount("北京银行储蓄卡");
        addAccount("工行储蓄卡");
        addAccount("支付宝");
        addAccount("QQ红包");
        addAccount("百度钱包");
        addAccount("理财账户");
        addAccount("微信");

        addIncomeAndExpenses("吃饭", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("外卖", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("超市", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("购物", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("房租", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("水电煤、公交、话费等", DBTablesDefinition.EXPENSES);
        addIncomeAndExpenses("杂项", DBTablesDefinition.EXPENSES);

        addIncomeAndExpenses("工资", DBTablesDefinition.INCOME);
        addIncomeAndExpenses("利息", DBTablesDefinition.INCOME);
        addIncomeAndExpenses("理财收益", DBTablesDefinition.INCOME);
        myLogger.debug("init database...");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_RECORDS);
        db.execSQL(SQL_DELETE_INCOME_OR_EXPENSES);
        db.execSQL(SQL_DELETE_ACCOUNTS);
        onCreate(db);
    }

    public void deleteWeeklyStatistic(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {

            db.delete(WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME, WeeklyStatisticsDefinition.ID + "=? ", new String[]{id});
        } finally {
        }
    }

    public void updateWeeklyStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String firstDay = MyAccountUtil.getMondayDate(dateStr);

//        System.out.println("update Weekly Statistics, date is " + firstDay + "incomeOrExpensesType is " + incomeOrExpensesType);
        List<WeeklyStatisticsEntity> list = getWeeklyStatistics(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + "=? and " + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        //如果已经有记录，就update，否则就insert
        if (list != null && !list.isEmpty() && list.size() == 1) {
            WeeklyStatisticsEntity weeklyStatisticsEntity = (WeeklyStatisticsEntity) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT, weeklyStatisticsEntity.getAmount() + amount);
            db.updateWithOnConflict(WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME, contentValues, WeeklyStatisticsDefinition.ID + "=?", new String[]{String.valueOf(weeklyStatisticsEntity.getId())}, SQLiteDatabase.CONFLICT_IGNORE);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE, firstDay);
            contentValues.put(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT, amount);
            contentValues.put(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, incomeOrExpensesType);
            db.insertWithOnConflict(WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }

        //如果更新后amount为0，则删除该条记录
        List<WeeklyStatisticsEntity> list1 = getWeeklyStatistics(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + "=?", new String[]{firstDay});
        if (list1 != null && !list.isEmpty() && list.size() == 1) {
            WeeklyStatisticsEntity weeklyStatisticsEntity1 = list1.get(0);
//            System.out.println("weeklyStatistics amount is :" + weeklyStatisticsEntity1.getAmount());

            if (Float.compare(weeklyStatisticsEntity1.getAmount(), 0.00f) <= 0) {
                db.delete(WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME, WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + "=?", new String[]{firstDay});
            }
        }

    }

    public int getWeeklyRecordsCount() {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String sql = "select count(*) from " + WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME;// + " where " + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT + ">0";
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        int length = c.getInt(0);
        c.close();
        return length;
    }

    public ArrayList<WeeklyStatisticsEntity> getCurrentPageWeeklyRecords(int currentPage, int pageSize) {
        int firstResult = (currentPage - 1) * pageSize;
//        int maxResult = currentPage * pageSize;

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select " + WeeklyStatisticsDefinition.ID + "," + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + "," +
                WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "," + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT +
                " from " + WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME
                //+ " where " + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT + ">0 "
                + " order by " + WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE +
                " desc limit ?,?";
        Cursor mCursor = db.rawQuery(
                sql,
                new String[]{String.valueOf(firstResult),
                        String.valueOf(pageSize)});
        ArrayList<WeeklyStatisticsEntity> items = new ArrayList<WeeklyStatisticsEntity>();

        while (mCursor.moveToNext()) {
            WeeklyStatisticsEntity dummyItem = new WeeklyStatisticsEntity(mCursor.getInt(0),
                    mCursor.getString(mCursor.getColumnIndex(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE)),
                    mCursor.getInt(2), mCursor.getFloat(3));
            items.add(dummyItem);

        }
        //不要关闭数据库
        return items;
    }

    public List<WeeklyStatisticsEntity> getWeeklyStatistics(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<WeeklyStatisticsEntity> WEEKLY_STATISTICS_LIST = new ArrayList<>();

        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                WeeklyStatisticsDefinition.ID,
                WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE,
                WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_AMOUNT
        };

        whereColumns = (whereColumns == null) ? "" : " 1=1 and " + whereColumns;

        List<String> list = new ArrayList<String>();

        if (whereValues != null) {
            for (String s : whereValues) {
                list.add(s);
            }
        }
        String[] values = new String[]{};

        whereValues = list.toArray(values);


        try {
        /* How you want the results sorted in the resulting Cursor*/
            String sortOrder =
                    WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE + " DESC";

            Cursor c = db.query(
                    WeeklyStatisticsDefinition.TABLE_WEEKLY_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (c.moveToNext()) {
                WeeklyStatisticsEntity dummyItem = new WeeklyStatisticsEntity(c.getInt(0),
                        c.getString(c.getColumnIndex(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE)),
                        c.getInt(2),
                        c.getFloat(3));

//                System.out.println("date type is " + c.getType(1));
//                System.out.print("WeeklyStatisticsEntity : id" + c.getInt(0) + " date:" + c.getString(c.getColumnIndex(WeeklyStatisticsDefinition.COLUMN_WEEKLY_STATISTICS_DATE))
//                        + " incomeorexpenses:" + c.getInt(2) + " amount:" + c.getFloat(3) + "=======");

                WEEKLY_STATISTICS_LIST.add(dummyItem);
            }
        } finally {

        }

        return WEEKLY_STATISTICS_LIST;
    }


    public void deleteMonthlyStatistic(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {

            db.delete(MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME, MonthlyStatisticsDefinition.ID + "=? ", new String[]{id});
        } finally {
        }
    }

    public void updateMonthlyStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(MyAccountUtil.stringToDate(dateStr));
        calendar1.set(Calendar.DAY_OF_MONTH, 1);

        final String firstDay = MyAccountUtil.dateToMonthlyString(calendar1.getTime());

//        System.out.println("update Monthly Statistics,date is " + firstDay + "incomeOrExpensesType is " + incomeOrExpensesType);
        List<MonthlyStatisticsEntity> list = getMonthlyStatistics(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + "=? and " + MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        if (list != null && !list.isEmpty()) {
            MonthlyStatisticsEntity monthlyStatistics = (MonthlyStatisticsEntity) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_AMOUNT, monthlyStatistics.getAmount() + amount);
            db.update(MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME, contentValues, MonthlyStatisticsDefinition.ID + "=?", new String[]{String.valueOf(monthlyStatistics.getId())});

            //如果更新后amount为0，则删除该条记录
            List<MonthlyStatisticsEntity> list1 = getMonthlyStatistics(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + "=?", new String[]{firstDay});
            if (list1 != null && !list.isEmpty() && list.size() == 1) {
                MonthlyStatisticsEntity monthlyStatistics1 = (MonthlyStatisticsEntity) list1.get(0);

                if (Float.compare(monthlyStatistics1.getAmount(), 0.00f) <= 0) {
                    db.delete(MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME, MonthlyStatisticsDefinition.ID + "=?", new String[]{String.valueOf(monthlyStatistics1.getId())});
                }
            }

        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_AMOUNT, amount);
            contentValues.put(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE, firstDay);
            contentValues.put(MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, String.valueOf(incomeOrExpensesType));
            db.insertWithOnConflict(MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public int getMonthlyRecordsCount() {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String sql = "select count(*) from " + MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        int length = c.getInt(0);
        c.close();
        return length;
    }

    public ArrayList<MonthlyStatisticsEntity> getCurrentPageMonthlyRecords(int currentPage, int pageSize) {
        int firstResult = (currentPage - 1) * pageSize;
//        int maxResult = currentPage * pageSize;

        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select " + MonthlyStatisticsDefinition.ID + "," + MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + "," +
                MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "," + MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_AMOUNT +
                " from " + MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME + " order by " + MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + " desc limit ?,?";
        Cursor mCursor = db.rawQuery(
                sql,
                new String[]{String.valueOf(firstResult),
                        String.valueOf(pageSize)});
        ArrayList<MonthlyStatisticsEntity> items = new ArrayList<MonthlyStatisticsEntity>();

        while (mCursor.moveToNext()) {
            MonthlyStatisticsEntity dummyItem = new MonthlyStatisticsEntity(mCursor.getInt(0), mCursor.getString(1), mCursor.getInt(2), mCursor.getFloat(3));
            items.add(dummyItem);

        }
        //不要关闭数据库
        return items;
    }


    public List<MonthlyStatisticsEntity> getMonthlyStatistics(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<MonthlyStatisticsEntity> MONTHLY_STATISTICS_LIST = new ArrayList<>();

        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                MonthlyStatisticsDefinition.ID,
                MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE,
                MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_AMOUNT
        };

        whereColumns = (whereColumns == null ? "" : " 1=1 and " + whereColumns);

        List<String> list = new ArrayList<String>();

        if (whereValues != null) {
            for (String s : whereValues) {
                list.add(s);
            }
        }
        String[] values = new String[]{};

        whereValues = list.toArray(values);


        try {
        /* How you want the results sorted in the resulting Cursor*/
            String sortOrder =
                    MonthlyStatisticsDefinition.COLUMN_MONTHLY_STATISTICS_DATE + " DESC";

            Cursor c = db.query(
                    MonthlyStatisticsDefinition.TABLE_MONTHLY_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );


            while (c.moveToNext()) {
                MonthlyStatisticsEntity dummyItem = new MonthlyStatisticsEntity(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3));

//                System.out.print("====================" + c.getInt(0) + c.getString(1) + c.getInt(2) + c.getFloat(3) + "=================");

                MONTHLY_STATISTICS_LIST.add(dummyItem);
            }
        } finally {

        }

        return MONTHLY_STATISTICS_LIST;
    }

    public void deleteAnnualStatistic(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {

            db.delete(AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME, AnnualStatisticsDefinition.ID + "=? ", new String[]{id});
        } finally {
        }
    }

    public void updateAnnualStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(MyAccountUtil.stringToDate(dateStr));

        calendar1.set(Calendar.DAY_OF_YEAR, 1);
        final String firstDay = MyAccountUtil.dateToYearString(calendar1.getTime());


//        System.out.println("update Annual Statistics, date is " + firstDay + " incomeOrExpensesType is " + incomeOrExpensesType);
        List<AnnualStatisticsEntity> list = getAnnualStatistics(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + "=? and " + AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        if (list != null && !list.isEmpty()) {
            AnnualStatisticsEntity annualStatistics = (AnnualStatisticsEntity) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_AMOUNT, annualStatistics.getAmount() + amount);
            db.update(AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME, contentValues, AnnualStatisticsDefinition.ID + "=?", new String[]{String.valueOf(annualStatistics.getId())});

            //如果更新后amount为0，则删除该条记录
            List<AnnualStatisticsEntity> list1 = getAnnualStatistics(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + "=?", new String[]{firstDay});
            if (list1 != null && !list.isEmpty() && list.size() == 1) {
                AnnualStatisticsEntity annualStatisticsEntity1 = (AnnualStatisticsEntity) list1.get(0);
                if (Float.compare(annualStatisticsEntity1.getAmount(), 0.00f) <= 0) {
                    db.delete(AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME, AnnualStatisticsDefinition.ID + "=?", new String[]{String.valueOf(annualStatisticsEntity1.getId())});
                }
            }
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_AMOUNT, amount);
            contentValues.put(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE, firstDay);
            contentValues.put(AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, String.valueOf(incomeOrExpensesType));
            db.insertWithOnConflict(AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public List<AnnualStatisticsEntity> getAnnualStatistics(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<AnnualStatisticsEntity> ANNUAL_STATISTICS_LIST = new ArrayList<>();

        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                AnnualStatisticsDefinition.ID,
                AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE,
                AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_AMOUNT
        };

        whereColumns = (whereColumns == null ? "" : " 1=1 and " + whereColumns);

        List<String> list = new ArrayList<String>();

        if (whereValues != null) {
            for (String s : whereValues) {
                list.add(s);
            }
        }
        String[] values = new String[]{};

        whereValues = list.toArray(values);

        try {
        /* How you want the results sorted in the resulting Cursor*/
            String sortOrder =
                    AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + " DESC";

            Cursor c = db.query(
                    AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (c.moveToNext()) {
                AnnualStatisticsEntity dummyItem = new AnnualStatisticsEntity(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3));

//                System.out.print("====================" + c.getInt(0) + c.getString(1) + c.getInt(2) + c.getFloat(3) + "=================");

                ANNUAL_STATISTICS_LIST.add(dummyItem);
            }
        } finally {

        }

        return ANNUAL_STATISTICS_LIST;
    }

    public int getAnnualRecordsCount() {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String sql = "select count(*) from " + AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        int length = c.getInt(0);
        c.close();
        return length;
    }

    public ArrayList<AnnualStatisticsEntity> getCurrentPageAnnualRecords(int currentPage, int pageSize) {
        int firstResult = (currentPage - 1) * pageSize;
//        int maxResult = currentPage * pageSize;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select " + AnnualStatisticsDefinition.ID + "," + AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + "," +
                AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "," + AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_AMOUNT +
                " from " + AnnualStatisticsDefinition.TABLE_ANNUAL_STATISTICS_NAME + " order by " + AnnualStatisticsDefinition.COLUMN_ANNUAL_STATISTICS_DATE + " desc limit ?,?";
        Cursor mCursor = db.rawQuery(
                sql,
                new String[]{String.valueOf(firstResult),
                        String.valueOf(pageSize)});
        ArrayList<AnnualStatisticsEntity> items = new ArrayList<AnnualStatisticsEntity>();

        while (mCursor.moveToNext()) {
            AnnualStatisticsEntity dummyItem = new AnnualStatisticsEntity(mCursor.getInt(0), mCursor.getString(1), mCursor.getInt(2), mCursor.getFloat(3));
            items.add(dummyItem);

        }
        //不要关闭数据库
        return items;
    }


    public long addAccount(String accountName) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        List list = getAccount(AccountsDefinition.COLUMN_ACCOUNT_NAME + "=?", new String[]{accountName});

        try {
            if (list != null && !list.isEmpty() && list.size() == 1) {
                AccountsEntity account = (AccountsEntity) list.get(0);
                updateAccount(String.valueOf(account.getId()), accountName, AccountsDefinition.ACCOUNT_AVAILABLE);
            } else {
                ContentValues values = new ContentValues();
                values.put(AccountsDefinition.COLUMN_ACCOUNT_NAME, accountName);
                values.put(AccountsDefinition.COLUMN_ACCOUNT_STATUS, AccountsDefinition.ACCOUNT_AVAILABLE);


            /* Insert the new row, returning the primary key value of the new content row*/
                return db.insertWithOnConflict(AccountsDefinition.TABLE_ACCOUNT_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } finally {
        }
        return -1;
    }

    public List<AccountsEntity> getAccount() {
        return getAccount(null, null);
    }

    public List<AccountsEntity> getAccount(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<AccountsEntity> ACCOUNT_LIST = new ArrayList<>();

        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                AccountsDefinition.ID,
                AccountsDefinition.COLUMN_ACCOUNT_NAME,
                AccountsDefinition.COLUMN_ACCOUNT_STATUS
        };

        try {
        /* How you want the results sorted in the resulting Cursor*/
            String sortOrder =
                    AccountsDefinition.ID + " ASC";
            Cursor c = db.query(
                    AccountsDefinition.TABLE_ACCOUNT_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (c.moveToNext()) {
//                System.out.println("get account" + c.getString(1));
                AccountsEntity dummyItem = new AccountsEntity(c.getInt(0), c.getString(1), c.getInt(2));
                ACCOUNT_LIST.add(dummyItem);
            }
        } finally {
        }
        return ACCOUNT_LIST;
    }

    public void deleteAccount(String id) {
        updateAccount(id, null, AccountsDefinition.ACCOUNT_UNAVAILABLE);
    }

    public void updateAccount(String id, String accountName, int status) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            ContentValues values = new ContentValues();
            if (accountName != null) {
                values.put(AccountsDefinition.COLUMN_ACCOUNT_NAME, accountName);
            }
            values.put(AccountsDefinition.COLUMN_ACCOUNT_STATUS, status);

            db.update(AccountsDefinition.TABLE_ACCOUNT_NAME, values, AccountsDefinition.ID + "=? ", new String[]{id});
        } finally {

        }
    }

    public int getDayRecordsCount() {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String sql = "select count(*) from " + RecordsDefinition.TABLE_RECORDS_NAME;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        int length = c.getInt(0);
        c.close();
        return length;
    }

    public ArrayList<DayRecordsEntity> getCurrentPageDayRecords(int currentPage, int pageSize) {
        int firstResult = (currentPage - 1) * pageSize;

//        sqllite limit is different with mysql,limit m,m means start with m and offset n

//        int maxResult = currentPage * pageSize;
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select " + RecordsDefinition.ID + "," + RecordsDefinition.COLUMN_RECORDS_ACCOUNT_NAME_ID + "," + RecordsDefinition.COLUMN_RECORDS_DATE + "," +
                RecordsDefinition.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE + "," + RecordsDefinition.COLUMN_RECORDS_AMOUNT + "," +
                RecordsDefinition.COLUMN_RECORDS_REMARKS + " from " + RecordsDefinition.TABLE_RECORDS_NAME + " order by " + RecordsDefinition.COLUMN_RECORDS_DATE + " desc limit ?,?";
        Cursor mCursor = db.rawQuery(
                sql,
                new String[]{String.valueOf(firstResult),
                        String.valueOf(pageSize)});
        ArrayList<DayRecordsEntity> items = new ArrayList<DayRecordsEntity>();
        int columnCount = mCursor.getColumnCount();
        while (mCursor.moveToNext()) {
            DayRecordsEntity dummyItem = new DayRecordsEntity(mCursor.getInt(0), mCursor.getString(1), mCursor.getString(2), mCursor.getInt(3), mCursor.getFloat(4), mCursor.getString(5));
            items.add(dummyItem);

        }
        //不要关闭数据库
        return items;
    }


    public List<DayRecordsEntity> getRecords() {
        return getRecords(null, null);
    }

    public List<DayRecordsEntity> getRecords(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<DayRecordsEntity> RECORDS_LIST = new ArrayList<>();


        SQLiteDatabase db = this.getSQLiteDatabase();

        /* Define a projection that specifies which columns from the database*/
        /* you will actually use after this query.*/
        String[] projection = {
                RecordsDefinition.ID,
                RecordsDefinition.COLUMN_RECORDS_ACCOUNT_NAME_ID,
                RecordsDefinition.COLUMN_RECORDS_DATE,
                RecordsDefinition.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE,
                RecordsDefinition.COLUMN_RECORDS_AMOUNT,
                RecordsDefinition.COLUMN_RECORDS_REMARKS
        };

        /* How you want the results sorted in the resulting Cursor*/
        String sortOrder =
                RecordsDefinition.COLUMN_RECORDS_DATE + " DESC";
        try {
            Cursor c = db.query(
                    RecordsDefinition.TABLE_RECORDS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (c.moveToNext()) {
                DayRecordsEntity dummyItem = new DayRecordsEntity(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getFloat(4), c.getString(5));
                RECORDS_LIST.add(dummyItem);
            }
        } finally {

        }

        return RECORDS_LIST;
    }

    public void deleteRecord(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {

            db.delete(RecordsDefinition.TABLE_RECORDS_NAME, RecordsDefinition.ID + "=? ", new String[]{id});
        } finally {
        }
    }

    public long addRecord(String accountID, String dateStr, int incomeOrExpenses, float amount, String remarks) {

        SQLiteDatabase db = this.getSQLiteDatabase();
        ContentValues values = new ContentValues();

        System.out.println("In addRecord function amount is " + amount);

        try {
            values.put(RecordsDefinition.COLUMN_RECORDS_ACCOUNT_NAME_ID, accountID);
            values.put(RecordsDefinition.COLUMN_RECORDS_DATE, dateStr);
            values.put(RecordsDefinition.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE, incomeOrExpenses);
            values.put(RecordsDefinition.COLUMN_RECORDS_AMOUNT, amount);
            values.put(RecordsDefinition.COLUMN_RECORDS_REMARKS, remarks);

        /* Insert the new row, returning the primary key value of the new row*/
            return db.insertWithOnConflict(RecordsDefinition.TABLE_RECORDS_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        } finally {
        }
    }

    public int updateRecord(ContentValues cv, String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            return db.update(RecordsDefinition.TABLE_RECORDS_NAME, cv, RecordsDefinition.ID + "=?", new String[]{id});
        } finally {

        }

    }

    public List<IncomeAndExpensesEntity> getIncomeAndExpenses() {
        return getIncomeAndExpenses(null, null);
    }

    public List<IncomeAndExpensesEntity> getIncomeAndExpenses(String whereColumns, String[] whereValues) {
        /* A map of records items, by ID.*/
        List<IncomeAndExpensesEntity> INCOME_AND_EXPENSES_LIST = new ArrayList<>();

        SQLiteDatabase db = this.getSQLiteDatabase();

        /* Define a projection that specifies which columns from the database*/
        /* you will actually use after this query.*/
        String[] projection = {
                IncomeOrExpensesDefinition.ID,
                IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME,
                IncomeOrExpensesDefinition.COLUMN_INOREXP_FLAG
        };

        /* How you want the results sorted in the resulting Cursor*/
        String sortOrder =
                IncomeOrExpensesDefinition.ID + " ASC";
        try {
            Cursor c = db.query(
                    IncomeOrExpensesDefinition.TABLE_INOREXP_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            while (c.moveToNext()) {
//                System.out.println("get income or expenses id is:" + c.getInt(0) + " , incomeorexpenses is " + c.getString(1));
                IncomeAndExpensesEntity dummyItem = new IncomeAndExpensesEntity(c.getInt(0), c.getString(1), c.getInt(2));
                INCOME_AND_EXPENSES_LIST.add(dummyItem);
            }
        } finally {
        }

        return INCOME_AND_EXPENSES_LIST;
    }

    public void deleteIncomeOrExpenses(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            db.delete(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME, IncomeOrExpensesDefinition.ID + "=? ", new String[]{id});
        } finally {

        }
    }

    public long addIncomeAndExpenses(String name, int flag) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        List list = getIncomeAndExpenses(IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME + "=?", new String[]{name});
        try {
            if (list.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(IncomeOrExpensesDefinition.COLUMN_INOREXP_NAME, name);
                values.put(IncomeOrExpensesDefinition.COLUMN_INOREXP_FLAG, flag);

            /* Insert the new row, returning the primary key value of the new row*/
                return db.insertWithOnConflict(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } finally {
        }
        return -1;
    }

    public void updateIncomeAndExpenses(String id, String name) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(AccountsDefinition.COLUMN_ACCOUNT_NAME, name);

            db.update(IncomeOrExpensesDefinition.TABLE_INOREXP_NAME, values, IncomeOrExpensesDefinition.ID + "=? ", new String[]{id});
        } finally {

        }
    }

}