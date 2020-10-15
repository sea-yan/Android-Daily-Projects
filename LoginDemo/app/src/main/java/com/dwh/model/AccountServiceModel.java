/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.model;

import android.content.Context;

import com.ts.app.account.R;
import com.ts.app.account.common.utilities.ToastUtils;
import com.ts.lib.account.AccountHmiManager;
import com.ts.lib.account.AccountServiceManager;
import com.ts.lib.account.BaseCommonManager;
import com.ts.lib.account.constants.AccountHmi;
import com.ts.lib.account.constants.AccountTypes;
import com.ts.lib.account.data.AccountData;
import com.ts.lib.account.listener.IAccountChangeListener;
import com.ts.lib.account.listener.IConnectStatusListener;

import java.util.ArrayList;
import java.util.List;

/**
 * AccountServiceModel.
 *
 * @author jiangwei
 * @version 1.0
 */
public class AccountServiceModel implements IConnectStatusListener {

    private static final String TAG = "AccountServiceModel";

    private static volatile AccountServiceModel sInstance;
    private AccountServiceManager mCarAccountService;
    private Context mContext;
    private boolean mIsInited;
    private List<IConnectStatusListener> mConnectStatusListeners = new ArrayList<>();

    private AccountServiceModel() {
    }

    /**
     * getInstance.
     *
     * @return AccountServiceModel.
     */
    public static synchronized AccountServiceModel getInstance() {
        if (sInstance == null) {
            sInstance = new AccountServiceModel();
        }
        return sInstance;
    }

    /**
     * init.
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        mCarAccountService = AccountServiceManager.getInstance(mContext);
        mCarAccountService.setConnectStatusListener(this);
        mCarAccountService.init();
    }

    @Override
    public void connectStatus(boolean success) {
        mIsInited = success;
        if (mIsInited) {
            final BaseCommonManager accountHmiManager = mCarAccountService.getManager(AccountHmi.NAME);
            if (accountHmiManager != null) {
                AccountHmiModel.getInstance().initHmiManager((AccountHmiManager) accountHmiManager);
            }
            for (IConnectStatusListener listener : mConnectStatusListeners) {
                listener.connectStatus(mIsInited);
            }
        }
    }

    /**
     * check Gear Status.
     *
     * @return true:can handle account
     */
    public boolean checkGearStatus() {
        boolean status = !AccountTypes.PARAMS_CLOSE
                .equals(mCarAccountService.getAccountProperty(AccountTypes.REQ_CHECK_GEAR_STATUS));
        if (!status) {
            ToastUtils.instance(mContext).toast(mContext.getString(R.string.tips_can_show_account));
        }
        return status;
    }

    /**
     * get Vehicle Active status.
     *
     * @return true:active
     */
    public boolean isVehicleActive() {
        String active = mCarAccountService.getAccountProperty(AccountTypes.REQ_SWITCH_ACTIVE_STATUS);
        return AccountTypes.PARAMS_OPEN.equals(active);
    }

    /**
     * get Account Property.
     *
     * @param reqCode reqCode
     * @return property
     */
    public String getAccountProperty(int reqCode) {
        if (mCarAccountService != null) {
            return mCarAccountService.getAccountProperty(reqCode);
        }
        return null;
    }

    /**
     * get Current Account.
     *
     * @return AccountData
     */
    public AccountData getCurrentAccount() {
        if (mCarAccountService != null) {
            return mCarAccountService.getCurrentAccount();
        }
        return null;
    }

    /**
     * logout Account.
     *
     * @param mobile mobile
     */
    public void logoutAccount(String mobile) {
        if (mCarAccountService != null) {
            mCarAccountService.logoutAccount(mobile);
        }
    }

    /**
     * removeAccountChangeListener.
     *
     * @param listener IAccountChangeListener
     */
    public void removeAccountChangeListener(IAccountChangeListener listener) {
        if (mCarAccountService != null) {
            mCarAccountService.removeAccountChangeListener(listener);
        }
    }

    /**
     * addAccountChangeListener.
     *
     * @param listener IAccountChangeListener
     */
    public void addAccountChangeListener(IAccountChangeListener listener) {
        if (mCarAccountService != null) {
            mCarAccountService.setAccountChangeListener(listener);
        }
    }

    /**
     * addAccountChangeListener.
     *
     * @param listener IConnectStatusListener
     */
    public void setConnectListener(IConnectStatusListener listener) {
        if (!mConnectStatusListeners.contains(listener)) {
            mConnectStatusListeners.add(listener);
        }
        if (mCarAccountService != null && mCarAccountService.isServiceConnected()) {
            listener.connectStatus(true);
        }
    }

    /**
     * removeConnectListener.
     *
     * @param listener IConnectStatusListener
     */
    public void removeConnectListener(IConnectStatusListener listener) {
        if (mConnectStatusListeners.contains(listener)) {
            mConnectStatusListeners.remove(listener);
        }
    }
}