package com.android.together.popupwindow;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

import com.android.together.BasePopupWindow;
import com.android.together.R;

public class LineDetailPopupwindow extends BasePopupWindow implements OnClickListener{

	public LineDetailPopupwindow(Context context){
		super(LayoutInflater.from(context).inflate(R.layout.line_detail, null)
			,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , context);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
	}
	
	@Override
	public void initViews() {

	}

	@Override
	public void initEvents() {

	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View view) {
		
	}

}
