package com.chenjl.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

/**
 * Created by chenjl on 2016/11/18.
 */
public class RadioFlowLayout extends AdapterFlowLayout {

    public interface OnCheckedChangeListener {
        void onCheckedChanged(RadioFlowLayout group, TagView tagView);
    }

    private WeakReference<TagView> mWeakRefCheckedView;

    private boolean mProtectFromCheckedChange;

    private PassThroughHierarchyChangeListener mPassThroughListener;

    private TagView.OnCheckedChangeListener mChildOnCheckedChangeListener;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public RadioFlowLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    public RadioFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RadioFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public void onNotifyChange() {
        setCurrentCheckedView(null);
        super.onNotifyChange();
    }

    /**
     * 当前选中的tagView
     */
    public TagView getCheckedTagView() {
        return mWeakRefCheckedView != null ? mWeakRefCheckedView.get() : null;
    }

    /**
     * 选中某一项
     */
    public void setCheckedTagViewByPosition(int position) {
        if (position < 0) {
            setPreCheckedViewState(false);
            setCurrentCheckedView(null);
            return;
        }
        int childCount = getChildCount();
        int realIndex = 0;
        for (int i = 0; i < childCount; i++) {
            View view = getChildAt(i);
            if (view != null && view instanceof TagView/* && view.isEnabled()*/) {
                TagView tagView = (TagView) view;
                if (realIndex == position) {
                    tagView.setChecked(true);
                    return;
                }
                realIndex++;
            }
        }
    }

    @Override
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        TagView checkedView = mWeakRefCheckedView != null ? mWeakRefCheckedView.get() : null;
        if (checkedView != null) {
            setPreCheckedViewState(true);
            setCurrentCheckedView(checkedView);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {

        if (child instanceof TagView) {
            final TagView tagView = (TagView) child;
            if (tagView.isChecked()) {
                setPreCheckedViewState(false);
                setCurrentCheckedView(tagView);
            }
        }
        super.addView(child, index, params);
    }

    private void setPreCheckedViewState(boolean checked) {
        mProtectFromCheckedChange = true;
        TagView checkedView = mWeakRefCheckedView != null ? mWeakRefCheckedView.get() : null;
        if (checkedView != null) {
            setCheckedStateForView(checkedView, checked);
        }
        mProtectFromCheckedChange = false;
    }

    private void setCheckedStateForView(TagView tagView, boolean checked) {
        tagView.setChecked(checked);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    private void setCurrentCheckedView(TagView tagView) {
        TagView checkedView = mWeakRefCheckedView != null ? mWeakRefCheckedView.get() : null;

        if (checkedView != null && checkedView == tagView) {
            return;
        }

        if (checkedView != null) {
            mWeakRefCheckedView.clear();
            mWeakRefCheckedView = null;
        }

        if (tagView != null) {
            mWeakRefCheckedView = new WeakReference<TagView>(tagView);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(this, tagView);
            }
        }
    }

    private class CheckedStateTracker implements TagView.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(TagView buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            setPreCheckedViewState(false);

            setCurrentCheckedView(isChecked ? buttonView : null);
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == RadioFlowLayout.this && child instanceof TagView) {
                ((TagView) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            if (parent == RadioFlowLayout.this && child instanceof TagView) {
                ((TagView) child).setOnCheckedChangeWidgetListener(null);
            }

            if(child instanceof TagView && ((TagView) child).isChecked()){
                TagView tagView = mWeakRefCheckedView != null ? mWeakRefCheckedView.get() : null;
                if(tagView != null && tagView == child){
                    mWeakRefCheckedView.clear();
                    mWeakRefCheckedView = null;
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
