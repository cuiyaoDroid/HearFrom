package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.tool.db.CurMessageListHelper;
import com.tingshuo.tool.db.CurMessageListHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.observer.Observable;
import com.tingshuo.tool.observer.Observer;
import com.tingshuo.tool.view.adapter.MessageListAdapter;

public class messageCenterActivity extends BaseAcivity implements Observer {
	private ListView messageList;
	private MessageListAdapter adapter;
	private List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
	private ProgressBar progressBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_cent);
		initContentView();
		messageList = (ListView) findViewById(R.id.message_list);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		messageList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				int user_id = (Integer) data.get(position).get(
						CurMessageListHelper.ID);
				Intent intent = new Intent(getApplicationContext(),
						RongYunChatActivity.class);
				intent.putExtra(UserInfoHelper.ID, user_id);
				startActivity(intent);
				CurMessageListHelper helper = new CurMessageListHelper(
						getApplicationContext());
				helper.zeroCount(user_id);
				helper.close();
			}
		});
		adapter = new MessageListAdapter(getApplicationContext(), data);
		messageList.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshMessageData();
		progressBar.setVisibility(View.GONE);
		CurMessageListHelper.mObservable.addObserver(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CurMessageListHelper.mObservable.deleteObserver(this);
	}

	private void refreshMessageData() {
		data.clear();
		CurMessageListHelper helper = new CurMessageListHelper(
				getApplicationContext());
		ArrayList<CurMessageListHolder> holders = helper.selectData();
		helper.close();
		UserInfoHelper user_helper = new UserInfoHelper(getApplicationContext());
		for (CurMessageListHolder holder : holders) {
			Map<String, Object> map = new HashMap<String, Object>();
			UserInfoHolder user_holder = user_helper.selectData_Id(holder
					.getId());
			if (user_holder == null) {
				continue;
			}
			map.put(CurMessageListHelper.ID, holder.getId());
			map.put(CurMessageListHelper.TYPE, holder.getType());
			map.put(CurMessageListHelper.USER_ID, holder.getUser_id());
			map.put(CurMessageListHelper.TIME, holder.getTime());
			map.put(CurMessageListHelper.CONTENT, holder.getContent());
			map.put(CurMessageListHelper.COUNT, holder.getCount());
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
			Intent intent = new Intent(getApplicationContext(),
					ContactsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	private Handler mHandler=new Handler();
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		int op=(Integer)arg;
		if(op==CurMessageListHelper.OB_OP_ZERO){
			return;
		}
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshMessageData();
			}
		});
	}
}
