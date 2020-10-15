/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.page.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ts.app.account.R;
import com.ts.app.account.common.utilities.ToastUtils;
import com.ts.app.account.presentation.listener.QrLoginContract;
import com.ts.app.account.presentation.presenter.QrLoginPresenter;
import com.ts.app.account.presentation.view.base.BaseLayout;

/**
 * RegisterView.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:46
 */
public class RegisterView extends BaseLayout<QrLoginPresenter> implements QrLoginContract.IView, View.OnClickListener {

    private ImageView mIvQr;
    private ProgressBar mPbLoading;
    private ImageView mIvRefresh;
    private FrameLayout mFlQrDefault;
    private TextView mTvClose;

    public RegisterView(Context context) {
        super(context);
    }

    public RegisterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RegisterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new QrLoginPresenter(mContext, this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_register;
    }

    @Override
    protected void initView() {
        mIvQr = mRootView.findViewById(R.id.iv_qr_img);
        mFlQrDefault = mRootView.findViewById(R.id.fl_qr_default);
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh);
        mPbLoading = mRootView.findViewById(R.id.pb_loading);
        mTvClose = mRootView.findViewById(R.id.tv_close);

        findViewById(R.id.iv_qr_img).setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mTvClose.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.cancelQrLogin();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFlQrDefault.setVisibility(VISIBLE);
        mPbLoading.setVisibility(VISIBLE);
        mIvRefresh.setVisibility(GONE);
        mPresenter.getQrImg();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mPresenter.release();
    }

    @Override
    public void loadQrImg(Bitmap bitmap) {
        mFlQrDefault.setVisibility(GONE);
        mIvRefresh.setVisibility(GONE);
        mPbLoading.setVisibility(GONE);
        mIvQr.setImageBitmap(bitmap);
    }

    @Override
    public void onFail(String errorMsg) {
        mFlQrDefault.setVisibility(VISIBLE);
        mIvRefresh.setVisibility(VISIBLE);
        mPbLoading.setVisibility(GONE);
        ToastUtils.instance(mContext).toast(errorMsg);
    }

    @Override
    public void loginSuccess(boolean isFirstLogin) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
            case R.id.iv_qr_img:
                mPresenter.updateQrImg();
                break;
            case R.id.iv_close:
            case R.id.tv_close:
                mPop.onBackPress();
                break;
            default:
                break;
        }
    }
}
