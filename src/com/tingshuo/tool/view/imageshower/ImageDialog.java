package com.tingshuo.tool.view.imageshower;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.DensityUtil;


public class ImageDialog extends AlertDialog {
	// 屏幕宽度
	public static int screenWidth;
	// 屏幕高度
	public static int screenHeight;
	private MyGallery gallery;
	public static List<String> imgs;
	public static int index;
	private GalleryAdapter galleryAdapter;
	private TextView title_txt;
	private boolean web=false;
	public ImageDialog(Context context, int theme, List<String> pic_data,int index,boolean web) {
		super(context, theme);
		this.web=web;
		this.imgs = pic_data;
		this.index=index;
	}
	public void setPosition(int position){
		gallery.setSelection(index);
	}
	public ImageDialog(Context context) {
		super(context);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_img);
		title_txt = (TextView)findViewById(R.id.title_txt);
		title_txt.setText("预览 "+(index+1)+"/"+imgs.size());
		screenWidth = getWindow().getWindowManager().getDefaultDisplay()
				.getWidth();
		screenHeight = getWindow().getWindowManager().getDefaultDisplay()
				.getHeight()-DensityUtil.getStatusBarHeight(getContext());
		gallery = (MyGallery) findViewById(R.id.gallery);
		gallery.setVerticalFadingEdgeEnabled(false);// 取消竖直渐变边框
		gallery.setHorizontalFadingEdgeEnabled(false);// 取消水平渐变边框
		galleryAdapter = new GalleryAdapter(getContext(), imgs,gallery,web);
		gallery.setAdapter(galleryAdapter);
		gallery.setSelection(index);
		gallery.setOnItemSelectedListener(new GalleryChangeListener());
	}

	float beforeLenght = 0.0f; // 两触点距离
	float afterLenght = 0.0f; // 两触点距离
	boolean isScale = false;
	float currentScale = 1.0f;// 当前图片的缩放比率

	private class GalleryChangeListener implements OnItemSelectedListener {

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			currentScale = 1.0f;
			isScale = false;
			beforeLenght = 0.0f;
			afterLenght = 0.0f;
			title_txt.setText("预览 "+(arg2+1)+"/"+imgs.size());
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
}