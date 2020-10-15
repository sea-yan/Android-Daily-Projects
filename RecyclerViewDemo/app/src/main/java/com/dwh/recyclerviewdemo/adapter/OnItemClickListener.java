package com.dwh.recyclerviewdemo.adapter;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.dwh.recyclerviewdemo.Fruit;



public interface OnItemClickListener {
    //参数（父组件，当前单击的View,单击的View的位置，数据）
    void onItemClick(RecyclerView parent, View view, int position, Fruit data);
}
