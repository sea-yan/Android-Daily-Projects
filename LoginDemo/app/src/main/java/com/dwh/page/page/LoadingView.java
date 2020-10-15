/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.page.page;


import android.content.Context;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;

import com.ts.app.account.R;
import com.ts.app.account.presentation.view.base.BaseLayout;

/**
 * LoadingView.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:45
 */
public class LoadingView extends BaseLayout {

    private static final String TAG = "LoadingView";
    private ProgressBar mPb;
    private long mLastClickTime;
    //5s later can close loading view
    private static final long MIN_DISMISS_PERIOD = 5000;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onViewAdded() {
        super.onViewAdded();
        mLastClickTime = SystemClock.uptimeMillis();
    }

    @Override
    protected void initView() {
        mPb = mRootView.findViewById(R.id.pb_loading);
        mRootView.findViewById(R.id.ll_loading).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((SystemClock.uptimeMillis() - mLastClickTime)
                        >= MIN_DISMISS_PERIOD) {
                    mPop.removeLoading();
                }
            }
        });

    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_loading;
    }

    @Override
    protected void initPresenter() {

    }
}