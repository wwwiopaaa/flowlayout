package com.chenjl.flowlayout;

import android.widget.Checkable;

/**
 * Created by chenjl on 2016/11/21.
 */
public interface TagView extends Checkable {

    public interface OnCheckedChangeListener {

        void onCheckedChanged(TagView buttonView, boolean isChecked);
    }

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener);

    void setTag(Object tag);

    Object getTag();
}
