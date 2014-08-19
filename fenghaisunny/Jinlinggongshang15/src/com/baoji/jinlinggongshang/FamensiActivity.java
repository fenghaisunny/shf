package com.baoji.jinlinggongshang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class FamensiActivity extends QinqiangActivity{
	
	public RelativeLayout love_location;
	public ImageView location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/famensi01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/famensi02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/famensi03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/famensi04.png" };	
		playBtnTitle = "语音播报";
		love_location = (RelativeLayout)findViewById(R.id.love);
		love_location.setVisibility(View.VISIBLE);
		location = (ImageView)findViewById(R.id.location);
		yuleluxian.setVisibility(View.VISIBLE);
		yule.setOnClickListener(new MyOnClickListener1());
		luxian.setOnClickListener(new MyOnClickListener1());
		canyin.setOnClickListener(new MyOnClickListener1());
		location.setOnClickListener(new MyOnClickListener1());
		play.setText(playBtnTitle);	
		folkdecs.setText(getResources().getString(R.string.famensidec));
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/famensipic.png", folkpic, options);
	}
	class MyOnClickListener1 implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.yule:
				Toast.makeText(FamensiActivity.this, "你点了娱乐1！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.lvyouluxian:
				Toast.makeText(FamensiActivity.this, "你点了路线1！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.meishicanyin:
				Toast.makeText(FamensiActivity.this, "你点了餐饮1！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.location:
				Intent callMap = new Intent(FamensiActivity.this,TransportationActivity.class);
				callMap.putExtra("callMap", true);
				callMap.putExtra("result", "法门寺");
				FamensiActivity.this.startActivity(callMap);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
				break;
			}
		}

	}	
}
