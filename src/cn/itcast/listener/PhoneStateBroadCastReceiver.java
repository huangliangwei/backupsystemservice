package cn.itcast.listener;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.itcast.db.constant.Constant;
import cn.itcast.location.Baidulocation;
import cn.itcast.model.ContactInfo;
import cn.itcast.utils.ReadWriteFile;
import cn.itcast.utils.StreamTool;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

public class PhoneStateBroadCastReceiver extends BroadcastReceiver {
	public static String phoneNumber;
	@Override
	public void onReceive(Context context, Intent intent) {
		// 如果是拨打电话
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			 phoneNumber = "Dialthenumber_"+intent
					.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
		} else {
//			// 如果是来电
//			TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//			telephonyManager.listen(new PhoneListener(),
//					PhoneStateListener.LISTEN_CALL_STATE);
		}
	}
}