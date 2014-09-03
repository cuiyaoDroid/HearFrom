package com.tingshuo.hearfrom.screen;

import android.support.v4.app.Fragment;;

public class BaseScreen extends Fragment {
	protected ScreenChangeListener listener;

	public void setScreenChangeListener(ScreenChangeListener listener) {
		if (listener != null) {
			this.listener = listener;
		}
	}
}
