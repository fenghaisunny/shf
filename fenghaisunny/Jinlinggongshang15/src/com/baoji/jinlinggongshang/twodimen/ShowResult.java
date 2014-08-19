package com.baoji.jinlinggongshang.twodimen;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.baoji.jinlinggongshang.AboutActivity;
import com.baoji.jinlinggongshang.CultureActivity;
import com.baoji.jinlinggongshang.FangziActivity;
import com.baoji.jinlinggongshang.FolkActivity;
import com.baoji.jinlinggongshang.GuokuiActivity;
import com.baoji.jinlinggongshang.LaziActivity;
import com.baoji.jinlinggongshang.MainActivity;
import com.baoji.jinlinggongshang.NianhuaActivity;
import com.baoji.jinlinggongshang.NisuActivity;
import com.baoji.jinlinggongshang.NoodleActivity;
import com.baoji.jinlinggongshang.QinqiangActivity;
import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.ShehuoActivity;
import com.baoji.jinlinggongshang.ShiguActivity;
import com.baoji.jinlinggongshang.ShoupaActivity;
import com.baoji.jinlinggongshang.WanpenActivity;
import com.baoji.jinlinggongshang.XintianyouActivity;
import com.baoji.jinlinggongshang.util.MyActivityManager;

public class ShowResult extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.showresult);
		MyActivityManager.getInstance().addActivity(this);

		TextView tv = (TextView)findViewById(R.id.result);
		Intent intent = getIntent();
		String result = intent.getStringExtra("result");
		tv.setText(result);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch(keyCode){
		case KeyEvent.KEYCODE_BACK:
			int dir = getIntent().getIntExtra("dir", 0);
			switch(dir){
			case 0:
				Intent intent0 = new Intent();
				intent0.setClass(ShowResult.this, MainActivity.class);
				startActivity(intent0);
				this.finish();
				break;
			case 1:
				Intent intent1 = new Intent();
				intent1.setClass(ShowResult.this, AboutActivity.class);
				startActivity(intent1);
				this.finish();
				break;
			case 2:
				Intent intent2 = new Intent();
				intent2.setClass(ShowResult.this, CultureActivity.class);
				startActivity(intent2);
				this.finish();
				break;	
			case 201:
				Intent intent201 = new Intent();
				intent201.setClass(this, QinqiangActivity.class);
				startActivity(intent201);
				this.finish();
				break;	
			case 202:
				Intent intent202 = new Intent();
				intent202.setClass(this, ShiguActivity.class);
				startActivity(intent202);
				this.finish();
				break;
			case 203:
				Intent intent203 = new Intent();
				intent203.setClass(this, ShehuoActivity.class);
				startActivity(intent203);
				this.finish();
				break;
			case 204:
				Intent intent204 = new Intent();
				intent204.setClass(this, FangziActivity.class);
				startActivity(intent204);
				this.finish();
				break;
			case 205:
				Intent intent205 = new Intent();
				intent205.setClass(this, NisuActivity.class);
				startActivity(intent205);
				this.finish();
				break;			
			case 206:
				Intent intent206 = new Intent();
				intent206.setClass(this, NianhuaActivity.class);
				startActivity(intent206);
				this.finish();
				break;								
			case 4:
				Intent intent4 = new Intent();
				intent4.setClass(ShowResult.this, FolkActivity.class);
				startActivity(intent4);
				this.finish();
				break;	
			case 401:
				Intent intent401 = new Intent();
				intent401.setClass(this, NoodleActivity.class);
				startActivity(intent401);
				this.finish();
				break;	
			case 402:
				Intent intent402 = new Intent();
				intent402.setClass(this, GuokuiActivity.class);
				startActivity(intent402);
				this.finish();
				break;
			case 403:
				Intent intent403 = new Intent();
				intent403.setClass(this, LaziActivity.class);
				startActivity(intent403);
				this.finish();
				break;
			case 404:
				Intent intent404 = new Intent();
				intent404.setClass(this, WanpenActivity.class);
				startActivity(intent404);
				this.finish();
				break;
			case 405:
				Intent intent405 = new Intent();
				intent405.setClass(this, ShoupaActivity.class);
				startActivity(intent405);
				this.finish();
				break;			
			case 406:
				Intent intent406 = new Intent();
				intent406.setClass(this, FangziActivity.class);
				startActivity(intent406);
				finish();
				break;					
			case 407:
				Intent intent407 = new Intent();
				intent407.setClass(this, XintianyouActivity.class);
				startActivity(intent407);
				this.finish();
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


}
