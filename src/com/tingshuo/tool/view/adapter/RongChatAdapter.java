package com.tingshuo.tool.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.TimeFormatTool;
import com.tingshuo.tool.XMPPHelper;
import com.tingshuo.tool.db.ChatMessageHelper;
import com.tingshuo.tool.db.ChatMessageHolder;
import com.tingshuo.tool.db.UserInfoHelper;

public class RongChatAdapter extends BaseAdapter {

	private Context mContext;
	private LayoutInflater mInflater;
	private List<? extends Map<String, Object>> data;

	public RongChatAdapter(Context context,
			List<? extends Map<String, Object>> data) {
		// super(context, android.R.layout.simple_list_item_1, cursor, from,
		// to);
		super();
		mContext = context;
		mInflater = LayoutInflater.from(context);
		this.data= data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ChatMessageHolder holder = (ChatMessageHolder) data.get(position).get(
				ChatMessageHelper.TABLE_NAME);
		String nick_name=(String) data.get(position).get(
				UserInfoHelper.NICK_NAME);
		ViewHolder viewHolder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher
						+ holder.getStatus()) == null) {
			if (holder.getStatus() == ChatMessageHolder.STATUS_SENDED
					|| holder.getStatus() == ChatMessageHolder.STATUS_SENDING) {
				convertView = mInflater.inflate(R.layout.chat_item_right,
						parent, false);
			} else {
				convertView = mInflater.inflate(R.layout.chat_item_left, null);
			}
			viewHolder = buildHolder(convertView);
			convertView.setTag(R.drawable.ic_launcher + holder.getStatus(),
					viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ holder.getStatus());
		}
		bindViewData(viewHolder, holder,  nick_name, holder.getContent());
		return convertView;
	}

	private void bindViewData(ViewHolder holder, ChatMessageHolder chat_date
			,String from, String message) {
		holder.avatar.setBackgroundResource(R.drawable.login_default_avatar);
		holder.content.setText(XMPPHelper.convertNormalStringToSpannableString(mContext,message,false));
		holder.time.setText(TimeFormatTool.format(chat_date.getTime()));
		if(chat_date.getStatus() == ChatMessageHolder.STATUS_SENDED
				|| chat_date.getStatus() == ChatMessageHolder.STATUS_RECIVED){
			holder.progressBar.setVisibility(View.GONE);
		}else {
			holder.progressBar.setVisibility(View.VISIBLE);
		}

	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.content = (TextView) convertView.findViewById(R.id.textView2);
		holder.time = (TextView) convertView.findViewById(R.id.datetime);
		holder.avatar = (ImageView) convertView.findViewById(R.id.icon);
		holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar1);
		return holder;
	}

	private static class ViewHolder {
		TextView content;
		TextView time;
		ImageView avatar; 
		ProgressBar progressBar;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
