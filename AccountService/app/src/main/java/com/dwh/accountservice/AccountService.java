package com.dwh.accountservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AccountService extends Service {

    private static final String TAG = AccountService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }


}
