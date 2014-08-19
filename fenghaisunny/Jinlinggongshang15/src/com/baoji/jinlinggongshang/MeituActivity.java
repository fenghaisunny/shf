package com.baoji.jinlinggongshang;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;

import com.baoji.jinlinggongshang.sharecontent.OnekeyShare;
import com.baoji.jinlinggongshang.sharecontent.ShareContentCustomizeDemo;
import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.FileUtils;
import com.baoji.jinlinggongshang.util.HttpUtils;
import com.baoji.jinlinggongshang.util.ImageLoaderUtils;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


public class MeituActivity extends Activity implements Callback, ViewFactory,
		OnClickListener, PlatformActionListener, OnTouchListener {
	
	
	private static final String FILE_NAME = "/pic01.jpg";

	public static final String SDK_SINAWEIBO_UID = "3189087725";
	/** 官方腾讯微博 */
	public static final String SDK_TENCENTWEIBO_UID = "shareSDK";

	protected Handler handler = new Handler(this);
	public static String TEST_IMAGE;
	ImageButton back;

	ImageView btn01, btn02, btn2_0, btn03, btn04, btn4_0, btn05, btn06, btn6_0,
			btn07, btn08, btn8_0, btn09, btn10, btn10_0, btn11, btn12, btn12_0,
			btn13, btn14, btn14_0, btn15, btn16, btn16_0, btn17, btn18,
			btn18_0, btn19, btn20, btn20_0, btn21, btn22, btn22_0, btn23,
			btn24, btn24_0, im1, im2, im3, im4, im5, im6, im7, im8, im9, im10,
			im11, im12;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	public static String[] imageUrls = new String[] {
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensiH.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensiH01.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensiH02.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensiH03.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensi.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensi01.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensi02.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_famensi03.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_fengxian.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_shigushanH.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_yuntaishan.png",
			"http://192.168.2.6:8080/JsonWeb01/images/meitu_yuntaishan01.png" };

	public static String[] small_imageUrls = new String[] {
			"http://192.168.2.6:8080/JsonWeb01/images/famensi_small.png",
			"http://192.168.2.6:8080/JsonWeb01/images/famensi_small01.png",
			"http://192.168.2.6:8080/JsonWeb01/images/famensi_small02.png",
			"http://192.168.2.6:8080/JsonWeb01/images/famensi_small03.png",
			"http://192.168.2.6:8080/JsonWeb01/images/shigushan_small.png" };

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码

	private LockableHorizontalScrollView scrollView;
	private ImageButton exit, category, camera_slide, twodimen_slide,
			call_slide,favourite_slide, theme;
	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;
	String str = null;
	Date date = null;

	LinearLayout lin01, lin02, lin03, lin04, lin05, lin06, lin07, lin08, lin09,
			lin10, lin11, lin12;

	private int screenWidth;
	private boolean isOpen = false;// 判断侧边栏的状态变量
	private final Handler slideBarHandler = new Handler();

	ImageButton shareAll;
	public static String imageUrl;
	public static String dec;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.meitu);
		MyActivityManager.getInstance().addActivity(this);

		back = (ImageButton) findViewById(R.id.back);

		ShareSDK.initSDK(this);
		// 去除注释，可以即可使用应用后台配置的应用信息，否则默认使用ShareSDK.conf中的信息
		// ShareSDK.setNetworkDevInfoEnable(true);

		ImageLoaderUtils.imageLoderIni(this);
		imageLoader = ImageLoaderUtils.imageLoader;
		options = ImageLoaderUtils.options;

		im1 = (ImageView) findViewById(R.id.im1);
		im2 = (ImageView) findViewById(R.id.im2);
		im3 = (ImageView) findViewById(R.id.im3);
		im4 = (ImageView) findViewById(R.id.im4);
		im5 = (ImageView) findViewById(R.id.im5);
		im6 = (ImageView) findViewById(R.id.im6);

		im7 = (ImageView) findViewById(R.id.im7);
		im8 = (ImageView) findViewById(R.id.im8);
		im9 = (ImageView) findViewById(R.id.im9);
		im10 = (ImageView) findViewById(R.id.im10);
		im11 = (ImageView) findViewById(R.id.im11);
		im12 = (ImageView) findViewById(R.id.im12);

		lin01 = (LinearLayout) findViewById(R.id.lin01);
		lin02 = (LinearLayout) findViewById(R.id.lin02);
		lin03 = (LinearLayout) findViewById(R.id.lin03);
		lin04 = (LinearLayout) findViewById(R.id.lin04);
		lin05 = (LinearLayout) findViewById(R.id.lin05);
		lin06 = (LinearLayout) findViewById(R.id.lin06);
		lin07 = (LinearLayout) findViewById(R.id.lin07);
		lin08 = (LinearLayout) findViewById(R.id.lin08);
		lin09 = (LinearLayout) findViewById(R.id.lin09);
		lin10 = (LinearLayout) findViewById(R.id.lin10);
		lin11 = (LinearLayout) findViewById(R.id.lin11);
		lin12 = (LinearLayout) findViewById(R.id.lin12);

		im1.setOnClickListener(this);
		im2.setOnClickListener(this);
		im3.setOnClickListener(this);
		im4.setOnClickListener(this);
		im5.setOnClickListener(this);
		im6.setOnClickListener(this);
		im7.setOnClickListener(this);
		im8.setOnClickListener(this);
		im9.setOnClickListener(this);
		im10.setOnClickListener(this);
		im11.setOnClickListener(this);
		im12.setOnClickListener(this);

		btn01 = (ImageView) findViewById(R.id.btn01);
		btn02 = (ImageView) findViewById(R.id.btn02);
		btn03 = (ImageView) findViewById(R.id.btn03);
		btn04 = (ImageView) findViewById(R.id.btn04);
		btn05 = (ImageView) findViewById(R.id.btn05);
		btn06 = (ImageView) findViewById(R.id.btn06);
		btn07 = (ImageView) findViewById(R.id.btn07);
		btn08 = (ImageView) findViewById(R.id.btn08);
		btn09 = (ImageView) findViewById(R.id.btn09);
		btn10 = (ImageView) findViewById(R.id.btn10);
		btn11 = (ImageView) findViewById(R.id.btn11);
		btn12 = (ImageView) findViewById(R.id.btn12);

		btn13 = (ImageView) findViewById(R.id.btn13);
		btn14 = (ImageView) findViewById(R.id.btn14);
		btn15 = (ImageView) findViewById(R.id.btn15);
		btn16 = (ImageView) findViewById(R.id.btn16);
		btn17 = (ImageView) findViewById(R.id.btn17);
		btn18 = (ImageView) findViewById(R.id.btn18);
		btn19 = (ImageView) findViewById(R.id.btn19);
		btn20 = (ImageView) findViewById(R.id.btn20);
		btn21 = (ImageView) findViewById(R.id.btn21);
		btn22 = (ImageView) findViewById(R.id.btn22);
		btn23 = (ImageView) findViewById(R.id.btn23);
		btn24 = (ImageView) findViewById(R.id.btn24);

		btn2_0 = (ImageView) findViewById(R.id.btn2_0);
		btn4_0 = (ImageView) findViewById(R.id.btn4_0);
		btn6_0 = (ImageView) findViewById(R.id.btn6_0);
		btn8_0 = (ImageView) findViewById(R.id.btn8_0);
		btn10_0 = (ImageView) findViewById(R.id.btn10_0);
		btn12_0 = (ImageView) findViewById(R.id.btn12_0);
		btn14_0 = (ImageView) findViewById(R.id.btn14_0);
		btn16_0 = (ImageView) findViewById(R.id.btn16_0);
		btn18_0 = (ImageView) findViewById(R.id.btn18_0);
		btn20_0 = (ImageView) findViewById(R.id.btn20_0);
		btn22_0 = (ImageView) findViewById(R.id.btn22_0);
		btn24_0 = (ImageView) findViewById(R.id.btn24_0);

		btn01.setAlpha(130);
		btn02.setAlpha(130);
		btn03.setAlpha(130);
		btn04.setAlpha(130);
		btn05.setAlpha(130);
		btn06.setAlpha(130);
		btn07.setAlpha(130);
		btn08.setAlpha(130);
		btn09.setAlpha(130);
		btn10.setAlpha(130);
		btn11.setAlpha(130);
		btn12.setAlpha(130);
		btn13.setAlpha(130);
		btn14.setAlpha(130);
		btn15.setAlpha(130);
		btn16.setAlpha(130);
		btn17.setAlpha(130);
		btn18.setAlpha(130);
		btn19.setAlpha(130);
		btn20.setAlpha(130);
		btn21.setAlpha(130);
		btn22.setAlpha(130);
		btn23.setAlpha(130);
		btn24.setAlpha(130);

		btn2_0.setAlpha(130);
		btn4_0.setAlpha(130);
		btn6_0.setAlpha(130);
		btn8_0.setAlpha(130);
		btn10_0.setAlpha(130);
		btn12_0.setAlpha(130);
		btn14_0.setAlpha(130);
		btn16_0.setAlpha(130);
		btn18_0.setAlpha(130);
		btn20_0.setAlpha(130);
		btn22_0.setAlpha(130);
		btn24_0.setAlpha(130);

		btn01.setOnClickListener(this);
		btn02.setOnClickListener(this);
		btn03.setOnClickListener(this);
		btn04.setOnClickListener(this);
		btn05.setOnClickListener(this);
		btn06.setOnClickListener(this);

		btn07.setOnClickListener(this);
		btn08.setOnClickListener(this);
		btn09.setOnClickListener(this);
		btn10.setOnClickListener(this);
		btn11.setOnClickListener(this);
		btn12.setOnClickListener(this);

		btn13.setOnClickListener(this);
		btn14.setOnClickListener(this);
		btn15.setOnClickListener(this);
		btn16.setOnClickListener(this);
		btn17.setOnClickListener(this);
		btn18.setOnClickListener(this);

		btn19.setOnClickListener(this);
		btn20.setOnClickListener(this);
		btn21.setOnClickListener(this);
		btn22.setOnClickListener(this);
		btn23.setOnClickListener(this);
		btn24.setOnClickListener(this);

		btn2_0.setOnClickListener(this);
		btn4_0.setOnClickListener(this);
		btn6_0.setOnClickListener(this);
		btn8_0.setOnClickListener(this);
		btn10_0.setOnClickListener(this);
		btn12_0.setOnClickListener(this);

		btn14_0.setOnClickListener(this);
		btn16_0.setOnClickListener(this);
		btn18_0.setOnClickListener(this);
		btn20_0.setOnClickListener(this);
		btn22_0.setOnClickListener(this);
		btn24_0.setOnClickListener(this);

		imageLoader.displayImage(small_imageUrls[0], im1, options);
		imageLoader.displayImage(small_imageUrls[1], im2, options);
		imageLoader.displayImage(small_imageUrls[2], im3, options);
		imageLoader.displayImage(small_imageUrls[3], im4, options);
		imageLoader.displayImage(imageUrls[4], im5, options);
		imageLoader.displayImage(imageUrls[5], im6, options);
		imageLoader.displayImage(imageUrls[6], im7, options);
		imageLoader.displayImage(imageUrls[7], im8, options);
		imageLoader.displayImage(imageUrls[8], im9, options);
		imageLoader.displayImage(small_imageUrls[4], im10, options);
		imageLoader.displayImage(imageUrls[10], im11, options);
		imageLoader.displayImage(imageUrls[11], im12, options);

		scrollView = (LockableHorizontalScrollView) findViewById(R.id.ScrollView);

		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);
		theme = (ImageButton) findViewById(R.id.theme);
		listViews = (LinearLayout) findViewById(R.id.list_views);
		cateMenu = (LinearLayout) findViewById(R.id.cate_menu);
		closeMenuLinear = (LinearLayout) findViewById(R.id.close_menu_linear);
		closeMenu = findViewById(R.id.close_menu);

		TextView title = (TextView) findViewById(R.id.titleBar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("宝鸡美图");

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

		final Handler handler = new Handler(this);
		new Thread() {
			public void run() {
				initImagePath(MeituActivity.this);
				handler.sendEmptyMessageDelayed(1, 100);
			}
		}.start();

	}

	public static void initImagePath(Context c) {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = c.getFilesDir().getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(c.getResources(),
						R.drawable.pic01);
				FileOutputStream fos = new FileOutputStream(file);
				pic.compress(CompressFormat.JPEG, 100, fos);
				fos.flush();
				fos.close();
			}
		} catch (Throwable t) {
			t.printStackTrace();
			TEST_IMAGE = null;
		}
	}

	/*
	 * public boolean handleMessage(Message msg) { //
	 * menu.triggerItem(MainAdapter.GROUP_DEMO, MainAdapter.ITEM_DEMO); return
	 * false; }
	 */

	/** 屏幕旋转后，此方法会被调用，以刷新侧栏的布局 */
	/*
	 * public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig); if (orientation !=
	 * newConfig.orientation) { orientation = newConfig.orientation;
	 * menu.refresh(); } }
	 */

	private void imageBrower(int position) {
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, imageUrls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}

	private Runnable mScrollToButton = new Runnable() {

		@Override
		public void run() {
			if (!isOpen) {
				scrollView.smoothScrollTo(screenWidth / 6 + 15, 0);
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
	public View makeView() {
		final ImageView i = new ImageView(this);
		i.setBackgroundColor(0xff000000);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return true;
	}

	public void networkStatueCheckDownload(int imageIndex) {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isConnected()) {

			new DownloadImageTask().execute(imageUrls[imageIndex]);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(MeituActivity.this,
							"图片已经下载至sdcard/BAOJIdownload", Toast.LENGTH_SHORT)
							.show();
				}
			}, 2000);
		} else {
			Toast.makeText(MeituActivity.this, "抱歉！未连接网络，无法下载！",
					Toast.LENGTH_SHORT).show();
		}

	}

	private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			String path = params[0];
			InputStream inputStream = HttpUtils.getInputStream(path);
			// 从输入流得到位图
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
			date = new Date();
			str = format.format(date);
			// 将图像存储到SD卡
			FileUtils.saveToSDCard(bitmap, "BAOJIdownload", str + ".jpg");
			return bitmap;
		}
	}

	protected View initPage() {
		return LayoutInflater.from(this).inflate(R.layout.page_demo, null);
	}

	// 使用快捷分享完成分享（请务必仔细阅读位于SDK解压目录下Docs文件夹中OnekeyShare类的JavaDoc）
	public static void showShare(Context c,boolean silent, String platform, String imageUrl,
			String dec) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,
				c.getString(R.string.app_name));
		oks.setTitle(c.getString(R.string.share));
		oks.setTitleUrl(imageUrl);
		oks.setText(dec);
		oks.setImagePath(MeituActivity.TEST_IMAGE);
		oks.setImageUrl(imageUrl);
		oks.setUrl(imageUrl);
		oks.setFilePath(MeituActivity.TEST_IMAGE);
		oks.setComment(c.getString(R.string.share));
		oks.setSite(c.getString(R.string.app_name));
		oks.setSiteUrl(imageUrl);
		oks.setVenueName(c.getString(R.string.app_name));
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(34.368279f);
		oks.setLongitude(107.097364f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}
		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());
		oks.show(c);
	}

	/** 操作演示的代码集中于此方法 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.category:
			slideBarHandler.post(mScrollToButton);
			break;
		case R.id.exit:
			ExitShowTips.showTips(this);
			break;
		case R.id.twodimen_sli:
			Intent twodimen = new Intent(this, CaptureActivity.class);
			startActivityForResult(twodimen, TWODIMEN);
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
			break;

		case R.id.theme:
			Intent themeintent = new Intent(this, ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.im1:
			MainActivity.showToast(this, "法门寺");
			lin01.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin01.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im2:
			MainActivity.showToast(this, "法门寺");
			lin02.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					lin02.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im3:
			MainActivity.showToast(this, "法门寺");
			lin03.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin03.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im4:
			MainActivity.showToast(this, "法门寺");
			lin04.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin04.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im5:
			MainActivity.showToast(this, "法门寺");
			lin05.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin05.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im6:
			MainActivity.showToast(this, "法门寺");
			lin06.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin06.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im7:
			MainActivity.showToast(this, "法门寺");
			lin07.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin07.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im8:
			MainActivity.showToast(this, "法门寺");
			lin08.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					lin08.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im9:
			MainActivity.showToast(this, "凤县");
			lin09.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin09.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im10:
			MainActivity.showToast(this, "石鼓山");
			lin10.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin10.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im11:
			MainActivity.showToast(this, "云台山");
			lin11.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin11.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im12:
			MainActivity.showToast(this, "云台山");
			lin12.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin12.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.btn01:
			imageBrower(0);
			break;
		case R.id.btn02:
			networkStatueCheckDownload(0);
			break;
		case R.id.btn2_0:
			showShare(this,false, null, imageUrls[0], "法门寺");
			break;
		case R.id.btn03:
			imageBrower(1);
			break;
		case R.id.btn04:
			networkStatueCheckDownload(1);
			break;
		case R.id.btn4_0:
			showShare(this,false, null, imageUrls[1],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn05:
			imageBrower(2);
			break;
		case R.id.btn06:
			networkStatueCheckDownload(2);
			break;
		case R.id.btn6_0:
			showShare(this,false, null, imageUrls[2],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn07:
			imageBrower(3);
			break;
		case R.id.btn08:
			networkStatueCheckDownload(3);
			break;
		case R.id.btn8_0:
			showShare(this,false, null, imageUrls[3],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn09:
			imageBrower(4);
			break;
		case R.id.btn10:
			networkStatueCheckDownload(4);
			break;
		case R.id.btn10_0:
			showShare(this,false, null, imageUrls[4],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn11:
			imageBrower(5);
			break;
		case R.id.btn12:
			networkStatueCheckDownload(5);
			break;
		case R.id.btn12_0:
			showShare(this,false, null, imageUrls[5],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn13:
			imageBrower(6);
			break;
		case R.id.btn14:
			networkStatueCheckDownload(6);
			break;
		case R.id.btn14_0:
			showShare(this,false, null, imageUrls[6],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn15:
			imageBrower(7);
			break;
		case R.id.btn16:
			networkStatueCheckDownload(7);
			break;
		case R.id.btn16_0:
			showShare(this,false, null, imageUrls[7],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn17:
			imageBrower(8);
			break;
		case R.id.btn18:
			networkStatueCheckDownload(8);
			break;
		case R.id.btn18_0:
			showShare(this,false, null, imageUrls[8],
					this.getString(R.string.fengxiandec));
			break;
		case R.id.btn19:
			imageBrower(9);
			break;
		case R.id.btn20:
			networkStatueCheckDownload(9);
			break;
		case R.id.btn20_0:
			showShare(this,false, null, imageUrls[9],
					this.getString(R.string.shigudec));
			break;
		case R.id.btn21:
			imageBrower(10);
			break;
		case R.id.btn22:
			networkStatueCheckDownload(10);
			break;
		case R.id.btn22_0:
			showShare(this,false, null, imageUrls[10],
					this.getString(R.string.famensidec));
			break;
		case R.id.btn23:
			imageBrower(11);
			break;
		case R.id.btn24:
			networkStatueCheckDownload(11);
			break;
		case R.id.btn24_0:
			showShare(this,false, null, imageUrls[11],
					this.getString(R.string.famensidec));
			break;

		default: {
			// 分享到具体的平台
			Object tag = v.getTag();
			if (tag != null) {
				showShare(this,false, ((Platform) tag).getName(), imageUrl, dec);
			}
		}
			break;
		}
	}

	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {

		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onCancel(Platform palt, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = palt;
		handler.sendMessage(msg);
	}

	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		handler.sendMessage(msg);
	}

	/** 处理操作结果 */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = MeituActivity.actionToString(msg.arg2);
		switch (msg.arg1) {
		case 1: {
			// 成功
			text = plat.getName() + " completed at " + text;
		}
			break;
		case 2: {
			// 失败
			text = plat.getName() + " caught error at " + text;
		}
			break;
		case 3: {
			// 取消
			text = plat.getName() + " canceled at " + text;
		}
			break;
		}
		// Toast.makeText(ShareActivity.this, text, Toast.LENGTH_SHORT).show();
		return false;
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
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			if (scanResult.startsWith("http://")
					|| scanResult.startsWith("www")
					|| scanResult.endsWith(".com") || scanResult.contains(".")
					|| scanResult.contains("/")) {
				Intent intent = new Intent(this, TwoDimenWeb.class);
				intent.putExtra("result", scanResult);
				startActivity(intent);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
			} else {
				Intent intent = new Intent(this, TransportationActivity.class);
				intent.putExtra("result", scanResult);
				intent.putExtra("callMap", true);
				startActivity(intent);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
			}

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

	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	/** 将action转换为String */
	public static String actionToString(int action) {
		switch (action) {
		case Platform.ACTION_AUTHORIZING:
			return "ACTION_AUTHORIZING";
		case Platform.ACTION_GETTING_FRIEND_LIST:
			return "ACTION_GETTING_FRIEND_LIST";
		case Platform.ACTION_FOLLOWING_USER:
			return "ACTION_FOLLOWING_USER";
		case Platform.ACTION_SENDING_DIRECT_MESSAGE:
			return "ACTION_SENDING_DIRECT_MESSAGE";
		case Platform.ACTION_TIMELINE:
			return "ACTION_TIMELINE";
		case Platform.ACTION_USER_INFOR:
			return "ACTION_USER_INFOR";
		case Platform.ACTION_SHARE:
			return "ACTION_SHARE";
		default: {
			return "UNKNOWN";
		}
		}
	}

}
