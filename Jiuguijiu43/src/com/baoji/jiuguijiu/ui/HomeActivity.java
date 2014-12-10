package com.baoji.jiuguijiu.ui;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.utils.AnimCommon;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends TabActivity {

	public static final String TAG = HomeActivity.class.getSimpleName();

	private SharedPreferences sp;
	private SharedPreferences loginSettingsp;
	private RadioGroup mTabButtonGroup;
	private TabHost mTabHost;
	private int backConut = 0;
	public static int screenWidth, screenHeight;

	public static final String TAB_MAIN = "MAIN_ACTIVITY";
	public static final String TAB_CATEGORY = "CATEGORY_ACTIVITY";
	public static final String TAB_BANSHIDA = "BANSHIDA_ACTIVITY";
	public static final String TAB_PERSONAL = "PERSONAL_ACTIVITY";
	public static final String TAB_MORE = "MORE_ACTIVITY";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActivity(this);
		setContentView(R.layout.activity_home);

		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;

		findViewById();
		initView();
	}

	private void findViewById() {
		mTabButtonGroup = (RadioGroup) findViewById(R.id.home_radio_button_group);

	}

	private void initView() {

		mTabHost = getTabHost();
		Intent i_main = new Intent(this, IndexActivity.class);
		Intent i_banshda = new Intent(this, BanshidaActivity.class);
		Intent i_category = new Intent(this, CategoryActivity.class);
		Intent i_personal = new Intent(this, LoginActivity.class);
		Intent i_more = new Intent(this, MoreActivity.class);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(i_main));

		mTabHost.addTab(mTabHost.newTabSpec(TAB_CATEGORY)
				.setIndicator(TAB_CATEGORY).setContent(i_category));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_BANSHIDA)
				.setIndicator(TAB_BANSHIDA).setContent(i_banshda));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_PERSONAL)
				.setIndicator(TAB_PERSONAL).setContent(i_personal));
		mTabHost.addTab(mTabHost.newTabSpec(TAB_MORE).setIndicator(TAB_MORE)
				.setContent(i_more));

		mTabButtonGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						switch (checkedId) {
						case R.id.home_tab_main:
							mTabHost.setCurrentTabByTag(TAB_MAIN);
							break;
						case R.id.home_tab_category:
							mTabHost.setCurrentTabByTag(TAB_CATEGORY);
							break;
						case R.id.home_tab_banshida:
							mTabHost.setCurrentTabByTag(TAB_BANSHIDA);
							break;
						case R.id.home_tab_personal:
							mTabHost.setCurrentTabByTag(TAB_PERSONAL);
							break;
						case R.id.home_tab_more:
							mTabHost.setCurrentTabByTag(TAB_MORE);
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.menu_about:
			break;
		case R.id.menu_setting:
			break;
		case R.id.menu_history:
			break;
		case R.id.menu_feedback:
			break;
		case R.id.menu_exit:
			showAlertDialog("退出程序", "确定退出酒快送？", "确定", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppManager.getInstance().AppExit(getApplicationContext());
					ImageLoader.getInstance().clearMemoryCache();
				}
			}, "取消", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			break;
		case R.id.menu_help:
			break;
		default:
			break;
		}
		return true;
	}

	/** 含有标题、内容两个按钮的对话框 **/
	protected void showAlertDialog(String title, String message,
			String positiveText,
			DialogInterface.OnClickListener onPositiveClickListener,
			String negativeText,
			DialogInterface.OnClickListener onNegativeClickListener) {
		new AlertDialog.Builder(this).setTitle(title).setMessage(message)
				.setPositiveButton(positiveText, onPositiveClickListener)
				.setNegativeButton(negativeText, onNegativeClickListener)
				.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("Destoryed!");
		if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
			sp = this.getSharedPreferences("userInfo",
					Context.MODE_WORLD_READABLE);
			sp.edit().clear().commit();
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		if (AnimCommon.in != 0 && AnimCommon.out != 0) {
			super.overridePendingTransition(AnimCommon.in, AnimCommon.out);
			AnimCommon.clear();
		}
		System.out.println("Paused!");
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("Stoped!");
	}

}
