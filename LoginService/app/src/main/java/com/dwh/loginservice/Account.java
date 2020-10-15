package com.dwh.loginservice;

import android.os.Parcel;
import android.os.Parcelable;

public class Account implements Parcelable {
    private String mPhoneNum;
    private String mPwd;

    public Account(Parcel in) {
        mPhoneNum = in.readString();
        mPwd = in.readString();
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };

    public Account() {

    }

    public String getmPhoneNum() {
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }

    public String getmPwd() {
        return mPwd;
    }

    public void setmPwd(String mPwd) {
        this.mPwd = mPwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPhoneNum);
        parcel.writeString(mPwd);
    }
}
