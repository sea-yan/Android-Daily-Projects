/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dwh.logindemo.R;

/**
 * KeyboardView.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:53
 */
public class KeyboardView extends FrameLayout implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private ImageView mIvDelete;
    private KeyboardInputer mInputer;

    public KeyboardView(Context context) {
        this(context, null);
    }

    public KeyboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor.
     */
    public KeyboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_keyboard, this);
        mIvDelete = view.findViewById(R.id.iv_delete);
        view.findViewById(R.id.tv_key_1).setOnClickListener(this);
        view.findViewById(R.id.tv_key_2).setOnClickListener(this);
        view.findViewById(R.id.tv_key_3).setOnClickListener(this);
        view.findViewById(R.id.tv_key_4).setOnClickListener(this);
        view.findViewById(R.id.tv_key_5).setOnClickListener(this);
        view.findViewById(R.id.tv_key_6).setOnClickListener(this);
        view.findViewById(R.id.tv_key_7).setOnClickListener(this);
        view.findViewById(R.id.tv_key_8).setOnClickListener(this);
        view.findViewById(R.id.tv_key_9).setOnClickListener(this);
        view.findViewById(R.id.tv_key_0).setOnClickListener(this);
        mIvDelete.setOnClickListener(this);
        mIvDelete.setOnLongClickListener(this);
    }

    /**
     * update delete icon light.
     */
    public void updateDeleteLight() {
        updateDeleteView();
    }

    private void updateDeleteView() {
        if (mInputer != null) {
            if (mInputer.isClear()) {
                mIvDelete.setImageResource(R.drawable.icon_key_delete_dark);
            } else {
                mIvDelete.setImageResource(R.drawable.icon_key_delete);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_key_1:
            case R.id.tv_key_2:
            case R.id.tv_key_3:
            case R.id.tv_key_4:
            case R.id.tv_key_5:
            case R.id.tv_key_6:
            case R.id.tv_key_7:
            case R.id.tv_key_8:
            case R.id.tv_key_9:
            case R.id.tv_key_0:
                if (mInputer != null && mInputer.canInput()) {
                    mInputer.input(((TextView) v).getText().toString());
                    updateDeleteView();
                }
                break;
            case R.id.iv_delete:
                if (!mInputer.isClear()) {
                    mInputer.delete();
                }
                updateDeleteView();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.iv_delete) {
            if (mInputer != null && !mInputer.isClear()) {
                mInputer.clear();
                updateDeleteView();
            }
        }
        return false;
    }

    public void setKeyboardInputer(KeyboardInputer inputer) {
        this.mInputer = inputer;
        updateDeleteView();
    }

    public interface KeyboardInputer {
        void input(String text);

        void delete();

        boolean canInput();

        boolean isClear();

        void clear();
    }
}
