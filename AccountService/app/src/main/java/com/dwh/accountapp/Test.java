package com.dwh.accountapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Test implements Parcelable {
    private String test;

    public Test() { }
    protected Test(Parcel in) {
        test = in.readString();
    }

    public static final Creator<Test> CREATOR = new Creator<Test>() {
        @Override
        public Test createFromParcel(Parcel in) {
            return new Test(in);
        }

        @Override
        public Test[] newArray(int size) {
            return new Test[size];
        }
    };

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(test);
    }

    public void readFromParcel(Parcel dest) {
        test = dest.readString();
    }
}
