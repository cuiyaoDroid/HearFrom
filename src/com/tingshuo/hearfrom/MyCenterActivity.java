package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.hearfrom.setting.SettingActivity;
import com.tingshuo.tool.view.ImgListView;

public class MyCenterActivity extends BaseAcivity{
	private ImgListView xListView;
	private SimpleAdapter adapter;
	private List<Map<String,Object>>adapterData=new ArrayList<Map<String,Object>>();
	private String[] buttons=new String[]{"我的主题","我的角色"};
	private ProgressBar progressBar;
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
		titleback.setVisibility(View.GONE);
		title_middle.setText("我的");
		titleRight.setVisibility(View.VISIBLE);
		titleRight.setImageResource(R.drawable.btn_top_setting_bg);
		xListView=(ImgListView)findViewById(R.id.xListView);
		progressBar=(ProgressBar)findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		xListView.setVisibility(View.GONE);
		xListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position==0){
					return;
				}
				String title=(String) adapterData.get(position-xListView.getHeaderViewsCount()).get("title");
				if(title.equals(buttons[0])){
					Intent intent=new Intent(getApplicationContext(),MyContentActivity.class);
					startActivity(intent);
				}
			}
		});
		xListView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				xListView.setImageId(R.drawable.top_img);
				for(String button:buttons){
					Map<String,Object>data=new HashMap<String, Object>();
					data.put("title", button);
					adapterData.add(data);
				}
				adapter=new SimpleAdapter(getApplicationContext(),adapterData
						,R.layout.cell_setting_img_click,new String[]{"title"},new int[]{R.id.title});
				xListView.setAdapter(adapter);
				progressBar.setVisibility(View.GONE);
				xListView.setVisibility(View.VISIBLE);
				xListView.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in));
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
		case R.id.title_right_imgbtn:
			Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
