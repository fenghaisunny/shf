package com.baoji.jiuguijiu.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.ActionSheet.ActionSheetListener;
import com.baoji.jiuguijiu.ui.base.BaseActivity;

public class ProductDetail extends BaseActivity implements ActionSheetListener,
		OnClickListener, ViewFactory, OnTouchListener, Callback {

	protected Handler handler = new Handler(this);
	private ProductListTableItem data = new ProductListTableItem();

	private TextView name, pricemarket, priceshop, title, winetype, brand,
			origin, alcostr, volume, smell, material, colour, desc,
			grapevarietie, paryear, level, wine, textureclass, materquality;
	private ImageView image;
	private Button oneKeyBuy, back;
	private String imagelist[];

	Handler imageSwitcherHandler = new Handler();

	public ImageSwitcher mImageSwitcher;
	/**
	 * 图片id数组
	 */
	public int currentPosition = 0;
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
	public int screenHeight;
	public int screenWidth;

	/*
	 * private Handler changeImage = new Handler() {
	 * 
	 * @Override public void handleMessage(Message msg) {
	 * changeImage.sendEmptyMessageDelayed(0, 3000); if (currentPosition <
	 * imagelist.length - 1) {
	 * mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
	 * getApplication(), R.anim.right_in));
	 * mImageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
	 * getApplication(), R.anim.lift_out)); currentPosition++; new
	 * Handler().post(loadImage); setImageBackground(currentPosition); } else if
	 * (currentPosition == imagelist.length - 1) { currentPosition = 0; new
	 * Handler().post(loadImage); setImageBackground(currentPosition); } } };
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_detail);
		ImageLoaderUtils.imageLoderIni(this);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		screenWidth = metric.widthPixels;
		screenHeight = metric.heightPixels;

		data = (ProductListTableItem) getIntent().getSerializableExtra(
				"proInfo");

		oneKeyBuy = (Button) findViewById(R.id.oneKey_buy_btn);
		oneKeyBuy.setOnClickListener(this);

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		title = (TextView) findViewById(R.id.titleBar_tv);
		title.setText(data.getTitle().substring(0, 6).concat("…"));
		name = (TextView) findViewById(R.id.name);
		pricemarket = (TextView) findViewById(R.id.pricemarket);
		priceshop = (TextView) findViewById(R.id.priceshop);
		name.setText(data.getTitle());
		
		pricemarket.setText("零售价：￥" + data.getPrice_market());
		if (data.getPrice_huiyuan().equals("0")) {
			priceshop.setText("折扣价：￥" + data.getPrice_jiukuaisong());
		} else
			priceshop.setText("会员价：￥" + data.getPrice_huiyuan());

		brand = (TextView) findViewById(R.id.brand);
		origin = (TextView) findViewById(R.id.origin);
		alcostr = (TextView) findViewById(R.id.alcostr);
		volume = (TextView) findViewById(R.id.volume);
		smell = (TextView) findViewById(R.id.smell);
		material = (TextView) findViewById(R.id.material);
		winetype = (TextView) findViewById(R.id.winetype);
		colour = (TextView) findViewById(R.id.colour);
		desc = (TextView) findViewById(R.id.desc);
		grapevarietie = (TextView) findViewById(R.id.grapevarietie);
		paryear = (TextView) findViewById(R.id.paryear);
		level = (TextView) findViewById(R.id.level);

		wine = (TextView) findViewById(R.id.wine);
		textureclass = (TextView) findViewById(R.id.textureclass);
		materquality = (TextView) findViewById(R.id.materquality);

		imagelist = data.getImageUrls();
		String pwinetype = data.getCategory();
		String pbrand = "品牌：" + data.getBrand();
		String porigin = "产地：" + data.getProduct_location();

		String palcostr = data.getJiujingdu();
		String pvolume = data.getRongliang();
		String psmell = data.getXiangwei();

		String pmaterial = data.getYuancailiao();
		String pcolour = data.getSeze();
		String pdesc = data.getIntroduce();

		String pgrapevarietie = data.getPutaopinzhong();
		String pparyear = data.getNianfen();
		String plevel = data.getJibie();

		String pwine = data.getJiuti();
		String ptextureclass = data.getKougan();
		String pmaterquality = data.getCaizhi();

		volume.setText(pvolume);
		brand.setText(pbrand);
		origin.setText(porigin);
		winetype.setText(pwinetype);
		desc.setText(pdesc);

		if (pwinetype.equals("")) {
			winetype.setVisibility(View.GONE);
		} else
			winetype.setText("类型：" + pwinetype);
		if (pdesc.equals("")) {
			desc.setVisibility(View.GONE);
		} else
			desc.setText("商品简介：" + pdesc);

		if (pvolume.equals("")) {
			volume.setVisibility(View.GONE);
		} else
			volume.setText("容量：" + pvolume);

		if (palcostr.equals("")) {
			alcostr.setVisibility(View.GONE);
		} else
			alcostr.setText("酒精度：" + palcostr);
		if (psmell.equals("")) {
			smell.setVisibility(View.GONE);
		} else
			smell.setText("香味：" + psmell);
		if (pmaterial.equals("")) {
			material.setVisibility(View.GONE);
		} else
			material.setText("原材料：" + pmaterial);
		if (pcolour.equals("")) {
			colour.setVisibility(View.GONE);
		} else
			colour.setText("色泽：" + pcolour);
		if (pgrapevarietie.equals("")) {
			grapevarietie.setVisibility(View.GONE);
		} else
			grapevarietie.setText("葡萄品种：" + pgrapevarietie);
		if (pparyear.equals("")) {
			paryear.setVisibility(View.GONE);
		} else
			paryear.setText("年份：" + pparyear);
		if (plevel.equals("")) {
			level.setVisibility(View.GONE);
		} else
			level.setText(plevel);
		if (pwine.equals("")) {
			wine.setVisibility(View.GONE);
		} else
			wine.setText("酒体：" + pwine);
		if (ptextureclass.equals("")) {
			textureclass.setVisibility(View.GONE);
		} else
			textureclass.setText("口感：" + ptextureclass);
		if (pmaterquality.equals("")) {
			materquality.setVisibility(View.GONE);
		} else
			materquality.setText("材质：" + pmaterquality);

		// 实例化ImageSwitcher
		mImageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);

		RelativeLayout imageSwich = (RelativeLayout) findViewById(R.id.image_switch);
		LayoutParams params = (LayoutParams) imageSwich.getLayoutParams();
		params.width = screenHeight / 2;
		params.height = screenHeight / 3;
		imageSwich.setLayoutParams(params);

		// 设置Factory
		mImageSwitcher.setFactory(this);
		mImageSwitcher.setOnTouchListener(this);

		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		tips = new ImageView[imagelist.length];
		for (int i = 0; i < imagelist.length; i++) {
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

		new Handler().post(loadImage);
		setImageBackground(0);

		// changeImage.sendEmptyMessageDelayed(0, 0);

	}

	public void showActionSheet() {
		ActionSheet.createBuilder(this, getSupportFragmentManager())
				.setCancelButtonTitle("取消")
				.setOtherButtonTitles("4008-966-999")
				.setCancelableOnTouchOutside(true).setListener(this).show();
	}

	@Override
	public void onOtherButtonClick(ActionSheet actionSheet, int index) {
		Intent callintent;
		switch (index) {
		case 0:
			callintent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
					+ "4008966999"));
			startActivity(callintent);
			break;
		}
	}

	@Override
	public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.oneKey_buy_btn:
			setTheme(R.style.ActionSheetStyleIOS6);
			showActionSheet();
			break;
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public View makeView() {
		final ImageView i = new ImageView(this);
		i.setBackgroundColor(0xffffffff);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	/**
	 * @author sunhaifeng 加载图片
	 */
	public Runnable loadImage = new Runnable() {

		@Override
		public void run() {
			ImageLoaderUtils.loadImageswicher(imagelist[currentPosition],
					mImageSwitcher);
		}
	};

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
				if (currentPosition > 0) {
					// 设置动画，这里的动画比较简单，不明白的去网上看看相关内容
					mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
							getApplication(), R.anim.left_in));
					mImageSwitcher.setOutAnimation(AnimationUtils
							.loadAnimation(getApplication(), R.anim.right_out));
					currentPosition--;
					new Handler().post(loadImage);
					setImageBackground(currentPosition);
				}
			}

			if (lastX < downX) {
				if (currentPosition < imagelist.length - 1) {
					mImageSwitcher.setInAnimation(AnimationUtils.loadAnimation(
							getApplication(), R.anim.right_in));
					mImageSwitcher.setOutAnimation(AnimationUtils
							.loadAnimation(getApplication(), R.anim.lift_out));
					currentPosition++;
					new Handler().post(loadImage);
					setImageBackground(currentPosition);
				}
			}
			return true;
		}
		}
		return false;
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
