package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.tingshuo.hearfrom.base.BaseSwipeBaceActivity;
import com.tingshuo.tool.db.UserInfoHelper;
import com.tingshuo.tool.db.UserInfoHolder;
import com.tingshuo.tool.view.CirclePageIndicator;
import com.tingshuo.tool.view.MsgListView;
import com.tingshuo.tool.view.MsgListView.IXListViewListener;
import com.tingshuo.tool.view.adapter.FaceAdapter;
import com.tingshuo.tool.view.adapter.FacePageAdeapter;

public class RongYunChatActivity extends BaseSwipeBaceActivity implements OnTouchListener,
		OnClickListener, IXListViewListener {
	public static final String INTENT_EXTRA_USERNAME = RongYunChatActivity.class
			.getName() + ".username";// �ǳƶ�Ӧ��key
	private MsgListView mMsgListView;// �Ի�ListView
	private ViewPager mFaceViewPager;// ����ѡ��ViewPager
	private int mCurrentPage = 0;// ��ǰ����ҳ
	private boolean mIsFaceShow = false;// �Ƿ���ʾ����
	private Button mSendMsgBtn;// ������Ϣbutton
	private ImageButton mFaceSwitchBtn;// �л����̺ͱ����button
	private EditText mChatEditText;// ��Ϣ�����
	private LinearLayout mFaceRoot;// ���鸸����
	private WindowManager.LayoutParams mWindowNanagerParams;
	private InputMethodManager mInputMethodManager;
	private List<String> mFaceMapKeys;// �����Ӧ���ַ�������
	private int user_id;
	private UserInfoHolder userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rong_chat);
		initContentView();
		initData();// ��ʼ������
		initView();// ��ʼ��view
		initFacePage();// ��ʼ������
		
	}
	@Override
	protected void initContentView() {
		super.initContentView();
		user_id=getIntent().getIntExtra(UserInfoHelper.ID, -1);
		if(user_id==-1){
			finish();
			return;
		}
		
		UserInfoHelper helper= new UserInfoHelper(getApplicationContext());
		userInfo=helper.selectData_Id(user_id);
		if(userInfo==null){
			finish();
			return;
		}
		helper.close();
		title_middle.setText("��"+userInfo.getNickname()+"������");
		titleback.setVisibility(View.GONE);
		titleback.setVisibility(View.VISIBLE);
		title_right.setVisibility(View.GONE);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private void initData() {
		// ������map��key������������
		
		Set<String> keySet = HearFromApp.getInstance().getFaceMap().keySet();
		mFaceMapKeys = new ArrayList<String>();
		mFaceMapKeys.addAll(keySet);
	}


	private void initView() {
		mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		mWindowNanagerParams = getWindow().getAttributes();

		mMsgListView = (MsgListView) findViewById(R.id.msg_listView);
		// ����ListView���ر�������뷨
		mMsgListView.setOnTouchListener(this);
		mMsgListView.setPullLoadEnable(false);
		mMsgListView.setXListViewListener(this);
		mSendMsgBtn = (Button) findViewById(R.id.send);
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
					Thread.sleep(80);// �����ʱ���һ����Ļ������
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
		case R.id.send:// ������Ϣ
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
		gv.setSelector(new ColorDrawable(Color.TRANSPARENT));// ����GridViewĬ�ϵ��Ч��
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
				if (arg2 == HearFromApp.NUM) {// ɾ������λ��
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
					// ע�͵Ĳ��֣���EditText����ʾ�ַ���
					// String ori = msgEt.getText().toString();
					// int index = msgEt.getSelectionStart();
					// StringBuilder stringBuilder = new StringBuilder(ori);
					// stringBuilder.insert(index, keys.get(count));
					// msgEt.setText(stringBuilder.toString());
					// msgEt.setSelection(index + keys.get(count).length());

					// �����ⲿ�֣���EditText����ʾ����
					Bitmap bitmap = BitmapFactory.decodeResource(
							getResources(), (Integer) HearFromApp.getInstance()
									.getFaceMap().values().toArray()[count]);
					if (bitmap != null) {
						int rawHeigh = bitmap.getHeight();
						int rawWidth = bitmap.getHeight();
						int newHeight = 40;
						int newWidth = 40;
						// ������������
						float heightScale = ((float) newHeight) / rawHeigh;
						float widthScale = ((float) newWidth) / rawWidth;
						// �½�������
						Matrix matrix = new Matrix();
						matrix.postScale(heightScale, widthScale);
						// ����ͼƬ����ת�Ƕ�
						// matrix.postRotate(-30);
						// ����ͼƬ����б
						// matrix.postSkew(0.1f, 0.1f);
						// ��ͼƬ��Сѹ��
						// ѹ����ͼƬ�Ŀ��͸��Լ�kB��С����仯
						Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0,
								rawWidth, rawHeigh, matrix, true);
						ImageSpan imageSpan = new ImageSpan(RongYunChatActivity.this,
								newBitmap);
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

	// ��ֹ��pageview�ҹ���
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


}