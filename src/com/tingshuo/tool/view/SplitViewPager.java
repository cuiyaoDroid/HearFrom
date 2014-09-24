package com.tingshuo.tool.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SplitViewPager extends ViewPager {
	private OnPageScroll onPageScroll;

	boolean ok = true;

	public SplitViewPager(Context context) {
		super(context);

	}

	public SplitViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public void setPageScrollListener(OnPageScroll onPageScroll) {
		this.onPageScroll = onPageScroll;
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		
		super.onScrollChanged(l, t, oldl, oldt);
		if (onPageScroll != null) {

			onPageScroll.onScroll(l);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		// TODO Auto-generated method stub
		if (ok)

			return super.onInterceptTouchEvent(arg0);
		else
			return false;
	}
	public void setok(boolean ok) {
		this.ok=ok;
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		
		return super.onTouchEvent(arg0);
	}

	public interface OnPageScroll {
		void onScroll(int left);

	}

}
