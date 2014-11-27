package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.actionbarsherlock.view.MenuItem;
import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.ActivityTool;
import com.tingshuo.tool.L;
import com.tingshuo.tool.RoleUtil;
import com.tingshuo.tool.db.CommentHelper;
import com.tingshuo.tool.db.CommentHolder;
import com.tingshuo.tool.db.CommentZanHelper;
import com.tingshuo.tool.db.CommentZanHolder;
import com.tingshuo.tool.db.Pager;
import com.tingshuo.tool.db.ResponseListHelper;
import com.tingshuo.tool.db.ResponseListHolder;
import com.tingshuo.tool.db.TopicZanHelper;
import com.tingshuo.tool.db.TopicZanHolder;
import com.tingshuo.tool.db.lock;
import com.tingshuo.tool.db.mainPostListHelper;
import com.tingshuo.tool.db.mainPostListHolder;
import com.tingshuo.tool.view.PullDownListView;
import com.tingshuo.tool.view.PullDownListView.OnRefreshListioner;
import com.tingshuo.tool.view.ResizeLayout;
import com.tingshuo.tool.view.ResizeLayout.OnResizeListener;
import com.tingshuo.tool.view.adapter.commentBtnClickListener;
import com.tingshuo.tool.view.adapter.commitAdapter;
import com.tingshuo.tool.view.adapter.resposeBtnClickListener;
import com.tingshuo.web.http.HttpJsonTool;

public class TopicDetailActivity extends BaseSwipeBaceActivity implements
		OnRefreshListioner, commentBtnClickListener,
		OnResizeListener, resposeBtnClickListener,
		commitAdapter.ItemClickListener {
	private Pager mPager;
	private PullDownListView mainpostListView;
	private ArrayList<Map<String, Object>> listData;
	private commitAdapter adapter;
	private int topick_id;
	private ResizeLayout resize_layout;
	private LinearLayout send_layout;
	private EditText send_edit;
	private Button send_btn;
	private ProgressBar commentlist_progressbar;
//	private View footView;
//	private TextView footTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_detail);
		hidekeyboard = getIntent().getBooleanExtra("hidekeyboard", true);
		topick_id = getIntent().getIntExtra(mainPostListHelper.ID, -1);
		initContentView();
		refreshList(-1, mPager.minId, mPager.pagesize, false);
	}

	@Override
	protected void initContentView() {
		super.initContentView();

		title_middle.setText("正文");
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.GONE);
		listData = new ArrayList<Map<String, Object>>();
		mPager = new Pager(0, Pager.Default_Page);
		adapter = new commitAdapter(TopicDetailActivity.this, listData);
		adapter.setItemClickListener(this);
		adapter.setCommentlistener(this);
		adapter.setResposeBtnClickListener(this);
		mainpostListView = (PullDownListView) findViewById(R.id.topic_respones_list);
		if (topick_id == -1) {
			return;
		}

		mainpostListView.setAutoLoadMore(true);
		mainpostListView.setRefreshListioner(this);
		mainpostListView.mListView.setAdapter(adapter);
		mainpostListView.mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub

			}
		});
		mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		resize_layout = (ResizeLayout) findViewById(R.id.resize_layout);
		resize_layout.setOnResizeListener(this);
		send_layout = (LinearLayout) findViewById(R.id.send_layout);
		send_edit = (EditText) findViewById(R.id.send_edit);
		send_btn = (Button) findViewById(R.id.send_btn);
		send_btn.setOnClickListener(this);

		commentlist_progressbar = (ProgressBar) findViewById(R.id.commentlist_progressbar);
//		footView = getLayoutInflater().inflate(
//				R.layout.cell_footview_nonecomment, null);
//		mainpostListView.getRefreshableView().addFooterView(footView);
//		footView.setVisibility(View.GONE);
//		footTxt = (TextView) footView.findViewById(R.id.txt);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	InputMethodManager mInputMethodManager;
	
	private void toggleSoftInputFromWindow() {
		mInputMethodManager.toggleSoftInputFromWindow(
				send_edit.getWindowToken(), 0,
				InputMethodManager.HIDE_NOT_ALWAYS);
		send_layout.setVisibility(mInputMethodManager.isActive()?View.VISIBLE:View.GONE);
		send_edit.requestFocus();
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
		} else {
			mainpostListView.onLoadMore();
			//commentlist_progressbar.setVisibility(View.VISIBLE);
		}
		mainpostDatetask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().getCommentList(
						getApplicationContext(), maxId, minId, page, topick_id);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//commentlist_progressbar.setVisibility(View.GONE);
				boolean hasmore = false;
				if (result.startsWith(HttpJsonTool.ERROR403)) {
					ActivityTool.gotoLoginView(getApplicationContext());
				} else if (result.startsWith(HttpJsonTool.ERROR)) {
					//T.showLong(getApplicationContext(),
							//result.replace(HttpJsonTool.ERROR, ""));
				} else if (result.startsWith(HttpJsonTool.SUCCESS)) {
					hasmore = refreshData(more);
				}
				if (more) {
					adapter.notifyDataSetChanged();
					mainpostListView.onRefreshComplete();
				} else {
					mainpostListView.onRefreshComplete();
				}
//				footView.setVisibility(!hasmore ? View.VISIBLE : View.GONE);
//				footTxt.setText(listData.size() < 2 ? "还没有回复" : "没有更多回复");
				mainpostListView.setMore(hasmore);
				adapter.notifyDataSetChanged();
			}
		};
		mainpostDatetask.execute();
	}

	private void refreshMainData() {
		mainPostListHelper helper = new mainPostListHelper(
				getApplicationContext());
		mainPostListHolder topicholder = helper.selectData_Id(topick_id);
		helper.close();
		TopicZanHelper zanhelper = new TopicZanHelper(getApplicationContext());
		TopicZanHolder zanholder = zanhelper.selectData_Id(topicholder.getId());
		if (zanholder == null) {
			zanholder = new TopicZanHolder(topicholder.getId(),
					TopicZanHolder.STATUS_CAI);
		}
		insertmainHolderData(topicholder, zanholder, TYPE_TOPIC);
		zanhelper.close();
	}

	private void sendComment(final CommentHolder holder) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().sendComment(
						getApplicationContext(), holder);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				L.i(result);
				mainpostListView.setMore(refreshData(false));
				adapter.notifyDataSetChanged();
			}

		};
		task.execute();
	}
	
	private void sendResponse(final ResponseListHolder holder) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().sendResponse(
						getApplicationContext(), holder);
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				L.i(result);
				mainpostListView.setMore(refreshData(false));
				
				adapter.notifyDataSetChanged();
			}

		};
		task.execute();
	}

	private boolean refreshData(boolean more) {
		if (!more) {
			listData.clear();
			refreshMainData();
		}
		CommentHelper helper = new CommentHelper(getApplicationContext());
		ArrayList<CommentHolder> holders;
		if (more) {
			holders = helper.selectData(topick_id, mPager.curpage
					* mPager.pagesize, mPager.pagesize, -1, -1);
		} else {
			holders = helper.selectData(topick_id, 0, (mPager.curpage + 1)
					* mPager.pagesize, -1, -1);
		}
		helper.close();
		CommentZanHelper zanhelper = new CommentZanHelper(
				getApplicationContext());
		ResponseListHelper respone_helper=new ResponseListHelper(getApplicationContext());
		for (CommentHolder holder : holders) {
			CommentZanHolder zanholder = zanhelper
					.selectData_Id(holder.getId());
			ArrayList<ResponseListHolder>response_holders=respone_helper.selectData_commentid(holder.getId());
			if (zanholder == null) {
				zanholder = new CommentZanHolder(holder.getId(), topick_id,
						CommentZanHolder.STATUS_CAI);
			}
			insertHolderData(holder, zanholder,response_holders, TYPE_COMMIT);
		}
		zanhelper.close();
		respone_helper.close();
		return holders.size() == mPager.pagesize;

	}

	public static final int TYPE_TOPIC = 0;
	public static final int TYPE_COMMIT = 1;
	public static final String TYPE = "type";

	private void insertHolderData(CommentHolder holder,
			CommentZanHolder zanholder,ArrayList<ResponseListHolder> response_holders, int type) {
		mPager.minId = holder.getId();
		L.i(holder.getNickname()+":"+holder.getContent());
		Map<String, Object> data = new HashMap<String, Object>();
		data.put(CommentHelper.HEAD, holder.getHead());
		data.put(CommentHelper.IMAGE, holder.getImage());
		data.put(CommentHelper.CONTENT, holder.getContent());
		data.put(CommentHelper.CAI_COUNT, holder.getCai_count());
		data.put(CommentHelper.COMMENT_COUNT, holder.getComment_count());
		data.put(CommentHelper.ZAN_COUNT, holder.getZan_count());
		data.put(CommentHelper.NICK_NAME, holder.getNickname());
		data.put(CommentHelper.ID, holder.getId());
		data.put(CommentHelper.USER_ID, holder.getUser_id());
		data.put(CommentHelper.TIME, holder.getTime());
		data.put(CommentZanHelper.STATUS, zanholder.getStatus());
		data.put("ResponseListHolders",response_holders);
		data.put(TYPE, type);
		listData.add(data);
	}

	private void insertmainHolderData(mainPostListHolder holder,
			TopicZanHolder zanholder, int type) {
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
		data.put(TopicZanHelper.STATUS, zanholder.getStatus());
		data.put(TYPE, type);
		listData.add(data);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mainpostListView.setMore(refreshData(false));
		adapter.notifyDataSetChanged();
		if(!hidekeyboard){
			mainpostListView.post(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					toggleSoftInputFromWindow();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mainpostDatetask != null) {
			mainpostDatetask.cancel(true);
		}
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
		case R.id.send_btn:
			String send = send_edit.getText().toString();
			if (send.trim().length() > 0) {
				send_edit.setText("");
				if(mInputMethodManager.isActive()){
					toggleSoftInputFromWindow();
				}
				send=send.replaceAll("\n", "<br></br>");
				if (commitTYPE == COMMENTBTN) {
					CommentHolder holder = new CommentHolder(-1,
							HearFromApp.user_id, topick_id, "", "", send, "",
							"", "", 0, 0, 0, 0,
							RoleUtil.getDefaultRoleId(getApplicationContext()),
							"", CommentZanHolder.STATUS_CAI);
					sendComment(holder);
				}else if(commitTYPE == RESPONESBTN){
					ResponseListHolder holder = new ResponseListHolder(-1,
							HearFromApp.user_id, clickCommentId, "", "", send, "",
							"", "", 0, 0, 0, 0,
							RoleUtil.getDefaultRoleId(getApplicationContext()),
							"", CommentZanHolder.STATUS_CAI);
					sendResponse(holder);
				}
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onZanCheckChange(int position, int zanCount, int status) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= listData.size()) {
			return;
		}
		int id = (Integer) (listData.get(position).get(mainPostListHelper.ID));
		TopicZanHelper helper = new TopicZanHelper(getApplicationContext());
		TopicZanHolder holder = new TopicZanHolder(id, status);
		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
		}
		helper.close();
		mainPostListHelper mainposthelper = new mainPostListHelper(
				getApplicationContext());
		synchronized (lock.Lock) {
			mainposthelper.updataZanCount(id, zanCount,
					mainposthelper.getWritableDatabase());
		}
		mainposthelper.close();
		zanMainPost(id, status == TopicZanHolder.STATUS_ZAN);
	}

	private void zanMainPost(final int post_id, final boolean isZan) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().setZanMainPost(
						getApplicationContext(), post_id, isZan);
			}
		};
		task.execute();
	}

	@Override
	public void onZanCommentCheckChange(int position, int zanCount, int status) {
		// TODO Auto-generated method stub
		if (position < 0 || position >= listData.size()) {
			return;
		}
		int id = (Integer) (listData.get(position).get(mainPostListHelper.ID));
		CommentZanHelper helper = new CommentZanHelper(getApplicationContext());
		CommentZanHolder holder = new CommentZanHolder(id, topick_id, status);
		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
		}
		helper.close();
		CommentHelper mCommentHelper = new CommentHelper(
				getApplicationContext());
		synchronized (lock.Lock) {
			mCommentHelper.updataZanCount(id, zanCount,
					mCommentHelper.getWritableDatabase());
		}
		mCommentHelper.close();
		zanCommentPost(id, status == CommentZanHolder.STATUS_ZAN);
	}

	private void zanCommentPost(final int post_id, final boolean isZan) {
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().setZanComment(
						getApplicationContext(), post_id, isZan);
			}
		};
		task.execute();
	}

	public static final int COMMENTBTN = 0;
	public static final int RESPONESBTN = 1;
	private int commitTYPE = COMMENTBTN;
	private int clickCommentId=1;
	@Override
	public void onCommentBtnClickClick(int position) {
		// TODO Auto-generated method stub
		commitTYPE = COMMENTBTN;
		toggleSoftInputFromWindow();
	}
	
	@Override
	public void onResposeBtnClick(int position) {
		// TODO Auto-generated method stub
		commitTYPE = RESPONESBTN;
		clickCommentId=(Integer) listData.get(position).get(CommentHelper.ID);
		toggleSoftInputFromWindow();
		
	}

	private boolean hidekeyboard = true;
	private static final int BIGGER = 1;
	private static final int SMALLER = 2;
	private boolean first = true;
	@Override
	public void OnResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		if (first) {
			first = false;
			return;
		}
		int change = BIGGER;
		if (h < oldh) {
			change = SMALLER;
		}
		send_layout.setVisibility(change==SMALLER?View.VISIBLE:View.GONE);
		send_edit.requestFocus();
	}

	@Override
	public void OnLayout(int l, int t, int r, int b) {
		// TODO Auto-generated method stub
//		send_layout.setVisibility(t <0 ? View.GONE : View.VISIBLE);
//		if(t <0){
//			send_edit.requestFocus();
//		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mainpostListView.onRefreshComplete();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		refreshList(-1, mPager.minId, mPager.pagesize, true);
	}
}
