package com.tingshuo.hearfrom.base;

import com.tingshuo.hearfrom.R;
import com.tingshuo.hearfrom.R.id;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BaseFragmentActivity extends FragmentActivity implements OnClickListener{
	protected TextView titleLeft;
	protected TextView title_middle;
	protected TextView title_right;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	protected void initContentView(){
		titleLeft = (TextView) findViewById(R.id.title_txt_left);
		titleLeft.setOnClickListener(this);
		title_middle = (TextView) findViewById(R.id.title_txt_middle);
		title_right = (TextView) findViewById(R.id.title_txt_right);
		title_right.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
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
