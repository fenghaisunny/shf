package com.baoji.jinlinggongshang;

import android.os.Bundle;
import android.view.View;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class NisuActivity extends QinqiangActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/nisu01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nisu02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nisu03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nisu04.png" };		
		playBtnTitle = "”Ô“Ù≤•±®";
		play.setText(playBtnTitle);
		
		folktitle.setVisibility(View.GONE);
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/nisupic.png", folkpic, options);
		folkdecs.setText(getResources().getString(R.string.nisudec));
		play.setText("”Ô“Ù≤•±®");
		
	}
}
