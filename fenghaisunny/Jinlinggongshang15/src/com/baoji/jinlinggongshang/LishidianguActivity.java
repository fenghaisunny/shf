package com.baoji.jinlinggongshang;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.ImageLoaderUtils;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;

@SuppressLint("SimpleDateFormat")
public class LishidianguActivity extends Activity implements OnClickListener {

	public String playdir = "";

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码

	public ImageView mugong, wenwang, anduchencang, zhougong, jinji;
	private LockableHorizontalScrollView scrollView;
	private ImageButton exit, back, category, camera_slide, twodimen_slide,
			call_slide,theme;

	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;
	String str = null;
	Date date = null;

	private int screenWidth;
	private boolean isOpen = false;
	private final Handler slideBarHandler = new Handler();
	private final Handler mediaPlay = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lishidiangu);
		MyActivityManager.getInstance().addActivity(this);

		ImageLoaderUtils.imageLoderIni(this);

		scrollView = (LockableHorizontalScrollView) this
				.findViewById(R.id.ScrollView);

		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);
		theme = (ImageButton)findViewById(R.id.theme);

		mugong = (ImageView) findViewById(R.id.mugong);
		wenwang = (ImageView) findViewById(R.id.wenwang);
		anduchencang = (ImageView) findViewById(R.id.anduchencang);
		zhougong = (ImageView) findViewById(R.id.zhougong);
		jinji = (ImageView) findViewById(R.id.jinji);

		ImageLoaderUtils.imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/ic_wenwang.png",
				wenwang, ImageLoaderUtils.options);
		ImageLoaderUtils.imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/ic_mugong.png",
				mugong, ImageLoaderUtils.options);
		ImageLoaderUtils.imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/ic_anduchencang.png",
				anduchencang, ImageLoaderUtils.options);
		ImageLoaderUtils.imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/ic_zhouli.png",
				zhougong, ImageLoaderUtils.options);
		ImageLoaderUtils.imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/ic_jinji.png",
				jinji, ImageLoaderUtils.options);

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
		
		mugong.setOnClickListener(this);
		wenwang.setOnClickListener(this);
		zhougong.setOnClickListener(this);
		anduchencang.setOnClickListener(this);
		jinji.setOnClickListener(this);

	}

	/**
	 * @author sunhaifeng 另起一个线程，用来加载图片
	 */
	/*
	 * public Runnable loadImage = new Runnable() {
	 * 
	 * @Override public void run() {
	 * ImageLoaderUtils.loadImageswicher(imageUrls[
	 * currentPosition],mImageSwitcher); } };
	 */

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

			Intent twodimen = new Intent(LishidianguActivity.this,
					CaptureActivity.class);
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
			break;
		case R.id.call_sli:
			Intent callintent = new Intent(Intent.ACTION_DIAL,
					Uri.parse("tel:"));
			startActivity(callintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.theme:
			Intent themeintent = new Intent(this,ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.mugong:
			Intent mugongintent = new Intent(this,MugongActivity.class);
			startActivity(mugongintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.wenwang:
			Intent wenwangintent = new Intent(this,WenwangActivity.class);
			startActivity(wenwangintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.anduchencang:
			Intent anduchencangintent = new Intent(this,AnduchencangActivity.class);
			startActivity(anduchencangintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;
		case R.id.zhougong:
			Intent zhougongintent = new Intent(this,ZhouliActivity.class);
			startActivity(zhougongintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;	
		case R.id.jinji:
			Intent jinjiintent = new Intent(this,JinjiActivity.class);
			startActivity(jinjiintent);
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
