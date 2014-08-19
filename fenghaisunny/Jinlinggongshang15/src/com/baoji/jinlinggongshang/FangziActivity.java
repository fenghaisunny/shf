package com.baoji.jinlinggongshang;

import android.os.Bundle;

import com.baoji.jinlinggongshang.util.MyActivityManager;



public class FangziActivity extends NoodleActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
	
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/nisupic.png", folkpic, options);
		folktitle.setText("房子半边盖");
		folkdecs.setText(getResources().getString(R.string.lazidec));

		
	}
	

}