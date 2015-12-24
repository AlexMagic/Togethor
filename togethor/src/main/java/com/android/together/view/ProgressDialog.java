package com.android.together.view;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.together.R;


public class ProgressDialog extends AlertDialog{
	
	private String proTitle;
	private TextView tv_titile;
	
	public ProgressDialog(Context context) {
		super(context);
	}
	
	public ProgressDialog(Context context , String proTitle){
		super(context);
		this.proTitle = proTitle;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog_layout);
		
		setCancelable(false);
		
		tv_titile = (TextView) findViewById(R.id.tv_title);
		if(proTitle!=null)
			tv_titile.setText(getProTitle());
	}

	public String getProTitle() {
		return proTitle;
	}

	public void setProTitle(String proTitle) {
		this.proTitle = proTitle;
	}
	
	@Override
	public void onBackPressed() {
		dismiss();
	}
	
}
