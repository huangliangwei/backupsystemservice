package cn.itcast.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Utils {
	// 创建多级目录
	public static boolean isFolderExists(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	// 创建data目录
	public static boolean isDataFolderExists(String strFolder) {

		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdirs()) {
				String str = "chmod " + strFolder + " " + "777"
						+ " && busybox chmod " + strFolder + " " + "777";
				try {
					Runtime.getRuntime().exec(str);
				} catch (IOException e) {
					System.out.println("有错啦");
				}
				return true;
			} else {
				String str = "chmod " + strFolder + " " + "777"
						+ " && busybox chmod " + strFolder + " " + "777";
				try {
					Runtime.getRuntime().exec(str);
				} catch (IOException e) {
					System.out.println("有错啦");
				}
				return false;
			}
		}

		return true;
	}

	// 创建一级目录
	public static boolean isOneFolderExists(String strFolder) {
		File file = new File(strFolder);
		if (!file.exists()) {
			if (file.mkdir()) {
				return true;
			} else
				return false;
		}
		String ss = File.separator;
		return true;
	}

	public static void openImageFile(String path, Context context) {
		if (!SdUtils.ExistSDCard()) {
			Toast.makeText(context, "您未插入Sd卡!", Toast.LENGTH_SHORT).show();
			return;
		}
		File baseFile = new File(path);
		Uri uri = Uri.fromFile(baseFile);
		Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
		viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		viewIntent.setDataAndType(uri, "image/*");
		context.startActivity(viewIntent);
	}

	public static void openTxtFile(String path, Context context) {
		if (!SdUtils.ExistSDCard()) {
			Toast.makeText(context, "您未插入Sd卡!", Toast.LENGTH_SHORT).show();
			return;
		}
		File baseFile = new File(path);
		Uri uri = Uri.fromFile(baseFile);
		Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
		viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		viewIntent.setDataAndType(uri, "text/plain");
		context.startActivity(viewIntent);
	}

	public static void openAudioFile(String path, Context context) {
		if (!SdUtils.ExistSDCard()) {
			Toast.makeText(context, "您未插入Sd卡!", Toast.LENGTH_SHORT).show();
			return;
		}
		File baseFile = new File(path);
		Uri uri = Uri.fromFile(baseFile);
		Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
		viewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		viewIntent.setDataAndType(uri, "audio/*");
		context.startActivity(viewIntent);
	}
}
