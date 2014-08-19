package com.baoji.jinlinggongshang;



import android.os.Bundle;

import com.baoji.jinlinggongshang.util.MyActivityManager;


public class LaziActivity extends NoodleActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/lazi01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/lazi02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/lazi03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/lazi04.png" };	
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);

		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/lazipic.png", folkpic, options);
		folktitle.setText("辣子一道菜");
		folkdecs.setText(getResources().getString(R.string.lazidec));
	}
	

}

