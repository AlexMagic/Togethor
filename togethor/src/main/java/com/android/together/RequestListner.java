package com.android.together;


@SuppressWarnings("hiding")
public abstract class RequestListner<RequestBean> {
	public abstract void onStart();
	public abstract void onComplete(String json);
}
