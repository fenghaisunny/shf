package com.baoji.jinlinggongshang.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class HttpPicGet {
	
	public static Bitmap getHttpBitmap(String data)
	{
		Bitmap bitmap = null;
		try
		{
			//初始化一个URL对象
			URL url = new URL(data);
			//获得HTTPConnection网络连接对象
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5*1000);
			connection.setDoInput(true);
			connection.connect();
			//得到输入流
			InputStream is = connection.getInputStream();
			Log.i("TAG", "*********inputstream**"+is);
			bitmap = BitmapFactory.decodeStream(is);
			Log.i("TAG", "*********bitmap****"+bitmap);
			//关闭输入流
			is.close();
			//关闭连接
			connection.disconnect();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
		
	}

}
