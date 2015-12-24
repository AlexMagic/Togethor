package com.android.together;



import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.entity.Line;
import com.android.entity.User;
import com.android.together.annotation.ViewInjectUtil;
import com.android.together.constant.Constant;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.sina.push.PushManager;

//import com.android.zns.db.DBHelper;
//import com.android.zns_pad.view.HeaderLayout.OnIconClickListner;

public abstract class BaseActivity extends FragmentActivity	implements OnClickListener{
	
	public MyApplication mApplication;
	
	/**
	 * ��Ļ�Ŀ�ȡ��߶ȡ��ܶ�
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;
	
	private OnClickListener onClickListener;
	
	private InputMethodManager manager;
	
	private SlidingMenu menu;
	protected RequestQueue mRequestQueue;
	
	private MyMenuItemClickListner menuItemClickListner;
	
	private PushManager pushManager;
	
	public DisplayImageOptions options;
	
	private boolean isServiceRunning = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mApplication = (MyApplication) getApplication();
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
		ViewInjectUtil.inject(this);
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		mRequestQueue =  Volley.newRequestQueue(this);  
		//setUpView();
		setListner();
		
		init();
		
//		NetBroadcastReceiver.mListeners.add(new MyNetEventHandler());
	}
	
	/**
	 * ��ʼ���ؼ�
	 */
	//ublic abstract void setUpView();
	
	/**
	 * ��Ӽ���
	 */
	public abstract void setListner();
	
	public void setOnClickListner(View view){};
	
	public void init(){
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ub__safetynet_circle_small)
		.showImageForEmptyUri(R.drawable.ub__safetynet_circle_small)
		.showImageOnFail(R.drawable.ub__safetynet_circle_small)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.displayer(new SimpleBitmapDisplayer())
		.build();
		
//		isServiceRunning = isPushRunning();
//		Log.d("sinapush", "push service is running::" + isServiceRunning);
//		
//		pushManager = PushManager.getInstance(getApplicationContext());
//		if (getIntent() != null) {
//			pushManager.sendClickFeedBack (getIntent());
//		}
//		
//		if(isServiceRunning){
//			startSinaPushService();
//		}
	};
	
	/** ������ʾToast��ʾ(����res) **/
	protected void showShortToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
	}

	/** ������ʾToast��ʾ(����String) **/
	protected void showShortToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	/** ��ʱ����ʾToast��ʾ(����res) **/
	protected void showLongToast(int resId) {
		Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
	}

	/** ��ʱ����ʾToast��ʾ(����String) **/
	protected void showLongToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	/** ͨ��Class��ת���� **/
	protected void startActivity(Class<?> cls) {
		startActivity(cls, null);
	}

	/** ����Bundleͨ��Class��ת���� **/
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
		intent.setClass(this, cls);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	/** ͨ��Action��ת���� **/
	protected void startActivity(String action) {
		startActivity(action, null);
	}

	/** ����Bundleͨ��Action��ת���� **/
	protected void startActivity(String action, Bundle bundle) {
		Intent intent = new Intent();
		intent.setAction(action);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		startActivity(intent);
	}

	
	protected void initMenu(){
		
		menu = new SlidingMenu(this);//ֱ��new��������getSlidingMenu

		menu.setMode(SlidingMenu.LEFT);
		
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		//menu.setShadowDrawable(R.drawable.shadow);

		//menu.setShadowWidthRes(R.dimen.shadow_width);

		//menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

		menu.setBehindWidth(mScreenWidth/10*7);//����SlidingMenu�˵��Ŀ��

		menu.setFadeDegree(0.5f);

		menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);//�������

		menu.setMenu(R.layout.menu_layout);//����ͨ�׵�layout����
		
		//menu.setSecondaryMenu(R.layout.menu_layout);//����ͨ�׵�layout����
		
		//menu.setBehindCanvasTransformer(mTransformer);
		
		menuItemClickListner = new MyMenuItemClickListner();
		
		LinearLayout layout_item = (LinearLayout) menu.findViewById(R.id.layout_item);
		
		for(int i=0 ; i<layout_item.getChildCount() ; i++){
			LinearLayout item = (LinearLayout) layout_item.getChildAt(i);
			item.setOnClickListener(menuItemClickListner);
		}
	}
	
	protected SlidingMenu getSlidingMenu(){
		return menu;
	}
	
	class MyMenuItemClickListner implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch(view.getId()){
			case R.id.layout_info:
				break;
			case R.id.layout_wallet:
				break;
			case R.id.layout_share:
				break;
			case R.id.layout_price:
				break;
			case R.id.layout_his:
				break;
			case R.id.layout_help:
				break;
			}
		}
	}

	@Override
	public void onClick(View view) {
		setOnClickListner(view);
	}
	
	
	public void onBackPressed() {
		
	}
	
	
    
//    class MyNetEventHandler implements NetEventHandler{
//
//		@Override
//		public void onNetChange() {
//			if(CommonUtils.isNetWorkConnected(BaseActivity.this)){
//				Toast.makeText(BaseActivity.this, "����������", Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(BaseActivity.this, "�����ѶϿ�", Toast.LENGTH_SHORT).show();
//			}
//		}
//    	
//    }
	
	public void setBgAlpha(BasePopupWindow popuWindow){
		if(popuWindow!=null){
			ColorDrawable cd = new ColorDrawable(0x000000);
			popuWindow.setBackgroundDrawable(cd); 
			WindowManager.LayoutParams lp=getWindow().getAttributes();
			lp.alpha = 0.4f;
			getWindow().setAttributes(lp);
		}else{
			WindowManager.LayoutParams lp=getWindow().getAttributes();
		    lp.alpha = 1f;
		    getWindow().setAttributes(lp);
		}
	}
	
	public  void respone(String response){};
	public  void error(String error){
		showShortToast(error);
	};
	
	public void string_request(String url , int method , final Map<String, String> map){
		StringRequest stringRequest = new StringRequest(method, url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println("h");
				String r;
				try {
					r = new String(response.getBytes("8859_1"),"utf-8");
					Log.d("request", "response -> " + r);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 
				respone(response);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				Log.d("request_error", "error -> " + error.getMessage());
				error(error.getMessage());
			}
		}){

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				if(map!=null)
					return map;
				return super.getParams();
			}
			
		};
		
		mRequestQueue.add(stringRequest);
		mRequestQueue.start();
	}
	
	
	/**
	 * push �Ƿ����ں�̨����
	 */
	private boolean isPushRunning() {
		// TODO Auto-generated method stub
		ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceList = mActivityManager
				.getRunningServices(Integer.MAX_VALUE);

		for (RunningServiceInfo info : serviceList) {

			if (Constant.NAME_PUSH_SERVICE.equals(info.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
//		refreshConnection();
		//
		/*
		 * �����ͳ�� if(pushSystemMethod != null){
		 * pushSystemMethod.sendClickFeedBack(getIntent()); }
		 */
	}
	
	/**
	 * ����SinaPush����
	 */
	private void startSinaPushService() {

		pushManager.initPushChannel(Constant.APPID, Constant.APPID, "100", "100");

	}

	/**
	 * �ر�SinaPush����
	 */
	private void stopSinaPushService() {
		pushManager.close();
	}

	/**
	 * ˢ��Push��������
	 */
	private void refreshConnection() {
		pushManager.refreshConnection();

	}
	
	public Line getLineInfo(Map<String , Object> res_json){
		Line line = null;
		
		LinkedHashMap<String, Object> base_info = (LinkedHashMap<String, Object>) res_json.get("base_info");
		LinkedHashMap<String, Object> user_info = (LinkedHashMap<String, Object>) res_json.get("user_list");
		
		
		line = new Line();
		
		line.setId(Integer.parseInt((String)base_info.get("line_id")));
		line.setStartAddr((String)base_info.get("start_addr"));
		line.setEndAddr((String)base_info.get("end_addr"));
//			line.setDateTime(DateUtil.changeLong(Long.parseLong((String)temp.get("date"))));
		line.setDateTime((String)base_info.get("date"));
		line.setNum(Integer.parseInt((String)base_info.get("num")));
		//line.setType(Integer.parseInt((String)base_info.get("type")));
		line.setReason((String)base_info.get("reason"));
		line.setUrl((String)base_info.get("head_url"));
		
		List<LinkedHashMap<String, Object>> driver_info = (List<LinkedHashMap<String, Object>>) user_info.get("driver");
		//@SuppressWarnings("unchecked")
		List<LinkedHashMap<String, Object>> pass_info = (List<LinkedHashMap<String, Object>>) user_info.get("passenger");
		
		if(driver_info!=null){
			
			User driver = new User();
			
			if(driver_info.get(0)!=null){
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
		}
		
		return line;
	}
	
	
	/**
	 * ���������
	 */
	public void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	
	
}
