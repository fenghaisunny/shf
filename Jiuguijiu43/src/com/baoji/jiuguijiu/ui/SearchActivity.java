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

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ViewSwitcher.ViewFactory;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.task.Callback;
import com.baoji.jiuguijiu.ui.LoginActivity.LoginPostImageThread;
import com.baoji.jiuguijiu.ui.PullDownView.OnPullDownListener;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.CommonTools;
import com.baoji.jiuguijiu.widgets.AutoClearEditText;

public class SearchActivity extends BaseActivity implements ViewFactory,
		Callback, OnItemClickListener {

	private AutoClearEditText mEditText = null;
	private ImageButton mImageButton = null;

	private ProgressDialog proDialog;
	Thread searchThread = null;

	private static final int WHAT_DID_LOAD_DATA = 0;// 首次加载
	private static final int WHAT_DID_REFRESH = 1;// 下拉刷新
	private static final int WHAT_DID_MORE = 2;// 上拉更多

	private int maxPageIndex;
	private String url = "http://www.jiukuaisong.cn/api/LikeSearch.ashx?type=like&keyword=";
	private ListView mListView;
	private PullDownView mPullDownView;

	private List<ProductListTableItem> mStrings = new ArrayList<ProductListTableItem>();// 适配器的适配数据

	private List<ProductListTableItem> posters = new ArrayList<ProductListTableItem>();// 盛放每次获取的json数据的容器

	private Message msg;//

	private ProductListAdapter mAdapter;
	private TextView searchTips;
	private String urlSubString,key;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		initView();
		key = getIntent().getStringExtra("key");
		mEditText.setText(key);
		
		proDialog = ProgressDialog.show(SearchActivity.this, "正在搜索..",
				"搜索中..请稍后....", true, true);
		searchThread = new Thread(new SearchingThread());
		searchThread.start();
	}

	Handler searchHandler = new Handler() {
		// @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				mAdapter.notifyDataSetChanged();
				break;
			case 0:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(SearchActivity.this, "抱歉，未搜索到相关商品！",
						Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mEditText = (AutoClearEditText) findViewById(R.id.search_edit);
		mImageButton = (ImageButton) findViewById(R.id.search_button);
		mListView = (ListView) findViewById(R.id.search_listview);
	}

	@Override
	protected void initView() {

		mListView.setOnItemClickListener(this);

		mAdapter = new ProductListAdapter(this, posters);
		mListView.setAdapter(mAdapter);

		ImageLoaderUtils.imageLoderIni(this);

		mImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				proDialog = ProgressDialog.show(SearchActivity.this, "正在搜索..",
						"搜索中..请稍后....", true, true);
				searchThread = new Thread(new SearchingThread());
				searchThread.start();
			}
		});
	}

	class SearchingThread implements Runnable {

		@Override
		public void run() {
			Looper.prepare();

			urlSubString = mEditText.getEditableText().toString();
			posters.clear();
			loadData(0);

			System.out.println("在run()方法中打印服务器端返回的数据" + searchHandler);
			Message msg = searchHandler.obtainMessage();
			if (posters.size() > 1) {
				msg.what = 1;
			} else
				msg.what = 0;

			searchHandler.sendMessage(msg);
		}
	}

	private void loadData(final int type) {

		if (type == 0) {
			getJsonData(url + urlSubString);
			System.out.println("开始加载第一页");
			System.out.println(posters.size());
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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
			JSONArray array = json.getJSONArray("likelist");
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
	public void onCallback(Object pCallbackValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Intent detail = new Intent(this, ProductDetail.class);
		detail.putExtra("proInfo", (Serializable) posters.get(position));
		startActivity(detail);
		overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
	}
}
