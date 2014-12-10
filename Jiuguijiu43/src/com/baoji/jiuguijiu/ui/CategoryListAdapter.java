package com.baoji.jiuguijiu.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoji.jiuguijiu.R;

public class CategoryListAdapter extends BaseAdapter {

	public static ImageView img2, img1;
	private List<CategoryTableItem> lists;
	private Context context;
/*	private String list_icons[] = { "drawable://" + R.drawable.item_baijiu,
			"drawable://" + R.drawable.item_putaojiu,
			"drawable://" + R.drawable.item_yangjiu,
			"drawable://" + R.drawable.icon_pijiu,
			"drawable://" + R.drawable.item_jiuju };
*/
	public CategoryListAdapter(Context context, List<CategoryTableItem> lists) {
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
			view = LayoutInflater.from(context).inflate(R.layout.category_item,
					null);
		}
		TextView name = (TextView) view.findViewById(R.id.item_title);// 标题

		img1 = (ImageView) view.findViewById(R.id.item_img1);
		switch (position) {
		case 0:
			view.setBackgroundResource(R.drawable.folklist_red_selector);
			img1.setImageResource(R.drawable.home_baijiu);
			break;
		case 1:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			img1.setImageResource(R.drawable.home_putaojiu);
			break;
		case 2:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			img1.setImageResource(R.drawable.home_yangjiu);
			break;
		case 3:
			view.setBackgroundResource(R.drawable.folklist_blue_selector);
			img1.setImageResource(R.drawable.pijiu02);
			break;
		case 4:
			view.setBackgroundResource(R.drawable.folklist_orange_selector);
			img1.setImageResource(R.drawable.home_jiuju);
			break;
		case 5:
			view.setBackgroundResource(R.drawable.folklist_red_selector);
			img1.setImageResource(R.drawable.home_baijiu);
			break;
		}
		name.setText(lists.get(position).getTableTitle());
		return view;
	}

}
