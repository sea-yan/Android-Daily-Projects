package com.dwh.accountservice;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dwh.accountapp.Test;

import java.util.concurrent.CopyOnWriteArrayList;

import com.dwh.accountapp.*;
public class AccountService extends Service {

    private static final String TAG = AccountService.class.getSimpleName();
    private static RemoteCallbackList<AidlCallback> callbackList;
    private CopyOnWriteArrayList<String> mList;

    public AccountService() {
    }

    private void init() {
        mList = new CopyOnWriteArrayList<>();
        callbackList = new RemoteCallbackList<>();
        Log.e(TAG, "init");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG,"create service");
        init();
    }

    private final AidlManager.Stub stub = new AidlManager.Stub() {


        @Override
        public void getTest(String test) throws RemoteException {
            Log.e(TAG, "getTest(String test)");
            mList.add("test");
        }

        @Override
        public void setTest(String test) throws RemoteException {
            Log.e(TAG, "setTest(String test)");
            mList.add("13415646");
        }

        @Override
        public void registerCallback(AidlCallback bc) throws RemoteException {
            //获取回调对象
            if (bc == null){
                return;
            }
            Log.e(TAG, "using Service registerCallback function");
            callbackList.register(bc);
        }
        @Override
        public void unregisterCallback(AidlCallback bc) throws RemoteException {
            //注销回调对象
            if (bc == null){
                return;
            }
            Log.e(TAG, "using Service unregisterCallback function");
            callbackList.unregister(bc);
        }

    };
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    private void dispatchResult (boolean result, String msg) throws RemoteException {
        int length = callbackList.beginBroadcast();
        for (int i = 0; i < length; i++) {
            AidlCallback callback = callbackList.getBroadcastItem(i);

                    callback.onClick(msg);

        }
        callbackList.finishBroadcast();
    }

}
