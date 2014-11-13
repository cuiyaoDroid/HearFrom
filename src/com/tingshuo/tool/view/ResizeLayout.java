package com.tingshuo.tool.view;

import android.content.Context;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class ResizeLayout extends RelativeLayout {
	private OnResizeListener mListener;

	public interface OnResizeListener {
		void OnResize(int w, int h, int oldw, int oldh);
		void OnLayout(int l, int t, int r, int b);
	}

	public void setOnResizeListener(OnResizeListener l) {
		mListener = l;
	}

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (mListener != null) {
			mListener.OnResize(w, h, oldw, oldh);
		}
	}
	@Override   
    protected void onLayout(boolean changed, int l, int t, int r, int b) {   
        super.onLayout(changed, l, t, r, b);   
        if (mListener != null&&changed) {
			mListener.OnLayout(l, t, r, b);
		}
    }  
}