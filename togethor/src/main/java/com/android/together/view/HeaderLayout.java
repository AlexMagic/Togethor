package com.android.together.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.together.R;



public class HeaderLayout extends LinearLayout {
	private LayoutInflater mInflater;
	private View mHeader;
	private ImageView mIvLogo;
	private CircleImageView user_head;
	private LinearLayout logoLayout;
	
	private View view_line;
	
	private RelativeLayout root_layout;
	
	private LinearLayout mLayoutLeftContainer;
	private LinearLayout mLayoutMiddleContainer;
	private LinearLayout mLayoutRightContainer;
	private OnIconClickListner onIconClickListner;
	//右边按钮
	private LinearLayout ll_header_layout_right_layout;
	private OnRightLayoutListner onRightLayoutListner;
	//中间按钮
	private LinearLayout ll_header_layout_middle_layout;
	private OnMiddleLayoutListner onMiddleLayoutListner;
	//左边按钮
	private LinearLayout ll_header_layout_left_layout;
	private LinearLayout ll_header_layout_leftview_container;
	private OnLeftLayoutListner onLefttLayoutListner;
	
	private HandyTextView header_htv_text;
	private HandyTextView header_only_text;
	//spinner 按钮
//	private LinearLayout ll_header_layout_spinner_container;
//	private LinearLayout ll_header_spinner_layout;
	
	
	
//	private HeaderSpinner mHsSpinner;
	
	
	public HeaderLayout(Context context) {
		super(context);
		init(context);
	}

	public HeaderLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void init(Context context) {
		mInflater = LayoutInflater.from(context);
		mHeader = mInflater.inflate(R.layout.common_headerbar, null); 
		addView(mHeader);
		initViews();

	}
	
	public void initViews() {
		
		mIvLogo = (ImageView) findViewByHeaderId(R.id.header_iv_logo);
		mIvLogo.setVisibility(View.VISIBLE);
		
		view_line = findViewByHeaderId(R.id.view_line);
		
		logoLayout = (LinearLayout) findViewByHeaderId(R.id.header_back_layout);
		
		root_layout = (RelativeLayout) findViewByHeaderId(R.id.root_header_layout);
		
		ll_header_layout_right_layout = (LinearLayout) findViewByHeaderId(R.id.header_layout_right_layout);
		ll_header_layout_middle_layout = (LinearLayout) findViewByHeaderId(R.id.header_layout_middle_layout);
		ll_header_layout_left_layout = (LinearLayout) findViewByHeaderId(R.id.header_layout_left_layout);
		ll_header_layout_leftview_container = (LinearLayout) findViewById(R.id.header_layout_leftview_container);
		
		header_htv_text = (HandyTextView) findViewById(R.id.header_htv_text);
		header_only_text = (HandyTextView) findViewById(R.id.header_only_text);
//		header_htv_text.setText("hahaha");
//		ll_header_layout_spinner_container = (LinearLayout) findViewById(R.id.header_layout_spinner_container);
//		ll_header_spinner_layout = (LinearLayout) findViewById(R.id.header_spinner_layout);
		
//		mHsSpinner = (HeaderSpinner) findViewByHeaderId(R.id.header_hs_spinner);
		
	}
	
	public View findViewByHeaderId(int id) {
		return mHeader.findViewById(id);
	}
	
	public void init(HeaderStyle style){
		switch(style){
		case DEFAULT_TITLE:
			defaultHeader();
			break;
		case TWO_TITLE_TEXT:
			showTextHeader(false , true , true , false);
			view_line.setVisibility(View.VISIBLE);
			break;
		case TITILE_LEFT_TEXT:
			showTextHeader(true , true, true , false);
			break;
		case TITLE_FOR_HELP:
			showTextHeader(false , false, true , true);
			break;
		case TITLE_TOP:
			showTextHeader(false, true, true, true);
			view_line.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	//默认标题栏
	public void defaultHeader(){
		ll_header_layout_leftview_container.setVisibility(View.GONE);
		ll_header_layout_right_layout.setVisibility(View.GONE);
		ll_header_layout_middle_layout.setVisibility(View.GONE);
		ll_header_layout_left_layout.setVisibility(View.GONE);
		header_htv_text.setVisibility(View.VISIBLE);
//		ll_header_layout_spinner_container.setVisibility(View.GONE);
//		ll_header_spinner_layout.setVisibility(View.GONE);
	}
	
	public void showTextHeader(boolean leftText , boolean midText ,boolean rightText , boolean title){
		defaultHeader();
		if(leftText){
			ll_header_layout_leftview_container.setVisibility(View.VISIBLE);
			ll_header_layout_left_layout.setVisibility(View.VISIBLE);
		}
		
		if(midText){
			ll_header_layout_middle_layout.setVisibility(View.VISIBLE);
		}
		
		if(rightText){
			ll_header_layout_right_layout.setVisibility(View.VISIBLE);
		}
		
		if(title){
			header_htv_text.setVisibility(View.VISIBLE);
		}
	}
	
	public void setRootLayoutColor(int color){
		root_layout.setBackgroundDrawable(null);
		root_layout.setBackgroundColor(color);
	}
	
	public void setHandyText(String text){
		if(header_htv_text.getVisibility() == View.VISIBLE){
			header_htv_text.setText(text);
		}
	}
	
	public void setHandyOnlyText(String text){
		if(header_only_text.getVisibility() == View.VISIBLE){
			header_only_text.setText(text);
		}
	}
	
	public void setIcon(int resId){
		if(resId>0)
			mIvLogo.setImageResource(resId);
		else
			mIvLogo.setImageDrawable(null);
	}
	
//	public void setUserHead(Bitmap bitmap){
//		user_head.setBackground(background);
//	}
	
	public void setIconClickListner(final OnIconClickListner onIconClickListner){
		if(onIconClickListner!=null){
			logoLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					System.out.println("hahahahah");
					onIconClickListner.onClick();
				}
			});
		}
	}
	
	public void setLayoutLinster(final OnLeftLayoutListner leftLisnter , 
			final OnMiddleLayoutListner midLisnter ,final OnRightLayoutListner rightLisnter){
		
		if(leftLisnter!=null){
			ll_header_layout_right_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					leftLisnter.onClick();
				}
			});
		}
		
		if(midLisnter!=null){
			ll_header_layout_middle_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					midLisnter.onClick();
				}
			});
		}
		
		if(rightLisnter!=null){
			ll_header_layout_right_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					rightLisnter.onClick();
				}
			});
		}
	}
	
	
	
//	public HeaderSpinner setSpinner(ArrayList<String> titile , onSpinnerClickListener onSpinnerListner){
//		
//		mHsSpinner.setText(titile.get(0));
//		mHsSpinner.setOnSpinnerClickListener(onSpinnerListner);
//		
//		return mHsSpinner;
//	}
	
	
	public void setOnLeftLayoutListner(OnLeftLayoutListner l){
		this.onLefttLayoutListner = l;
	}
	
	public void setOnMiddleLayoutListner(OnMiddleLayoutListner l){
		this.onMiddleLayoutListner = l;
	}
	
	public void setOnRightLayoutListner(OnRightLayoutListner l){
		this.onRightLayoutListner = l;
	}
	
//	public void setOnIconClickListner(OnIconClickListner onIconClickListner){
//		System.out.println("setttt");
//		this.onIconClickListner = onIconClickListner;
//	}
	
	
	public interface OnLeftLayoutListner{
		void onClick();
	}
	
	
	public interface OnMiddleLayoutListner{
		void onClick();
	}
	
	public interface OnRightLayoutListner{
		void onClick();
	}
	
	public interface OnIconClickListner{
		void onClick();
	}
	
	public enum HeaderStyle {
		DEFAULT_TITLE, TWO_TITLE_TEXT,  TITILE_LEFT_TEXT , TITLE_FOR_HELP , TITLE_TOP;
	}
	
	public void setTextForItem(int pos , String text){
		switch(pos){
		case 0:
			break;
		case 1:
			TextView tv_mid = (TextView)ll_header_layout_right_layout.findViewById(R.id.header_ib_middle_tv);
			tv_mid.setText(text);
			break;
		case 2:
			TextView tv_right = (TextView)ll_header_layout_right_layout.findViewById(R.id.header_ib_right_tv);
			tv_right.setText(text);
			break;
		}
	}
}
