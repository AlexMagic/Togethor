package com.android.togethor.activity;

import java.util.HashMap;
import java.util.Map;

import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.view.HeaderLayout;
import com.android.together.view.HeaderLayout.HeaderStyle;
import com.android.together.view.HeaderLayout.OnIconClickListner;
import com.android.together.view.HeaderLayout.OnRightLayoutListner;
import com.android.volley.Request;

@ContentView(value = R.layout.activity_end)
public class EndLineActivity extends BaseActivity {

	
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		
		
		
		
		headerLayout.setIcon(R.drawable.bg_btn_header_back);
		headerLayout.setHandyOnlyText("ÐÐ³Ì½áÊø");
		headerLayout.setIconClickListner(new OnIconClickListner() {
			
			@Override
			public void onClick() {
				startActivity(NearLineActivity.class);
				finish();
			}
		});
		
		Map<String , String> map = new HashMap<String, String>();
		map.put("method", "set_line_status");
		map.put("line_id", MyApplication.getInstance().getLineId()+"");
		map.put("line_status", 3+"");
		
		string_request(Constant.LINE_URL, Request.Method.POST, map);
		
	}
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		super.respone(response);
		MyApplication.getInstance().setLineStatus(3);
	}
	
	@Override
	public void setListner() {
		// TODO Auto-generated method stub

	}

}
