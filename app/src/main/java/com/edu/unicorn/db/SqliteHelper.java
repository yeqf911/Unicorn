package com.edu.unicorn.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    public static Integer Version = 1;
    public static String DB_NAME = "UserStore.db";
    public static String USER_TABLE = "users";
    public static String BILL_TABLE = "bills";

    private static final String CREATE_USER_TABLE = "create table users(id integer primary key autoincrement," +
            "name varchar(64)," +
            "password varchar(64)," +
            "phone varchar(64)," +
            "email varchar(64))"; // email是唯一的，name可以重复

    private static final String CREATE_BILL_TABLE = "create table bills(id integer primary key autoincrement," +
            "type varchar(64)," +
            "comment varchar(64)," +
            "date varchar(64)," +
            "way varchar(64)," +
            "income REAL," +
            "outcome REAL)";

    private Context context;

    public SqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_BILL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
