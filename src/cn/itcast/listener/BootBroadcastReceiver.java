package cn.itcast.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, PhoneService.class);
		context.startService(service);
		Log.i("BootBroadcastReceiver", "service started");
	}

}
