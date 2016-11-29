package com.chenjl.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by chenjl on 2016/11/29.
 */
public class ComponentTagView extends CheckedTextView implements TagView {

    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    private OnCheckedChangeListener mOnCheckedChangeListener;

    public ComponentTagView(Context context) {
        super(context);
    }

    public ComponentTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public void toggle() {
        super.toggle();
    }

    @Override
    public void setChecked(boolean checked) {
        boolean preChecked = isChecked();
        if(checked != preChecked){
            if (mBroadcasting) {
                return;
            }

            mBroadcasting = true;

            if (mOnCheckedChangeWidgetListener != null) {
                mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
            }

            if(mOnCheckedChangeListener != null){
                mOnCheckedChangeListener.onCheckedChanged(this, checked);
            }

            mBroadcasting = false;
        }

        super.setChecked(checked);
    }

    @Override
    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
}
