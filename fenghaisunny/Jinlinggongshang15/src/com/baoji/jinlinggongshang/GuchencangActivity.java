package com.baoji.jinlinggongshang;

import android.os.Bundle;

public class GuchencangActivity extends ShennongshiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/guchencang01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guchencang02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guchencang03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guchencang04.png" };
		chuanshuo_title = "¹Å³Â²Ö";
		chuanshuo.setText(chuanshuo_title);
		chuanhsuodecStr01 = getString(R.string.guchencangdec_tv01);
		chuanhsuodecStr02 = getString(R.string.guchencangdec_tv02);
		chuanshuodec_tv01.setText(chuanhsuodecStr01);
		chuanshuodec_tv02.setText(chuanhsuodecStr02);
	}

	
}
