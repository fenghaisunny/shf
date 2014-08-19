package com.baoji.jinlinggongshang.sharecontent;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
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

/**
 * ��ʾ��ȡ�û�����
 * <p>
 * ����ҳ��ʱ����һ��int���͵��ֶ�type�����ڱ�ǻ�ȡ�Լ������ϣ�type = 0��
 *���Ǳ��˵����ϣ�type = 1����������Ի�ȡ���˵����ϣ�ʾ��������ȡ��ͬ
 *ƽ̨Share SDK�Ĺٷ��ʺŵ����ϡ�
 * <p>
 * ������ϻ�ȡ�ɹ�����ͨ��{@link JsonPage}չʾ
 */
public class GetInforPage extends Activity implements Callback,
		OnClickListener, PlatformActionListener {
	private TitleLayout llTitle;
	private Handler handler;
	private PlatAdapter adapter;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int type = getIntent().getIntExtra("type", 0);
		handler = new Handler(this);
		setContentView(R.layout.page_get_user_info);
		llTitle = (TitleLayout) findViewById(R.id.llTitle);
		llTitle.getBtnBack().setOnClickListener(this);
		llTitle.getTvTitle().setText(type == 0 ? R.string.demo_get_my_info
				: R.string.demo_get_other_info);

		ListView lvPlats = (ListView) findViewById(R.id.lvPlats);
		lvPlats.setSelector(new ColorDrawable());
		adapter = new PlatAdapter(this);
		adapter.setType(type);
		lvPlats.setAdapter(adapter);
	}

	/** �����ȡ���ϵ���ʾ���� */
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

		Message msg2 = new Message();
		msg2.what = 1;
		JsonUtils ju = new JsonUtils();
		String json = ju.fromHashMap(res);
		msg2.obj = ju.format(json);
		handler.sendMessage(msg2);
	}

	public void onError(Platform palt, int action, Throwable t) {
		t.printStackTrace();

		Message msg = new Message();
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = palt;
		handler.sendMessage(msg);
	}

	public void onCancel(Platform plat, int action) {
		Message msg = new Message();
		msg.arg1 = 3;
		msg.arg2 = action;
		msg.obj = plat;
		handler.sendMessage(msg);
	}

	/** ���������� */
	public boolean handleMessage(Message msg) {
		switch(msg.what) {
			case 1: {
				Intent i = new Intent(this, JsonPage.class);
				i.putExtra("title", llTitle.getTvTitle().getText().toString());
				i.putExtra("data", String.valueOf(msg.obj));
				startActivity(i);
			}
			break;
			default: {
				Platform plat = (Platform) msg.obj;
				String text = ShareActivity.actionToString(msg.arg2);
				switch (msg.arg1) {
					case 1: {
						// �ɹ�
						text = plat.getName() + " completed at " + text;
					}
					break;
					case 2: {
						// ʧ��
						text = plat.getName() + " caught error at " + text;
					}
					break;
					case 3: {
						// ȡ��
						text = plat.getName() + " canceled at " + text;
					}
					break;
				}

				Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return false;
	}

	private static class PlatAdapter extends BaseAdapter implements OnClickListener {
		private GetInforPage page;
		private ArrayList<Platform> platforms;
		// Ҫ��ȡ���ϵĶ���0���Լ������ϣ�1�����˵�����
		private int type;

		public PlatAdapter(GetInforPage page) {
			this.page = page;
		}

		public void setType(int type) {
			this.type = type;

			// ��ȡƽ̨�б�
			Platform[] tmp = ShareSDK.getPlatformList(page);
			platforms = new ArrayList<Platform>();

			if (type == 0) {
				for (Platform p : tmp) {
					String name = p.getName();
					if (!ShareCore.canAuthorize(p.getContext(), name)) {
						continue;
					}
					platforms.add(p);
				}
			}
			else {
				for (Platform p : tmp) {
					String name = p.getName();
					if ("SinaWeibo".equals(name) || "TencentWeibo".equals(name)) {
						platforms.add(p);
					}
					continue;
				}
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
			int resId = type == 0 ? R.string.get_user_info_format : R.string.get_other_info_format;
			btn.setText(page.getString(resId, getName(plat)));
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
			String name = plat.getName();
			plat.setPlatformActionListener(page);
			String account = null;
			if ("SinaWeibo".equals(name)) {
				account = ShareActivity.SDK_SINAWEIBO_UID;
			}
			else if ("TencentWeibo".equals(name)) {
				account = ShareActivity.SDK_TENCENTWEIBO_UID;
			}
			plat.showUser(type == 0 ? null : account);
		}

	}

}
