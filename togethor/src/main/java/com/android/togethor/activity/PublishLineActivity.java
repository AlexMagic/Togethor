package com.android.togethor.activity;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.popupwindow.AddrSearchPopupWindow;
import com.android.together.util.DateUtil;
import com.android.together.util.JsonUtil;
import com.android.together.util.StringUtil;
import com.android.together.view.HeaderLayout;
import com.android.together.view.HeaderLayout.OnIconClickListner;
import com.android.together.view.ProgressDialog;
import com.android.volley.Request;

@ContentView(value = R.layout.publish_line)
public class PublishLineActivity extends BaseActivity {

	public final static int SEARCH_FOR_START = 0;
	public final static int SEARCH_FOR_END = 1;
	public final static int DATE_TIME = 2;
	
	private int type;
	
	@ViewInject(R.id.layout_start)
	private LinearLayout layout_start;
	
	@ViewInject(R.id.tv_start_addr)
	private TextView tv_start_addr;
	
	
	@ViewInject(R.id.layout_end)
	private LinearLayout layout_end;
	
	@ViewInject(R.id.tv_end_addr)
	private TextView tv_end_addr;

	@ViewInject(R.id.layout_date)
	private LinearLayout layout_date;
	
	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	
	@ViewInject(R.id.btn_publish)
	private Button btn_publish;
	
	@ViewInject(R.id.radiogroup)
	private RadioGroup radioGroup;
	
	@ViewInject(R.id.rb_on_work)
	private RadioButton rb_on_work;
	
	@ViewInject(R.id.rb_left_work)
	private RadioButton rb_left_work;
	
	@ViewInject(R.id.rb_go_out)
	private RadioButton rb_go_out;
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.people_layout)
	private LinearLayout people_layout;
	private RelativeLayout[] views;
	
	@ViewInject(R.id.tv_mode)
	private TextView tv_mode;
	
	private int people_num=0;
	private String reason = "";
	private String[] points = new String[2];
	
	private AddrSearchPopupWindow addrSearchPopupWindow;
	
	private ProgressDialog progressDialog;
	
	private Intent search_intent;
	
	
	
	@Override
	public void init() {
		super.init();
		
		type = getIntent().getIntExtra("type", 0);
		
		if(type==0){
			tv_mode.setText("上车人数:");
			headerLayout.setHandyOnlyText("(乘客)发布路线");
		}else{
			tv_mode.setText("接载人数:");
			headerLayout.setHandyOnlyText("(车主)发布路线");
		}
		headerLayout.setIcon(R.drawable.bg_btn_header_back);
		
		headerLayout.setIconClickListner(new OnIconClickListner() {
			
			@Override
			public void onClick() {
				PublishLineActivity.this.finish();
			}
		});
	}
	
	
	@Override
	public void setListner() {
		initPeopleView(people_layout);
		layout_start.setOnClickListener(this);
		layout_end.setOnClickListener(this);
		layout_date.setOnClickListener(this);
		btn_publish.setOnClickListener(this);
		
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup rb, int id) {
				// TODO Auto-generated method stub
				switch(id){
				case R.id.rb_on_work:
					reason ="上班";
					break;
				case R.id.rb_left_work:
					reason ="下班";
					break;
				case R.id.rb_go_out:
					reason ="出游";
					break;
				}
			}
		});
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		super.onClick(view);
		addrSearchPopupWindow = new AddrSearchPopupWindow(PublishLineActivity.this);
		addrSearchPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				PublishLineActivity.this.setBgAlpha(null);
				String str_addr = addrSearchPopupWindow.getStr_addr();
				if(str_addr!=null){
					if(addrSearchPopupWindow.isStart_or_end()){
						tv_start_addr.setText(str_addr);
					}else{
						tv_end_addr.setText(str_addr);
					}
				}else{
					if(addrSearchPopupWindow.isStart_or_end()){
						tv_start_addr.setHint("出发地点");
					}else{
						tv_end_addr.setHint("目的地点");
					}
				}
			}
		});
		
		switch(view.getId()){
		case R.id.layout_start:
			
			 
			
			if(search_intent == null){
				search_intent = new Intent(PublishLineActivity.this , SearchActivity.class);
			}
			
			startActivityForResult(search_intent, SEARCH_FOR_START);
			break;
		case R.id.layout_end:
			
			if(search_intent == null){
				search_intent = new Intent(PublishLineActivity.this , SearchActivity.class);
			}
			
			startActivityForResult(search_intent, SEARCH_FOR_END);
			
			
			
			break;
		case R.id.layout_date:
			//弹出日期选择框
			Intent intent = new Intent(PublishLineActivity.this , DateAndTimePicker.class);
			startActivityForResult(intent, DATE_TIME);
			break;
		case R.id.btn_publish:
//			Intent line_intent = new Intent(PublishLineActivity.this , NearLineActivity.class);

			if(people_num>0 && !reason.equals("")
					&&!tv_end_addr.getText().toString().equals("")
					&&!tv_start_addr.getText().toString().equals("")
					&&!tv_date.getText().toString().equals("")){
				
				string_request(Constant.LINE_URL, Request.Method.POST, setLineInfo());
			
			}else{
				Toast.makeText(PublishLineActivity.this, "请填写完整路线信息", Toast.LENGTH_SHORT).show();
			}  
			
			
			break;
		case R.id.people_1:
			people_num = 1;
			setUpPeopleView(people_num , views);
			break;
		case R.id.people_2:
			people_num = 2;
			setUpPeopleView(people_num , views);
			break;
		case R.id.people_3:
			people_num = 3;
			setUpPeopleView(people_num , views);
			break;
		case R.id.people_4:
			people_num = 4;
			setUpPeopleView(people_num , views);
			break;
		case R.id.people_5:
			people_num = 5;
			setUpPeopleView(people_num , views);
			break;
		}
	}
	
	//乘客和司机的view
	public void setViewForUser(int type){
		
	}
	
	
	public void initPeopleView(LinearLayout people_layout){
		if(people_layout!=null){
			int child_num = people_layout.getChildCount();
			views = new RelativeLayout[child_num-1];
			for(int i=1 ; i<child_num ; i++){
				RelativeLayout view = (RelativeLayout) people_layout.getChildAt(i);
				view.setOnClickListener(this);
				views[i-1] = view;
			}
		}
	}
	
	public void setUpPeopleView(int count , RelativeLayout[] views){
		if(count>0){
			//重置
			for(int i=0;i<views.length ; i++){
				RelativeLayout view = views[i];
				View v = view.getChildAt(1);
				v.setVisibility(View.INVISIBLE);
			}
			
			for(int i=0;i<count ; i++){
				RelativeLayout view = views[i];
				View v = view.getChildAt(1);
				v.setVisibility(View.VISIBLE);
			}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			switch(requestCode){
			case SEARCH_FOR_START:
				String addr_start = data.getStringExtra("addr");
				String point_start = data.getStringExtra("point");
				points[0] = point_start;
				tv_start_addr.setText(addr_start);
				break;
			case SEARCH_FOR_END:
				String addr_end = data.getStringExtra("addr");
				String point_end = data.getStringExtra("point");
				points[1] = point_end;
				tv_end_addr.setText(addr_end);
				break;
			case DATE_TIME:
				String date = DateUtil.changeLong(data.getLongExtra("date_and_time", -1));
				tv_date.setText(date);
				break;
			}
		}
	}
	
	public Map<String , String> setLineInfo(){
		Map<String , Object> line_info = new LinkedHashMap<String, Object>();
		Map<String , String> temp = new LinkedHashMap<String, String>();
		
		line_info.put("phone", MyApplication.getInstance().getTempPhone());
		line_info.put("start_addr", StringUtil.decodeString(tv_start_addr.getText().toString()));
		line_info.put("end_addr", StringUtil.decodeString(tv_end_addr.getText().toString()));
		line_info.put("date_time", StringUtil.decodeString(tv_date.getText().toString()));
		line_info.put("num", people_num);
		line_info.put("reason", StringUtil.decodeString(reason));
		line_info.put("type", type);
		
		for(int i=0 ; i < points.length ; i++){
			String point = points[i];
			String[] p = point.split(",");
			switch(i){
			case 0:
				line_info.put("start_lat", p[0]);
				line_info.put("start_lng", p[1]);
				break;
			case 1:
				line_info.put("end_lat", p[0]);
				line_info.put("end_lng", p[1]);
				break;
			}
		}
		
		
		String json = JsonUtil.Map2Json(line_info);
		System.out.println(json);
		temp.put("info", json);
		temp.put("method", "insert_line");
		
		return temp;
	}

	@Override
	public void respone(String response) {
		super.respone(response);
			
		Map<String , Object> map = JsonUtil.Json2Map(response);
			
		if(map!=null){
			Set<String> set = map.keySet();
			for (Iterator<String> it = set.iterator(); it.hasNext();){
				String key = it.next();
	            if(key.equals("status")){
	         		String status = (String) map.get(key);
		    		if(status.equals("200")){
		    			  Toast.makeText(PublishLineActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
			        }else{
			        	Toast.makeText(PublishLineActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
			        	return ;
			        }
	            }
		        	   
		        if(key.equals("line_id")){
	        	   int line_id = (Integer) map.get(key);
	        	   if(type==1){
	        		   Intent intent_1 = new Intent(PublishLineActivity.this , PassengerWaitingActivity.class);
	        		   intent_1.putExtra("line_id", line_id);
	        		   startActivity(intent_1);
	        	   }else if(type==0){
	        		   Intent intent_1 = new Intent(PublishLineActivity.this , DriverWaitingActivity.class);
	        		   intent_1.putExtra("line_id", line_id);
	        		   startActivity(intent_1);
	        	   }
		         }
			}
		}else{
			Toast.makeText(PublishLineActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public void error(String error) {
		// TODO Auto-generated method stub
		super.error(error);
		System.out.println("error--:"+error);
	}
	
	

}
