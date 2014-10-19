package com.tingshuo.tool.view.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.TimeFormatTool;
import com.tingshuo.tool.db.TopicZanHelper;
import com.tingshuo.tool.db.TopicZanHolder;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.view.imageshower.ImageDialog;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.fetcher.ImageCache;
import com.tingshuo.web.img.fetcher.ImageFetcher;

public class mainPostAdapter extends BaseAdapter {
	private List<? extends Map<String, Object>> list;
	protected LayoutInflater mInflater;
	private ImageFetcher mImageFetcher;
	private ImageCache ImageCache;
	private Context context;
	private commentBtnClickListener commentlistener;
	
	public void setCommentlistener(commentBtnClickListener commentlistener) {
		this.commentlistener = commentlistener;
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

	public mainPostAdapter(Context context, List<? extends Map<String, Object>> list) {
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
			convertView = mInflater.inflate(R.layout.cell_main_post, null);
			viewHolder.zan_check = (CheckBox) convertView
					.findViewById(R.id.zan_check);
			viewHolder.post_check = (CheckBox) convertView
					.findViewById(R.id.post_check);
			viewHolder.head_img = (ImageView) convertView
					.findViewById(R.id.head_img);
			viewHolder.name_txt = (TextView) convertView
					.findViewById(R.id.name_txt);
			viewHolder.time_txt = (TextView) convertView
					.findViewById(R.id.time_txt);
			viewHolder.content_txt = (TextView) convertView
					.findViewById(R.id.content_txt);
			viewHolder.img_gridview = (GridView) convertView
					.findViewById(R.id.img_gridview);
			viewHolder.zan_txt = (TextView) convertView
					.findViewById(R.id.zan_txt);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		int zan_status=(Integer) list.get(position).get(TopicZanHelper.STATUS);
		viewHolder.zan_check.setChecked(zan_status==TopicZanHolder.STATUS_ZAN);
		int zanCount=(Integer)list.get(position).get(mainPostListHelper.ZAN_COUNT);
		viewHolder.zan_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isChecked=viewHolder.zan_check.isChecked();
				int status = isChecked?TopicZanHolder.STATUS_ZAN:TopicZanHolder.STATUS_CAI;
				viewHolder.zan_check.setChecked(isChecked);
				list.get(position).put(TopicZanHelper.STATUS
						, status);
				int zanCount=(Integer)list.get(position).get(mainPostListHelper.ZAN_COUNT);
				list.get(position).put(mainPostListHelper.ZAN_COUNT, isChecked?(++zanCount):(--zanCount));
				viewHolder.zan_check.setText(zanCount<1?"до":String.valueOf(zanCount));
				if(listener!=null){
					listener.onZanCheckChange(position,zanCount, status);
				}
				if (isChecked){
					 addAnimation(viewHolder.zan_check);
				}
			}
		});
		viewHolder.zan_check.setText(zanCount<1?"до":String.valueOf(zanCount));
		viewHolder.post_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(commentlistener!=null){
					commentlistener.onCommentBtnClickClick(position);
				}
			}
		});
		String headpath=(String) list.get(position).get(mainPostListHelper.HEAD);
		mImageFetcher.loadImage(HttpJsonTool.imgServerUrl+headpath, viewHolder.head_img);
		String content=(String) list.get(position).get(mainPostListHelper.CONTENT);
		viewHolder.content_txt.setText(content);
		String name=(String) list.get(position).get(mainPostListHelper.NICK_NAME);
		viewHolder.name_txt.setText(name);
		long time=(Long) list.get(position).get(mainPostListHelper.TIME);
		viewHolder.time_txt.setText(format.format(time*TimeFormatTool.phpTojava));
		String images=(String) list.get(position).get(mainPostListHelper.IMAGE);
		viewHolder.img_gridview.setVisibility(images.trim().length()>0?View.VISIBLE:View.GONE);
		String[] images_array=images.split(",");
		if(images.trim().length()>0){
			if(viewHolder.adapter==null){
				viewHolder.adapter=new mianPostImgAdapter(context, images_array, mImageFetcher);
				viewHolder.img_gridview.setAdapter(viewHolder.adapter);
			}else{
				viewHolder.adapter.setList(images.split(","));
				viewHolder.adapter.notifyDataSetChanged();
			}
		}
		if(images_array.length==4){
			viewHolder.img_gridview.setNumColumns(2);
		}else{
			viewHolder.img_gridview.setNumColumns(3);
		}
		viewHolder.img_gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(imageDialog!=null){
					if(imageDialog.isShowing()){
						return;
					}
				}
				ArrayList<String>datalist=new ArrayList<String>();
				for(String data:viewHolder.adapter.getList()){
					datalist.add(data);
				}
				imageDialog = new ImageDialog(
						context, R.style.imgDialog, datalist,
						position,true);
				imageDialog.setCanceledOnTouchOutside(false);
				imageDialog.show();
			}
		});
		
		return convertView;
	}
	ImageDialog imageDialog;
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}
	private ItemClickListener listener;
	public void setItemClickListener(ItemClickListener listener){
		this.listener=listener;
	}
	public interface ItemClickListener{
		void onZanCheckChange(int position,int zanCount,int status);
	}
	public static class ViewHolder {
		public CheckBox zan_check;
		public CheckBox post_check;
		public ImageView head_img;
		public TextView name_txt;
		public TextView time_txt;
		public TextView content_txt;
		public mianPostImgAdapter adapter;
		public GridView img_gridview;
		public TextView zan_txt;
		public String images[];
	}

}
