package com.android.together.util;

import java.util.regex.Pattern;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

public class InvalidateUtil {
	
	public static boolean isNull(View view) {
		
		if (view instanceof EditText) {
			EditText editText = (EditText) view;
			String text = editText.getText().toString().trim();
			if (text != null && text.length() > 0) {
				return false;
			}
		}
		
		if(view instanceof RadioButton){
			RadioButton rb = (RadioButton) view;
			if(rb.isChecked()){
				return false;
			}
		}
		
		return true;
		
	}

	public static boolean matchPhone(String text) {
		if (Pattern.compile("(\\d{11})|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	public static boolean matchEmail(String text) {
		if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
				.matches()) {
			return true;
		}
		return false;
	}
	
	public static boolean isEqual(EditText editText_1 , EditText editText_2) {
		String text_1 = editText_1.getText().toString().trim();
		String text_2 = editText_2.getText().toString().trim();
		
		if(text_1.equals(text_2)){
			return true;
		}
		return false;
	}
	
}
