package com.baoji.jinlinggongshang.sharecontent;

import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * OneKeyShareCallback�ǿ�ݷ����ܵ�һ�����ⲿ�ص���ʾ����
 *��ʾ�����ͨ�����extra�ķ���������ݷ���ķ������ص���
 *���������Զ��崦��
 */
public class OneKeyShareCallback implements PlatformActionListener {

	public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
		System.out.println(res.toString());
		// ��������ӷ���ɹ��Ĵ������
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();
		// ��������ӷ���ʧ�ܵĴ������
	}

	public void onCancel(Platform plat, int action) {
		// ���������ȡ������Ĵ������
	}

}
