package com.chenjl.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckedTextView;

/**
 * Created by chenjl on 2016/11/18.
 */
public class RadioTagView extends ComponentTagView {

    public RadioTagView(Context context) {
        super(context);
    }

    public RadioTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void toggle() {
        if (!isChecked()) {
            super.toggle();
        }
    }
}
