/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.model.listener;

/**
 * IStringCallback.
 *
 * @author: jiangwei
 * @Date: 19-2-27 PM1:56
 */
public interface IStringCallback {
    /**
     * callback.
     *
     * @param reqCode reqCode
     * @param success success
     * @param extra   extra
     * @param message message
     */
    void callback(int reqCode, boolean success, String extra, String message);
}
