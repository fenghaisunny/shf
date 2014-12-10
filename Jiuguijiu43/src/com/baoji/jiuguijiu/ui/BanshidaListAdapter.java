package com.baoji.jiuguijiu.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.image.ImageLoaderConfig;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BanshidaListAdapter extends BaseAdapter {

	public static ImageView img2;
	private List<TableItem> lists;
	private Context context;

	public BanshidaListAdapter(Context context, List<TableItem> lists) {
		this.context = context;
		this.lists = lists;
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView != null) {
			view = convertView;
		} else {
			view = LayoutInflater.from(context).inflate(R.layout.banshida_item,
					null);
		}
		TextView name = (TextView) view.findViewById(R.id.item_title);// 标题
		TextView content = (TextView) view.findViewById(R.id.item_content);// 内容
		ImageView img1 = (ImageView) view.findViewById(R.id.item_img1);// 图片
		img2 = (ImageView) view.findViewById(R.id.item_img2);

		switch (position) {
		case 0:
			view.setBackgroundResource(R.drawable.folklist_red_selector);
			break;
		case 1:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			break;
		case 2:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			break;
		case 3:
			view.setBackgroundResource(R.drawable.folklist_blue_selector);
			break;
		case 4:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			break;
		case 5:
			view.setBackgroundResource(R.drawable.folklist_red_selector);
			break;
		}
		name.setText(lists.get(position).getTableTitle());
		content.setText(lists.get(position).getTableText());
		img1.setImageBitmap(lists.get(position).getTablePic1());

		int screenWidth = CategoryActivity.screenWidth;
		int screenHeight = CategoryActivity.screenHeight;

		if (!lists.get(position).getTableUrl().equals("")) {
			LayoutParams params = (LayoutParams) img2.getLayoutParams();
			params.width = screenWidth / 5;
			params.height = screenWidth / 5;
			img2.setLayoutParams(params);
			ImageLoader.getInstance().displayImage(
					lists.get(position).getTableUrl(), img2);
		}
		System.out.println(lists.get(position).getTableUrl());
		System.out.println(lists.get(position).getTableUrl().equals(""));
		return view;
	}

}
