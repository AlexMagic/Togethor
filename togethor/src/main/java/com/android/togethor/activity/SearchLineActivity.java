package com.android.togethor.activity;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.android.entity.Line;
import com.android.together.BaseActivity;
import com.android.together.R;
import com.android.together.adapter.NearLineAdapter;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.popupwindow.AddrSearchPopupWindow;
import com.android.together.view.HeaderLayout;
import com.android.together.view.HeaderLayout.OnIconClickListner;
import com.android.together.view.MyRefreshListView;


@ContentView(value = R.layout.search_line)
public class SearchLineActivity extends BaseActivity {

	@ViewInject(R.id.btn_search)
	private Button btn_search;
	
	@ViewInject(R.id.tv_start_addr)
	private TextView tv_start_addr;
	
	@ViewInject(R.id.tv_end_addr)
	private TextView tv_end_addr;
	
	@ViewInject(R.id.layout_start)
	private LinearLayout layout_start;

	@ViewInject(R.id.layout_end)
	private LinearLayout layout_end;
	
	@ViewInject(R.id.layout_searching)
	private LinearLayout layout_searching;
	
	@ViewInject(R.id.common_header)
	private HeaderLayout headerLayout;
	
	@ViewInject(R.id.listview)
	private MyRefreshListView listView;

	
	private List<Line> line_list;
	private NearLineAdapter adapter;

	
	private AddrSearchPopupWindow addrSearchPopupWindow;
	
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		headerLayout.setIcon(R.drawable.bg_btn_header_back);
		headerLayout.setHandyOnlyText("查找路线");
		
		headerLayout.setIconClickListner(new OnIconClickListner() {
			
			@Override
			public void onClick() {
				SearchLineActivity.this.finish();
			}
		});
	}
	
	@Override
	public void setListner() {
		layout_start.setOnClickListener(this);
		layout_end.setOnClickListener(this);
		btn_search.setOnClickListener(this);
	}
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		addrSearchPopupWindow = new AddrSearchPopupWindow(SearchLineActivity.this);
		addrSearchPopupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				SearchLineActivity.this.setBgAlpha(null);
				String str_addr = addrSearchPopupWindow.getStr_addr();
				if(str_addr!=null){
					if(addrSearchPopupWindow.isStart_or_end()){
						tv_start_addr.setText(str_addr);
					}else{
						tv_end_addr.setText(str_addr);
					}
				}else{
					if(addrSearchPopupWindow.isStart_or_end()){
						tv_start_addr.setHint("出发地点");
					}else{
						tv_end_addr.setHint("目的地点");
					}
				}
			}
		});
		switch(view.getId()){
		case R.id.btn_search:
			
			break;
		case R.id.layout_start:
			addrSearchPopupWindow.showViewTopCenter(LayoutInflater.from(SearchLineActivity.this).inflate(R.layout.publish_line, null));
			addrSearchPopupWindow.setStart_or_end(true);
			
			SearchLineActivity.this.setBgAlpha(addrSearchPopupWindow);
			break;
		case R.id.layout_end:
			addrSearchPopupWindow.showViewTopCenter(LayoutInflater.from(SearchLineActivity.this).inflate(R.layout.publish_line, null));
			addrSearchPopupWindow.setStart_or_end(false);
			
			SearchLineActivity.this.setBgAlpha(addrSearchPopupWindow);
			break;
		case R.id.layout_search:
			layout_searching.setVisibility(View.VISIBLE);
			
			break;
			
		}
	}

}
