package com.android.togethor.activity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.android.together.BaseActivity;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.util.DateUtil;

@ContentView(R.layout.date_time_picker)
public class DateAndTimePicker extends BaseActivity {

	@ViewInject(R.id.dpPicker)
	private DatePicker datePicker;
	
	@ViewInject(R.id.tpPicker)
	private TimePicker timePicker;
	
	@ViewInject(R.id.btn_close)
	private ImageView btn_cancel;
	
	private Calendar cal = Calendar.getInstance();
	
	private String dateAndTime;
	private String date , time;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);  
		timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);  
		
		date = cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE);
		if(cal.get(Calendar.MINUTE)<10)
			time = cal.get(Calendar.HOUR_OF_DAY)+":0"+cal.get(Calendar.MINUTE);
		else
			time = cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
	}
	
	@Override
	public void setListner() {
		datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				// TODO Auto-generated method stub
				cal.set(year, monthOfYear, dayOfMonth);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				date = format.format(cal.getTime());
			}
		});
		
		timePicker.setIs24HourView(true);
		timePicker.setOnTimeChangedListener(new OnTimeChangedListener() {
			
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay,
					int minute) {
				
				if(minute<10)
					time = hourOfDay+":0"+minute;
				else
					time = hourOfDay+":"+minute;
			}
		});
		
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dateAndTime = "";
				dateAndTime = date+" "+time;
				//System.out.println(dateAndTime);
				long time = DateUtil.changeDateFomate(dateAndTime);
				Intent intent = new Intent();
				intent.putExtra("date_and_time", time);
				
				DateAndTimePicker.this.setResult(RESULT_OK, intent);
				DateAndTimePicker.this.finish();
			}
		});
	}

}
