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
 * @author sunhaifeng 封装退出全部应用程序方法的类
 */
public class ExitShowTips {

	/**
	 * 2014-2-12
	 * 
	 * @author sunhaifeng
	 * @param activity
	 *            封装退出弹出对话框的功能，并调用Activity的管理类的exit方法，使Activity栈中的所有Activity都退出
	 */
	public static void showTips(Context c) {
		final AlertDialog dlg = new AlertDialog.Builder(c).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
		window.setContentView(R.layout.shrew_exit_dialog);
		// 为确认按钮添加事件,执行退出应用操作
		Button ok = (Button) window.findViewById(R.id.btn_ok);
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				MyActivityManager.getInstance().exit(); // 退出应用...
			}
		});

		// 关闭alert对话框架
		Button cancel = (Button) window.findViewById(R.id.btn_cancel);
		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.cancel();
			}
		});

	}

}
