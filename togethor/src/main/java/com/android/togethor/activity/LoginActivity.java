package com.android.togethor.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.popupwindow.LoadingPopupwindow;
import com.android.volley.Request;


@ContentView(value = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

	private final static int LOGIN_SUC = 0;
	private final static int LOGIN_ERR = 1;
	
	@ViewInject(R.id.btn_login)
	private Button btn_login;
	
	@ViewInject(R.id.btn_reg)
	private Button btn_reg;
	
	@ViewInject(R.id.et_phone)
	private EditText et_phone;
	
	@ViewInject(R.id.et_password)
	private EditText et_password;
	
	private LoadingPopupwindow loadingPopupwindow;
	
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case LOGIN_SUC:
				loadingPopupwindow.dismiss();
				break;
			case LOGIN_ERR:
				loadingPopupwindow.dismiss();
				Toast.makeText(LoginActivity.this, "用户名或者密码有误", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
	}

	@Override
	public void setListner() {
		
		btn_login.setOnClickListener(this);
		btn_reg.setOnClickListener(this);
		
	}

	

	@Override
	public void setOnClickListner(View view) {
		hideKeyboard();
		loadingPopupwindow = new LoadingPopupwindow(LoginActivity.this);
		loadingPopupwindow.setLoadingContent("正在登陆...");
		switch(view.getId()){
		case R.id.btn_login:
			
			loadingPopupwindow.showViewCenter(loadingPopupwindow.getView());
			login();
			break;
		case R.id.btn_reg:
			Intent intent_reg = new Intent(LoginActivity.this , RegisterActivity.class);
			startActivity(intent_reg);
			overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
			break;
		}
	}
	
	public void login(){
		
		Map<String, String> map = new HashMap<String, String>();  
        map.put("phone", et_phone.getText().toString());  
        map.put("pwd", et_password.getText().toString());
		
		string_request(Constant.LOGIN_URL, Request.Method.POST, map);
		
	}

	@Override
	public void respone(String response) {
		if(response.equals("1")){
			loadingPopupwindow.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss() {
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							MyApplication.getInstance().setUserName(et_phone.getText().toString());
							MyApplication.getInstance().setPwd(et_password.getText().toString());
							
							
							Intent intent_login = new Intent(LoginActivity.this , NearLineActivity.class);
							startActivity(intent_login);
							overridePendingTransition(R.anim.roll_left	, R.anim.roll_not_move);
							finish();
							
						}
					}).start();
				}
			});
			Message msg_1 = new Message();
			msg_1.what = LOGIN_SUC;
			handler.sendMessage(msg_1);
		}else if(response.equals("0")){
			Message msg_2 = new Message();
			msg_2.what = LOGIN_ERR;
			handler.sendMessage(msg_2);
		}
	}
	
	
	
}
