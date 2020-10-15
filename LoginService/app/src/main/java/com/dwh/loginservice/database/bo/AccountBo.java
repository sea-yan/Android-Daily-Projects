/**
 * Copyright (c) 2018-2019
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with THUNDERSOFT in writing by applicable law.
 */

package com.dwh.loginservice.database.bo;

/**
 * AccountBo.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM1:05
 */
public class AccountBo {
    private String keyId;
    private String phonenum;
    private String pwd;


    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public AccountBo() {
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

}