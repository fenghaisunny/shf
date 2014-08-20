package com.thoughtworks.showlistview.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.thoughtworks.showlistview.MainActivity;
import com.thoughtworks.showlistview.R;
import com.thoughtworks.showlistview.util.ImageLoaderUtils;
import com.thoughtworks.showlistview.view.TableItem;

public class ListAdapter extends BaseAdapter {

	public static ImageView img2;
	private List<TableItem> lists;
	private Context context;

	public ListAdapter(Context context, List<TableItem> lists) {
		this.context = context;
		this.lists = lists;
		ImageLoaderUtils.imageLoderIni(context);
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
			view = LayoutInflater.from(context).inflate(R.layout.pulldown_item,
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
		

		int screenWidth = MainActivity.screenWidth;
		int screenHeight = MainActivity.screenHeight;

		if(lists.get(position).getTableImageUrl()!=null&&lists.get(position).getTableImageUrl().length()>4){
			LayoutParams params = (LayoutParams) img2.getLayoutParams();
			params.width = screenWidth/5;
			params.height = screenWidth/5;
			img2.setLayoutParams(params);
			ImageLoaderUtils.imageLoader.displayImage(lists.get(position)
					.getTableImageUrl(), img2, ImageLoaderUtils.options);
		}else img2.setVisibility(View.GONE);
		System.out.println(lists.get(position).getTableImageUrl().equals(""));
		return view;
	}

}
