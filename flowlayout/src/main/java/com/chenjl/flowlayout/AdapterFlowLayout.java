package com.chenjl.flowlayout;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.CallSuper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;

/**
 * Created by chenjl on 2016/11/18.
 */
public class AdapterFlowLayout extends FlowLayout {

    private BaseAdapter mAdapter;

    public AdapterFlowLayout(Context context) {
        super(context);
    }

    public AdapterFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AdapterFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        mAdapter.registerDataSetObserver(mDataSetObserver);
        onNotifyChange();
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            onNotifyChange();
        }
    };

    @CallSuper
    public void onNotifyChange() {
        removeAllViews();

        final BaseAdapter adapter = mAdapter;
        if (adapter == null) {
            return;
        }

        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View view = adapter.getView(i, null, this);
            if (view.getId() == NO_ID) {
                //ViewCompat.generateViewId()
            }
            addView(view);
        }
    }
}
