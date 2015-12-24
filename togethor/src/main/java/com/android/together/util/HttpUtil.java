package com.android.together.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


/**
 * http工具类
 * @author Administrator
 *
 */
public class HttpUtil {
	// 基础URL  
    // 获得Get请求对象request 
	public static HttpGet getHttpGet(String url){
		HttpGet httpGet = new HttpGet(url);
		return httpGet;
	}
	
	// 获得Post请求对象request  
	public static HttpPost getHttpPost(String url){
		HttpPost httpPost = new HttpPost(url);
		return httpPost;
	}
	
	// 根据请求获得响应对象response  
    public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{  
        HttpResponse response = new DefaultHttpClient().execute(request);  
        return response;  
    }  
   
    // 根据请求获得响应对象response  
    public static HttpResponse getHttpResponse(HttpPost request) throws ClientProtocolException, IOException{  
        HttpResponse response = new DefaultHttpClient().execute(request);  
        return response;  
    }
    
    // 发送Post请求(带参数的)，获得响应查询结果  
    public static String queryStringForPost(String url ,List<BasicNameValuePair> params){  
       
        String result = null;  
        
        try {  
        	
        	// 根据url获得HttpPost对象  
            HttpPost request = HttpUtil.getHttpPost(url);  
//            if(params != null)
//            
//            System.out.println("["+JsonUtil.Map2Json(map)+"]");
            if(params != null){
            	request.setEntity((new UrlEncodedFormEntity(params, "utf-8")));
//            	request.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            }
            
            
            // 获得响应对象  
            HttpResponse response = HttpUtil.getHttpResponse(request);  
            // 判断是否请求成功  
            if(response.getStatusLine().getStatusCode()==200){  
                // 获得响应  
                result = EntityUtils.toString(response.getEntity());  
//                result=new  String(result.getBytes("8859_1"),"utf-8");  //这句可要可不要，以你不出现乱码为准  
                Log.i("main", "resCode = " + response.getStatusLine()); //获取响应码  
                return result;  
            }  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
            result = "-1";  
            return result;  
        } catch (IOException e) {  
            e.printStackTrace();  
            result = "-1";  
            return result;  
        }  
        return null;  
    }  
    
    
	
}
