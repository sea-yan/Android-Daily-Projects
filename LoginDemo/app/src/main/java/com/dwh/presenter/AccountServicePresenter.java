/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.presenter;

import android.content.Context;
import android.content.Intent;

import com.ts.app.account.R;
import com.ts.app.account.common.utilities.ToastUtils;
import com.ts.lib.account.constants.AccountHmi;
import com.ts.lib.account.constants.AccountTypes;

/**
 * AccountServicePresenter.
 *
 * @author: jiangwei
 * @Date: 19-4-8 PM2:38
 */
public class AccountServicePresenter extends BasePresenter {
    private static final String TAG = "AccountServicePresenter";
    private static AccountServicePresenter sInstance;

    private AccountServicePresenter(Context context) {
        super(context);
        mAccountServiceModel.init(mContext);
        mAccountHmiModel.registerCallback(this);
    }

    /**
     * getInstance.
     *
     * @return HomePresenter.
     */
    public static synchronized AccountServicePresenter getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new AccountServicePresenter(context);
        }
        return sInstance;
    }

    /**
     * handleIntent.
     *
     * @param intent intent
     */
    public void handleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        final int intExtra = intent.getIntExtra(AccountHmi.KEY, 0);
        switch (intExtra) {
            case AccountHmi.REQ_SHOW_ACCOUNT_LIST:
                mPop.showAccountList();
                break;
            case AccountHmi.REQ_SHOW_GUIDE:
                mPop.showGuideView();
                break;
            case AccountHmi.REQ_SHOW_LOGIN:
                mPop.showLogin(null);
                break;
            case AccountHmi.REQ_VIN_UNBIND:
                IndexDownloadPresenter.getInstance(mContext).vinUnbind();
                break;
            default:
                break;
        }
    }

    /**
     * checkGearStatus.
     *
     * @return true:can handle account
     */
    public boolean checkGearStatus() {
        boolean status = !AccountTypes.PARAMS_CLOSE
                .equals(mAccountServiceModel.getAccountProperty(AccountTypes.REQ_CHECK_GEAR_STATUS));
        if (!status) {
            ToastUtils.instance(mContext).toast(mContext.getString(R.string.tips_can_show_account));
        }
        return status;
    }
}
