package com.tingshuo.hearfrom;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.tingshuo.tool.T;

@SuppressWarnings("deprecation")
public class HearFromTabMainActivity extends TabActivity implements
		OnClickListener {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		initContentView();
		initTabHost();
	}

	private void initContentView() {
	}

	@SuppressLint("SimpleDateFormat")
	private TextView txtCount;
	private TextView txtCount2;
	private TextView txtCount3;
	private TextView txtCount4;
	private TextView txtCount5;

	private void initTabHost() {
		TabHost host = getTabHost();

		View view = getLayoutInflater().inflate(R.layout.tabicon, null);
		txtCount = (TextView) view.findViewById(R.id.txtCount);
		txtCount.setVisibility(View.GONE);
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		icon.setImageResource(R.drawable.tab_item1);
		host.addTab(host.newTabSpec("tab_item1").setIndicator(view)
				.setContent(new Intent(this, MainListActivity.class)));

		
		
		
		View view2 = getLayoutInflater().inflate(R.layout.tabicon, null);
		txtCount2 = (TextView) view2.findViewById(R.id.txtCount);
		txtCount2.setVisibility(View.GONE);
		ImageView icon2 = (ImageView) view2.findViewById(R.id.icon);
		icon2.setImageResource(R.drawable.tab_item2);
		host.addTab(host.newTabSpec("tab_item2").setIndicator(view2)
				.setContent(new Intent(Intent.ACTION_VIEW
						,Uri.parse("rong://com.tingshuo.hearfrom/conversationlist"))));
				//new Intent(this, ContactsActivity.class)));
		
		View view3 = getLayoutInflater().inflate(R.layout.tabicon, null);
		txtCount3 = (TextView) view3.findViewById(R.id.txtCount);
		txtCount3.setVisibility(View.GONE);
		ImageView icon3 = (ImageView) view3.findViewById(R.id.icon);
		icon3.setImageResource(R.drawable.tab_item3);
		icon3.setBackgroundResource(R.drawable.tab_bg_background);
		host.addTab(host.newTabSpec("tab_item3").setIndicator(view3)
				.setContent(new Intent(this, MainListActivity.class)));

		view3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						publishTopickActivity.class);
				startActivity(intent);
			}
		});

		View view4 = getLayoutInflater().inflate(R.layout.tabicon, null);
		txtCount4 = (TextView) view4.findViewById(R.id.txtCount);
		txtCount4.setVisibility(View.GONE);
		ImageView icon4 = (ImageView) view4.findViewById(R.id.icon);
		icon4.setImageResource(R.drawable.tab_item4);
		host.addTab(host.newTabSpec("tab_item4").setIndicator(view4)
				.setContent(new Intent(this, ResearchActivity.class)));

		View view5 = getLayoutInflater().inflate(R.layout.tabicon, null);
		txtCount5 = (TextView) view5.findViewById(R.id.txtCount);
		txtCount5.setVisibility(View.GONE);
		ImageView icon5 = (ImageView) view5.findViewById(R.id.icon);
		icon5.setImageResource(R.drawable.tab_item5);
		host.addTab(host.newTabSpec("tab_item5").setIndicator(view5)
				.setContent(new Intent(this, MyCenterActivity.class)));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}

	private long pressTime = 0;

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			long curtime = System.currentTimeMillis();
			if (curtime - pressTime <= 3000) {
				finish();
			} else {
				pressTime = curtime;
				T.show(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_LONG);
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	@Override
	public void startActivity(Intent intent) {
		// TODO Auto-generated method stub
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		super.startActivity(intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		// TODO Auto-generated method stub
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		super.startActivityForResult(intent, requestCode);
	}
}
