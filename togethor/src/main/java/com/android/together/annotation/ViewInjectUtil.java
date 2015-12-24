package com.android.together.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.Activity;

public class ViewInjectUtil {

	private static final String METHOD_SET_CONTENTVIEW = "setContentView";
	private static final String METHOD_SET_VIEWS = "findViewById";
	
	/**
	 * 注入布局文件
	 */
	public static void injectContentView(Activity activity){
		 Class<? extends Activity> clazz = activity.getClass(); 
		 //查询类上是否有注解
		 ContentView contentView = clazz.getAnnotation(ContentView.class);
		 
		 if(contentView!=null){
			 int contentViewValueId = contentView.value();
			 try {
				Method method = clazz.getMethod(METHOD_SET_CONTENTVIEW,  
				         int.class);
				method.setAccessible(true);  
				method.invoke(activity, contentViewValueId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		 }
	}
	
	/**
	 * 注入所有控件
	 */
	public static final void injectViews(Activity activity){
		Class<? extends Activity> clazz = activity.getClass(); 
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			ViewInject viewInject = field.getAnnotation(ViewInject.class);
			
			if(viewInject!=null){
				int resId = viewInject.value();
				try {
					Method method = clazz.getMethod(METHOD_SET_VIEWS,  
					         int.class);
					Object resView = method.invoke(activity, resId);
					field.setAccessible(true);  
					field.set(activity, resView);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static final void inject(Activity activity){
		injectContentView(activity);
		injectViews(activity);
		
	}
}
