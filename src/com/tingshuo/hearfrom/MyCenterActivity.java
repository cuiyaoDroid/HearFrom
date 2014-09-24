package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.tingshuo.tool.view.ImgListView;

public class MyCenterActivity extends BaseAcivity{
	private ImgListView xListView;
	private SimpleAdapter adapter;
	private List<Map<String,Object>>adapterData=new ArrayList<Map<String,Object>>();
	private String[] buttons=new String[]{"我的主题","我的角色"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_center);
		initContentView();
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		titleLeft.setVisibility(View.GONE);
		title_middle.setText("我的");
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("设置");
		xListView=(ImgListView)findViewById(R.id.xListView);
		for(String button:buttons){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("title", button);
			adapterData.add(data);
		}
		adapter=new SimpleAdapter(getApplicationContext(),adapterData
				,R.layout.cell_setting_img_click,new String[]{"title"},new int[]{R.id.title});
		xListView.setAdapter(adapter);
		xListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String title=(String) adapterData.get(position-xListView.getHeaderViewsCount()).get("title");
				if(title.equals(buttons[0])){
					Intent intent=new Intent(getApplicationContext(),MyContentActivity.class);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_right:
			Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
