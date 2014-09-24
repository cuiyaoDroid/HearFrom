package com.tingshuo.tool.view.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.ShowImageActivity;
import com.tingshuo.tool.imagescan.MyImageView;
import com.tingshuo.tool.imagescan.MyImageView.OnMeasureListener;
import com.tingshuo.tool.imagescan.NativeImageLoader;
import com.tingshuo.tool.imagescan.NativeImageLoader.NativeImageCallBack;


public class HeadAdapter extends BaseAdapter {
	private Point mPoint = new Point(80, 80);;//������װImageView�Ŀ��͸ߵĶ���
	/**
	 * �����洢ͼƬ��ѡ�����
	 */
	private GridView mGridView;
	private List<String> list;
	protected LayoutInflater mInflater;
	private Context context;



	public HeadAdapter(Context context, List<String> list, GridView mGridView) {
		this.context=context;
		this.list = list;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
//	private static final int FADE_IN_TIME = 200;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list.get(position);
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			//��������ImageView�Ŀ��͸�
			viewHolder.mImageView.setOnMeasureListener(new OnMeasureListener() {
				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		viewHolder.mImageView.setTag(path);
		
		viewHolder.mCheckBox.setVisibility(View.GONE);
		if(path.equals(ShowImageActivity.CAMRE)){
			viewHolder.mImageView.setImageResource(android.R.drawable.ic_menu_camera);
			return convertView;
		}
		//����NativeImageLoader����ر���ͼƬ
		Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageCallBack() {
			
			@Override
			public void onImageLoader(Bitmap bitmap, String path) {
				ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
				if(bitmap != null && mImageView != null){
					mImageView.setImageBitmap(bitmap);
				}
			}
		});
		
		if(bitmap != null){
			viewHolder.mImageView.setImageBitmap(bitmap);
		}else{
			viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		
		return convertView;
	}
	
	public static class ViewHolder{
		public MyImageView mImageView;
		public CheckBox mCheckBox;
	}



}