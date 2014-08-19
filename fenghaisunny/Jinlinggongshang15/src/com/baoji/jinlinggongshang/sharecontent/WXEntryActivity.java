package com.baoji.jinlinggongshang.sharecontent;

import android.content.Intent;
import android.widget.Toast;
import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

import com.baoji.jinlinggongshang.ShareActivity;

public class WXEntryActivity extends WechatHandlerActivity {
	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		startActivity(new Intent(this, ShareActivity.class));
	}
	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

}
