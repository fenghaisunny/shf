package com.baoji.jiuguijiu.ui;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.ActionSheet.ActionSheetListener;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.NetworkUtils;

public class JifenshangchengActivity extends BaseActivity implements
		OnClickListener, ActionSheetListener {

	private Button back;
	private Boolean isNetAvailable;
	private TextView duihuan;

	private String[] titles /*
							 * = new String[] { "三星Galaxy", "ipad", "itouch",
							 * "ipod", "拉菲葡萄酒", "西凤五年", "伏特加" }
							 */;
	// 图片的第二行文字
	private String[] description /*
								 * = new String[] { "满500积分", "满1000积分",
								 * "满2000积分", "满3000积分", "满4000积分", "满5000积分",
								 * "满6000积分" }
								 */;
	// 图片ID数组
	private String[] images/*
							 * = {
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/2365a9db-5d24-42a5-8b24-77d19124b3bd.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/013bc5d5-78d1-4ded-a01b-34367238b01d.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/feaa442b-d58a-457f-b389-a4240fe77a6a.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/ed904e23-786e-42ed-96b9-46aca17b48cb.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/dc671716-87eb-4078-af6f-2c5be0bbc77a.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/013bc5d5-78d1-4ded-a01b-34367238b01d.jpg"
							 * ,
							 * "http://www.jiukuaisong.cn/images/ProductDiagram/Small/feaa442b-d58a-457f-b389-a4240fe77a6a.jpg"
							 * }
							 */;
	private GridView gridView;

	private String url = "http://www.jiukuaisong.cn/api/GetIntegralrules.ashx?type=integral";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jifen);
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
		duihuan = (TextView) findViewById(R.id.duihuan);
		duihuan.setOnClickListener(this);

		ImageLoaderUtils.imageLoderIni(this);

		isNetAvailable = NetworkUtils.isNetworkAvailable(this);
		if (isNetAvailable) {
			getGridInfoHandler.sendEmptyMessageDelayed(0, 0);
		} else {
		}
		gridView = (GridView) findViewById(R.id.gridview);
		/*
		 * gridView = (GridView) findViewById(R.id.gridview); GridViewAdapter
		 * adapter = new GridViewAdapter(titles, images, description, this);
		 * gridView.setAdapter(adapter);
		 */

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
/*				Toast.makeText(JifenshangchengActivity.this,
						"item" + (position + 1), Toast.LENGTH_SHORT).show();*/
			}
		});
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

	private Handler getGridInfoHandler = new Handler() {

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
				String resCode = json.getString("msg");
				System.out.println(info);

				JSONArray jifenInfoArray = json.getJSONArray("integralllist");

				titles = new String[jifenInfoArray.length()];
				description = new String[jifenInfoArray.length()];
				images = new String[jifenInfoArray.length()];
				for (int i = 0; i < jifenInfoArray.length(); i++) {
					JSONObject js = jifenInfoArray.getJSONObject(i);
					String title = js.getString("integralprize");
					String desc = "所需积分："+js.getString("integralscore");
					String image = js.getString("integralimg");
					titles[i] = title;
					description[i] = desc;
					images[i] = image;
				}
				GridViewAdapter adapter = new GridViewAdapter(titles, images,
						description, JifenshangchengActivity.this);
				gridView.setAdapter(adapter);
				/**
				 * for (int i = 0; i < originArray.length(); i++) { JSONObject
				 * js = originArray.getJSONObject(i); String origin =
				 * js.getString("olist"); originList[i] = origin; }
				 */
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.duihuan:
			setTheme(R.style.ActionSheetStyleIOS6);
			showActionSheet();
			break;
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
	protected void findViewById() {
	}

	@Override
	protected void initView() {
	}
}
