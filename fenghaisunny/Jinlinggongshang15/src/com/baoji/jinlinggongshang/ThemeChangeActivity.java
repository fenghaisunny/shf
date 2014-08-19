package com.baoji.jinlinggongshang;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baoji.jinlinggongshang.util.MyActivityManager;

/**2014-3-5
 * @author sunhaifeng
 * 实现更换主页面风格
 *
 */
public class ThemeChangeActivity extends Activity implements OnClickListener{

	private RadioButton theme01, theme02, theme03, theme04;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.themechange);
		MyActivityManager.getInstance().addActivity(this);

		RadioGroup radio = (RadioGroup) findViewById(R.id.radioGroup);
		theme01 = (RadioButton) findViewById(R.id.theme01);
		theme02 = (RadioButton) findViewById(R.id.theme02);
		theme03 = (RadioButton) findViewById(R.id.theme03);
		theme04 = (RadioButton) findViewById(R.id.theme04);
		
		ImageButton back = (ImageButton)findViewById(R.id.back);
		back.setOnClickListener(this);
		ImageButton category = (ImageButton)findViewById(R.id.category);
		category.setVisibility(View.GONE);
		TextView tv = (TextView)findViewById(R.id.titleBar_tv);
		tv.setVisibility(View.VISIBLE);
		tv.setText("更换主页风格");
		
		//读出当前表示主题的thememode，以作处理
		SharedPreferences pre = getSharedPreferences("thememode",
				MODE_WORLD_READABLE);
		int thememode = pre.getInt("theme", 1);
		//实现checkbox选中状态的保存
		switch (thememode) {
		case 1:
			theme01.setChecked(true);
			break;
		case 2:
			theme02.setChecked(true);
			break;
		case 3:
			theme03.setChecked(true);
			break;
		case 4:
			theme04.setChecked(true);
			break;
		}

		radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			// group是当前被点击的组，checkedId是当前组中被选中RadioButton的id
			@SuppressLint("WorldWriteableFiles")
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == theme01.getId()) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"thememode", MODE_WORLD_WRITEABLE).edit();
					editor.putInt("theme", 1);
					editor.commit();
					Intent intent = new Intent(ThemeChangeActivity.this,MainActivity.class);
					startActivity(intent);
					ThemeChangeActivity.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				} else if (checkedId == theme02.getId()) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"thememode", MODE_WORLD_WRITEABLE).edit();
					editor.putInt("theme", 2);
					editor.commit();
					Intent intent = new Intent(ThemeChangeActivity.this,MainActivity.class);
					startActivity(intent);
					ThemeChangeActivity.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				} else if (checkedId == theme03.getId()) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"thememode", MODE_WORLD_WRITEABLE).edit();
					editor.putInt("theme", 3);
					editor.commit();
					Intent intent = new Intent(ThemeChangeActivity.this,MainActivity.class);
					startActivity(intent);
					ThemeChangeActivity.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				} else if (checkedId == theme04.getId()) {
					SharedPreferences.Editor editor = getSharedPreferences(
							"thememode", MODE_WORLD_WRITEABLE).edit();
					editor.putInt("theme", 4);
					editor.commit();
					Intent intent = new Intent(ThemeChangeActivity.this,MainActivity.class);
					startActivity(intent);
					ThemeChangeActivity.this.finish();
					overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.back){
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
			this.finish();
			overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
		}
		return super.onKeyDown(keyCode, event);
	}
	
}