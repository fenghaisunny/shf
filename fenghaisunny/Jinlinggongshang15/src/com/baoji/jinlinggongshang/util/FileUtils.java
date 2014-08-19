package com.baoji.jinlinggongshang.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class FileUtils {
	private static String TAG = "File";

	public static String getSDCardRootPath() {
		// SD����Ŀ¼
		String sDCardRoot = Environment.getExternalStorageDirectory()
				.getAbsolutePath();

		return sDCardRoot;
	}

	public static void saveToSDCard(Bitmap bitmap, String filePath,
			String fileName) {

		// �������ļ�·�����ļ�����SD��·����������
		String sdcardRoot = getSDCardRootPath();
		// �����ļ�·��
		File dir = new File(sdcardRoot + File.separator + filePath);
		Log.i(TAG, "dir: " + dir);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File targetFile = new File(dir, fileName);

		try {
			targetFile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
