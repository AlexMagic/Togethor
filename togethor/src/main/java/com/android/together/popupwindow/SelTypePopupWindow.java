package com.android.together.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.android.together.BasePopupWindow;
import com.android.together.R;
import com.android.togethor.activity.PublishLineActivity;

public class SelTypePopupWindow extends BasePopupWindow {

	private Button btn_pass;
	private Button btn_driver;
	
	private int sel_type = 0;
	
	private OnClickListener onDriverClickListener;
	private OnClickListener onPassClickListener;
	
	public SelTypePopupWindow(Context context) {
		super(LayoutInflater.from(context).inflate(R.layout.popup_type, null)
				,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , context);
			setAnimationStyle(R.style.Popup_Animation_PushDownUp);
	}
	
	@Override
	public void initViews() {
		btn_pass = (Button) findViewById(R.id.btn_pass);
		btn_driver = (Button) findViewById(R.id.btn_driver);
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub
		if(onPassClickListener!=null){
			btn_pass.setOnClickListener(onPassClickListener);
		}
		
		if(onDriverClickListener!=null){
			btn_driver.setOnClickListener(onDriverClickListener);
		}
		
//		btn_pass.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				SelTypePopupWindow.this.dismiss();
//				Intent publish_intent = new Intent(context , PublishLineActivity.class);
//				publish_intent.putExtra("type", 0);
//				context.startActivity(publish_intent);
//				((Activity) context).overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
//				
//			}
//		});
//		
//		btn_driver.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				SelTypePopupWindow.this.dismiss();
//				Intent publish_intent = new Intent(context , PublishLineActivity.class);
//				publish_intent.putExtra("type", 1);
//				context.startActivity(publish_intent);
//				((Activity) context).overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
//			}
//		});
	}

	@Override
	public void init() {

	}

	public int getSel_type() {
		return sel_type;
	}

	public OnClickListener getOnDriverClickListener() {
		return onDriverClickListener;
	}

	public void setOnDriverClickListener(OnClickListener onDriverClickListener) {
		this.onDriverClickListener = onDriverClickListener;
	}

	public OnClickListener getOnPassClickListener() {
		return onPassClickListener;
	}

	public void setOnPassClickListener(OnClickListener onPassClickListener) {
		this.onPassClickListener = onPassClickListener;
	}

	
}
