package com.tingshuo.hearfrom;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class BaseAcivity extends SherlockActivity implements OnClickListener{
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
}
