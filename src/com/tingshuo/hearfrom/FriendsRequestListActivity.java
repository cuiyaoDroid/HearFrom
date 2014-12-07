package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.db.FriendsRequestHelper;
import com.tingshuo.tool.db.FriendsRequestHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.view.adapter.RequestListAdapter;
import com.tingshuo.tool.view.adapter.RequestListAdapter.AgreeClicklistener;
import com.tingshuo.web.http.HttpJsonTool;

public class FriendsRequestListActivity extends BaseSwipeBaceActivity implements AgreeClicklistener{
	private ListView requestList;
	private RequestListAdapter adapter;
	private List<Map<String,Object>>data=new ArrayList<Map<String,Object>>();
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_request);
		initContentView();
		requestList = (ListView) findViewById(R.id.request_list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		adapter=new RequestListAdapter(getApplicationContext(), data);
		adapter.setAgreeListener(this);
		requestList.setAdapter(adapter);
		getRequestData();
	}

	
	private void getRequestData(){
		progressBar.setVisibility(View.VISIBLE);
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				FriendsRequestHelper reHelper = new FriendsRequestHelper(getApplicationContext());
				reHelper.clear();
				reHelper.close();
				HttpJsonTool.getInstance().friend_getfromme(getApplicationContext());
				return HttpJsonTool.getInstance().friend_gettome(getApplicationContext());
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				if(result.startsWith(HttpJsonTool.SUCCESS)){
					refreshRequestData();
				}else{
				}
			}
		};
		task.execute();
	}
	private void agreeFriendsAdd(){
		progressBar.setVisibility(View.VISIBLE);
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().friend_gettome(getApplicationContext());
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				if(result.startsWith(HttpJsonTool.SUCCESS)){
					refreshRequestData();
				}else{
				}
			}
		};
		task.execute();
	}
	private void refreshRequestData(){
		data.clear();
		FriendsRequestHelper helper=new FriendsRequestHelper(getApplicationContext());
		ArrayList<FriendsRequestHolder>holders=helper.selectData();
		helper.close();
		UserInfoHelper user_helper=new UserInfoHelper(getApplicationContext());
		for(FriendsRequestHolder holder : holders){
			Map<String,Object>map=new HashMap<String, Object>();
			UserInfoHolder user_holder=user_helper.selectData_Id(holder.getUser_id());
			if(user_holder==null){
				continue;
			}
			map.put(FriendsRequestHelper.ID, holder.getId());
			map.put(FriendsRequestHelper.TYPE, holder.getType());
			map.put(FriendsRequestHelper.USER_ID, holder.getUser_id());
			map.put(FriendsRequestHelper.TIME, holder.getTime());
			map.put(UserInfoHelper.NICK_NAME, user_holder.getNickname());
			map.put(UserInfoHelper.HEAD, user_holder.getHead());
			data.add(map);
		}
		user_helper.close();
		adapter.notifyDataSetChanged();
	}
	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		title_middle.setText("∫√”—«Î«Û");
		titleback.setVisibility(View.VISIBLE);
		titleRight.setVisibility(View.VISIBLE);
    	titleRight.setImageResource(R.drawable.btn_top_add_bg);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_img_back:
			finish();
			break;
		case R.id.title_right_imgbtn:
			Intent intent=new Intent(getApplicationContext(),AddFriendsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}


	@Override
	public void agree(int position) {
		// TODO Auto-generated method stub
		
	}
}
