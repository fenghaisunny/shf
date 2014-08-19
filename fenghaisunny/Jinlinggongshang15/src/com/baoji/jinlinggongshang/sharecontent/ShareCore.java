package com.baoji.jinlinggongshang.sharecontent;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map.Entry;

import android.content.Context;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

/**
 * ShareCore�ǿ�ݷ����ʵ�ʳ��ڣ�����ʹ���˷���ķ�ʽ����ϴ��ݽ�����HashMap��
 *����{@link ShareParams}���󣬲�ִ�з���ʹ��ݷ�������Ҫ����Ŀ��ƽ̨
 */
public class ShareCore {
	private ShareContentCustomizeCallback customizeCallback;

	/** �������ڷ�������У����ݲ�ͬƽ̨�Զ���������ݵĻص� */
	public void setShareContentCustomizeCallback(ShareContentCustomizeCallback callback) {
		customizeCallback = callback;
	}

	/**
	 * ��ָ��ƽ̨��������
	 * <p>
	 * <b>ע�⣺</b><br>
	 * ����data�ļ�ֵ��Ҫ�ϸ���{@link ShareParams}��ͬ��������ֶ���������
	 *�����޷�������ֶΣ�Ҳ�޷�������ֵ��
	 */
	public boolean share(Platform plat, HashMap<String, Object> data) {
		if (plat == null || data == null) {
			return false;
		}

		Platform.ShareParams sp = null;
		try {
			sp = getShareParams(plat, data);
		} catch(Throwable t) {
			sp = null;
		}

		if (sp != null) {
			if (customizeCallback != null) {
				customizeCallback.onShare(plat, sp);
			}
			plat.share(sp);
		}
		return true;
	}

	private Platform.ShareParams getShareParams(Platform plat,
			HashMap<String, Object> data) throws Throwable {
		String className = plat.getClass().getName() + "$ShareParams";
		Class<?> cls = Class.forName(className);
		if (cls == null) {
			return null;
		}

		Object sp = cls.newInstance();
		if (sp == null) {
			return null;
		}

		for (Entry<String, Object> ent : data.entrySet()) {
			try {
				Field fld = cls.getField(ent.getKey());
				if (fld != null) {
					fld.setAccessible(true);
					fld.set(sp, ent.getValue());
				}
			} catch(Throwable t) {}
		}

		return (Platform.ShareParams) sp;
	}

	/** �ж�ָ��ƽ̨�Ƿ�ʹ�ÿͻ��˷��� */
	public static boolean isUseClientToShare(Context context, String platform) {
		if ("Wechat".equals(platform) || "WechatMoments".equals(platform)
				|| "ShortMessage".equals(platform) || "Email".equals(platform)
				|| "GooglePlus".equals(platform) || "QQ".equals(platform)
				|| "Pinterest".equals(platform) || "Instagram".equals(platform)) {
			return true;
		} else if ("Evernote".equals(platform)) {
			Platform plat = ShareSDK.getPlatform(context, platform);
			if ("true".equals(plat.getDevinfo("ShareByAppClient"))) {
				return true;
			}
		}

		return false;
	}

	/** �ж�ָ��ƽ̨�Ƿ����������Ȩ */
	public static boolean canAuthorize(Context context, String platform) {
		if ("Wechat".equals(platform) || "WechatMoments".equals(platform)
				|| "ShortMessage".equals(platform) || "Email".equals(platform)
				|| "GooglePlus".equals(platform) || "QQ".equals(platform)
				|| "Pinterest".equals(platform)) {
			return false;
		}
		return true;
	}

}
