package com.android.together.popupwindow;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.together.BasePopupWindow;
import com.android.together.R;
import com.android.together.annotation.ViewInject;
import com.android.together.view.MyRefreshListView;

public class AddrSearchPopupWindow extends BasePopupWindow{

	
	private EditText et_addr;
	private Button btn_del;
	private MyRefreshListView listView;
	private LinearLayout layout_start;
	private TextView tv_start_addr;
	private LinearLayout layout_end;
	private TextView tv_end_addr;
	
	
	private String str_addr;
	
	private boolean start_or_end;
	
	public AddrSearchPopupWindow(Context context){
		super(LayoutInflater.from(context).inflate(R.layout.popup_sel_addr, null)
			,LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT , context);
		setAnimationStyle(R.style.Popup_Animation_PushDownUp);
	}
	
	
	@Override
	public void initViews() {
		et_addr = (EditText) findViewById(R.id.et_addr);
		btn_del = (Button) findViewById(R.id.btn_del);
		listView = (MyRefreshListView) findViewById(R.id.listview);
	}

	@Override
	public void initEvents() {
		et_addr.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				str_addr = s.toString();
				setStr_addr(str_addr);
				if(str_addr!=null && !str_addr.equals("")){
					btn_del.setVisibility(View.VISIBLE);
				}else{
					btn_del.setVisibility(View.GONE);
				}
				//通过百度地图查找附近的地址列表
			}
		});
		
		btn_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				et_addr.setText(null);
			}
		});
	}

	@Override
	public void init() {
		if(str_addr!=null){
			et_addr.setText(str_addr);
			btn_del.setVisibility(View.VISIBLE);
		}
	}


	public String getStr_addr() {
		return str_addr;
	}


	public void setStr_addr(String str_addr) {
		this.str_addr = str_addr;
	}


	public boolean isStart_or_end() {
		return start_or_end;
	}


	public void setStart_or_end(boolean start_or_end) {
		this.start_or_end = start_or_end;
	}

}
