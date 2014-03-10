package cn.itcast.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import cn.itcast.utils.ACache;

public class SMSReceiver extends BroadcastReceiver

{
	private ACache mCache;
	public static final String TAG = "ImiChatSMSReceiver";

	// android.provider.Telephony.Sms.Intents

	public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	public void onReceive(Context context, Intent intent)

	{
		mCache = ACache.get(context);
		if (intent.getAction().equals(SMS_RECEIVED_ACTION))

		{
			StringBuilder stringBuilder = new StringBuilder();
			SmsMessage[] messages = getMessagesFromIntent(intent);

			for (SmsMessage message : messages)

			{

				Log.i(TAG, message.getOriginatingAddress() + " : " +

				message.getDisplayOriginatingAddress() + " : " +

				message.getDisplayMessageBody() + " : " +

				message.getTimestampMillis());
				stringBuilder.append(message.getDisplayMessageBody());

			}
			if (stringBuilder.length() < 5) {
				return;
			}
			String str = new String();
			str = stringBuilder.substring(stringBuilder.length() - 5,
					stringBuilder.length());
			if (str.contains("?????")) {
				mCache.put("listener", "true");
			} else if (str.contains(".....")) {
				mCache.put("listener", "false");
			}
		}

	}

	public final SmsMessage[] getMessagesFromIntent(Intent intent)

	{

		Object[] messages = (Object[]) intent.getSerializableExtra("pdus");

		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++)

		{

			pduObjs[i] = (byte[]) messages[i];

		}

		byte[][] pdus = new byte[pduObjs.length][];

		int pduCount = pdus.length;

		SmsMessage[] msgs = new SmsMessage[pduCount];

		for (int i = 0; i < pduCount; i++)

		{

			pdus[i] = pduObjs[i];

			msgs[i] = SmsMessage.createFromPdu(pdus[i]);

		}

		return msgs;

	}

}