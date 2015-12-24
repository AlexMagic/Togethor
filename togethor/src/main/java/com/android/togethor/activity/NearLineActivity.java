package com.android.togethor.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

import com.android.entity.Line;
import com.android.entity.User;
import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.adapter.NearLineAdapter;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.popupwindow.SelTypePopupWindow;
import com.android.together.util.JsonUtil;
import com.android.together.util.LocationUtil;
import com.android.together.util.LocationUtil.PointCallback;
import com.android.together.view.HeaderLayout;
import com.android.together.view.HeaderLayout.HeaderStyle;
import com.android.together.view.HeaderLayout.OnRightLayoutListner;
import com.android.together.view.MyRefreshListView;
import com.android.together.view.MyRefreshListView.OnCancelListener;
import com.android.together.view.MyRefreshListView.OnRefreshListener;
import com.android.volley.Request;
import com.baidu.location.BDLocation;

@ContentView(value = R.layout.near_line)
public class NearLineActivity extends BaseActivity implements OnRefreshListener, OnCancelListener{

	private List<Line> line_list;
	private NearLineAdapter adapter;
	
	@ViewInject(R.id.listview)
	private MyRefreshListView listView;
	
	@ViewInject(R.id.layout_publish)
	private LinearLayout layout_publish;
	
	@ViewInject(R.id.layout_search)
	private LinearLayout layout_search;
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.loading_layout)
	private LinearLayout loading_layout;
	
	@ViewInject(R.id.layout_none)
	private  RelativeLayout none_layout;
	
	private SelTypePopupWindow selTypePopupWindow;
	
	private LocationUtil locationUtil ;
	
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			
		}
	};
	
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		initMenu();
		headerLayout.setIcon(R.drawable.bg_sliding_btn);
		headerLayout.setHandyOnlyText("推荐路线");
		
		headerLayout.init(HeaderStyle.TITLE_FOR_HELP);
		headerLayout.setTextForItem(2, "注销");
		headerLayout.setLayoutLinster(null, null, new OnRightLayoutListner() {
			
			@Override
			public void onClick() {
				MyApplication.getInstance().setUserName(null);
				MyApplication.getInstance().setPwd(null);
				//MyApplication.getInstance().setLineId(-1);
				//MyApplication.getInstance().setLineStatus(-1);
				//MyApplication.getInstance().setType(-1);
				startActivity(LoginActivity.class);
				finish();
			}
		});
		
		locationUtil = LocationUtil.getInstance(NearLineActivity.this);
		
		locationUtil.setCallBack(new PointCallback() {
			
			@Override
			public void callback(BDLocation location) {
				Map<String , String> map = new LinkedHashMap<String, String>();
				map.put("method", "comm_line");
				map.put("lat", location.getLatitude()+"");
				map.put("lng", location.getLongitude()+"");
				System.out.println(location.getLatitude()+"   "+location.getLongitude());
				
				string_request(Constant.LINE_URL, Request.Method.POST, map);
			}
		});
		
		locationUtil.getmLocationClient().start();
		
		
	
	}
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		super.respone(response);
		
		List<LinkedHashMap<String, Object>> comm_line = JsonUtil.json2List(response);
		
		line_list = new ArrayList<Line>();
		
		if(comm_line!=null && comm_line.size()>0){
			//Map<String, Object> returnMap = comm_line.get(0);
			
//			String s = (String)returnMap.get("status");
//			if(s.equals("200")){
				
				//List<LinkedHashMap<String, Object>> line_Info = (List<LinkedHashMap<String, Object>>)returnMap.get("line_info");
				
				//System.out.println("line_Info.size()--:"+line_Info.size());
				
				Map<String, Object> temp;
				
				
				
				for(int i=0 ; i<comm_line.size();i++){
					Line line = new Line();
					
					temp = comm_line.get(i);
					
					line.setId(Integer.parseInt((String)temp.get("line_id")));
					line.setStartAddr((String)temp.get("start_addr"));
					line.setEndAddr((String)temp.get("end_addr"));
//					line.setDateTime(DateUtil.changeLong(Long.parseLong((String)temp.get("date"))));
					line.setDateTime((String)temp.get("date"));
					line.setNum((Integer)temp.get("num"));
					line.setType(Integer.parseInt((String)temp.get("type")));
					line.setReason((String)temp.get("reason"));
					line.setUrl((String)temp.get("head_url"));
					line.setStatus(Integer.parseInt((String)temp.get("status")));
					line.setPhone((String)temp.get("phone"));
					if(line.getType()==1)
						line.setLeft_num((Integer)temp.get("left_num"));
					//line.setUser(setUser((LinkedHashMap<String, Object>)temp.get("user_info")));
					
					if(Integer.parseInt((String)temp.get("status"))==0 || Integer.parseInt((String)temp.get("status"))==1){
						line_list.add(line);
					}
				}
				
				loading_layout.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				
				adapter = new NearLineAdapter(this, line_list, R.layout.near_line_item);
				
				listView.setAdapter(adapter);
			
			
		}else{
			loading_layout.setVisibility(View.GONE);
			none_layout.setVisibility(View.VISIBLE);
		}
	}
	
	private User setUser(LinkedHashMap<String, Object> user_info){
		User user = new User();
		
		user.setPhone((String)user_info.get("phone"));
		user.setHead_url((String)user_info.get("head_url"));
		user.setName((String)user_info.get("name"));
		
		return user;
	}
	
	
	
	
	@Override
	public void setListner() {
		layout_publish.setOnClickListener(this);
		layout_search.setOnClickListener(this);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent detail_intent = new Intent(NearLineActivity.this , LineDetailActivity.class);
				Bundle b = new Bundle();
				b.putSerializable("line_info", (Serializable) listView.getItemAtPosition(position));
				detail_intent.putExtra("line_info", (Serializable) listView.getItemAtPosition(position));
				
				startActivity(detail_intent);
			}
		});
		
		listView.setOnRefreshListener(this);
		
	}
	
	
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		switch(view.getId()){
		case R.id.layout_publish:
			//MyApplication.getInstance().setType(0);
			selTypePopupWindow = new SelTypePopupWindow(NearLineActivity.this);
			if(MyApplication.getInstance().getType()==1){
				
				
				selTypePopupWindow.setOnDriverClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						selTypePopupWindow.dismiss();
						Intent publish_intent = new Intent(NearLineActivity.this , PublishLineActivity.class);
						publish_intent.putExtra("type", 1);
						startActivity(publish_intent);
						overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
					}
				});
				
				selTypePopupWindow.setOnPassClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						selTypePopupWindow.dismiss();
						Intent publish_intent = new Intent(NearLineActivity.this , PublishLineActivity.class);
						publish_intent.putExtra("type", 0);
						startActivity(publish_intent);
						overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
					}
				});
				
				selTypePopupWindow.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss() {
						// TODO Auto-generated method stub
						NearLineActivity.this.setBgAlpha(null);
					}
				});
				selTypePopupWindow.initEvents();
				NearLineActivity.this.setBgAlpha(selTypePopupWindow);
				selTypePopupWindow.showViewCenter(selTypePopupWindow.getView());
			}else{
				Intent publish_intent = new Intent(NearLineActivity.this , PublishLineActivity.class);
				startActivity(publish_intent);
				overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
			}
			
			break;
		case R.id.layout_search:
			Intent _intent = new Intent(NearLineActivity.this , SearchLineActivity.class);
			startActivity(_intent);
			overridePendingTransition(R.anim.roll_left	, R.anim.roll_right);
			break;
		}
	}

	public void setselTypePopupWindow(){
		
		
		
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		listView.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		listView.onRefreshComplete();
	}
	
	
}
