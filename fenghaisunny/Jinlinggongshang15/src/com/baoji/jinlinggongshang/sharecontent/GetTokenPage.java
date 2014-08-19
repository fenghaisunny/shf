package com.baoji.jinlinggongshang.sharecontent;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.TitleLayout;

import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.ShareActivity;

/** ��ʾ��Ȩ����ȡ��ȡAccessToken */
public class GetTokenPage extends Activity implements Callback,
		OnClickListener, PlatformActionListener {
	private TitleLayout llTitle;
	private Handler handler;
	private AuthAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler(this);
		setContentView(R.layout.page_get_access_token);
		llTitle = (TitleLayout) findViewById(R.id.llTitle);
		llTitle.getBtnBack().setOnClickListener(this);
		llTitle.getTvTitle().setText(R.string.demo_get_access_token);

		ListView lvPlats = (ListView) findViewById(R.id.lvPlats);
		lvPlats.setSelector(new ColorDrawable());
		adapter = new AuthAdapter(this);
		lvPlats.setAdapter(adapter);
	}

	/** ��ʾ���߼����� */
	public void onClick(View v) {
		if (v.equals(llTitle.getBtnBack())) {
			finish();
		}
	}

	public void onComplete(Platform plat, int action,
			HashMap<String, Object> res) {
		Message msg = new Message();
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onCancel(Platform plat, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	/** ͨ��Toast��ʾ������� */
	public boolean handleMessage(Message msg) {
		Platform plat = (Platform) msg.obj;
		String text = ShareActivity.actionToString(msg.arg2);
		switch (msg.arg1) {
			case 1: {
				// �ɹ�
				text = plat.getName() + " get token: " + plat.getDb().getToken();
			}
			break;
			case 2: {
				// ʧ��
				text = plat.getName() + " caught error";
			}
			break;
			case 3: {
				// ȡ��
				text = plat.getName() + " authorization canceled";
			}
			break;
		}

		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return false;
	}

	private static class AuthAdapter extends BaseAdapter implements OnClickListener {
		private GetTokenPage page;
		private ArrayList<Platform> platforms;

		public AuthAdapter(GetTokenPage page) {
			this.page = page;

			// ��ȡƽ̨�б�
			Platform[] tmp = ShareSDK.getPlatformList(page);
			platforms = new ArrayList<Platform>();
			for (Platform p : tmp) {
				String name = p.getName();
				if (!ShareCore.canAuthorize(p.getContext(), name)) {
					continue;
				}
				platforms.add(p);
			}
		}

		public int getCount() {
			return platforms == null ? 0 : platforms.size();
		}

		public Platform getItem(int position) {
			return platforms.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(page, R.layout.button_list_item, null);
			}

			Platform plat = getItem(position);
			Button btn = (Button) convertView.findViewById(R.id.btn);
			btn.setOnClickListener(this);
			btn.setText(page.getString(R.string.get_token_format, getName(plat)));
			btn.setTag(plat);

			return convertView;
		}

		private String getName(Platform plat) {
			if (plat == null) {
				return "";
			}

			String name = plat.getName();
			if (name == null) {
				return "";
			}

			int resId = cn.sharesdk.framework.utils.R.getStringRes(page, plat.getName());
			return page.getString(resId);
		}

		public void onClick(View v) {
			Platform plat = (Platform) v.getTag();
			plat.setPlatformActionListener(page);
			plat.authorize();
		}

	}

}
