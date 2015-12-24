package com.android.togethor.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entity.Line;
import com.android.entity.User;
import com.android.together.BaseActivity;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.util.JsonUtil;
import com.android.together.view.HeaderLayout;
import com.android.together.view.ProgressDialog;
import com.android.together.view.HeaderLayout.HeaderStyle;
import com.android.together.view.HeaderLayout.OnRightLayoutListner;
import com.android.volley.Request;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(value = R.layout.activity_nav)
public class NavigationActivity extends BaseActivity implements OnGetRoutePlanResultListener{

	
	@ViewInject(R.id.btn_menu)
	private Button btn_menu;
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.mapView)
	private MapView mapView;
	
	@ViewInject(R.id.h_scrollview)
	private HorizontalScrollView horizontalScrollView;
	
	@ViewInject(R.id.layout_user)
	private LinearLayout layout_user;
	
	private SlidingMenu slidingMenuu;
	
	private Line line;
	private List<User> pass_list;
	
	private int cur_request = -1;
	
	//搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
	
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		line = (Line) getIntent().getExtras().getSerializable("line_info");
		
		
		
		initMenu();
		slidingMenuu = getSlidingMenu();
		slidingMenuu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		
		
		progressDialog = new ProgressDialog(NavigationActivity.this,"请稍等...");
		progressDialog.show();
		
		MyApplication.getInstance().setLineStatus(2);
		
		if(line!=null){
			cur_request = 0;
			Map<String , String> map = new HashMap<String, String>();
			map.put("method", "nav_line");
			map.put("line_id", line.getId()+"");
			map.put("line_status", 2+"");
			string_request(Constant.LINE_URL, Request.Method.POST, map);
		}else{
			cur_request = 1;
			
			Map<String , String> map = new HashMap<String, String>();
			map.put("method", "sel_line_by_id");
			map.put("line_id", getIntent().getIntExtra("line_id", -1)+"");
			map.put("line_status", 2+"");
			MyApplication.getInstance().setLineStatus(2);
			
			string_request(Constant.LINE_URL, Request.Method.POST, map);
		}
		
		
		
	}
	
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		
		super.respone(response);
		Map<String , Object> map = JsonUtil.Json2Map(response);
		switch(cur_request){
		case 0:
			
			if(map!=null){
				String status = (String) map.get("status");
				String message = (String) map.get("message");
				if(status.equals("200")){
					initLineView();
					setUserView(line);
				}else{
					
				}
			}
			break;
		case 1:
			line = getLineInfo(map); 
			if(line!=null){
				initLineView();
				setUserView(line);
			}else{
				//showShortToast("");
				finish();
			}
			break;
		}
		
		headerLayout.setHandyOnlyText("一起("+line.getReason()+")");
		
		headerLayout.init(HeaderStyle.TITLE_FOR_HELP);
		headerLayout.setTextForItem(2, "结束");
		
		headerLayout.setLayoutLinster(null, null, new OnRightLayoutListner() {
			
			@Override
			public void onClick() {
				MyApplication.getInstance().setLineStatus(-1);
				startActivity(EndLineActivity.class);
				finish();
			}
		});
		
		progressDialog.dismiss();
	}
	
	public void setUserView(Line line){
		pass_list = line.getPassenger_List();
		if(pass_list!=null){
			for(int i=0 ; i<pass_list.size()+1;i++){
				View view = LayoutInflater.from(NavigationActivity.this).inflate(R.layout.layout_nav_user, null);
				view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT, 1.0f) );
				TextView tv_name = (TextView) view.findViewById(R.id.tv_user_name);
				TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
				ImageView iv = (ImageView) view.findViewById(R.id.iv_head);
				
				if(i==0){
					tv_name.setText(line.getDriver().getName());
					tv_type.setText("车主");
					ImageLoader.getInstance().displayImage(line.getDriver().getHead_url(), iv,options);
				}else{
					
					tv_name.setText(pass_list.get(i-1).getName());
					tv_type.setText("乘客"+i);
					ImageLoader.getInstance().displayImage(line.getUrl(), iv,options);
				}
				
				layout_user.addView(view);
			}
		}
	}
	
	
	private void initLineView(){
		//设置起终点信息，对于tranist search 来说，城市名无意义
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("深圳", line.getStartAddr());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("深圳", "西丽法庭");
        PlanNode stNode_1 = PlanNode.withCityNameAndPlaceName("深圳", "莲花山");
        PlanNode enNode_1 = PlanNode.withCityNameAndPlaceName("深圳", "塘朗山");
        PlanNode enNode_2 = PlanNode.withCityNameAndPlaceName("深圳", line.getEndAddr());
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        
        mSearch.setOnGetRoutePlanResultListener(this);
        
//        wayList.add(enNode_1);
        
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode_2));
        
	}
	
	@Override
	public void setListner() {
		btn_menu.setOnClickListener(this);
	}
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		switch(view.getId()){
		case R.id.btn_menu:
			slidingMenuu.toggle();
			break;
		}
	}
	
	
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
        	BaiduMap  baiduMap = mapView.getMap();
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
//            routeOverlay = overlay;
            baiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(result.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
        }
	}

	@Override
	public void onGetTransitRouteResult(TransitRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

    	private BaiduMap baiduMap;
    	
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
            this.baiduMap = baiduMap;
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.trip_self_bubble_startpoint);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.drawable.trip_self_bubble_endpoint);
        }

//		@Override
//		public void setData(DrivingRouteLine drivingRouteLine) {
//			// TODO Auto-generated method stub
//			super.setData(drivingRouteLine);
//			OverlayOptions overlayOptions = null;  
//			Marker marker = null; 
//			List<RouteNode> li = drivingRouteLine.getWayPoints();
//			if()
//			for(RouteNode rd:li){
//				LatLng ll = rd.getLocation();
//				System.out.println(rd.getTitle());
//				// 设置自定义图标  
//                BitmapDescriptor mCurrentMarker = getTerminalMarker();
//                
//                overlayOptions = new MarkerOptions().position(ll)  
//                        .icon(mCurrentMarker);  
//                baiduMap.addOverlay(overlayOptions); 
//			}
//		}
        
        
     
    }

}
