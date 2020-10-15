/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 */

package com.dwh.page.page;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.ts.app.account.R;
import com.ts.app.account.presentation.view.adapter.ViewPagerAdapter;
import com.ts.app.account.presentation.view.base.BaseLayout;
import com.ts.app.account.presentation.view.customview.KeyboardView;
import com.ts.app.account.presentation.view.customview.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * LoginView.
 *
 * @author: jiangwei
 * @Date: 19-2-16 PM12:45
 */
public class LoginView extends BaseLayout implements View.OnClickListener {

    private static final String TAG = "LoginView";
    private TabLayout mTabs;
    private ViewPager mViewPager;
    private List<View> mViews;
    private String[] mTitles;
    private TextView mTitle;
    private QrLoginView mQrLoginView;
    private PhoneLoginView mPhoneLoginView;
    private KeyboardView mKeyboard;
    private VerifyCodeView mVerifyCodeView;
    private View mMain;
    private View mLineVert;
    private int mPageIndex;

    public LoginView(Context context) {
        super(context);
    }

    public LoginView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected int attachLayoutRes() {
        return R.layout.layout_login;
    }

    @Override
    protected void initView() {
        mKeyboard = mRootView.findViewById(R.id.keyboard);
        mLineVert = mRootView.findViewById(R.id.v_line_vert);

        //initRootLayout VerifyCodeView
        mVerifyCodeView = mRootView.findViewById(R.id.v_verify_code);
        mVerifyCodeView.setLoginView(this);

        //initRootLayout qr-phone loginview
        initChildViews(mContext);
        mMain = mRootView.findViewById(R.id.ll_main);
        mTitle = mRootView.findViewById(R.id.tv_title);
        mViewPager = mRootView.findViewById(R.id.vp);
        mTabs = mRootView.findViewById(R.id.tablayout);
        mRootView.findViewById(R.id.iv_close).setOnClickListener(this);
        mViewPager.setAdapter(new ViewPagerAdapter(mContext, mViews));
        mTabs.setDataList(Arrays.asList(mTitles));
        mTabs.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                //滚动
            }

            @Override
            public void onPageSelected(int i) {
                mPageIndex = i;
                showMainView();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void initArgs(Object obj) {
        super.initArgs(obj);
        mPhoneLoginView.initArgs(getArgs());
        mQrLoginView.initArgs(getArgs());
        mVerifyCodeView.initArgs(getArgs());
    }

    @Override
    public void onViewAdded() {
        super.onViewAdded();
        mPhoneLoginView.onViewAdded();
        mQrLoginView.onViewAdded();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPhoneLoginView.onPause();
        mQrLoginView.onPause();
        mVerifyCodeView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPhoneLoginView.onResume();
        mQrLoginView.onResume();
        mVerifyCodeView.onResume();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mQrLoginView.dismiss();
        mPhoneLoginView.dismiss();
        mVerifyCodeView.dismiss();
    }

    private void initChildViews(Context context) {
        mTitles = context.getResources().getStringArray(R.array.title_tabs);
        mQrLoginView = new QrLoginView(context);
        mPhoneLoginView = new PhoneLoginView(context);
        mPhoneLoginView.setLoginView(this);
        mPhoneLoginView.setKeyboardView(mKeyboard);

        mViews = new ArrayList<>();
        mViews.add(mQrLoginView);
        mViews.add(mPhoneLoginView);
    }

    /**
     * showMainView.
     */
    public void showMainView() {
        mMain.setVisibility(VISIBLE);
        mVerifyCodeView.setVisibility(GONE);
        mQrLoginView.switchLoginTab(mPageIndex == 0);
        if (mPageIndex == 0) {
            mTitle.setText(mContext.getString(R.string.title_qr_login));
            mKeyboard.setVisibility(GONE);
            mLineVert.setVisibility(GONE);
        } else {
            mTitle.setText(mContext.getString(R.string.title_phone_login));
            mKeyboard.setVisibility(VISIBLE);
            mLineVert.setVisibility(VISIBLE);
            mPhoneLoginView.setKeyboardView(mKeyboard);
        }
    }

    /**
     * showVerifyCodeView.
     */
    public void showVerifyCodeView(String phone) {
        mMain.setVisibility(INVISIBLE);
        mLineVert.setVisibility(GONE);
        mVerifyCodeView.setVisibility(VISIBLE);
        mVerifyCodeView.onViewAdded();
        mVerifyCodeView.setKeyboardView(mKeyboard);
        mVerifyCodeView.reset(phone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                mPop.onBackPress();
                break;
            default:
                break;
        }
    }

    /**
     * reset login view.
     */
    public void reset() {
        mViewPager.setCurrentItem(0);
    }
}
