// AidlManager.aidl
package com.dwh.accountapp;

// Declare any non-default types here with import statements
import com.dwh.accountapp.Test;
import com.dwh.accountapp.AidlCallback;
interface AidlManager {

    void getTest(String test);
    void setTest(String test);

    void registerCallback(AidlCallback aidlcallback);
    void unregisterCallback(AidlCallback aidlcallback);
}
