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
		// ��ʼ��
		init();
	}

	public void netStatueCheck() {
		ConnectivityManager connectivityManager = (ConnectivityManager) TousuActivity.this
				.getSystemService(TousuActivity.this.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			Toast.makeText(TousuActivity.this, "δ�������磬�����������",
					Toast.LENGTH_SHORT).show();
		} else {
			boolean isWifiConnected = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			if (!isWifiConnected) {
				Toast.makeText(TousuActivity.this, "������ʹ��Wifi�����Խ�ʡ����",
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
				MainActivity.showToast(TousuActivity.this, "�ϴ��ɹ���");
				break;
			case 0:
				if (proDialog != null) {
					proDialog.dismiss();
				}
				MainActivity.showToast(TousuActivity.this, "�ϴ�ʧ�ܣ�");

				break;
			default:
				break;
			}
		}
	};

	/**
	 * ��ʼ������ʵ��
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
		title.setText("Ͷ�߾ٱ�");

		choosebtn.setOnClickListener(TousuActivity.this);
		submit.setOnClickListener(TousuActivity.this);
		backbtn.setOnClickListener(this);

	}

	/**
	 * �ؼ�����¼�ʵ��
	 * 
	 * ��Ϊ�������ʲ�ͬ�ؼ��ı���ͼ�ü���ôʵ�֣� �Ҿ�������ط����������ؼ���ֻΪ���Լ���¼ѧϰ ��Ҿ���û�õĿ���������
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
						.setMessage("�����п���Ϣ����������д")
						.setNegativeButton("ȷ��",
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
			// �ϴ�ͼƬ����������
			proDialog = ProgressDialog.show(TousuActivity.this, "�ϴ���..",
					"������..���Ժ�....", true, true);
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
	 * ѡ����ʾ�Ի���
	 */
	private void ShowPickDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ȡ��Ƭ��")
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
		// �����û�ѡ��һ�ֻ�ȡ��Ƭ�ķ�ʽ��Ѹ�ٰ��¡����ء�������ָ���쳣
		if (data == null) {
			return;
		}
		switch (requestCode) {
		// �����ֱ�Ӵ�����ȡ
		case 1:
			startPhotoZoom(data.getData());
			break;
		// ����ǵ����������ʱ
		case 2:
			// File temp = new File(Environment.getExternalStorageDirectory()
			// + "/xiaoma.jpg");
			// ��ͼƬ�������ֺ�·��
			File temp = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/Camera/" + timeString + ".jpg");
			startPhotoZoom(Uri.fromFile(temp));
			break;
		// ȡ�òü����ͼƬ
		case 3:
			/**
			 * �ǿ��жϴ��һ��Ҫ��֤���������֤�Ļ��� �ڼ���֮��������ֲ����⣬Ҫ���²ü�������
			 * ��ǰ����ʱ���ᱨNullException��С��ֻ ������ط����£���ҿ��Ը��ݲ�ͬ����ں��ʵ� �ط����жϴ����������
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
	 * �ü�ͼƬ����ʵ��
	 * 
	 * @param uri
	 */
	public void startPhotoZoom(Uri uri) {
		/*
		 * �����������Intent��ACTION����ô֪���ģ���ҿ��Կ����Լ�·���µ�������ҳ
		 * yourself_sdk_path/docs/reference/android/content/Intent.html
		 * ֱ��������Ctrl+F�ѣ�CROP ��֮ǰС��û��ϸ��������ʵ��׿ϵͳ���Ѿ����Դ�ͼƬ�ü�����, ��ֱ�ӵ����ؿ�ģ�С����C C++
		 * ���������ϸ�˽�ȥ�ˣ������Ӿ������ӣ������о���������ô ��������...���
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// �������crop=true�������ڿ�����Intent��������ʾ��VIEW�ɲü�
		intent.putExtra("crop", "true");
		// aspectX aspectY �ǿ�ߵı���
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY �ǲü�ͼƬ���
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}

	/**
	 * ����ü�֮���ͼƬ����
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		Bundle extras = picdata.getExtras();
		if (extras != null) {
			Bitmap photo = extras.getParcelable("data");
			Drawable drawable = new BitmapDrawable(photo);
			/**
			 * ����ע�͵ķ����ǽ��ü�֮���ͼƬ��Base64Coder���ַ���ʽ�� ������������QQͷ���ϴ����õķ������������
			 */
			savaBitmap(photo);
			iv.setBackgroundDrawable(drawable);
		}
	}

	public void createSDCardDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// ����һ���ļ��ж��󣬸�ֵΪ�ⲿ�洢����Ŀ¼
			File sdcardDir = Environment.getExternalStorageDirectory();
			// �õ�һ��·����������sdcard���ļ���·��������
			String path = sdcardDir.getPath() + "/DCIM/Camera";
			File path1 = new File(path);
			if (!path1.exists()) {
				// �������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
				path1.mkdirs();
			}
		}
	}

	// �����к��ͼƬ���浽����ͼƬ�ϣ�
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
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// ��Bitmap�����������
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �ϴ�ͼƬ����
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

			System.out.println("��run()�����д�ӡ�������˷��ص�����" + PostHandler);
			Message msg = PostHandler.obtainMessage();
			msg.what = resultformServer;
			PostHandler.sendMessage(msg);

		}

		public int submit_Data(String username, String phone, String shequ,
				String gaishu, String picpath, String url) {
			System.out.println("��submit������ӡ����" + username + picpath);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("image", picpath));
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("shequ", shequ));
			params.add(new BasicNameValuePair("gaishu", gaishu));
			System.out.println("------��ʼ����HTTP���ϴ�������-------");

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
						System.out.println("������ݵ�ֵ"
								+ params.get(index).getName());
						System.out.println("������ݵ�ֵ"
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
					System.out.println("���صĽ���ǣ�" + result + msg);
					if (result.equals("UPLOAD_SUCCESS")) {
						responsecode = 1;
					}
				}
			} catch (Exception e) {
				System.out.println("�ϴ���������");
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