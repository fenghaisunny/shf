package com.baoji.jinlinggongshang.sharecontent;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.twitter.Twitter;

import com.baoji.jinlinggongshang.R;

/**
 * 快捷分享项目现在添加为不同的平台添加不同分享内容的方法。
 *本类用于演示如何区别新浪微博的分享内容和其他平台分享内容。
 *本类会在{@link DemoPage#showShare(boolean, String)}方法
 *中被调用。
 */
public class ShareContentCustomizeDemo implements ShareContentCustomizeCallback {

	public void onShare(Platform platform, ShareParams paramsToShare) {
		// 改写twitter分享内容中的text字段，否则会超长，
		// 因为twitter会将图片地址当作文本的一部分去计算长度
		if (Twitter.NAME.equals(platform.getName())) {
			Twitter.ShareParams sp = (Twitter.ShareParams) paramsToShare;
			sp.text = platform.getContext().getString(R.string.share_content_short);
		}
	}

}
