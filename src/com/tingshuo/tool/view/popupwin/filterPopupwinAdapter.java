package com.tingshuo.tool.view.popupwin;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.tingshuo.hearfrom.R;


public class filterPopupwinAdapter extends BaseAdapter {
	/**
	 * 用来存储图片的选中情况
	 */
	private List<? extends Map<String,?>> list;
	protected LayoutInflater mInflater;
	private Context context;

	public filterPopupwinAdapter(Context context, List<? extends Map<String,?>> list) {
		this.context=context;
		this.list = list;
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
		
		String content = (String) list.get(position).get("content");
		boolean check = (Boolean) list.get(position).get("check");
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.cell_popup_grid, null);
			viewHolder = new ViewHolder();
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.filte_btn);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.mCheckBox.setChecked(check);
		viewHolder.mCheckBox.setText(content);
		viewHolder.mCheckBox.setVisibility(content.length()==0?View.GONE:View.VISIBLE);
		return convertView;
	}
	
	
	public static class ViewHolder{
		public CheckBox mCheckBox;
	}



}
