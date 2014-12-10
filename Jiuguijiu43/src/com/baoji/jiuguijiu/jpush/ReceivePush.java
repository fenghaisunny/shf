package com.baoji.jiuguijiu.jpush;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.HomeActivity;
import com.baoji.jiuguijiu.ui.ImageLoaderUtils;
import com.baoji.jiuguijiu.ui.MainActivity;

public class ReceivePush extends Activity implements OnClickListener {

	private String imageurl, linkurl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.receivepush);
		ImageLoaderUtils.imageLoderIni(this);

		TextView pushcontent = (TextView) findViewById(R.id.pushcontent);
		ImageView pushimage = (ImageView) findViewById(R.id.pushimage);
		TextView linkurl_tv = (TextView) findViewById(R.id.linkurl);
		linkurl_tv.setOnClickListener(this);
		
		Button back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);

		Intent pushintent = getIntent();
		if (null != pushintent && getIntent().getExtras() != null) {
			Bundle bundle = getIntent().getExtras();
			String title = bundle
					.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
			String content = bundle.getString(JPushInterface.EXTRA_ALERT);

			
			String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject json = new JSONObject(type);
				imageurl = json.getString("imageurl");
				linkurl = json.getString("linkurl");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (imageurl == null||imageurl.equals("")) {
				pushimage.setImageDrawable(getResources().getDrawable(
						R.drawable.jiukuaisong_icon));
			} else
				ImageLoaderUtils.imageLoader.displayImage(imageurl, pushimage,
						ImageLoaderUtils.options);
			if (linkurl == null||linkurl.equals("")) {
				linkurl_tv.setVisibility(View.GONE);
			} else
				linkurl_tv.setText("点击我进去看看：" + linkurl);
			pushcontent.setText(content);
			// pushcontent.setText(content+extra1+extra2); 	
		}
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			Intent backintent = new Intent(this, HomeActivity.class);
			startActivity(backintent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.linkurl:
			Intent linkintent = new Intent(this, WebViewActivity.class);
			linkintent.putExtra("result", linkurl);
			startActivity(linkintent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent backintent = new Intent(this, HomeActivity.class);
			startActivity(backintent);
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return true;
	}
}
