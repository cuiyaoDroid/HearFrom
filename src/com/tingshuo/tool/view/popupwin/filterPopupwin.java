package com.tingshuo.tool.view.popupwin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.RoleUtil;

public class filterPopupwin extends PopupWindow {
	private List<Map<String,Object>>listdata;
	private GridView gridview;
	private filterPopupwinAdapter adapter;
	private popupClickCallBack mCallback;
	public filterPopupwin(Context context, popupClickCallBack callback) {
		this.mCallback=callback;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.popup_filter_mainlist,
				null);
		gridview=(GridView)contentView.findViewById(R.id.gridView);
		listdata=new ArrayList<Map<String,Object>>();
		
		Map<String,Object>alldata=new HashMap<String, Object>();
		alldata.put("role_id", -1);
		alldata.put("sex", -1);
		alldata.put("content", "È«²¿");
		alldata.put("check", true);
		listdata.add(alldata);
		
		Map<String,Object>meladata=new HashMap<String, Object>();
		meladata.put("role_id", -1);
		meladata.put("sex", 1);
		meladata.put("content", "ÄÐ");
		meladata.put("check", false);
		listdata.add(meladata);
		
		Map<String,Object>femeladata=new HashMap<String, Object>();
		femeladata.put("role_id", -1);
		femeladata.put("sex", 0);
		femeladata.put("content", "Å®");
		femeladata.put("check", false);
		listdata.add(femeladata);
		
		Map<String,Object>kongdata=new HashMap<String, Object>();
		kongdata.put("role_id", -1);
		kongdata.put("sex", -1);
		kongdata.put("content", "");
		kongdata.put("check", false);
		listdata.add(kongdata);
		
		for(int i=0;i<RoleUtil.role_ids.length;i++){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("role_id", RoleUtil.role_ids[i]);
			data.put("content", RoleUtil.role_names[i]);
			data.put("sex", -1);
			data.put("check", false);
			listdata.add(data);
		}
		
		adapter=new filterPopupwinAdapter(context,listdata);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String content=(String) listdata.get(position).get("content");
				if(content.length()==0){
					return;
				}
				if(mCallback!=null){
					int role_id=(Integer) listdata.get(position).get("role_id");
					int sex=(Integer) listdata.get(position).get("sex");
					mCallback.clicked(role_id, sex);
				}
				for(int i=0;i<listdata.size();i++){
					listdata.get(i).put("check", position==i);
				}
				adapter.notifyDataSetChanged();
			}
		});
		this.setContentView(contentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		// this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xffEFECEA);
		this.setBackgroundDrawable(dw);
	}
	public interface popupClickCallBack{
		void clicked(int role_id,int sex);
	}
}
