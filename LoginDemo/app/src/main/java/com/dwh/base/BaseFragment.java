/**
 * Copyright (c) 2018-2019
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with THUNDERSOFT in writing by applicable law.
 */

package com.dwh.base;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * BaseFragment.
 *
 * @author: jiangwei
 * @Date: 18-12-21 PM3:17
 */
public abstract class BaseFragment extends Fragment {

    protected View mRootView = null;
    private boolean mIsMulti;
    private boolean mHasCreateView;

    private boolean mIsFragmentVisible;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariable();
    }

    protected abstract void initPresenter();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint() && mRootView != null && !mIsMulti) {
            mIsMulti = true;
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!mHasCreateView && getUserVisibleHint()) {
            onFragmentVisibleChange(true);
            mIsFragmentVisible = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initPresenter();
        if (mRootView == null) {
            mRootView = inflater.inflate(attachLayoutRes(), container, false);
            initViews();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        loadData();
        return mRootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRootView = null;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mRootView == null) {
            return;
        }
        mHasCreateView = true;
        if (isVisibleToUser) {
            onFragmentVisibleChange(true);
            mIsFragmentVisible = true;
            return;
        }
        if (mIsFragmentVisible) {
            onFragmentVisibleChange(false);
            mIsFragmentVisible = false;
        }
    }

    private void initVariable() {
        mHasCreateView = false;
        mIsFragmentVisible = false;
    }

    protected void onFragmentVisibleChange(boolean isVisible) {
    }

    /**
     * attachLayoutRes.
     *
     * @return layout id
     */
    protected abstract int attachLayoutRes();

    /**
     * initViews.
     */
    protected abstract void initViews();

    /**
     * loadData.
     */
    protected abstract void loadData();

}
