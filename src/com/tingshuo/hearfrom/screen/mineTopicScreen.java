package com.tingshuo.hearfrom.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.tingshuo.hearfrom.HearFromApp;
import com.tingshuo.hearfrom.LoginActivity;
import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.L;
import com.tingshuo.tool.T;
import com.tingshuo.tool.db.Pager;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.tool.view.adapter.mainPostAdapter;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.Mode;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.tingshuo.tool.view.pulltorefresh.PullToRefreshListView;
import com.tingshuo.web.http.HttpJsonTool;

public class mineTopicScreen extends BaseScreen implements
		OnRefreshListener2<ListView> {
	private Pager mPager;
	private PullToRefreshListView mainpostListView;
	private ArrayList<Map<String, Object>> listData;
	private mainPostAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View settingLayout = inflater.inflate(R.layout.screen_topick_list,  
                container, false);  
		initContentView(settingLayout);
		return settingLayout;
	}
	protected void initContentView(View settingLayout) {
		listData = new ArrayList<Map<String, Object>>();
		mPager = new Pager(0, Pager.Default_Page);
		adapter = new mainPostAdapter(getActivity(), listData);
		mainpostListView = (PullToRefreshListView) settingLayout.findViewById(R.id.main_post_list);
		mainpostListView.setMode(Mode.BOTH);
		mainpostListView.setOnRefreshListener(this);
		mainpostListView.setAdapter(adapter);
		refreshList(-1, -1, mPager.pagesize, false);

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
						getActivity(), maxId, minId, page,-1,-1);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				boolean hasmore = false;
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					gotoLoginView();
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					T.showLong(getActivity(),
							result.replace(HttpJsonTool.ERROR, ""));
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					hasmore = refreshData(more);
				}
				if (more) {
					adapter.notifyDataSetChanged();
					mainpostListView.onRefreshComplete();
				} else {
					mainpostListView.onRefreshComplete();
				}
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
				getActivity());
		ArrayList<mainPostListHolder> holders;
		if (more) {
			L.i(mPager.curpage * mPager.pagesize + "");
			holders = helper.selectData(mPager.curpage * mPager.pagesize,
					mPager.pagesize,-1,-1);
		} else {
			holders = helper.selectData(0, mPager.pagesize,-1,-1);
		}
		helper.close();
		for (mainPostListHolder holder : holders) {
			L.i("id:" + holder.getId() + "");
			insertHolderData(holder);
		}
		return holders.size() == mPager.pagesize;

	}

	private void insertHolderData(mainPostListHolder holder) {
		mPager.minId = holder.getId();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(mainPostListHelper.HEAD, holder.getHead());
		data.put(mainPostListHelper.IMAGE, holder.getImage());
		data.put(mainPostListHelper.CONTENT, holder.getContent());
		data.put(mainPostListHelper.CAI_COUNT, holder.getCai_count());
		data.put(mainPostListHelper.COMMENT_COUNT, holder.getComment_count());
		data.put(mainPostListHelper.ZAN_COUNT, holder.getZan_count());
		data.put(mainPostListHelper.NICK_NAME, holder.getNickname());
		data.put(mainPostListHelper.ID, holder.getNickname());
		data.put(mainPostListHelper.USER_ID, holder.getUser_id());
		data.put(mainPostListHelper.TIME, holder.getTime());
		listData.add(data);
	}

	private void gotoLoginView() {
		HearFromApp.token = "";
		try {
			Intent intent = new Intent(getActivity(),
					LoginActivity.class);
			T.show(getActivity(), "您的账号已在其他设备上登录，请重新登录",
					Toast.LENGTH_LONG);
			startActivity(intent);
		} catch (Exception e) {
		}
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mainpostListView.setMode(refreshData(false) ? Mode.BOTH
				: Mode.PULL_FROM_START);
		adapter.notifyDataSetChanged();
	}
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mainpostDatetask.cancel(true);
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
}
