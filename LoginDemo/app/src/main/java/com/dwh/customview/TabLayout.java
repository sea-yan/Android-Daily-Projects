/**
 * Copyright (c) 2018-2020 ThunderSoft
 * All Rights Reserved by Thunder Software Technology Co., Ltd and its affiliates.
 * You may not use, copy, distribute, modify, transmit in any form this file
 * except in compliance with ThunderSoft in writing by applicable law.
 *
 */

package com.dwh.customview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.dwh.logindemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout.
 *
 * @author: jiangwei
 * @Date: 19-4-10 PM3:26
 */
public class TabLayout extends HorizontalScrollView {
    private static final int DEFAULT_NORMAL_TEXT_SIZE_SP = 14;
    private int mNormalTextSize;
    private static final int DEFAULT_SELECT_TEXT_SIZE_SP = 16;
    private int mSelectTextSize;
    private static final int DEFAULT_NORMAL_TEXT_COLOR = Color.GRAY;
    private static final int DEFAULT_SELECT_TEXT_COLOR = Color.WHITE;
    private ColorStateList mTextColor;
    private static final int DEFAULT_INDICATOR_HEIGHT_DP = 2;
    private int mIndicatorHeight = DEFAULT_INDICATOR_HEIGHT_DP;
    private static final int DEFAULT_INDICATOR_COLOR = Color.RED;
    private int mIndicatorColor = DEFAULT_INDICATOR_COLOR;
    private static final int DEFAULT_DIVIDER_WIDTH = 1;
    private int mDividerWidth = DEFAULT_DIVIDER_WIDTH;
    private static final int DEFAULT_DIVIDER_COLOR = Color.GRAY;
    private int mDividerColor = DEFAULT_DIVIDER_COLOR;
    private Paint mDividerPaint;
    private static final int DEFAULT_DIVIDER_PADDING = 5;
    private int mDividerPadding = DEFAULT_DIVIDER_PADDING;
    private boolean hasShowDivider = false;
    private static final int DEFAULT_MSG_ROUND_COLOR = Color.RED;
    private int mMsgRoundColor = DEFAULT_MSG_ROUND_COLOR;
    private SparseBooleanArray mInitSetMap;
    private SparseIntArray mMsgNumMap;
    private Paint mMsgPaint;
    private Paint mMsgNumPaint;
    private int mMsgNumColor = Color.WHITE;
    private static final int MSG_TEXT_SIZE_SP = 10;
    private int mMsgPadding;
    private static final int DEFAULT_TAB_MIN_WIDTH = 140;
    private int mMinTabWidth;
    private static final int DEFAULT_PADDING = 10;
    private static final float PRESS_STROKE_WIDTH = 0.7f;
    private int mTabPadding;
    private ViewPager mViewPager;
    private IndicationTabLayout mTabContainer;
    private int mTabCount;
    private int mCurrentTabPosition;
    private float mCurrentOffset;
    private List<String> mDataList;

    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor.
     */
    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initStyle(context, attrs);
        setFillViewport(true);
        setHorizontalScrollBarEnabled(false);
        mTabContainer = new IndicationTabLayout(context);
        mTabContainer.setSelectedIndicatorColor(mIndicatorColor);
        mTabContainer.setSelectedIndicatorHeight(mIndicatorHeight);
        addView(mTabContainer, 0, new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
        mDataList = new ArrayList<>();
        mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMsgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMsgNumPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInitSetMap = new SparseBooleanArray();
        mMsgNumMap = new SparseIntArray();
    }

    private void initStyle(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.TabLayout, 0, 0);
        mNormalTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TabLayout_tab_normal_textSize,
                sp2px(DEFAULT_NORMAL_TEXT_SIZE_SP));
        mSelectTextSize = typedArray.getDimensionPixelSize(
                R.styleable.TabLayout_tab_select_textSize,
                sp2px(DEFAULT_SELECT_TEXT_SIZE_SP));
        mTextColor = typedArray.getColorStateList(R.styleable.TabLayout_tab_textColor);
        if (mTextColor == null) {
            mTextColor = createDefaultTextColor();
        }
        mIndicatorHeight = (int) typedArray.getDimension(R.styleable.TabLayout_tab_indicatorHeight,
                DEFAULT_INDICATOR_HEIGHT_DP);
        mIndicatorColor = typedArray.getColor(R.styleable.TabLayout_tab_indicatorColor,
                DEFAULT_INDICATOR_COLOR);
        mMinTabWidth = typedArray.getColor(R.styleable.TabLayout_tab_min_width,
                DEFAULT_TAB_MIN_WIDTH);
        mDividerColor = typedArray.getColor(R.styleable.TabLayout_tab_dividerColor,
                DEFAULT_DIVIDER_COLOR);
        mDividerWidth = (int) typedArray.getDimension(R.styleable.TabLayout_tab_dividerWidth,
                DEFAULT_DIVIDER_WIDTH);
        mDividerPadding = (int) typedArray.getDimension(R.styleable.TabLayout_tab_dividerPadding,
                DEFAULT_DIVIDER_PADDING);
        mTabPadding =
                (int) typedArray.getDimension(R.styleable.TabLayout_tab_Padding, DEFAULT_PADDING);
        hasShowDivider = typedArray.getBoolean(R.styleable.TabLayout_tab_dividerShow, false);
        typedArray.recycle();
    }

    private ColorStateList createDefaultTextColor() {
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{{android.R.attr.state_selected}, {0}},
                new int[]{DEFAULT_SELECT_TEXT_COLOR, DEFAULT_NORMAL_TEXT_COLOR});
        return colorStateList;
    }

    public void setDataList(List<String> dataList) {
        this.mDataList.clear();
        this.mDataList.addAll(dataList);
    }

    /**
     * setupWithViewPager.
     *
     * @param viewPager viewPager
     */
    public void setupWithViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
        if (viewPager == null) {
            throw new IllegalArgumentException("viewpager not is null");
        }
        PagerAdapter pagerAdapter = viewPager.getAdapter();
        if (pagerAdapter == null) {
            throw new IllegalArgumentException("pagerAdapter not is null");
        }
        this.mViewPager.addOnPageChangeListener(new TabPagerChanger());
        mTabCount = pagerAdapter.getCount();
        mCurrentTabPosition = viewPager.getCurrentItem();
        notifyDataSetChanged();
    }

    /**
     * notifyDataSetChanged.
     */
    public void notifyDataSetChanged() {
        mTabContainer.removeAllViews();
        for (int i = 0; i < mTabCount; i++) {
            final int currentPosition = i;
            TextView tabTextView = createTextView();
            tabTextView.setPadding(mTabPadding, 0, mTabPadding, 0);
            tabTextView.setText(mDataList.get(i));
            tabTextView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(currentPosition);
                }
            });
            mTabContainer.addView(tabTextView);
        }
        setSelectedTabView(mCurrentTabPosition);
    }

    private TextView createTextView() {
        TextView textView = new TextView(getContext());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mNormalTextSize);
        textView.setTextColor(mTextColor);
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.tablayout_background);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.weight = 1;
        textView.setLayoutParams(layoutParams);
        return textView;
    }

    protected void setSelectedTabView(int position) {
        for (int i = 0; i < mTabCount; i++) {
            View view = mTabContainer.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                textView.setSelected(position == i);
                final TextPaint tp = textView.getPaint();
                tp.setStyle(Paint.Style.FILL_AND_STROKE);
                if (position == i) {
                    tp.setStrokeWidth(PRESS_STROKE_WIDTH);
                } else {
                    tp.setStrokeWidth(0f);
                }
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        position == i ? mSelectTextSize : mNormalTextSize);
            }
        }
    }

    private class TabPagerChanger implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            setScrollPosition(position, positionOffset);
        }

        @Override
        public void onPageSelected(int position) {
            setSelectedTabView(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private void setScrollPosition(int position, float positionOffset) {
        this.mCurrentTabPosition = position;
        this.mCurrentOffset = positionOffset;
        mTabContainer.setIndicatorPositionFromTabPosition(position, positionOffset);
        scrollTo(calculateScrollXForTab(mCurrentTabPosition, mCurrentOffset), 0);
        final int posi = 3;
        if (position == posi) {
            hideMsg(position);
        }
    }

    private int calculateScrollXForTab(int position, float positionOffset) {
        final float rate = 0.5f;
        final View selectedChild = mTabContainer.getChildAt(position);
        final View nextChild = position + 1 < mTabContainer.getChildCount()
                ? mTabContainer.getChildAt(position + 1) : null;
        final int selectedWidth = selectedChild != null ? selectedChild.getWidth() : 0;
        final int nextWidth = nextChild != null ? nextChild.getWidth() : 0;
        return selectedChild.getLeft()
                + ((int) ((selectedWidth + nextWidth) * positionOffset * rate))
                + (selectedChild.getWidth() / 2) - (getWidth() / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mTabCount <= 0) {
            return;
        }
        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        if (mDividerWidth > 0 && hasShowDivider) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding,
                        paddingLeft + tab.getRight(),
                        height - mDividerPadding, mDividerPaint);
            }
        }
        for (int i = 0; i < mTabCount - 1; i++) {
            if (mInitSetMap.get(i)) {
                updateMsgPosition(canvas, mTabContainer.getChildAt(i), mMsgNumMap.get(i));
            }
        }
    }

    private void updateMsgPosition(final Canvas canvas, final View updateView, final int msgNum) {
        if (updateView == null) {
            return;
        }
        final float rate = 0.5f;
        int circleX;
        int circleY;
        if (updateView.getWidth() > 0) {
            int selectTextPadding = (int) ((updateView.getWidth()
                    - measureTextLength(updateView)) / 2 + rate);
            circleX = updateView.getRight() - selectTextPadding + mMsgPadding;
            circleY = (int) ((mTabContainer.getHeight() - measureTextHeight(updateView)) / 2
                    - mMsgPadding);
            drawMsg(canvas, circleX, circleY, msgNum);
        }
    }

    private void drawMsg(Canvas canvas, int msgCircleX, int msgCircleY, int msgNum) {
        mMsgPaint.setStyle(Paint.Style.FILL);
        mMsgPaint.setColor(mMsgRoundColor);
        final int maxNum = 99;
        final float rate = 0.5f;
        if (msgNum > 0) {
            mMsgNumPaint.setTextSize(MSG_TEXT_SIZE_SP);
            mMsgNumPaint.setColor(mMsgNumColor);
            mMsgNumPaint.setTextAlign(Paint.Align.CENTER);
            String showTxt = msgNum > maxNum ? "99+" : String.valueOf(msgNum);
            int msgNumRadius = (int) Math.max(mMsgNumPaint.descent() - mMsgNumPaint.ascent(),
                    mMsgNumPaint.measureText(showTxt)) / 2 + dp2px(2);
            canvas.drawCircle(msgCircleX + msgNumRadius, msgCircleY, msgNumRadius, mMsgPaint);
            Paint.FontMetricsInt fontMetrics = mMsgNumPaint.getFontMetricsInt();
            int baseline = (int) ((2 * msgCircleY - (fontMetrics.descent - fontMetrics.ascent)) / 2
                    - fontMetrics.ascent + rate);
            canvas.drawText(showTxt, msgCircleX + msgNumRadius, baseline, mMsgNumPaint);
        } else {
            canvas.drawCircle(msgCircleX + dp2px(2), msgCircleY, dp2px(2), mMsgPaint);
        }
    }

    /**
     * showMsg.
     */
    public void showMsg(int msgPosition, int msgNum, int msgPadding) {
        mInitSetMap.put(msgPosition, true);
        this.mMsgNumMap.put(msgPosition, msgNum);
        mMsgPadding = msgPadding;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    /**
     * hideMsg.
     *
     * @param msgPosition msgPosition
     */
    public void hideMsg(int msgPosition) {
        mInitSetMap.put(msgPosition, false);
        this.mMsgNumMap.delete(msgPosition);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private float measureTextLength(View measureView) {
        if (measureView instanceof TextView) {
            TextView textView = ((TextView) measureView);
            String text = textView.getText().toString();
            return textView.getPaint().measureText(text);
        }
        return 0;
    }

    private float measureTextHeight(View measureView) {
        if (measureView instanceof TextView) {
            TextView textView = ((TextView) measureView);
            Paint textPaint = textView.getPaint();
            return textPaint.descent() - textPaint.ascent();
        }
        return 0;
    }

    public class IndicationTabLayout extends LinearLayout {
        private int mSelectedIndicatorHeight;
        private Paint mSelectedIndicatorPaint;
        private int mSelectedPosition = -1;
        private float mSelectionOffset;
        private int mIndicatorLeft = -1;
        private int mIndicatorRight = -1;

        public IndicationTabLayout(Context context) {
            this(context, null);
        }

        public IndicationTabLayout(Context context, AttributeSet attrs) {
            this(context, attrs, 0);
        }

        /**
         * constructor.
         */
        public IndicationTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setWillNotDraw(false);
            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER_HORIZONTAL);
            mSelectedIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }

        private void setSelectedIndicatorColor(int color) {
            if (mSelectedIndicatorPaint.getColor() != color) {
                mSelectedIndicatorPaint.setColor(color);
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        private void setSelectedIndicatorHeight(int height) {
            if (mSelectedIndicatorHeight != height) {
                mSelectedIndicatorHeight = height;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        private void setIndicatorPositionFromTabPosition(int position, float positionOffset) {
            mSelectedPosition = position;
            mSelectionOffset = positionOffset;
            updateIndicatorPosition();
        }

        private void updateIndicatorPosition() {
            final float extra = 0.5f;
            final View selectedTitle = getChildAt(mSelectedPosition);
            int left;
            int right;
            if (selectedTitle != null && selectedTitle.getWidth() > 0) {
                int selectTextPadding = (int) ((selectedTitle.getWidth()
                        - measureTextLength(selectedTitle)) / 2 + extra);
                left = selectedTitle.getLeft() + selectTextPadding;
                right = selectedTitle.getRight() - selectTextPadding;
                if (mSelectionOffset > 0f && mSelectedPosition < getChildCount() - 1) {
                    View nextTitle = getChildAt(mSelectedPosition + 1);
                    int textPadding = (int) ((nextTitle.getWidth()
                            - measureTextLength(nextTitle)) / 2 + extra);
                    int moveLeft = nextTitle.getLeft() + textPadding - left;
                    int moveRight = nextTitle.getRight() - textPadding - right;
                    left = (int) (left + moveLeft * mSelectionOffset);
                    right = (int) (right + moveRight * mSelectionOffset);
                }
            } else {
                left = right = -1;
            }
            setIndicatorPosition(left, right);
        }

        private void setIndicatorPosition(int left, int right) {
            if (left != mIndicatorLeft || right != mIndicatorRight) {
                mIndicatorLeft = left;
                mIndicatorRight = right;
                ViewCompat.postInvalidateOnAnimation(this);
            }
        }

        private float measureTextLength(View measureView) {
            if (measureView instanceof TextView) {
                TextView textView = ((TextView) measureView);
                String text = textView.getText().toString();
                return textView.getPaint().measureText(text);
            }
            return 0;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            if (mIndicatorLeft >= 0 && mIndicatorRight > mIndicatorLeft) {
                canvas.drawRect(mIndicatorLeft, getHeight() - mSelectedIndicatorHeight,
                        mIndicatorRight, getHeight(), mSelectedIndicatorPaint);
            }
        }
    }

    private int dp2px(int value) {
        final float extra = 0.5f;
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (value * scale + extra);
    }

    private int sp2px(int value) {
        final float extra = 0.5f;
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (value * fontScale + extra);
    }

}
