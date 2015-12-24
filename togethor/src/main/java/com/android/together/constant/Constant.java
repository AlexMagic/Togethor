package com.android.together.constant;

import android.os.Environment;

public class Constant {
	public static int RESEND_SCONDE = 60;
	public static int SEND_STATE = 0;//发送状态
	public static int RESEND_STATE = 1;//超时重新获取
	
	public final static int REQUEST_CAMERA = 0;
	public final static int REQUEST_LOCAL = 1;
	
	
	public final static String IMG_PATH = Environment.getExternalStorageDirectory() + "/together_img/";
	
	//请求地址
	public final static String VAL_URL = "http://1.cometogether.sinaapp.com/mycode/SendTemplateSMS.php?";
	public final static String REG_URL = "http://1.cometogether.sinaapp.com/mycode/register.php";
	public final static String LOGIN_URL = "http://1.cometogether.sinaapp.com/mycode/Login.php";
	public final static String USER_CAR_URL = "http://1.cometogether.sinaapp.com/mycode/User.php";
	public final static String LINE_URL = "http://1.cometogether.sinaapp.com/mycode/Line.php";
	
	public final static String IMAGE_URL = "http://1.cometogether.sinaapp.com/mycode/Image.php?";
	
	//新浪sae推送标记
	public static final String NAME_PUSH_SERVICE = "com.sina.push.service.SinaPushService";
	public static final String APPID = "24003";
	
}
