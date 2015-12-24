package com.android.together.util;

import java.io.UnsupportedEncodingException;

public class StringUtil {
	public static String decodeString(String str){
		String s = null;
		try {
			s = new String(str.getBytes("UTF-8"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return s;
	}
	
	public static String changeStringForAbc(String str){
		String s = str+"*";
		return s;
	}
	
	public static String changeString(String str){
		String s = str+",";
		return s;
	}
}
