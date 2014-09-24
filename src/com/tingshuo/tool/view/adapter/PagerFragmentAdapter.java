package com.tingshuo.tool.view.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

//适配器
public class PagerFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	private List<String> titleList;

	public PagerFragmentAdapter(FragmentManager fm,
			List<Fragment> fragmentList, List<String> titleList) {
		super(fm);
		this.fragmentList = fragmentList;
		this.titleList = titleList;
	}

	// ViewPage中显示的内容
	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return (fragmentList == null || fragmentList.size() == 0) ? null
				: fragmentList.get(arg0);
	}

	// Title中显示的内容
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return (titleList.size() > position) ? titleList.get(position) : "";
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragmentList == null ? 0 : fragmentList.size();
	}

}