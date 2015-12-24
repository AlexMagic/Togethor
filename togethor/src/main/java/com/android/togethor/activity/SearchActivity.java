package com.android.togethor.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.together.BaseActivity;
import com.android.together.R;
import com.android.together.adapter.SuggestionAdapter;
import com.android.together.annotation.ContentView;
import com.android.together.annotation.ViewInject;
import com.android.together.popupwindow.RegTipsPopupwindow;
import com.android.together.util.LocationUtil;
import com.android.together.util.LocationUtil.PointCallback;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;


@ContentView(value = R.layout.activity_sel_addr)
public class SearchActivity extends BaseActivity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener
,OnGetGeoCoderResultListener{

	@ViewInject(R.id.layout_searching)
	private LinearLayout waiting_layout;
	
	@ViewInject(R.id.listview)
	private ListView listView;
	
	@ViewInject(R.id.btn_sel)
	private Button btn_sel;
	
	@ViewInject(R.id.et_addr)
	private EditText et_addr;
	
	@ViewInject(R.id.btn_del)
	private Button btn_del;
	
	@ViewInject(R.id.tv_mine)
	private TextView tv_mine;
	
	@ViewInject(R.id.bar)
	private ProgressBar bar;
	
	private String str_addr;
	private String point;
	
	private PoiSearch mPoiSearch = null;
	private SuggestionSearch mSuggestionSearch = null;
	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	//private ArrayAdapter<String> sugAdapter = null;
	
	private SuggestionAdapter adapter = null;
	private ArrayList<SuggestionInfo> mData;
	
	private RegTipsPopupwindow tipsPopupwindow;
	private Intent intent = new Intent();
	@Override
	public void setListner() {
		
		btn_del.setOnClickListener(this);
		
		et_addr.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				str_addr = s.toString();
				if(str_addr!=null && !str_addr.equals("")){
					btn_del.setVisibility(View.VISIBLE);
					//通过百度地图查找附近的地址列表
					/**
					 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
					 */
					
					waiting_layout.setVisibility(View.VISIBLE);
					
					mSuggestionSearch
					.requestSuggestion((new SuggestionSearchOption())
							.keyword(str_addr).city("深圳"));
				}else{
					btn_sel.setVisibility(View.GONE);
					btn_del.setVisibility(View.GONE);
				}
				
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				SuggestionInfo info = (SuggestionInfo) adapter.getItem(position);
				intent.putExtra("addr", info.key);
				// Geo搜索
				mSearch.geocode(new GeoCodeOption().city(
						"深圳").address(info.key));
			}
		});
	}
	
	@Override
	public void init() {
		
		mData = new ArrayList<SuggestionResult.SuggestionInfo>();
		
		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
		
		initMinePos();
		initPoi();
		
		if(str_addr!=null){
			et_addr.setText(str_addr);
			btn_del.setVisibility(View.VISIBLE);
		}
		
		
	}
	public void initMinePos(){
		LocationUtil l = LocationUtil.getInstance(this);
		l.setCallBack(new PointCallback() {
			
			@Override
			public void callback(BDLocation location) {
				bar.setVisibility(View.GONE);
				tv_mine.setText(location.getLocationDescribe());
			}
		});
		
		l.getmLocationClient().start();
	}
	
	public void initPoi(){
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSuggestionSearch = SuggestionSearch.newInstance();
		mSuggestionSearch.setOnGetSuggestionResultListener(this);
		
	}
	
	@Override
	public void setOnClickListner(View view) {
		// TODO Auto-generated method stub
		super.setOnClickListner(view);
		
		switch(view.getId()){
		case R.id.btn_sel:
			
			break;
			
		case R.id.btn_del:
			et_addr.setText(null);
			break;
			
		}
		
	}
	
	public void setTipsPopupwindow(){
		tipsPopupwindow = new RegTipsPopupwindow(SearchActivity.this);
		tipsPopupwindow.setContext("无法找到相关地点，请返回后重新输入");
		tipsPopupwindow.btnContent(0, "返回");
		tipsPopupwindow.setContentVisible(1);
		
		
		
		tipsPopupwindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				SearchActivity.this.setBgAlpha(null);
			}
		});
		
		SearchActivity.this.setBgAlpha(tipsPopupwindow);
		tipsPopupwindow.showViewCenter(tipsPopupwindow.getView());
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			waiting_layout.setVisibility(View.GONE);
			//弹出提示窗口,重新输入
			//sugAdapter.clear();
			mData.clear();
			setTipsPopupwindow();
			return;
		}
		waiting_layout.setVisibility(View.GONE);
		//sugAdapter.clear();
		mData.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null){
				
				//System.out.println(info.pt.latitude+" --- "+info.pt.longitude);
				mData.add(info);
			}
		}
		if(adapter==null){
			adapter = new SuggestionAdapter(SearchActivity.this, mData, android.R.layout.simple_dropdown_item_1line);
			listView.setAdapter(adapter);
		}
		listView.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();
		
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(SearchActivity.this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(SearchActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
					.show();
			return;
		}
		point = result.getLocation().latitude+","+result.getLocation().longitude;
		intent.putExtra("point", point);
		SearchActivity.this.setResult(RESULT_OK, intent);
		SearchActivity.this.finish();
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		// TODO Auto-generated method stub
		
	}

}
