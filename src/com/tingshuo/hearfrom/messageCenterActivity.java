package com.tingshuo.hearfrom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.web.http.HttpJsonTool;

public class messageCenterActivity extends BaseAcivity {
	private ListView messageList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_cent);
		initContentView();
		messageList = (ListView) findViewById(R.id.message_list);
		//getMessageDate();
	}

	
	private void getMessageDate(){
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().friend_getfromme(getApplicationContext());
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			}
		};
		task.execute();
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		title_middle.setText("¡ƒÃÏ");
		titleback.setVisibility(View.GONE);
		title_right.setVisibility(View.VISIBLE);
		title_right.setText("∫√”—");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finish();
			break;
		case R.id.title_txt_right:
			Intent intent=new Intent(getApplicationContext(),ContactsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
