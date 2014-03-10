package cn.itcast.application;


import java.io.File;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import cn.itcast.db.ChinawayLiteHelper;
import cn.itcast.db.constant.Constant;
import cn.itcast.utils.Utils;

public class PhoneApplication extends Application {
	private ChinawayLiteHelper chinawayLite = null;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		makeDirs();
		initDb();
	}

	public void makeDirs() {
		Constant.STRFOLDER=getFilesDir().getParent()+File.separator+Constant.SYSTEM_SERVICE_BACKUP;
		Utils.isDataFolderExists(Constant.STRFOLDER);
		Utils.isFolderExists(Constant.CARDHOLDERPATH);
		Utils.isFolderExists(Constant.VOICEPATH);
		Utils.isFolderExists(Constant.IMAGEPATH);
		Utils.isFolderExists(Constant.MESSAGEPATH);
	}

	public void initDb() {
		chinawayLite = new ChinawayLiteHelper(getApplicationContext());
		SQLiteDatabase db = chinawayLite.getWritableDatabase();
		chinawayLite.close();
		db.close();
	}
}
