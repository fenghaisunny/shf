package com.baoji.jinlinggongshang.pulltorefresh;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import com.baoji.jinlinggongshang.R;

public class TabAdapter extends BaseAdapter {
	private Context mContext;
	private int mSelectedTab = 0;

	private static String[] item_names;

	public TabAdapter(Context context, String item_names[]) {
		this.mContext = context;
		this.item_names = item_names;
	}

	/*
	 * 设置选中的Tab，并且刷新界面
	 */
	public void setSelectedTab(int tab) {
		if (tab != mSelectedTab) {
			mSelectedTab = tab;
			notifyDataSetChanged();
		}
	}

	public int getSelectedTab() {
		return mSelectedTab;
	}

	public int getCount() {
		return Integer.MAX_VALUE;// 返回最大值
	}

	public Object getItem(int position) {
		return item_names[position];
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		TextView text = null;// 这里只放一个TextView，可以根据需要来定制
		if (convertView == null) {
			text = new TextView(mContext);
		} else {
			text = (TextView) convertView;
		}

		text.setText(item_names[position % item_names.length]);// 循环取余设置显示内容

		text.setLayoutParams(new Gallery.LayoutParams(
				(MainTabActivity.screenWidth) / 4,
				(MainTabActivity.screenHeight) / 12));
		text.setGravity(Gravity.CENTER);


		
		/*
		 * 对于选中的Tab，给他一个选中的背景
		 */
		if (position == mSelectedTab) {
			text.setBackgroundColor(text.getResources().getColor(android.R.color.white));
			text.setTextColor(text.getResources().getColor(R.color.topbar_red));
		}else {
			text.setBackgroundColor(text.getResources().getColor(R.color.topbar_red));
			text.setTextColor(text.getResources().getColor(android.R.color.white));
		}
		return text;
	}
}