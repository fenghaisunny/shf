package com.baoji.jinlinggongshang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class FengxianActivity extends FamensiActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[] {
				"http://192.168.2.6:8080/JsonWeb01/images/fengxian01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/fengxian02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/fengxian03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/fengxian04.png" };
		playBtnTitle = "��������";
		play.setText(playBtnTitle);
		folktitle.setVisibility(View.GONE);
		yule.setOnClickListener(new MyOnClickListener7());
		luxian.setOnClickListener(new MyOnClickListener7());
		canyin.setOnClickListener(new MyOnClickListener7());
		location.setOnClickListener(new MyOnClickListener7());
		imageLoader.displayImage(
				"http://192.168.2.6:8080/JsonWeb01/images/fengxianpic.png",
				folkpic, options);
		folkdecs.setText(getResources().getString(R.string.fengxiandec));
		play.setText("��������");
	}
	class MyOnClickListener7 implements OnClickListener {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.yule:
				Toast.makeText(FengxianActivity.this, "���������7��", Toast.LENGTH_SHORT).show();
				break;
			case R.id.lvyouluxian:
				Toast.makeText(FengxianActivity.this, "�����·��7��", Toast.LENGTH_SHORT).show();
				break;
			case R.id.meishicanyin:
				Toast.makeText(FengxianActivity.this, "����˲���7��", Toast.LENGTH_SHORT).show();
				break;
			case R.id.location:
				Intent callMap = new Intent(FengxianActivity.this,TransportationActivity.class);
				callMap.putExtra("callMap", true);
				callMap.putExtra("result", "����");
				FengxianActivity.this.startActivity(callMap);
				overridePendingTransition(R.anim.rotate_fade_in,
						R.anim.rotate_fade_out);
				break;
			}
		}

	}

}
