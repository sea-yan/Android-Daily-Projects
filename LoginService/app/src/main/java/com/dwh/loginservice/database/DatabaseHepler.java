/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.loginservice.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


/**
 * DatabaseHepler.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM1:02
 */
public class DatabaseHepler extends SQLiteOpenHelper {

    public static final String CREATE_User = "create table Account ("
            + "id integer primary key autoincrement, "
            + "phonenum text,"
            +"pwd text)";
    public static final String TB_NAME = "Account";
    private Context mContext;


    public DatabaseHepler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                TB_NAME + " ( _id integer primary key autoincrement," +
                "phonenum varchar," +
                "pwd varchar" +
                ") ");
    }

    @Override
    public final void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        onCreate(db);
    }


}
