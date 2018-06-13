package com.bawaaba.rninja4.rookie.custom_views;

import android.widget.GridView;

/**
 * Created by rninja4 on 10/19/17.
 */

public class MyGridView  extends GridView {

    public MyGridView(android.content.Context context,
                      android.util.AttributeSet attrs)
    {
        super(context, attrs);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
