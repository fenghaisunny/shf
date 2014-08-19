package com.baoji.jinlinggongshang;

import android.os.Bundle;

public class JifengshanActivity extends ShennongshiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/jifengshan01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/jifengshan02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/jifengshan03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/jifengshan04.png" };
		chuanshuo_title = "º¶∑Â…Ω";
		chuanshuo.setText(chuanshuo_title);
		chuanhsuodecStr01 = getString(R.string.jifengshandec_tv01);
		chuanhsuodecStr02 = getString(R.string.jifengshandec_tv02);
		chuanshuodec_tv01.setText(chuanhsuodecStr01);
		chuanshuodec_tv02.setText(chuanhsuodecStr02);
	}

	
}
