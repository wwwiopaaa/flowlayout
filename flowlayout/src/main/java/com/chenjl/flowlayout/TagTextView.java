package com.chenjl.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by chenjl on 2016/11/18.
 */
public class TagTextView extends CheckedTextView {

    private boolean mBroadcasting;

    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;

    public TagTextView(Context context) {
        super(context);
    }

    public TagTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

            mBroadcasting = false;
        }
        super.setChecked(checked);
    }

    public void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }

    public interface OnCheckedChangeListener {

        void onCheckedChanged(TagTextView buttonView, boolean isChecked);
    }
}
