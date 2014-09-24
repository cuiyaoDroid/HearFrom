package com.tingshuo.tool.view.popupwin;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.DensityUtil;
import com.tingshuo.tool.imagescan.GroupAdapter;
import com.tingshuo.tool.imagescan.ImageBean;
/**
 * @blog http://blog.csdn.net/xiaanming
 * 
 * @author xiaanming
 * 
 *
 */
public class SpinnerPathPopupwin extends PopupWindow {
	private GroupAdapter adapter;
	private ListView mGroupGridView;
	public static final String ALL="Ыљга";
	
	public interface SpinnerPathPopupCallBack{
		void onClick(int position,String parent);
	}
	private SpinnerPathPopupCallBack mCallback;
	public SpinnerPathPopupwin(Context context, SpinnerPathPopupCallBack callback,final List<ImageBean> list) {
		this.mCallback=callback;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View contentView = inflater.inflate(R.layout.activity_select_img,
				null);
		mGroupGridView = (ListView) contentView.findViewById(R.id.main_grid);
		
		mGroupGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCallback.onClick(position,list.get(position).getFolderName());
			}
		});
		adapter = new GroupAdapter(context, list, mGroupGridView);
		mGroupGridView.setAdapter(adapter);
		this.setContentView(contentView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(DensityUtil.dip2px(context, 350));
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0x66000000);
		this.setBackgroundDrawable(dw);
	}

}
