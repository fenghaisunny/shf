package com.baoji.jinlinggongshang;


import android.os.Bundle;
import android.widget.TextView;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class XintianyouActivity extends NoodleActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		imageUrls = new String[]{
				"http://192.168.2.6:8080/JsonWeb01/images/xintianyou01.png",
				"http://192.168.2.6:8080/JsonWeb01/images/xintianyou02.png",
				"http://192.168.2.6:8080/JsonWeb01/images/xintianyou03.png",
				"http://192.168.2.6:8080/JsonWeb01/images/xintianyou04.png" };		
		super.onCreate(savedInstanceState);
		//loadImage("http://192.168.2.6:8080/JsonWeb01/images/xintianyou01.png");
		MyActivityManager.getInstance().addActivity(this);
		
		
		int ran = (int) (Math.random() * 10);
		String musicUrls[] = {
				"http://zhangmenshiting.baidu.com/data2/music/87718421/87718421.mp3?xcode=fcdbac7c526a874e838067c52cd193fc4f5e48fa25a532b2",
				"http://zhangmenshiting.baidu.com/data2/music/7341387/7341387.mp3?xcode=47aec4719ab4f57addad2eeafb8fc0b0551ed74f97540da6",
				"http://zhangmenshiting.baidu.com/data2/music/64563852/64563852.mp3?xcode=418525ce120d14781bf7bc8017e2ccc14f5500234571548e",
				"http://zhangmenshiting.baidu.com/data2/music/10233439/10233439.mp3?xcode=1b2de28f0ae45a25c1c594f2a90947a075a17bbcd4a2fbbf",
				"http://zhangmenshiting.baidu.com/data2/music/35420495/35420495.mp3?xcode=ce9865ee8d37395ca2667198a11883a35c1e649d511ced50",
				"http://zhangmenshiting.baidu.com/data2/music/44061909/44061909.mp3?xcode=80fe86b52ac423743e20658e1a83e44e4325f469c6ba5ddb",
				"http://zhangmenshiting.baidu.com/data2/music/57130533/57130533.mp3?xcode=c67448e7039b5f17e8720799fa5714cf1489e1db000d3934",
				"http://zhangmenshiting.baidu.com/data2/music/64022204/64022204.mp3?xcode=3521d93742f03f25e787c9c22ea13d1cec0497093d29d6f3",
				"http://zhangmenshiting.baidu.com/data2/music/89553687/89553687.mp3?xcode=c28a2ef9e0b2ad8629f64cda1c28cdc99a890100bd4b099e",
				"http://zhangmenshiting.baidu.com/data2/music/46729925/46729925.mp3?xcode=c67448e7039b5f1789d48c6e1e7342cc44b52c81891b10c8" };
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
		imageLoader.displayImage("http://192.168.2.6:8080/JsonWeb01/images/xintianyoupic.png", folkpic, options);
		folktitle.setText("信天游");
		folkdecs.setText(getResources().getString(R.string.xintianyoudec));
		TextView play = (TextView)findViewById(R.id.play);
		play.setText("随机试听");
		
	}
	

}