/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.page.page;


import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ts.app.account.R;
import com.ts.app.account.common.utilities.CommonUtils;
import com.ts.app.account.presentation.listener.PhoneLoginContract;
import com.ts.app.account.presentation.presenter.PhoneLoginPresenter;
import com.ts.app.account.presentation.view.base.BaseLayout;
import com.ts.app.account.presentation.view.customview.KeyboardView;
import com.ts.app.account.presentation.view.customview.NoSoftEditText;
import com.ts.lib.account.data.AccountData;

/**
 * LoginFragment.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:45
 */
public class PhoneLoginView extends BaseLayout<PhoneLoginPresenter> implements
        View.OnClickListener, PhoneLoginContract.IView, KeyboardView.KeyboardInputer {

    public static final int MAX_PHONE_LENGTH = 11;//max phone length
    private NoSoftEditText mTvPhone;
    private KeyboardView mKeyboardView;
    private LoginView mLoginView;
    private View mLlError;
    private TextView mTvError;
    private static final int HINT_PHONE_SIZE = 24;//textview hint size
    private TextView mTvCaptchaLogin;
    private TextView mTvPwdLogin;
    private String mPhoneNum;//current phone number
    private View mLlPhone;

    public PhoneLoginView(Context context) {
        super(context);
    }

    public PhoneLoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhoneLoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyboardView(KeyboardView keyboardView) {
        this.mKeyboardView = keyboardView;
        mKeyboardView.setKeyboardInputer(this);
    }

    public void setLoginView(LoginView loginView) {
        this.mLoginView = loginView;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new PhoneLoginPresenter(getContext());
        mPresenter.setPhoneLoginView(this);
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_phone_login;
    }

    @Override
    protected void initView() {
        mLlPhone = mRootView.findViewById(R.id.ll_phone_input);
        mTvPhone = mRootView.findViewById(R.id.tv_phone);

        mLlError = mRootView.findViewById(R.id.ll_error);
        mTvError = mRootView.findViewById(R.id.tv_error);

        mTvCaptchaLogin = mRootView.findViewById(R.id.tv_captcha_login);
        mTvPwdLogin = mRootView.findViewById(R.id.tv_pwd_login);

        findViewById(R.id.tv_register).setOnClickListener(this);
        mTvCaptchaLogin.setOnClickListener(this);
        mTvPwdLogin.setOnClickListener(this);
        checkCanNext();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    private void loadData() {
        String hint = mContext.getString(R.string.input_phone_num);
        SpannableString ss = new SpannableString(hint);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(HINT_PHONE_SIZE, true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvPhone.setHint(new SpannedString(ss));
        showError(null);
    }

    @Override
    public void onViewAdded() {
        super.onViewAdded();
        loadData();
    }

    @Override
    public void initArgs(Object obj) {
        super.initArgs(obj);
        if (getArgs() != null && getArgs() instanceof AccountData) {
            AccountData accountBo = (AccountData) getArgs();
            mTvPhone.setText(accountBo.getMobile());
        } else {
            mTvPhone.setText("");
        }
        checkCanNext();
        if (mKeyboardView != null) {
            mKeyboardView.updateDeleteLight();
        }
    }

    @Override
    public void getPhoneCodeSuccess() {
        mLoginView.showVerifyCodeView(mPhoneNum);
    }

    @Override
    public void onFail(String msg) {
        showError(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_captcha_login:
                if (!checkPhoneNum()) {
                    return;
                }
                mPresenter.getPhoneCode(mPhoneNum);
                break;
            case R.id.tv_register:
                mPop.showRegisterView();
                break;
            default:
                break;
        }
    }

    private boolean checkPhoneNum() {
        mPhoneNum = mTvPhone.getText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNum)) {
            showError(mContext.getString(R.string.input_phone_num));
            return false;
        }
        if (!CommonUtils.isMobileNo(mPhoneNum)) {
            showError(mContext.getString(R.string.input_phone_num_right_tips));
            return false;
        }
        return true;
    }

    private void showError(String error) {
        if (TextUtils.isEmpty(error)) {
            mLlError.setVisibility(GONE);
            mLlPhone.setBackgroundResource(R.drawable.icon_key_nor);
        } else {
            mLlError.setVisibility(VISIBLE);
            mTvError.setText(error);
            mLlPhone.setBackgroundResource(R.drawable.bg_input_error);
        }
    }

    private void checkCanNext() {
        String phone = mTvPhone.getText().toString();
        boolean canClick;
        if (CommonUtils.isMobileNo(phone)) {
            canClick = true;
            mTvCaptchaLogin.setTextColor(Color.WHITE);
            mTvPwdLogin.setTextColor(Color.WHITE);
        } else {
            canClick = false;
            mTvCaptchaLogin.setTextColor(mContext.getResources().getColor(R.color.color_unclick));
            mTvPwdLogin.setTextColor(mContext.getResources().getColor(R.color.color_unclick));
            if (phone.length() == MAX_PHONE_LENGTH) {
                showError(mContext.getString(R.string.input_phone_num_right_tips));
            }
        }
        mTvCaptchaLogin.setEnabled(canClick);
        mTvPwdLogin.setEnabled(canClick);
    }

    @Override
    public void input(String text) {
        int index = mTvPhone.getSelectionStart();
        mTvPhone.getText().insert(index, text);
        showError(null);
        checkCanNext();
    }

    @Override
    public void delete() {
        int index = mTvPhone.getSelectionStart();
        if (index > 0) {
            mTvPhone.getText().delete(index - 1, index);
            checkCanNext();
        }
        if (mTvPhone.getText().toString().trim().length() == 0) {
            showError(null);
        }
    }

    @Override
    public boolean canInput() {
        final String phone = mTvPhone.getText().toString();
        return phone.length() < MAX_PHONE_LENGTH;
    }

    @Override
    public boolean isClear() {
        final String phone = mTvPhone.getText().toString();
        return TextUtils.isEmpty(phone);
    }

    @Override
    public void clear() {
        mTvPhone.setText("");
        showError(null);
    }
}
