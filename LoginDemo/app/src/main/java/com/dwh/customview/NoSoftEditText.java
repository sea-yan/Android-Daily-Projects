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
import android.widget.EditText;

import java.lang.reflect.Method;

/**
 * NoSoftEditText.
 *
 * @author: jiangwei
 * @Date: 19-4-17 PM2:44
 */
public class NoSoftEditText extends EditText {
    public NoSoftEditText(Context context) {
        super(context);
        disableShowSoftInput();
    }

    public NoSoftEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        disableShowSoftInput();
    }

    public NoSoftEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        disableShowSoftInput();
    }

    /**
     * disableShowSoftInput.
     */
    public void disableShowSoftInput() {
        Class<EditText> cls = EditText.class;
        Method method;
        try {
            method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
            method.setAccessible(true);
            method.invoke(this, false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        setSelection(text.length());
    }
}
