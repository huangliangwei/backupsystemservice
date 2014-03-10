package cn.itcast.db;

import cn.itcast.db.constant.Constant;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChinawayLiteHelper extends SQLiteOpenHelper {

	public ChinawayLiteHelper(Context context) {
		super(context,Constant.DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE if not exists phonelistener (_id INTEGER PRIMARY KEY AUTOINCREMENT,phone text,cardholderpath text,voicepath text,longitude text,latitude text,address text,gpstime text,imagepath  text,message text)");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS phonelistener");
		onCreate(db);
	}
}
