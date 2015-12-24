package com.android.togethor.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.android.entity.Line;
import com.android.together.BaseActivity;
import com.android.together.BasePopupWindow.onSubmitClickListener;
import com.android.together.MyApplication;
import com.android.together.R;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.constant.Constant;
import com.android.together.popupwindow.RegTipsPopupwindow;
import com.android.together.popupwindow.SelTypePopupWindow;
import com.android.together.util.JsonUtil;
import com.android.together.view.CircleImageView;
import com.android.together.view.HeaderLayout;
import com.android.together.view.HeaderLayout.HeaderStyle;
import com.android.together.view.HeaderLayout.OnIconClickListner;
import com.android.together.view.HeaderLayout.OnRightLayoutListner;
import com.android.together.view.ProgressDialog;
import com.android.volley.Request;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
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
import com.nostra13.universalimageloader.core.ImageLoader;

@ContentView(value = R.layout.line_detail)
public class LineDetailActivity extends BaseActivity implements OnGetRoutePlanResultListener{

	private String line_1 = "深圳职业技术学院";
	private String line_2 = "世界之窗";
	
	private boolean temp = false;
	
	private int sel_type = 0;
	
	private Intent intent;
	private Line line;
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.mapView)
	private MapView mapView;
	
	@ViewInject(R.id.tv_start_addr)
	private TextView tv_start_addr;
	
	@ViewInject(R.id.tv_end_addr)
	private TextView tv_end_addr;

	@ViewInject(R.id.tv_date)
	private TextView tv_date;
	
	@ViewInject(R.id.tv_reason)
	private TextView tv_reason;
	
	@ViewInject(R.id.iv_type)
	private ImageView iv_type;
	
	@ViewInject(R.id.iv_head)
	private ImageView circleImageView;
	
	private RegTipsPopupwindow tipsPopupwindow;
	private SelTypePopupWindow selTypePopupWindow;
	
	private ProgressDialog progressDialog;
	
	public LocationClient mLocationClient;
//    public MyLocationListener mMyLocationListener;
	
    //搜索相关
    private RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch_1 = null;    // 搜索模块，也可去掉地图模块独立使用
    private RoutePlanSearch mSearch_2 = null;    // 搜索模块，也可去掉地图模块独立使用
      
    private boolean isRequest = false;//是否手动触发请求定位  
    private boolean isFirstLoc = true;//是否首次定位  
      
    /** 
     * 弹出窗口图层的View 
     */  
    private View mPopupView;  
    private BDLocation location;  
	

	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		
		intent = this.getIntent();
		line = (Line) intent.getSerializableExtra("line_info");
		
		tv_start_addr.setText(line.getStartAddr());
		tv_end_addr.setText(line.getEndAddr());
		tv_date.setText(line.getDateTime());
		tv_reason.setText(line.getReason());
		ImageLoader.getInstance().displayImage(line.getUrl(), circleImageView,options);
		
		if(line.getType()==1){
			iv_type.setImageResource(R.drawable.bts_homepage_owner);
		}else{
			iv_type.setImageResource(R.drawable.bts_homepage_passenger);
		}
		
		headerLayout.setIcon(R.drawable.bg_btn_header_back);
		headerLayout.setHandyOnlyText("路线详情");
		headerLayout.setIconClickListner(new OnIconClickListner() {
			
			@Override
			public void onClick() {
				LineDetailActivity.this.finish();
			}
		});
		
		headerLayout.init(HeaderStyle.TITLE_FOR_HELP);
		headerLayout.setTextForItem(2, "响应");
		
		headerLayout.setLayoutLinster(null, null, new MyRightLayoutListner());
		
		initMenu();
//		mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
	}
	
	
	class MyRightLayoutListner implements OnRightLayoutListner{

		@Override
		public void onClick() {
			
			setTipsPopupwindow();
		}
		
	}
	
	@Override
	public void respone(String response) {
		// TODO Auto-generated method stub
		super.respone(response);
		progressDialog.dismiss();
		
		Map<String,Object> res_map = JsonUtil.Json2Map(response);
		if(res_map!=null){
			//LinkedHashMap<String, Object> base_info = (LinkedHashMap<String, Object>) res_map.get("base_info");
			//LinkedHashMap<String, Object> user_info = (LinkedHashMap<String, Object>) res_map.get("user_info");
			
			String status = (String) res_map.get("status");
			String msg = (String) res_map.get("message");
			if(status.equals("200")){
				/**
				 * 如果响应人为乘客身份
				 * 如果改路线发布人为车主则进入车主界面，同时把消息推送给给车主
				 * 如果改路线发布人为乘客进去车主界面，同时等待车主响应（推送给合适的车主，让车主响应）
				 * 
				 * 如果响应人为车主身份
				 * 如果该路线发布人为车主，则提示错误
				 * 如果该路线发布人为乘客进入乘客界面，同时把消息推送给乘客
				 * 
				 */
				
				MyApplication.getInstance().setLineId(line.getId());
				
				if(sel_type == 0){
					if(line.getType()==1){
						
					}else{
						
					}
					Intent pass_intent = new Intent(LineDetailActivity.this,DriverWaitingActivity.class);
					pass_intent.putExtra("line_id", line.getId());
					startActivity(pass_intent);
					finish();
				}else if(sel_type == 1){

					Intent pass_intent = new Intent(LineDetailActivity.this,PassengerWaitingActivity.class);
					pass_intent.putExtra("line_id", line.getId());
					startActivity(pass_intent);
					finish();
				}
				
			}else{
				Toast.makeText(LineDetailActivity.this,msg , Toast.LENGTH_SHORT).show();
			}
			
		}else{
			Toast.makeText(LineDetailActivity.this,response , Toast.LENGTH_SHORT).show();
		}
	}
	
	
	@Override
	public void setListner() {
		
	}
	

	public void initMenu(){
		mapView.showZoomControls(false);
		mLocationClient = new LocationClient(this.getApplicationContext());
		initLocation();
		initLineView();
//        mMyLocationListener = new MyLocationListener();
//        mLocationClient.registerLocationListener(mMyLocationListener);
	}
	
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	 
	private String tempcoor="gcj02";
	 private void initLocation(){
	        LocationClientOption option = new LocationClientOption();
	        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
	        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
	        
	        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
	        option.setOpenGps(true);//可选，默认false,设置是否使用gps
	        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
	        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
	        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
	        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
	        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
	        mLocationClient.setLocOption(option);
	    }
	
	public void setTipsPopupwindow(){
		tipsPopupwindow = new RegTipsPopupwindow(LineDetailActivity.this);
		tipsPopupwindow.setContext("响应该路线后将无法选择其他路线，是否响应？");
		tipsPopupwindow.btnContent(0, "再去看看");
		tipsPopupwindow.btnContent(1, "确定");
		
		tipsPopupwindow.setOnSubmitClickListener(new onSubmitClickListener() {
			
			@Override
			public void onClick() {
				temp = true;
				tipsPopupwindow.dismiss();
//				Intent intent = new Intent(LineDetailActivity.this , PassengerWaitingActivity.class);
//				startActivity(intent);
//				finish();
			}
		});
		
		tipsPopupwindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				LineDetailActivity.this.setBgAlpha(null);
				
				if(temp){
					if(MyApplication.getInstance().getType()==1){
						selTypePopupWindow = new SelTypePopupWindow(LineDetailActivity.this);
						
						selTypePopupWindow.setOnDriverClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								LineDetailActivity.this.setBgAlpha(null);
								progressDialog = new ProgressDialog(LineDetailActivity.this , "正在响应...");
								progressDialog.show();
								
								Map<String , String> map = new HashMap<String, String>();
								map.put("line_id", line.getId()+"");
								map.put("method", "insert_user");
								
								Map<String, Object> data = new LinkedHashMap<String, Object>();
								data.put("phone", line.getPhone());
								data.put("type", 1);
								
								String data_json = JsonUtil.Map2Json(data);
								
								map.put("data_json", data_json);
								
								sel_type = 1;
								
								string_request(Constant.LINE_URL, Request.Method.POST, map);
							}
						});
						
						selTypePopupWindow.setOnPassClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								LineDetailActivity.this.setBgAlpha(null);
								progressDialog = new ProgressDialog(LineDetailActivity.this , "正在响应...");
								progressDialog.show();
								
								Map<String , String> map = new HashMap<String, String>();
								map.put("line_id", line.getId()+"");
								map.put("method", "insert_user");
								
								Map<String, Object> data = new LinkedHashMap<String, Object>();
								data.put("phone", line.getPhone());
								data.put("type", 0);
								
								String data_json = JsonUtil.Map2Json(data);
								
								map.put("data_json", data_json);
								sel_type = 0;
//								System.out.println(data_json+"s");
								string_request(Constant.LINE_URL, Request.Method.POST, map);
							}
						});
						
						selTypePopupWindow.setOnDismissListener(new OnDismissListener() {
							
							@Override
							public void onDismiss() {
	//							// TODO Auto-generated method stub
								LineDetailActivity.this.setBgAlpha(null);
	
								
	
							}
						});
						//LineDetailActivity.this.setBgAlpha(selTypePopupWindow);
						selTypePopupWindow.initEvents();
						selTypePopupWindow.showViewCenter(selTypePopupWindow.getView());
					}else{
						//setTipsPopupwindow();
						progressDialog = new ProgressDialog(LineDetailActivity.this , "添加中...");
						progressDialog.show();
						
						Map<String , String> map = new HashMap<String, String>();
						map.put("line_id", line.getId()+"");
						map.put("method", "insert_user");
						
						Map<String, Object> data = new LinkedHashMap<String, Object>();
						data.put("phone", MyApplication.getInstance().getUserName());
						data.put("type", 0);
						
						String data_json = JsonUtil.Map2Json(data);
						
						map.put("data_json", data_json);
						
						string_request(Constant.LINE_URL, Request.Method.POST, map);
					}
				}
			}
		});
		
		LineDetailActivity.this.setBgAlpha(tipsPopupwindow);
		tipsPopupwindow.showViewCenter(tipsPopupwindow.getView());
	}
	
	
	/**
     * 实现实时位置回调监听
     */
//    public class MyLocationListener implements BDLocationListener {
//
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//            //Receive Location
//            StringBuffer sb = new StringBuffer(256);
//            sb.append("time : ");
//            sb.append(location.getTime());
//            sb.append("\nerror code : ");
//            sb.append(location.getLocType());
//            sb.append("\nlatitude : ");
//            sb.append(location.getLatitude());
//            sb.append("\nlontitude : ");
//            sb.append(location.getLongitude());
//            sb.append("\nradius : ");
//            sb.append(location.getRadius());
//            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// 单位：公里每小时
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// 单位：米
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps定位成功");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //运营商信息
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("网络定位成功");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
//                sb.append("\ndescribe : ");
//                sb.append("离线定位成功，离线定位结果也是有效的");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("网络不同导致定位失败，请检查网络是否通畅");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
//            }
//            sb.append("\nlocationdescribe : ");// 位置语义化信息
//            sb.append(location.getLocationDescribe());
//            List<Poi> list = location.getPoiList();// POI信息
//            if (list != null) {
//                sb.append("\npoilist size = : ");
//                sb.append(list.size());
//                for (Poi p : list) {
//                    sb.append("\npoi= : ");
//                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
//                }
//            }
//            
//            System.out.println("sb.toString()---:"+sb.toString());
//            MyLocationData locData = new MyLocationData.Builder()  
//            .accuracy(location.getRadius())
//            // 此处设置开发者获取到的方向信息，顺时针0-360  
//            .direction(100).latitude(location.getLatitude())  
//            .longitude(location.getLongitude()).build();
//            
//            LatLng ll = new LatLng(location.getLatitude(),  location.getLongitude()); 
//            BaiduMap mBaiduMap = mapView.getMap();
//            mBaiduMap.setMyLocationEnabled(true);
//            mBaiduMap.setMyLocationData(locData); 
//            
//            MapStatusUpdate u_1 = MapStatusUpdateFactory.newLatLng(ll);
//            MapStatusUpdate u_2 = MapStatusUpdateFactory.zoomTo(15);
//            mBaiduMap.animateMapStatus(u_1);
//            mBaiduMap.animateMapStatus(u_2);
//            
//        }
//
//
//    }
	List<PlanNode> wayList = new ArrayList<PlanNode>();
	
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
        
        wayList.add(enNode_1);
        
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(stNode)
                .to(enNode_2));
        
//        mSearch_1.drivingSearch((new DrivingRoutePlanOption())
//                .from(enNode_1)
//                .to(enNode_2));
        
//        mSearch_2.drivingSearch((new DrivingRoutePlanOption())
//                .from(enNode_1)
//                .to(enNode_2));
	}

	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(LineDetailActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
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
