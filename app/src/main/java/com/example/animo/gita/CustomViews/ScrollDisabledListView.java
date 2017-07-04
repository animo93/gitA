package com.example.animo.gita.CustomViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by animo on 26/6/17.
 */

public class ScrollDisabledListView extends ListView{
    public ScrollDisabledListView(Context context) {
        super(context);
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ScrollDisabledListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_MOVE)
            return true;
        return super.dispatchTouchEvent(ev);
    }
}
