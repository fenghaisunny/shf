package com.baoji.jinlinggongshang;

import android.os.Bundle;
import android.view.View;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class ShiguActivity extends QinqiangActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/shigu01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/shigu02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/shigu03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/shigu04.png" };		
		
		playBtnTitle = "”Ô“Ù≤•±®";
		play.setText(playBtnTitle);		
		folktitle.setVisibility(View.GONE);
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/shigupic.png", folkpic, options);
		folkdecs.setText(getResources().getString(R.string.shigudec));
		play.setText("”Ô“Ù≤•±®");
	}
	

}
