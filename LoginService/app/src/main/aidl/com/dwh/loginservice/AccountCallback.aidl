// AccountCallback.aidl
package com.dwh.login;

// Declare any non-default types here with import statements
import com.dwh.login.Account;

interface AccountCallback {

    void onSuccess(String msg);
    void onFailed(String msg);
}
