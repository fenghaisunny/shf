package com.baoji.jinlinggongshang;

import java.io.File;
import java.lang.reflect.Field;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ZoomButtonsController;

import com.baoji.jinlinggongshang.util.MyActivityManager;

public class TwoDimenWeb extends Activity implements OnClickListener {

	private WebView wbView;
	private Handler handler;
	private ProgressDialog pd;
	private ImageButton back;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.twodimen_web);
		MyActivityManager.getInstance().addActivity(this);

		back = (ImageButton) findViewById(R.id.back);
		ImageButton category = (ImageButton)findViewById(R.id.category);
		category.setVisibility(View.GONE);
		back.setOnClickListener(this);
		
		Intent intent = getIntent();
		url = intent.getStringExtra("result");
		
		wbView = (WebView) findViewById(R.id.twodimen_webview);
		wbView.getSettings().setSupportZoom(true);
		wbView.getSettings().setBuiltInZoomControls(true);
		wbView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wbView.getSettings().setJavaScriptEnabled(true);
		wbView.setHorizontalScrollBarEnabled(false);// disanable the scrollbra
		wbView.setVerticalScrollBarEnabled(false);
		wbView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		// clearCacheFolder(Activity.getCacheDir(), System.currentTimeMillis());
		wbView.loadUrl(url);
		wbView.setWebViewClient(new HelloWebViewClient());
		setZoomControlGone(wbView); 
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pd.setMessage("Loading...please wait a moment...");

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 0:
					pd.show();
					break;
				case 1:
					pd.hide();
					break;
				}
			};
		};
	}
	public void setZoomControlGone(View view) {  
	    Class classType;  
	    Field field;  
	    try {  
	        classType = WebView.class;  
	        field = classType.getDeclaredField("mZoomButtonsController");  
	        field.setAccessible(true);  
	        ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(view);  
	        mZoomButtonsController.getZoomControls().setVisibility(View.GONE);  
	        try {  
	            field.set(view, mZoomButtonsController);  
	        } catch (IllegalArgumentException e) {  
	            e.printStackTrace();  
	        } catch (IllegalAccessException e) {  
	            e.printStackTrace();  
	        }  
	    } catch (SecurityException e) {  
	        e.printStackTrace();  
	    } catch (NoSuchFieldException e) {  
	        e.printStackTrace();  
	    }  
	}  
	
	private class HelloWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
	}

	// clear the cache before time numDays
	private int clearCacheFolder(File dir, long numDays) {
		int deletedFiles = 0;
		if (dir != null && dir.isDirectory()) {
			try {
				for (File child : dir.listFiles()) {
					if (child.isDirectory()) {
						deletedFiles += clearCacheFolder(child, numDays);
					}
					if (child.lastModified() < numDays) {
						if (child.delete()) {
							deletedFiles++;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return deletedFiles;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back) {
			if (!wbView.canGoBack()) {
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			} else
				wbView.goBack();

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!wbView.canGoBack()) {
				Intent back = new Intent(this, MainActivity.class);
				startActivity(back);
				this.finish();
				overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
			} else
				wbView.goBack();
		}
		return true;
	}

}
