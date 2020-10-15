/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;



/**
 * AccountBroadCastReceiver.
 *
 * @author jiangwei
 */
public class AccountBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "AccountBroadCastReceiver";
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";
    private BroadcastListener mBroadcastListener;

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "onReceive***action" + action);
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                NetworkInfo info
                        = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI
                                || info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            mBroadcastListener.netContent(true);
                        }
                    } else {
                        mBroadcastListener.netContent(false);
                    }
                }
                break;

            case Intent.ACTION_CLOSE_SYSTEM_DIALOGS:
                // android.intent.action.CLOSE_SYSTEM_DIALOGS
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                Log.d(TAG, "reason: " + reason);

                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    // short click Home
                }
                break;
            default:
                break;
        }
    }

    /**
     * set BroadcastListener.
     *
     * @param broadcastListener BroadcastListener
     */
    public void setBroadcastListener(BroadcastListener broadcastListener) {
        this.mBroadcastListener = broadcastListener;
    }

    public interface BroadcastListener {
        void netContent(boolean isConnected);
    }
}