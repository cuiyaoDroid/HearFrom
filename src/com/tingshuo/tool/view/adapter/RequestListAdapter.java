package com.tingshuo.tool.view.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.TimeFormatTool;
import com.tingshuo.tool.db.FriendsRequestHelper;
import com.tingshuo.tool.db.FriendsRequestHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.fetcher.ImageCache;
import com.tingshuo.web.img.fetcher.ImageFetcher;

public class RequestListAdapter extends BaseAdapter {
	private List<? extends Map<String, Object>> list;
	protected LayoutInflater mInflater;
	private ImageFetcher mImageFetcher;
	private ImageCache ImageCache;
	private Context context;
	public interface AgreeClicklistener{
		void agree(int position);
	}
	private AgreeClicklistener listener;
	@SuppressLint("SimpleDateFormat")
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
	public void setAgreeListener(AgreeClicklistener listener){
		this.listener = listener;
	}
	public RequestListAdapter(Context context,
			List<? extends Map<String, Object>> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		ImageCache = new ImageCache(context, "tingshuo");
		mImageFetcher = new ImageFetcher(context, 240);
		mImageFetcher.setImageCache(ImageCache);
		this.context = context;
	}

	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.cell_request_list, null);
			viewHolder.content_img = (ImageView) convertView
					.findViewById(R.id.content_img);
			viewHolder.content_txt = (TextView) convertView
					.findViewById(R.id.content_txt);
			viewHolder.title_txt = (TextView) convertView
					.findViewById(R.id.title_txt);
			viewHolder.request_btn = (Button) convertView
					.findViewById(R.id.agree_btn);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String images = (String) list.get(position).get(UserInfoHelper.HEAD);
		long time = (Long) list.get(position).get(FriendsRequestHelper.TIME);
		String str_time = format.format(time * TimeFormatTool.phpTojava);
		String name = (String) list.get(position).get(UserInfoHelper.NICK_NAME);
		int type = (Integer) list.get(position).get(FriendsRequestHelper.TYPE);
		viewHolder.title_txt.setText(name + "  " + str_time);
		boolean from_me = type == FriendsRequestHolder.TYPE_FROM_ME;
		viewHolder.content_txt.setText(from_me ? "请求添加对方为好友" : "请求添加你为好友");
		viewHolder.request_btn
				.setVisibility(from_me ? View.GONE : View.VISIBLE);
		if (images.length() > 0) {
			mImageFetcher.loadImage(HttpJsonTool.imgServerUrl + images,
					viewHolder.content_img);
		} else {
			viewHolder.content_img.setImageBitmap(null);
		}
		viewHolder.request_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener!=null){
					listener.agree(position);
				}
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		public ImageView content_img;
		public TextView content_txt;
		public TextView title_txt;
		public Button request_btn;
	}

}
