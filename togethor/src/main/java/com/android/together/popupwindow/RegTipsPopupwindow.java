package com.android.together.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.together.BasePopupWindow;
import com.android.together.R;

public class RegTipsPopupwindow extends BasePopupWindow {

	
	private LinearLayout not_set_layout;
	private LinearLayout set_layout;
	
	private TextView tv_context;
	
	public RegTipsPopupwindow(Context context){
		super(LayoutInflater.from(context).inflate(R.layout.popup_reg_tips, null)
			,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT , context);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
		
		setFocusable(false);
		
	}
	
	
	

	@Override
	public void initViews() {
		not_set_layout = (LinearLayout) findViewById(R.id.not_set_layout);
		set_layout = (LinearLayout) findViewById(R.id.set_layout);
		tv_context = (TextView) findViewById(R.id.tv_context);
	}

	@Override
	public void initEvents() {
		not_set_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				RegTipsPopupwindow.this.dismiss();
			}
		});
		
		set_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(mOnSubmitClickListener!=null){
					mOnSubmitClickListener.onClick();
				}
			}
		});
	}
	
	public void setContext(String content){
		tv_context.setText(content);
	}
	
	public void btnContent(int i , String content){
		if(i==0){
			TextView tv_1 = (TextView) not_set_layout.getChildAt(0);
			tv_1.setText(content);
		}else{
			TextView tv_2 = (TextView) set_layout.getChildAt(0);
			tv_2.setText(content);
		}
	}
	
	public void setContentVisible(int i){
		if(i==0){
			not_set_layout.setVisibility(View.GONE);
		}else{
			set_layout.setVisibility(View.GONE);
		}
	}

	@Override
	public void init() {
		
	}

}
