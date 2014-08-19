package com.baoji.jinlinggongshang;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.baoji.jinlinggongshang.pulltorefresh.ListAdapter;
import com.baoji.jinlinggongshang.pulltorefresh.PullDownView;
import com.baoji.jinlinggongshang.pulltorefresh.PullDownView.OnPullDownListener;
import com.baoji.jinlinggongshang.pulltorefresh.TableItem;
import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;


public class CultureActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnPullDownListener {

	private static final int WHAT_DID_LOAD_DATA = 0;// 椤甸潰鍒濆鍖栨椂锛屽姞杞芥暟鎹?
	private static final int WHAT_DID_REFRESH = 1;// 涓嬫媺鍒锋柊鏁版嵁
	private static final int WHAT_DID_MORE = 2;// 涓婂垝鍔犺浇鏇村鏁版嵁

	private int currentCount = 8;
	private static final int LOADCOUNT = 4;
	private int jsonSize = 0;
	private String url = "http://192.168.2.6:8080/JsonWeb01/JsonServlet";

	private ListView mListView;// 璧勮鏍忕洰鍒楄〃
	private ListAdapter mAdapter;// 鍒楄〃閫傞厤鍣?

	private PullDownView mPullDownView;// 鑷畾涔変笅鎷夛紝涓婂垝view
	private List<TableItem> mStrings = new ArrayList<TableItem>();// 鏇存柊鍒楄〃鐨勬暟鎹泦鍚?

	private List<TableItem> posters = new ArrayList<TableItem>();// 瑙ｆ瀽鍑虹殑璧勮鏍忕洰闆嗗悎
	private int pageIndex = 0;// 鍔犺浇鐨勯〉闈?
	private Message msg;// 鍚慔andler鍙戦?鐨勬秷鎭?

	public boolean isPlaying_internet = false;
	public boolean isPlaying_local = false;
	public String playdir = "";

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码

	private LockableHorizontalScrollView scrollView;
	private ImageButton exit, back, category, camera_slide, twodimen_slide,
			call_slide, theme;
	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;

	String str = null;
	Date date = null;

	private int screenWidth;
	private boolean isOpen = false;
	private final Handler slideBarHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.culture);
		MyActivityManager.getInstance().addActivity(this);

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setVerticalFadingEdgeEnabled(false);
		mListView = mPullDownView.getListView();
		mListView.setDivider(getResources()
				.getDrawable(R.drawable.whitedivider));
		mListView.setPadding(30, 20, 30, 20);
		mListView.setOnItemClickListener(this);

		mAdapter = new ListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);

		mPullDownView.enableAutoFetchMore(true, 1);

		loadData(0);

		scrollView = (LockableHorizontalScrollView) this
				.findViewById(R.id.ScrollView);

		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);
		theme = (ImageButton) findViewById(R.id.theme);

		listViews = (LinearLayout) this.findViewById(R.id.list_views);
		cateMenu = (LinearLayout) this.findViewById(R.id.cate_menu);
		closeMenuLinear = (LinearLayout) this
				.findViewById(R.id.close_menu_linear);
		closeMenu = this.findViewById(R.id.close_menu);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;

		LayoutParams menuparams = (LayoutParams) cateMenu.getLayoutParams();
		menuparams.width = screenWidth / 6 + 15;
		cateMenu.setLayoutParams(menuparams);
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listViews
				.getLayoutParams();
		params.width = screenWidth;
		listViews.setLayoutParams(params);

		LayoutParams scroll_params = (LayoutParams) scrollView
				.getLayoutParams();
		scroll_params.width = screenWidth;
		scrollView.setLayoutParams(scroll_params);
		LayoutParams closeparams = (LayoutParams) closeMenu.getLayoutParams();
		closeparams.width = screenWidth / 6;
		closeMenu.setLayoutParams(closeparams);

		category.setOnClickListener(this);
		back.setOnClickListener(this);
		exit.setOnClickListener(this);
		camera_slide.setOnClickListener(this);
		twodimen_slide.setOnClickListener(this);
		call_slide.setOnClickListener(this);
		theme.setOnClickListener(this);
	}

	private void loadData(final int type) {
		new Thread(new Runnable() {

			@Override
			public void run() {

				// 鑾峰彇JSON鏁版嵁
				if (type == 0) {
					getJsonData(0, currentCount, url);
					System.out.println("开始加载第一页");
					System.out.println(posters.size());
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				pageIndex = pageIndex + 1;
				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;
				case 2:
					msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					pageIndex = 1;
					break;

				default:
					break;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	public void getJsonData(int startIndex, int endIndex, String url) {

		List<TableItem> strings = new ArrayList<TableItem>();

		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpPost mPost = new HttpPost(url);
			HttpResponse response = mHttpClient.execute(mPost);
			HttpEntity entity = response.getEntity();
			String info = EntityUtils.toString(entity);

			System.out.println(info);

			JSONArray array = new JSONArray(info);
			jsonSize = array.length();
			for (int i = startIndex; i < endIndex; i++) {
				TableItem ti = new TableItem();
				JSONObject json = array.getJSONObject(i);
				String name = json.getString("name");
				String addr = json.getString("address");
				/*
				 * String imageUrl = json.getString("imageUrl"); Bitmap image =
				 * HttpPicGet.getHttpBitmap(imageUrl);
				 */

				ti.setTableText(name);
				ti.setTableTitle(addr);
				ti.setCommentsUrl(null);
				ti.setTableId(null);
				ti.setTablePic1(null);
				ti.setTablePic2(null);
				ti.setTableUrl(null);
				ti.setTime(null);
				posters.add(ti);
			}

			for (TableItem body : posters) {
				strings.add(body);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(2);
			}
		}).start();
	}

	/**
	 * 鍔犺浇鏇村
	 */
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(1);
			}
		}).start();
	}

	/**
	 * 鏇存柊椤甸潰鏁版嵁
	 */
	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				System.out.println(posters.size());
				List<TableItem> strings = new ArrayList<TableItem>();
				if (posters != null) {
					for (int i = 0; i < currentCount; i++) {

						strings.add(posters.get(i));
					}

					if (!strings.isEmpty()) {
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
				}
				mAdapter.notifyDataSetChanged();
				// 璇夊畠鏁版嵁鍔犺浇瀹屾瘯;
				mPullDownView.notifyDidLoad();
				break;
			}
			case WHAT_DID_REFRESH: {
				List<TableItem> strings = new ArrayList<TableItem>();
				currentCount += LOADCOUNT;
				if (currentCount > jsonSize) {
					Toast.makeText(CultureActivity.this, "亲，没有更多了哦！",
							Toast.LENGTH_SHORT).show();
					posters.clear();
					getJsonData(currentCount - LOADCOUNT, jsonSize, url);
					strings.addAll(posters);
					if (!strings.isEmpty()) {
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
					// 鍛婅瘔瀹冭幏鍙栨洿澶氬畬姣?
					mPullDownView.notifyDidRefresh();
					break;
				}
				posters.clear();
				getJsonData(currentCount - LOADCOUNT, currentCount, url);
				strings.addAll(posters);
				if (!strings.isEmpty()) {
					System.out.println(strings.size());
					mStrings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				// 鍛婅瘔瀹冭幏鍙栨洿澶氬畬姣?
				mPullDownView.notifyDidRefresh();
				break;
			}
			case WHAT_DID_MORE: {
				List<TableItem> strings = new ArrayList<TableItem>();
				currentCount += LOADCOUNT;
				if (currentCount > jsonSize) {
					Toast.makeText(CultureActivity.this, "亲，没有更多了哦！",
							Toast.LENGTH_SHORT).show();
					posters.clear();
					getJsonData(currentCount - LOADCOUNT, jsonSize, url);
					strings.addAll(posters);
					if (!strings.isEmpty()) {
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
					// 鍛婅瘔瀹冭幏鍙栨洿澶氬畬姣?
					mPullDownView.notifyDidMore();
					break;
				}
				posters.clear();
				getJsonData(currentCount - LOADCOUNT, currentCount, url);
				strings.addAll(posters);
				if (!strings.isEmpty()) {
					System.out.println(strings.size());
					mStrings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				// 鍛婅瘔瀹冭幏鍙栨洿澶氬畬姣?
				mPullDownView.notifyDidMore();
				break;
			}
			}
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			Intent qinqiangintent = new Intent(this, QinqiangActivity.class);
			this.startActivity(qinqiangintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case 1:
			Intent shiguintent = new Intent(this, ShiguActivity.class);
			this.startActivity(shiguintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case 2:
			Intent shehuointent = new Intent(this, ShehuoActivity.class);
			this.startActivity(shehuointent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case 3:
			Intent famensiintent = new Intent(this, FamensiActivity.class);
			this.startActivity(famensiintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case 4:
			Intent nisuintent = new Intent(this, NisuActivity.class);
			this.startActivity(nisuintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case 5:
			Intent nianhuaintent = new Intent(this, NianhuaActivity.class);
			this.startActivity(nianhuaintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		}
	}

	private Runnable mScrollToButton = new Runnable() {
		@Override
		public void run() {
			if (!isOpen) {
				scrollView.smoothScrollTo(screenWidth / 6 + 15, 0);// 鏀瑰彉婊氬姩鏉＄殑浣嶇疆
				isOpen = true;
				closeMenuLinear.setVisibility(View.VISIBLE);
			} else {
				scrollView.smoothScrollTo(0, 0);
				isOpen = false;
				closeMenuLinear.setVisibility(View.GONE);
			}
		}
	};

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.category:
			slideBarHandler.post(mScrollToButton);
			break;
		case R.id.exit:
			ExitShowTips.showTips(this);
			break;
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.twodimen_sli:
			Intent twodimen = new Intent(CultureActivity.this,
					CaptureActivity.class);
			startActivityForResult(twodimen, TWODIMEN);
			overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
			break;
		case R.id.camera_sli:
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
			date = new Date();
			str = format.format(date);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), str + ".jpg")));
			startActivityForResult(intent, CAMERA);
			overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
			break;
		case R.id.call_sli:
			Intent callintent = new Intent(Intent.ACTION_DIAL,
					Uri.parse("tel:"));
			startActivity(callintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.theme:
			Intent themeintent = new Intent(this, ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (data == null) {
			return;
		}
		if (requestCode == CAMERA) {
			// 设置文件保存路径这里放在根目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/" + str + ".jpg");
			Toast.makeText(this, "拍摄的照片将放在SD卡根目录哦亲！", Toast.LENGTH_SHORT)
					.show();
		} else if (requestCode == TWODIMEN) {
			Intent intent = new Intent(this, TransportationActivity.class);
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			intent.putExtra("result", scanResult);
			intent.putExtra("callMap", true);
			startActivity(intent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			slideBarHandler.post(mScrollToButton);
			break;
		case KeyEvent.KEYCODE_BACK:
			if (isOpen) {
				slideBarHandler.post(mScrollToButton);
			} else {
				this.finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			VolumeController.volUp(audioManager);
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			VolumeController.volDown(audioManager);
			break;
		}
		return true;
	}
}
