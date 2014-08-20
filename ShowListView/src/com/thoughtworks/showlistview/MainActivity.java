package com.thoughtworks.showlistview;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtworks.showlistview.adapter.ListAdapter;
import com.thoughtworks.showlistview.util.ImageLoaderUtils;
import com.thoughtworks.showlistview.view.PullDownView;
import com.thoughtworks.showlistview.view.PullDownView.OnPullDownListener;
import com.thoughtworks.showlistview.view.TableItem;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener, OnPullDownListener, OnItemLongClickListener,
		Callback {

	public static int screenWidth, screenHeight;

	private String url = "http://thoughtworks-ios.herokuapp.com/facts.json";

	private ListView mListView;// PullDownView中的内嵌ListView
	private ListAdapter mAdapter;// 为ListView适配数据的适配器（作为大环境为多个PullDownView所共有）

	private PullDownView mPullDownView;// 盛放支持刷新的ListView的容器
	private List<TableItem> posters = new ArrayList<TableItem>();// 盛放每次获取的json数据的容器
	private List<TableItem> mStrings = new ArrayList<TableItem>();// 适配器的适配数据
	private Message msg;//

	private static final int WHAT_DID_LOAD_DATA = 0;// 首次加载
	private static final int WHAT_DID_REFRESH = 1;// 下拉刷新
	private static final int WHAT_DID_MORE = 2;// 上拉更多

	private int currentCount = 8;// 定义初次加载的ListView的条数（可以根据网络条件限定）
	private static final int LOADCOUNT = 4;// 自定义每次加载ListView的条数（可以根据网络条件限定）
	private int jsonSize = 0;// json数据条数的最大值，作为刷新的界限

	private TextView title_text;
	public String toptitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;
		
		title_text = (TextView) findViewById(R.id.titleBar_tv);

		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setVerticalFadingEdgeEnabled(false);

		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);

		mAdapter = new ListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);

		ImageLoaderUtils.imageLoderIni(this);

		loadData(0);
	}

	private void loadData(final int type) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (type == 0) {
					currentCount = 8;
					getJsonData(0, currentCount, url);
					System.out.println("开始加载第一页");
					System.out.println(posters.size());
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;
				case 2:
					msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					break;

				default:
					break;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	public static String getJsonContent(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(3000);
			connection.setRequestMethod("GET");
			connection.setDoInput(true);
			int code = connection.getResponseCode();
			if (code == 200) {
				return changeInputString(connection.getInputStream());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}

	private static String changeInputString(InputStream inputStream) {

		String jsonString = "";
		ByteArrayOutputStream outPutStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		try {
			while ((len = inputStream.read(data)) != -1) {
				outPutStream.write(data, 0, len);
			}
			jsonString = new String(outPutStream.toByteArray());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	public void getJsonData(int startIndex, int endIndex, String url) {

		List<TableItem> strings = new ArrayList<TableItem>();

		try {
			String info = getJsonContent(url);
			System.out.println(info);

			JSONObject jsonObj = new JSONObject(info);
			toptitle = jsonObj.getString("title");
			JSONArray datalist = jsonObj.getJSONArray("rows");
			jsonSize = datalist.length();
			for (int i = startIndex; i < endIndex; i++) {
				TableItem ti = new TableItem();
				JSONObject json = datalist.getJSONObject(i);

				String title = json.getString("title");
				String description = json.getString("description");
				String imageHref = json.getString("imageHref");

				ti.setTableId(null);
				if(title == null||title.equals("null")){
					title =" ";
				}
				if(description == null||description.equals("null")){
					description = " ";
				}
				ti.setTableTitle(title);
				ti.setTableText(description);
				System.out.println(imageHref);
				if (imageHref == null||imageHref.equals("")||imageHref.length()<=1) {
					ti.setTableImageUrl(null);
				} else
					ti.setTableImageUrl(imageHref);
				posters.add(ti);
			}
			for (TableItem body : posters) {
				strings.add(body);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
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
	 * 加载更多
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

	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				try {
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
					// 数据加载完毕
					mPullDownView.notifyDidLoad();

					break;
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					posters.clear();
					mStrings.clear();
					mAdapter.notifyDataSetChanged();
				}

			}
			case WHAT_DID_REFRESH: {
				try {
					List<TableItem> strings = new ArrayList<TableItem>();
					currentCount += LOADCOUNT;
					if (currentCount > jsonSize) {
						Toast.makeText(MainActivity.this, "Sorry!No more!",
								Toast.LENGTH_SHORT).show();
						posters.clear();
						getJsonData(currentCount - LOADCOUNT, jsonSize, url);
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							System.out.println(strings.size());
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
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
					mPullDownView.notifyDidRefresh();
					break;

				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					posters.clear();
					mStrings.clear();
					mAdapter.notifyDataSetChanged();

				}

			}
			case WHAT_DID_MORE: {
				List<TableItem> strings = new ArrayList<TableItem>();
				currentCount += LOADCOUNT;
				if (currentCount > jsonSize) {
					Toast.makeText(MainActivity.this, "Sorry!No more!",
							Toast.LENGTH_SHORT).show();
					posters.clear();
					getJsonData(currentCount - LOADCOUNT, jsonSize, url);
					strings.addAll(posters);
					if (!strings.isEmpty()) {
						mStrings.addAll(strings);
						mAdapter.notifyDataSetChanged();
					}
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
				mPullDownView.notifyDidMore();

				break;
			}
			}
		}

	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
