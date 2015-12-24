package com.android.together.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class PhoneInfo {

	
	private TelephonyManager telephonyManager;
	/** 
     * �����ƶ��û�ʶ���� 
     */  
    private String IMSI;  
	private Context context;  
   
	public PhoneInfo(Context context) {  
        this.context=context;  
        telephonyManager = (TelephonyManager) context  
                .getSystemService(Context.TELEPHONY_SERVICE);  
    }  

	/** 
     * ��ȡ�绰���� 
     */  
    public String getNativePhoneNumber() {  
        String NativePhoneNumber=null;  
        NativePhoneNumber=telephonyManager.getLine1Number();  
        return NativePhoneNumber;  
    }
    
    /** 
     * ��ȡ�ֻ���������Ϣ 
     */  
    public String getProvidersName() {  
        String ProvidersName = "N/A";  
        try{  
	        IMSI = telephonyManager.getSubscriberId();  
	        // IMSI��ǰ��3λ460�ǹ��ң������ź���2λ00 02���й��ƶ���01���й���ͨ��03���й����š�  
	        System.out.println(IMSI);  
	        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {  
	            ProvidersName = "�й��ƶ�";  
	        } else if (IMSI.startsWith("46001")) {  
	            ProvidersName = "�й���ͨ";  
	        } else if (IMSI.startsWith("46003")) {  
	            ProvidersName = "�й�����";  
	        }  
	    }catch(Exception e){  
	            e.printStackTrace();  
	    }  
        return ProvidersName;  
    }  
    
    public String getPhoneInfo(){  
    	TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);  
        StringBuilder sb = new StringBuilder();  
 
        sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());  
        sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());  
        sb.append("\nLine1Number = " + tm.getLine1Number());  
        sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());  
        sb.append("\nNetworkOperator = " + tm.getNetworkOperator());  
        sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());  
        sb.append("\nNetworkType = " + tm.getNetworkType());  
        sb.append("\nPhoneType = " + tm.getPhoneType());  
        sb.append("\nSimCountryIso = " + tm.getSimCountryIso());  
        sb.append("\nSimOperator = " + tm.getSimOperator());  
        sb.append("\nSimOperatorName = " + tm.getSimOperatorName());  
        sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());  
        sb.append("\nSimState = " + tm.getSimState());  
        sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());  
        sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());  
        return  sb.toString();  
   }  
    
   public TelephonyManager getTelephonyManager(){
	   return  (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE); 
   }
	
}



