package com.android.together.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.android.together.BaseFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

	private List<BaseFragment> fragList;
	private List<String> titleList;
	public MyFragmentPagerAdapter(FragmentManager fm,List<BaseFragment> fragList,List<String> titleList) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fragList=fragList;
//		this.titleList=titleList;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return fragList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fragList.size();
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return "";
	}
	

}
