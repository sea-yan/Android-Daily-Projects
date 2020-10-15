package com.dwh.accountapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.dwh.accountapp.AidlCallback;

public class MainActivity extends Activity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    private Button mButton;
    private boolean connected;
    private AidlManager aidlManager;
    Test test = new Test();
    AidlCallback callBack = new AidlCallback.Stub() {
        @Override
        public void onClick(String msg) throws RemoteException {
            Log.e(TAG, "callback: Button");
        }

    };

    //    private AidlManager aidlManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                test.setTest("非常高会尽快的手机号发货的舒服撒技术积分和看你姐夫");
                Log.e(TAG, "onClick: Button" + test.getTest());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("This is Resume","this is resume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("This is Pause","this is pause");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.e("This is Stop","this is stop");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (connected) {
//            unbindService(serviceConnection);
//        }
        Log.e("This is Destroy","this is destroy");
    }
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
                aidlManager = AidlManager.Stub.asInterface(service);
            connected = true;
            Log.e("MainActivity", "****connect success****");
            try {
                if (callBack != null){
                    Log.e("MainActivity", "callback success");
                    aidlManager.registerCallback(callBack);
                }else {
                    Log.e("MainActivity", "callback failed");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            Log.e("MainActivity", "****connect falied****");
        }
    };
    
    private void bindService() {
        Log.e("MainActivity", "using bindService(服务端客户端建立连接)");
        Intent intent = new Intent();
        intent.setPackage("com.dwh.AccountService");
        intent.setAction("com.dwh.AccountService");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

}