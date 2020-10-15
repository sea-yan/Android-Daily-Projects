/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * MobileBean bean .
 *
 * @author cuibowen
 * @version 1.0
 */
public class MobileBean implements Parcelable {
    private Long id;
    private int code;
    private String msg;
    private String simCard;
    private String iccid;
    private String totalFlow;
    private String usedFlow;
    private String restFlow;
    private long expireDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSimCard() {
        return simCard;
    }

    public void setSimCard(String simCard) {
        this.simCard = simCard;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(String totalFlow) {
        this.totalFlow = totalFlow;
    }

    public String getUsedFlow() {
        return usedFlow;
    }

    public void setUsedFlow(String usedFlow) {
        this.usedFlow = usedFlow;
    }

    public String getRestFlow() {
        return restFlow;
    }

    public void setRestFlow(String restFlow) {
        this.restFlow = restFlow;
    }

    public long getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(long expireDate) {
        this.expireDate = expireDate;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(code);
        dest.writeString(msg);
        dest.writeString(simCard);
        dest.writeString(iccid);
        dest.writeString(totalFlow);
        dest.writeString(usedFlow);
        dest.writeString(restFlow);
        dest.writeLong(expireDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MobileBean> CREATOR = new Creator<MobileBean>() {
        @Override
        public MobileBean createFromParcel(Parcel in) {
            return new MobileBean();
        }

        @Override
        public MobileBean[] newArray(int size) {
            return new MobileBean[size];
        }
    };
}
