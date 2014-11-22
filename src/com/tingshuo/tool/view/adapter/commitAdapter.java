package com.tingshuo.tool.view.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.TopicDetailActivity;
import com.tingshuo.tool.L;
import com.tingshuo.tool.TimeFormatTool;
import com.tingshuo.tool.db.CommentHelper;
import com.tingshuo.tool.db.CommentZanHelper;
import com.tingshuo.tool.db.CommentZanHolder;
import com.tingshuo.tool.db.ResponseListHelper;
import com.tingshuo.tool.db.ResponseListHolder;
import com.tingshuo.tool.db.TopicZanHelper;
import com.tingshuo.tool.db.TopicZanHolder;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.view.adapter.mainPostAdapter.ItemClickListener;
import com.tingshuo.tool.view.imageshower.ImageDialog;
import com.tingshuo.web.http.HttpJsonTool;
import com.tingshuo.web.img.fetcher.ImageCache;
import com.tingshuo.web.img.fetcher.ImageFetcher;

public class commitAdapter extends BaseAdapter {
	private List<? extends Map<String, Object>> list;
	protected LayoutInflater mInflater;
	private ImageFetcher mImageFetcher;
	private ImageCache ImageCache;
	private Context context;
	private commentBtnClickListener commentlistener;
	private resposeBtnClickListener resposeBtnlistener;

	public void setCommentlistener(commentBtnClickListener commentlistener) {
		this.commentlistener = commentlistener;
	}
	public void setResposeBtnClickListener(resposeBtnClickListener resposeBtnlistener) {
		this.resposeBtnlistener = resposeBtnlistener;
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

	public commitAdapter(Context context, List<? extends Map<String, Object>> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		int type = (Integer) list.get(position).get(TopicDetailActivity.TYPE);
		switch (type) {
		case TopicDetailActivity.TYPE_TOPIC:
			convertView = getMainPost(position, convertView);
			break;
		default:
			convertView = getCommentPost(position, convertView);
			break;
		}

		return convertView;
	}

	private View getCommentPost(final int position, View convertView) {
		final commentViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cell_comment_list, null);
			viewHolder = new commentViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			Object object = convertView.getTag();
			if (object instanceof commentViewHolder) {
				viewHolder = (commentViewHolder) object;
			} else {
				convertView = mInflater.inflate(R.layout.cell_comment_list,
						null);
				viewHolder = new commentViewHolder(convertView);
				convertView.setTag(viewHolder);
			}
		}
		int zan_status=(Integer) list.get(position).get(TopicZanHelper.STATUS);
		viewHolder.zan_check.setChecked(zan_status==CommentZanHolder.STATUS_ZAN);
		int zanCount=(Integer)list.get(position).get(CommentHelper.ZAN_COUNT);
		viewHolder.zan_check.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isChecked=viewHolder.zan_check.isChecked();
				int status = isChecked?CommentZanHolder.STATUS_ZAN:CommentZanHolder.STATUS_CAI;
				viewHolder.zan_check.setChecked(isChecked);
				list.get(position).put(CommentZanHelper.STATUS
						, status);
				int zanCount=(Integer)list.get(position).get(CommentHelper.ZAN_COUNT);
				list.get(position).put(CommentHelper.ZAN_COUNT, isChecked?(++zanCount):(--zanCount));
				viewHolder.zan_check.setText(zanCount<1?"ÔÞ":String.valueOf(zanCount));
				if(listener!=null){
					listener.onZanCommentCheckChange(position,zanCount, status);
				}
				if (isChecked){
					 addAnimation(viewHolder.zan_check);
				}
			}
		});
		viewHolder.zan_check.setText(zanCount<1?"ÔÞ":String.valueOf(zanCount));
		String headpath = (String) list.get(position).get(
				CommentHelper.HEAD);
		mImageFetcher.loadImage(HttpJsonTool.imgServerUrl + headpath,
				viewHolder.head_img);
		String content = (String) list.get(position).get(
				CommentHelper.CONTENT);
		viewHolder.content_txt.setText(Html.fromHtml(content));
		String name = (String) list.get(position).get(
				CommentHelper.NICK_NAME);
		viewHolder.name_txt.setText(name);
		long time = (Long) list.get(position).get(CommentHelper.TIME);
		viewHolder.time_txt.setText(format.format(time
				* TimeFormatTool.phpTojava));
		viewHolder.post_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (resposeBtnlistener != null) {
					resposeBtnlistener.onResposeBtnClick(position);
				}
			}
		});
		
		ArrayList<ResponseListHolder>res_holders = 
				(ArrayList<ResponseListHolder>) list.get(position).get("ResponseListHolders");
		viewHolder.respone_linear.removeAllViews();
		viewHolder.respone_linear.setVisibility(res_holders.size()>0?View.VISIBLE:View.GONE);
		for(ResponseListHolder res_holder:res_holders){
			View res_cell=mInflater.inflate(R.layout.cell_respone_list, null);
			TextView content_txt=(TextView) res_cell.findViewById(R.id.content_txt);
			content_txt.setText(
					Html.fromHtml("<font color=#0099cc>"+res_holder.getNickname()+":</font>"
							+res_holder.getContent()));
			
			viewHolder.respone_linear.addView(res_cell);
		}
		if(res_holders.size()==2){
			View res_cell=mInflater.inflate(R.layout.cell_more_respone, null);
			viewHolder.respone_linear.addView(res_cell);
		}
		return convertView;
	}

	private View getMainPost(final int position, View convertView) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cell_comment_main, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			Object object = convertView.getTag();
			if (object instanceof ViewHolder) {
				viewHolder = (ViewHolder) object;
			} else {
				convertView = mInflater.inflate(R.layout.cell_comment_main, null);
				viewHolder = new ViewHolder(convertView);
				convertView.setTag(viewHolder);
			}
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
				viewHolder.zan_check.setText(zanCount<1?"ÔÞ":String.valueOf(zanCount));
				if(listener!=null){
					listener.onZanCheckChange(position,zanCount, status);
				}
				if (isChecked){
					 addAnimation(viewHolder.zan_check);
				}
			}
		});
		viewHolder.zan_check.setText(zanCount<1?"ÔÞ":String.valueOf(zanCount));
		viewHolder.post_check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (commentlistener != null) {
					commentlistener.onCommentBtnClickClick(position);
				}
			}
		});
		String headpath = (String) list.get(position).get(
				mainPostListHelper.HEAD);
		mImageFetcher.loadImage(HttpJsonTool.imgServerUrl + headpath,
				viewHolder.head_img);
		String content = (String) list.get(position).get(
				mainPostListHelper.CONTENT);
		viewHolder.content_txt.setText(Html.fromHtml(content));
		String name = (String) list.get(position).get(
				mainPostListHelper.NICK_NAME);
		viewHolder.name_txt.setText(name);
		long time = (Long) list.get(position).get(mainPostListHelper.TIME);
		viewHolder.time_txt.setText(format.format(time
				* TimeFormatTool.phpTojava));
		String images = (String) list.get(position).get(
				mainPostListHelper.IMAGE);
		viewHolder.img_gridview
				.setVisibility(images.trim().length() > 0 ? View.VISIBLE
						: View.GONE);
		if (images.trim().length() > 0) {
			if (viewHolder.adapter == null) {
				viewHolder.adapter = new mianPostImgAdapter(context,
						images.split(","), mImageFetcher);
				viewHolder.img_gridview.setAdapter(viewHolder.adapter);
			} else {
				viewHolder.adapter.setList(images.split(","));
				viewHolder.adapter.notifyDataSetChanged();
			}
		}
		viewHolder.img_gridview
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						if (imageDialog != null) {
							if (imageDialog.isShowing()) {
								return;
							}
						}
						ArrayList<String> datalist = new ArrayList<String>();
						for (String data : viewHolder.adapter.getList()) {
							datalist.add(data);
						}
						imageDialog = new ImageDialog(context,
								R.style.imgDialog, datalist, position, true);
						imageDialog.setCanceledOnTouchOutside(false);
						imageDialog.show();
					}
				});
		return convertView;
	}

	ImageDialog imageDialog;
	private ItemClickListener listener;
	public void setItemClickListener(ItemClickListener listener){
		this.listener=listener;
	}
	public interface ItemClickListener{
		void onZanCheckChange(int position,int zanCount,int status);
		void onZanCommentCheckChange(int position,int zanCount,int status);
	}
	private void addAnimation(View view) {
		float[] vaules = new float[] { 0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f,
				1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f };
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
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

		public ViewHolder(View convertView) {
			zan_check = (CheckBox) convertView.findViewById(R.id.zan_check);
			post_check = (CheckBox) convertView.findViewById(R.id.post_check);
			head_img = (ImageView) convertView.findViewById(R.id.head_img);
			name_txt = (TextView) convertView.findViewById(R.id.name_txt);
			time_txt = (TextView) convertView.findViewById(R.id.time_txt);
			content_txt = (TextView) convertView.findViewById(R.id.content_txt);
			img_gridview = (GridView) convertView
					.findViewById(R.id.img_gridview);
			zan_txt = (TextView) convertView.findViewById(R.id.zan_txt);
		}
	}

	public static class commentViewHolder {
		public CheckBox zan_check;
		public CheckBox post_check;
		public ImageView head_img;
		public TextView name_txt;
		public TextView time_txt;
		public TextView content_txt;
		public TextView zan_txt;
		public LinearLayout respone_linear;
		public commentViewHolder(View convertView) {
			zan_check = (CheckBox) convertView.findViewById(R.id.zan_check);
			post_check = (CheckBox) convertView.findViewById(R.id.post_check);
			head_img = (ImageView) convertView.findViewById(R.id.head_img);
			name_txt = (TextView) convertView.findViewById(R.id.name_txt);
			time_txt = (TextView) convertView.findViewById(R.id.time_txt);
			content_txt = (TextView) convertView.findViewById(R.id.content_txt);
			zan_txt = (TextView) convertView.findViewById(R.id.zan_txt);
			respone_linear = (LinearLayout) convertView.findViewById(R.id.respone_linear);
		}
	}
}
