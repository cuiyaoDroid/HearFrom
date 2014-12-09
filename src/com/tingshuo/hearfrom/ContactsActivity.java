package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.L;
import com.tingshuo.tool.db.FriendsListHelper;
import com.tingshuo.tool.db.FriendsListHolder;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.view.SideBar;
import com.tingshuo.web.http.HttpJsonTool;

public class ContactsActivity extends BaseSwipeBaceActivity {

	ExpandableListView expandableListView;
	TextView tViewShowLetter;
	private ContactsExpandableAdapter mAdapter;
	private final static String[] letters = new String[] { "*", "a", "b", "c",
			"d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p",
			"q", "r", "s", "t", "u", "v", "w", "x", "y", "z" };
	private ArrayList<String> letterArray = new ArrayList<String>();
	private ArrayList<String> groupData = new ArrayList<String>();
	private HashMap<String, ArrayList<Map<String, Object>>> lettersDivider 
		= new HashMap<String, ArrayList<Map<String, Object>>>();
	HanyuPinyinOutputFormat outputFormat = new HanyuPinyinOutputFormat();
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contacts);
		initContentView();
		initPinYin();
		for (String letter : letters) {
			letterArray.add(letter);
		}
		tViewShowLetter = (TextView) findViewById(R.id.tView_letter);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		initHeadView();
		expandableListView
				.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						return true;// 屏蔽点击收缩事件
					}
				});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				int user_id = (Integer) lettersDivider.get(
						groupData.get(groupPosition)).get(childPosition).get(UserInfoHelper.ID);
				Intent intent = new Intent(getApplicationContext(),
						RongYunChatActivity.class);
				intent.putExtra(UserInfoHelper.ID, user_id);
				startActivity(intent);
				return false;
			}
		});
		mAdapter = new ContactsExpandableAdapter();
		expandableListView.setAdapter(mAdapter);

		SideBar sideBar = ((SideBar) findViewById(R.id.side_bar));
		sideBar.setOnLetterTouchListener(new SideBar.OnLetterTouchListener() {
			@Override
			public void onLetterTouch(String letter, int position) {
				tViewShowLetter.setVisibility(View.VISIBLE);
				tViewShowLetter.setText(letter);
				for (int i = 0; i < groupData.size(); i++) {
					if (groupData.get(i).equals(letter)) {
						expandableListView.setSelectedGroup(i);
						break;
					}
				}
			}

			@Override
			public void onActionUp() {
				tViewShowLetter.setVisibility(View.GONE);
			}
		});
		sideBar.setShowString(letters);
		refreshFriendsInThread();
		getFriendsList();
		
	}

	private void getFriendsList() {
		if (getFriendsListtask != null) {
			getFriendsListtask.cancel(true);
		}
		getFriendsListtask = new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				HttpJsonTool.getInstance().friend_getlist(
						getApplicationContext());
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressBar.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
				refreshFriendsData();
			}
		};
		getFriendsListtask.execute();
		
	}

	private AsyncTask<Void, Void, String> getFriendsListtask;

	private void refreshFriendsInThread() {
		expandableListView.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
		expandableListView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				refreshFriendsData();
				progressBar.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
			}
		});
		
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (getFriendsListtask != null) {
			getFriendsListtask.cancel(true);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void initPinYin() {
		outputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		outputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
	}

	private synchronized void refreshFriendsData() {
		groupData.clear();
		lettersDivider.clear();
		L.i("lettersDivider  clear");
		FriendsListHelper helper = new FriendsListHelper(
				getApplicationContext());
		ArrayList<FriendsListHolder> holders = helper.selectData();
		helper.close();
		UserInfoHelper userHelper = new UserInfoHelper(getApplicationContext());
		for (FriendsListHolder holder : holders) {
			int user_id = holder.getUser_id();
			UserInfoHolder userHolder = userHelper.selectData_Id(user_id);
			if (userHolder == null) {
				continue;
			}
			if (userHolder.getNickname().length() == 0) {
				continue;
			}
			try {
				String start_char = "*";
				char[] ch = userHolder.getNickname().trim().toCharArray();
				if (Character.toString(ch[0]).matches("[\u4e00-\u9fa5]+")) {
					String[] start = PinyinHelper.toHanyuPinyinStringArray(
							ch[0], outputFormat);
					if (start != null) {
						if (start[0].length() > 0)
							start_char = start[0].substring(0, 1);
					}
				} else {
					start_char = Character.toString(ch[0]);
				}
				if (!letterArray.contains(start_char)) {
					start_char = "*";
				}
				Map<String, Object> data = new HashMap<String, Object>();
				data.put(UserInfoHelper.TABLE_NAME, userHolder);
				data.put(UserInfoHelper.ID, user_id);
				if (lettersDivider.containsKey(start_char)) {
					lettersDivider.get(start_char).add(data);
				} else {
					ArrayList<Map<String, Object>>array=new ArrayList<Map<String,Object>>();
					array.add(data);
					groupData.add(start_char);
					lettersDivider.put(start_char, array);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		userHelper.close();
		mAdapter.notifyDataSetChanged();
	}

	private void initHeadView() {
		View headview = getLayoutInflater().inflate(
				R.layout.expandcell_child_contracts, null);
		TextView tViewChildName = (TextView) headview
				.findViewById(R.id.tView_child_name);
		tViewChildName.setText("新的朋友");
		headview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						FriendsRequestListActivity.class);
				startActivity(intent);
			}
		});
		expandableListView.addHeaderView(headview);
	}

	@Override
	protected void initContentView() {
		// TODO Auto-generated method stub
		super.initContentView();
		title_middle.setText("好友");
		titleRight.setVisibility(View.VISIBLE);
		titleRight.setImageResource(R.drawable.btn_top_add_bg);
		titleback.setVisibility(View.VISIBLE);
	}

	class ContactsExpandableAdapter extends BaseExpandableListAdapter {

		@Override
		public int getGroupCount() {
			return groupData.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return lettersDivider.get(groupData.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			if (!isExpanded) {
				expandableListView.expandGroup(groupPosition);// 展开组
			}
			if (convertView == null) {
				convertView = View.inflate(ContactsActivity.this,
						R.layout.expendcell_group_contracts, null);
				convertView.setTag(new GroupViewHolder(convertView));
			}
			GroupViewHolder holder = (GroupViewHolder) convertView.getTag();
			holder.tViewGroupName.setText(groupData.get(groupPosition));
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(ContactsActivity.this,
						R.layout.expandcell_child_contracts, null);
				convertView.setTag(new ChildViewHolder(convertView));
			}
			ChildViewHolder holder = (ChildViewHolder) convertView.getTag();
			UserInfoHolder userHolder = (UserInfoHolder) lettersDivider.get(
					groupData.get(groupPosition))
					.get(childPosition).get(UserInfoHelper.TABLE_NAME);
			holder.tViewChildName.setText(userHolder.getNickname());
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

		class GroupViewHolder {
			TextView tViewGroupName;

			public GroupViewHolder(View parent) {
				tViewGroupName = (TextView) parent
						.findViewById(R.id.tView_group_name);
			}
		}

		class ChildViewHolder {
			TextView tViewChildName;

			public ChildViewHolder(View parent) {
				tViewChildName = (TextView) parent
						.findViewById(R.id.tView_child_name);
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_right_imgbtn:
			Intent intent = new Intent(getApplicationContext(),
					AddFriendsActivity.class);
			startActivity(intent);
			break;
		case R.id.title_img_back:
			finish();
		default:
			break;
		}
	}
}