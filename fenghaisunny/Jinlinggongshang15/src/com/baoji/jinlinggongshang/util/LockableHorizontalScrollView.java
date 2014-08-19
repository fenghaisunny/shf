package com.baoji.jinlinggongshang.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class LockableHorizontalScrollView extends HorizontalScrollView {
	
	public LockableHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context,attrs,defStyle);
		init(context);
	}
	
	public LockableHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context,attrs);
		init(context);
	}
	
	public LockableHorizontalScrollView(Context context) {
		super(context);
		init(context);
	}
	
	void init(Context context) {
        // remove the fading as the HSV looks better without it
        setHorizontalFadingEdgeEnabled(false);
        setVerticalFadingEdgeEnabled(false);
    }

	@Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Do not allow touch events.
        return false;
    }
}
