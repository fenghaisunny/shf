package com.baoji.jinlinggongshang;

import android.os.Bundle;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class NianhuaActivity extends QinqiangActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
	}
}
