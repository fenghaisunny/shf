package com.baoji.jiuguijiu.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.utils.NetworkUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShaixuanActivity extends ExpandableListActivity implements
		OnClickListener {

	List<String> group; // 组列表
	List<List<String>> child; // 子列表
	ContactsInfoAdapter adapter; // 数据适配器
	private Button back;
	private String keyname, keyvalue;
	private String category;
	private String winetype;

	private String[] priceList = { "1-99", "100-299", "300-599", "600-999",
			"1000-1999", "2000以上" };

	private String[] brandList /* = { "茅台", "五粮液", "西凤", "泸州老窖", "红星", "牛栏山" } */;

	private String[] originList /* = { "四川", "陕西", "贵州","山西","湖北","江苏" } */;

	private String[] brandListTemp = { "茅台", "五粮液", "西凤", "泸州老窖", "红星", "牛栏山" };

	private String[] originListTemp = { "四川", "陕西", "贵州", "山西", "湖北", "江苏" };

	private String url = "http://www.jiukuaisong.cn/api/Pbrandoriginlist.ashx?type=pbrandoriginlist&listkey=getpbrandoriginlist";

	private Boolean isNetAvailable;
	private String type;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置为无标题
		setContentView(R.layout.shaixuan);

		isNetAvailable = NetworkUtils.isNetworkAvailable(this);
		if (isNetAvailable) {
			getbrandoriginHandler.sendEmptyMessageDelayed(0, 0);
		} else {
			brandList = brandListTemp;
			originList = originListTemp;
			initializeData();
			getExpandableListView().setAdapter(new ContactsInfoAdapter());
		}

		category = getIntent().getStringExtra("winetype");

		if (category.equals("白酒")) {
			winetype = "winetype=2";
			type = "&winetype=2";
		} else if (category.equals("葡萄酒")) {
			winetype = "winetype=1";
			type = "&winetype=1";
		} else if (category.equals("洋酒")) {
			winetype = "winetype=3";
			type = "&winetype=3";
		} else if (category.equals("啤酒")) {
			winetype = "winetype=4";
			type = "&winetype=4";
		} else if (category.equals("酒具酒柜")) {
			winetype = "pgravessel=1";
			type = "&winetype=6";
		} else if (category.equals("半时达")) {
			winetype = "halftype=1";
			type = "&winetype=5";
		}
		getExpandableListView().setOnChildClickListener(
				new OnChildClickListener() {
					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						Intent shaixuanDetail = new Intent(
								ShaixuanActivity.this, ShaixuanDetail.class);
						System.out.println(groupPosition);
						System.out.println(childPosition);
						switch (groupPosition) {
						// 以嵌套的形式依次获取各个父选项和其子选项的位置关系
						case 0:
							keyname = "brand";
							keyvalue = brandList[childPosition];
							shaixuanDetail.putExtra("winetype", winetype);
							shaixuanDetail.putExtra("keyname", keyname);
							shaixuanDetail.putExtra("keyvalue", keyvalue);
							ShaixuanActivity.this.startActivity(shaixuanDetail);
							overridePendingTransition(R.anim.push_down_in,
									R.anim.push_down_out);
							break;
						case 1:
							keyname = "origin";
							keyvalue = originList[childPosition];
							shaixuanDetail.putExtra("winetype", winetype);
							shaixuanDetail.putExtra("keyname", keyname);
							shaixuanDetail.putExtra("keyvalue", keyvalue);
							ShaixuanActivity.this.startActivity(shaixuanDetail);
							overridePendingTransition(R.anim.push_down_in,
									R.anim.push_down_out);
							System.out.println(winetype);
							System.out.println(keyname);
							System.out.println(keyvalue);
							break;
						case 2:
							keyname = "price";
							keyvalue = priceList[childPosition];
							shaixuanDetail.putExtra("winetype", winetype);
							shaixuanDetail.putExtra("keyname", keyname);
							shaixuanDetail.putExtra("keyvalue", keyvalue);
							ShaixuanActivity.this.startActivity(shaixuanDetail);
							overridePendingTransition(R.anim.push_down_in,
									R.anim.push_down_out);
							System.out.println(winetype);
							System.out.println(keyname);
							System.out.println(keyvalue);
							break;
						default:
							break;
						}
						return true;
					}
				});
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		getExpandableListView().setCacheColorHint(0); // 设置拖动列表的时候防止出现黑色背景

	}

	/**
	 * 初始化组、子列表数据
	 */
	private void initializeData() {
		group = new ArrayList<String>();
		child = new ArrayList<List<String>>();

		addInfo("按品牌", brandList);
		addInfo("按产地", originList);
		addInfo("按价格", priceList);
	}

	private Handler getbrandoriginHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			try {
				DefaultHttpClient mHttpClient = new DefaultHttpClient();
				HttpPost mPost = new HttpPost(url + type);
				HttpResponse response = mHttpClient.execute(mPost);
				int reponseCode = response.getStatusLine().getStatusCode();
				System.out.println(reponseCode);
				HttpEntity entity = response.getEntity();
				String info = EntityUtils.toString(entity);

				System.out.println(info);	
				JSONObject json = new JSONObject(info);
				String resCode = json.getString("msg");

				JSONArray brandArray = json.getJSONArray("brandlist");
				JSONArray originArray = json.getJSONArray("originlist");

				brandList = new String[brandArray.length()];
				originList = new String[originArray.length()];

				for (int i = 0; i < brandArray.length(); i++) {
					JSONObject js = brandArray.getJSONObject(i);
					String brand = js.getString("blist");
					brandList[i] = brand;
				}

				for (int i = 0; i < originArray.length(); i++) {
					JSONObject js = originArray.getJSONObject(i);
					String origin = js.getString("olist");
					originList[i] = origin;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			initializeData();
			getExpandableListView().setAdapter(new ContactsInfoAdapter());
		}
	};

	/**
	 * 模拟给组、子列表添加数据
	 * 
	 * @param g
	 *            -group
	 * @param c
	 *            -child
	 */
	private void addInfo(String g, String[] c) {
		group.add(g);
		List<String> childitem = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			childitem.add(c[i]);
		}
		child.add(childitem);
	}

	class ContactsInfoAdapter extends BaseExpandableListAdapter {

		// -----------------Child----------------//
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return child.get(groupPosition).size();
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String string = child.get(groupPosition).get(childPosition);
			return getGenericView(string);
		}

		// ----------------Group----------------//
		@Override
		public Object getGroup(int groupPosition) {
			return group.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public int getGroupCount() {
			return group.size();
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			String string = group.get(groupPosition);
			return getGenericView(string);
		}

		// 创建组/子视图
		public TextView getGenericView(String s) {
			// Layout parameters for the ExpandableListView
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.FILL_PARENT, 80);

			TextView text = new TextView(ShaixuanActivity.this);
			text.setLayoutParams(lp);
			// Center the text vertically
			text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			// Set the text starting position
			text.setPadding(100, 10, 0, 0);
			text.setText(s);
			return text;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		default:
			break;
		}
		return true;
	}
}
