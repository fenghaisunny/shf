package com.baoji.jiuguijiu.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.MD5;

public class RegisterNormalActivity extends BaseActivity implements
		OnClickListener {

	private EditText username, email, password, re_password;
	private String username_str, email_str, password_str, re_password_str;
	private Button register;
	private String url = "http://www.jiukuaisong.cn/api/register.ashx";
	private Button back;
	private SharedPreferences userInfosp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_register_normal);
		findViewById();
		register.setOnClickListener(this);
		back.setOnClickListener(this);
		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
	}

	@Override
	protected void findViewById() {

		username = (EditText) findViewById(R.id.username);
		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		re_password = (EditText) findViewById(R.id.re_password);
		register = (Button) findViewById(R.id.register);
		back = (Button) findViewById(R.id.back);
	}

	@Override
	protected void initView() {
		username_str = username.getText().toString();
		email_str = email.getText().toString();
		password_str = MD5.stringToMD5(password.getText().toString());
		re_password_str = MD5.stringToMD5(re_password.getText().toString());
		System.out.println(username_str);
		System.out.println(email_str);
		System.out.println(password_str);
		System.out.println(re_password_str);
	}

	public boolean register() {

		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpPost mPost = new HttpPost(url);

		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("uname", username_str));
		pairs.add(new BasicNameValuePair("pwd", password_str));
		pairs.add(new BasicNameValuePair("email", email_str));

		try {
			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			HttpResponse response = mHttpClient.execute(mPost);
			int res = response.getStatusLine().getStatusCode();
			System.out.println(res);

			if (res == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String info = EntityUtils.toString(entity);
					System.out.println("info-----------" + info);
					try {
						JSONObject json = new JSONObject(info);
						String registerSuccess = json.getString("msg");
						System.out.println(registerSuccess);
						if (!registerSuccess.equals("1")) {
							return false;
						} else
							return true;

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		if (v.getId() == R.id.register) {
			initView();
			if (username_str.equals("") || email_str.equals("")
					|| password_str.equals("")) {
				Toast.makeText(this, "输入有空内容，请重新输入！", Toast.LENGTH_SHORT)
						.show();
			} else if (username_str.length() != 11) {
				Toast.makeText(this, "手机号码格式不正确，请重新输入！", Toast.LENGTH_SHORT)
						.show();
			} else if (!(password_str.equals(re_password_str))) {
				Toast.makeText(this, "确认密码与首次输入不一致！", Toast.LENGTH_SHORT)
						.show();
			} else if (!email_str.contains("@")) {
				Toast.makeText(this, "邮箱格式不正确，请重新输入！", Toast.LENGTH_SHORT)
						.show();
			} else if (register()) {
				Toast.makeText(this, "注册成功！", Toast.LENGTH_SHORT).show();
				Intent login = new Intent(RegisterNormalActivity.this,
						LoginActivity.class);
				login.putExtra("username", username_str);
				login.putExtra("email", email_str);
				login.putExtra("password", password.getText().toString());
				login.setAction("register");
				this.startActivity(login);
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			} else
				Toast.makeText(this, "该用户名已被注册！", Toast.LENGTH_SHORT).show();

		} else if (v.getId() == R.id.back) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		default:
			break;
		}
		return true;
	}
}
