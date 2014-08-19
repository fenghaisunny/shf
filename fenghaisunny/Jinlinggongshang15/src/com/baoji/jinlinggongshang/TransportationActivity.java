package com.baoji.jinlinggongshang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PoiOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.baoji.jinlinggongshang.baidumap.MyAdapter;
import com.baoji.jinlinggongshang.util.MyActivityManager;

public class TransportationActivity extends Activity implements OnClickListener {
	private GridView gv;
	private PopupWindow mPop;
	private PopupWindow mPop1;

	public boolean callMap = false;

	private View layout;
	private View layout1;
	private static boolean isWeiXing = false;
	private static boolean isSimple = true;
	private static LocationData locationData = new LocationData();
	private boolean flag_new = true;
	private MapView mMapView;
	private BMapManager mBMapMan;// 加载引擎
	private EditText et_search;
	private ImageView btn_search;
	private ImageButton back;
	private RadioButton radio_btn_changemode;// 改变模式的radio_btn_changemode
	private RadioButton radio_btn_refresh;// 更新位置的radio_btn_refresh
	// private RadioButton radio_btn_more;//更新位置的radio_btn_refresh
	private RadioButton radio_btn_fujin;// 更新位置的radio_btn_refresh
	private RadioButton radio_btn_search_bus;// 公交路线
	private EditText et_from;// 搜索公交路线
	private EditText et_to;
	private ImageButton bus_search;
	private String key = "60BC0E114DF04490271E1FD3974546C4CDAD4CC3";
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private MapController mMapController;
	private MKSearch mkSearch;
	private String content;// 用户输入的要查询的内容
	private GeoPoint pt1; // 当前坐标点
	private int[] icons = { R.drawable.icon_class_ktv,
			R.drawable.icon_class_hotel, R.drawable.icon_class_meishi,
			R.drawable.icon_class_petrolstation,
			R.drawable.icon_class_supermarket, R.drawable.icon_company,
			R.drawable.icon_class_viewspot,
			R.drawable.icon_class_xiaochikuaican,
			R.drawable.icon_class_xingjijiudian, R.drawable.icon_class_bath,
			R.drawable.icon_class_atm, R.drawable.icon_class_bank };
	private String[] items = { "KTV", "宾馆", "美食", "加油站", "超市", "公司", "景点",
			"快餐", "酒店", "洗浴", "取款机", "银行" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(key, null);
		// 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
		setContentView(R.layout.transportation);
		MyActivityManager.getInstance().addActivity(this);
		mMapView = (MapView) findViewById(R.id.bmapView);

		callMap = getIntent().getBooleanExtra("callMap", false);
		
		mMapView.setBuiltInZoomControls(true);
		// mMapView.setDrawOverlayWhenZooming(true);
		mMapController = mMapView.getController();
		// 得到mMapView的控制权,可以用它控制和驱动平移和缩放
		mMapController.setZoom(14);// 设置地图zoom级别

		initView();
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// 返回的定位结果包含地址信息
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(5000);// 设置发起定位请求的间隔时间为5000ms
		option.disableCache(true);// 禁止启用缓存定位
		option.setPoiNumber(5); // 最多返回POI个数
		option.setPoiDistance(1000); // poi查询距离
		option.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息



		mkSearch = new MKSearch();
		mkSearch.init(mBMapMan, new MKSearchListener() {

			@Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			@Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(TransportationActivity.this, "抱歉，未找到结果",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(
						TransportationActivity.this,
						"乘坐" + res.getPlan(0).getContent() + "经过"
								+ res.getPlan(0).getLine(0).getNumViaStops()
								+ "站，行驶" + res.getPlan(0).getDistance() + "米",
						Toast.LENGTH_LONG).show();
				System.out.println("乘坐" + res.getPlan(0).getContent());
				System.out.println(res.getPlan(0).getLine(0).getNumViaStops()
						+ "站");
				System.out.println("距离" + res.getPlan(0).getDistance() + "米");
				/*
				 * int start = res.getPlan(0).getStart().getLatitudeE6(); int
				 * end = res.getPlan(0).getEnd().getLatitudeE6();
				 */
				double distance1 = res.getPlan(0).getDistance() / 1000;
				String dist1 = new java.text.DecimalFormat("0.00")
						.format(distance1);
				String plan1 = "乘坐" + res.getPlan(0).getContent() + "经过"
						+ res.getPlan(0).getLine(0).getNumViaStops() + "站，行驶"
						+ dist1 + "千米";
				String plan2 = "无";
				if (res.getPlan(1) != null) {
					plan2 = "乘坐" + res.getPlan(1).getContent() + "经过"
							+ res.getPlan(1).getLine(0).getNumViaStops()
							+ "站，行驶" + res.getPlan(0).getDistance() + "米";
				}
				
				new AlertDialog.Builder(TransportationActivity.this)
				.setTitle("推荐乘车方案")
				.setItems(new String[] { plan1, plan2 }, null)
				.setNegativeButton("查看地图路线", null).show();
				
/*				AlertDialog dlg =  new AlertDialog.Builder(TransportationActivity.this).create();
				dlg.show();
				Window window = dlg.getWindow();
				window.setContentView(R.layout.shrew_exit_dialog);*/
				
				
				/*
				 * RouteTransitResult result = new RouteTransitResult(res);
				 * System.out.println(result.getRouteDescripe(0));
				 */
				TransitOverlay routeOverlay = new TransitOverlay(
						TransportationActivity.this, mMapView);
				// 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0));
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(routeOverlay);
				mMapView.refresh();
				// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mMapView.getController().zoomToSpan(
						routeOverlay.getLatSpanE6(),
						routeOverlay.getLonSpanE6());
				mMapView.getController().animateTo(res.getStart().pt);
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {
			}

			@Override
			public void onGetPoiResult(MKPoiResult res, int type, int error) {

				if (error == MKEvent.ERROR_RESULT_NOT_FOUND) {
					Toast.makeText(TransportationActivity.this,
							"抱歉，未找到“" + content + "”的位置信息", Toast.LENGTH_LONG)
							.show();
					et_from.setText("");
					et_to.setText("");
					return;
				} else if (error != 0 || res == null) {
					Toast.makeText(TransportationActivity.this, "搜索出错啦..",
							Toast.LENGTH_LONG).show();
					return;
				}
					MainActivity.showToast(TransportationActivity.this, content);
				// 将poi结果显示到地图上
				PoiOverlay poiOverlay = new PoiOverlay(
						TransportationActivity.this, mMapView);
				poiOverlay.setData(res.getAllPoi());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(poiOverlay);
				mMapView.refresh();
				// mPop.dismiss();
				// 当ePoiType为2（公交线路）或4（地铁线路）时， poi坐标为空
				for (MKPoiInfo info : res.getAllPoi()) {
					if (info.pt != null) {
						mMapView.getController().animateTo(info.pt);
						break;
					}
				}
			}

			@Override
			public void onGetPoiDetailSearchResult(int arg0, int arg1) {
			}

			@Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult arg0,
					int arg1) {
			}

			@Override
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
	
			}

			@Override
			public void onGetAddrResult(MKAddrInfo arg0, int arg1) {
			}

		});

		mLocationClient.setLocOption(option);
		mLocationClient.start();
		// mMapView.refresh();
		
		if (callMap) {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					//callMap = false;
					System.out.println(callMap);
					content = getIntent().getStringExtra("result");
					System.out.println(content);
					mkSearch.poiSearchNearBy(content, new GeoPoint((int) (34.368279f * 1E6),
							(int) (107.097364f * 1E6)), 100000);
					mMapView.refresh();
				}
			}, 2000);
		}
	}

	// 初始化控件
	private void initView() {

		back = (ImageButton) findViewById(R.id.back);
		btn_search = (ImageView) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		et_search = (EditText) findViewById(R.id.et_search);
		content = et_search.getText().toString();
		radio_btn_changemode = (RadioButton) findViewById(R.id.radio_btn_changemode);
		radio_btn_refresh = (RadioButton) findViewById(R.id.radio_btn_refresh);
		// radio_btn_more = (RadioButton) findViewById(R.id.radio_btn_more);
		radio_btn_fujin = (RadioButton) findViewById(R.id.radio_btn_fujin);
		radio_btn_search_bus = (RadioButton) findViewById(R.id.radio_btn_search_bus);
		radio_btn_changemode.setOnClickListener(this);
		radio_btn_search_bus.setOnClickListener(this);
		back.setOnClickListener(this);
		radio_btn_refresh.setOnClickListener(this);
		radio_btn_fujin.setOnClickListener(this);
		layout = View.inflate(this, R.layout.window, null);
		layout1 = View.inflate(this, R.layout.route, null);

		et_from = (EditText) layout1.findViewById(R.id.et_form);
		et_to = (EditText) layout1.findViewById(R.id.et_to);
		bus_search = (ImageButton) layout1.findViewById(R.id.bus_search);
		bus_search.setOnClickListener(this);
		gv = (GridView) layout.findViewById(R.id.gridview);
		MyAdapter adapter = new MyAdapter(this, items, icons);
		gv.setAdapter(adapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// 每个item点击进行搜索
				mkSearch.poiSearchNearBy(items[position], pt1, 5000);
				content = "您附近的"+items[position];
				mPop.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// mMapView.setSatellite(true);//设置卫星地图
		// mMapView.setTraffic(true); 设置普通地图
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int width = metric.widthPixels; // 屏幕宽度（像素）
		int height = metric.heightPixels; // 屏幕高度（像素）
		switch (v.getId()) {
		case R.id.back: // 返回
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.radio_btn_changemode: // 改变模式的点击事件
			if (isWeiXing) {
				isWeiXing = false;
				isSimple = true;
				mMapView.setSatellite(isWeiXing);
				mMapView.setTraffic(isSimple);
			} else if (isSimple) {
				isSimple = false;
				isWeiXing = true;
				mMapView.setSatellite(isWeiXing);
				mMapView.setTraffic(isSimple);
			}
			break;
		case R.id.radio_btn_refresh: // 更新位置的点击事件
			flag_new = true;
			break;
		case R.id.btn_search: // 搜索附近的点击事件
			content = et_search.getText().toString();
			mkSearch.poiSearchNearBy(content, pt1, 100000);// 附近10000米进行搜索
			// mkSearch.poiSearchInCity("宝鸡", content);
			break;
		case R.id.radio_btn_fujin: // 附近
			initPopWindow();
			// mPop.showAsDropDown(v,20,-284);//以这个Button为anchor（可以理解为锚，基准），在下方弹出
			mPop.showAtLocation(
					TransportationActivity.this.findViewById(R.id.rl),
					Gravity.CENTER, 0, 0);// 在屏幕居中，无偏移
			break;
		case R.id.radio_btn_search_bus: // 公交路线(底部导航的)
			initPopWindow1();
			mPop1.showAsDropDown(v, -180, height * 3 / 4);// 以这个Button为anchor（可以理解为锚，基准），在下方弹出
			// mPop1.showAsDropDown(v,20,-20);//横轴偏移20，纵轴-20，一个状态栏的长度
			// mPop1.showAtLocation(TransptationActivity.this.findViewById(R.id.rl),
			// Gravity.CENTER, 0, 0);//在屏幕居中，无偏移
			break;
		case R.id.bus_search: // 公交路线搜	索按钮
			// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
			MKPlanNode stNode = new MKPlanNode();
			stNode.name = et_from.getText().toString();
			MKPlanNode enNode = new MKPlanNode();
			enNode.name = et_to.getText().toString();
			mkSearch.transitSearch("宝鸡", stNode, enNode);
			break;
		}
		
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		if (mBMapMan != null) {
			mBMapMan.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
		if (mBMapMan != null) {
			mBMapMan.start();
		}
		super.onResume();
	}

	private class MyLocationListener implements BDLocationListener {

		// 大概一秒钟定位一次
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			if (flag_new == true) {
				Toast.makeText(TransportationActivity.this,
						location.getAddrStr(), Toast.LENGTH_LONG).show();

				System.out.println(location.getAddrStr());
				pt1 = new GeoPoint((int) (location.getLatitude() * 1E6),
						(int) (location.getLongitude() * 1E6));
				locationData.latitude = location.getLatitude();
				locationData.longitude = location.getLongitude();
				locationData.direction = 2.0f;
				locationData.accuracy = location.getRadius();// 获取服务
				locationData.direction = location.getDerect();
				// myLocationOverlay.setData(locationData);//异步加载locationData,必须异步加载，否则myLocationOverlay不会显示
				mMapView.refresh();// 此处刷新必须有
				// 定位本地位置，此句没有，则无法定位
				mMapController.animateTo(new GeoPoint(
						(int) (locationData.latitude * 1e6),
						(int) (locationData.longitude * 1e6)));
				MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
						mMapView);
				// LocationData locData = new LocationData();
				// 手动将位置源置为天安门，在实际应用中，请使用百度定位SDK获取位置信息，要在SDK中显示一个位置，需要
				// 使用百度经纬度坐标（bd09ll）
				// locData.latitude = location.getLatitude();
				// locData.longitude = location.getLongitude();
				locationData.direction = 2.0f;
				myLocationOverlay.setData(locationData);
				mMapView.getOverlays().add(myLocationOverlay);


				mMapView.refresh();
				mMapView.getController().animateTo(
						new GeoPoint((int) (locationData.latitude * 1e6),
								(int) (locationData.longitude * 1e6)));
				flag_new = false;
				System.out.println(callMap);
/*				if (callMap && location != null) {
					//callMap = false;
					System.out.println(callMap);
					content = getIntent().getStringExtra("result");
					System.out.println(content);
					mkSearch.poiSearchNearBy(content, pt1, 100000);
				}*/
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				Toast.makeText(getApplicationContext(), "null", 1).show();
				return;
			}
			Toast.makeText(getApplicationContext(), "111111", 1).show();
			Log.i("onReceivePoi", "______________________");
			// 初始化mkSearch
		}
	}

	public static LocationData getLocationDate() {
		return locationData;
	}

	/* 初始化一个弹窗 */
	@SuppressLint("ShowToast")
	private void initPopWindow() {
		if (mPop == null) {
			mPop = new PopupWindow(layout, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPop.setBackgroundDrawable(new BitmapDrawable());
			mPop.setOutsideTouchable(true);
			mPop.setFocusable(true);
		}
		if (mPop.isShowing()) {
			mPop.dismiss();
		}
	}

	/* 初始化输入站点弹窗 */
	private void initPopWindow1() {
		if (mPop1 == null) {
			mPop1 = new PopupWindow(layout1, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			mPop1.setBackgroundDrawable(new BitmapDrawable());
			mPop1.setOutsideTouchable(true);
			mPop1.setFocusable(true);
		}
		if (mPop1.isShowing()) {
			mPop1.dismiss();
		}
	}

	// 截获按键事件
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return true;
	}
}
