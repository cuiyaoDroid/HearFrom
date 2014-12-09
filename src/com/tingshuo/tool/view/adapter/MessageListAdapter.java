package com.tingshuo.tool.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.db.CurMessageListHelper;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.fetcher.ImageCache;
import com.tingshuo.web.img.fetcher.ImageFetcher;

public class MessageListAdapter extends BaseAdapter {
	private List<? extends Map<String, Object>> list;
	protected LayoutInflater mInflater;
	private ImageFetcher mImageFetcher;
	private ImageCache ImageCache;
	private Context context;

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

	public MessageListAdapter(Context context,
			List<? extends Map<String, Object>> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		ImageCache = new ImageCache(context, "tingshuo");
		mImageFetcher = new ImageFetcher(context, 240);
		mImageFetcher.setImageCache(ImageCache);
		this.context = context;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.cell_message, null);
			viewHolder.content_img = (ImageView) convertView
					.findViewById(R.id.content_img);
			viewHolder.content_txt = (TextView) convertView
					.findViewById(R.id.content_txt);
			viewHolder.title_txt = (TextView) convertView
					.findViewById(R.id.title_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String images = (String) list.get(position).get(
				UserInfoHelper.HEAD);
		String content = (String) list.get(position).get(
				CurMessageListHelper.CONTENT);
		String title = (String) list.get(position).get(
				UserInfoHelper.NICK_NAME);
		viewHolder.title_txt.setText(title);
		viewHolder.content_txt.setText(content);
		if (images.length() > 0) {
			mImageFetcher.loadImage(HttpJsonTool.imgServerUrl + images,
					viewHolder.content_img);
		} else {
			viewHolder.content_img.setImageBitmap(null);
		}
		return convertView;
	}

	public static class ViewHolder {
		public ImageView content_img;
		public TextView content_txt;
		public TextView title_txt;
	}

}
