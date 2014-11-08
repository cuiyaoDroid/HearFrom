package com.tingshuo.hearfrom.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.tingshuo.hearfrom.R;

public class BaseAcivity extends SherlockActivity implements OnClickListener{
	protected TextView titleLeft;
	protected ImageButton titleback;
	protected TextView title_middle;
	protected TextView title_right;
	protected ImageButton titleRight;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	protected void initContentView(){
		titleLeft = (TextView) findViewById(R.id.title_txt_left);
		titleLeft.setOnClickListener(this);
		titleback = (ImageButton) findViewById(R.id.title_img_back);
		titleback.setOnClickListener(this);
		title_middle = (TextView) findViewById(R.id.title_txt_middle);
		title_right = (TextView) findViewById(R.id.title_txt_right);
		title_right.setOnClickListener(this);
		titleRight = (ImageButton)findViewById(R.id.title_right_imgbtn);
		titleRight.setOnClickListener(this);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
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
