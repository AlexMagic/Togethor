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

	private String line_1 = "����ְҵ����ѧԺ";
	private String line_2 = "����֮��";
	
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
	
    //�������
    private RoutePlanSearch mSearch = null;    // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
    private RoutePlanSearch mSearch_1 = null;    // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
    private RoutePlanSearch mSearch_2 = null;    // ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
      
    private boolean isRequest = false;//�Ƿ��ֶ���������λ  
    private boolean isFirstLoc = true;//�Ƿ��״ζ�λ  
      
    /** 
     * ��������ͼ���View 
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
		headerLayout.setHandyOnlyText("·������");
		headerLayout.setIconClickListner(new OnIconClickListner() {
			
			@Override
			public void onClick() {
				LineDetailActivity.this.finish();
			}
		});
		
		headerLayout.init(HeaderStyle.TITLE_FOR_HELP);
		headerLayout.setTextForItem(2, "��Ӧ");
		
		headerLayout.setLayoutLinster(null, null, new MyRightLayoutListner());
		
		initMenu();
//		mLocationClient.start();//��λSDK start֮���Ĭ�Ϸ���һ�ζ�λ���󣬿����������ж�isstart����������request
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
				 * �����Ӧ��Ϊ�˿����
				 * �����·�߷�����Ϊ��������복�����棬ͬʱ����Ϣ���͸�������
				 * �����·�߷�����Ϊ�˿ͽ�ȥ�������棬ͬʱ�ȴ�������Ӧ�����͸����ʵĳ������ó�����Ӧ��
				 * 
				 * �����Ӧ��Ϊ�������
				 * �����·�߷�����Ϊ����������ʾ����
				 * �����·�߷�����Ϊ�˿ͽ���˿ͽ��棬ͬʱ����Ϣ���͸��˿�
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
	        option.setLocationMode(tempMode);//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
	        option.setCoorType(tempcoor);//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ��
	        
	        option.setIsNeedAddress(true);//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
	        option.setOpenGps(true);//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
	        option.setLocationNotify(true);//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
	        option.setIgnoreKillProcess(true);//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
	        option.setEnableSimulateGps(false);//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
	        option.setIsNeedLocationDescribe(true);//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
	        option.setIsNeedLocationPoiList(true);//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
	        mLocationClient.setLocOption(option);
	    }
	
	public void setTipsPopupwindow(){
		tipsPopupwindow = new RegTipsPopupwindow(LineDetailActivity.this);
		tipsPopupwindow.setContext("��Ӧ��·�ߺ��޷�ѡ������·�ߣ��Ƿ���Ӧ��");
		tipsPopupwindow.btnContent(0, "��ȥ����");
		tipsPopupwindow.btnContent(1, "ȷ��");
		
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
								progressDialog = new ProgressDialog(LineDetailActivity.this , "������Ӧ...");
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
								progressDialog = new ProgressDialog(LineDetailActivity.this , "������Ӧ...");
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
						progressDialog = new ProgressDialog(LineDetailActivity.this , "�����...");
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
     * ʵ��ʵʱλ�ûص�����
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
//            if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS��λ���
//                sb.append("\nspeed : ");
//                sb.append(location.getSpeed());// ��λ������ÿСʱ
//                sb.append("\nsatellite : ");
//                sb.append(location.getSatelliteNumber());
//                sb.append("\nheight : ");
//                sb.append(location.getAltitude());// ��λ����
//                sb.append("\ndirection : ");
//                sb.append(location.getDirection());
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                sb.append("\ndescribe : ");
//                sb.append("gps��λ�ɹ�");
//
//            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// ���綨λ���
//                sb.append("\naddr : ");
//                sb.append(location.getAddrStr());
//                //��Ӫ����Ϣ
//                sb.append("\noperationers : ");
//                sb.append(location.getOperators());
//                sb.append("\ndescribe : ");
//                sb.append("���綨λ�ɹ�");
//            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
//                sb.append("\ndescribe : ");
//                sb.append("���߶�λ�ɹ������߶�λ���Ҳ����Ч��");
//            } else if (location.getLocType() == BDLocation.TypeServerError) {
//                sb.append("\ndescribe : ");
//                sb.append("��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��");
//            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
//                sb.append("\ndescribe : ");
//                sb.append("���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��");
//            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
//                sb.append("\ndescribe : ");
//                sb.append("�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�");
//            }
//            sb.append("\nlocationdescribe : ");// λ�����廯��Ϣ
//            sb.append(location.getLocationDescribe());
//            List<Poi> list = location.getPoiList();// POI��Ϣ
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
//            // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360  
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
		//�������յ���Ϣ������tranist search ��˵��������������
        PlanNode stNode = PlanNode.withCityNameAndPlaceName("����", line.getStartAddr());
        PlanNode enNode = PlanNode.withCityNameAndPlaceName("����", "������ͥ");
        PlanNode stNode_1 = PlanNode.withCityNameAndPlaceName("����", "����ɽ");
        PlanNode enNode_1 = PlanNode.withCityNameAndPlaceName("����", "����ɽ");
        PlanNode enNode_2 = PlanNode.withCityNameAndPlaceName("����", line.getEndAddr());
        // ��ʼ������ģ�飬ע���¼�����
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
            Toast.makeText(LineDetailActivity.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //���յ��;�����ַ����壬ͨ�����½ӿڻ�ȡ�����ѯ��Ϣ
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
	
	//����RouteOverly
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
//				// �����Զ���ͼ��  
//                BitmapDescriptor mCurrentMarker = getTerminalMarker();
//                
//                overlayOptions = new MarkerOptions().position(ll)  
//                        .icon(mCurrentMarker);  
//                baiduMap.addOverlay(overlayOptions); 
//			}
//		}
        
        
     
    }

}
