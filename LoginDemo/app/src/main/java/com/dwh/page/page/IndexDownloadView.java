/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.page.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ts.app.account.R;
import com.ts.app.account.common.utilities.LongClickUtils;
import com.ts.app.account.common.utilities.ToastUtils;
import com.ts.app.account.presentation.listener.IndexDownloadContract;
import com.ts.app.account.presentation.presenter.IndexDownloadPresenter;
import com.ts.app.account.presentation.view.base.BaseLayout;

/**
 * IndexDownloadFragment.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:46
 */
public class IndexDownloadView extends BaseLayout<IndexDownloadPresenter> implements
        IndexDownloadContract.IView, View.OnClickListener {

    private ImageView mIvQr;
    private ProgressBar mPbLoading;
    private ImageView mIvRefresh;
    private FrameLayout mFlQrDefault;
    private ImageView mIvClose;
    private static final long DELAY_TIMES = 10000;

    public IndexDownloadView(Context context) {
        super(context);
    }

    public IndexDownloadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexDownloadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPresenter() {
        mPresenter = IndexDownloadPresenter.getInstance(mContext);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_index_download;
    }

    @Override
    public void onViewAdded() {
        super.onViewAdded();
        mFlQrDefault.setVisibility(VISIBLE);
        mPbLoading.setVisibility(VISIBLE);
        mIvRefresh.setVisibility(GONE);
        if (mPresenter.isFactory()) {
            mIvClose.setVisibility(VISIBLE);
        } else {
            mIvClose.setVisibility(GONE);
        }
        mPresenter.getQrCode();
    }

    @Override
    protected void initView() {
        mIvClose = mRootView.findViewById(R.id.iv_close);
        mIvQr = mRootView.findViewById(R.id.iv_qr_img);
        mFlQrDefault = mRootView.findViewById(R.id.fl_qr_default);
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh);
        mPbLoading = mRootView.findViewById(R.id.pb_loading);

        findViewById(R.id.iv_qr_img).setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
        LongClickUtils.setLongClick(mRootView.findViewById(R.id.tv_title),
                DELAY_TIMES, new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mPresenter.dismiss();
                        return false;
                    }
                });
    }

    @Override
    public void loadQrImg(Bitmap bitmap) {
        mFlQrDefault.setVisibility(GONE);
        mIvRefresh.setVisibility(GONE);
        mPbLoading.setVisibility(GONE);
        mIvQr.setImageBitmap(bitmap);
    }

    @Override
    public void onGetQrFail(String message) {
        mFlQrDefault.setVisibility(VISIBLE);
        mIvRefresh.setVisibility(VISIBLE);
        mPbLoading.setVisibility(GONE);
        ToastUtils.instance(mContext).toast(message);
    }

    @Override
    public void onQueryQrFail(String message) {
        ToastUtils.instance(mContext).toast(message);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_refresh:
            case R.id.iv_qr_img:
                mPresenter.getQrCode();
                break;
            case R.id.iv_close:
                mPresenter.dismiss();
                break;
            default:
                break;
        }
    }
}
