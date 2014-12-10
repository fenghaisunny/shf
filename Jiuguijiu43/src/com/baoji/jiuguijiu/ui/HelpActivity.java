package com.baoji.jiuguijiu.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.base.BaseActivity;

public class HelpActivity extends BaseActivity implements OnClickListener {

	private TextView title;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		title = (TextView) findViewById(R.id.titleBar_tv);
		title.setText("°ïÖú");
		back = (Button) findViewById(R.id.back);
		back.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
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
