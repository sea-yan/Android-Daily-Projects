/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.domain;

/**
 * PhoneLoginBean.
 *
 * @author: jiangwei
 * @Date: 19-4-4 PM4:50
 */
public class PhoneLoginBean {
    private String code;
    private String freeLogin;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFreeLogin() {
        return freeLogin;
    }

    public void setFreeLogin(String freeLogin) {
        this.freeLogin = freeLogin;
    }
}
