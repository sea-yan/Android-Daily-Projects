package com.dwh.loginservice.database.tablefield;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dwh.loginservice.database.DatabaseHepler;

import java.util.ArrayList;
import java.util.Map;

public class DatabaceManager {

    private DatabaseHepler dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ArrayList<Map<String, Object>> data;
    public boolean isLogin ;
    //插入数据
    protected void dbAdd() {
        // TODO Auto-generated method stub
        ContentValues values = new ContentValues();
        values.put("phonenum", 123456);
        values.put("pwd", 123);
        long rowid = db.insert("Account", null, values);
        if (rowid == -1)
            Log.i("myDbDemo", "数据插入失败！");
        else
            Log.i("myDbDemo", "数据插入成功!" + rowid);
    }

    //查询数据
    protected boolean dbFindAll() {
        // TODO Auto-generated method stub

        data.clear();
        cursor = db.query("Account", null, null, null, null, null, "_id ASC");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            String phonenum = cursor.getString(cursor.getColumnIndex("phonenum"));
            String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            if (phonenum == "123456" && pwd == "123") {
                isLogin = true;
            } else {
                isLogin = false;
            }

            cursor.moveToNext();

        }
        return isLogin;
    }
}
