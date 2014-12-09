package com.tingshuo.tool.view.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.TimeUtil;
import com.tingshuo.tool.db.ChatProvider;
import com.tingshuo.tool.db.ChatProvider.ChatConstants;


public class ChatAdapter extends SimpleCursorAdapter {

	private static final int DELAY_NEWMSG = 2000;
	private Context mContext;
	private LayoutInflater mInflater;

	public ChatAdapter(Context context, Cursor cursor, String[] from) {
		// super(context, android.R.layout.simple_list_item_1, cursor, from,
		// to);
		super(context, 0, cursor, from, null);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cursor cursor = this.getCursor();
		cursor.moveToPosition(position);

		long dateMilliseconds = cursor.getLong(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DATE));

		int _id = cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants._ID));
		String date = TimeUtil.getChatTime(dateMilliseconds);
		String message = cursor.getString(cursor
				.getColumnIndex(ChatProvider.ChatConstants.MESSAGE));
		int come = cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DIRECTION));// 消息来自
		boolean from_me = (come == ChatConstants.OUTGOING);
		String jid = cursor.getString(cursor
				.getColumnIndex(ChatProvider.ChatConstants.JID));
		int delivery_status = cursor.getInt(cursor
				.getColumnIndex(ChatProvider.ChatConstants.DELIVERY_STATUS));
		ViewHolder viewHolder;
		if (convertView == null
				|| convertView.getTag(R.drawable.ic_launcher + come) == null) {
			if (come == ChatConstants.OUTGOING) {
				convertView = mInflater.inflate(R.layout.chat_item_right,
						parent, false);
			} else {
				convertView = mInflater.inflate(R.layout.chat_item_left, null);
			}
			viewHolder = buildHolder(convertView);
			convertView.setTag(R.drawable.ic_launcher + come, viewHolder);
			convertView
					.setTag(R.string.app_name, R.drawable.ic_launcher + come);
		} else {
			viewHolder = (ViewHolder) convertView.getTag(R.drawable.ic_launcher
					+ come);
		}


		bindViewData(viewHolder, date, from_me, jid, message, delivery_status);
		return convertView;
	}


	private void bindViewData(ViewHolder holder, String date, boolean from_me,
			String from, String message, int delivery_status) {
		holder.avatar.setBackgroundResource(R.drawable.login_default_avatar);
		if (from_me
				&& !PreferenceUtils.getPrefBoolean(mContext,
						PreferenceConstants.SHOW_MY_HEAD, true)) {
			holder.avatar.setVisibility(View.GONE);
		}
		holder.content.setText(message);
		holder.time.setText(date);

	}

	private ViewHolder buildHolder(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.content = (TextView) convertView.findViewById(R.id.textView2);
		holder.time = (TextView) convertView.findViewById(R.id.datetime);
		holder.avatar = (ImageView) convertView.findViewById(R.id.icon);
		return holder;
	}

	private static class ViewHolder {
		TextView content;
		TextView time;
		ImageView avatar;

	}

}
