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
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;

@SuppressLint("SimpleDateFormat")
public class TourismActivity extends Activity implements OnClickListener {


	public boolean isPlaying_internet = false;
	public boolean isPlaying_local = false;


	public String playdir = "";

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码
	
	private LockableHorizontalScrollView scrollView;
	private ImageButton exit,back,category,camera_slide,twodimen_slide,call_slide, theme,tuijian;
	private LinearLayout listViews, cateMenu, closeMenuLinear;
	private View closeMenu;
	
	private RelativeLayout famensi,honghegu,yuntaishan,jialingjiang,wuzhangyuan,zhougongmiao,fengxian,guanshan,jiangziya;


	
	String str = null;
	Date date = null;

	private int screenWidth;
	private boolean isOpen = false;
	private final Handler slideBarHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tourism);
		MyActivityManager.getInstance().addActivity(this);

		scrollView = (LockableHorizontalScrollView) this
				.findViewById(R.id.ScrollView);
		famensi = (RelativeLayout)findViewById(R.id.famensi);
		famensi.setOnClickListener(this);
		honghegu = (RelativeLayout)findViewById(R.id.honghegu);
		honghegu.setOnClickListener(this);
		yuntaishan = (RelativeLayout)findViewById(R.id.yuntaishan);
		yuntaishan.setOnClickListener(this);
		jialingjiang = (RelativeLayout)findViewById(R.id.jialingjiang);
		jialingjiang.setOnClickListener(this);
		wuzhangyuan = (RelativeLayout)findViewById(R.id.wuzhangyuan);
		wuzhangyuan.setOnClickListener(this);
		fengxian = (RelativeLayout)findViewById(R.id.fengxian);
		fengxian.setOnClickListener(this);
		zhougongmiao = (RelativeLayout)findViewById(R.id.zhougongmiao);
		zhougongmiao.setOnClickListener(this);
		guanshan = (RelativeLayout)findViewById(R.id.guanshan);
		guanshan.setOnClickListener(this);
		jiangziya = (RelativeLayout)findViewById(R.id.jiangziya);
		jiangziya.setOnClickListener(this);
		tuijian = (ImageButton)findViewById(R.id.tuijian);
		tuijian.setVisibility(View.VISIBLE);
		tuijian.setOnClickListener(this);
		
		
		
		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton)findViewById(R.id.call_sli);
		theme = (ImageButton)findViewById(R.id.theme);
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
		case R.id.tuijian:
			Intent tuijianintent = new Intent(TourismActivity.this,TourismTuijianActivity.class);
			startActivity(tuijianintent);
			overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
			break;			
		case R.id.exit:
			ExitShowTips.showTips(this);
			break;
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			break;
		case R.id.twodimen_sli:
			Intent twodimen = new Intent(TourismActivity.this,
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
			Intent callintent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:")); 
			startActivity(callintent);
			overridePendingTransition(R.anim.rotate_fade_in, R.anim.rotate_fade_out);
			break;
		case R.id.theme:
			Intent themeintent = new Intent(this,
					ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;			
		case R.id.famensi:
			Intent famensiintent = new Intent();
			famensiintent.setClass(TourismActivity.this, FamensiActivity.class);
			startActivity(famensiintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.honghegu:
			Intent hongheguintent = new Intent();
			hongheguintent.setClass(TourismActivity.this, HongheguActivity.class);
			startActivity(hongheguintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.yuntaishan:
			Intent yuntaishanintent = new Intent();
			yuntaishanintent.setClass(TourismActivity.this, YuntaishanActivity.class);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			startActivity(yuntaishanintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.jialingjiang:
			Intent jialingjiangintent = new Intent();
			jialingjiangintent.setClass(TourismActivity.this, JialingjiangActivity.class);
			startActivity(jialingjiangintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.wuzhangyuan:
			Intent wuzhangyuanintent = new Intent();
			wuzhangyuanintent.setClass(TourismActivity.this, WuzhangyuanActivity.class);
			startActivity(wuzhangyuanintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.zhougongmiao:
			Intent zhougongmiaointent = new Intent();
			zhougongmiaointent.setClass(TourismActivity.this, ZhougongmiaoActivity.class);
			startActivity(zhougongmiaointent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;	
		case R.id.fengxian:
			Intent fengxianintent = new Intent();
			fengxianintent.setClass(TourismActivity.this, FengxianActivity.class);
			startActivity(fengxianintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.guanshan:
			Intent guanshanintent = new Intent();
			guanshanintent.setClass(TourismActivity.this, GuanshanActivity.class);
			startActivity(guanshanintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.jiangziya:
			Intent jiangziyaintent = new Intent();
			jiangziyaintent.setClass(TourismActivity.this, JiangziyaActivity.class);
			startActivity(jiangziyaintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;				
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if(data ==null){
			return;
		}
		if (requestCode == CAMERA) {
			// 设置文件保存路径这里放在根目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/" + str + ".jpg");
			Toast.makeText(this, "拍摄的照片将放在SD卡根目录哦亲！",
					Toast.LENGTH_SHORT).show();
		}else if(requestCode == TWODIMEN){
			Intent intent = new Intent(this, TransportationActivity.class);
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			intent.putExtra("result", scanResult);
			intent.putExtra("callMap", true);
			startActivity(intent);
			overridePendingTransition(R.anim.rotate_fade_in, R.anim.rotate_fade_out);
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
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
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

