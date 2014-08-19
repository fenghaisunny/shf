package com.baoji.jinlinggongshang.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.baoji.jinlinggongshang.R;

/**
 * 2014-2-12
 * 
 * @author sunhaifeng ��װ�˳�ȫ��Ӧ�ó��򷽷�����
 */
public class ExitShowTips {

	/**
	 * 2014-2-12
	 * 
	 * @author sunhaifeng
	 * @param activity
	 *            ��װ�˳������Ի���Ĺ��ܣ�������Activity�Ĺ������exit������ʹActivityջ�е�����Activity���˳�
	 */
	public static void showTips(Context c) {
		final AlertDialog dlg = new AlertDialog.Builder(c).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** ��Ҫ����������ʵ������Ч����.
		// ���ô��ڵ�����ҳ��,shrew_exit_dialog.xml�ļ��ж���view����
		window.setContentView(R.layout.shrew_exit_dialog);
		// Ϊȷ�ϰ�ť����¼�,ִ���˳�Ӧ�ò���
		Button ok = (Button) window.findViewById(R.id.btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MyActivityManager.getInstance().exit(); // �˳�Ӧ��...
			}
		});

		// �ر�alert�Ի����
		Button cancel = (Button) window.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});

	}

}
