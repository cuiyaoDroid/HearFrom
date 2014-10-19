package com.tingshuo.hearfrom;

import com.tingshuo.hearfrom.base.BaseAcivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResearchActivity extends BaseAcivity{
	private View add_role;
	private TextView add_role_title;
	
	private View hot_role;
	private TextView hot_role_title;
	
	private View hot_topic;
	private TextView hot_topic_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_research);
		initContentView();
	}

	@Override
	protected void initContentView() {
		super.initContentView();
		titleLeft.setVisibility(View.GONE);
		title_middle.setText("发现");
		
		add_role=findViewById(R.id.add_role);
		add_role_title=(TextView)add_role.findViewById(R.id.title);
		add_role_title.setText("申请角色");
		add_role.setOnClickListener(this);
		
		hot_role=findViewById(R.id.hot_role);
		hot_role_title=(TextView)hot_role.findViewById(R.id.title);
		hot_role_title.setText("热门角色");
		hot_role.setOnClickListener(this);
		
		hot_topic=findViewById(R.id.hot_topic);
		hot_topic_title=(TextView)hot_topic.findViewById(R.id.title);
		hot_topic_title.setText("热门主题");
		hot_topic.setOnClickListener(this);
		
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
		case R.id.title_txt_left:
			finish();
			break;
		case R.id.title_txt_right:
			break;
		case R.id.add_role:
			break;
		case R.id.hot_role:
			break;
		case R.id.hot_topic:
			break;
		default:
			break;
		}
	}
}
