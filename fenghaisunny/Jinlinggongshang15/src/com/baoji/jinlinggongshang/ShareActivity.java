
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
import android.os.CountDownTimer;
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
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
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
import com.baoji.jinlinggongshang.util.VolumeController;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ShareActivity extends Activity implements Callback, ViewFactory,
		OnClickListener, PlatformActionListener, OnTouchListener {
	private static final String FILE_NAME = "/pic.jpg";

	public static final String SDK_SINAWEIBO_UID = "3189087725";
	/** 官方腾讯微博 */
	public static final String SDK_TENCENTWEIBO_UID = "shareSDK";

	protected Handler handler = new Handler(this);
	public static String TEST_IMAGE;
	ImageButton back;
	ImageView btn01;
	ImageView btn02;
	ImageView btn2_0;
	ImageView btn03;
	ImageView btn04;
	ImageView btn4_0;
	ImageView btn05;
	ImageView btn06;
	ImageView btn6_0;
	ImageView btn07;
	ImageView btn08;
	ImageView btn8_0;
	ImageView btn09;
	ImageView btn10;
	ImageView btn10_0;
	ImageView btn11;
	ImageView btn12;
	ImageView btn12_0;

	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	public static String[] imageUrls = new String[] {
			"http://news.xinhuanet.com/video/2008-03/25/xin_032030525110915616363.jpg",
			"http://a3.att.hudong.com/61/74/300001062059131823746316283_950.jpg",
			"http://sp2.yokacdn.com/photos/rss/2009/09/19/1253372560_79709.jpg",
			"http://images.zj.com/ent/news/345618_1.jpg",
			"http://i4.cqnews.net/news/attachement/jpg/site82/2011-08-23/1967186900252773048.jpg",
			"http://lady.iyaxin.com/attachement/jpg/site2/20100803/0019667872c80dc1cc3124.jpg", };

	/**
	 * ImagaSwitcher 的引用
	 */
	private ImageSwitcher mImageSwitcher;
	public RelativeLayout imageBrowser;
	/**
	 * 图片id数组
	 */

	private int currentPosition;
	/**
	 * 按下点的X坐标
	 */
	private float downX;
	/**
	 * 装载点点的容器
	 */
	private LinearLayout linearLayout;
	/**
	 * 点点数组
	 */
	private ImageView[] tips;


	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码

	private LockableHorizontalScrollView scrollView;
	private ImageButton exit;
	private ImageButton category;
	private ImageButton camera_slide;
	private ImageButton twodimen_slide;
	public ImageButton call_slide;
	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;
	String str = null;
	Date date = null;

	ImageView im1;
	ImageView im2;
	ImageView im3;
	ImageView im4;
	ImageView im5;
	ImageView im6;

	LinearLayout lin01;
	LinearLayout lin02;
	LinearLayout lin03;
	LinearLayout lin04;
	LinearLayout lin05;
	LinearLayout lin06;

	private int screenWidth;
	private int screenHeight;
	private boolean isOpen = false;// 判断侧边栏的状态变量
	private final Handler slideBarHandler = new Handler();

	public String image_Urls[] = {
			"http://192.168.2.6:8080/JsonWeb01/images/noodles01.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles02.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles03.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles04.png" };
	public String imagedecs[] = { "高圆圆1", "高圆圆2", "高圆圆3", "高圆圆4", "高圆圆5",
			"高圆圆6" };

	ImageButton shareAll;
	public static String imageUrl;
	public static String dec;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.page_demo);

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

		lin01 = (LinearLayout) findViewById(R.id.lin01);
		lin02 = (LinearLayout) findViewById(R.id.lin02);
		lin03 = (LinearLayout) findViewById(R.id.lin03);
		lin04 = (LinearLayout) findViewById(R.id.lin04);
		lin05 = (LinearLayout) findViewById(R.id.lin05);
		lin06 = (LinearLayout) findViewById(R.id.lin06);


		im1.setOnClickListener(this);
		im2.setOnClickListener(this);
		im3.setOnClickListener(this);
		im4.setOnClickListener(this);
		im5.setOnClickListener(this);
		im6.setOnClickListener(this);

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

		btn2_0 = (ImageView) findViewById(R.id.btn2_0);
		btn4_0 = (ImageView) findViewById(R.id.btn4_0);
		btn6_0 = (ImageView) findViewById(R.id.btn6_0);
		btn8_0 = (ImageView) findViewById(R.id.btn8_0);
		btn10_0 = (ImageView) findViewById(R.id.btn10_0);
		btn12_0 = (ImageView) findViewById(R.id.btn12_0);

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

		btn2_0.setAlpha(130);
		btn4_0.setAlpha(130);
		btn6_0.setAlpha(130);
		btn8_0.setAlpha(130);
		btn10_0.setAlpha(130);
		btn12_0.setAlpha(130);

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

		btn2_0.setOnClickListener(this);
		btn4_0.setOnClickListener(this);
		btn6_0.setOnClickListener(this);
		btn8_0.setOnClickListener(this);
		btn10_0.setOnClickListener(this);
		btn12_0.setOnClickListener(this);

		imageLoader.displayImage(imageUrls[0], im1, options);
		imageLoader.displayImage(imageUrls[1], im2, options);
		imageLoader.displayImage(imageUrls[2], im3, options);
		imageLoader.displayImage(imageUrls[3], im4, options);
		imageLoader.displayImage(imageUrls[4], im5, options);
		imageLoader.displayImage(imageUrls[5], im6, options);

		scrollView = (LockableHorizontalScrollView) findViewById(R.id.ScrollView);

		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);
		listViews = (LinearLayout) findViewById(R.id.list_views);
		cateMenu = (LinearLayout) findViewById(R.id.cate_menu);
		closeMenuLinear = (LinearLayout) findViewById(R.id.close_menu_linear);
		closeMenu = findViewById(R.id.close_menu);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;

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

		// 实例化ImageSwitcher
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		// 设置Factory
		mImageSwitcher.setFactory(this);
		// 设置OnTouchListener，我们通过Touch事件来切换图片
		mImageSwitcher.setOnTouchListener(this);
		// listViews.setOnTouchListener(this);
		// imagebrowser.setOnTouchListener(this);

		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		tips = new ImageView[image_Urls.length];
		for (int i = 0; i < image_Urls.length; i++) {
			ImageView mImageView = new ImageView(this);
			tips[i] = mImageView;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.rightMargin = 3;
			layoutParams.leftMargin = 3;

			mImageView
					.setBackgroundResource(R.drawable.page_indicator_unfocused);
			linearLayout.addView(mImageView, layoutParams);
		}

		mImageSwitcher.setImageResource(R.drawable.guodupic);

		setImageBackground(currentPosition);
		// 实现ImageSwitch自动翻页（先延迟2秒，之后启动计时器，每隔三秒切换一次）
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				new CountDownTimer(90000000, 3000) {
					public void onTick(long millisUntilFinished) {
						if (currentPosition < image_Urls.length - 1) {
							mImageSwitcher.setInAnimation(AnimationUtils
									.loadAnimation(getApplication(),
											R.anim.right_in));
							mImageSwitcher.setOutAnimation(AnimationUtils
									.loadAnimation(getApplication(),
											R.anim.lift_out));
							currentPosition++;
							new Handler().post(loadImage);
							setImageBackground(currentPosition);
						} else if (currentPosition == image_Urls.length - 1) {
							currentPosition = 0;
							new Handler().post(loadImage);
							setImageBackground(currentPosition);
						}
					}

					public void onFinish() {
					}
				}.start();
			}
		}, 2000);

		final Handler handler = new Handler(this);
		new Thread() {
			public void run() {
				initImagePath();
				handler.sendEmptyMessageDelayed(1, 100);
			}
		}.start();
	}

	private void initImagePath() {
		try {
			if (Environment.MEDIA_MOUNTED.equals(Environment
					.getExternalStorageState())
					&& Environment.getExternalStorageDirectory().exists()) {
				TEST_IMAGE = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + FILE_NAME;
			} else {
				TEST_IMAGE = getApplication().getFilesDir().getAbsolutePath()
						+ FILE_NAME;
			}
			File file = new File(TEST_IMAGE);
			if (!file.exists()) {
				file.createNewFile();
				Bitmap pic = BitmapFactory.decodeResource(getResources(),
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

	/**
	 * @author sunhaifeng 另起一个线程，用来加载图片
	 */
	public Runnable loadImage = new Runnable() {

		@Override
		public void run() {
			ImageLoaderUtils.loadImageswicher(image_Urls[currentPosition],
					mImageSwitcher);
		}
	};

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

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

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
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 手指按下的X坐标
			downX = event.getX();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			float lastX = event.getX();
			// 抬起的时候的X坐标大于按下的时候就显示上一张图片
			if (lastX > downX) {
				if (event.getY() < screenHeight / 2 - 20) {
					if (currentPosition > 0) {
						// 设置动画，这里的动画比较简单，不明白的去网上看看相关内容
						mImageSwitcher
								.setInAnimation(AnimationUtils.loadAnimation(
										getApplication(), R.anim.left_in));
						mImageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(getApplication(),
										R.anim.right_out));
						currentPosition--;
						new Handler().post(loadImage);
						setImageBackground(currentPosition);
					}
					/*
					 * else{ Toast.makeText(getApplication(), "已经是第一张",
					 * Toast.LENGTH_SHORT).show(); }
					 */

				}/*
				 * else if (event.getY() > screenHeight / 2 - 20) {
				 * 
				 * slideBarHandler.post(mScrollToButton); }
				 */
			}

			if (lastX < downX) {
				if (event.getY() < screenHeight / 2 - 20) {
					if (currentPosition < image_Urls.length - 1) {
						mImageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(getApplication(),
										R.anim.right_in));
						mImageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(getApplication(),
										R.anim.lift_out));
						currentPosition++;
						new Handler().post(loadImage);
						setImageBackground(currentPosition);
					}
					/*
					 * else{ Toast.makeText(getApplication(), "到了最后一张",
					 * Toast.LENGTH_SHORT).show(); }
					 */

				} /*
				 * else if (event.getY() > screenHeight / 2 - 20) {
				 * 
				 * slideBarHandler.post(mScrollToButton); }
				 */
			}
			return true;
		}

		}

		return false;
	}

	public void networkStatueCheckDownload(int imageIndex) {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null != networkInfo && networkInfo.isConnected()) {

			new DownloadImageTask().execute(imageUrls[imageIndex]);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ShareActivity.this,
							"图片已经下载至sdcard/BAOJIdownload", Toast.LENGTH_SHORT)
							.show();
				}
			}, 2000);
		} else {
			Toast.makeText(ShareActivity.this, "抱歉！未连接网络，无法下载！",
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
	private void showShare(boolean silent, String platform, String imageUrl,
			String dec) {
		final OnekeyShare oks = new OnekeyShare();
		oks.setNotification(R.drawable.ic_launcher,
				getString(R.string.app_name));
		oks.setTitle(getString(R.string.share));
		oks.setTitleUrl(imageUrl);
		oks.setText(dec);
		oks.setImagePath(ShareActivity.TEST_IMAGE);
		oks.setImageUrl(imageUrl);
		oks.setUrl(imageUrl);
		oks.setFilePath(ShareActivity.TEST_IMAGE);
		oks.setComment(getString(R.string.share));
		oks.setSite(getString(R.string.app_name));
		oks.setSiteUrl(imageUrl);
		oks.setVenueName(getString(R.string.app_name));
		oks.setVenueDescription("This is a beautiful place!");
		oks.setLatitude(34.368279f);
		oks.setLongitude(107.097364f);
		oks.setSilent(silent);
		if (platform != null) {
			oks.setPlatform(platform);
		}

		oks.setShareContentCustomizeCallback(new ShareContentCustomizeDemo());

		oks.show(this);
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
		case R.id.im1:
			lin01.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin01.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im2:
			lin02.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					lin02.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im3:
			lin03.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin03.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im4:
			lin04.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin04.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im5:
			lin05.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin05.setVisibility(View.GONE);
				}
			}, 2000);
			break;
		case R.id.im6:
			lin06.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					lin06.setVisibility(View.GONE);
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
			showShare(false, null, imageUrls[0], imagedecs[0]);
			break;
		case R.id.btn03:
			imageBrower(1);
			break;
		case R.id.btn04:
			networkStatueCheckDownload(1);
			break;
		case R.id.btn4_0:
			showShare(false, null, imageUrls[1], imagedecs[1]);
			break;
		case R.id.btn05:
			imageBrower(2);
			break;
		case R.id.btn06:
			networkStatueCheckDownload(2);
			break;
		case R.id.btn6_0:
			showShare(false, null, imageUrls[2], imagedecs[2]);
			break;
		case R.id.btn07:
			imageBrower(3);
			break;
		case R.id.btn08:
			networkStatueCheckDownload(3);
			break;
		case R.id.btn8_0:
			showShare(false, null, imageUrls[3], imagedecs[3]);
			break;
		case R.id.btn09:
			imageBrower(4);
			break;
		case R.id.btn10:
			networkStatueCheckDownload(4);
			break;
		case R.id.btn10_0:
			showShare(false, null, imageUrls[4], imagedecs[4]);
			break;
		case R.id.btn11:
			imageBrower(5);
			break;
		case R.id.btn12:
			networkStatueCheckDownload(5);
			break;
		case R.id.btn12_0:
			showShare(false, null, imageUrls[5], imagedecs[5]);
			break;

		default: {
			// 分享到具体的平台
			Object tag = v.getTag();
			if (tag != null) {
				showShare(false, ((Platform) tag).getName(), imageUrl, dec);
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
		String text = ShareActivity.actionToString(msg.arg2);
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
