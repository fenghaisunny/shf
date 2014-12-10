package com.baoji.jiuguijiu.ui;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoji.jiuguijiu.R;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class GridViewAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<GridItem> gridItemList;

	public GridViewAdapter(String[] titles, String[] imageUrls, String[] description,
			Context context) {
		super();
		gridItemList = new ArrayList<GridItem>();
		inflater = LayoutInflater.from(context);
		for (int i = 0; i < imageUrls.length; i++) {
			GridItem picture = new GridItem(titles[i], imageUrls[i],
					description[i]);
			gridItemList.add(picture);
		}
	}

	@Override
	public int getCount() {
		if (null != gridItemList) {
			return gridItemList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return gridItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			view = inflater.inflate(R.layout.gridview_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.image);
			TextView title = (TextView) view.findViewById(R.id.title);
			TextView desc = (TextView) view.findViewById(R.id.description);

			title.setText(gridItemList.get(position).getTitle());
			desc.setText(gridItemList.get(position).getDescription());
			ImageLoaderUtils.imageLoader.displayImage(gridItemList
					.get(position).getImageUrl(), image,
					ImageLoaderUtils.options);
		} else {
			view = convertView;
		}
		return view;
	}
}