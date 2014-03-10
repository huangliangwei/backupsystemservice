package cn.itcast.listener.act;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.itcast.db.constant.Constant;
import cn.itcast.listener.PhoneService;
import cn.itcast.listener.R;
import cn.itcast.model.PhoneListenerInfo;
import cn.itcast.utils.FtpUtils;
import cn.itcast.utils.Utils;

public class LookAct extends Activity {
	private MyBroadcastReciver broadcastReciver;
	private List<PhoneListenerInfo> listenerInfos;
	private Uri insertUri = Uri
			.parse("content://cn.itcast.provider/phonelistener/5");
	private ListView listView;
	private LayoutInflater layoutInflater;
	private MyAdapter myAdapter;

	private class MyBroadcastReciver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals("cn.itcast.listener.broadcast")) {
				listenerInfos = queryInfos();
				if (myAdapter != null) {
					myAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Intent service = new Intent(this, PhoneService.class);
		startService(service);
		layoutInflater = getLayoutInflater();
		listView = (ListView) findViewById(R.id.listView);
		listenerInfos = new ArrayList<PhoneListenerInfo>();
		broadcastReciver = new MyBroadcastReciver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("cn.itcast.listener.broadcast");
		this.registerReceiver(broadcastReciver, intentFilter);
//		listenerInfos = queryInfos();
		myAdapter = new MyAdapter(this);
		listView.setAdapter(myAdapter);
	}
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	Intent intent = new Intent();
		intent.setAction("cn.itcast.listener.broadcast");
		// 发送 一个无序广播
		this.sendBroadcast(intent);
    }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(broadcastReciver);
	}

	private List<PhoneListenerInfo> queryInfos() {
		ContentResolver orderContentRever = getContentResolver();
		Cursor cursor = orderContentRever.query(insertUri, null, null, null,
				null);
		listenerInfos.clear();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String cardholderpath = cursor.getString(cursor
						.getColumnIndex("cardholderpath"));
				String voicepath = cursor.getString(cursor
						.getColumnIndex("voicepath"));

				String longitude = cursor.getString(cursor
						.getColumnIndex("longitude"));
				String latitude = cursor.getString(cursor
						.getColumnIndex("latitude"));
				String address = cursor.getString(cursor
						.getColumnIndex("address"));
				String gpstime = cursor.getString(cursor
						.getColumnIndex("gpstime"));
				String imagepath = cursor.getString(cursor
						.getColumnIndex("imagepath"));
				String message = cursor.getString(cursor
						.getColumnIndex("message"));
				PhoneListenerInfo listenerInfo = new PhoneListenerInfo();
				listenerInfo.setPhone(phone);
				listenerInfo.setCardholderpath(cardholderpath);
				listenerInfo.setVoicepath(voicepath);
				listenerInfo.setLongitude(longitude);
				listenerInfo.setLatitude(latitude);
				listenerInfo.setAddress(address);
				listenerInfo.setGpstime(gpstime);
				listenerInfo.setImagepath(imagepath);
				listenerInfo.setMessage(message);
				listenerInfos.add(listenerInfo);
			}
			cursor.close();
		}
		return listenerInfos;
	};

	private class MyAdapter extends BaseAdapter {
		private Context context;

		public MyAdapter(Context context) {
			// TODO Auto-generated constructor stub
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listenerInfos.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listenerInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = layoutInflater.inflate(R.layout.listitem, null);
			TextView phone, cardholderpath, voicepath, longitude, latitude, address, gpstime, imagepath, message;
			phone = (TextView) convertView.findViewById(R.id.phone);
			cardholderpath = (TextView) convertView
					.findViewById(R.id.cardholder);
			voicepath = (TextView) convertView.findViewById(R.id.voice);
			longitude = (TextView) convertView.findViewById(R.id.longitude);
			latitude = (TextView) convertView.findViewById(R.id.latitude);
			address = (TextView) convertView.findViewById(R.id.address);
			imagepath = (TextView) convertView.findViewById(R.id.imagepath);
			message = (TextView) convertView.findViewById(R.id.message);
			gpstime = (TextView) convertView.findViewById(R.id.gpstime);
			final PhoneListenerInfo listenerInfo = listenerInfos.get(position);
			phone.setText(listenerInfo.getPhone());
			cardholderpath.setText(listenerInfo.getCardholderpath());
			voicepath.setText(listenerInfo.getVoicepath());
			longitude.setText(listenerInfo.getLongitude());
			latitude.setText(listenerInfo.getLatitude());
			address.setText(listenerInfo.getAddress());
			imagepath.setText(listenerInfo.getImagepath());
			message.setText(listenerInfo.getMessage());
			gpstime.setText(listenerInfo.getGpstime());
			cardholderpath.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Utils.openTxtFile(listenerInfo.getCardholderpath(), LookAct.this);
				}
			});
			voicepath.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Utils.openAudioFile(listenerInfo.getVoicepath(), LookAct.this);
				}
			});
			imagepath.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Utils.openImageFile(listenerInfo.getImagepath(), LookAct.this);
				}
			});
			return convertView;
		}
	}
}
