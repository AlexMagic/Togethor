package com.android.together.fragment;

import java.util.LinkedHashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.together.BaseFragment;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.constant.Constant;
import com.android.together.util.InvalidateUtil;
import com.android.together.util.JsonUtil;
import com.android.togethor.activity.NearLineActivity;
import com.android.togethor.activity.RegisterActivity;
import com.android.volley.Request;

public class CarInfoFragment extends BaseFragment {

	private EditText et_carname;
	private EditText et_lincense;
	private ImageView iv_car_name;
	private ImageView iv_lincense;
	private RelativeLayout layout;
	
	
	public CarInfoFragment(Context context , int layout_id ){
		super(context , layout_id);
	}
	
	
	@Override
	protected void setUpData() {
		
	}
	
	@Override
	public void respone(String response) {
		
		Map<String , Object> map = JsonUtil.Json2Map(response);
		if(map!=null){
			String status = (String) map.get("status");
			//String code = (String) map.get("code");
			String msg = (String) map.get("message");
			if(status.equals("200")){
				MyApplication.getInstance().setType(1);
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context , NearLineActivity.class);
				startActivity(intent);
				((Activity) context).overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
				((Activity) context).finish();
				
			}else{
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void setUpListner() {
		layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//提交车辆信息
				if(isValidate())
					string_request(Constant.USER_CAR_URL, Request.Method.POST, setInfo());
			}
		});
		
		et_carname.addTextChangedListener(new MyTextWatcher(et_carname));
		et_lincense.addTextChangedListener(new MyTextWatcher(et_lincense));
	}
	
	
	
	
	public Map<String , String> setInfo(){
		Map<String , Object> car_info = new LinkedHashMap<String, Object>();
		Map<String , String> temp = new LinkedHashMap<String, String>();
		
		String car_name = et_carname.getText().toString();
		String lincense = et_lincense.getText().toString();
		
		car_info.put("phone", MyApplication.getInstance().getTempPhone());
		car_info.put("car_name", car_name);
		car_info.put("car_lincense", lincense);
		
		String json = JsonUtil.Map2Json(car_info);
		System.out.println(json);
		temp.put("info", json);
		temp.put("method", "insert_car");
		
		return temp;
	}

	@Override
	protected void setUpView() {
		et_carname = (EditText) findViewById(R.id.et_car_name);
		et_lincense = (EditText) findViewById(R.id.et_car_lincense);
		iv_car_name = (ImageView) findViewById(R.id.im_car_name);
		iv_lincense = (ImageView) findViewById(R.id.im_lincense);
		layout =  (RelativeLayout) findViewById(R.id.layout_submit);
	}
	
	class MyTextWatcher implements TextWatcher{

		private EditText et;
		
		public MyTextWatcher(EditText et){
			this.et = et;
		}
		
		
		@Override
		public void afterTextChanged(Editable s) {
			
			switch(et.getId()){
			case R.id.et_car_name:
				iv_car_name.setVisibility(View.GONE);
				break;
			case R.id.et_car_lincense:
				iv_lincense.setVisibility(View.GONE);
				break;
			
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public boolean isValidate() {
		
		boolean isValidate = true;
		
		if(InvalidateUtil.isNull(et_carname)){
			iv_car_name.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		if(InvalidateUtil.isNull(et_lincense)){
			iv_lincense.setVisibility(View.VISIBLE);
			isValidate = false;
		}
		
		
		
		return isValidate;
	}
	

}
