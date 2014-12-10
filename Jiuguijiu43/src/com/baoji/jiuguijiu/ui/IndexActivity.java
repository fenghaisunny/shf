package com.baoji.jiuguijiu.ui;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.adapter.IndexGalleryAdapter;
import com.baoji.jiuguijiu.entity.IndexGalleryItemData;
import com.baoji.jiuguijiu.ui.ActionSheet.ActionSheetListener;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.AnimCommon;
import com.baoji.jiuguijiu.utils.CommonTools;
import com.baoji.jiuguijiu.utils.MD5;
import com.baoji.jiuguijiu.widgets.HomeSearchBarPopupWindow;
import com.baoji.jiuguijiu.widgets.HomeSearchBarPopupWindow.onSearchBarItemClickListener;
import com.baoji.jiuguijiu.widgets.jazzviewpager.JazzyViewPager;
import com.baoji.jiuguijiu.widgets.jazzviewpager.JazzyViewPager.TransitionEffect;
import com.baoji.jiuguijiu.widgets.jazzviewpager.OutlineContainer;
import com.nostra13.universalimageloader.core.ImageLoader;

public class IndexActivity extends BaseActivity implements OnClickListener,
		onSearchBarItemClickListener, ActionSheetListener {
	public static final String TAG = IndexActivity.class.getSimpleName();

	/**
	 * 装载点点的容器
	 */
	public LinearLayout linearLayout;
	/**
	 * 点点数组
	 */
	private SharedPreferences sp;
	private SharedPreferences loginSettingsp;
	private SharedPreferences userInfosp = null;
	private int backConut = 0;
	private ImageView mMiaoShaImage = null;
	private TextView mIndexHour = null;
	private TextView mIndexMin = null;
	private TextView mIndexSeconds = null;
	private TextView mIndexPrice = null;
	private TextView mIndexRawPrice = null;
	private Button oneKeyBuy;
	private boolean isDataEmpty = false;
	private ImageView search;

	private LinearLayout list01, list02, list03, list04, list05, list06,
			list07, list08, list09;

	private TextView jingxuanTitle01, jingxuanTitle02, jingxuanTitle03,
			jingxuanTitle04, jingxuanTitle05, jingxuanTitle06, jingxuanTitle07,
			jingxuanTitle08, jingxuanTitle09, jingxuanTitle10, jingxuanTitle11,
			jingxuanTitle12, jingxuanTitle13, jingxuanTitle14, jingxuanTitle15,
			jingxuanTitle16, jingxuanTitle17, jingxuanTitle18;
	private TextView jingxuanPrice01, jingxuanPrice02, jingxuanPrice03,
			jingxuanPrice04, jingxuanPrice05, jingxuanPrice06, jingxuanPrice07,
			jingxuanPrice08, jingxuanPrice09, jingxuanPrice10, jingxuanPrice11,
			jingxuanPrice12, jingxuanPrice13, jingxuanPrice14, jingxuanPrice15,
			jingxuanPrice16, jingxuanPrice17, jingxuanPrice18;

	private RelativeLayout jingxuan01, jingxuan02, jingxuan03, jingxuan04,
			jingxuan05, jingxuan06, jingxuan07, jingxuan08, jingxuan09,
			jingxuan10, jingxuan11, jingxuan12, jingxuan13, jingxuan14,
			jingxuan15, jingxuan16, jingxuan17, jingxuan18;

	private String image_Urls[] /*
								 * = {
								 * "http://www.jiukuaisong.cn/images/HomeCarouselDiagram/10.jpg"
								 * ,
								 * "http://www.jiukuaisong.cn/images/HomeCarouselDiagram/11.jpg"
								 * ,
								 * "http://www.jiukuaisong.cn/images/HomeCarouselDiagram/12.jpg"
								 * ,
								 * "http://www.jiukuaisong.cn/images/HomeCarouselDiagram/13.jpg"
								 * ,
								 * "http://www.jiukuaisong.cn/images/HomeCarouselDiagram/14.jpg"
								 * }
								 */;

	private String tempimage_Urls[] = {
			"http://www.jiukuaisong.cn/images/HomeCarouselDiagram/10.jpg",
			"http://www.jiukuaisong.cn/images/HomeCarouselDiagram/11.jpg",
			"http://www.jiukuaisong.cn/images/HomeCarouselDiagram/12.jpg",
			"http://www.jiukuaisong.cn/images/HomeCarouselDiagram/13.jpg",
			"http://www.jiukuaisong.cn/images/HomeCarouselDiagram/14.jpg" };

	private String[] requestUrls = {
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&activitytype=3&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&activitytype=4&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&activitytype=2&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=2&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=1&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&winetype=3&pageindex=",
			"http://www.jiukuaisong.cn/api/Prolist.ashx?pgravessel=1&type=prolist&pageindex=",
			"http://www.jiukuaisong.cn/api/login.ashx" };
	// =============中部导航栏模块=====
	private ImageButton tejia, zhekou, remai, baijiu, putaojiu, yangjiu,
			jiujujiugui, jifen;
	private ImageView topLogin;
	private Intent mIntent;

	// ============== 广告切换 ===================
	private JazzyViewPager mViewPager = null;
	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mIndicators;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private ImageView[] mImageViews;
	private ImageView jingxuanImage01, jingxuanImage02, jingxuanImage03,
			jingxuanImage04, jingxuanImage05, jingxuanImage06, jingxuanImage07,
			jingxuanImage08, jingxuanImage09, jingxuanImage10, jingxuanImage11,
			jingxuanImage12, jingxuanImage13, jingxuanImage14, jingxuanImage15,
			jingxuanImage16, jingxuanImage17, jingxuanImage18;
	private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private String mImageUrl = null;
	private static final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private static final int PHOTO_CHANGE_TIME = 3000;
	// ============== 广告切换 ===================

	private IndexGalleryAdapter mStormAdapter = null;
	private IndexGalleryAdapter mPromotionAdapter = null;
	private List<IndexGalleryItemData> mStormListData = new ArrayList<IndexGalleryItemData>();
	private List<IndexGalleryItemData> mPromotionListData = new ArrayList<IndexGalleryItemData>();
	private IndexGalleryItemData mItemData = null;
	private HomeSearchBarPopupWindow mBarPopupWindow = null;
	private EditText mSearchBox = null;
	private LinearLayout mTopLayout = null;

	private String url = "http://www.jiukuaisong.cn/api/thedaily.ashx?type=daily";

	public String jingxuanImageUrls[] = {
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery03.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery02.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery03.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery04.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery05.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery06.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery07.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery08.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery01.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery02.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery03.png",
			"http://124.116.242.162:8080/JsonWeb01/images/image_gallery04.png" };

	public static int screenWidth, screenHeight;
	private String serverUrl = "";

	public ImageView[] tips;

	private ArrayList<ProductListTableItem> productDetailInfo = new ArrayList<ProductListTableItem>();

	private ProductListTableItem jingxuanInfo01 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo02 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo03 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo04 = new ProductListTableItem();

	private ProductListTableItem jingxuanInfo05 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo06 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo07 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo08 = new ProductListTableItem();

	private ProductListTableItem jingxuanInfo09 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo10 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo11 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo12 = new ProductListTableItem();
	
	private ProductListTableItem jingxuanInfo13 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo14 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo15 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo16 = new ProductListTableItem();
	
	private ProductListTableItem jingxuanInfo17 = new ProductListTableItem();
	private ProductListTableItem jingxuanInfo18 = new ProductListTableItem();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		screenWidth = metric.widthPixels;
		

		System.out.println(MD5.stringToMD5("123456"));

		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);

		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);

		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case MSG_CHANGE_PHOTO:
					int index = mViewPager.getCurrentItem();
					if (index == mImageUrls.size() - 1) {
						index = -1;
					}
					mViewPager.setCurrentItem(index + 1);
					mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
							PHOTO_CHANGE_TIME);
				}
			}
		};
		jingxuanHandler.sendEmptyMessageDelayed(0, 0);

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		search = (ImageView) findViewById(R.id.btn_search);
		oneKeyBuy = (Button) findViewById(R.id.oneKey_buy_btn);
		topLogin = (ImageView) findViewById(R.id.index_top_login);
		mIndexHour = (TextView) findViewById(R.id.index_miaosha_hour);
		mIndexMin = (TextView) findViewById(R.id.index_miaosha_min);
		mIndexSeconds = (TextView) findViewById(R.id.index_miaosha_seconds);
		mIndexPrice = (TextView) findViewById(R.id.index_miaosha_price);
		mIndexRawPrice = (TextView) findViewById(R.id.index_miaosha_raw_price);

		mMiaoShaImage = (ImageView) findViewById(R.id.index_miaosha_image);
		mViewPager = (JazzyViewPager) findViewById(R.id.index_product_images_container);
		LayoutParams params0 = (LayoutParams) mViewPager.getLayoutParams();
		params0.width = screenWidth;
		params0.height = (screenWidth)*3 / 10;
		mViewPager.setLayoutParams(params0);
		
		
		linearLayout = (LinearLayout) findViewById(R.id.viewGroup);
		mSearchBox = (EditText) findViewById(R.id.index_search_edit);
		
		LayoutParams params = (LayoutParams) mSearchBox.getLayoutParams();
		params.width = (screenWidth)*3 / 5;
		mSearchBox.setLayoutParams(params);
		
		mTopLayout = (LinearLayout) findViewById(R.id.index_top_layout);

		tejia = (ImageButton) findViewById(R.id.index_tejia_btn);
		zhekou = (ImageButton) findViewById(R.id.index_zhekou_btn);
		remai = (ImageButton) findViewById(R.id.index_remai_btn);
		baijiu = (ImageButton) findViewById(R.id.index_baijiu_btn);
		putaojiu = (ImageButton) findViewById(R.id.index_putaojiu_btn);
		yangjiu = (ImageButton) findViewById(R.id.index_yangjiu_btn);
		jifen = (ImageButton) findViewById(R.id.index_jifen_btn);
		jiujujiugui = (ImageButton) findViewById(R.id.index_jiujujiugui_btn);
		// 添加事件

		list01 = (LinearLayout) findViewById(R.id.list01);
		list02 = (LinearLayout) findViewById(R.id.list02);
		list03 = (LinearLayout) findViewById(R.id.list03);
		
		list04 = (LinearLayout) findViewById(R.id.list04);
		list05 = (LinearLayout) findViewById(R.id.list05);
		list06 = (LinearLayout) findViewById(R.id.list06);
		
		list07 = (LinearLayout) findViewById(R.id.list07);
		list08 = (LinearLayout) findViewById(R.id.list08);
		list09 = (LinearLayout) findViewById(R.id.list09);

		jingxuan01 = (RelativeLayout) findViewById(R.id.jingxuan_1);
		jingxuan02 = (RelativeLayout) findViewById(R.id.jingxuan_2);
		jingxuan03 = (RelativeLayout) findViewById(R.id.jingxuan_3);
		jingxuan04 = (RelativeLayout) findViewById(R.id.jingxuan_4);
		jingxuan05 = (RelativeLayout) findViewById(R.id.jingxuan_5);
		jingxuan06 = (RelativeLayout) findViewById(R.id.jingxuan_6);
		jingxuan07 = (RelativeLayout) findViewById(R.id.jingxuan_7);
		jingxuan08 = (RelativeLayout) findViewById(R.id.jingxuan_8);
		jingxuan09 = (RelativeLayout) findViewById(R.id.jingxuan_9);
		jingxuan10 = (RelativeLayout) findViewById(R.id.jingxuan_10);
		jingxuan11 = (RelativeLayout) findViewById(R.id.jingxuan_11);
		jingxuan12 = (RelativeLayout) findViewById(R.id.jingxuan_12);
		jingxuan13 = (RelativeLayout) findViewById(R.id.jingxuan_13);
		jingxuan14 = (RelativeLayout) findViewById(R.id.jingxuan_14);
		jingxuan15 = (RelativeLayout) findViewById(R.id.jingxuan_15);
		jingxuan16 = (RelativeLayout) findViewById(R.id.jingxuan_16);
		jingxuan17 = (RelativeLayout) findViewById(R.id.jingxuan_17);
		jingxuan18 = (RelativeLayout) findViewById(R.id.jingxuan_18);

		jingxuan01.setOnClickListener(this);
		jingxuan02.setOnClickListener(this);
		jingxuan03.setOnClickListener(this);
		jingxuan04.setOnClickListener(this);
		jingxuan05.setOnClickListener(this);
		jingxuan06.setOnClickListener(this);
		jingxuan07.setOnClickListener(this);
		jingxuan08.setOnClickListener(this);
		jingxuan09.setOnClickListener(this);
		jingxuan10.setOnClickListener(this);
		jingxuan11.setOnClickListener(this);
		jingxuan12.setOnClickListener(this);
		jingxuan13.setOnClickListener(this);
		jingxuan14.setOnClickListener(this);
		jingxuan15.setOnClickListener(this);
		jingxuan16.setOnClickListener(this);
		jingxuan17.setOnClickListener(this);
		jingxuan18.setOnClickListener(this);

		tejia.setOnClickListener(this);
		zhekou.setOnClickListener(this);
		remai.setOnClickListener(this);
		baijiu.setOnClickListener(this);
		putaojiu.setOnClickListener(this);
		yangjiu.setOnClickListener(this);
		jiujujiugui.setOnClickListener(this);
		jifen.setOnClickListener(this);
		search.setOnClickListener(this);

		jingxuanTitle01 = (TextView) findViewById(R.id.jingxuan1_title);
		jingxuanTitle02 = (TextView) findViewById(R.id.jingxuan2_title);
		jingxuanTitle03 = (TextView) findViewById(R.id.jingxuan3_title);
		jingxuanTitle04 = (TextView) findViewById(R.id.jingxuan4_title);
		jingxuanTitle05 = (TextView) findViewById(R.id.jingxuan5_title);
		jingxuanTitle06 = (TextView) findViewById(R.id.jingxuan6_title);
		jingxuanTitle07 = (TextView) findViewById(R.id.jingxuan7_title);
		jingxuanTitle08 = (TextView) findViewById(R.id.jingxuan8_title);
		jingxuanTitle09 = (TextView) findViewById(R.id.jingxuan9_title);
		jingxuanTitle10 = (TextView) findViewById(R.id.jingxuan10_title);
		jingxuanTitle11 = (TextView) findViewById(R.id.jingxuan11_title);
		jingxuanTitle12 = (TextView) findViewById(R.id.jingxuan12_title);
		jingxuanTitle13 = (TextView) findViewById(R.id.jingxuan13_title);
		jingxuanTitle14 = (TextView) findViewById(R.id.jingxuan14_title);
		jingxuanTitle15 = (TextView) findViewById(R.id.jingxuan15_title);
		jingxuanTitle16 = (TextView) findViewById(R.id.jingxuan16_title);
		jingxuanTitle17 = (TextView) findViewById(R.id.jingxuan17_title);
		jingxuanTitle18 = (TextView) findViewById(R.id.jingxuan18_title);

		jingxuanPrice01 = (TextView) findViewById(R.id.jingxuan1_price);
		jingxuanPrice02 = (TextView) findViewById(R.id.jingxuan2_price);
		jingxuanPrice03 = (TextView) findViewById(R.id.jingxuan3_price);
		jingxuanPrice04 = (TextView) findViewById(R.id.jingxuan4_price);
		jingxuanPrice05 = (TextView) findViewById(R.id.jingxuan5_price);
		jingxuanPrice06 = (TextView) findViewById(R.id.jingxuan6_price);
		jingxuanPrice07 = (TextView) findViewById(R.id.jingxuan7_price);
		jingxuanPrice08 = (TextView) findViewById(R.id.jingxuan8_price);
		jingxuanPrice09 = (TextView) findViewById(R.id.jingxuan9_price);
		jingxuanPrice10 = (TextView) findViewById(R.id.jingxuan10_price);
		jingxuanPrice11 = (TextView) findViewById(R.id.jingxuan11_price);
		jingxuanPrice12 = (TextView) findViewById(R.id.jingxuan12_price);
		jingxuanPrice13 = (TextView) findViewById(R.id.jingxuan13_price);
		jingxuanPrice14 = (TextView) findViewById(R.id.jingxuan14_price);
		jingxuanPrice15 = (TextView) findViewById(R.id.jingxuan15_price);
		jingxuanPrice16 = (TextView) findViewById(R.id.jingxuan16_price);
		jingxuanPrice17 = (TextView) findViewById(R.id.jingxuan17_price);
		jingxuanPrice18 = (TextView) findViewById(R.id.jingxuan18_price);


		jingxuanImage01 = (ImageView) findViewById(R.id.jingxuan1_image);
		jingxuanImage02 = (ImageView) findViewById(R.id.jingxuan2_image);
		jingxuanImage03 = (ImageView) findViewById(R.id.jingxuan3_image);
		jingxuanImage04 = (ImageView) findViewById(R.id.jingxuan4_image);
		jingxuanImage05 = (ImageView) findViewById(R.id.jingxuan5_image);
		jingxuanImage06 = (ImageView) findViewById(R.id.jingxuan6_image);
		jingxuanImage07 = (ImageView) findViewById(R.id.jingxuan7_image);
		jingxuanImage08 = (ImageView) findViewById(R.id.jingxuan8_image);
		jingxuanImage09 = (ImageView) findViewById(R.id.jingxuan9_image);
		jingxuanImage10 = (ImageView) findViewById(R.id.jingxuan10_image);
		jingxuanImage11 = (ImageView) findViewById(R.id.jingxuan11_image);
		jingxuanImage12 = (ImageView) findViewById(R.id.jingxuan12_image);
		jingxuanImage13 = (ImageView) findViewById(R.id.jingxuan13_image);
		jingxuanImage14 = (ImageView) findViewById(R.id.jingxuan14_image);
		jingxuanImage15 = (ImageView) findViewById(R.id.jingxuan15_image);
		jingxuanImage16 = (ImageView) findViewById(R.id.jingxuan16_image);
		jingxuanImage17 = (ImageView) findViewById(R.id.jingxuan17_image);
		jingxuanImage18 = (ImageView) findViewById(R.id.jingxuan18_image);
	}

	@Override
	protected void initView() {

		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		oneKeyBuy.setOnClickListener(this);

		mIndexHour.setText("00");
		mIndexMin.setText("53");
		mIndexSeconds.setText("49");
		mIndexPrice.setText("￥269.99");
		mIndexRawPrice.setText("￥459.99");
		mIndexRawPrice.getPaint().setFlags(
				Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
		// ======= 初始化ViewPager ========
		/*
		 * mIndicators = new ImageView[mImageUrls.size()]; if (mImageUrls.size()
		 * <= 1) { mIndicator.setVisibility(View.GONE); }
		 * 
		 * for (int i = 0; i < mIndicators.length; i++) { ImageView imageView =
		 * new ImageView(this); LayoutParams params = new LayoutParams(0,
		 * LayoutParams.WRAP_CONTENT); if (i != 0) { params.leftMargin = 5; }
		 * imageView.setLayoutParams(params); mIndicators[i] = imageView; if (i
		 * == 0) { mIndicators[i]
		 * .setBackgroundResource(R.drawable.page_indicator_focused); } else {
		 * mIndicators[i]
		 * .setBackgroundResource(R.drawable.page_indicator_unfocused); }
		 * mIndicator.addView(imageView); }
		 */

		mImageViews = new ImageView[mImageUrls.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.FIT_XY);
			mImageViews[i] = imageView;
		}
		mViewPager.setTransitionEffect(TransitionEffect.Standard);
		mViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

		mViewPager.setAdapter(new MyAdapter());
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mImageUrls.size() == 0 || mImageUrls.size() == 1)
					return true;
				else
					return false;
			}
		});

		// ======= 初始化ViewPager ========

		mStormAdapter = new IndexGalleryAdapter(this,
				R.layout.activity_index_gallery_item, mStormListData,
				new int[] { R.id.index_gallery_item_image,
						R.id.index_gallery_item_text });

		mBarPopupWindow = new HomeSearchBarPopupWindow(this,
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mBarPopupWindow.setOnSearchBarItemClickListener(this);

		//mSearchBox.setOnClickListener(this);
		topLogin.setOnClickListener(this);

		//mSearchBox.setInputType(InputType.TYPE_NULL);
	}

	private void initData() {

		for (int i = 0; i < image_Urls.length; i++) {
			mImageUrls.add(i, image_Urls[i]);
		}

		mItemData = new IndexGalleryItemData();
		mItemData.setId(1);
		mItemData
				.setImageUrl("http://124.116.242.162:8080/JsonWeb01/images/image_gallery01.png");
		mItemData.setPrice("￥79.00");
		mStormListData.add(mItemData);
	}

	public void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
			} else {
				tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
			}
		}
	}

	private Handler jingxuanHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				DefaultHttpClient mHttpClient = new DefaultHttpClient();
				HttpPost mPost = new HttpPost(url);
				HttpResponse response = mHttpClient.execute(mPost);
				int reponseCode = response.getStatusLine().getStatusCode();
				System.out.println(reponseCode);
				HttpEntity entity = response.getEntity();
				String info = EntityUtils.toString(entity);

				System.out.println(info);
				JSONObject json = new JSONObject(info);
				JSONArray array = json.getJSONArray("dailylist");
				JSONArray pictureArray = json.getJSONArray("picturelist");
				System.out.println(array.length());

				List<String> pictureList = new ArrayList<String>();
				for (int j = 0; j < pictureArray.length(); j++) {
					JSONObject js = pictureArray.getJSONObject(j);
					String pictureurl = js.getString("picture");
					if (!pictureurl.equals("")) {
						pictureList.add(pictureurl);
					}
				}
				String pictureUrls[] = new String[pictureList.size()];
				for (int k = 0; k < pictureUrls.length; k++) {
					pictureUrls[k] = pictureList.get(k);
				}
				if (pictureUrls.equals("") || pictureUrls == null) {
					image_Urls = tempimage_Urls;
				} else
					image_Urls = pictureUrls;

				tips = new ImageView[image_Urls.length];
				for (int i = 0; i < image_Urls.length; i++) {
					ImageView mImageView = new ImageView(IndexActivity.this);
					tips[i] = mImageView;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
							new ViewGroup.LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
					layoutParams.rightMargin = 3;
					layoutParams.leftMargin = 3;

					mImageView
							.setBackgroundResource(R.drawable.page_indicator_unfocused);
					linearLayout.addView(mImageView, layoutParams);
				}

				initData();
				findViewById();
				initView();

				for (int i = 0; i < array.length(); i++) {

					JSONObject singlejson = array.getJSONObject(i);

					String name = singlejson.getString("pname");
					String marketprice = singlejson.getString("pmarketprice");
					String shopprice = singlejson.getString("pshopprice");
					String huiyuanprice = singlejson.getString("pactiveprice");

					String winetype = singlejson.getString("pwinetype");
					String brand = singlejson.getString("pbrand");
					String origin = singlejson.getString("porigin");

					String alcostr = singlejson.getString("palcostr");
					String volume = singlejson.getString("pvolume");
					String smell = singlejson.getString("psmell");

					String material = singlejson.getString("pmaterial");
					String colour = singlejson.getString("pcolour");
					String desc = singlejson.getString("pdesc");

					String grapevarietie = singlejson
							.getString("pgrapevarietie");
					String paryear = singlejson.getString("pparyear");
					String level = singlejson.getString("plevel");

					String wine = singlejson.getString("pwine");
					String textureclass = singlejson.getString("ptextureclass");
					String materquality = singlejson.getString("pmaterquality");

					List<String> list = new ArrayList<String>();
					JSONArray imageArray = singlejson.getJSONArray("imglist");
					for (int j = 0; j < imageArray.length(); j++) {
						JSONObject js = imageArray.getJSONObject(j);
						String imageurl = js.getString("smallimg");
						if (!imageurl.equals("")) {
							list.add(imageurl);
						}
					}
					String imageUrls[] = new String[list.size()];
					for (int k = 0; k < imageUrls.length; k++) {
						imageUrls[k] = list.get(k);
					}

					ProductListTableItem pti = new ProductListTableItem();

					pti.setTitle(name);
					pti.setBrand(brand);
					pti.setCaizhi(materquality);
					pti.setCategory(winetype);
					pti.setImageUrls(imageUrls);
					pti.setIntroduce(desc);
					pti.setJibie(level);
					pti.setNianfen(paryear);
					pti.setPrice_jiukuaisong(shopprice);
					pti.setPrice_market(marketprice);
					pti.setPrice_huiyuan(huiyuanprice);
					pti.setProduct_location(origin);
					pti.setSeze(colour);
					pti.setJiujingdu(alcostr);
					pti.setJiuti(wine);
					pti.setXiangwei(smell);
					pti.setRongliang(volume);
					pti.setKougan(textureclass);
					pti.setYuancailiao(material);
					pti.setPutaopinzhong(grapevarietie);

					productDetailInfo.add(i, pti);

					switch (i) {
					case 0:
						jingxuanInfo01 = productDetailInfo.get(0);
						System.out.println(jingxuanInfo01.getImageUrls());
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage01);
						jingxuanTitle01.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice01.setText("￥" + shopprice);
						}else
							jingxuanPrice01.setText("￥" + huiyuanprice);
						break;
					case 1:
						jingxuanInfo02 = productDetailInfo.get(1);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage02);
						jingxuanTitle02.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice02.setText("￥" + shopprice);
						}else
							jingxuanPrice02.setText("￥" + huiyuanprice);
						break;
					case 2:
						jingxuanInfo03 = productDetailInfo.get(2);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage03);
						jingxuanTitle03.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice03.setText("￥" + shopprice);
						}else
							jingxuanPrice03.setText("￥" + huiyuanprice);
						break;
					case 3:
						jingxuanInfo04 = productDetailInfo.get(3);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage04);
						jingxuanTitle04.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice04.setText("￥" + shopprice);
						}else
							jingxuanPrice04.setText("￥" + huiyuanprice);
						break;
					case 4:
						jingxuanInfo05 = productDetailInfo.get(4);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage05);
						jingxuanTitle05.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice05.setText("￥" + shopprice);
						}else
							jingxuanPrice05.setText("￥" + huiyuanprice);
						break;
					case 5:
						jingxuanInfo06 = productDetailInfo.get(5);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage06);
						jingxuanTitle06.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice06.setText("￥" + shopprice);
						}else
							jingxuanPrice06.setText("￥" + huiyuanprice);
						break;
					case 6:
						jingxuanInfo07 = productDetailInfo.get(6);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage07);
						jingxuanTitle07.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice07.setText("￥" + shopprice);
						}else
							jingxuanPrice07.setText("￥" + huiyuanprice);
						break;
					case 7:
						jingxuanInfo08 = productDetailInfo.get(7);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage08);
						jingxuanTitle08.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice08.setText("￥" + shopprice);
						}else
							jingxuanPrice08.setText("￥" + huiyuanprice);
						break;
					case 8:
						jingxuanInfo09 = productDetailInfo.get(8);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage09);
						jingxuanTitle09.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice09.setText("￥" + shopprice);
						}else
							jingxuanPrice09.setText("￥" + huiyuanprice);
						break;
					case 9:
						jingxuanInfo10 = productDetailInfo.get(9);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage10);
						jingxuanTitle10.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice10.setText("￥" + shopprice);
						}else
							jingxuanPrice10.setText("￥" + huiyuanprice);
						break;
					case 10:
						jingxuanInfo11 = productDetailInfo.get(10);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage11);
						jingxuanTitle11.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice11.setText("￥" + shopprice);
						}else
							jingxuanPrice11.setText("￥" + huiyuanprice);
						break;
					case 11:
						jingxuanInfo12 = productDetailInfo.get(11);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage12);
						jingxuanTitle12.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice12.setText("￥" + shopprice);
						}else
							jingxuanPrice12.setText("￥" + huiyuanprice);
						break;
					case 12:
						jingxuanInfo13 = productDetailInfo.get(12);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage13);
						jingxuanTitle13.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice13.setText("￥" + shopprice);
						}else
							jingxuanPrice13.setText("￥" + huiyuanprice);
						break;
					case 13:
						jingxuanInfo14 = productDetailInfo.get(13);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage14);
						jingxuanTitle14.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice14.setText("￥" + shopprice);
						}else
							jingxuanPrice14.setText("￥" + huiyuanprice);
						break;
					case 14:
						jingxuanInfo15 = productDetailInfo.get(14);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage15);
						jingxuanTitle15.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice15.setText("￥" + shopprice);
						}else
							jingxuanPrice15.setText("￥" + huiyuanprice);
						break;
					case 15:
						jingxuanInfo16 = productDetailInfo.get(15);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage16);
						jingxuanTitle16.setText(name.substring(0,7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice16.setText("￥" + shopprice);
						}else
							jingxuanPrice16.setText("￥" + huiyuanprice);
						break;
					case 16:
						jingxuanInfo17 = productDetailInfo.get(16);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage17);
						jingxuanTitle17.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice17.setText("￥" + shopprice);
						}else
							jingxuanPrice17.setText("￥" + huiyuanprice);
						break;
					case 17:
						jingxuanInfo18 = productDetailInfo.get(17);
						ImageLoader.getInstance().displayImage(imageUrls[0],
								jingxuanImage18);
						jingxuanTitle18.setText(name.substring(0, 7)
								.concat("…"));
						if(huiyuanprice.equals("0")){
							jingxuanPrice18.setText("￥" + shopprice);
						}else
							jingxuanPrice18.setText("￥" + huiyuanprice);
						break;
					default:
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public class MyAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPager
					.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(View container, int position) {
			ImageLoader.getInstance().displayImage(mImageUrls.get(position),
					mImageViews[position]);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			return mImageViews[position];
		}
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

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			setImageBackground(position);
		}

		public void onPageScrollStateChanged(int arg0) {
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
	}

	/*	*//**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	/*
	 * private void setImageBackground(int selectItemsIndex) { for (int i = 0; i
	 * < mIndicators.length; i++) { if (i == selectItemsIndex) { mIndicators[i]
	 * .setBackgroundResource(R.drawable.page_indicator_focused); } else {
	 * mIndicators[i]
	 * .setBackgroundResource(R.drawable.page_indicator_unfocused); } } }
	 */

	@Override
	public void onClick(View v) {

		if (productDetailInfo.size() < 1) {
			isDataEmpty = true;
		}
		Intent detail = new Intent(this, ProductDetail.class);
		Intent categorylistDetail = new Intent(this, CategoryDetail.class);
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:
			Intent intent0 = new Intent(IndexActivity.this, SearchActivity.class);
			intent0.putExtra("key", mSearchBox.getText().toString());
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			this.startActivity(intent0);
			break;
		case R.id.index_top_login:
			Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
			intent.setAction("topLogin");
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			this.startActivity(intent);
			break;
		case R.id.oneKey_buy_btn:
			setTheme(R.style.ActionSheetStyleIOS6);
			showActionSheet();
			break;
		case R.id.jingxuan_1:
			System.out.println(productDetailInfo.size());
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				openActivity(ProductDetail.class, jingxuanInfo01);
			}
			break;
		case R.id.jingxuan_2:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo02);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_3:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo03);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_4:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo04);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_5:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo05);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_6:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo06);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_7:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo07);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_8:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo08);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_9:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo09);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_10:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo10);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_11:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo11);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_12:
			if (isDataEmpty) {
				Toast.makeText(this, "网络异常或服务器", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo12);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_13:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo13);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_14:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo14);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_15:
			if (isDataEmpty) {
				Toast.makeText(this, "网络异常或服务器", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo15);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_16:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo16);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_17:
			if (isDataEmpty) {
				Toast.makeText(this, "网络环境或服务器异常！", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo17);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.jingxuan_18:
			if (isDataEmpty) {
				Toast.makeText(this, "网络异常或服务器", Toast.LENGTH_SHORT).show();
			} else {
				detail.putExtra("proInfo", (Serializable) jingxuanInfo18);
				this.startActivity(detail);
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			}
			break;
		case R.id.index_tejia_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[0]);
			categorylistDetail.putExtra("title", "特价促销");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_zhekou_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[1]);
			categorylistDetail.putExtra("title", "折扣商品");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_remai_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[2]);
			categorylistDetail.putExtra("title", "热卖专场");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_baijiu_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[3]);
			categorylistDetail.putExtra("title", "白酒");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_putaojiu_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[4]);
			categorylistDetail.putExtra("title", "葡萄酒");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_yangjiu_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[5]);
			categorylistDetail.putExtra("title", "洋酒");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_jiujujiugui_btn:
			categorylistDetail.putExtra("requestUrl", requestUrls[6]);
			categorylistDetail.putExtra("title", "酒具酒柜");
			this.startActivity(categorylistDetail);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.index_jifen_btn:
			Intent intent01 = new Intent(IndexActivity.this,
					LoginActivity.class);
			intent01.setAction("topLogin");
			this.startActivity(intent01);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			backConut++;
			if (backConut == 1) {
				Toast.makeText(this, "再按一次退出“酒快送”", Toast.LENGTH_LONG).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						backConut = 0;
					}
				}, 4000);
			} else if (backConut == 2) {
				if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)
						&& userInfosp != null) {
					userInfosp.edit().clear().commit();
				}
				AppManager.getInstance().AppExit(this);
				backConut = 0;
			}
			break;
		}
		return true;
	}

	@Override
	public void onBarCodeButtonClick() {
	}

	@Override
	public void onCameraButtonClick() {
		CommonTools.showShortToast(this, "拍照购");
	}

	@Override
	public void onColorButtonClick() {
		// TODO Auto-generated method stub
		CommonTools.showShortToast(this, "颜色购");
	}

}
