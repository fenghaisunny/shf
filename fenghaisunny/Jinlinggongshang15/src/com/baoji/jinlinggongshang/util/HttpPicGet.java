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
			//��ʼ��һ��URL����
			URL url = new URL(data);
			//���HTTPConnection�������Ӷ���
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setConnectTimeout(5*1000);
			connection.setDoInput(true);
			connection.connect();
			//�õ�������
			InputStream is = connection.getInputStream();
			Log.i("TAG", "*********inputstream**"+is);
			bitmap = BitmapFactory.decodeStream(is);
			Log.i("TAG", "*********bitmap****"+bitmap);
			//�ر�������
			is.close();
			//�ر�����
			connection.disconnect();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;
		
	}

}
