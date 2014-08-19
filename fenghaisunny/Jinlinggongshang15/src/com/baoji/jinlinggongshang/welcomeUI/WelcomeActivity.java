package com.baoji.jinlinggongshang.welcomeUI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import cn.jpush.android.api.JPushInterface;

import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.jpush.ExampleUtil;



public class WelcomeActivity extends Activity {
	public static boolean isForeground = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.strat);
		init();
		registerMessageReceiver();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

/*				SharedPreferences setting = getSharedPreferences(
						"CitiGame.ini", 0);
				Boolean user_first = setting.getBoolean("FIRST", true);
				if (user_first) {					
					Intent intent = new Intent(WelcomeActivity.this,
							WhatsnewPagesA.class);
					startActivity(intent);
					WelcomeActivity.this.finish();
					setting.edit().putBoolean("FIRST", false).commit();
				} else {
					Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				}*/
				
				  Intent intent = new Intent(WelcomeActivity.this,
				  WhatsnewPagesA.class);
				  startActivity(intent);
				  WelcomeActivity.this.finish();
				 
			}
		}, 2000);
	}
	private void init(){
		 JPushInterface.init(getApplicationContext());
	}


	@Override
	protected void onResume() {
		isForeground = true;
		super.onResume();
	}


	@Override
	protected void onPause() {
		isForeground = false;
		super.onPause();
	}


	@Override
	protected void onDestroy() {
		unregisterReceiver(mMessageReceiver);
		super.onDestroy();
	}
	

	//for receive customer msg from jpush server
	private MessageReceiver mMessageReceiver;
	public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
	public static final String KEY_TITLE = "title";
	public static final String KEY_MESSAGE = "message";
	public static final String KEY_EXTRAS = "extras";
	
	public void registerMessageReceiver() {
		mMessageReceiver = new MessageReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
		filter.addAction(MESSAGE_RECEIVED_ACTION);
		registerReceiver(mMessageReceiver, filter);
	}

	public class MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
             String messge = intent.getStringExtra(KEY_MESSAGE);
             String extras = intent.getStringExtra(KEY_EXTRAS);
             StringBuilder showMsg = new StringBuilder();
             showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
             if (!ExampleUtil.isEmpty(extras)) {
           	  showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
             }
             //setCostomMsg(showMsg.toString());
			}
		}
	}
	
/*	private void setCostomMsg(String msg){
		 if (null != msgText) {
			 msgText.setText(msg);
			 msgText.setVisibility(android.view.View.VISIBLE);
        }
	}	*/
}
