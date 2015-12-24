package com.android.togethor.activity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.entity.Line;
import com.android.entity.User;
import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.util.JsonUtil;
import com.android.together.view.HeaderLayout;
import com.android.together.view.ProgressDialog;
import com.android.volley.Request;
import com.nostra13.universalimageloader.core.ImageLoader;


@ContentView(value = R.layout.pre_waiting_passenger)
public class DriverWaitingActivity extends BaseActivity {

	
	private int line_id;

	private Line line;
	User driver = new User();
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.btn_start)
	private Button btn_start;
	
	@ViewInject(R.id.btn_cancel)
	private Button btn_cancel;
	
	@ViewInject(R.id.tv_start_addr)
	private TextView tv_start_addr;
	
	@ViewInject(R.id.tv_end_addr)
	private TextView tv_end_addr;
	
	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	
	@ViewInject(R.id.tv_driver)
	private TextView tv_driver;
	
	@ViewInject(R.id.tv_car_name)
	private TextView tv_car_name;
	
	@ViewInject(R.id.tv_lincense)
	private TextView tv_lincense;
	
	@ViewInject(R.id.layout_driver)
	private LinearLayout layout_driver;
	
	@ViewInject(R.id.layout_loading)
	private LinearLayout layout_loading;
	
	@ViewInject(R.id.iv_head)
	private ImageView circleImageView;
	
//	private ProgressDialog progressDialog;
	private int curRequest = -1;
	@Override
	public void init() {
		super.init();
		btn_start.setVisibility(View.GONE);
		initMenu();
		headerLayout.setIcon(R.drawable.bg_sliding_btn);
		headerLayout.setHandyOnlyText("准备行程");
		
		
		int line_id = getIntent().getIntExtra("line_id", -1);
//		progressDialog = new ProgressDialog(DriverWaitingActivity.this, "加载中...");
//		progressDialog.show();
		
		Map<String , String> map = new HashMap<String, String>();
		map.put("method", "sel_line_by_id");
		map.put("line_id", line_id+"");
		
		MyApplication.getInstance().setLineStatus(0);
		
		curRequest = 0;
		string_request(Constant.LINE_URL, Request.Method.POST, map);
		
	}
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		super.respone(response);
		
		Map<String , Object> map = JsonUtil.Json2Map(response);
		
		switch(curRequest){
		case 0:
			if(map!=null){
				Map<String , Object> res_json = JsonUtil.Json2Map(response);
				
				LinkedHashMap<String, Object> base_info = (LinkedHashMap<String, Object>) res_json.get("base_info");
				LinkedHashMap<String, Object> user_info = (LinkedHashMap<String, Object>) res_json.get("user_list");
				
				
				line = new Line();
				
				line.setId(Integer.parseInt((String)base_info.get("line_id")));
				line.setStartAddr((String)base_info.get("start_addr"));
				line.setEndAddr((String)base_info.get("end_addr"));
		//		line.setDateTime(DateUtil.changeLong(Long.parseLong((String)temp.get("date"))));
				line.setDateTime((String)base_info.get("date"));
				line.setNum((Integer)base_info.get("num"));
				//line.setType(Integer.parseInt((String)base_info.get("type")));
				line.setReason((String)base_info.get("reason"));
				line.setUrl((String)base_info.get("head_url"));
				
				List<LinkedHashMap<String, Object>> driver_info = (List<LinkedHashMap<String, Object>>) user_info.get("driver");
		//		List<LinkedHashMap<String, Object>> passenger = 
				List<LinkedHashMap<String, Object>> pass_info = (List<LinkedHashMap<String, Object>>) user_info.get("passenger");
				System.out.println(driver_info==null);
				if(driver_info!=null){
					
					setView(driver_info.get(0));
				}
				
				List<User> list = new ArrayList<User>();
				
				if(pass_info!=null){
					for(int i=0; i<pass_info.size() ; i++){
						User passenger = new User();
						LinkedHashMap<String, Object> pass = (LinkedHashMap<String, Object>) pass_info.get(i);
						
						passenger.setPhone((String)pass.get("phone"));
						passenger.setName((String)pass.get("name"));
						passenger.setNum((Integer)pass.get("num"));
						passenger.setHead_url((String)pass.get("head_url"));
						
						list.add(passenger);
					}
					
					line.setPassenger_List(list);
				}
				
				
				MyApplication.getInstance().setLineId(line.getId());
				MyApplication.getInstance().setLineStatus(0);
			}
			break;
		case 1:
			if(map!=null){
				
				String status = (String) map.get("status");
				String message = (String) map.get("message");
				if(status.equals("200")){
					//MyApplication.getInstance().setLineId(-1);
					startActivity(NearLineActivity.class);
				}else{
					showShortToast(message);
				}
				
			}else{
				
			}
			
			break;
		}
		
	}
	
	public void setView(LinkedHashMap<String, Object> driver_info){
		
		//progressDialog.dismiss();
		
		if(driver_info!=null){
			driver.setPhone((String)driver_info.get("phone"));
			driver.setName((String)driver_info.get("name"));
			driver.setCar_license((String)driver_info.get("car_lincense"));
			driver.setCar_type((String)driver_info.get("car_name"));
			driver.setHead_url((String)driver_info.get("head_url"));
			line.setDriver(driver);
		}
		layout_loading.setVisibility(View.GONE);
		layout_driver.setVisibility(View.VISIBLE);
		
		btn_start.setVisibility(View.VISIBLE);
		
		tv_start_addr.setText(line.getStartAddr());
		tv_date.setText(line.getDateTime());
		tv_end_addr.setText(line.getEndAddr());
		tv_driver.setText(driver.getName());
		tv_car_name.setText(driver.getCar_type());
		tv_lincense.setText(driver.getCar_license());
		
		
		ImageLoader.getInstance().displayImage(driver.getHead_url(), circleImageView ,options);
	}
	
	@Override
	public void setListner() {
		btn_cancel.setOnClickListener(this);
		btn_start.setOnClickListener(this);
		
		
	}
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		
		switch (view.getId()) {
		case R.id.btn_cancel:
//			Map<String , String> map = new LinkedHashMap<String, String>();
//			map.put("line_id", getIntent().getIntExtra("line_id", 0)+"");
//			map.put("phone", MyApplication.getInstance().getUserName());
//			map.put("type", 1+"");
//			map.put("method", "remove_user");
//			string_request(Constant.LINE_URL, Request.Method.POST, map);
//			curRequest = 1;
			
			
			break;
		case R.id.btn_start:
			Bundle b = new Bundle();
			System.out.println(line==null);
			b.putSerializable("line_info", line);
			startActivity(NavigationActivity.class , b);
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		//finish();
	}

}
