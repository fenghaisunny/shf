package com.baoji.jinlinggongshang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				/*
				 * SharedPreferences setting = getSharedPreferences(
				 * "CitiGame.ini", 0); Boolean user_first =
				 * setting.getBoolean("FIRST", true); if (user_first) { Intent
				 * intent = new Intent(WelcomeActivity.this,
				 * WhatsnewPagesA.class); startActivity(intent);
				 * WelcomeActivity.this.finish();
				 * setting.edit().putBoolean("FIRST", false).commit(); } else {
				 * Intent intent = new Intent(WelcomeActivity.this,
				 * MainActivity.class); startActivity(intent); finish(); }
				 */

				Intent intent = new Intent(splash.this, HomeActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
				splash.this.finish();

			}
		}, 2000);
	}

}
