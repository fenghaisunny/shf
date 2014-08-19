package com.baoji.jinlinggongshang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class YuntaishanActivity extends FamensiActivity{
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/guanshan01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guanshan02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guanshan03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/guanshan04.png" };		
		
		playBtnTitle = "语音播报";
		play.setText(playBtnTitle);		
		folktitle.setVisibility(View.GONE);
		yule.setOnClickListener(new MyOnClickListener3());
		luxian.setOnClickListener(new MyOnClickListener3());
		canyin.setOnClickListener(new MyOnClickListener3());
		location.setOnClickListener(new MyOnClickListener3());
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/guanshanpic.png", folkpic, options);
		folkdecs.setText(getResources().getString(R.string.guanshandec));
		play.setText("语音播报");
	}
	
	class MyOnClickListener3 implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.yule:
				Toast.makeText(YuntaishanActivity.this, "你点了娱乐3！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.lvyouluxian:
				Toast.makeText(YuntaishanActivity.this, "你点了路线3！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.meishicanyin:
				Toast.makeText(YuntaishanActivity.this, "你点了餐饮3！", Toast.LENGTH_SHORT).show();
				break;
			case R.id.location:
				Intent callMap = new Intent(YuntaishanActivity.this,TransportationActivity.class);
				callMap.putExtra("callMap", true);
				callMap.putExtra("result", "云台山");
				YuntaishanActivity.this.startActivity(callMap);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
				break;
			}
		}

	}
}
