//package com.android.together;
//
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//import com.android.zns.jsonhelper.AnalysisHelper;
//import com.android.zns.jsonhelper.StorageHelper;
//import com.android.zns.jsonhelper.TrayHelper;
//import com.android.zns.jsonhelper.UrlHelper;
//import com.android.zns.jsonhelper.UserHelper;
//
//
//public class AsyncForData {
//	private Executor mPool;
//	
//	
//	private UserHelper mUserHelper;
//	private AnalysisHelper mAnalysisHelper;
//	private TrayHelper mTrayHelper;
//	private StorageHelper mStorageHelper;
//	private UrlHelper mUrlHelper;
//	
//	public AsyncForData(){
//		mPool = Executors.newFixedThreadPool(5);
//		mAnalysisHelper = new AnalysisHelper();
//		mUserHelper = new UserHelper();
//		mTrayHelper = new TrayHelper();
//		mStorageHelper = new StorageHelper();
//		mUrlHelper = new UrlHelper();
//	}
//	
//	public void getUrl(String request, RequestListner listener){
//		mUrlHelper.getUrl(mPool, request, listener);
//	}
//	
//	public void getAnalysis(String request, RequestListner listener){
//		mAnalysisHelper.getAsyncData(mPool, request, listener);
//	}
//	
//	public void sendAnalysis(String request, RequestListner listener){
//		mAnalysisHelper.sendAnalysisData(mPool, request, listener);
//	}
//	
//	public void getAsynUser(String request , RequestListner listener){
//		mUserHelper.getUser(mPool, request, listener);
//	}
//	
//	public void getMaterialInfo(String request, RequestListner listener){
//		mTrayHelper.getGoodsData(mPool, request, listener);
//	}
//	
//	public void sendTrayInfo(String request, RequestListner listener){
//		mTrayHelper.sendTrayInfo(mPool , request , listener);
//	}
//	
//	public void requestStorage(String request, RequestListner listener){
//		mStorageHelper.getStorage(mPool, request, listener);
//	}
//	
//	public void requestStorages(String request, RequestListner listener){
//		mStorageHelper.getStorages(mPool, request, listener);
//	}
//	
//	public void sendStorage(String request, RequestListner listener){
//		mStorageHelper.sendStorage(mPool, request, listener);
//	}
//	
//	public void sendOutPut(String request, RequestListner listener){
//		mStorageHelper.sendOutPut(mPool, request, listener);
//	}
//}
