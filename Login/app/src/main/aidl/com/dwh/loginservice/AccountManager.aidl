// AccountManager.aidl
package com.dwh.login;

// Declare any non-default types here with import statements
import com.dwh.login.Account;
import com.dwh.login.AccountCallback;

interface AccountManager {

    String getUser(String mPhoneNum,String mPwd);
    String setUser(String mPhoneNum,String mPwd);
    void registerCallback(AccountCallback accountcallback);
    void unregisterCallback(AccountCallback accountcallback);
}