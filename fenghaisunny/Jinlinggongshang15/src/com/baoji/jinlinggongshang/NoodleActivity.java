package com.baoji.jinlinggongshang;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.baoji.jinlinggongshang.DB.DBHelper;
import com.baoji.jinlinggongshang.DB.Datarecords.Datarecord;
import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.ImageLoaderUtils;
import com.baoji.jinlinggongshang.util.LockableHorizontalScrollView;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NoodleActivity extends Activity implements ViewFactory,
		OnClickListener, OnTouchListener, OnErrorListener,
		OnCompletionListener, OnBufferingUpdateListener, OnPreparedListener {

	public static final int CAMERA = -2;// 拍照
	public static final int TWODIMEN = 2;// 二维码
	public TextView play;
	public String musicUrl = "http://192.168.2.6:8080/JsonWeb01/music/01.mp3";
	TextView bufferValueTextView;
	
	public LinearLayout yuleluxian;

	MediaPlayer mediaPlayer01;
	Button stopButton01, startButton01;
	public RelativeLayout getBuffer;
	public int currentVol;

	public ImageLoader imageLoader;
	public DisplayImageOptions options;

	public boolean isMp3Complete = false;
	public boolean isBuffering = false;
	public boolean isPlaying_internet = false;
	public String playBtnTitle = "语音播报";

	/**
	 * ImagaSwitcher 的引用
	 */
	public ImageSwitcher mImageSwitcher;
	/**
	 * 图片id数组
	 */
	public int currentPosition;
	/**
	 * 按下点的X坐标
	 */
	public float downX;
	/**
	 * 装载点点的容器
	 */
	public LinearLayout linearLayout;
	/**
	 * 点点数组
	 */
	public ImageView[] tips;

	public String playdir = "";

	public LockableHorizontalScrollView scrollView;

	public ImageView folkpic,yule,luxian,canyin;
	public TextView folktitle,folkdecs;
	public ImageButton exit,back,category,camera_slide,twodimen_slide,call_slide,theme;
	public LinearLayout listViews, cateMenu, closeMenuLinear;
	public View closeMenu;
	String str = null;
	Date date = null;

	public int screenHeight;
	public int screenWidth;
	public boolean isOpen = false;
	public final Handler slideBarHandler = new Handler();
	public final Handler mediaPlay = new Handler();

	DBHelper helper = new DBHelper(this);
	ContentResolver rp = null;
	Uri uri = Datarecord.CONTENT_URI;
	public String imageUrls[] = {
			"http://192.168.2.6:8080/JsonWeb01/images/noodles01.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles02.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles03.png",
			"http://192.168.2.6:8080/JsonWeb01/images/noodles04.png" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cul_folkdetail);
		MyActivityManager.getInstance().addActivity(this);

		ImageLoaderUtils.imageLoderIni(this);
		imageLoader = ImageLoaderUtils.imageLoader;
		options = ImageLoaderUtils.options;

/*		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
				.detectDiskReads().detectDiskWrites().detectNetwork()
				.penaltyLog().build());*/

		rp = getContentResolver();

		/*
		 * ContentValues values = new ContentValues();
		 * values.put(Datarecord._ID, 101); values.put(Datarecord.NAME,
		 * "miantiao"); rp.insert(uri, values);
		 * 
		 * values.put(Datarecord._ID, 102); values.put(Datarecord.NAME,
		 * "guokui"); rp.insert(uri, values);
		 */

/*		Cursor c = rp.query(uri,
				new String[] { Datarecord._ID, Datarecord.TITLE },
				Datarecord.TITLE + "=lovename", null, null);
		while (c.moveToNext()) {
			String lc = c.getString(c.getColumnIndex(Datarecord.TITLE));
			System.out.println(lc);
		}
*/
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		currentVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		play = (TextView) findViewById(R.id.play);
		play.setText(playBtnTitle);
		folkpic = (ImageView) findViewById(R.id.folk_pic);
		folktitle = (TextView) findViewById(R.id.folk_titles);
		folkdecs = (TextView) findViewById(R.id.folk_decs);

		imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/noodlepic.png",
				folkpic, options);
		folktitle.setText("面条像裤带");
		folkdecs.setText(getResources().getString(R.string.noodledec));

		getBuffer = (RelativeLayout) findViewById(R.id.getBuffer);

		getBuffer.setOnClickListener(this);

		bufferValueTextView = (TextView) findViewById(R.id.BufferValueTextView);
		scrollView = (LockableHorizontalScrollView) this
				.findViewById(R.id.ScrollView);

		category = (ImageButton) findViewById(R.id.category);
		back = (ImageButton) findViewById(R.id.back);
		exit = (ImageButton) findViewById(R.id.exit);
		camera_slide = (ImageButton) findViewById(R.id.camera_sli);
		twodimen_slide = (ImageButton) findViewById(R.id.twodimen_sli);
		call_slide = (ImageButton) findViewById(R.id.call_sli);
		theme = (ImageButton)findViewById(R.id.theme);
		
		yuleluxian = (LinearLayout)findViewById(R.id.yuleluxian);
		yule = (ImageView)findViewById(R.id.yule);
		luxian = (ImageView)findViewById(R.id.lvyouluxian);
		canyin = (ImageView)findViewById(R.id.meishicanyin);

		listViews = (LinearLayout) this.findViewById(R.id.list_views);
		cateMenu = (LinearLayout) this.findViewById(R.id.cate_menu);
		closeMenuLinear = (LinearLayout) this
				.findViewById(R.id.close_menu_linear);
		closeMenu = this.findViewById(R.id.close_menu);

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
		theme.setOnClickListener(this);

		// 实例化ImageSwitcher
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		// 设置Factory
		mImageSwitcher.setFactory(this);
		// 设置OnTouchListener，我们通过Touch事件来切换图片
		listViews.setOnTouchListener(this);

		mImageSwitcher.scrollBy(0, 0);

		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		tips = new ImageView[imageUrls.length];
		for (int i = 0; i < imageUrls.length; i++) {
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
						if (currentPosition < imageUrls.length - 1) {
							mImageSwitcher.setInAnimation(AnimationUtils
									.loadAnimation(getApplication(),
											R.anim.right_in));
							mImageSwitcher.setOutAnimation(AnimationUtils
									.loadAnimation(getApplication(),
											R.anim.lift_out));
							currentPosition++;
							new Handler().post(loadImage);
							setImageBackground(currentPosition);
						} else if (currentPosition == imageUrls.length - 1) {
							currentPosition = 0;
							new Handler().post(loadImage);
							setImageBackground(currentPosition);
						}
					}

					public void onFinish() {
					}
				}.start();
			}
		}, 1000);
	}

	public Runnable mScrollToButton = new Runnable() {
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

	/**
	 * @author sunhaifeng 另起一个线程，用来加载图片
	 */
	public Runnable loadImage = new Runnable() {

		@Override
		public void run() {
			ImageLoaderUtils.loadImageswicher(imageUrls[currentPosition],
					mImageSwitcher);
		}
	};

	public Runnable httpMediaPlayer = new Runnable() {
		@Override
		public void run() {
			mediaPlayer01 = new MediaPlayer();
			mediaPlayer01.setOnCompletionListener(NoodleActivity.this);
			mediaPlayer01.setOnErrorListener(NoodleActivity.this);
			mediaPlayer01.setOnBufferingUpdateListener(NoodleActivity.this);
			mediaPlayer01.setOnPreparedListener(NoodleActivity.this);
			try {
				mediaPlayer01.setDataSource(musicUrl);

				// mediaPlayer01.prepare();
				// mediaPlayer01.start();
				mediaPlayer01.prepareAsync();// 开始在后台缓冲音频文件并返回

			} catch (IOException e) {
				Log.v("AUDIOHTTPPLAYER", e.getMessage());
			}
		}
	};

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	public void setImageBackground(int selectItems) {
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
		i.setBackgroundColor(0xffffffff);
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
					}/*
					 * else{ Toast.makeText(getApplication(), "已经是第一张",
					 * Toast.LENGTH_SHORT).show(); }
					 */

				} else if (event.getY() > screenHeight / 2 - 20) {

					slideBarHandler.post(mScrollToButton);
				}

			}

			if (lastX < downX) {
				if (event.getY() < screenHeight / 2 - 20) {
					if (currentPosition < imageUrls.length - 1) {
						mImageSwitcher.setInAnimation(AnimationUtils
								.loadAnimation(getApplication(),
										R.anim.right_in));
						mImageSwitcher.setOutAnimation(AnimationUtils
								.loadAnimation(getApplication(),
										R.anim.lift_out));
						currentPosition++;
						new Handler().post(loadImage);
						setImageBackground(currentPosition);
					}/*
					 * else{ Toast.makeText(getApplication(), "到了最后一张",
					 * Toast.LENGTH_SHORT).show(); }
					 */
				} else if (event.getY() > screenHeight / 2 - 20) {

					slideBarHandler.post(mScrollToButton);
				}
			}
			return true;
		}

		}

		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// 当完成prepareAsync方法时，将调用活动的onPrepared方法
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		// 当MediaPlayer正在缓冲时，将调用活动的onBufferingUpdate方法
		// bufferValueTextView.setText("" + percent + "%");
		bufferValueTextView.setVisibility(View.GONE);
		if (percent != 0 && percent != 100) {
			isBuffering = true;
		} else if (percent == 100) {
			isBuffering = false;
		}
		if (percent > 5 && (!isPlaying_internet)) {
			mediaPlayer01.start();
			isPlaying_internet = true;
			play.setText("停止");
		}

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (isPlaying_internet) {
			play.setText(playBtnTitle);
			isPlaying_internet = false;
			isMp3Complete = true;
			// bufferValueTextView.setText("缓冲进度");
		}

	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		switch (what) {
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK:
			break;
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			break;
		}
		return false;
	}

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
			if (isPlaying_internet) {
				mediaPlayer01.stop();
			}
			this.finish();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			break;
		case R.id.twodimen_sli:
			if (isPlaying_internet) {
				mediaPlayer01.stop();
			}
			Intent twodimen = new Intent(NoodleActivity.this,
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
		case R.id.getBuffer:
			if (!isPlaying_internet) {
				bufferValueTextView.setVisibility(View.VISIBLE);
				bufferValueTextView.setText("正在缓冲……");
				mediaPlay.post(httpMediaPlayer);
			} else {
				bufferValueTextView.setVisibility(View.GONE);
				mediaPlayer01.stop();
				isPlaying_internet = false;
				play.setText(playBtnTitle);
			}
			break;
		case R.id.theme:
			Intent themeintent = new Intent(this,
					ThemeChangeActivity.class);
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
				if (isPlaying_internet) {
					mediaPlayer01.stop();
				}
				this.finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
