package com.baoji.jinlinggongshang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class ChuanshuoActivity extends Activity implements OnClickListener{

	
	public RelativeLayout shennongshi,chenbao,guchencang,jifengshan,nvdeng;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chuanshuo);
		
		ImageButton back = (ImageButton)findViewById(R.id.back);
		back.setOnClickListener(this);
		
		shennongshi = (RelativeLayout)findViewById(R.id.shennong);
		chenbao = (RelativeLayout)findViewById(R.id.chenbao);
		guchencang = (RelativeLayout)findViewById(R.id.guchencang);
		jifengshan = (RelativeLayout)findViewById(R.id.jifengshan);
		nvdeng = (RelativeLayout)findViewById(R.id.nvdeng);
		
		shennongshi.setOnClickListener(this);
		chenbao.setOnClickListener(this);
		guchencang.setOnClickListener(this);
		jifengshan.setOnClickListener(this);
		nvdeng.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			break;
		case R.id.shennong:
			Intent shennongintent = new Intent(this,ShennongshiActivity.class);
			startActivity(shennongintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.chenbao:
			Intent chenbaointent = new Intent(this,ChenBaoActivity.class);
			startActivity(chenbaointent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.guchencang:
			Intent guchencangintent = new Intent(this,GuchencangActivity.class);
			startActivity(guchencangintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.jifengshan:
			Intent jifengshanintent = new Intent(this,JifengshanActivity.class);
			startActivity(jifengshanintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.nvdeng:
			Intent nvdengintent = new Intent(this,NvdengActivity.class);
			startActivity(nvdengintent);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		}
		return super.onKeyDown(keyCode, event);
	}


	

}
