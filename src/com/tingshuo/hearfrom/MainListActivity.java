package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.view.MenuItem;
import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.tool.ActivityTool;
import com.tingshuo.tool.L;
import com.tingshuo.tool.db.Pager;
import com.tingshuo.tool.db.TopicZanHelper;
import com.tingshuo.tool.db.TopicZanHolder;
import com.tingshuo.tool.db.lock;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.tool.view.adapter.commentBtnClickListener;
import com.tingshuo.tool.view.adapter.mainPostAdapter;
import com.tingshuo.tool.view.popupwin.filterPopupwin;
import com.tingshuo.tool.view.popupwin.filterPopupwin.popupClickCallBack;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.Mode;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshListView;
import com.tingshuo.web.http.HttpJsonTool;

public class MainListActivity extends BaseAcivity implements
		OnRefreshListener2<ListView>,mainPostAdapter.ItemClickListener,commentBtnClickListener {
	private Pager mPager;
	private PullToRefreshListView mainpostListView;
	private ArrayList<Map<String, Object>> listData;
	private mainPostAdapter adapter;
	private filterPopupwin mfilterPopupwin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_list);
		initContentView();
	}
	@Override
	protected void initContentView() {
		super.initContentView();
		titleLeft.setVisibility(View.VISIBLE);
		listData = new ArrayList<Map<String, Object>>();
		mPager = new Pager(0, Pager.Default_Page);
		adapter = new mainPostAdapter(MainListActivity.this, listData);
		adapter.setItemClickListener(this);
		adapter.setCommentlistener(this);
		mainpostListView = (PullToRefreshListView) findViewById(R.id.main_post_list);
		mainpostListView.setMode(Mode.BOTH);
		mainpostListView.setOnRefreshListener(this);
		mainpostListView.setAdapter(adapter);
		mainpostListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				position = position - mainpostListView.getRefreshableView().getHeaderViewsCount();
				Intent intent=new Intent(MainListActivity.this,TopicDetailActivity.class);
				intent.putExtra(mainPostListHelper.ID
						, (Integer)(listData.get(position).get(mainPostListHelper.ID)));
				intent.putExtra("hidekeyboard", true);
				startActivity(intent);
			}
		});
		refreshList(-1, -1, mPager.pagesize, false);
	}

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		// TODO Auto-generated method stub
		getSupportMenuInflater().inflate(R.menu.main_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_new:
			Intent intent = new Intent(getApplicationContext(),
					publishTopickActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	AsyncTask<Void, Void, String> mainpostDatetask = null;

	private void refreshList(final int maxId, final int minId, final int page,
			final boolean more) {
		if (more) {
			mPager.curpage++;
		}
		mainpostDatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getTingshuoList(
						getApplicationContext(), maxId, minId, page,mRole_id,mSex);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				L.i(result);
				boolean hasmore = false;
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					ActivityTool.gotoLoginView(getApplicationContext());
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					//T.showLong(getApplicationContext(),
							//result.replace(HttpJsonTool.ERROR, ""));
					hasmore = refreshData(more);
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					hasmore = refreshData(more);
				}
				if (more) {
					adapter.notifyDataSetChanged();
				} 
				mainpostListView.onRefreshComplete();
				mainpostListView.setMode(hasmore ? Mode.BOTH
						: Mode.PULL_FROM_START);
				adapter.notifyDataSetChanged();
			}
		};
		mainpostDatetask.execute();
	}
    
	private boolean refreshData(boolean more) {
		if (!more) {
			listData.clear();
		}
		mainPostListHelper helper = new mainPostListHelper(
				getApplicationContext());
		ArrayList<mainPostListHolder> holders;
		if (more) {
			holders = helper.selectData(mPager.curpage * mPager.pagesize,
					mPager.pagesize,mRole_id,mSex);
		} else {
			holders = helper.selectData(0, mPager.pagesize,mRole_id,mSex);
		}
		helper.close();
		TopicZanHelper zanHelper=new TopicZanHelper(getApplicationContext());
		for (mainPostListHolder holder : holders) {
			TopicZanHolder zanHolder=zanHelper.selectData_Id(holder.getId());
			if(zanHolder==null){
				zanHolder=new TopicZanHolder(holder.getId(), TopicZanHolder.STATUS_CAI);
			}
			insertHolderData(holder,zanHolder);
		}
		zanHelper.close();
		return holders.size() == mPager.pagesize;

	}

	private void insertHolderData(mainPostListHolder holder,TopicZanHolder zanHolder) {
		mPager.minId = holder.getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(mainPostListHelper.HEAD, holder.getHead());
		data.put(mainPostListHelper.IMAGE, holder.getImage());
		data.put(mainPostListHelper.CONTENT, holder.getContent());
		data.put(mainPostListHelper.CAI_COUNT, holder.getCai_count());
		data.put(mainPostListHelper.COMMENT_COUNT, holder.getComment_count());
		data.put(mainPostListHelper.ZAN_COUNT, holder.getZan_count());
		data.put(mainPostListHelper.NICK_NAME, holder.getNickname());
		data.put(mainPostListHelper.ID, holder.getId());
		data.put(mainPostListHelper.USER_ID, holder.getUser_id());
		data.put(mainPostListHelper.TIME, holder.getTime());
		data.put(TopicZanHelper.STATUS, zanHolder.getStatus());
		listData.add(data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mainpostListView.setMode(refreshData(false) ? Mode.BOTH
				: Mode.PULL_FROM_START);
		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(mainpostDatetask!=null){
			mainpostDatetask.cancel(true);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		mPager.curpage = 0;
		refreshList(-1, -1, mPager.pagesize, false);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub
		refreshList(-1, mPager.minId, mPager.pagesize, true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_left:
			if(mfilterPopupwin==null){
				mfilterPopupwin=new filterPopupwin(MainListActivity.this,callBack);
				mfilterPopupwin.showAsDropDown(titleLeft);
			}else{
				mfilterPopupwin.showAsDropDown(titleLeft);
			}
			break;
		case R.id.title_txt_right:
			break;
		default:
			break;
		}
	}
	private int mRole_id=-1;
	private int mSex=-1;
	popupClickCallBack callBack=new popupClickCallBack() {
		
		@Override
		public void clicked(int role_id, int sex) {
			// TODO Auto-generated method stub
			mRole_id = role_id;
			mSex = sex;
			mainpostDatetask.cancel(true);
			mainpostListView.onRefreshComplete();
			mainpostListView.setRefreshing(true);
		}
	};
	@Override
	public void onZanCheckChange(int position,int zanCount, int status) {
		// TODO Auto-generated method stub
		if(position<0||position>=listData.size()){
			return;
		}
		int id=(Integer)(listData.get(position).get(mainPostListHelper.ID));
		TopicZanHelper helper=new TopicZanHelper(getApplicationContext());
		TopicZanHolder holder=new TopicZanHolder(id, status);
		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
		}
		helper.close();
		mainPostListHelper mainposthelper=new mainPostListHelper(getApplicationContext());
		synchronized (lock.Lock) {
			mainposthelper.updataZanCount(id,zanCount,mainposthelper.getWritableDatabase());
		}
		mainposthelper.close();
		zanMainPost(id,status==TopicZanHolder.STATUS_ZAN);
	}
	
	private void zanMainPost(final int post_id,final boolean isZan){
		AsyncTask<Void, Void, String>task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().setZanMainPost(getApplicationContext(), post_id,isZan);
			}
		};
		task.execute();
	}
	@Override
	public void onCommentBtnClickClick(int position) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(getApplicationContext(),
				TopicDetailActivity.class);
		intent.putExtra(mainPostListHelper.ID
				, (Integer)(listData.get(position).get(mainPostListHelper.ID)));
		intent.putExtra("hidekeyboard", false);
		startActivity(intent);
	}
}
