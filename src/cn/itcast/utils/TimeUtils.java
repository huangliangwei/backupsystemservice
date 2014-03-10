package cn.itcast.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeUtils {
	@SuppressLint("SimpleDateFormat")
	public static String getTime() {
		SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calender = Calendar.getInstance();
		String yyMmDd = yymmdd.format(calender.getTime());
		return yyMmDd;

	}

	public static String getTimea() {
		SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Calendar calender = Calendar.getInstance();
		String yyMmDd = yymmdd.format(calender.getTime());
		return yyMmDd;

	}

	public static String getDate() {
		SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calender = Calendar.getInstance();
		String yyMmDd = yymmdd.format(calender.getTime());
		return yyMmDd;
	}

	public static String getCurrentDay() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sf.format(cal.getTime());
		str = str +" "+"23:59:59";
		return str;
	}

	public static String getYesteDay(int day) {
		Calendar cal = Calendar.getInstance();
		// 日期的DATE减去10 就是往后推10 天 同理 +10 就是往后推十天
		cal.add(Calendar.DATE, day);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

		String str = sf.format(cal.getTime());
		str = str +" "+"00:00:01";
		return str;
	};
	public static String getBeforeDay(int day) {
	    Calendar cal = Calendar.getInstance();
		cal.get(Calendar.DAY_OF_MONTH); 
		cal.get(Calendar.MONTH);
		cal.get(Calendar.YEAR);
		// 如果要前几天就使用负值
		cal.add(Calendar.DAY_OF_MONTH, day);
//		cal.add(Calendar.MONTH, 1);
		int YEAR=cal.get(Calendar.YEAR);
		int MONTH=cal.get(Calendar.MONTH);
		int DATE=cal.get(Calendar.DATE);
		SimpleDateFormat yymmdd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String yyMmDd = yymmdd.format(cal.getTime());
	    return yyMmDd;
	};
}
