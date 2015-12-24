package com.android.together.common.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHolder {
	
	private final SparseArray<View> mViews;  
    private int mPosition;  
    private View mConvertView;  
    
    private ViewHolder(Context context, ViewGroup parent, int layoutId,  
            int position) {  
        this.mPosition = position;  
        this.mViews = new SparseArray<View>();  
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,  
                false);  
        // setTag  
        mConvertView.setTag(this);  
    }  
    
    /**
     * 获取ViewHolder实例
     */
     public static ViewHolder getViewHolder(Context context,View convertView,ViewGroup parent, int layoutId,  
             int position ){
    	 //if(convertView == null){
    		 return new ViewHolder(context  , parent , layoutId , position);
    	 //}
    	 
    	 //return (ViewHolder) convertView.getTag();
     }
     
     public View getConvertView(){
    	 return mConvertView;
     }
     
     /**
      * 获取控件
      */
     @SuppressWarnings("unchecked")
	public <T extends View> T getView(int resid){
    	 View view = mViews.get(resid);
    	 
    	 if(view==null){
    		 view = mConvertView.findViewById(resid);
    		 mViews.put(resid, view);
    	 }
    	 
    	 return (T) view;
     }
     
     public TextView setTextView(int resid , String str){
    	 TextView tv = getView(resid);
    	 tv.setText(str);
    	 return tv;
     }
     
     public ImageView setIv(int resid , int srcid){
    	 ImageView iv = getView(resid);
    	
//    	 iv.setImageDrawable( getResources().getDrawable());
    	 return iv;
     }
     
     
     
     public int getPosition(){
    	return mPosition; 
     }
    
}
