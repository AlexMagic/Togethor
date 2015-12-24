package com.android.together;

//import com.android.zns_pad.view.HeaderLayout.OnIconClickListner;

import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.android.together.util.InvalidateUtil;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public abstract class BaseFragment extends Fragment {
	
	protected Context context;
	protected int layout_id;
	protected static View mView;
	private MyApplication myApplication;
	
	private InvalidateUtil invalidateUtil;
	
	protected RequestQueue mRequestQueue;  
	
	public BaseFragment(){
		super();
	}
	
	public BaseFragment(Context context , int layout_id){
		super();
		this.context = context;
		this.layout_id = layout_id;
		invalidateUtil = new InvalidateUtil();
		
		mRequestQueue =  Volley.newRequestQueue(context);  
		
		System.out.println("new fragment");
	}
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		System.out.println("onCreateView fragment");

		mView = inflater.inflate(layout_id, null);
		
		setUpView();
		setUpListner();
//		setUpData();
		return mView;
	}
	
	
	protected abstract void setUpData();

	protected abstract void setUpListner();

	protected abstract void setUpView();
	
	public View findViewById(int id) {
		return mView.findViewById(id);
	}
	
	
	public  void respone(String response){};
	public  void error(){};
	
	public void string_request(String url , int method , final Map<String, String> map){
		StringRequest stringRequest = new StringRequest(method, url, new Listener<String>() {

			@Override
			public void onResponse(String response) {
				System.out.println("h");
				Log.d("val", "response -> " + response);
				respone(response);
			}
		}, new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				 Log.d("phone_va_ssl", "error -> " + error.getMessage());
				error();
			}
		}){

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				// TODO Auto-generated method stub
				if(map!=null)
					return map;
				return super.getParams();
			}
			
		};
		
		mRequestQueue.add(stringRequest);
		mRequestQueue.start();
	}
	
	//Òþ²ØÐéÄâ¼üÅÌ
    public static void HideKeyboard()
    {
        InputMethodManager imm = ( InputMethodManager ) mView.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );     
      if ( imm.isActive( ) ) {     
          imm.hideSoftInputFromWindow(mView.getApplicationWindowToken( ) , 0 );   
          
      }    
    }
	
	
}
