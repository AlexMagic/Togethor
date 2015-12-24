package com.android.together.util;

import java.util.Random;

public class MathUtil {
	public static String randomNum(){
		String num = "";
		Random random = new Random();
		for(int i=0;i<6;i++){
			int n = random.nextInt(9);
			num += n;
		}
		
		return num;
	}
}
