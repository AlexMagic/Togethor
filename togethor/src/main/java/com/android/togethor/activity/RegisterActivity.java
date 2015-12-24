package com.android.togethor.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.android.together.BaseActivity;
import com.android.together.BaseFragment;
import com.android.together.MyApplication;
import com.android.together.BasePopupWindow.onSubmitClickListener;
import com.android.together.R;
import com.android.together.activity.reg.InfoRegFragment;
import com.android.together.activity.reg.PhoneRegFragment;
import com.android.together.adapter.MyFragmentPagerAdapter;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.fragment.CarInfoFragment;
import com.android.together.popupwindow.RegTipsPopupwindow;
import com.android.together.util.JsonUtil;
import com.android.together.view.HeaderLayout;
import com.android.together.view.ProgressDialog;
import com.android.together.view.HeaderLayout.HeaderStyle;
import com.android.together.view.HeaderLayout.OnIconClickListner;
import com.android.together.view.HeaderLayout.OnRightLayoutListner;
import com.android.together.view.MyViewPager;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


@ContentView(value = R.layout.activity_reg)
public class RegisterActivity extends BaseActivity {

	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.viewpager)
	private MyViewPager myViewPager;
	
	private int send_state=-1;
	
	private RegTipsPopupwindow tipsPopupwindow;
	
	private MyFragmentPagerAdapter myFragmentPagerAdapter;

	private List<BaseFragment> list_fragment;
	
	
	//手机验证的fragment
	private PhoneRegFragment phoneRegFragment;
	
	//用户信息填写的fragment
	private InfoRegFragment infoRegFragment;
	
	//车辆信息填写
	private CarInfoFragment carInfoFragment;
	
	private ProgressDialog dialog;
	
	@SuppressLint("ResourceAsColor")
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case 2:
				Toast.makeText(RegisterActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
				
				
				MyApplication.getInstance().setTempPhone(phone);
				
				myViewPager.setCurrentItem(1, true);
				
				headerLayout.setIcon(-1);
				headerLayout.setHandyOnlyText("基本信息");
				

				break;
			case 3:
				Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
				
				MyApplication.getInstance().setUserName(phoneRegFragment.getPhone());
				
				MyApplication.getInstance().setPwd(infoRegFragment.getPwd());
				
				tipsPopupwindow = new RegTipsPopupwindow(RegisterActivity.this);
				
				
            	
				tipsPopupwindow.setContext("您已成功注册，默认为乘客身份\n您可以填写相关信息兼为车主身份");
            	
				tipsPopupwindow.setOnSubmitClickListener(new onSubmitClickListener() {
					
					@Override
					public void onClick() {
						myViewPager.setCurrentItem(2, true);
						
						headerLayout.setIcon(R.drawable.bg_btn_header_back);
						headerLayout.setHandyOnlyText("车辆信息");
						headerLayout.setIconClickListner(new OnIconClickListner() {
							
							@Override
							public void onClick() {
								setTipsPopupwindow();
							}
						});
						tipsPopupwindow.dismiss();
					}
				});
				
				tipsPopupwindow.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						RegisterActivity.this.setBgAlpha(null);
						if(myViewPager.getCurrentItem()==1){
							Intent intent = new Intent(RegisterActivity.this , NearLineActivity.class);
							startActivity(intent);
							finish();
						}
					}
				});
				tipsPopupwindow.showViewCenter(tipsPopupwindow.getView());
				RegisterActivity.this.setBgAlpha(tipsPopupwindow);
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
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		headerLayout.setIcon(R.drawable.bg_btn_header_back);
		headerLayout.setHandyOnlyText("注册");
		
		
		headerLayout.init(HeaderStyle.TITLE_FOR_HELP);
		headerLayout.setTextForItem(2, "下一步");
		
		headerLayout.setLayoutLinster(null, null, new MyRightLayoutListner());
		
		initFragment();
	}
	
	
	private void initFragment() {
		phoneRegFragment = new PhoneRegFragment(RegisterActivity.this, R.layout.reg_phone);
		infoRegFragment = new InfoRegFragment(RegisterActivity.this, R.layout.reg_info , mHandler);
		carInfoFragment = new CarInfoFragment(RegisterActivity.this, R.layout.frag_car_info);
		
		list_fragment = new ArrayList<BaseFragment>();
		list_fragment.add(phoneRegFragment);
		list_fragment.add(infoRegFragment);
		list_fragment.add(carInfoFragment);
		
		myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list_fragment, null);
		
		myViewPager.setAdapter(myFragmentPagerAdapter);
		myViewPager.invalidate();
	}

	@Override
	public void setListner() {
		tipsPopupwindow = new RegTipsPopupwindow(RegisterActivity.this);
		tipsPopupwindow.setOnSubmitClickListener(new onSubmitClickListener() {
			
			@Override
			public void onClick() {
				
			}
		});
		
		tipsPopupwindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				Intent intent = new Intent(RegisterActivity.this , NearLineActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
	
	
	@SuppressLint("ResourceAsColor")
	@SuppressWarnings("null")
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		switch(view.getId()){
		case R.id.btn_get_val:
			
		case R.id.btn_reg:
			
		}
		
	}
	String phone;
	
	class MyRightLayoutListner implements OnRightLayoutListner{

		@Override
		public void onClick() {
			
			switch(myViewPager.getCurrentItem()){
			case 0:
				phone = phoneRegFragment.getPhone();
				System.out.println(phone);
				
				
				if(phone !=null && !phone.equals("")){
					if(phoneRegFragment.getSend_state()==Constant.SEND_STATE){
					//if(true){
						if(phoneRegFragment.getVal()!=null && phoneRegFragment.getVal().equals(phoneRegFragment.getCurVal())){
						//	if(true){
							new Thread(new Runnable() {
								
								@Override
								public void run() {
									
									
									Message msg = new Message();
									msg.what = 2;
									mHandler.sendMessage(msg);
									
								}
							}).start();
						}else{
							Toast.makeText(RegisterActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(RegisterActivity.this, "请获取验证码", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(RegisterActivity.this, "请输入您的手机号码", Toast.LENGTH_SHORT).show();
				}
				break;
			case 1:
				if(infoRegFragment.isValidate()){
					
					Map<String, Object> info = infoRegFragment.setInfo();
					info.put("phone", phone);
					
					reg_info(Constant.REG_URL,info);
					
				}else{
					Toast.makeText(RegisterActivity.this, "请检查您的信息", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		backTips();
	}

	private void reg_info(String url , Map<String , Object> infos){
		
		final String json_info = JsonUtil.Map2Json(infos);
		
		//infoRegFragment.startUpload();
		
		dialog = new ProgressDialog(RegisterActivity.this, "正在注册...");
		dialog.show();
		StringRequest stringRequest = new StringRequest(Request.Method.POST,url,
			    new Response.Listener<String>() {
			        @Override
			        public void onResponse(String response) {
			            Log.d("phone_val", "response -> " + response);
			            if(response.equals("-1")){
			            	Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
			            }else{
//			            	Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
			            	//上传头像
			            	dialog.dismiss();
			            	infoRegFragment.startUpload();
			            	
			            	
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
		            map.put("method", "reg_base_info");  
		            map.put("info", json_info);  
		            return map;
			    }
			};  
			
			mRequestQueue.add(stringRequest); 
			 
			mRequestQueue.start();
	}
	
	public void setTipsPopupwindow(){
		tipsPopupwindow = new RegTipsPopupwindow(RegisterActivity.this);
		tipsPopupwindow.setContext("是否结束信息填写？");
		tipsPopupwindow.btnContent(0, "继续填写");
		tipsPopupwindow.btnContent(1, "是的");
		
		tipsPopupwindow.setOnSubmitClickListener(new onSubmitClickListener() {
			
			@Override
			public void onClick() {
				Intent intent = new Intent(RegisterActivity.this , NearLineActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		tipsPopupwindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				RegisterActivity.this.setBgAlpha(null);
			}
		});
		
		RegisterActivity.this.setBgAlpha(tipsPopupwindow);
		tipsPopupwindow.showViewCenter(tipsPopupwindow.getView());
	}
	
	private void backTips() {
		
	}
	
}
