package com.dwh.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OpenHelper extends SQLiteOpenHelper {
    //建表语句（创建用户表）
    public static final String CREATE_USER = "create table user ("
            + "userid integer primary key autoincrement, "
            + "username text, "
            + "password text)";

    /**
     * 构造方法
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    /**
     * 初次创建
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_USER);//创建用户表
    }

    /**
     * 当数据库版本出现改变时
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
