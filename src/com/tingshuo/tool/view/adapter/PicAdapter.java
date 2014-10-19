package com.tingshuo.tool.view.adapter;

import java.util.List;
import java.util.Map;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.publishTopickActivity;
import com.tingshuo.tool.view.imagescan.MyImageView;
import com.tingshuo.tool.view.imagescan.NativeImageLoader;
import com.tingshuo.tool.view.imagescan.MyImageView.OnMeasureListener;
import com.tingshuo.tool.view.imagescan.NativeImageLoader.NativeImageCallBack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class PicAdapter extends BaseAdapter{
	private List<? extends Map<String, ?>> list;
	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	private GridView mGridView;
	protected LayoutInflater mInflater;
	
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
	private Context context;
	public PicAdapter(Context context, List<? extends Map<String, ?>> list, GridView mGridView){
		this.list = list;
		this.context=context;
		this.mGridView = mGridView;
		mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		@SuppressWarnings("unchecked")
		Map<String,Object> data = (Map<String, Object>) list.get(position);
		String path = (String) data.get("path");
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.cell_add_photo, null);
			viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.add_photo);
			//用来监听ImageView的宽和高
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
		
		if(path.equals(publishTopickActivity.PIC_ADD)){
			viewHolder.mImageView.setImageResource(R.drawable.btn_add_photo);
			return convertView;
		}
		//利用NativeImageLoader类加载本地图片
		Bitmap bitmap = NativeImageLoader.getInstance(context).loadNativeImage(path, mPoint, new NativeImageCallBack() {
			
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
		public TextView mTextViewTitle;
		public TextView mTextViewCounts;
	}




	
}
