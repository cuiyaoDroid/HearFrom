package com.tingshuo.tool.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.fetcher.ImageFetcher;


public class mianPostImgAdapter extends BaseAdapter{
	private String[] list;
	private ImageFetcher fetcher;
	protected LayoutInflater mInflater;
	
	public String[] getList() {
		return list;
	}

	public void setList(String[] list) {
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {
		return list[position];
	}


	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public mianPostImgAdapter(Context context, String[] list, ImageFetcher fetcher){
		this.list = list;
		this.fetcher = fetcher;
		mInflater = LayoutInflater.from(context);
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		String path = list[position];
		if(convertView == null){
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.grid_main_post_img, null);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.child_image);
			
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		fetcher.loadImage(HttpJsonTool.imgServerUrl+"small/"+path, viewHolder.mImageView);
		return convertView;
	}
	
	
	
	public static class ViewHolder{
		public ImageView mImageView;
	}

	
}
