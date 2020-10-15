package com.dwh.loginservice;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

public class LoginService extends Service {

    private static final int RECEIVE_MESSAGE_CODE = 0x0001;
    private static final int SEND_MESSAGE_CODE = 0x0002;
    private Messenger clientMessenger = null;
    private Messenger loginMessger = new Messenger(new ServiceHandler());

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("LoginService", "MyServivce -> onBind");
        return loginMessger.getBinder();
    }


    private class ServiceHandler extends Handler {


        @Override
        public void handleMessage(Message msg) {
            Log.i("LoginService", "ServiceHandler -> handleMessage");
            if (msg.what == RECEIVE_MESSAGE_CODE) {
                Bundle data = msg.getData();
                if (data != null) {
                    String str = data.getString("msg");
                    Log.i("LoginService", "MyService收到客户端如下信息: " + str);
                }
                //通过Message的replyTo获取到客户端自身的Messenger
                //Service可以通过它向客户端发送消息
                clientMessenger = msg.replyTo;
                if (clientMessenger != null) {
                    Log.i("LoginService", "MyService向客户端回信");
                    Message msgToClient = Message.obtain();
                    msgToClient.what = SEND_MESSAGE_CODE;
                    //可以通过Bundle发送跨进程的信息
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "你好，login客户端，我是MyService");
                    msgToClient.setData(bundle);
                    try {
                        clientMessenger.send(msgToClient);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Log.e("LoginService", "MyService向客户端发送信息失败: " + e.getMessage());
                    }
                }
            }
        }


        public void onCreate() {
            Log.i("LoginService", "MyService -> onCreate");

        }





        public void onDestroy() {
            Log.i("LoginService", "MyService -> onDestroy");
            clientMessenger = null;

        }

    }
}