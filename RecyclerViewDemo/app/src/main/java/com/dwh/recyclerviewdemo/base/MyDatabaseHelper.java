//package com.dwh.recyclerviewdemo.base;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import android.widget.Toast;
//
//
//public class MyDatabaseHelper extends SQLiteOpenHelper {
//
//    public static final String CREATE_Fruit = "create table fruit ("
//        + "id integer primary key autoincrement, "
//        + "name text)";
//    private Context mContext;
//
//    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
//        super(context, name, factory, version);
//        mContext = context;
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        db.execSQL(CREATE_Fruit);
//        Toast.makeText(mContext,"Create succeeded",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//
//}
