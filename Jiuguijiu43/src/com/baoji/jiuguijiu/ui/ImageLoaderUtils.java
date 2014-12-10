package com.baoji.jiuguijiu.ui;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;

import com.baoji.jiuguijiu.BuildConfig;
import com.baoji.jiuguijiu.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

/**2014-2-14
 * @author sunhaifeng
 * 封装与动态加载网络图片的方法，实现ImageLoader的初始化，网络环境的监控、网络图片Bitmap的获取，以及与Imageswitcher、ImageView的数据适配的功能。
 */
public class ImageLoaderUtils {
	public static DisplayImageOptions options;
	public static ImageLoader imageLoader;
	
	
	/**2014-2-14
	 * @param activity
	 * ImageLoader的初始化
	 */
	public static void imageLoderIni(Context c) {

		ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
				c).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO);
		if (BuildConfig.DEBUG) {
			builder.writeDebugLogs();
		}

		ImageLoader.getInstance().init(builder.build());

		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.empty_photo)
				.showImageForEmptyUri(R.drawable.empty_photo)
				.showImageOnFail(R.drawable.empty_photo).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.ARGB_8888)
				.build();
		imageLoader = ImageLoader.getInstance();
		// 检查网络
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(c.getApplicationContext().CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (null == networkInfo) {
			Toast.makeText(c, "未连接网络，请打开网络连接", Toast.LENGTH_SHORT).show();
		} else {
			boolean isWifiConnected = connectivityManager.getNetworkInfo(
					ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ? true
					: false;
			if (!isWifiConnected) {
				Toast.makeText(c, "建议您使用Wifi连接以节省流量", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}
	
	/**2014-2-14
	 * @param url
	 * @param imageSwitcher
	 * ImageSwitcher的数据适配
	 */
	public static void loadImageswicher(String url,final ImageSwitcher imageSwitcher) {
		imageLoader.loadImage(url, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {

				BitmapDrawable bd = new BitmapDrawable(loadedImage);
				imageSwitcher.setImageDrawable(bd);
			}
		});

	}
	/**2014-2-14
	 * @param url
	 * @param image
	 * ImageView的数据适配
	 */	
	public static void loadImageView(String url,final ImageView image) {
		imageLoader.loadImage(url, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				image.setImageBitmap(loadedImage);
			}
		});
	}
	
	public static void setImageViewWallPaper(String url,final Context c) {
		imageLoader.loadImage(url, options, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				try {
					c.setWallpaper(loadedImage);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
}
