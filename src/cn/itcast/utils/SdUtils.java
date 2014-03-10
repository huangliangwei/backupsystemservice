package cn.itcast.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.os.Environment;

public class SdUtils {
	/**
	 * 判断sd卡是否存在
	 * 
	 * @param filename
	 * @param stringlog
	 */
	public static boolean ExistSDCard() {  
		  if (android.os.Environment.getExternalStorageState().equals(  
		    android.os.Environment.MEDIA_MOUNTED)) {  
		   return true;  
		  } else  
		   return false;  
		 }  
	
}
