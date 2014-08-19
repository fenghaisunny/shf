package com.baoji.jinlinggongshang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class HongheguActivity extends FamensiActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/honghegu01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/honghegu02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/honghegu03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/honghegu04.png" };		
		
		playBtnTitle = "语音播报";
		play.setText(playBtnTitle);		
		folktitle.setVisibility(View.GONE);
		yule.setOnClickListener(new MyOnClickListener2());
		luxian.setOnClickListener(new MyOnClickListener2());
		canyin.setOnClickListener(new MyOnClickListener2());
		location.setOnClickListener(new MyOnClickListener2());
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/honghegupic.png", folkpic, options);
		folkdecs.setText(getResources().getString(R.string.honghegudec));
		play.setText("语音播报");
	}
	
	class MyOnClickListener2 implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.yule:
				Toast.makeText(HongheguActivity.this, "你点了娱乐2！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.lvyouluxian:
				Toast.makeText(HongheguActivity.this, "你点了路线2！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.meishicanyin:
				Toast.makeText(HongheguActivity.this, "你点了餐饮2！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.location:
				Intent callMap = new Intent(HongheguActivity.this,TransportationActivity.class);
				callMap.putExtra("callMap", true);
				callMap.putExtra("result", "红河谷");
				HongheguActivity.this.startActivity(callMap);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
				break;
			}
		}

	}
}
