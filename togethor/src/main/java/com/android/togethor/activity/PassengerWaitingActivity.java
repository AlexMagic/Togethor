package com.android.togethor.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.android.volley.Request;
import com.nostra13.universalimageloader.core.ImageLoader;


@ContentView(value = R.layout.pre_waiting_driver)
public class PassengerWaitingActivity extends BaseActivity {

	private Line line;
	private User driver;
	private List<User> passengers = new ArrayList<User>();
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.btn_cancel)
	private Button btn_cancel;
	
	@ViewInject(R.id.btn_start)
	private Button btn_start;
	
	@ViewInject(R.id.tv_start_addr)
	private TextView tv_start_addr;
	
	@ViewInject(R.id.tv_end_addr)
	private TextView tv_end_addr;
	
	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	
	@ViewInject(R.id.layout_passenger)
	private LinearLayout layout_passenger;
	
	@ViewInject(R.id.tv_pass_num)
	private TextView tv_pass_num;
	
	@ViewInject(R.id.scrollview)
	private ScrollView scrollView;
	
	private int line_id;
	
	private int curRequest = -1;
	
//	private LoadingPopupwindow loadingPopupwindow;
	
	
	@Override
	public void setListner() {
		btn_start.setOnClickListener(this);
	}
	
	@Override
	public void init() {
		
		initMenu();
		
		headerLayout.setIcon(R.drawable.bg_sliding_btn);
		headerLayout.setHandyOnlyText("准备行程");
//		setLoadingView();
		Map<String , String> map = new LinkedHashMap<String, String>();
		map.put("line_id", getIntent().getIntExtra("line_id", 0)+"");
		map.put("phone", MyApplication.getInstance().getUserName());
		map.put("method", "sel_line_by_id");

		MyApplication.getInstance().setLineStatus(1);
		
		string_request(Constant.LINE_URL, Request.Method.POST, map);
		curRequest = 0;
	}
	
	@Override
	public void respone(String response) {
		//respone json;
//		loadingPopupwindow.dismiss();
		
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
	//			line.setDateTime(DateUtil.changeLong(Long.parseLong((String)temp.get("date"))));
				line.setDateTime((String)base_info.get("date"));
				line.setNum((Integer)base_info.get("num"));
				//line.setType(Integer.parseInt((String)base_info.get("type")));
				line.setReason((String)base_info.get("reason"));
				line.setUrl((String)base_info.get("head_url"));
				
				List<LinkedHashMap<String, Object>> driver_info = (List<LinkedHashMap<String, Object>>) user_info.get("driver");
				//@SuppressWarnings("unchecked")
				List<LinkedHashMap<String, Object>> pass_info = (List<LinkedHashMap<String, Object>>) user_info.get("passenger");
				
				if(driver_info!=null){
					
					
					if(driver_info.get(0)!=null){
						
						driver = new User();
						
						driver.setPhone((String)driver_info.get(0).get("phone"));
						driver.setName((String)driver_info.get(0).get("name"));
						driver.setCar_license((String)driver_info.get(0).get("car_lincense"));
						driver.setCar_type((String)driver_info.get(0).get("car_name"));
						driver.setHead_url((String)driver_info.get(0).get("head_url"));
						line.setDriver(driver);
					}
				}
				
				List<User> list = new ArrayList<User>();
				
				if(pass_info!=null){
					for(int i=0; i<pass_info.size() ; i++){
						User passenger = new User();
						LinkedHashMap<String, Object> pass = pass_info.get(i);
						
						passenger.setPhone((String)pass.get("phone"));
						passenger.setName((String)pass.get("name"));
						passenger.setNum((Integer)pass.get("num"));
						passenger.setHead_url((String)pass.get("head_url"));
						
						list.add(passenger);
					}
					
					line.setPassenger_List(list);
					
					setPassLayout(list);
				}
				
				MyApplication.getInstance().setLineId(line.getId());
				MyApplication.getInstance().setLineStatus(1);
			}else{
				
			}
			break;
		case 1:
			
			if(map!=null){
				
				String status = (String) map.get("status");
				String message = (String) map.get("message");
				if(status.equals("200")){
					MyApplication.getInstance().setLineId(-1);
					startActivity(NearLineActivity.class);
				}else{
					showShortToast(message);
				}
				
			}else{
				
			}
			
			break;
		}
		
	}
	
	public void setPassLayout(List<User> list){
		layout_passenger.removeAllViews();
		if(list!=null){
			for(int i=0 ; i<list.size() ; i++){
				View pass_view = LayoutInflater.from(PassengerWaitingActivity.this).inflate(R.layout.layout_passenger, null,true);
				ImageView iv_head = (ImageView) pass_view.findViewById(R.id.iv_head);
				TextView tv_name = (TextView) pass_view.findViewById(R.id.tv_name);
				TextView tv_num = (TextView) pass_view.findViewById(R.id.tv_num);
				
				ImageLoader.getInstance().displayImage(list.get(i).getHead_url(), iv_head ,options);
				tv_name.setText(list.get(i).getName());
				tv_num.setText("上车人数:"+list.get(i).getNum()+"人");
				
				layout_passenger.addView(pass_view);
			}
			
			tv_pass_num.setText("等待乘客响应...(已响应"+list.size()+"人)");
		}
	}
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		switch(view.getId()){
		case R.id.btn_start:
			Bundle b = new Bundle();
			b.putSerializable("line_info", line);
			startActivity(NavigationActivity.class , b);
			break;
		case R.id.btn_cancel:
			Map<String , String> map = new LinkedHashMap<String, String>();
			map.put("line_id", getIntent().getIntExtra("line_id", 0)+"");
			map.put("phone", MyApplication.getInstance().getUserName());
			map.put("type", 0+"");
			map.put("method", "remove_user");
			string_request(Constant.LINE_URL, Request.Method.POST, map);
			curRequest = 1;
			
			break;
		}
	}
	
//	private void setLoadingView(){
//		loadingPopupwindow = new LoadingPopupwindow(PassengerWaitingActivity.this);
//		loadingPopupwindow.setOnDismissListener(new OnDismissListener() {
//			
//			@Override
//			public void onDismiss() {
//				PassengerWaitingActivity.this.setBgAlpha(null);
//			}
//		});
//		PassengerWaitingActivity.this.setBgAlpha(loadingPopupwindow);
//		loadingPopupwindow.showViewCenter(loadingPopupwindow.getView());
//	}
	
	

}
