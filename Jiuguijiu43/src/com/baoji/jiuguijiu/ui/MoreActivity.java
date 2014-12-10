package com.baoji.jiuguijiu.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.jpush.PushSettingActivity;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.AnimCommon;

public class MoreActivity extends BaseActivity implements OnClickListener {

	private int backConut = 0;
	private RelativeLayout pushset, exit,help,about;
	private SharedPreferences loginSettingsp;
	private SharedPreferences userInfosp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_more);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		pushset = (RelativeLayout) findViewById(R.id.push_setting);
		exit = (RelativeLayout) findViewById(R.id.more_exit);
		help = (RelativeLayout) findViewById(R.id.more_help);
		about = (RelativeLayout) findViewById(R.id.more_aboutus);
		
		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);
		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
	}

	@Override
	protected void initView() {
		pushset.setOnClickListener(this);
		exit.setOnClickListener(this);
		about.setOnClickListener(this);
		help.setOnClickListener(this);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.push_setting:
			Intent pushset = new Intent(MoreActivity.this,
					PushSettingActivity.class);
			this.startActivity(pushset);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case R.id.more_exit:
			if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
				userInfosp.edit().clear().commit();
			}
			AppManager.getInstance().AppExit(this);
			break;
		case R.id.more_help:
			Intent help = new Intent(MoreActivity.this,
					HelpActivity.class);
			this.startActivity(help);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		case R.id.more_aboutus:
			Intent aboutus = new Intent(MoreActivity.this,
					AboutActivity.class);
			this.startActivity(aboutus);
			AnimCommon.set(R.anim.push_down_in,R.anim.push_down_out); 
			break;
		default:
			break;
		}
	}
}
