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

public class ProductListAdapter extends BaseAdapter {

	public static ImageView img2;
	private List<ProductListTableItem> lists;
	private Context context;

	public ProductListAdapter(Context context, List<ProductListTableItem> lists) {
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
			view = LayoutInflater.from(context).inflate(R.layout.product_item,
					null);
		}
		TextView name = (TextView) view.findViewById(R.id.item_title);// 鏍囬
		TextView pricemarket = (TextView) view.findViewById(R.id.pricemarket);// 鍐呭
		TextView priceshop = (TextView) view.findViewById(R.id.priceshop);// 鍐呭

		ImageView img1 = (ImageView) view.findViewById(R.id.item_img1);// 鍥剧墖
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
		name.setText(lists.get(position).getTitle());
		pricemarket.setText("零售价：￥" + lists.get(position).getPrice_market());
		if (lists.get(position).getPrice_huiyuan().equals("0")) {
			priceshop.setText("折扣价：￥"
					+ lists.get(position).getPrice_jiukuaisong());
		} else
			priceshop.setText("会员价：￥" + lists.get(position).getPrice_huiyuan());

		int screenWidth = HomeActivity.screenWidth;
		int screenHeight = HomeActivity.screenHeight;

		if (!lists.get(position).getImageUrls().equals("")) {
			LayoutParams params = (LayoutParams) img2.getLayoutParams();
			params.width = screenWidth / 4 + 20;
			params.height = screenWidth / 4 + 20;
			img2.setLayoutParams(params);
			ImageLoader.getInstance().displayImage(
					lists.get(position).getImageUrls()[0], img2);
		}
		System.out.println(lists.get(position).getImageUrls().equals(""));
		return view;
	}

}
