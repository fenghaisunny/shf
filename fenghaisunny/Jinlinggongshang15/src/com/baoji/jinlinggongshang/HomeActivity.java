package com.baoji.jinlinggongshang;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.baoji.jinlinggongshang.pulltorefresh.MainTabActivity;
import com.baoji.jinlinggongshang.pulltorefresh.TousuActivity;
import com.baoji.jinlinggongshang.util.MyActivityManager;


public class HomeActivity extends BaseSlidingFragmentActivity implements OnClickListener{

	private ImageButton jigou,xinwen,baodao,zhengwu,banshi,sousuo,wangge;
	private RelativeLayout guanggao;
	
	private SlidingMenu sm;
	private ImageView category;
	
	private int backConut = 0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home);
		
		initSlidingMenu();
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);

		int screenWidth = metric.widthPixels;
		int screenHeight = metric.heightPixels;
		
		guanggao = (RelativeLayout)findViewById(R.id.guanggao);
		LayoutParams params = (LayoutParams) guanggao.getLayoutParams();
		params.width = screenWidth;
		params.height = screenHeight/3;
		guanggao.setLayoutParams(params);
		
		
		jigou = (ImageButton)findViewById(R.id.ib1);
		xinwen = (ImageButton)findViewById(R.id.ib2);
		baodao = (ImageButton)findViewById(R.id.ib3);
		zhengwu = (ImageButton)findViewById(R.id.ib4);
		banshi = (ImageButton)findViewById(R.id.ib5);
		sousuo = (ImageButton)findViewById(R.id.ib6);
		wangge = (ImageButton)findViewById(R.id.ib7);
		
		category = (ImageView)findViewById(R.id.category);
		
		jigou.setOnClickListener(this);
		xinwen.setOnClickListener(this);
		baodao.setOnClickListener(this);
		zhengwu.setOnClickListener(this);
		banshi.setOnClickListener(this);
		sousuo.setOnClickListener(this);
		wangge.setOnClickListener(this);
		category.setOnClickListener(this);
		
	}
	private void initSlidingMenu() {
		setBehindContentView(R.layout.behind_slidingmenu);
		// customize the SlidingMenu
		sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
		sm.setShadowWidth(20);
		// sm.setBehindScrollScale(0.333f);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ib1:
			Intent intent1 = new Intent(this,MainTabActivity.class);
			intent1.putExtra("item_index", 0);
			startActivity(intent1);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib2:
			Intent intent2 = new Intent(this,MainTabActivity.class);
			intent2.putExtra("item_index", 1);
			startActivity(intent2);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib3:
			Intent intent3 = new Intent(this,MainTabActivity.class);
			intent3.putExtra("item_index", 2);
			startActivity(intent3);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib4:
			Intent intent4 = new Intent(this,MainTabActivity.class);
			intent4.putExtra("item_index", 3);
			startActivity(intent4);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib5:
			Intent intent5 = new Intent(this,MainTabActivity.class);
			intent5.putExtra("item_index", 4);
			startActivity(intent5);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib6:
			Intent intent6 = new Intent(this,MainTabActivity.class);
			intent6.putExtra("item_index", 5);
			startActivity(intent6);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.ib7:
			Intent intent7 = new Intent(this,TousuActivity.class);
			startActivity(intent7);
			overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
			break;
		case R.id.category:
			if (sm.isMenuShowing()) {
				toggle();
			} else {
				showMenu();
			}
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (sm.isMenuShowing()) {
				toggle();
			} else
				backConut++;
				if(backConut == 1){
					Toast.makeText(this, "再按一次退出“金陵工商”", Toast.LENGTH_LONG).show();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							backConut = 0;
						}
					}, 4000);
				}else if(backConut == 2){
					MyActivityManager.getInstance().exit();
					backConut = 0;
				}
			break;
		case KeyEvent.KEYCODE_MENU:
			if (sm.isMenuShowing()) {
				toggle();
			} else {
				showMenu();
			}
			break;
		}
		return true;
	
	}

}
