/**  
 * MyGallery.java
 * @version 1.0
 * @author Haven
 * @createTime 2011-12-9 ����03:42:53
 * android.widget.Gallery���Ӻ������������Ҫ��������ϸ��
 */
package com.tingshuo.tool.view.imageshower;



import com.tingshuo.tool.L;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Gallery;

public class MyGallery extends Gallery {
	private GestureDetector gestureScanner;
	private ShowerImageView imageView;

	public MyGallery(Context context) {
		super(context);

	}

	public MyGallery(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyGallery(Context context, AttributeSet attrs) {
		super(context, attrs);

		gestureScanner = new GestureDetector(new MySimpleGesture());
		this.setOnTouchListener(new OnTouchListener() {

			float baseValue;
			float originalScale;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				View view = MyGallery.this.getSelectedView();
				if (view instanceof ShowerImageView) {
					imageView = (ShowerImageView) view;

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						baseValue = 0;
						originalScale = imageView.getScale();
					}
					if (event.getAction() == MotionEvent.ACTION_MOVE) {
						if (event.getPointerCount() == 2) {
							float x = event.getX(0) - event.getX(1);
							float y = event.getY(0) - event.getY(1);
							float value = (float) Math.sqrt(x * x + y * y);// ��������ľ���
							// System.out.println("value:" + value);
							if (baseValue == 0) {
								baseValue = value;
							} else {
								float scale = value / baseValue;// ��ǰ�����ľ��������ָ����ʱ�����ľ��������Ҫ���ŵı�����
								// scale the image
								imageView.zoomTo(originalScale * scale, x + event.getX(1), y + event.getY(1));

							}
						}
					}
				}
				return false;
			}

		});
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		View view = MyGallery.this.getSelectedView();
		if (view instanceof ShowerImageView) {
			imageView = (ShowerImageView) view;

			float v[] = new float[9];
			Matrix m = imageView.getImageMatrix();
			m.getValues(v);
			// ͼƬʵʱ��������������
			float left, right;
			// ͼƬ��ʵʱ������
			float width, height;
			width = imageView.getScale() * imageView.getImageWidth();
			height = imageView.getScale() * imageView.getImageHeight();
			// һ���߼�Ϊ�ƶ�ͼƬ�ͻ���gallery�������߼������û����������˽�ķǳ��������Ķ����µĴ���ǰ����˼������������
			if ((int) width <= ImageDialog.screenWidth && (int) height <= ImageDialog.screenHeight)// ���ͼƬ��ǰ��С<��Ļ��С��ֱ�Ӵ��������¼�
			{
				try{
				super.onScroll(e1, e2, distanceX, distanceY);
				}catch(Exception e){
					
				}
			} else {
				left = v[Matrix.MTRANS_X];
				right = left + width;
				Rect r = new Rect();
				imageView.getGlobalVisibleRect(r);

				if(Math.abs(distanceY)>Math.abs(distanceX)){
					imageView.postTranslate(-distanceX, -distanceY);
				}
				else if (distanceX > 0)// ���󻬶�
				{
					if (r.left > 0) {// �жϵ�ǰImageView�Ƿ���ʾ��ȫ
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (right < ImageDialog.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				} else if (distanceX < 0)// ���һ���
				{
					if (r.right < ImageDialog.screenWidth) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else if (left > 0) {
						super.onScroll(e1, e2, distanceX, distanceY);
					} else {
						imageView.postTranslate(-distanceX, -distanceY);
					}
				} 


			}

		} else {
			super.onScroll(e1, e2, distanceX, distanceY);
		}
		return false;
	}
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		int kEvent;
		if (isScrollingLeft(e1, e2)) {
			// Check if scrolling left
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			// Otherwise scrolling right
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureScanner.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// �ж����±߽��Ƿ�Խ��
			View view = MyGallery.this.getSelectedView();
			if (view instanceof ShowerImageView) {
				imageView = (ShowerImageView) view;
				float width = imageView.getScale() * imageView.getImageWidth();
				float height = imageView.getScale() * imageView.getImageHeight();
				if (imageView.getScale() < imageView.getScaleRate()) {
					imageView.zoomTo(imageView.getScaleRate(), ImageDialog.screenWidth / 2, ImageDialog.screenHeight / 2, 200f);
					imageView.layoutToCenter();
				}
				if ((int) width <= ImageDialog.screenWidth && (int) height <= ImageDialog.screenHeight)// ���ͼƬ��ǰ��С<��Ļ��С���жϱ߽�
				{
					break;
				}
				float v[] = new float[9];
				Matrix m = imageView.getImageMatrix();
				m.getValues(v);
				float top = v[Matrix.MTRANS_Y];
				float bottom = top + height;
				if (top > 0) {
					imageView.postTranslateDur(-top, 200f);
				}
				L.i("manga", "bottom:" + bottom);
				if (bottom < ImageDialog.screenHeight) {
					imageView.postTranslateDur(ImageDialog.screenHeight - bottom, 200f);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	private class MySimpleGesture extends SimpleOnGestureListener {
		// �����µĵڶ���Touch downʱ����
		public boolean onDoubleTap(MotionEvent e) {
			View view = MyGallery.this.getSelectedView();
			if (view instanceof ShowerImageView) {
				imageView = (ShowerImageView) view;
				if (imageView.getScale() > imageView.getScaleRate()) {
					imageView.zoomTo(imageView.getScaleRate(), ImageDialog.screenWidth / 2, ImageDialog.screenHeight / 2, 200f);
					imageView.layoutToCenter();
				} else {
					imageView.zoomTo(5.0f, ImageDialog.screenWidth / 2, ImageDialog.screenHeight / 2, 200f);
					imageView.layoutToCenter();
				}

			} else {

			}
			// return super.onDoubleTap(e);
			return true;
		}
	}
}