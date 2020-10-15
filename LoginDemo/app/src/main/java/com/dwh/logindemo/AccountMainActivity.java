package com.dwh.logindemo;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.dwh.constant.Constants;
import com.dwh.receiver.AccountBroadCastReceiver;
import com.ts.app.account.model.AccountServiceModel;
import com.ts.app.account.model.OpenAppModel;
import com.dwh.fragment.HomeFragment;
import com.ts.lib.account.constants.AccountHmi;
import com.ts.lib.account.utils.LogUtils;

/**
 * AccountMainActivity.
 *
 * @Author: jiangwei
 * @Date: 18-12-19 PM4:03
 */
public class AccountMainActivity extends AppCompatActivity {
    private static final String TAG = "AccountMainActivity";
    private FragmentManager mFragmentMgr;
    private Fragment mCurrentFrg;
    private AccountBroadCastReceiver mReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.no_slide, R.anim.no_slide);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        Log.d(TAG, "onCreate*******");
        setContentView(R.layout.activity_main);
        showFragment(Constants.UI_ID_CENTER, null);
        findViewById(R.id.tv_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AccountServiceModel.getInstance().checkGearStatus()) {
                    return;
                }
                OpenAppModel.getInstance(AccountMainActivity.this).openPlanet();
            }
        });
        registBroadcast();
        startForegroundService(new Intent(this, AccountHmiService.class));
    }

    private void registBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mReceiver = new AccountBroadCastReceiver();
        mReceiver.setBroadcastListener(new AccountBroadCastReceiver.BroadcastListener() {
            @Override
            public void netContent(boolean isConnected) {
                ((HomeFragment) mCurrentFrg).netChanged();
            }
        });
        registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_slide, R.anim.no_slide);
    }

    /**
     * show Fragment by type.
     *
     * @param type   String. Constants.UI_ID_CENTER
     * @param bundle Bundle
     */
    public void showFragment(String type, Bundle bundle) {
        if (mFragmentMgr == null) {
            mFragmentMgr = getFragmentManager();
        }
        FragmentTransaction trx = mFragmentMgr.beginTransaction();
        Fragment newFragment = mFragmentMgr.findFragmentByTag(type);
        if (newFragment == null) {
            switch (type) {
                case Constants.UI_ID_CENTER:
                    newFragment = new HomeFragment();
                    break;
                default:
                    break;
            }
        }

        if (newFragment == null) {
            return;
        }

        if (bundle != null) {
            newFragment.setArguments(bundle);
        }
        if (mCurrentFrg != null) {
            trx.hide(mCurrentFrg);
        }
        if (!newFragment.isAdded()) {
            trx.add(R.id.fl_container, newFragment, type).commitAllowingStateLoss();
        } else {
            trx.show(newFragment).commitAllowingStateLoss();
        }
        mCurrentFrg = newFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }

}