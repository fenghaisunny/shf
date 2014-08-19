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
	private BMapManager mBMapMan;// ��������
	private EditText et_search;
	private ImageView btn_search;
	private ImageButton back;
	private RadioButton radio_btn_changemode;// �ı�ģʽ��radio_btn_changemode
	private RadioButton radio_btn_refresh;// ����λ�õ�radio_btn_refresh
	// private RadioButton radio_btn_more;//����λ�õ�radio_btn_refresh
	private RadioButton radio_btn_fujin;// ����λ�õ�radio_btn_refresh
	private RadioButton radio_btn_search_bus;// ����·��
	private EditText et_from;// ��������·��
	private EditText et_to;
	private ImageButton bus_search;
	private String key = "60BC0E114DF04490271E1FD3974546C4CDAD4CC3";
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private MapController mMapController;
	private MKSearch mkSearch;
	private String content;// �û������Ҫ��ѯ������
	private GeoPoint pt1; // ��ǰ�����
	private int[] icons = { R.drawable.icon_class_ktv,
			R.drawable.icon_class_hotel, R.drawable.icon_class_meishi,
			R.drawable.icon_class_petrolstation,
			R.drawable.icon_class_supermarket, R.drawable.icon_company,
			R.drawable.icon_class_viewspot,
			R.drawable.icon_class_xiaochikuaican,
			R.drawable.icon_class_xingjijiudian, R.drawable.icon_class_bath,
			R.drawable.icon_class_atm, R.drawable.icon_class_bank };
	private String[] items = { "KTV", "����", "��ʳ", "����վ", "����", "��˾", "����",
			"���", "�Ƶ�", "ϴԡ", "ȡ���", "����" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mBMapMan = new BMapManager(getApplication());
		mBMapMan.init(key, null);
		// ע�⣺��������setContentViewǰ��ʼ��BMapManager���󣬷���ᱨ��
		setContentView(R.layout.transportation);
		MyActivityManager.getInstance().addActivity(this);
		mMapView = (MapView) findViewById(R.id.bmapView);

		callMap = getIntent().getBooleanExtra("callMap", false);
		
		mMapView.setBuiltInZoomControls(true);
		// mMapView.setDrawOverlayWhenZooming(true);
		mMapController = mMapView.getController();
		// �õ�mMapView�Ŀ���Ȩ,�����������ƺ�����ƽ�ƺ�����
		mMapController.setZoom(14);// ���õ�ͼzoom����

		initView();
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������

		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setAddrType("all");// ���صĶ�λ���������ַ��Ϣ
		option.setCoorType("bd09ll");// ���صĶ�λ����ǰٶȾ�γ��,Ĭ��ֵgcj02
		option.setScanSpan(5000);// ���÷���λ����ļ��ʱ��Ϊ5000ms
		option.disableCache(true);// ��ֹ���û��涨λ
		option.setPoiNumber(5); // ��෵��POI����
		option.setPoiDistance(1000); // poi��ѯ����
		option.setPoiExtraInfo(true); // �Ƿ���ҪPOI�ĵ绰�͵�ַ����ϸ��Ϣ



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
					Toast.makeText(TransportationActivity.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(
						TransportationActivity.this,
						"����" + res.getPlan(0).getContent() + "����"
								+ res.getPlan(0).getLine(0).getNumViaStops()
								+ "վ����ʻ" + res.getPlan(0).getDistance() + "��",
						Toast.LENGTH_LONG).show();
				System.out.println("����" + res.getPlan(0).getContent());
				System.out.println(res.getPlan(0).getLine(0).getNumViaStops()
						+ "վ");
				System.out.println("����" + res.getPlan(0).getDistance() + "��");
				/*
				 * int start = res.getPlan(0).getStart().getLatitudeE6(); int
				 * end = res.getPlan(0).getEnd().getLatitudeE6();
				 */
				double distance1 = res.getPlan(0).getDistance() / 1000;
				String dist1 = new java.text.DecimalFormat("0.00")
						.format(distance1);
				String plan1 = "����" + res.getPlan(0).getContent() + "����"
						+ res.getPlan(0).getLine(0).getNumViaStops() + "վ����ʻ"
						+ dist1 + "ǧ��";
				String plan2 = "��";
				if (res.getPlan(1) != null) {
					plan2 = "����" + res.getPlan(1).getContent() + "����"
							+ res.getPlan(1).getLine(0).getNumViaStops()
							+ "վ����ʻ" + res.getPlan(0).getDistance() + "��";
				}
				
				new AlertDialog.Builder(TransportationActivity.this)
				.setTitle("�Ƽ��˳�����")
				.setItems(new String[] { plan1, plan2 }, null)
				.setNegativeButton("�鿴��ͼ·��", null).show();
				
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
				// �˴���չʾһ��������Ϊʾ��
				routeOverlay.setData(res.getPlan(0));
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(routeOverlay);
				mMapView.refresh();
				// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
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
							"��Ǹ��δ�ҵ���" + content + "����λ����Ϣ", Toast.LENGTH_LONG)
							.show();
					et_from.setText("");
					et_to.setText("");
					return;
				} else if (error != 0 || res == null) {
					Toast.makeText(TransportationActivity.this, "����������..",
							Toast.LENGTH_LONG).show();
					return;
				}
					MainActivity.showToast(TransportationActivity.this, content);
				// ��poi�����ʾ����ͼ��
				PoiOverlay poiOverlay = new PoiOverlay(
						TransportationActivity.this, mMapView);
				poiOverlay.setData(res.getAllPoi());
				mMapView.getOverlays().clear();
				mMapView.getOverlays().add(poiOverlay);
				mMapView.refresh();
				// mPop.dismiss();
				// ��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
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

	// ��ʼ���ؼ�
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
				// ÿ��item�����������
				mkSearch.poiSearchNearBy(items[position], pt1, 5000);
				content = "��������"+items[position];
				mPop.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// mMapView.setSatellite(true);//�������ǵ�ͼ
		// mMapView.setTraffic(true); ������ͨ��ͼ
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		// int width = metric.widthPixels; // ��Ļ��ȣ����أ�
		int height = metric.heightPixels; // ��Ļ�߶ȣ����أ�
		switch (v.getId()) {
		case R.id.back: // ����
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.radio_btn_changemode: // �ı�ģʽ�ĵ���¼�
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
		case R.id.radio_btn_refresh: // ����λ�õĵ���¼�
			flag_new = true;
			break;
		case R.id.btn_search: // ���������ĵ���¼�
			content = et_search.getText().toString();
			mkSearch.poiSearchNearBy(content, pt1, 100000);// ����10000�׽�������
			// mkSearch.poiSearchInCity("����", content);
			break;
		case R.id.radio_btn_fujin: // ����
			initPopWindow();
			// mPop.showAsDropDown(v,20,-284);//�����ButtonΪanchor���������Ϊê����׼�������·�����
			mPop.showAtLocation(
					TransportationActivity.this.findViewById(R.id.rl),
					Gravity.CENTER, 0, 0);// ����Ļ���У���ƫ��
			break;
		case R.id.radio_btn_search_bus: // ����·��(�ײ�������)
			initPopWindow1();
			mPop1.showAsDropDown(v, -180, height * 3 / 4);// �����ButtonΪanchor���������Ϊê����׼�������·�����
			// mPop1.showAsDropDown(v,20,-20);//����ƫ��20������-20��һ��״̬���ĳ���
			// mPop1.showAtLocation(TransptationActivity.this.findViewById(R.id.rl),
			// Gravity.CENTER, 0, 0);//����Ļ���У���ƫ��
			break;
		case R.id.bus_search: // ����·����	����ť
			// ������յ��name���и�ֵ��Ҳ����ֱ�Ӷ����긳ֵ����ֵ�����򽫸��������������
			MKPlanNode stNode = new MKPlanNode();
			stNode.name = et_from.getText().toString();
			MKPlanNode enNode = new MKPlanNode();
			enNode.name = et_to.getText().toString();
			mkSearch.transitSearch("����", stNode, enNode);
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

		// ���һ���Ӷ�λһ��
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
				locationData.accuracy = location.getRadius();// ��ȡ����
				locationData.direction = location.getDerect();
				// myLocationOverlay.setData(locationData);//�첽����locationData,�����첽���أ�����myLocationOverlay������ʾ
				mMapView.refresh();// �˴�ˢ�±�����
				// ��λ����λ�ã��˾�û�У����޷���λ
				mMapController.animateTo(new GeoPoint(
						(int) (locationData.latitude * 1e6),
						(int) (locationData.longitude * 1e6)));
				MyLocationOverlay myLocationOverlay = new MyLocationOverlay(
						mMapView);
				// LocationData locData = new LocationData();
				// �ֶ���λ��Դ��Ϊ�찲�ţ���ʵ��Ӧ���У���ʹ�ðٶȶ�λSDK��ȡλ����Ϣ��Ҫ��SDK����ʾһ��λ�ã���Ҫ
				// ʹ�ðٶȾ�γ�����꣨bd09ll��
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
			// ��ʼ��mkSearch
		}
	}

	public static LocationData getLocationDate() {
		return locationData;
	}

	/* ��ʼ��һ������ */
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

	/* ��ʼ������վ�㵯�� */
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

	// �ػ񰴼��¼�
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return true;
	}
}
