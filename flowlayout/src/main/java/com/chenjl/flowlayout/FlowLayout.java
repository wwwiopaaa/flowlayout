package com.chenjl.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {

    public static final int ALIGN_NONE = 0;
    public static final int ALIGN_CENTER = 1;
    public static final int ALIGN_LEFT = 2;
    public static final int ALIGN_RIGHT = 3;

    @IntDef({ALIGN_NONE, ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignType {
    }

    private int mAlign = ALIGN_NONE;

    private int lineSpacing = 0; // the spacing between lines in flowlayout

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
        lineSpacing = mTypedArray.getDimensionPixelSize(R.styleable.FlowLayout_lineSpacing, 0);
        mAlign = mTypedArray.getInt(R.styleable.FlowLayout_align, ALIGN_NONE);
        mTypedArray.recycle();
    }

    private List<LayoutParams> mMeasureLineView = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();
        int mPaddingBottom = getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineY = mPaddingTop;
        int lineHeight = 0;
        int lineNum = 0;
        if (!mMeasureLineView.isEmpty()) {
            mMeasureLineView.clear();
        }

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams childLp = (LayoutParams) child.getLayoutParams();
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, lineY);
            int spaceWidth = childLp.leftMargin + childLp.rightMargin + child.getMeasuredWidth();
            int spaceHeight = childLp.topMargin + childLp.bottomMargin + child.getMeasuredHeight();

            if (lineUsed + spaceWidth > widthSize) {
                ++lineNum;
                adjustAlightParamsOfLine(mMeasureLineView, widthSize - lineUsed);
                mMeasureLineView.clear();
                //approach the limit of width and move to next line
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineHeight = 0;
            }
            childLp.mLineNum = lineNum;

            mMeasureLineView.add(childLp);
            lineHeight = spaceHeight > lineHeight ? spaceHeight : lineHeight;
            lineUsed += spaceWidth;
        }

        adjustAlightParamsOfLine(mMeasureLineView, widthSize - lineUsed);
        mMeasureLineView.clear();

        setMeasuredDimension(widthSize,
                heightMode == MeasureSpec.EXACTLY ? heightSize : lineY + lineHeight + mPaddingBottom
        );
    }

    /**
     * 计算并设置对齐参数
     */
    private void adjustAlightParamsOfLine(final List<LayoutParams> params, int remainWidth) {
        if (remainWidth <= 0 || params == null || params.isEmpty()) {
            return;
        }

        final int perSpace = remainWidth / params.size();
        for (LayoutParams item : params) {
            switch (mAlign) {
                case ALIGN_CENTER:
                    item.mAlignLeft = perSpace / 2;
                    item.mAlignRight = perSpace / 2;
                    break;
                case ALIGN_RIGHT:
                    item.mAlignLeft = perSpace;
                    item.mAlignRight = 0;
                    break;
                case ALIGN_LEFT:
                    item.mAlignLeft = 0;
                    item.mAlignRight = perSpace;
                    break;
                default://None
                    item.mAlignLeft = 0;
                    item.mAlignRight = 0;
                    break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mPaddingLeft = getPaddingLeft();
        int mPaddingRight = getPaddingRight();
        int mPaddingTop = getPaddingTop();

        int lineX = mPaddingLeft;
        int lineY = mPaddingTop;
        int lineWidth = r - l;
        int lineUsed = mPaddingLeft + mPaddingRight;
        int lineHeight = 0;

        int lineNum = 0;

        for (int i = 0; i < this.getChildCount(); i++) {
            View child = this.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            LayoutParams childLp = (LayoutParams) child.getLayoutParams();
            lineX += childLp.mAlignLeft;

            int spaceWidth = childLp.leftMargin + childLp.rightMargin;
            int spaceHeight = childLp.topMargin + childLp.bottomMargin;
            int left = lineX + childLp.leftMargin;
            int top = lineY + childLp.topMargin;
            int right = lineX + childLp.leftMargin + childWidth;
            int bottom = lineY + childLp.topMargin + childHeight;

            spaceWidth += childWidth;
            spaceHeight += childHeight;

            if (lineNum < childLp.mLineNum/*lineUsed + spaceWidth > lineWidth*/) { //next line
                lineNum = childLp.mLineNum;
                lineY += lineHeight + lineSpacing;
                lineUsed = mPaddingLeft + mPaddingRight;
                lineX = mPaddingLeft + childLp.mAlignLeft;
                lineHeight = 0;

                left = lineX + childLp.leftMargin;
                top = lineY + childLp.topMargin;
                right = lineX + childLp.leftMargin + childWidth;
                bottom = lineY + childLp.topMargin + childHeight;

            }
            child.layout(left, top, right, bottom);

            lineHeight = spaceHeight > lineHeight ? spaceHeight : lineHeight;
            lineUsed += spaceWidth;
            lineX += spaceWidth + childLp.mAlignRight;
        }
        // add the num of last line
    }

    public void setAlign(@AlignType int align) {
        mAlign = align;
        requestLayout();
    }

    public void specifyLines(final int lineNum) {
        int count = getChildCount();
        while (0 < count) {
            View child = getChildAt(count - 1);
            if (child == null || child.getVisibility() == GONE) {
                continue;
            }
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if(layoutParams.mLineNum < lineNum){
                break;
            }
            removeView(child);
            count = getChildCount();
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new FlowLayout.LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new FlowLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FlowLayout.LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int mAlignLeft, mAlignRight;
        /*package*/ int mLineNum;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}