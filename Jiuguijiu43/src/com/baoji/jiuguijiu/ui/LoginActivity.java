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
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.baoji.jiuguijiu.AppManager;
import com.baoji.jiuguijiu.R;
import com.baoji.jiuguijiu.ui.base.BaseActivity;
import com.baoji.jiuguijiu.utils.AnimCommon;
import com.baoji.jiuguijiu.utils.MD5;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final String Tag = "LoginActivity";
	private LoginActivity loginActivity = null;
	private ImageView loginLogo, login_more;
	private TextView login_title, coins,forget;
	private EditText loginaccount, loginpassword;
	private ToggleButton isShowPassword;
	private boolean isDisplayflag = false;// 是否显示密码
	private String getpassword;
	private Button loginBtn, register, checkout;
	private CheckBox rem_pw, auto_login;
	private Intent mIntent;
	public static String MOBILE_SERVERS_URL = "http://mserver.e-cology.cn/servers.do";
	private EditText userName, password;
	private String userNameValue, passwordValue;
	private SharedPreferences userInfosp;
	private SharedPreferences loginSettingsp;

	private Boolean loginSuccess;
	private Boolean isFromRegister = false;
	private Boolean isFromActivity = false;

	public LinearLayout login_area;
	private int backConut = 0;
	private Intent way;
	private TextView jifen;

	private String reg_username = "";
	private String reg_password = "";
	private Button back;

	String useraddr = "";
	int usercoins = 0;
	String imageUrl = "";
	JSONObject jsonObject = null;
	String url = "http://www.jiukuaisong.cn/api/login.ashx";
	HttpResponse response = null;

	private int responsecode = 0;
	private ProgressDialog proDialog;
	Thread loginThread = null;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginActivity = LoginActivity.this;
		findViewById();
		initView();
		if (userInfosp.getBoolean("isLogined", false)) {
			jifen.setVisibility(View.VISIBLE);
			login_area.setVisibility(View.GONE);
			login_title.setText("欢迎您" + userInfosp.getString("username", "").substring(0, 5).concat("…"));
			checkout.setVisibility(View.VISIBLE);
			coins.setVisibility(View.VISIBLE);
			coins.setText("您的积分：" + userInfosp.getInt("coins", 0));
		}
		way = getIntent();
		if (way != null && way.getAction() != null) {
			isFromActivity = true;
			back.setVisibility(View.VISIBLE);
			checkout.setVisibility(View.GONE);
			// 获取从注册界面跳转而来传递的会员信息。
			if (way.getAction().equals("register")) {
				isFromRegister = true;
				reg_username = way.getStringExtra("username");
				reg_password = way.getStringExtra("password");
				System.out.println(reg_username != null);
				userName.setText(reg_username);
				password.setText(reg_password);
			} else if (way.getAction().equals("topLogin")) {
				if (loginSettingsp.getBoolean("ISCHECK", false)) {
					// 设置默认是记录密码状态
					rem_pw.setChecked(true);
					userName.setText(loginSettingsp.getString("USER_NAME", ""));
					password.setText(loginSettingsp.getString("PASSWORD", ""));
					// 判断自动登陆多选框状态
					if (loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
						// 设置默认是自动登录状态
						auto_login.setChecked(true);
					}
				}
			}
		} else {
			if (loginSettingsp.getBoolean("ISCHECK", false)) {
				// 设置默认是记录密码状态
				rem_pw.setChecked(true);
				userName.setText(loginSettingsp.getString("USER_NAME", ""));
				password.setText(loginSettingsp.getString("PASSWORD", ""));
				// 判断自动登陆多选框状态
				if (loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
					// 设置默认是自动登录状态
					auto_login.setChecked(true);
				}
			}
		}

		auto_login.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (auto_login.isChecked()) {
					System.out.println("自动登录已选中");
					loginSettingsp.edit().putBoolean("AUTO_ISCHECK", true)
							.commit();
				} else {
					System.out.println("自动登录没有选中");
					loginSettingsp.edit().putBoolean("AUTO_ISCHECK", false)
							.commit();
				}
			}
		});

		// 监听记住密码多选框按钮事件
		rem_pw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (rem_pw.isChecked()) {
					System.out.println("记住密码已选中");
					loginSettingsp.edit().putBoolean("ISCHECK", true).commit();
				} else {
					System.out.println("记住密码没有选中");
					loginSettingsp.edit().putBoolean("ISCHECK", false).commit();
				}
			}
		});
	}

	public void netStatueCheck() {
		ConnectivityManager connectivityManager = (ConnectivityManager) LoginActivity.this
				.getSystemService(LoginActivity.this.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			Toast.makeText(LoginActivity.this, "未连接网络，请打开网络连接",
					Toast.LENGTH_SHORT).show();
		} else {
			boolean isWifiConnected = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			if (!isWifiConnected) {
				Toast.makeText(LoginActivity.this, "建议您使用Wifi连接以节省流量",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void findViewById() {

		userInfosp = this.getSharedPreferences("userInfo",
				Context.MODE_WORLD_READABLE);
		loginSettingsp = this.getSharedPreferences("login_setting",
				Context.MODE_WORLD_READABLE);
		login_area = (LinearLayout) findViewById(R.id.login_area);
		login_title = (TextView) findViewById(R.id.login_title);
		userName = (EditText) this.findViewById(R.id.loginaccount);
		password = (EditText) this.findViewById(R.id.loginpassword);
		rem_pw = (CheckBox) this.findViewById(R.id.cb_mima);
		auto_login = (CheckBox) this.findViewById(R.id.cb_auto);
		loginBtn = (Button) this.findViewById(R.id.login);
		checkout = (Button) this.findViewById(R.id.checkout);
		loginLogo = (ImageView) this.findViewById(R.id.logo);
		// login_more=(ImageView)this.findViewById(R.id.login_more);
		loginaccount = (EditText) this.findViewById(R.id.loginaccount);
		loginpassword = (EditText) this.findViewById(R.id.loginpassword);
		jifen = (TextView) findViewById(R.id.jifen);

		back = (Button) findViewById(R.id.back);
		isShowPassword = (ToggleButton) this.findViewById(R.id.isShowPassword);
		loginBtn = (Button) this.findViewById(R.id.login);
		register = (Button) this.findViewById(R.id.register);
		coins = (TextView) findViewById(R.id.coins);
		forget = (TextView) findViewById(R.id.forget);
		getpassword = loginpassword.getText().toString();
	}

	@Override
	protected void initView() {

		register.setOnClickListener(this);
		back.setOnClickListener(this);
		forget.setOnClickListener(this);
		isShowPassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						Log.i(Tag, "开关按钮状态=" + isChecked);

						if (isChecked) {
							// 隐藏
							loginpassword.setInputType(0x90);
							// loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
						} else {
							// 明文显示
							loginpassword.setInputType(0x81);
							// loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
						}
						Log.i("togglebutton", "" + isChecked);
						// loginpassword.postInvalidate();
					}
				});
		loginBtn.setOnClickListener(this);
		checkout.setOnClickListener(this);
		jifen.setOnClickListener(this);
	}

	Handler loginHandler = new Handler() {
		// @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (proDialog != null) {
					proDialog.dismiss();
					jifen.setVisibility(View.VISIBLE);
					login_area.setVisibility(View.GONE);
					login_title.setText("欢迎您" + userNameValue.substring(0, 5).concat("…"));
					checkout.setVisibility(View.VISIBLE);
					coins.setVisibility(View.VISIBLE);
					coins.setText("您的积分：" + usercoins);
				}
				Toast.makeText(LoginActivity.this, "登录成功!", Toast.LENGTH_SHORT)
						.show();
				break;
			case 0:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新输入!",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register:
			mIntent = new Intent(LoginActivity.this,
					RegisterNormalActivity.class);
			startActivity(mIntent);
			if (isFromActivity) {
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			} else
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);

			break;
		case R.id.login:
			netStatueCheck();
			userNameValue = userName.getText().toString();
			passwordValue = password.getText().toString();
			if (userNameValue.equals("") || passwordValue.equals("")) {
				Toast.makeText(loginActivity, "输入有空信息！", Toast.LENGTH_SHORT)
						.show();
			} else {
				proDialog = ProgressDialog.show(LoginActivity.this, "正在登陆..",
						"连接中..请稍后....", true, true);
				loginThread = new Thread(new LoginPostImageThread());
				loginThread.start();
			}
			break;
		case R.id.forget:
			Intent i = new Intent(this,ForgetActivity.class);
			this.startActivity(i);
			break;
		case R.id.checkout:
			userInfosp.edit().putBoolean("isLogined", false).commit();
			login_area.setVisibility(View.VISIBLE);
			jifen.setVisibility(View.GONE);
			userName.setText(loginSettingsp.getString("USER_NAME", ""));
			password.setText(loginSettingsp.getString("PASSWORD", ""));
			login_title.setText("会员中心");
			checkout.setVisibility(View.GONE);
			coins.setVisibility(View.GONE);
			break;
		case R.id.back:
			this.finish();
			if (isFromActivity) {
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			} else
				AnimCommon.set(R.anim.push_right_in, R.anim.push_left_out);
			break;
		case R.id.jifen:
			Intent jifen = new Intent(LoginActivity.this,
					JifenshangchengActivity.class);
			this.startActivity(jifen);
			if (isFromActivity) {
				overridePendingTransition(R.anim.push_down_in,
						R.anim.push_down_out);
			} else
				AnimCommon.set(R.anim.push_down_in, R.anim.push_down_out);
			break;
		default:
			break;
		}
	}

	class LoginPostImageThread implements Runnable {

		@Override
		public void run() {
			Looper.prepare();
			System.out.println(checkUser());
			if (checkUser()) {
				Editor editor = userInfosp.edit();
				editor.putBoolean("isLogined", true).commit();
				editor.putInt("coins", usercoins).commit();
				editor.putString("username" + "", userNameValue).commit();
				// 登录成功和记住密码框为选中状态才保存用户信息
				if (rem_pw.isChecked()) {
					if (!isFromRegister) {
						// 记住用户名、密码
						Editor editor01 = loginSettingsp.edit();
						editor01.putString("USER_NAME", userNameValue).commit();
						editor01.putString("PASSWORD", passwordValue).commit();
						editor01.commit();
					} else {
						Editor editor02 = loginSettingsp.edit();
						editor02.putString("USER_NAME", reg_username).commit();
						editor02.putString("PASSWORD", reg_password).commit();
						editor02.commit();
					}
				}
			} else {
				Toast.makeText(LoginActivity.this, "用户名或密码错误，请重新登录",
						Toast.LENGTH_LONG).show();
				userName.clearComposingText();
				password.clearComposingText();
			}

			System.out.println("在run()方法中打印服务器端返回的数据" + loginHandler);
			Message msg = loginHandler.obtainMessage();
			msg.what = responsecode;
			loginHandler.sendMessage(msg);
		}
	}

	private boolean checkUser() {

		username = userName.getText().toString();
		String pass = MD5.stringToMD5(password.getText().toString());

		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpPost mPost = new HttpPost(url);

		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("uname", username));
		pairs.add(new BasicNameValuePair("pwd", pass));
		System.out.println(pass);

		try {
			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		try {
			HttpResponse response = mHttpClient.execute(mPost);
			responsecode = response.getStatusLine().getStatusCode();
			System.out.println(responsecode);

			if (responsecode == 200) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					String info = EntityUtils.toString(entity);
					System.out.println("info-----------" + info);
					try {
						JSONObject json = new JSONObject(info);
						String flag = json.getString("msg");
						String data = json.getString("ui");
						System.out.println(data);

						usercoins = json.getInt("ui");
						System.out.println(usercoins);

						System.out.println(flag);

						if (flag.equals("1")) {
							loginSuccess = true;
						} else {
							loginSuccess = false;
						}
						System.out.println(loginSuccess);
						if (loginSuccess) {// 此处是在没有当前没有实现登陆验证的前提下为了让CheckUser()方法返回“true”而设置的临时判断
							responsecode = 1;
							return true;
						} else
							responsecode = 0;
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
		return false;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (userInfosp.getBoolean("isLogined", false)) {
			login_area.setVisibility(View.GONE);
			login_title.setText("欢迎您" + userInfosp.getString("username", "").substring(0,5).concat("…"));
			checkout.setVisibility(View.VISIBLE);
			coins.setVisibility(View.VISIBLE);
			coins.setText("您的积分：" + userInfosp.getInt("coins", 0));
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (userInfosp.getBoolean("isLogined", false)) {
			login_area.setVisibility(View.GONE);
			login_title.setText("欢迎您" + userInfosp.getString("username", "").substring(0, 5).concat("…"));
			checkout.setVisibility(View.VISIBLE);
			coins.setVisibility(View.VISIBLE);
			coins.setText("您的积分：" + userInfosp.getInt("coins", 0));
		}
		System.out.println("onPause");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if (userInfosp.getBoolean("isLogined", false)) {
			login_area.setVisibility(View.GONE);
			login_title.setText("欢迎您" + userInfosp.getString("username", "").substring(0, 5).concat("…"));
			checkout.setVisibility(View.VISIBLE);
			coins.setVisibility(View.VISIBLE);
			coins.setText("您的积分：" + userInfosp.getInt("coins", 0));
		}
		System.out.println("onRestart");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (userInfosp.getBoolean("isLogined", false)) {
			login_area.setVisibility(View.GONE);
			login_title.setText("欢迎您" + userInfosp.getString("username", "").substring(0, 5).concat("…"));
			checkout.setVisibility(View.VISIBLE);
			coins.setVisibility(View.VISIBLE);
			coins.setText("您的积分：" + userInfosp.getInt("coins", 0));
		}
		System.out.println("onStart");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (!(way != null && way.getAction() != null)) {
				backConut++;
				if (backConut == 1) {
					Toast.makeText(this, "再按一次退出“酒快送”", Toast.LENGTH_LONG)
							.show();
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							backConut = 0;
						}
					}, 4000);
				} else if (backConut == 2) {
					if (!loginSettingsp.getBoolean("AUTO_ISCHECK", false)) {
						userInfosp.edit().clear().commit();
					}
					AppManager.getInstance().AppExit(this);
					backConut = 0;
				}
			} else {
				this.finish();
				overridePendingTransition(R.anim.push_right_in,
						R.anim.push_left_out);
			}

			break;
		}
		return true;
	}
}
