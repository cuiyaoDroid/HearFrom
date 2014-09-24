package com.tingshuo.tool.view.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.tingshuo.hearfrom.R;

public class SettingAdapter extends BaseAdapter {
	private List<? extends Map<String, ?>> list;
	protected LayoutInflater mInflater;
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

	public SettingAdapter(Context context, List<? extends Map<String, ?>> list) {
		this.list = list;
		mInflater = LayoutInflater.from(context);
		this.context = context;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.cell_main_post, null);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}


	public static class ViewHolder {
		public TextView title_txt;
		public CheckBox checker;
		public EditText editer;
	}

}
