package com.shanshan.myaccountbook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.shanshan.myaccountbook.MyAccountUtil;
import com.shanshan.myaccountbook.MyLogger;
import com.shanshan.myaccountbook.entity.Entities;

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
            "CREATE TABLE " + DBTablesDefinition.Records.TABLE_RECORDS_NAME + " (" +
                    DBTablesDefinition.Records.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.Records.COLUMN_RECORDS_ACCOUNT_NAME_ID + INT + COMMA_SEP +
                    DBTablesDefinition.Records.COLUMN_RECORDS_DATE + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.Records.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    DBTablesDefinition.Records.COLUMN_RECORDS_AMOUNT + FLOAT + COMMA_SEP +
                    DBTablesDefinition.Records.COLUMN_RECORDS_REMARKS + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_WEEKLY_STATISTICS =
            "CREATE TABLE " + DBTablesDefinition.WeeklyStatistics.TABLE_WEEKLY_STATISTICS_NAME + " (" +
                    DBTablesDefinition.WeeklyStatistics.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_MONTHLY_STATISTICS =
            "CREATE TABLE " + DBTablesDefinition.MonthlyStatistics.TABLE_MONTHLY_STATISTICS_NAME + " (" +
                    DBTablesDefinition.MonthlyStatistics.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_ANNUAL_STATISTICS =
            "CREATE TABLE " + DBTablesDefinition.AnnualStatistics.TABLE_ANNUAL_STATISTICS_NAME + " (" +
                    DBTablesDefinition.AnnualStatistics.ID + INT + " PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_DATE + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + INT + COMMA_SEP +
                    DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_AMOUNT + FLOAT + " )";

    private static final String SQL_CREATE_INCOME_OR_EXPENSES =
            "CREATE TABLE " + DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME + " (" +
                    DBTablesDefinition.IncomeOrExpenses.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_FLAG + INT +
                    " )";

    private static final String SQL_CREATE_ACCOUNT =
            "CREATE TABLE " + DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME + " (" +
                    DBTablesDefinition.Accounts.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME + TEXT_TYPE + COMMA_SEP +
                    DBTablesDefinition.Accounts.COLUMN_ACCOUNT_STATUS + INT +
                    " )";

    private static final String SQL_DELETE_RECORDS =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.Records.TABLE_RECORDS_NAME;
    private static final String SQL_DELETE_WEEKLY_STATISTICS =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.WeeklyStatistics.TABLE_WEEKLY_STATISTICS_NAME;
    private static final String SQL_DELETE_MONTHLY_STATISTICS =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.MonthlyStatistics.TABLE_MONTHLY_STATISTICS_NAME;
    private static final String SQL_DELETE_ANNUAL_STATISTICS =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.AnnualStatistics.TABLE_ANNUAL_STATISTICS_NAME;
    private static final String SQL_DELETE_INCOME_OR_EXPENSES =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME;
    private static final String SQL_DELETE_ACCOUNTS =
            "DROP TABLE IF EXISTS " + DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME;

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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_RECORDS);
        db.execSQL(SQL_DELETE_INCOME_OR_EXPENSES);
        db.execSQL(SQL_DELETE_ACCOUNTS);
        onCreate(db);
    }


    public void updateWeeklyStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(MyAccountUtil.stringToDate(dateStr));
        calendar1.setFirstDayOfWeek(Calendar.MONDAY);

        calendar1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        final String firstDay = MyAccountUtil.dateToShortString(calendar1.getTime());

        System.out.println("update Weekly Statistics, date is " + firstDay + "incomeOrExpensesType is " + incomeOrExpensesType);
        List<Entities.WeeklyStatistics> list = getWeeklyStatistics(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE + "=? and " + DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        if (list != null && !list.isEmpty()) {
            Entities.WeeklyStatistics weeklyStatistics = (Entities.WeeklyStatistics) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_AMOUNT, weeklyStatistics.amount + amount);
            db.update(DBTablesDefinition.WeeklyStatistics.TABLE_WEEKLY_STATISTICS_NAME, contentValues, DBTablesDefinition.WeeklyStatistics.ID + "=?", new String[]{String.valueOf(weeklyStatistics.id)});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE, firstDay);
            contentValues.put(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_AMOUNT, amount);
            contentValues.put(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, incomeOrExpensesType);
            db.insertWithOnConflict(DBTablesDefinition.WeeklyStatistics.TABLE_WEEKLY_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public List<Entities.WeeklyStatistics> getWeeklyStatistics(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                DBTablesDefinition.WeeklyStatistics.ID,
                DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE,
                DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_AMOUNT
        };

        whereColumns = DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_AMOUNT + ">? " + (whereColumns == null ? "" : " and " + whereColumns);

        List<String> list = new ArrayList<String>();
        list.add(0, "0");

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
                    DBTablesDefinition.WeeklyStatistics.ID + " ASC";

            Cursor c = db.query(
                    DBTablesDefinition.WeeklyStatistics.TABLE_WEEKLY_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.WEEKLY_STATISTICS_ID.clear();
            Entities.WEEKLY_STATISTICS_MAP.clear();

            while (c.moveToNext()) {
                Entities.WeeklyStatistics dummyItem = new Entities().new WeeklyStatistics(c.getInt(0),
                        c.getString(c.getColumnIndex(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE)),
                        c.getInt(2),
                        c.getFloat(3));

                System.out.println("date type is " + c.getType(1));
                System.out.print("weeklyStatistics : id" + c.getInt(0) + " date:" + c.getString(c.getColumnIndex(DBTablesDefinition.WeeklyStatistics.COLUMN_WEEKLY_STATISTICS_DATE))
                        + " incomeorexpenses:" + c.getInt(2) + " amount:" + c.getFloat(3) + "=======");

                Entities.WEEKLY_STATISTICS_ID.add(dummyItem);
                Entities.WEEKLY_STATISTICS_MAP.put(c.getString(0), dummyItem);
            }
        } finally {

        }

        return Entities.WEEKLY_STATISTICS_ID;
    }


    public void updateMonthlyStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(MyAccountUtil.stringToDate(dateStr));
        calendar1.set(Calendar.DAY_OF_MONTH, 1);

        final String firstDay = MyAccountUtil.dateToMonthlyString(calendar1.getTime());

        System.out.println("update Monthly Statistics,date is " + firstDay + "incomeOrExpensesType is " + incomeOrExpensesType);
        List<Entities.MonthlyStatistics> list = getMonthlyStatistics(DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_DATE + "=? and " + DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        if (list != null && !list.isEmpty()) {
            Entities.MonthlyStatistics monthlyStatistics = (Entities.MonthlyStatistics) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_AMOUNT, monthlyStatistics.amount + amount);
            db.update(DBTablesDefinition.MonthlyStatistics.TABLE_MONTHLY_STATISTICS_NAME, contentValues, DBTablesDefinition.MonthlyStatistics.ID + "=?", new String[]{String.valueOf(monthlyStatistics.id)});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_AMOUNT, amount);
            contentValues.put(DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_DATE, firstDay);
            contentValues.put(DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, String.valueOf(incomeOrExpensesType));
            db.insertWithOnConflict(DBTablesDefinition.MonthlyStatistics.TABLE_MONTHLY_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public List<Entities.MonthlyStatistics> getMonthlyStatistics(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                DBTablesDefinition.MonthlyStatistics.ID,
                DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_DATE,
                DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_AMOUNT
        };

        whereColumns = DBTablesDefinition.MonthlyStatistics.COLUMN_MONTHLY_STATISTICS_AMOUNT + ">? " + (whereColumns == null ? "" : " and " + whereColumns);

        List<String> list = new ArrayList<String>();
        list.add(0, "0");

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
                    DBTablesDefinition.MonthlyStatistics.ID + " ASC";

            Cursor c = db.query(
                    DBTablesDefinition.MonthlyStatistics.TABLE_MONTHLY_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.MONTHLY_STATISTICS_ID.clear();
            Entities.MONTHLY_STATISTICS_MAP.clear();

            while (c.moveToNext()) {
                Entities.MonthlyStatistics dummyItem = new Entities().new MonthlyStatistics(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3));

                System.out.print("====================" + c.getInt(0) + c.getString(1) + c.getInt(2) + c.getFloat(3) + "=================");

                Entities.MONTHLY_STATISTICS_ID.add(dummyItem);
                Entities.MONTHLY_STATISTICS_MAP.put(c.getString(0), dummyItem);
            }
        } finally {

        }

        return Entities.MONTHLY_STATISTICS_ID;
    }


    public void updateAnnualStatistics(String dateStr, float amount, int incomeOrExpensesType) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(MyAccountUtil.stringToDate(dateStr));

        calendar1.set(Calendar.DAY_OF_YEAR, 1);
        final String firstDay = MyAccountUtil.dateToYearString(calendar1.getTime());


        System.out.println("update Annual Statistics, date is " + firstDay + " incomeOrExpensesType is " + incomeOrExpensesType);
        List<Entities.AnnualStatistics> list = getAnnualStatistics(DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_DATE + "=? and " + DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE + "=?",
                new String[]{firstDay, String.valueOf(incomeOrExpensesType)});

        if (list != null && !list.isEmpty()) {
            Entities.AnnualStatistics annualStatistics = (Entities.AnnualStatistics) list.get(0);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_AMOUNT, annualStatistics.amount + amount);
            db.update(DBTablesDefinition.AnnualStatistics.TABLE_ANNUAL_STATISTICS_NAME, contentValues, DBTablesDefinition.AnnualStatistics.ID + "=?", new String[]{String.valueOf(annualStatistics.id)});
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_AMOUNT, amount);
            contentValues.put(DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_DATE, firstDay);
            contentValues.put(DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE, String.valueOf(incomeOrExpensesType));
            db.insertWithOnConflict(DBTablesDefinition.AnnualStatistics.TABLE_ANNUAL_STATISTICS_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public List<Entities.AnnualStatistics> getAnnualStatistics(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                DBTablesDefinition.AnnualStatistics.ID,
                DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_DATE,
                DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_FLAG_OF_INCOME_OR_EXPENSE,
                DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_AMOUNT
        };

        whereColumns = DBTablesDefinition.AnnualStatistics.COLUMN_ANNUAL_STATISTICS_AMOUNT + ">? " + (whereColumns == null ? "" : " and " + whereColumns);

        List<String> list = new ArrayList<String>();
        list.add(0, "0");

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
                    DBTablesDefinition.AnnualStatistics.ID + " ASC";

            Cursor c = db.query(
                    DBTablesDefinition.AnnualStatistics.TABLE_ANNUAL_STATISTICS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.ANNUAL_STATISTICS_ID.clear();
            Entities.ANNUAL_STATISTICS_MAP.clear();

            while (c.moveToNext()) {
                Entities.AnnualStatistics dummyItem = new Entities().new AnnualStatistics(c.getInt(0), c.getString(1), c.getInt(2), c.getFloat(3));

                System.out.print("====================" + c.getInt(0) + c.getString(1) + c.getInt(2) + c.getFloat(3) + "=================");

                Entities.ANNUAL_STATISTICS_ID.add(dummyItem);
                Entities.ANNUAL_STATISTICS_MAP.put(c.getString(0), dummyItem);
            }
        } finally {

        }

        return Entities.ANNUAL_STATISTICS_ID;
    }

    public long addAccount(String accountName) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        List list = getAccount(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME + "=?", new String[]{accountName});

        try {
            if (list != null && !list.isEmpty()) {
                Entities.Accounts account = (Entities.Accounts) list.get(0);
                updateAccount(String.valueOf(account.id), accountName, DBTablesDefinition.Accounts.ACCOUNT_AVAILABLE);
            } else {
                ContentValues values = new ContentValues();
                values.put(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME, accountName);
                values.put(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_STATUS, DBTablesDefinition.Accounts.ACCOUNT_AVAILABLE);


            /* Insert the new row, returning the primary key value of the new content row*/
                return db.insertWithOnConflict(DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } finally {
        }
        return -1;
    }

    public List<Entities.Accounts> getAccount() {
        return getAccount(null, null);
    }

    public List<Entities.Accounts> getAccount(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        String[] projection = {
                DBTablesDefinition.Accounts.ID,
                DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME,
                DBTablesDefinition.Accounts.COLUMN_ACCOUNT_STATUS
        };

        try {
        /* How you want the results sorted in the resulting Cursor*/
            String sortOrder =
                    DBTablesDefinition.Accounts.ID + " ASC";
            Cursor c = db.query(
                    DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.ACCOUNT_ID.clear();
            Entities.ACCOUNT_MAP.clear();
            while (c.moveToNext()) {
                System.out.println("get account" + c.getString(1));
                Entities.Accounts dummyItem = new Entities().new Accounts(c.getInt(0), c.getString(1), c.getInt(2));
                Entities.ACCOUNT_ID.add(dummyItem);
                Entities.ACCOUNT_MAP.put(c.getString(0), dummyItem);
            }
        } finally {
        }
        return Entities.ACCOUNT_ID;
    }

    public void deleteAccount(String id) {
        updateAccount(id, null, DBTablesDefinition.Accounts.ACCOUNT_UNAVAILABLE);
    }

    public void updateAccount(String id, String accountName, int status) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            ContentValues values = new ContentValues();
            if (accountName != null) {
                values.put(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME, accountName);
            }
            values.put(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_STATUS, status);

            db.update(DBTablesDefinition.Accounts.TABLE_ACCOUNT_NAME, values, DBTablesDefinition.Accounts.ID + "=? ", new String[]{id});
        } finally {

        }
    }

    public List<Entities.Records> getRecords() {
        return getRecords(null, null);
    }

    public List<Entities.Records> getRecords(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();

        /* Define a projection that specifies which columns from the database*/
        /* you will actually use after this query.*/
        String[] projection = {
                DBTablesDefinition.Records.ID,
                DBTablesDefinition.Records.COLUMN_RECORDS_ACCOUNT_NAME_ID,
                DBTablesDefinition.Records.COLUMN_RECORDS_DATE,
                DBTablesDefinition.Records.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE,
                DBTablesDefinition.Records.COLUMN_RECORDS_AMOUNT,
                DBTablesDefinition.Records.COLUMN_RECORDS_REMARKS
        };

        /* How you want the results sorted in the resulting Cursor*/
        String sortOrder =
                DBTablesDefinition.Records.ID + " ASC";
        try {
            Cursor c = db.query(
                    DBTablesDefinition.Records.TABLE_RECORDS_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.RECORDS_ID.clear();
            Entities.RECORDS_MAP.clear();

            while (c.moveToNext()) {
                Entities.Records dummyItem = new Entities().new Records(c.getInt(0), c.getString(1), c.getString(2), c.getInt(3), c.getFloat(4), c.getString(5));
                Entities.RECORDS_ID.add(dummyItem);
                Entities.RECORDS_MAP.put(c.getString(0), dummyItem);
            }
        } finally {

        }

        return Entities.RECORDS_ID;
    }

    public void deleteRecord(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {

            db.delete(DBTablesDefinition.Records.TABLE_RECORDS_NAME, DBTablesDefinition.Records.ID + "=? ", new String[]{id});
        } finally {
        }
    }

    public long addRecord(String accountID, String dateStr, int incomeOrExpenses, float amount, String remarks) {

        SQLiteDatabase db = this.getSQLiteDatabase();
        ContentValues values = new ContentValues();

        System.out.println("In addRecord function amount is " + amount);

        try {
            values.put(DBTablesDefinition.Records.COLUMN_RECORDS_ACCOUNT_NAME_ID, accountID);
            values.put(DBTablesDefinition.Records.COLUMN_RECORDS_DATE, dateStr);
            values.put(DBTablesDefinition.Records.COLUMN_RECORDS_FLAG_OF_INCOME_OR_EXPENSE, incomeOrExpenses);
            values.put(DBTablesDefinition.Records.COLUMN_RECORDS_AMOUNT, amount);
            values.put(DBTablesDefinition.Records.COLUMN_RECORDS_REMARKS, remarks);

        /* Insert the new row, returning the primary key value of the new row*/
            return db.insertWithOnConflict(DBTablesDefinition.Records.TABLE_RECORDS_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        } finally {
        }
    }

    public int updateRecord(ContentValues cv, String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            return db.update(DBTablesDefinition.Records.TABLE_RECORDS_NAME, cv, DBTablesDefinition.Records.ID + "=?", new String[]{id});
        } finally {

        }

    }

    public List<Entities.IncomeAndExpenses> getIncomeAndExpenses() {
        return getIncomeAndExpenses(null, null);
    }

    public List<Entities.IncomeAndExpenses> getIncomeAndExpenses(String whereColumns, String[] whereValues) {
        SQLiteDatabase db = this.getSQLiteDatabase();

        /* Define a projection that specifies which columns from the database*/
        /* you will actually use after this query.*/
        String[] projection = {
                DBTablesDefinition.IncomeOrExpenses.ID,
                DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME,
                DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_FLAG
        };

        /* How you want the results sorted in the resulting Cursor*/
        String sortOrder =
                DBTablesDefinition.IncomeOrExpenses.ID + " ASC";
        try {
            Cursor c = db.query(
                    DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME,  // The table to query
                    projection,                               // The columns to return
                    whereColumns,                                // The columns for the WHERE clause
                    whereValues,                            // The values for the WHERE clause
                    null,                                     // don't group the rows
                    null,                                     // don't filter by row groups
                    sortOrder                                 // The sort order
            );

            /*clear old datas*/
            Entities.INCOME_AND_EXPENSES_ID.clear();
            Entities.INCOME_AND_EXPENSES_MAP.clear();

            while (c.moveToNext()) {
                System.out.println("get income or expenses id is:" + c.getInt(0) + " , incomeorexpenses is " + c.getString(1));
                Entities.IncomeAndExpenses dummyItem = new Entities().new IncomeAndExpenses(c.getInt(0), c.getString(1), c.getInt(2));
                Entities.INCOME_AND_EXPENSES_ID.add(dummyItem);
                Entities.INCOME_AND_EXPENSES_MAP.put(c.getString(0), dummyItem);
            }
        } finally {
        }

        return Entities.INCOME_AND_EXPENSES_ID;
    }

    public void deleteIncomeOrExpenses(String id) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            db.delete(DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME, DBTablesDefinition.IncomeOrExpenses.ID + "=? ", new String[]{id});
        } finally {

        }
    }

    public long addIncomeAndExpenses(String name, int flag) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        List list = getIncomeAndExpenses(DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME + "=?", new String[]{name});
        try {
            if (list.isEmpty()) {
                ContentValues values = new ContentValues();
                values.put(DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_NAME, name);
                values.put(DBTablesDefinition.IncomeOrExpenses.COLUMN_INOREXP_FLAG, flag);

            /* Insert the new row, returning the primary key value of the new row*/
                return db.insertWithOnConflict(DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } finally {
        }
        return -1;
    }

    public void updateIncomeAndExpenses(String id, String name) {
        SQLiteDatabase db = this.getSQLiteDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(DBTablesDefinition.Accounts.COLUMN_ACCOUNT_NAME, name);

            db.update(DBTablesDefinition.IncomeOrExpenses.TABLE_INOREXP_NAME, values, DBTablesDefinition.IncomeOrExpenses.ID + "=? ", new String[]{id});
        } finally {

        }
    }

}