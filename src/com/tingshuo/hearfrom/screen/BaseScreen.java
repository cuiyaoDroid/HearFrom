package com.tingshuo.hearfrom.screen;

import android.annotation.SuppressLint;
import android.app.Fragment;

@SuppressLint("NewApi")
public class BaseScreen extends Fragment {
	protected ScreenChangeListener listener;

	public void setScreenChangeListener(ScreenChangeListener listener) {
		if (listener != null) {
			this.listener = listener;
		}
	}
}
