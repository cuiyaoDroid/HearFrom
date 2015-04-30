package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.db.ChatMessageHelper;
import com.tingshuo.tool.db.ChatMessageHolder;
import com.tingshuo.tool.db.CurMessageListHelper;
import com.tingshuo.tool.db.CurMessageListHolder;
import com.tingshuo.tool.db.Pager;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.db.lock;
import com.tingshuo.tool.im.RongIMTool;
import com.tingshuo.tool.im.RongMessageTYPE;
import com.tingshuo.tool.observer.Observable;
import com.tingshuo.tool.observer.Observer;
import com.tingshuo.tool.view.CirclePageIndicator;
import com.tingshuo.tool.view.MsgListView;
import com.tingshuo.tool.view.MsgListView.IXListViewListener;
import com.tingshuo.tool.view.ResizeLinearLayout;
import com.tingshuo.tool.view.ResizeLinearLayout.OnResizeListener;
import com.tingshuo.tool.view.adapter.FaceAdapter;
import com.tingshuo.tool.view.adapter.FacePageAdeapter;
import com.tingshuo.tool.view.adapter.RongChatAdapter;

public class RongYunChatActivity extends BaseSwipeBaceActivity implements
		OnTouchListener, OnClickListener, IXListViewListener, Observer,
		OnResizeListener {
	public static final String INTENT_EXTRA_USERNAME = RongYunChatActivity.class
			.getName() + ".username";// 昵称对应的key
	private MsgListView mMsgListView;// 对话ListView
	private ViewPager mFaceViewPager;// 表情选择ViewPager
	private int mCurrentPage = 0;// 当前表情页
	private boolean mIsFaceShow = false;// 是否显示表情
	private Button mSendMsgBtn;// 发送消息button
	private ImageButton mFaceSwitchBtn;// 切换键盘和表情的button
	private EditText mChatEditText;// 消息输入框
	private LinearLayout mFaceRoot;// 表情父容器
	private WindowManager.LayoutParams mWindowNanagerParams;
	private InputMethodManager mInputMethodManager;
	private List<String> mFaceMapKeys;// 表情对应的字符串数组
	private int friend_id;
	private UserInfoHolder userInfo;
	private RongChatAdapter mChatAdapter;
	private Pager mPage;
	private List<Map<String, Object>> mChatData = new ArrayList<Map<String, Object>>();
	private ResizeLinearLayout rootview;

	public static int in_use_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rong_chat);
		initContentView();
		initData();// 初始化数据
		initView();// 初始化view
		initFacePage();// 初始化表情
		clearChatCount();
	}
	private void clearChatCount(){
		CurMessageListHelper helper = new CurMessageListHelper(
				getApplicationContext());
		helper.zeroCount(friend_id);
		helper.close();
	}
	@Override
	protected void initContentView() {
		super.initContentView();
		mPage = new Pager(0, Pager.Default_Page);
		friend_id = getIntent().getIntExtra(UserInfoHelper.ID, -1);
		if (friend_id == -1) {
			finish();
			return;
		}
		in_use_id = friend_id;
		UserInfoHelper helper = new UserInfoHelper(getApplicationContext());
		userInfo = helper.selectData_Id(friend_id);
		if (userInfo == null) {
			finish();
			return;
		}
		helper.close();
		title_middle.setText("与" + userInfo.getNickname() + "聊天中");
		titleback.setVisibility(View.GONE);
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.GONE);
		rootview = (ResizeLinearLayout) findViewById(R.id.root);
		rootview.setOnResizeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ChatMessageHelper.mObservable.addObserver(this);
		refreshChatData(false);
	}

	private void refreshChatData(boolean more) {
		if (more) {
			mPage.curpage++;
		} else {
			mChatData.clear();
		}
		ChatMessageHelper helper = new ChatMessageHelper(
				getApplicationContext());
		ArrayList<ChatMessageHolder> holders = helper.selectData(friend_id,
				mPage.curpage * mPage.pagesize, mPage.pagesize);
		helper.close();
		if (more) {
			for (ChatMessageHolder holder : holders) {
				Map<String, Object> data = new HashMap<String, Object>();
				data.put(ChatMessageHelper.TABLE_NAME, holder);
				data.put(UserInfoHelper.NICK_NAME, userInfo.getNickname());
				data.put(UserInfoHelper.HEAD, userInfo.getHead());
				mChatData.add(0, data);
			}
		} else {
			for (int i = holders.size() - 1; i >= 0; i--) {
				ChatMessageHolder holder = holders.get(i);
				Map<String, Object> data = new HashMap<String, Object>();
				data.put(ChatMessageHelper.TABLE_NAME, holder);
				data.put(UserInfoHelper.NICK_NAME, userInfo.getNickname());
				data.put(UserInfoHelper.HEAD, userInfo.getHead());
				mChatData.add(data);
			}
		}
		mChatAdapter.notifyDataSetChanged();
		if (!more) {
			mMsgListView.setSelection(mChatAdapter.getCount() - 1);
		} else {
			if (holders.size() > 0) {
				mMsgListView.setSelection(holders.size());
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ChatMessageHelper.mObservable.deleteObserver(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		in_use_id = -1;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private void initData() {
		// 将表情map的key保存在数组中
		Set<String> keySet = HearFromApp.getInstance().getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}

	private void initView() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mWindowNanagerParams = getWindow().getAttributes();

		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		// 触摸ListView隐藏表情和输入法
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setPullLoadEnable(false);
		mMsgListView.setXListViewListener(this);
		mChatAdapter = new RongChatAdapter(getApplicationContext(), mChatData);
		mMsgListView.setAdapter(mChatAdapter);
		mSendMsgBtn = (Button) findViewById(R.id.send);
		mSendMsgBtn.setEnabled(false);
		mFaceSwitchBtn = (ImageButton) findViewById(R.id.face_switch_btn);
		mChatEditText = (EditText) findViewById(R.id.input);
		mFaceRoot = (LinearLayout) findViewById(R.id.face_ll);
		mFaceViewPager = (ViewPager) findViewById(R.id.face_pager);
		mChatEditText.setOnTouchListener(this);
		mChatEditText.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mWindowNanagerParams.softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
							|| mIsFaceShow) {
						mFaceRoot.setVisibility(View.GONE);
						mIsFaceShow = false;
						// imm.showSoftInput(msgEt, 0);
						return true;
					}
				}
				return false;
			}
		});
		mChatEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (s.length() > 0) {
					mSendMsgBtn.setEnabled(true);
				} else {
					mSendMsgBtn.setEnabled(false);
				}
			}
		});
		mFaceSwitchBtn.setOnClickListener(this);
		mSendMsgBtn.setOnClickListener(this);
	}

	@Override
	public void onRefresh() {
		refreshChatData(true);
		mMsgListView.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// do nothing
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.face_switch_btn:
			if (!mIsFaceShow) {
				mInputMethodManager.hideSoftInputFromWindow(
						mChatEditText.getWindowToken(), 0);
				try {
					Thread.sleep(80);// 解决此时会黑一下屏幕的问题
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mFaceRoot.setVisibility(View.VISIBLE);
				mFaceSwitchBtn.setImageResource(R.drawable.aio_keyboard);
				mIsFaceShow = true;
			} else {
				mFaceRoot.setVisibility(View.GONE);
				mInputMethodManager.showSoftInput(mChatEditText, 0);
				mFaceSwitchBtn
						.setImageResource(R.drawable.qzone_edit_face_drawable);
				mIsFaceShow = false;
			}
			break;
		case R.id.send:// 发送消息
			sendMessage();
			break;
		case R.id.title_img_back:
			finish();
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.msg_listView:
			mInputMethodManager.hideSoftInputFromWindow(
					mChatEditText.getWindowToken(), 0);
			mFaceSwitchBtn
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;
		case R.id.input:
			mInputMethodManager.showSoftInput(mChatEditText, 0);
			mFaceSwitchBtn
					.setImageResource(R.drawable.qzone_edit_face_drawable);
			mFaceRoot.setVisibility(View.GONE);
			mIsFaceShow = false;
			break;

		default:
			break;
		}
		return false;
	}

	private void initFacePage() {
		// TODO Auto-generated method stub
		List<View> lv = new ArrayList<View>();
		for (int i = 0; i < HearFromApp.NUM_PAGE; ++i)
			lv.add(getGridView(i));
		FacePageAdeapter adapter = new FacePageAdeapter(lv);
		mFaceViewPager.setAdapter(adapter);
		mFaceViewPager.setCurrentItem(mCurrentPage);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mFaceViewPager);
		adapter.notifyDataSetChanged();
		mFaceRoot.setVisibility(View.GONE);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				mCurrentPage = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// do nothing
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// do nothing
			}
		});

	}

	private GridView getGridView(int i) {
		// TODO Auto-generated method stub
		GridView gv = new GridView(this);
		gv.setNumColumns(7);
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// 屏蔽GridView默认点击效果
		gv.setBackgroundColor(Color.TRANSPARENT);
		gv.setCacheColorHint(Color.TRANSPARENT);
		gv.setHorizontalSpacing(1);
		gv.setVerticalSpacing(1);
		gv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		gv.setGravity(Gravity.CENTER);
		gv.setAdapter(new FaceAdapter(this, i));
		gv.setOnTouchListener(forbidenScroll());
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == HearFromApp.NUM) {// 删除键的位置
					int selection = mChatEditText.getSelectionStart();
					String text = mChatEditText.getText().toString();
					if (selection > 0) {
						String text2 = text.substring(selection - 1);
						if ("]".equals(text2)) {
							int start = text.lastIndexOf("[");
							int end = selection;
							mChatEditText.getText().delete(start, end);
							return;
						}
						mChatEditText.getText()
								.delete(selection - 1, selection);
					}
				} else {
					int count = mCurrentPage * HearFromApp.NUM + arg2;
					// 注释的部分，在EditText中显示字符串
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// 下面这部分，在EditText中显示表情
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) HearFromApp.getInstance()
									.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// 计算缩放因子
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// 新建立矩阵
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// 设置图片的旋转角度
						// matrix.postRotate(-30);
						// 设置图片的倾斜
						// matrix.postSkew(0.1f, 0.1f);
						// 将图片大小压缩
						// 压缩后图片的宽和高以及kB大小均会变化
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(
								RongYunChatActivity.this, newBitmap);
						String emojiStr = mFaceMapKeys.get(count);
						SpannableString spannableString = new SpannableString(
								emojiStr);
						spannableString.setSpan(imageSpan,
								emojiStr.indexOf('['),
								emojiStr.indexOf(']') + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						mChatEditText.append(spannableString);
					} else {
						String ori = mChatEditText.getText().toString();
						int index = mChatEditText.getSelectionStart();
						StringBuilder stringBuilder = new StringBuilder(ori);
						stringBuilder.insert(index, mFaceMapKeys.get(count));
						mChatEditText.setText(stringBuilder.toString());
						mChatEditText.setSelection(index
								+ mFaceMapKeys.get(count).length());
					}
				}
			}
		});
		return gv;
	}

	// 防止乱pageview乱滚动
	private OnTouchListener forbidenScroll() {
		return new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_MOVE) {
					return true;
				}
				return false;
			}
		};
	}

	private Handler mHandler = new Handler();

	private void sendMessage() {
		String content = mChatEditText.getText().toString();
		int msg_id = RongIMTool.getInstance().sendMessage(
				RongMessageTYPE.MESSAGE_TYPE_TEXT, content,
				String.valueOf(friend_id));
		if (msg_id == -1) {
			Toast.makeText(getApplicationContext(), "发送失败网络状况不好",
					Toast.LENGTH_SHORT);
			return;
		}
		ChatMessageHelper helper = new ChatMessageHelper(
				getApplicationContext());
		long time = System.currentTimeMillis();
		ChatMessageHolder holder = new ChatMessageHolder(msg_id,
				HearFromApp.user_id, time, String.valueOf(HearFromApp.user_id),
				String.valueOf(friend_id), RongMessageTYPE.MESSAGE_TYPE_TEXT,
				content, ChatMessageHolder.STATUS_SENDING);

		CurMessageListHelper curhelper = new CurMessageListHelper(
				getApplicationContext());
		CurMessageListHolder curholder = new CurMessageListHolder(friend_id,
				HearFromApp.user_id, time, String.valueOf(HearFromApp.user_id),
				String.valueOf(friend_id), RongMessageTYPE.MESSAGE_TYPE_TEXT,
				content, ChatMessageHolder.STATUS_SENDING, 0);

		synchronized (lock.Lock) {
			helper.insert(holder, helper.getWritableDatabase());
			curhelper.insert(curholder, helper.getWritableDatabase());
		}
		helper.close();
		curhelper.close();
		mChatEditText.setText("");
	}

	@Override
	public void update(Observable o, final Object msg) {
		// TODO Auto-generated method stub
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (msg instanceof ChatMessageHolder) {
					UserInfoHelper userHelper = new UserInfoHelper(
							getApplicationContext());
					ChatMessageHolder holder = (ChatMessageHolder) msg;
					if (!(holder.getFrom_id().equals(String.valueOf(friend_id)) || holder
							.getTo_id().equals(String.valueOf(friend_id)))) {
						return;
					}
					Map<String, Object> data = new HashMap<String, Object>();
					data.put(ChatMessageHelper.TABLE_NAME, holder);
					data.put(UserInfoHelper.NICK_NAME, userInfo.getNickname());
					data.put(UserInfoHelper.HEAD, userInfo.getHead());
					mChatData.add(data);
					userHelper.close();
					mChatAdapter.notifyDataSetChanged();
					mMsgListView.setSelection(mChatAdapter.getCount() - 1);
				} else if (msg instanceof ChatMessageHelper.UpdataHolder) {
					ChatMessageHelper.UpdataHolder update = (ChatMessageHelper.UpdataHolder) msg;
					for (int i = mChatData.size()-1; i >= 0; i--) {
						ChatMessageHolder holder = (ChatMessageHolder) mChatData
								.get(i).get(ChatMessageHelper.TABLE_NAME);
						if(holder.getId()==update.id){
							holder.setStatus(update.status);
							mChatData.get(i).put(ChatMessageHelper.TABLE_NAME, holder);
							mChatAdapter.notifyDataSetChanged();
							break;
						}
					}

				}
			}
		});
	}

	@Override
	public void OnResize(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		mMsgListView.setSelection(mChatAdapter.getCount() - 1);
	}

	@Override
	public void OnLayout(int l, int t, int r, int b) {
		// TODO Auto-generated method stub

	}

}
