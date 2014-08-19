package com.baoji.jinlinggongshang;

import android.os.Bundle;

import com.baoji.jinlinggongshang.util.ImageLoaderUtils;

public class WenwangActivity extends MugongActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		imageUrl = "http://192.168.2.6:8080/JsonWeb01/images/wenwangdec.png";
		ImageLoaderUtils.imageLoader.displayImage(imageUrl, diangudec_image, ImageLoaderUtils.options);
		diangudec_tv.setText(getString(R.string.wenwangdec));
	}

	
}
