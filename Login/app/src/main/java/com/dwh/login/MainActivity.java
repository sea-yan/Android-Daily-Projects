package com.dwh.login;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dwh.login.R;
import com.dwh.loginservice.Account;

public class MainActivity extends Activity implements View.OnClickListener{

    private String TAG = "MainActivity";
    private Button mLogin;
    private EditText mPhoneNum;
    private EditText mPwd;

    private static final int SEND_MESSAGE_CODE = 0x0001;
    private static final int RECEIVE_MESSAGE_CODE = 0x0002;
    private boolean isBound = false;
    private Context mContext;
    private android.accounts.AccountManager AccountManager;
    private boolean connected;
    //用于启动MyService的Intent对应的action
    private final String SERVICE_ACTION = "com.dwh.Account";

    //serviceMessenger表示的是Service端的Messenger，其内部指向了MyService的ServiceHandler实例
    //可以用serviceMessenger向MyService发送消息
    private Messenger loginMessenger = null;

    //clientMessenger是客户端自身的Messenger，内部指向了ClientHandler的实例
    //MyService可以通过Message的replyTo得到clientMessenger，从而MyService可以向客户端发送消息，
    //并由ClientHandler接收并处理来自于Service的消息
    private Messenger clientMessenger = new Messenger(new ClientHandler());

    private class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.i("MainActivity", "ClientHandler -> handleMessage");
            if (msg.what == RECEIVE_MESSAGE_CODE) {
                Bundle data = msg.getData();
                if (data != null) {
                    String str = data.getString("msg");
                    Log.i("MainActivity", "客户端收到Service的消息: " + str);
                }
            }
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            //客户端与Service建立连接
            Log.i("MainActivity", "客户端 onServiceConnected");

            //我们可以通过从Service的onBind方法中返回的IBinder初始化一个指向Service端的Messenger
            loginMessenger = new Messenger(binder);
            isBound = true;

            Message msg = Message.obtain();
            msg.what = SEND_MESSAGE_CODE;
            //此处跨进程Message通信不能将msg.obj设置为non-Parcelable的对象，应该使用Bundle
            //msg.obj = "你好，MyService，我是客户端";
            Bundle data = new Bundle();
            data.putString("msg", "你好，MyService，我是login客户端");
            msg.setData(data);
            //需要将Message的replyTo设置为客户端的clientMessenger，
            //以便Service可以通过它向客户端发送消息
            msg.replyTo = clientMessenger;
            try {
                Log.i("MainActivity", "客户端向service发送信息");
                loginMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.i("MainActivity", "客户端向service发送消息失败: " + e.getMessage());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //客户端与Service失去连接

            loginMessenger = null;
            isBound = false;
            Log.i("MainActivity", "客户端 onServiceDisconnected");
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_login) {
            //单击了bindService按钮
            if (!isBound) {
                Intent intent = new Intent();
                intent.setAction(SERVICE_ACTION);
                intent.addCategory(Intent.CATEGORY_DEFAULT);

                PackageManager pm = getPackageManager();
                //我们先通过一个隐式的Intent获取可能会被启动的Service的信息
                ResolveInfo info = pm.resolveService(intent, 0);

                if (info != null) {
                    //如果ResolveInfo不为空，说明我们能通过上面隐式的Intent找到对应的Service
                    //我们可以获取将要启动的Service的package信息以及类型
                    String packageName = info.serviceInfo.packageName;
                    String serviceNmae = info.serviceInfo.name;
                    //然后我们需要将根据得到的Service的包名和类名，构建一个ComponentName
                    //从而设置intent要启动的具体的组件信息，这样intent就从隐式变成了一个显式的intent
                    //之所以大费周折将其从隐式转换为显式intent，是因为从Android 5.0 Lollipop开始，
                    //Android不再支持通过通过隐式的intent启动Service，只能通过显式intent的方式启动Service
                    //在Android 5.0 Lollipop之前的版本倒是可以通过隐式intent启动Service
                    ComponentName componentName = new ComponentName(packageName, serviceNmae);
                    intent.setComponent(componentName);
                    try {
                        Log.i("MainActivity", "客户端调用bindService方法");
                        bindService(intent, conn, Context.BIND_AUTO_CREATE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("MainActivity", e.getMessage());
                    }
                }
            }
        } else if (v.getId() == R.id.tv_captcha_login) {
            //单击了unbindService按钮
            if (isBound) {
                Log.i("MainActivity", "客户端调用unbindService方法");
                unbindService(conn);
            }
        }
            showLogin();
    }

//    AccountCallback callback = new AccountCallback.Stub() {
//
//        @Override
//        public void onSuccess(String msg) throws RemoteException {
//            Log.e(TAG, "从服务端接收： " + msg);
//        }
//
//        @Override
//        public void onFailed(String msg) throws RemoteException {
//            Log.e(TAG, "从服务端接收：" + msg);
//        }
//    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();
        /*mPhoneNum = this.findViewById(R.id.tv_phone);*/
        mLogin = findViewById(R.id.btn_login);
        mLogin.setOnClickListener(this);


    }

    private void showLogin() {
        final Dialog mLogin = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.layout_phone_login,null);
        mLogin.setContentView(view);
        ImageView mImageView1 = mLogin.findViewById(R.id.image);
        mPhoneNum = mLogin.findViewById(R.id.tv_phone);
        mPwd = mLogin.findViewById(R.id.pwd);

        TextView mTextView1 = mLogin.findViewById(R.id.tv_captcha_login);
        mTextView1.setOnClickListener(this);
        TextView mTextView2 = mLogin.findViewById(R.id.tv_register);
        TextView mTextView3 = mLogin.findViewById(R.id.tittle);
        mLogin.show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("MainActivity","onRestart!!");
    }
}