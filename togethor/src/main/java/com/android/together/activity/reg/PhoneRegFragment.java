package com.android.together.activity.reg;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.together.BaseFragment;
import com.android.together.R;
import com.android.together.constant.Constant;
import com.android.together.util.InvalidateUtil;
import com.android.together.util.MathUtil;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

@SuppressLint("ValidFragment")
public class PhoneRegFragment extends BaseFragment implements OnClickListener{

	
//	private Context context;
	
	private EditText et_val;
	
	private EditText et_phone;
	
	private Button btn_get_val;
	
	private int send_state=-1;
	
	private String curVal;
	
	private String phone;
	
	
	@SuppressLint("ResourceAsColor")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 0:
				send_state=Constant.SEND_STATE;
				btn_get_val.setText(msg.arg1+"秒后重新获取");
				break;
			case 1:
				send_state=Constant.RESEND_STATE; //超时重新获取	
				System.out.println("超时重新获取");
				btn_get_val.setBackgroundResource(R.color.tab_bg);
				btn_get_val.setEnabled(true);
				btn_get_val.setText("获取验证码");
				curVal = "";
				break;
			}
		}
	};
	
	public PhoneRegFragment(Context context , int layout_id){
		super(context , layout_id);
	}
	
	@Override
	protected void setUpData() {

	}

	@Override
	protected void setUpListner() {
		btn_get_val.setOnClickListener(this);
	}

	@Override
	protected void setUpView() {
		et_val = (EditText) findViewById(R.id.et_val);
		btn_get_val = (Button) findViewById(R.id.btn_get_val);
		et_phone = (EditText) findViewById(R.id.et_phone);
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onClick(View view) {
		phone = et_phone.getText().toString();
		switch(view.getId()){
		case R.id.btn_get_val:
			if(phone !=null && !phone.equals("")){
				
				if(!InvalidateUtil.matchPhone(phone)){
					Toast.makeText(context, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
					return ;
				}
				
				if(send_state==1||send_state==-1){
					
					valPhone(Constant.REG_URL);
					
				}
			}else{
				Toast.makeText(context, "请输入您的手机号码", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private void valPhone(String url){
		StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
			    new Response.Listener<String>() {
			        @SuppressLint("ResourceAsColor")
					@Override
			        public void onResponse(String response) {
			            Log.d("phone_val", "response -> " + response);
			            if(response.equals("0")){
			            	
			            	//发送
							btn_get_val.setBackgroundColor(R.color.bg_headerbar_spinner_selected);
							btn_get_val.setEnabled(false);
							setSend_state(Constant.SEND_STATE);
							
							curVal = MathUtil.randomNum();
			            	
							sendMsg(Constant.VAL_URL);
			            	
			            	new Thread(new Runnable() {
								
								@Override
								public void run() {
									for(int i=Constant.RESEND_SCONDE ; i>=0 ; i--){
										
										
										if(i==0){
											Message msg = new Message();
											msg.what = Constant.RESEND_STATE;
											msg.arg1 = i;
											mHandler.sendMessage(msg);
											
										}else{
											Message msg = new Message();
											msg.what = Constant.SEND_STATE;
											msg.arg1 = i;
											mHandler.sendMessage(msg);
										}
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								}
							}).start();
			            }else{
			            	Toast.makeText(context, "手机号已被注册", Toast.LENGTH_SHORT).show();
			            }
			        }
			    }, new Response.ErrorListener() {
			        @Override
			        public void onErrorResponse(VolleyError error) {
			        }
			    }) {
			    @Override
			    protected Map<String, String> getParams() {
			        //在这里设置需要post的参数
		            Map<String, String> map = new HashMap<String, String>();  
		            map.put("method", "val_phone");  
		            map.put("phone", phone);  
		            return map;
			    }
			};  
			
			mRequestQueue.add(stringRequest); 
			 
			mRequestQueue.start();
	}
	
	private void sendMsg(String url){
		
		url = url+"phone="+phone+"&val="+curVal;
		
		StringRequest request = new StringRequest(url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				
			}
		}, null);
		
		 mRequestQueue.add(request); 
		 
		 mRequestQueue.start();
	}
	
	public String getPhone(){
		return et_phone.getText().toString();
	}
	
	public String getVal(){
		return et_val.getText().toString();
	}

	public int getSend_state() {
		return send_state;
	}

	public void setSend_state(int send_state) {
		this.send_state = send_state;
	}

	public String getCurVal() {
		return curVal;
	}

	
	

}
