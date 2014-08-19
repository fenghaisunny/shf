package com.baoji.jinlinggongshang.util;

import android.media.AudioManager;
import android.os.Handler;

public class VolumeController {
	public static void volUp(final AudioManager audioManager ){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				audioManager.adjustVolume(AudioManager.ADJUST_RAISE, 1);
			}
		}, 0);
	}
	public static void volDown(final AudioManager audioManager ){
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				audioManager.adjustVolume(AudioManager.ADJUST_LOWER, 1);
			}
		}, 0);
	}	
	
}
