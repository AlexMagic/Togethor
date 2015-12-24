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
	
	private boolean isOnce = true; //�Ƿ�ֻ��λһ��
	
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
	
	/**
     * ʵ��ʵʱλ�ûص�����
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            
//            MyLocationData locData = new MyLocationData.Builder()  
//            .accuracy(location.getRadius())
//            // �˴����ÿ����߻�ȡ���ķ�����Ϣ��˳ʱ��0-360  
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
