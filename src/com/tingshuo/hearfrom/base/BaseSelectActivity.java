package com.tingshuo.hearfrom.base;

import com.tingshuo.hearfrom.R;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class BaseSelectActivity extends BaseSwipeBaceActivity {
	private ListView select_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_select);
		initContentView();
		select_list = (ListView) findViewById(R.id.select_list);
	}

	public ListView getSelect_list() {
		return select_list;
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		title_middle.setText("");
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finish();
			break;
		case R.id.title_txt_right:
			break;
		default:
			break;
		}
	}
}
