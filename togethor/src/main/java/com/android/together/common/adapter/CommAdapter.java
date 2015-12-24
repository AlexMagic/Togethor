package com.android.together.common.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.entity.Entity;
import com.android.together.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;


public abstract class CommAdapter<T> extends BaseAdapter {

	protected LayoutInflater mLayoutInflater;
	protected List<T> mDatas;
	protected Context context;
	protected int layoutId;
	
	private DisplayImageOptions options;
	
	public CommAdapter(Context context , List<T> mDatas , int layoutId){ 
		this.context = context;
		this.mDatas = mDatas;
		this.mLayoutInflater = LayoutInflater.from(context);
		this.layoutId = layoutId;
		
		initOptions();
	}
	
	public void initOptions(){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.abc_ab_bottom_solid_dark_holo)
		.showImageForEmptyUri(R.drawable.abc_ab_bottom_solid_light_holo)
		.showImageOnFail(R.drawable.abc_ab_bottom_transparent_light_holo)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getViewHolder(context, convertView, parent, layoutId, position);
		convert(holder);
		return holder.getConvertView();
	}
	
	public abstract void convert(ViewHolder holder);
	
	
	public void setPicForImageView(String pic_url , ImageView imageView){
		ImageLoader.getInstance().displayImage(pic_url, imageView,options);
	}

}
