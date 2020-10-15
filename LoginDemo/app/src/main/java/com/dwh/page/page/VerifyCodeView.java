/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.page.page;


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.app.account.R;
import com.ts.app.account.common.constant.Constants;
import com.ts.app.account.model.SendCodeIntervalHelper;
import com.ts.app.account.presentation.listener.PhoneLoginContract;
import com.ts.app.account.presentation.presenter.PhoneLoginPresenter;
import com.ts.app.account.presentation.view.base.BaseLayout;
import com.ts.app.account.presentation.view.customview.CaptchaView;
import com.ts.app.account.presentation.view.customview.KeyboardView;
import com.ts.lib.account.data.AccountData;

/**
 * VerifyCodeView.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:45
 */
public class VerifyCodeView extends BaseLayout<PhoneLoginPresenter>
        implements PhoneLoginContract.IVerifyView, View.OnClickListener {

    private TextView mTvPhone;
    private CaptchaView mCaptcha;
    private TextView mTvError;
    private TextView mTvResend;
    private TextView mTvBack;
    private LoginView mLoginView;
    private LinearLayout mLlError;
    private LinearLayout mLlCb;
    private CheckBox mCb;

    public VerifyCodeView(Context context) {
        super(context);
    }

    public VerifyCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerifyCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyboardView(KeyboardView keyboardView) {
        keyboardView.setKeyboardInputer(mCaptcha);
    }

    public void setLoginView(LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PhoneLoginPresenter(mContext);
        mPresenter.setVerifyView(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_verify_code;
    }

    @Override
    protected void initView() {
        mTvPhone = mRootView.findViewById(R.id.tv_phone);
        mCaptcha = mRootView.findViewById(R.id.captcha);

        mLlError = mRootView.findViewById(R.id.ll_error);
        mTvError = mRootView.findViewById(R.id.tv_error);
        mTvResend = mRootView.findViewById(R.id.tv_resend);
        mTvBack = mRootView.findViewById(R.id.tv_back);

        mLlCb = mRootView.findViewById(R.id.ll_cb);
        mCb = mRootView.findViewById(R.id.cb);

        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
        mTvResend.setOnClickListener(this);
        mTvBack.setOnClickListener(this);
        mCaptcha.setOnEditListener(new CaptchaView.OnEditListener() {
            @Override
            public void onEnd(String text) {
                mPresenter.verifyCode(text);
                mPresenter.setFreeLoginChecked(mCb.isChecked());
            }

            @Override
            public void onEdit(String text) {
                showError(null);
            }
        });
    }

    @Override
    public void onViewAdded() {
        super.onViewAdded();
        if (getArgs() != null && getArgs() instanceof AccountData) {
            final String isNoSecretLogin = ((AccountData) getArgs()).getIsNoSecretLogin();
            mCb.setChecked(Constants.AGREE_NO_SECRET_LOGIN.equals(isNoSecretLogin));
        } else {
            mCb.setChecked(true);
        }
        mPresenter.setFreeLoginChecked(mCb.isChecked());
    }

    @Override
    public void dismiss() {
        super.dismiss();
        SendCodeIntervalHelper.getInstance(mContext).cancelTimer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_resend:
                mPresenter.resendPhone();
                break;
            case R.id.iv_close:
                if (mPop != null) {
                    mPop.dismiss();
                }
                break;
            case R.id.tv_back:
                mLoginView.showMainView();
                break;
            default:
                break;
        }
    }

    private void showError(String error) {
        if (TextUtils.isEmpty(error)) {
            mLlError.setVisibility(GONE);
            mLlCb.setVisibility(VISIBLE);
        } else {
            mLlCb.setVisibility(INVISIBLE);
            mLlError.setVisibility(VISIBLE);
            mTvError.setText(error);
        }
    }

    /**
     * reset view.
     *
     * @param phone phone
     */
    public void reset(String phone) {
        mTvPhone.setText(phone);
        mCaptcha.clear();
        mLlError.setVisibility(GONE);
        SendCodeIntervalHelper.getInstance(mContext).start(mTvResend, phone);
    }

    @Override
    public void verifyCodeResult(boolean success, String resultMsg) {
        if (!success) {
            showError(resultMsg);
        }
    }

    @Override
    public void resendCodeResult(boolean success, String resultMsg) {
        if (success) {
            reset(mTvPhone.getText().toString().trim());
        } else {
            showError(resultMsg);
        }
    }
}