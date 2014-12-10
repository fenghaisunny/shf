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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.task.Callback;
import com.baoji.jiuguijiu.ui.PullDownView.OnPullDownListener;

public class CategoryDetail extends Activity implements ViewFactory,
		OnPullDownListener, Callback, OnItemClickListener, OnClickListener {

	private static final int WHAT_DID_LOAD_DATA = 0;// 首次加载
	private static final int WHAT_DID_REFRESH = 1;// 下拉刷新
	private static final int WHAT_DID_MORE = 2;// 上拉更多

	private Button back;

	private int maxPageIndex;
	private String url = "";
	private ListView mListView;
	private PullDownView mPullDownView;
	private Boolean isEmpty = false;

	private List<ProductListTableItem> mStrings = new ArrayList<ProductListTableItem>();// 适配器的适配数据

	private List<ProductListTableItem> posters = new ArrayList<ProductListTableItem>();// 盛放每次获取的json数据的容器

	private int pageIndex = 0;//
	private Message msg;//

	private String newsTitle, newsContent, newsImageUrl;
	private ProductListAdapter mAdapter;
	private TextView title, shaixuan;
	String title_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_detail);

		url = getIntent().getStringExtra("requestUrl");

		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		title = (TextView) findViewById(R.id.title);
		shaixuan = (TextView) findViewById(R.id.shaixuan);
		shaixuan.setOnClickListener(this);
		title_text = getIntent().getStringExtra("title");
		title.setText(getIntent().getStringExtra("title"));
		if (title_text.equals("特价促销") || title_text.equals("折扣商品")
				|| title_text.equals("热卖专场")) {
			shaixuan.setVisibility(View.GONE);
		}
		mPullDownView = (PullDownView) findViewById(R.id.pull_down_view);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setVerticalFadingEdgeEnabled(false);

		mListView = mPullDownView.getListView();
		mListView.setOnItemClickListener(this);

		mAdapter = new ProductListAdapter(this, mStrings);
		mListView.setAdapter(mAdapter);
		mPullDownView.enableAutoFetchMore(true, 1);

		ImageLoaderUtils.imageLoderIni(this);

		posters.clear();
		mStrings.clear();
		loadData(0);
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				
				if (posters.size() < 1 || mStrings.size() < 1) {
					Toast.makeText(CategoryDetail.this, "抱歉！暂时没有此分类的酒品", Toast.LENGTH_LONG).show();
					shaixuan.setEnabled(false);
				}

			}
		}, 8000);
	}

	private void loadData(final int type) {

		new Thread(new Runnable() {

			@Override
			public void run() {

				if (type == 0) {
					pageIndex = 0;
					getJsonData(url + pageIndex);
					System.out.println(url + pageIndex);
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
			String resCode = json.getString("msg");
			isEmpty = true;
			/*
			 * if(resCode.equals("0")){ shaixuan.setEnabled(false); }
			 */

			for (int i = 0; i < array.length(); i++) {

				JSONObject singlejson = array.getJSONObject(i);

				String name = singlejson.getString("pname");
				String marketprice = singlejson.getString("pmarketprice");
				String shopprice = singlejson.getString("pshopprice");
				String huiyuanprice = singlejson.getString("pactiveprice");
				String winetype = singlejson.getString("pwinetype");
				if (winetype.equals("")) {
					winetype = "酒具酒柜";
				}
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
					Toast.makeText(CategoryDetail.this, "亲，没有更多了哦！",
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
					Toast.makeText(CategoryDetail.this, "亲，没有更多了哦！",
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
		// TODO Auto-generated meth
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent detail = new Intent(this, ProductDetail.class);
		detail.putExtra("proInfo", (Serializable) mStrings.get(position));
		startActivity(detail);
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		} else if (v.getId() == R.id.shaixuan) {
			Intent i = new Intent(CategoryDetail.this, ShaixuanActivity.class);
			i.putExtra("winetype", title_text);
			this.startActivity(i);
			overridePendingTransition(R.anim.push_left_in,
					R.anim.push_right_out);
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

}
