package com.baoji.jinlinggongshang.sharecontent;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.twitter.Twitter;

import com.baoji.jinlinggongshang.R;

/**
 * ��ݷ�����Ŀ�������Ϊ��ͬ��ƽ̨��Ӳ�ͬ�������ݵķ�����
 *����������ʾ�����������΢���ķ������ݺ�����ƽ̨�������ݡ�
 *�������{@link DemoPage#showShare(boolean, String)}����
 *�б����á�
 */
public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

	public void onShare(Platform platform, ShareParams paramsToShare) {
		// ��дtwitter���������е�text�ֶΣ�����ᳬ����
		// ��Ϊtwitter�ὫͼƬ��ַ�����ı���һ����ȥ���㳤��
		if (Twitter.NAME.equals(platform.getName())) {
			Twitter.ShareParams sp = (Twitter.ShareParams) paramsToShare;
			sp.text = platform.getContext().getString(R.string.share_content_short);
		}
	}

}
