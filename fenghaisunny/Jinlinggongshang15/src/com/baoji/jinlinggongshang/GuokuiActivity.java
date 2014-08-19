package com.baoji.jinlinggongshang;

import android.os.Bundle;

import com.baoji.jinlinggongshang.util.MyActivityManager;




public class GuokuiActivity extends NoodleActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/guokui01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guokui02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guokui03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guokui04.png" };
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		
		
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/guokuipic.png", folkpic, options);
		folktitle.setText("锅盔像锅盖");
		folkdecs.setText(getResources().getString(R.string.guokuidec));
	}
	

}