package com.baoji.jinlinggongshang.sharecontent;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.sharesdk.framework.TitleLayout;

import com.baoji.jinlinggongshang.R;

/** Json数据显示页面 */
public class JsonPage extends Activity implements OnClickListener {
	public static String bigData;
	private TitleLayout llTitle;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		String title = getIntent().getStringExtra("title");

		setContentView(R.layout.page_show_user_info);
		llTitle = (TitleLayout) findViewById(R.id.llTitle);
		llTitle.getBtnBack().setOnClickListener(this);
		if (TextUtils.isEmpty(title)) {
			llTitle.getTvTitle().setText(R.string.app_name);
		}
		else {
			llTitle.getTvTitle().setText(title);
		}

		final TextView tvJson = (TextView) findViewById(R.id.tvJson);
		if (bigData == null) {
			tvJson.setText(getIntent().getStringExtra("data"));
		}
		else {
			tvJson.postDelayed(new Runnable() {
				public void run() {
					tvJson.setText(bigData);
					bigData = null;
				}
			}, 333);
		}
	}

	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			finish();
		}
	}

}
