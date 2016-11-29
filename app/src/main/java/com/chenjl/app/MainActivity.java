package com.chenjl.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.chenjl.flowlayout.FlowLayout;
import com.chenjl.flowlayout.RadioFlowLayout;
import com.chenjl.flowlayout.RadioTagView;
import com.chenjl.flowlayout.TagView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RadioFlowLayout mRadioFlowLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadioFlowLayout = (RadioFlowLayout) findViewById(R.id.radio_flow_layout);
        mRadioFlowLayout.setOnCheckedChangeListener(new RadioFlowLayout.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioFlowLayout group, TagView tagView) {
                if (tagView != null) {
                    Toast.makeText(MainActivity.this, ((TextView) tagView).getText().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void align(View view) {
        mRadioFlowLayout.setAlign(FlowLayout.ALIGN_CENTER);
    }

    public void none(View view) {
        mRadioFlowLayout.setAlign(FlowLayout.ALIGN_NONE);
    }

    public void left(View view) {
        mRadioFlowLayout.setAlign(FlowLayout.ALIGN_LEFT);
    }

    public void right(View view) {
        mRadioFlowLayout.setAlign(FlowLayout.ALIGN_RIGHT);
    }

    public void specifyLines(View view) {
        mRadioFlowLayout.specifyLines(1);
    }

    public void add(View view) {
        TextView radioTagView = new TextView(this);
        radioTagView.setText("other item:" + (mRadioFlowLayout.getChildCount() + 1));
        radioTagView.setTextColor(getResources().getColor(R.color.textcolor));
        mRadioFlowLayout.addView(radioTagView);
    }

    public void remove(View view) {
        int childCount = mRadioFlowLayout.getChildCount();
        if (childCount > 0) {
            mRadioFlowLayout.removeViewAt(childCount - 1);
        }
    }

    public void setAdapter(View view) {
        mRadioFlowLayout.setAdapter(new BaseAdapter() {
            List<String> datas = Arrays.asList("你好", "I'm fine!", "are you ok!", "thanks you!",
                    "I'm fine!", "hello!", "这个要比较长！！!", "你好", "这个更长！！！！！！!",
                    "are you ok!"
                    , "这个要绕地球n+1+1+1+1+1+1+1+1+1+1+1+1+1+1圈!");

            @Override
            public int getCount() {
                return datas.size();
            }

            @Override
            public String getItem(int i) {
                return datas.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                RadioTagView tagTextView = new RadioTagView(MainActivity.this);
                tagTextView.setText(getItem(i));
                tagTextView.setTextColor(getResources().getColor(R.color.textcolor));
                tagTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                tagTextView.setBackgroundResource(R.drawable.radio_selector);
                tagTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
                tagTextView.setPadding(10, 5, 10, 5);
                return tagTextView;
            }
        });
    }

    public void getCurCheckedView(View view) {
        RadioTagView checkedTagView = (RadioTagView) mRadioFlowLayout.getCheckedTagView();
        if (checkedTagView != null) {
            Toast.makeText(this, checkedTagView.getText().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
