package com.tingshuo.hearfrom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tingshuo.hearfrom.base.BaseSwipeFragmentActivity;
import com.tingshuo.hearfrom.screen.mineSecondScreen;
import com.tingshuo.hearfrom.screen.mineTopicScreen;
import com.tingshuo.tool.view.SplitViewPager;
import com.tingshuo.tool.view.SplitViewPager.OnPageScroll;
import com.tingshuo.tool.view.adapter.PagerFragmentAdapter;

public class MyContentActivity extends BaseSwipeFragmentActivity  {
	private SplitViewPager mPager;// ҳ������
	private ImageView cursor;// ����ͼƬ
	private TextView t1, t2, t3;// ҳ��ͷ��
	private int bmpW;// ����ͼƬ���
	int a = 0;
	/** ����ֵ **/
	int offValue = 0;
	TranslateAnimation animation;
	float x;
	private List<Fragment> fragmentList;
	private List<String> titleList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_content_mine);
		InitImageView();
		InitTextView();
		initContentView();
		InitViewPager();
	}
	@Override
	protected void initContentView() {
		super.initContentView();
		titleLeft.setVisibility(View.VISIBLE);
		titleLeft.setText("����");
		title_right.setVisibility(View.GONE);
		title_middle.setText("�ҵ�����");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_txt_right:
			break;
		case R.id.title_txt_left:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʼ��ͷ��
	 */
	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(new splitOnClickListener(0));
		t2.setOnClickListener(new splitOnClickListener(1));
		t3.setOnClickListener(new splitOnClickListener(2));
	}

	/**
	 * ��ʼ��ViewPager
	 */
	private void InitViewPager() {
		mPager = (SplitViewPager) findViewById(R.id.vPager);
		mPager.setPageScrollListener(new OnPageScroll() {

			@Override
			public void onScroll(int left) {

				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) cursor
						.getLayoutParams();
				int b = 0;
				if ((left - a) > 0) {

					b = lp.leftMargin + (left + 1 - a) / 3;
				} else {
					b = lp.leftMargin + (left - 1 - a) / 3;

				}
				if (b < 0) {
					lp.leftMargin = 0;
				} else {
					lp.leftMargin = b;
				}
				if (left == 0) {
					lp.leftMargin = 0;
				} else if (left == 720) {
					lp.leftMargin = 240;
				} else if (left == 1440) {
					lp.leftMargin = 480;
				}
				cursor.setLayoutParams(lp);
				a = left;
			}

		});
		fragmentList = new ArrayList<Fragment>();
		titleList = new ArrayList<String>();// ��������
		for (int i = 0; i < 2; i++) {
            mineTopicScreen mViewPagerFragment = new mineTopicScreen();
            Bundle bundle = new Bundle();
            mViewPagerFragment.setArguments(bundle);// ���ò���
            titleList.add("Title " + (i+1));
            fragmentList.add(mViewPagerFragment);
        }
		
		 mineSecondScreen mViewPagerFragment = new mineSecondScreen();
         Bundle bundle = new Bundle();
         mViewPagerFragment.setArguments(bundle);// ���ò���
         titleList.add("Title " + 3);
         fragmentList.add(mViewPagerFragment);
		
		mPager.setAdapter(new PagerFragmentAdapter(getSupportFragmentManager(),fragmentList,titleList));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * ��ʼ������
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		// bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
		// .getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		bmpW = screenW / 3;
		LayoutParams lp = cursor.getLayoutParams();
		lp.width = bmpW;
		cursor.setLayoutParams(lp);
		// ����������ֵ
		offValue = new BigDecimal(
				getResources().getDisplayMetrics().widthPixels / 120.0)
				.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
		System.out.println("--����ֵ " + offValue);
	}

	/**
	 * ViewPager������
	 */
	private int clickIndex = 0;

	/**
	 * ͷ��������
	 */
	public class splitOnClickListener implements View.OnClickListener {
		private int index = 0;

		public splitOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			clickIndex = index;
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int arg0) {
			clickIndex = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}
}
