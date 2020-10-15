/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.logindemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.IBinder;

import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.dwh.presenter.AccountServicePresenter;



/**
 * AccountHmiService.
 *
 * @author: jiangwei
 * @Date: 19-1-7 pm 3:54
 */
public class AccountHmiService extends Service {

    private static final String TAG = "AccountHmiService";
    private static final int ID = 1;
    private static final String ID_CHANNEL = "11";
    private static final String NAME_CHANNEL = "AccountHmiService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate***************");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            final NotificationChannel channel = new NotificationChannel(
                    ID_CHANNEL, NAME_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, ID_CHANNEL);
            startForeground(ID, builder.build());
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AccountServicePresenter.getInstance(this).handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy*****************");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
