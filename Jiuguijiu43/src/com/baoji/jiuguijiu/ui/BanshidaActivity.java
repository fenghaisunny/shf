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
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.task.Callback;
import com.baoji.jiuguijiu.ui.PullDownView.OnPullDownListener;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.AnimCommon;

public class BanshidaActivity extends BaseActivity implements ViewFactory,
		OnPullDownListener, Callback, OnItemClickListener, OnClickListener {

	private Button cart_login, cart_market;
	private Intent mIntent;
	private int backConut = 0;
	private SharedPreferences loginSettingsp;
	private SharedPreferences userInfosp;

	private static final int WHAT_DID_LOAD_DATA = 0;// 首次加载
	private static final int WHAT_DID_REFRESH = 1;// 下拉刷新
	private static final int WHAT_DID_MORE = 2;// 上拉更多

	private int maxPageIndex;
	private String url = "http://www.jiukuaisong.cn/api/Prolist.ashx?type=prolist&phalfof=1&pageindex=";
	private ListView mListView;
	private PullDownView mPullDownView;

	private List<ProductListTableItem> mStrings = new ArrayList<ProductListTableItem>();// 适配器的适配数据

	private List<ProductListTableItem> posters = new ArrayList<ProductListTableItem>();// 盛放每次获取的json数据的容器

	private int pageIndex = 0;//
	private Message msg;//

	private String newsTitle, newsContent, newsImageUrl;
	private ProductListAdapter mAdapter;
	private TextView shaixuan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.yishida_detail);

		findViewById();
		initView();
		posters.clear();
		mStrings.clear();
		loadData(0);
	}

	@Override
	protected void findViewById() {
		// cart_login = (Button) this.findViewById(R.id.cart_login);
		// cart_market = (Button) this.findViewById(R.id.cart_market);
		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);
		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mListView = mPullDownView.getListView();
		shaixuan = (TextView) findViewById(R.id.shaixuan);
	}

	@Override
	protected void initView() {
		// cart_login.setOnClickListener(this);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setVerticalFadingEdgeEnabled(false);
		mListView.setOnItemClickListener(this);

		mAdapter = new ProductListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);
		shaixuan.setOnClickListener(this);
	}

	private void loadData(final int type) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (type == 0) {
					pageIndex = 0;
					getJsonData(url + pageIndex);
					System.out.println("开始加载第一页");
					System.out.println(posters.size());
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				switch (type) {
				case 0:
					msg = mUIHandler.obtainMessage(WHAT_DID_LOAD_DATA);
					break;
				case 1:
					msg = mUIHandler.obtainMessage(WHAT_DID_MORE);
					break;
				case 2:
					msg = mUIHandler.obtainMessage(WHAT_DID_REFRESH);
					break;
				default:
					break;
				}
				msg.sendToTarget();
			}
		}).start();
	}

	/**
	 * 2014-4-4
	 * 
	 * @author Sunhaifeng
	 * @param startIndex
	 *            获取Json数据的起点
	 * @param endIndex
	 *            获取Json数据的终点
	 * @param url
	 *            为实现ListView数据的动态加载封装的方法
	 */
	public void getJsonData(String url) {

		List<ProductListTableItem> strings = new ArrayList<ProductListTableItem>();

		try {
			DefaultHttpClient mHttpClient = new DefaultHttpClient();
			HttpPost mPost = new HttpPost(url);
			HttpResponse response = mHttpClient.execute(mPost);
			HttpEntity entity = response.getEntity();
			String info = EntityUtils.toString(entity);

			System.out.println(info);
			JSONObject json = new JSONObject(info);
			maxPageIndex = Integer.parseInt(json.getString("maxpageIndex"));
			System.out.println(maxPageIndex);
			JSONArray array = json.getJSONArray("prolist");
			System.out.println(array.length());

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

				String grapevarietie = singlejson.getString("pgrapevarietie");
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
				posters.add(pti);
			}
			for (ProductListTableItem body : posters) {
				strings.add(body);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(2);
			}
		}).start();
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onMore() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				loadData(1);
			}
		}).start();
	}

	private Handler mUIHandler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case WHAT_DID_LOAD_DATA: {
				try {
					System.out.println(posters.size());
					List<ProductListTableItem> strings = new ArrayList<ProductListTableItem>();
					if (posters != null) {
						strings.addAll(posters);
						if (!strings.isEmpty()) {
							mStrings.addAll(strings);
							mAdapter.notifyDataSetChanged();
						}
					}
					mAdapter.notifyDataSetChanged();
					// 数据加载完毕
					mPullDownView.notifyDidLoad();
					break;
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					posters.clear();
					mStrings.clear();
					mAdapter.notifyDataSetChanged();
				}
			}
			case WHAT_DID_REFRESH: {
				List<ProductListTableItem> strings = new ArrayList<ProductListTableItem>();
				pageIndex++;
				if (pageIndex > maxPageIndex) {
					Toast.makeText(BanshidaActivity.this, "亲，没有更多了哦！",
							Toast.LENGTH_SHORT).show();
					mPullDownView.notifyDidRefresh();
					break;
				}
				posters.clear();
				getJsonData(url + pageIndex);
				strings.addAll(posters);
				if (!strings.isEmpty()) {
					System.out.println(strings.size());
					mStrings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				mPullDownView.notifyDidRefresh();
				break;
			}
			case WHAT_DID_MORE: {
				List<ProductListTableItem> strings = new ArrayList<ProductListTableItem>();
				pageIndex++;
				if (pageIndex > maxPageIndex) {
					Toast.makeText(BanshidaActivity.this, "亲，没有更多了哦！",
							Toast.LENGTH_SHORT).show();
					mPullDownView.notifyDidMore();
					break;
				}
				posters.clear();
				getJsonData(url + pageIndex);
				strings.addAll(posters);
				if (!strings.isEmpty()) {
					System.out.println(strings.size());
					mStrings.addAll(strings);
					mAdapter.notifyDataSetChanged();
				}
				mPullDownView.notifyDidMore();
				break;
			}
			}
		}
	};

	@Override
	public void onCallback(Object pCallbackValue) {
		// TODO Auto-generated method stub
	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cart_login:
			mIntent = new Intent(this, LoginActivity.class);
			startActivity(mIntent);
			AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.shaixuan:
			Intent i = new Intent(BanshidaActivity.this, ShaixuanActivity.class);
			i.putExtra("winetype", "半时达");
			this.startActivity(i);
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent detail = new Intent(this, ProductDetail.class);
		detail.putExtra("proInfo", (Serializable) mStrings.get(position));
		startActivity(detail);
		AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
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
				if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
					userInfosp.edit().clear().commit();
				}
				AppManager.getInstance().AppExit(this);
				backConut = 0;
			}
			break;
		}
		return true;
	}
}
