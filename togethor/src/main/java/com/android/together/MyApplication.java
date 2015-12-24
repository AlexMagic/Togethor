package com.android.together;

import java.util.Calendar;

import com.android.together.annotation.ViewInjectUtil;
import com.baidu.mapapi.SDKInitializer;
//import com.android.zns.constant.Constant;
//import com.android.zns.db.DBHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyApplication extends Application{
	
	private static  Context applicationContext;
	private static MyApplication instance;
//	public AsyncForData asyncForData = new AsyncForData();
	
	public static final int NOWADAY = 20;
	public static final int NOWMONTH = 5;
	
	//temp phone
	public static final String PREF_TEMP = "temp_phone";
	private String temp_phone = null;
	
	//login user name 
	public static final String PREF_USERNAME = "username";
	private String userName = null;
	//login password
	private static final String PREF_PWD = "pwd";
	private String pwd = null;
	
	//current district
	public static final String PREF_DISTRICT = "district";
	private String cur_district = null;
	
	//cur type
	public static final String PREF_TYPE = "type";
	private int cur_type = 0;
	
	
	//cur type
	public static final String PREF_LINE_TYPE = "line_type";
	private int line_type = 0;
	
	
	//respone line_id
	public static final String PREF_LINE_ID = "line_id";
	private int line_id = -1;
	
	public static MyApplication getInstance(){
		if(instance == null)
			instance = new MyApplication();
		
		return instance;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		applicationContext = this;
		instance = this;
		
		SDKInitializer.initialize(this);
		
		initImageLoader(applicationContext);
		
		
	}
	


	/**
	 * 初始化ImageLoader
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you may tune some of them,
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getTempPhone() {
		if (temp_phone == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			temp_phone = preferences.getString(PREF_USERNAME, null);
		}
		return temp_phone;
	}
	
	
	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setTempPhone(String temp_phone) {
		if (temp_phone != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, temp_phone).commit()) {
				this.temp_phone = temp_phone;
			}
		}
	}
	
	
	/**
	 * 获取当前登陆用户名
	 * 
	 * @return
	 */
	public String getUserName() {
		if (userName == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			userName = preferences.getString(PREF_USERNAME, null);
		}
		return userName;
	}
	
	
	/**
	 * 设置用户名
	 * 
	 * @param user
	 */
	public void setUserName(String username) {
		if (username != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_USERNAME, username).commit()) {
				userName = username;
			}
		}
	}
	
	
	/**
	 * 设置密码
	 * 
	 * @param user
	 */
	public void setPwd(String pwd) {
		if (pwd != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_PWD, pwd).commit()) {
				this.pwd = pwd;
			}
		}
	}
	
	/**
	 * 获取当前用户名密码
	 * 
	 * @return
	 */
	public String getPwd() {
		if (this.pwd == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			this.pwd = preferences.getString(PREF_PWD, null);
		}
		return this.pwd;
	}
	
	/**
	 * 设置当前城市
	 * 
	 * @param user
	 */
	public void setDistrict(String district) {
		if (district != null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			SharedPreferences.Editor editor = preferences.edit();
			if (editor.putString(PREF_DISTRICT, district).commit()) {
				this.cur_district = district;
			}
		}
	}
	
	/**
	 * 获取当前城市
	 * 
	 * @return
	 */
	public String getDistrict() {
		if (this.cur_district == null) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
			this.cur_district = preferences.getString(PREF_DISTRICT, null);
		}
		return this.cur_district;
	}
	
	/**
	 * 设置当前用户类型
	 * 
	 * @param user
	 */
	public void setType(int type) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putInt(PREF_TYPE, type).commit()) {
			this.cur_type = type;
		}
	}
	
	/**
	 * 获取当前用户类型
	 * 
	 * @return
	 */
	public int getType() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		this.cur_type = preferences.getInt(PREF_TYPE, 0);
		return this.cur_type;
	}
	
	/**
	 * 设置当前响应路线id
	 * 
	 * @param user
	 */
	public void setLineId(int line_id) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putInt(PREF_LINE_ID, line_id).commit()) {
			this.line_id = line_id;
		}
	}
	
	/**
	 * 获取当前响应路线id
	 * 
	 * @return
	 */
	public int getLineId() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		this.line_id = preferences.getInt(PREF_LINE_ID, -1);
		return this.line_id;
	}
	
	/**
	 * 设置当前响应路线id
	 * 
	 * @param user
	 */
	public void setLineStatus(int line_type) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		SharedPreferences.Editor editor = preferences.edit();
		if (editor.putInt(PREF_LINE_TYPE, line_type).commit()) {
			this.line_type = line_type;
		}
	}
	
	/**
	 * 获取当前响应路线id
	 * 
	 * @return
	 */
	public int getLineStatus() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		this.line_type = preferences.getInt(PREF_LINE_TYPE, 0);
		return this.line_type;
	}
	
	/**
	 * 退出登录,清空数据
	 */
	public void logout() {
		// 关闭SQLite连接
//		DBHelper.getInstance(applicationContext).closeDB();
		// reset password to null
		
	}
	
}
