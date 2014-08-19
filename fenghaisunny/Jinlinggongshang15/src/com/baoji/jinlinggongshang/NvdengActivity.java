package com.baoji.jinlinggongshang;

import android.os.Bundle;

public class NvdengActivity extends ShennongshiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/nvdeng01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nvdeng02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nvdeng03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/nvdeng04.png" };
		chuanshuo_title = "Ñ×µÛÄ¸Ç×-Å®µÇ";
		chuanshuo.setText(chuanshuo_title);
		chuanhsuodecStr01 = getString(R.string.nvdengdec_tv01);
		chuanhsuodecStr02 = getString(R.string.nvdengdec_tv02);
		chuanshuodec_tv01.setText(chuanhsuodecStr01);
		chuanshuodec_tv02.setText(chuanhsuodecStr02);
	}

	
}
