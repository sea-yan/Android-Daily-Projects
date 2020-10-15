/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.model;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.dwh.model.listener.IStringCallback;
import com.dwh.AccountHmiManager;
import com.dwh.constants.AccountTypes;
import com.dwh.data.BaseCallbackBean;
import com.dwh.listener.IBindCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountHmiModel.
 *
 * @author jiangwei
 * @version 1.0
 */
public class AccountHmiModel implements IBindCallback {

    private static final String TAG = "AccountHmiModel";

    private static volatile AccountHmiModel sInstance;
    private AccountHmiManager mHmiManager;
    private List<IStringCallback> mCallBacks = new ArrayList<>();
    private static final int WHAT_HANDLE_CALLBACK = 101;
    private static final int WHAT_REGISTER_CALLBACK = 102;
    private static final int WHAT_UNREGISTER_CALLBACK = 103;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_HANDLE_CALLBACK:
                    int reqCode = msg.arg1;
                    BaseCallbackBean response = msg.obj == null ? null : (BaseCallbackBean) msg.obj;
                    boolean success = true;
                    String extra = "";
                    String message = "";
                    if (response != null) {
                        success = AccountTypes.CODE_SUCCESS == response.getCode();
                        extra = response.getData();
                        message = response.getMessage();
                    }
                    for (IStringCallback callback : mCallBacks) {
                        callback.callback(reqCode, success, extra, message);
                    }
                    break;
                case WHAT_REGISTER_CALLBACK:
                    IStringCallback callback = msg.obj == null ? null : (IStringCallback) msg.obj;
                    if (callback != null && !mCallBacks.contains(callback)) {
                        mCallBacks.add(callback);
                    }
                    break;
                case WHAT_UNREGISTER_CALLBACK:
                    IStringCallback removeCallback = msg.obj == null ? null : (IStringCallback) msg.obj;
                    if (removeCallback != null && mCallBacks.contains(removeCallback)) {
                        mCallBacks.remove(removeCallback);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private AccountHmiModel() {
    }

    /**
     * getInstance.
     *
     * @return AccountServiceModel.
     */
    public static synchronized AccountHmiModel getInstance() {
        if (sInstance == null) {
            sInstance = new AccountHmiModel();
        }
        return sInstance;
    }

    /**
     * init HmiManager.
     *
     * @param manager AccountHmiManager
     */
    public void initHmiManager(AccountHmiManager manager) {
        this.mHmiManager = manager;
        mHmiManager.setBindCallback(this);
    }

    /**
     * request.
     *
     * @param reqCode reqCode
     * @param args    args
     */
    public void request(int reqCode, String args) {
        if (mHmiManager != null) {
            mHmiManager.request(reqCode, args);
        }
    }

    /**
     * registerCallback.
     *
     * @param callback callback.
     */
    public void registerCallback(IStringCallback callback) {
        final Message message = Message.obtain();
        message.what = WHAT_REGISTER_CALLBACK;
        message.obj = callback;
        mHandler.sendMessage(message);
    }

    /**
     * removeCallback.
     *
     * @param callback callback.
     */
    public void removeCallback(IStringCallback callback) {
        final Message message = Message.obtain();
        message.what = WHAT_UNREGISTER_CALLBACK;
        message.obj = callback;
        mHandler.sendMessage(message);
    }

    @Override
    public void callback(final int reqCode, BaseCallbackBean response) {
        Message message = Message.obtain();
        message.what = WHAT_HANDLE_CALLBACK;
        message.arg1 = reqCode;
        message.obj = response;
        mHandler.sendMessage(message);
    }
}