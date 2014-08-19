package com.baoji.jinlinggongshang.pulltorefresh;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoji.jinlinggongshang.MainActivity;
import com.baoji.jinlinggongshang.R;
import com.baoji.jinlinggongshang.util.CharSetUtil;

public class TousuActivity extends Activity implements OnClickListener {
	private String filename;
	private String cutnameString;
	private ImageView iv = null;
	private Button choosebtn = null, backbtn = null,refresh;
	private Button submit;
	private int responsecode = 0;
	private ProgressDialog proDialog;
	private String timeString;
	private EditText username_EditText;
	private EditText phone_EditText, suoshushequ_EditText,
			tousugaishu_EditText;
	private String phoneString;
	private String usernameString, shequString, gaishuString;
	private TextView title;

	Thread PostThread = null;

	String upLoadServerUri = "http://124.116.242.162:8080/Upload/UploadServlet";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tousu);
		// 初始化
		init();
	}

	public void netStatueCheck() {
		ConnectivityManager connectivityManager = (ConnectivityManager) TousuActivity.this
				.getSystemService(TousuActivity.this.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			Toast.makeText(TousuActivity.this, "未连接网络，请打开网络连接",
					Toast.LENGTH_SHORT).show();
		} else {
			boolean isWifiConnected = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			if (!isWifiConnected) {
				Toast.makeText(TousuActivity.this, "建议您使用Wifi连接以节省流量",
						Toast.LENGTH_SHORT).show();

			}
		}

	}

	Handler PostHandler = new Handler() {
		// @SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				MainActivity.showToast(TousuActivity.this, "上传成功！");
				break;
			case 0:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				MainActivity.showToast(TousuActivity.this, "上传失败！");

				break;
			default:
				break;
			}
		}
	};

	/**
	 * 初始化方法实现
	 */
	private void init() {
		// timeString=new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()).toString();
		// timeString="yebar";

		iv = (ImageView) findViewById(R.id.imageView1);
		choosebtn = (Button) findViewById(R.id.button1);
		refresh = (Button)findViewById(R.id.refresh);
		refresh.setVisibility(View.GONE);
		username_EditText = (EditText) findViewById(R.id.jubaoname_edittext);
		phone_EditText = (EditText) findViewById(R.id.phonenum_edittext);
		suoshushequ_EditText = (EditText) findViewById(R.id.suoshushequ_edittext);
		tousugaishu_EditText = (EditText) findViewById(R.id.tousugaishu_edittext);
		submit = (Button) findViewById(R.id.submit);
		backbtn = (Button) findViewById(R.id.back);
		title = (TextView) findViewById(R.id.titleBar_tv);
		title.setVisibility(View.VISIBLE);
		title.setText("投诉举报");

		choosebtn.setOnClickListener(TousuActivity.this);
		submit.setOnClickListener(TousuActivity.this);
		backbtn.setOnClickListener(this);

	}

	/**
	 * 控件点击事件实现
	 * 
	 * 因为有朋友问不同控件的背景图裁剪怎么实现， 我就在这个地方用了三个控件，只为了自己记录学习 大家觉得没用的可以跳过啦
	 */
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.back:
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
			break;
		case R.id.button1:
			ShowPickDialog();
			break;
		case R.id.submit:
			netStatueCheck();
			usernameString = CharSetUtil.changeToUnicode(username_EditText.getText()
					.toString().concat(" "));
			phoneString = CharSetUtil.changeToUnicode(phone_EditText.getText().toString()
					.concat(" "));
			shequString = CharSetUtil.changeToUnicode(suoshushequ_EditText.getText()
					.toString().concat(" "));
			gaishuString =CharSetUtil.changeToUnicode(tousugaishu_EditText.getText()
					.toString().concat(" "));
			if (username_EditText.getText().toString().equals("")
					|| phone_EditText.toString().equals("")
					|| suoshushequ_EditText.toString().equals("")
					|| tousugaishu_EditText.toString().equals("")) {
				new AlertDialog.Builder(TousuActivity.this)
						.setMessage("输入有空信息，请重新填写")
						.setNegativeButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										return;
									}
								}).create().show();
				return;
			}
			// 上传图片和数据资料
			proDialog = ProgressDialog.show(TousuActivity.this, "上传中..",
					"连接中..请稍后....", true, true);
			PostThread = new Thread(new PostImageThread(usernameString,
					phoneString, shequString, gaishuString, filename,
					upLoadServerUri));
			PostThread.start();
			break;
		default:
			break;
		}
	}

	/**
	 * 选择提示对话框
	 */
	private void ShowPickDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("获取照片…")
				.setItems(
						getResources().getStringArray(R.array.uploadimage_menu),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									Date date = new Date(System
											.currentTimeMillis());
									SimpleDateFormat dateFormat = new SimpleDateFormat(
											"'IMG'_yyyyMMddHHmmss");
									timeString = dateFormat.format(date);
									createSDCardDir();
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(
											MediaStore.EXTRA_OUTPUT,
											Uri.fromFile(new File(
													Environment
															.getExternalStorageDirectory()
															+ "/DCIM/Camera",
													timeString + ".jpg")));
									startActivityForResult(intent, 2);
									overridePendingTransition(
											R.anim.rotate_fade_in,
											R.anim.rotate_fade_out);
									break;
								case 1:
									Intent cameraintent = new Intent(
											Intent.ACTION_PICK, null);
									cameraintent
											.setDataAndType(
													MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
													"image/*");
									startActivityForResult(cameraintent, 1);
									break;
								case 2:
									break;
								}
							}
						}).show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 避免用户选择一种获取照片的方式后迅速按下“返回”，报空指针异常
		if (data == null) {
			return;
		}
		switch (requestCode) {
		// 如果是直接从相册获取
		case 1:
			startPhotoZoom(data.getData());
			break;
		// 如果是调用相机拍照时
		case 2:
			// File temp = new File(Environment.getExternalStorageDirectory()
			// + "/xiaoma.jpg");
			// 给图片设置名字和路径
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// 取得裁剪后的图片
		case 3:
			/**
			 * 非空判断大家一定要验证，如果不验证的话， 在剪裁之后如果发现不满意，要重新裁剪，丢弃
			 * 当前功能时，会报NullException，小马只 在这个地方加下，大家可以根据不同情况在合适的 地方做判断处理类似情况
			 * 
			 */
			if (data != null) {
				setPicToView(data);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 裁剪图片方法实现
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * 至于下面这个Intent的ACTION是怎么知道的，大家可以看下自己路径下的如下网页
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * 直接在里面Ctrl+F搜：CROP ，之前小马没仔细看过，其实安卓系统早已经有自带图片裁剪功能, 是直接调本地库的，小马不懂C C++
		 * 这个不做详细了解去了，有轮子就用轮子，不再研究轮子是怎么 制做的了...吼吼
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			/**
			 * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上 传到服务器，QQ头像上传采用的方法跟这个类似
			 */
			savaBitmap(photo);
			iv.setBackgroundDrawable(drawable);
		}
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// 创建一个文件夹对象，赋值为外部存储器的目录
			File sdcardDir = Environment.getExternalStorageDirectory();
			// 得到一个路径，内容是sdcard的文件夹路径和名字
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// 若不存在，创建目录，可以在应用启动的时候创建
				path1.mkdirs();
			}
		}
	}

	// 将剪切后的图片保存到本地图片上！
	@SuppressLint("SimpleDateFormat")
	public void savaBitmap(Bitmap bitmap) {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMddHHmmss");
		cutnameString = dateFormat.format(date);
		filename = Environment.getExternalStorageDirectory().getPath() + "/"
				+ cutnameString + ".jpg";
		File f = new File(filename);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 上传图片方法
	class PostImageThread implements Runnable {
		private String username;
		private String picpath;
		private String url;
		private String phone;
		private String shequ;
		private String gaishu;

		public PostImageThread(String username, String phone, String shequ,
				String gaishu, String picpath, String url) {
			this.url = url;
			this.username = username;
			this.picpath = picpath;
			this.phone = phone;
			this.shequ = shequ;
			this.gaishu = gaishu;
		}

		@Override
		public void run() {

			int resultformServer = submit_Data(username, phone, shequ, gaishu,
					picpath, url);

			System.out.println("在run()方法中打印服务器端返回的数据" + PostHandler);
			Message msg = PostHandler.obtainMessage();
			msg.what = resultformServer;
			PostHandler.sendMessage(msg);

		}

		public int submit_Data(String username, String phone, String shequ,
				String gaishu, String picpath, String url) {
			System.out.println("在submit方法打印参数" + username + picpath);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("image", picpath));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("shequ", shequ));
			params.add(new BasicNameValuePair("gaishu", gaishu));
			System.out.println("------开始调用HTTP，上传数据啦-------");

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();

			HttpParams httpParams = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			HttpPost httpPost = new HttpPost(url);
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				// MultipartEntity entity = new
				// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
				MultipartEntity entity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				for (int index = 0; index < params.size(); index++) {
					if (params.get(index).getName().equalsIgnoreCase("image")) {
						System.out.println("post - if");
						// If the key equals to "image", we use FileBody to
						// transfer the data
						entity.addPart(params.get(index).getName(),
								new FileBody(new File(params.get(index)
										.getValue())));

					} else {
						System.out.println("post - else");
						// Normal string data
						System.out.println("输出数据的值"
								+ params.get(index).getName());
						System.out.println("输出数据的值"
								+ params.get(index).getValue());

						entity.addPart(params.get(index).getName(),
								new StringBody(params.get(index).getValue(),
										Charset.forName("UTF-8")));
					}
				}
				System.out.println("post - done" + entity);
				httpPost.setEntity(entity);
				HttpResponse response = httpClient.execute(httpPost,
						localContext);
				responsecode = response.getStatusLine().getStatusCode();
				System.out.println(responsecode);
				if (responsecode == 200) {
					// HttpEntity resEntity = response.getEntity();
					HttpEntity resEntity = response.getEntity();
					String info = EntityUtils.toString(resEntity);

					System.out.println(info);

					JSONArray array = new JSONArray(info);
					JSONObject jsonobject = array.getJSONObject(0);
					// Get the result variables from response
					final String result = (jsonobject.getString("result"));
					final String msg = (jsonobject.getString("msg"));
					System.out.println("返回的结果是：" + result + msg);
					if (result.equals("UPLOAD_SUCCESS")) {
						responsecode = 1;
					}
				}
			} catch (Exception e) {
				System.out.println("上传出错啦！");
			}
			return responsecode;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			overridePendingTransition(R.anim.push_right_in,
					R.anim.push_left_out);
		}
		return true;
	}

}