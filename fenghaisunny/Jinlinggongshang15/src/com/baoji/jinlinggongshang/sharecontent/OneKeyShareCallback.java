package com.baoji.jinlinggongshang.sharecontent;

import java.util.HashMap;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * OneKeyShareCallback是快捷分享功能的一个“外部回调”示例。
 *演示了如何通过添加extra的方法，将快捷分享的分享结果回调到
 *外面来做自定义处理。
 */
public class OneKeyShareCallback implements PlatformActionListener {

	public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
		System.out.println(res.toString());
		// 在这里添加分享成功的处理代码
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();
		// 在这里添加分享失败的处理代码
	}

	public void onCancel(Platform plat, int action) {
		// 在这里添加取消分享的处理代码
	}

}
