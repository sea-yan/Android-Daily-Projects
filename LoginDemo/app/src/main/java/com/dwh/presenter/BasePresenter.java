/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.presenter;

import android.content.Context;

import com.dwh.model.AccountHmiModel;
import com.dwh.model.AccountServiceModel;
import com.dwh.model.listener.IStringCallback;

/**
 * BasePresenter.
 *
 * @author: jiangwei
 * @Date: 19-4-8 PM2:09
 */
public class BasePresenter implements IStringCallback {
    protected Context mContext;
    protected AccountServiceModel mAccountServiceModel;
    protected AccountHmiModel mAccountHmiModel;


    /**
     * request.
     *
     * @param reqCode reqCode
     * @param args    args
     */
    protected void request(int reqCode, String args) {
        mAccountHmiModel.request(reqCode, args);
    }

    /**
     * Constructor.
     *
     * @param context Context
     */
    public BasePresenter(Context context) {
        mContext = context.getApplicationContext();
        mAccountServiceModel = AccountServiceModel.getInstance();
        mAccountHmiModel = AccountHmiModel.getInstance();

    }

    @Override
    public void callback(int reqCode, boolean success, String extra, String message) {

    }
}
