package com.android.together.popupwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.android.together.BasePopupWindow;
import com.android.together.R;

public class LoadingPopupwindow extends BasePopupWindow {

	private TextView tv_loading;
	
	private String loadingContent = "Мгдижа...";
	
	public LoadingPopupwindow(Context context){
		super(LayoutInflater.from(context).inflate(R.layout.popup_loading, null)
			,LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT , context);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
		
		setFocusable(false);
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		tv_loading = (TextView) findViewById(R.id.tv_loading);
		tv_loading.setText(getLoadingContent());
	}

	@Override
	public void initEvents() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		
	}

	public String getLoadingContent() {
		return loadingContent;
	}

	public void setLoadingContent(String loadingContent) {
		this.loadingContent = loadingContent;
	}
	
	

}
