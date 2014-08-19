package com.baoji.jinlinggongshang;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.baoji.jinlinggongshang.twodimen.CaptureActivity;
import com.baoji.jinlinggongshang.util.ExitShowTips;
import com.baoji.jinlinggongshang.util.MyActivityManager;
import com.baoji.jinlinggongshang.util.VolumeController;

/**
 * ${2014-02-12}
 * 
 * @author SunHaifeng
 * 
 */

public class QinqiangActivity extends NoodleActivity {
	
	
	String musicUrls[] = {
			"http://192.168.2.6:8080/JsonWeb01/music/01.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/02.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/03.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/04.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/05.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/06.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/07.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/08.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/09.mp3",
			"http://192.168.2.6:8080/JsonWeb01/music/10.mp3" };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		MyActivityManager.getInstance().addActivity(this);
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/qinqiang01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/qinqiang02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/qinqiang03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/qinqiang04.png" };
		playBtnTitle = "随机试听";
		play.setText(playBtnTitle);
		folktitle.setVisibility(View.GONE);
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/qinqiangpic.png", folkpic, options);
		folkdecs.setText(getResources().getString(R.string.qinqiangdec));

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// 如果调用父类的方法，则会使一个View的点击触发两个不同的事件，而一旦没有调用，则需要对每个View的点击进行重新监听，不然子类的View的点击将不会响应
		// super.onClick(v);
		switch (v.getId()) {
		case R.id.category:
			slideBarHandler.post(mScrollToButton);
			break;
		case R.id.back:
			if (isPlaying_internet) {
				mediaPlayer01.stop();
			}
			this.finish();
			overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			break;
		case R.id.getBuffer:
			int ran = (int) (Math.random() * 10);
			switch (ran) {
			case 0:
				musicUrl = musicUrls[0];
				break;
			case 1:
				musicUrl = musicUrls[1];
				break;
			case 2:
				musicUrl = musicUrls[2];
				break;
			case 3:
				musicUrl = musicUrls[3];
				break;
			case 4:
				musicUrl = musicUrls[4];
				break;
			case 5:
				musicUrl = musicUrls[5];
				break;
			case 6:
				musicUrl = musicUrls[6];
				break;
			case 7:
				musicUrl = musicUrls[7];
				break;
			case 8:
				musicUrl = musicUrls[8];
				break;
			case 9:
				musicUrl = musicUrls[9];
				break;
			}
			if (!isPlaying_internet) {
				bufferValueTextView.setVisibility(View.VISIBLE);
				bufferValueTextView.setText("正在缓冲……");
				mediaPlay.post(httpMediaPlayer);
			} else {
				bufferValueTextView.setVisibility(View.GONE);
				mediaPlayer01.stop();
				isPlaying_internet = false;
				play.setText(playBtnTitle);
			}
			break;
		case R.id.exit:
			ExitShowTips.showTips(this);
			break;
		case R.id.twodimen_sli:
			if (isPlaying_internet) {
				mediaPlayer01.stop();
			}
			Intent twodimen = new Intent(this, CaptureActivity.class);
			startActivityForResult(twodimen, TWODIMEN);
			overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
			break;
		case R.id.camera_sli:
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");// 获取当前时间，进一步转化为字符串
			date = new Date();
			str = format.format(date);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					Environment.getExternalStorageDirectory(), str + ".jpg")));
			startActivityForResult(intent, CAMERA);
			overridePendingTransition(R.anim.demo_scale, R.anim.demo_translate);
			break;
		case R.id.call_sli:
			Intent callintent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:")); 
			startActivity(callintent);
			overridePendingTransition(R.anim.rotate_fade_in, R.anim.rotate_fade_out);
			break;	
		case R.id.theme:
			Intent themeintent = new Intent(this,
					ThemeChangeActivity.class);
			startActivity(themeintent);
			overridePendingTransition(R.anim.rotate_fade_in,
					R.anim.rotate_fade_out);
			break;			
			
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		switch (keyCode) {
		case KeyEvent.KEYCODE_MENU:
			slideBarHandler.post(mScrollToButton);
			break;
		case KeyEvent.KEYCODE_BACK:
			if (isOpen) {
				slideBarHandler.post(mScrollToButton);
			} else {
				if (isPlaying_internet) {
					mediaPlayer01.stop();
				}
				this.finish();
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
			break;
		case KeyEvent.KEYCODE_VOLUME_UP:
			VolumeController.volUp(audioManager);
			break;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			VolumeController.volDown(audioManager);
			break;
		}
		return true;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if(data ==null){
			return;
		}
		if (requestCode == CAMERA) {
			// 设置文件保存路径这里放在根目录下
			File picture = new File(Environment.getExternalStorageDirectory()
					+ "/" + str + ".jpg");
			Toast.makeText(this, "拍摄的照片将放在SD卡根目录哦亲！",
					Toast.LENGTH_SHORT).show();
		}else if(requestCode == TWODIMEN){
			Intent intent = new Intent(this, TransportationActivity.class);
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			intent.putExtra("result", scanResult);
			intent.putExtra("twoCall", true);
			startActivity(intent);
			overridePendingTransition(R.anim.rotate_fade_in, R.anim.rotate_fade_out);
		}
	}

}
