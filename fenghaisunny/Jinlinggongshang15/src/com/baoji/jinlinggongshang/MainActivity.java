package com.baoji.jinlinggongshang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.baoji.jinlinggongshang.pulltorefresh.MainTabActivity;
import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.WeatherIcon;

/**2014-3-5
 * @author sunhaifeng
 */
@SuppressLint({ "HandlerLeak", "WorldReadableFiles" })
public class MainActivity extends Activity implements OnClickListener {
	// 定义sensor管理器
	private SensorManager mSensorManager;
	
	//用来存放图标id的容器
	public List<Object> iconIds = new ArrayList<Object>();

	public int thememode = 0;

	ImageView about, twodimen, history, camera, speciality, convenient, folk,
			legend, transportation, tourism, food, culture;

	public static final int TWODIMEN = 2;// 二维码
	public static final int CAMERA = -2;// 拍照

	public static final int DIR = 1;

	public static final String IMAGE_UNSPECIFIED = "image/*";
	String str = null;
	Date date = null;
	// 预设的时间显示（无网络）
	TextView today_text;
	// 侧边栏里的按钮
	public ImageButton camera_slide;
	public ImageButton twodimen_slide;
	public ImageButton call_slide;

	private LockableHorizontalScrollView scrollView;
	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;

	private int screenWidth;
	private boolean isOpen = false;
	private final Handler slideBarHandler = new Handler();
	// 震动
	Vibrator vibrator;
	// 震动监听器
	private sensorListener sensor;

	// 代表背景变换状态的标识变量
	public static int flag = 0;

	private static RelativeLayout rel = null;

	private String url = "http://m.weather.com.cn/data/" + 101110901 + ".html";

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_silde);
		MyActivityManager.getInstance().addActivity(this);

		Intent pushintent = getIntent();
		if (null != pushintent && getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle
					.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);

			Toast.makeText(this, "“" + title + "”为您发送来的推送：" + content,
					Toast.LENGTH_SHORT).show();
		}
		//存放图标id的数组
		int iconIds01[] = { R.drawable.twodimen_icon, R.drawable.about_icon,
				R.drawable.culture_icon, R.drawable.history_icon,
				R.drawable.folk_icon, R.drawable.food_icon,
				R.drawable.speciality_icon, R.drawable.legend_icon,
				R.drawable.convenient_icon, R.drawable.transportation_icon,
				R.drawable.tourism_icon, R.drawable.camera_icon };
		int iconIds02[] = { R.drawable.a_0, R.drawable.a_1, R.drawable.a_2,
				R.drawable.a_3, R.drawable.a_4, R.drawable.a_5, R.drawable.a_6,
				R.drawable.a_7, R.drawable.a_8, R.drawable.a_9,
				R.drawable.a_10, R.drawable.a_11 };
		
		//取出表示主题模式的共享变量
		SharedPreferences pre = getSharedPreferences("thememode",
				MODE_WORLD_READABLE);
		thememode = pre.getInt("theme", 1);
		
		//根据取出的thememode向装载图标id的容器中放入响应的值
		if (thememode == 1) {
			iconIds.clear();
			for (int i = 0; i < iconIds01.length; i++) {
				//这里用到了java集合框架的自动打包知识，将基本数据类型（整型）自动打包为Integer类型
				iconIds.add(i, iconIds01[i]);
			}
			//切换相应的背景
			flag = 0;
			bgChangeHandler.sendEmptyMessage(0);
			System.out.println(iconIds.size());

		} else if (thememode == 2) {
			iconIds.clear();
			for (int i = 0; i < iconIds02.length; i++) {
				iconIds.add(i, iconIds02[i]);
			}
			flag = 1;
			bgChangeHandler.sendEmptyMessage(0);
			System.out.println(iconIds.size());
		} else if(thememode == 3){
			iconIds.clear();
			for (int i = 0; i < iconIds01.length; i++) {
				iconIds.add(i, iconIds01[i]);
			}
			flag = 2;
			bgChangeHandler.sendEmptyMessage(0);
		} else if(thememode == 4){
			iconIds.clear();
			for (int i = 0; i < iconIds01.length; i++) {
				iconIds.add(i, iconIds01[i]);
			}
			flag = 3;
			bgChangeHandler.sendEmptyMessage(0);
		}

		scrollView = (LockableHorizontalScrollView) this
				.findViewById(R.id.ScrollView);
		listViews = (LinearLayout) this.findViewById(R.id.list_views);
		cateMenu = (LinearLayout) this.findViewById(R.id.cate_menu);
		closeMenuLinear = (LinearLayout) this
				.findViewById(R.id.close_menu_linear);
		closeMenu = this.findViewById(R.id.close_menu);
		// 获取侧边栏布局的参数并设置侧边栏的显示效果（宽度）
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		LayoutParams menuparams = (LayoutParams) cateMenu.getLayoutParams();
		menuparams.width = screenWidth / 6 + 15;
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

		twodimen = (ImageView) findViewById(R.id.twodimen);
		about = (ImageView) findViewById(R.id.about);
		culture = (ImageView) findViewById(R.id.culture);
		history = (ImageView) findViewById(R.id.history);
		folk = (ImageView) findViewById(R.id.folk);
		food = (ImageView) findViewById(R.id.food);
		speciality = (ImageView) findViewById(R.id.speciality);
		legend = (ImageView) findViewById(R.id.legend);
		convenient = (ImageView) findViewById(R.id.convenient);
		transportation = (ImageView) findViewById(R.id.transportation);
		tourism = (ImageView) findViewById(R.id.tourism);
		camera = (ImageView) findViewById(R.id.camera);

		setImageIcon(iconIds);
		
		ImageButton theme = (ImageButton)findViewById(R.id.theme);
		theme.setOnClickListener(this);

		ImageView exit = (ImageView) findViewById(R.id.exit);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);

		twodimen.setAlpha(150);
		about.setAlpha(150);
		culture.setAlpha(150);
		history.setAlpha(150);
		folk.setAlpha(150);
		food.setAlpha(150);
		speciality.setAlpha(150);
		legend.setAlpha(150);
		convenient.setAlpha(150);
		transportation.setAlpha(150);
		tourism.setAlpha(150);
		camera.setAlpha(150);

		twodimen.setOnClickListener(this);
		twodimen_slide.setOnClickListener(this);
		about.setOnClickListener(this);
		folk.setOnClickListener(this);
		culture.setOnClickListener(this);
		history.setOnClickListener(this);
		food.setOnClickListener(this);
		speciality.setOnClickListener(this);
		legend.setOnClickListener(this);
		transportation.setOnClickListener(this);
		tourism.setOnClickListener(this);
		exit.setOnClickListener(this);
		camera.setOnClickListener(this);
		camera_slide.setOnClickListener(this);
		call_slide.setOnClickListener(this);

		today_text = (TextView) findViewById(R.id.date);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 获取当前时间，进一步转化为字符串
		date = new Date();
		// 初始化日期信息
		str = format.format(date);
		today_text.setText(str);
		// 获得天气信息（调用一系列方法并更新UI）
		getweather(url);

		// “摇一摇”，初始化背景
		rel = (RelativeLayout) findViewById(R.id.rel);
		Resources r = getResources();
		rel.setBackgroundDrawable(r.getDrawable(R.drawable.bg01));
		// 获取传感器管理服务
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		// 震动
		vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
		// 监听器
		sensor = new sensorListener();
		// 加速度传感器
		mSensorManager.registerListener(sensor,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	// 实现侧边栏收展的线程
	private Runnable mScrollToButton = new Runnable() {
		@Override
		public void run() {
			if (!isOpen) {
				scrollView.smoothScrollTo(screenWidth / 6 + 15, 0);// 设置侧边栏的宽度
				isOpen = true;
				closeMenuLinear.setVisibility(View.VISIBLE);
			} else {
				scrollView.smoothScrollTo(0, 0);
				isOpen = false;
				closeMenuLinear.setVisibility(View.GONE);
			}
		}
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		bgChangeHandler.removeMessages(0);
	}
	
	

	/**2014-3-10
	 * @author sunhaifeng
	 * 自定义样式的Toast
	 * @param context 上下文环境	
	 * @param toastContent 自定义显示的toast字符串
	 */
	public static void showToast(Context context,String toastContent) {
		Toast toast = Toast.makeText(context,
				toastContent, Toast.LENGTH_SHORT);
			   toast.setGravity(Gravity.CENTER, 0, 0);
			   LinearLayout toastView = (LinearLayout) toast.getView();
			   ImageView imageCodeProject = new ImageView(context);
			   imageCodeProject.setImageResource(R.drawable.icon);
			   toastView.setBackgroundResource(R.drawable.gridview_bg);
			   toastView.addView(imageCodeProject, 0);
			   toast.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.twodimen:
			showToast(this,"二维码");
			goToTwoDimen();
			break;
		case R.id.about:
			showToast(this,"宝鸡概况");
			Intent aboutIntent = new Intent(MainActivity.this,
					AboutActivity.class);
			startActivity(aboutIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.culture:
			showToast(this,"宝鸡文化");
			Intent cultureIntent = new Intent(MainActivity.this,
					CultureActivity.class);
			startActivity(cultureIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.tourism:
			showToast(this,"旅游资源");
			Intent tourismIntent = new Intent(MainActivity.this,
					TourismActivity.class);
			startActivity(tourismIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.folk:
			showToast(this,"宝鸡民俗");
			Intent folkIntent = new Intent(MainActivity.this,
					FolkActivity.class);
			startActivity(folkIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.transportation:
			showToast(this,"交通信息");
			Intent transportationIntent = new Intent(MainActivity.this,
					TransportationActivity.class);
			startActivity(transportationIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.speciality:
			Intent specialityIntent = new Intent(MainActivity.this,
					MainTabActivity.class);
			startActivity(specialityIntent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.exit:
			ExitShowTips.showTips(this);
			break;
		case R.id.camera:
			showToast(this,"我要拍照");
			goToCamera();
			break;
		case R.id.twodimen_sli:
			goToTwoDimen();
			break;
		case R.id.camera_sli:
			goToCamera();
			break;
		case R.id.call_sli:
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"));
			startActivity(intent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.history:
			showToast(this,"宝鸡历史");
			Intent historyintent = new Intent(MainActivity.this,
					ShareActivity.class);
			startActivity(historyintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.food:
			showToast(this,"宝鸡美食");
			Intent foodintent = new Intent(MainActivity.this,
					ThemeChangeActivity.class);
			startActivity(foodintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.legend:
			showToast(this,"宝鸡传说");
			Intent legendintent = new Intent(MainActivity.this,
					ChuanshuoActivity.class);
			startActivity(legendintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.theme:
			Intent themeintent = new Intent(MainActivity.this,
					ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;

		}
	}
	
	/**2014-3-5
	 * @author Sunhaifeng
	 * @param iconids
	 * 根据传入的id容器中装载的图标的id设置图标资源
	 */
	public void setImageIcon(List<Object> iconids) {
		//(Integer) iconIds.get(location)涉及到Java集合容器对于类类型（Integer）到基本数据类型（int）的自动解包知识
		twodimen.setImageResource((Integer) iconIds.get(0));
		about.setImageResource((Integer) iconIds.get(1));
		culture.setImageResource((Integer) iconIds.get(2));
		history.setImageResource((Integer) iconIds.get(3));
		folk.setImageResource((Integer) iconIds.get(4));
		food.setImageResource((Integer) iconIds.get(5));
		speciality.setImageResource((Integer) iconIds.get(6));
		legend.setImageResource((Integer) iconIds.get(7));
		convenient.setImageResource((Integer) iconIds.get(8));
		transportation.setImageResource((Integer) iconIds.get(9));
		tourism.setImageResource((Integer) iconIds.get(10));
		camera.setImageResource((Integer) iconIds.get(11));
	}

	// 启动camera
	@SuppressLint("SimpleDateFormat")
	public void goToCamera() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
		date = new Date();
		str = format.format(date);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), str + ".jpg")));
		startActivityForResult(intent, CAMERA);
		overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
	}

	// 启动二维码扫描
	public void goToTwoDimen() {
		Intent openCameraIntent = new Intent(MainActivity.this,
				CaptureActivity.class);
		startActivityForResult(openCameraIntent, TWODIMEN);
		overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
	}

	// “摇一摇”动态改变背景的线程
	@SuppressLint("HandlerLeak")
	public Handler bgChangeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Resources r = getResources();
			switch (flag) {
			case 0:
				rel.setBackgroundDrawable(r.getDrawable(R.drawable.bg01));
				break;
			case 1:
				rel.setBackgroundDrawable(r
						.getDrawable(R.drawable.bg02));
				break;
			case 2:
				rel.setBackgroundDrawable(r
						.getDrawable(R.drawable.bg03));
				break;
			case 3:
				rel.setBackgroundDrawable(r
						.getDrawable(R.drawable.bg04));
				break;
			}
		}

	};

	private Thread thread;
	// 用来获得天气信息的线程，异步更新天气预报UI
	@SuppressLint("HandlerLeak")
	private Handler weatherhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				JSONObject weather = (JSONObject) msg.obj;
				refreshWeatherUI(weather);
				try {
					thread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
	};

	/**
	 * 2014-2-12
	 * @author sunhaifeng
	 * @param url
	 * 处理获取天气信息的请求，启动线程，进行http操作，获取Json信息
	 */
	public void requestWeatherInfo(final String url) {

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				HttpClient client = new DefaultHttpClient();
				HttpGet httpget = new HttpGet(url);
				HttpResponse response;
				String sbuf = null;
				try {
					response = client.execute(httpget);
					int res = response.getStatusLine().getStatusCode();
					System.out.println(res);
					HttpEntity httpentity = response.getEntity();
					if (httpentity != null) {
						BufferedReader br = new BufferedReader(
								new InputStreamReader(httpentity.getContent(),
										"utf-8"));
						sbuf = br.readLine();
					}
					System.out.println(sbuf);
					JSONObject object = new JSONObject(sbuf);
					JSONObject data = (JSONObject) object
							.getJSONObject("weatherinfo");

					Message msg = new Message();
					msg.what = 1;
					msg.obj = data;
					weatherhandler.sendMessage(msg);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	/**
	 * @author sunhaifeng
	 * @param url
	 * 
	 */
	public void getweather(String url) {
		requestWeatherInfo(url);
	}

	/**
	 * 2014-2-12
	 * @author sunhaifeng
	 * @param jsonobject
	 * 异步更新天气情况信息
	 */
	public void refreshWeatherUI(JSONObject jsonobject) {

		JSONObject jsonData = jsonobject;
		try {
			// TextView today_text = (TextView) findViewById(R.id.date);
			today_text.setText(jsonData.getString("date_y"));

			TextView city_text = (TextView) findViewById(R.id.city);
			city_text.setText(jsonData.getString("city"));

			TextView today_weather = (TextView) findViewById(R.id.wea);
			today_weather.setText(jsonData.getString("weather1"));

			TextView qiweng_text = (TextView) findViewById(R.id.temperature);
			qiweng_text.setText(jsonData.getString("temp1"));

			TextView week_text = (TextView) findViewById(R.id.weekday);
			week_text.setText(jsonData.getString("week"));

			ImageView image = (ImageView) findViewById(R.id.weather_icon);
			int icon = WeatherIcon.parseIcon(jsonData.getString("img1")
					+ ".gif");
			image.setImageResource(icon);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private class sensorListener implements SensorEventListener {
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSensorChanged(SensorEvent event) {
			int sensorType = event.sensor.getType();
			// values[0]:X轴，values[1]：Y轴，values[2]：Z轴
			float[] values = event.values;
			if (sensorType == Sensor.TYPE_ACCELEROMETER) {

				if ((Math.abs(values[0]) > 14 || Math.abs(values[1]) > 14 || Math
						.abs(values[2]) > 14)) {
					flag++;
					if (flag > 3)
						flag = 0;
					bgChangeHandler.sendEmptyMessage(0);
				}
			}
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
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (isOpen) {
				slideBarHandler.post(mScrollToButton);
			} else
				ExitShowTips.showTips(this);
			break;
		case KeyEvent.KEYCODE_MENU:
			slideBarHandler.post(mScrollToButton);
			break;
		}
		return true;
	}
}