package com.baoji.jinlinggongshang.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtils
{

    public static InputStream getInputStream(String path)
    {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;

        try
        {
            URL url = new URL(path);
            if (null != url)
            {
                httpURLConnection = (HttpURLConnection) url.openConnection();

                // ������������ĳ�ʱʱ��
                httpURLConnection.setConnectTimeout(5000);

                // ��������
                httpURLConnection.setDoInput(true);

                // ���ñ���Http����ʹ�õķ���
                httpURLConnection.setRequestMethod("GET");

                if (200 == httpURLConnection.getResponseCode())
                {
                    // �ӷ��������һ��������
                    inputStream = httpURLConnection.getInputStream();

                }

            }
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return inputStream;
    }

}