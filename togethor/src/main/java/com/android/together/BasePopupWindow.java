package com.android.together;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public abstract class BasePopupWindow extends PopupWindow {

	protected View mContentView;
	protected onSubmitClickListener mOnSubmitClickListener;
	protected Context context;

	public BasePopupWindow() {
		super();
	}

	public BasePopupWindow(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	public BasePopupWindow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BasePopupWindow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BasePopupWindow(Context context) {
		super(context);
	}

	public BasePopupWindow(int width, int height) {
		super(width, height);
	}

	public BasePopupWindow(View contentView, int width, int height,
			boolean focusable) {
		super(contentView, width, height, focusable);
	}

	public BasePopupWindow(View contentView) {
		super(contentView);
	}

	@SuppressWarnings("deprecation")
	public BasePopupWindow(View contentView, int width, int height , Context context) {
		super(contentView, width, height, true);
		mContentView = contentView;
		this.context = context;
		setBackgroundDrawable(new BitmapDrawable());
		initViews();
		initEvents();
		init();
	}

	public abstract void initViews();

	public abstract void initEvents();

	public abstract void init();

	public View findViewById(int id) {
		return mContentView.findViewById(id);
	}

	/**
	 * ��ʾ��parent���ϲ���ˮƽ����
	 * 
	 * @param parent
	 */
	public void showViewTopCenter(View parent) {
		showAtLocation(parent, Gravity.TOP | Gravity.CENTER, 0, 40);
	}

	/**
	 * ��ʾ��parent������
	 * 
	 * @param parent
	 */
	public void showViewCenter(View parent) {
		showAtLocation(parent, Gravity.CENTER, 0, 0);
	}

	/**
	 * ���ȷ�ϵ�������
	 * 
	 * @param l
	 */
	public void setOnSubmitClickListener(onSubmitClickListener l) {
		mOnSubmitClickListener = l;
	}

	public interface onSubmitClickListener {
		void onClick();
	}

	
	public View getView(){
		return mContentView;
	}
	
	
}
