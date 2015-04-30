package com.tingshuo.hearfrom.screen;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;

;

public class BaseScreen extends Fragment {
	protected ScreenChangeListener listener;

	public void setScreenChangeListener(ScreenChangeListener listener) {
		if (listener != null) {
			this.listener = listener;
		}
	}

	public void onDetach() {
		super.onDetach();
		try {
			Field localField = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			localField.setAccessible(true);
			localField.set(this, null);
			return;
		} catch (NoSuchFieldException localNoSuchFieldException) {
			throw new RuntimeException(localNoSuchFieldException);
		} catch (IllegalAccessException localIllegalAccessException) {
			throw new RuntimeException(localIllegalAccessException);
		}
	}
}
