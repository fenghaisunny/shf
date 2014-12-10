package com.baoji.jiuguijiu.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.task.Callback;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.AnimCommon;

public class CategoryActivity extends BaseActivity implements Callback,
		OnClickListener, OnItemClickListener {

	
	private SharedPreferences loginSettingsp;
	private SharedPreferences userInfosp;
	private ListView catergory_listview;
	private LayoutInflater layoutInflater;
	private int backConut = 0;
	public static int screenWidth, screenHeight;
	private ListView mListView;
	private String[] changshititle = { "白酒", "葡萄酒", "洋酒", "啤酒", "酒具/酒柜" };
	private Intent showCategory;
	private String[] requestUrls = {
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=2&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=1&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=3&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=4&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?pgravessel=1&type=prolist&pageindex="
			 };

	private CategoryListAdapter mAdapter;
	List<CategoryTableItem> strings = new ArrayList<CategoryTableItem>();
	private List<CategoryTableItem> posters = new ArrayList<CategoryTableItem>();
	private List<CategoryTableItem> mStrings = new ArrayList<CategoryTableItem>();// 鏇存柊鍒楄〃鐨勬暟鎹泦鍚?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_category);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		mListView = (ListView) this.findViewById(R.id.catergory_listview);
		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);
		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mListView.setOnItemClickListener(this);
		mListView.setPadding(10, 10, 10, 10);
		mAdapter = new CategoryListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);
		loadListViewData(changshititle);
	}

	public void loadListViewData(String listtitles[]) {

		for (int i = 0; i < listtitles.length; i++) {
			CategoryTableItem ti = new CategoryTableItem();
			// ti.setTableText((String[]) imageurls.get(i));
			ti.setTableTitle(listtitles[i]);
			ti.setCommentsUrl(requestUrls[i]);
			ti.setTableId(null);
			ti.setTablePic1(null);
			ti.setTablePic2(null);
			posters.add(ti);
		}
		for (CategoryTableItem body : posters) {
			strings.add(body);
		}
		if (!strings.isEmpty()) {
			mStrings.addAll(strings);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 0:
			showCategory = new Intent(CategoryActivity.this,
					CategoryDetail.class);
			showCategory.putExtra("title", "白酒");
			showCategory.putExtra("requestUrl", requestUrls[0]);
			this.startActivity(showCategory);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case 1:
			showCategory = new Intent(CategoryActivity.this,
					CategoryDetail.class);
			showCategory.putExtra("title", "葡萄酒");
			showCategory.putExtra("requestUrl", requestUrls[1]);
			this.startActivity(showCategory);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case 2:
			showCategory = new Intent(CategoryActivity.this,
					CategoryDetail.class);
			showCategory.putExtra("title", "洋酒");
			showCategory.putExtra("requestUrl", requestUrls[2]);
			this.startActivity(showCategory);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case 3:
			showCategory = new Intent(CategoryActivity.this,
					CategoryDetail.class);
			showCategory.putExtra("title", "啤酒");
			showCategory.putExtra("requestUrl", requestUrls[3]);
			this.startActivity(showCategory);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case 4:
			showCategory = new Intent(CategoryActivity.this,
					CategoryDetail.class);
			showCategory.putExtra("title", "酒具酒柜");
			showCategory.putExtra("requestUrl", requestUrls[4]);
			this.startActivity(showCategory);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		
	}

	@Override
	public void onCallback(Object pCallbackValue) {
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			backConut++;
			if (backConut == 1) {
				Toast.makeText(this, "再按一次退出“酒快送”", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						backConut = 0;
					}
				}, 4000);
			} else if (backConut == 2) {
				if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
					userInfosp.edit().clear().commit();
				}
				AppManager.getInstance().AppExit(this);
				backConut = 0;
			}
			break;
		}
		return true;
	}
}
