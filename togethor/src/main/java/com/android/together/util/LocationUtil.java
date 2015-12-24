package com.android.together.util;

import java.util.List;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

public class LocationUtil {
	
	private Context context;
	
	public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;
    
    private static double Lat;
	private static double Lng;
	
	private String position;
	
	private static LocationUtil instance;
	
	private PointCallback callback;

	
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	 
	private String tempcoor="gcj02";
	
	private boolean isOnce = true; //是否只定位一次
	
	public LocationUtil(Context context) {
		mLocationClient = new LocationClient(context);
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
		initLocation();
	}

	public static LocationUtil getInstance(Context context){
		if(instance==null)
			return new LocationUtil(context);
		
		return instance;
	}
	
	
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
	
	/**
     * 实现实时位置回调监听
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            
//            MyLocationData locData = new MyLocationData.Builder()  
//            .accuracy(location.getRadius())
//            // 此处设置开发者获取到的方向信息，顺时针0-360  
//            .direction(100).latitude(location.getLatitude())  
//            .longitude(location.getLongitude()).build();
//            
//            LatLng ll = new LatLng(location.getLatitude(),  location.getLongitude());
        	System.out.println("location");
            if(callback!=null){
            	callback.callback(location);
            }
            
            if(isOnce){
            	mLocationClient.stop();
            }
            
        }
    }
    
    
    
    public LocationClient getmLocationClient() {
		return mLocationClient;
	}

	
	public boolean isOnce() {
		return isOnce;
	}

	public void setOnce(boolean isOnce) {
		this.isOnce = isOnce;
	}

	public void setCallBack(PointCallback callback){
		this.callback = callback;
	}
	
	public PointCallback getCallback(){
		return callback;
	}
	
	public interface PointCallback{
		public void callback(BDLocation location);
	}
	
}
