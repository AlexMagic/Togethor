package com.android.together.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.entity.Line;
import com.android.together.R;
import com.android.together.common.adapter.CommAdapter;
import com.android.together.common.adapter.ViewHolder;
import com.android.together.view.CircleImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class NearLineAdapter extends CommAdapter<Line> {

	
	private DisplayImageOptions options;
	
	public NearLineAdapter(Context context, List<Line> mDatas, int layoutId) {
		super(context, mDatas, layoutId);
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ub__safetynet_circle_small)
		.showImageForEmptyUri(R.drawable.ub__safetynet_circle_small)
		.showImageOnFail(R.drawable.ub__safetynet_circle_small)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
	}

	@Override
	public void convert(ViewHolder holder) {
		int curPos = holder.getPosition();
		final Line item = mDatas.get(curPos);
		
		
		TextView tv_start_addr = holder.setTextView(R.id.tv_start_addr,item.getStartAddr());
		TextView tv_end_addr = holder.setTextView(R.id.tv_end_addr,item.getEndAddr());
		TextView tv_date = holder.setTextView(R.id.tv_date,item.getDateTime());
		//View v = LayoutInflater.from(context).inflate(R.layout.image_for_people, null);
		
		//ImageView iv_num = holder.getView(R.id.iv_num);
		LinearLayout layout_num = holder.getView(R.id.layout_num);
		int type = item.getType();
		ImageView iv_type = holder.getView(R.id.iv_type);
		if(type==1){
			iv_type.setImageDrawable(context.getResources().getDrawable(R.drawable.bts_home_page_owner));
			TextView tv_for_type = holder.setTextView(R.id.tv_for_type, "剩余接载");
			for(int i=0;i<item.getNum()-item.getLeft_num();i++){
				ImageView iv_num = new ImageView(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				params.setMargins(2, 0, 2, 0); 
				iv_num.setLayoutParams(params);
				iv_num.setImageResource(R.drawable.icon_account);
				layout_num.addView(iv_num);
			}

		}else if(type==0){
			iv_type.setImageDrawable(context.getResources().getDrawable(R.drawable.bts_home_page_passenger));
			TextView tv_for_type = holder.setTextView(R.id.tv_for_type, "上车人数");
			
			for(int i=0;i<item.getNum();i++){
				ImageView iv_num = new ImageView(context);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
				params.setMargins(2, 0, 2, 0); 
				iv_num.setLayoutParams(params);
				iv_num.setImageResource(R.drawable.icon_account);
				layout_num.addView(iv_num);
			}
			
		}
		
//		CircleImageView iv_head = holder.getView(R.id.iv_head);
		ImageView iv = holder.getView(R.id.iv_head);
		ImageLoader.getInstance().displayImage(item.getUrl(), iv,options);
		
		
//		ImageView iv_head = holder.getView(11);
//		setPicForImageView("", iv_head);
//		
//		ImageView iv = holder.getView(R.id.iv_head);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
//				Intent intent = new Intent(context , LineDetailActivity.class);
//				intent.putExtra("info", item);
//				context.startActivity(intent);
				
			}
		});
		
	}
	

}
