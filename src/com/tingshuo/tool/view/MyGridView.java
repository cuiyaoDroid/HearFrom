package com.tingshuo.tool.view;

import com.tingshuo.tool.L;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class MyGridView extends GridView {
	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyGridView(Context context) {
		super(context);
	}

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	public interface OnTouchBlankPositionListener {
		/**
		 * 
		 * @return 是否要终止事件的路由
		 */
		boolean onTouchBlankPosition();
	}
	private OnTouchBlankPositionListener mTouchBlankPosListener;
	public void setOnTouchBlankPositionListener(
			OnTouchBlankPositionListener listener) {
		mTouchBlankPosListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

//		if (mTouchBlankPosListener != null) {
			if (!isEnabled()) {
				// A disabled view that is clickable still consumes the touch
				// events, it just doesn't respond to them.
				return isClickable() || isLongClickable();
			}
			if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
				final int motionPosition = pointToPosition((int) event.getX(),
						(int) event.getY());
				if (motionPosition == INVALID_POSITION ) {
					return false;
				}
			}else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
				final int motionPosition = pointToPosition((int) event.getX(),
						(int) event.getY());
				if (motionPosition == INVALID_POSITION ) {
					return false;
				}
			}
//		}

		return super.onTouchEvent(event);
	}
}