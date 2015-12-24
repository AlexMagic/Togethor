package com.android.together.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.android.together.common.adapter.CommAdapter;
import com.android.together.common.adapter.ViewHolder;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;

public class SuggestionAdapter extends CommAdapter<SuggestionInfo> {

	public SuggestionAdapter(Context context, List<SuggestionInfo> mDatas,
			int layoutId) {
		super(context, mDatas, layoutId);
		
	}

	@Override
	public void convert(ViewHolder holder) {
		int curPos = holder.getPosition();
		SuggestionInfo info = mDatas.get(curPos);
		
		TextView tv = holder.setTextView(android.R.id.text1, info.key);
		
	}

}
