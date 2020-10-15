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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ts.app.account.R;
import com.ts.app.account.common.constant.Constants;
import com.ts.app.account.common.utilities.ToastUtils;
import com.ts.app.account.presentation.listener.QrLoginContract;
import com.ts.app.account.presentation.presenter.QrLoginPresenter;
import com.ts.app.account.presentation.view.base.BaseLayout;
import com.ts.lib.account.data.AccountData;

/**
 * LoginFragment.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:45
 */
public class QrLoginView extends BaseLayout<QrLoginPresenter> implements QrLoginContract.IView, View.OnClickListener {

    private static final String TAG = "QrLoginView";
    private ImageView mIvQrImg;
    private CheckBox mCb;
    private ProgressBar mPbLoading;
    private ImageView mIvRefresh;
    private FrameLayout mFlQrDefault;
    private TextView mTvRegister;
    private AccountData mAccount;

    public QrLoginView(Context context) {
        super(context);
    }

    public QrLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public QrLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPresenter() {
        mPresenter = new QrLoginPresenter(getContext(), this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_qr_login;
    }

    @Override
    protected void initView() {
        mIvQrImg = mRootView.findViewById(R.id.iv_qr);
        mFlQrDefault = mRootView.findViewById(R.id.fl_qr_default);
        mIvRefresh = mRootView.findViewById(R.id.iv_refresh);
        mPbLoading = mRootView.findViewById(R.id.pb_loading);
        mTvRegister = mRootView.findViewById(R.id.tv_register);
        mCb = mRootView.findViewById(R.id.cb);

        mTvRegister.setOnClickListener(this);
        mIvRefresh.setOnClickListener(this);
        mIvQrImg.setOnClickListener(this);
        mCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPresenter.setFreeLoginChecked(mCb.isChecked());
            }
        });
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
    public void onViewAdded() {
        super.onViewAdded();
        if (getArgs() != null && getArgs() instanceof AccountData) {
            mAccount = (AccountData) getArgs();
            String isNoSecretLogin = ((AccountData) getArgs()).getIsNoSecretLogin();
            boolean canFreeLogin = Constants.AGREE_NO_SECRET_LOGIN.equals(isNoSecretLogin);
            mCb.setChecked(canFreeLogin);
            mPresenter.setFreeLoginChecked(canFreeLogin);
        } else {
            mAccount = null;
        }
    }

    /**
     * switch Login tab.
     *
     * @param isQrTab true:qr login
     */
    public void switchLoginTab(boolean isQrTab) {
        mFlQrDefault.setVisibility(VISIBLE);
        mPbLoading.setVisibility(VISIBLE);
        mIvRefresh.setVisibility(GONE);
        if (isQrTab) {
            mPresenter.getQrImg();
        } else {
            mPresenter.cancelQrLogin();
        }
    }

    @Override
    public void loadQrImg(Bitmap bitmap) {
        mFlQrDefault.setVisibility(GONE);
        mPbLoading.setVisibility(GONE);
        mIvRefresh.setVisibility(GONE);
        mIvQrImg.setImageBitmap(bitmap);
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
            case R.id.iv_qr:
                mPresenter.updateQrImg();
                break;
            case R.id.tv_register:
                mPop.showRegisterView();
                break;
            default:
                break;
        }
    }
}
