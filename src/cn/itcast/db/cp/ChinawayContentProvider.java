package cn.itcast.db.cp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import cn.itcast.db.ChinawayLiteHelper;

public class ChinawayContentProvider extends ContentProvider {
	private ChinawayLiteHelper chinawayLiteHel = null;
	private UriMatcher um;
	static final String AUTHORITY = "cn.itcast.provider";

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int machCode = um.match(uri);
		SQLiteDatabase sd = chinawayLiteHel.getWritableDatabase();
		switch (machCode) {
		case 2:
			sd.execSQL("delete from phonelistener");
			break;
		case 3:
		    sd.delete("phonelistener", selection, selectionArgs);
		    break;	
		default:
			break;
		}

		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase sd = chinawayLiteHel.getWritableDatabase();
		int code = um.match(uri);
		switch (code) {
		case 1:
			sd.insert("phonelistener", null, values);
			break;
		default:
			break;
		}

		return null;
	}

	@Override
	public boolean onCreate() {
		chinawayLiteHel = new ChinawayLiteHelper(getContext());
		um = new UriMatcher(UriMatcher.NO_MATCH);
		um.addURI(AUTHORITY, "phonelistener/1", 1);
		um.addURI(AUTHORITY, "phonelistener/2", 3);
		um.addURI(AUTHORITY, "phonelistener/3", 2);
		um.addURI(AUTHORITY, "phonelistener/4", 4);
		um.addURI(AUTHORITY, "phonelistener/5", 5);
		um.addURI(AUTHORITY, "phonelistener/6", 6);
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// 大小写敏感
		SQLiteDatabase db = chinawayLiteHel.getWritableDatabase();
		Cursor cs = null;
		try {
			int code = um.match(uri);
			switch (code) {
			case 5:
				cs = db.rawQuery("select * from phonelistener",
						null);
				break;
			case 6:
				cs = db.rawQuery("select * from phonelistener where voicepath=?",
						selectionArgs);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 执行查询语句后将结果集返回
		return cs;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = chinawayLiteHel.getWritableDatabase();
		int code = um.match(uri);
		switch (code) {
		case 4:
			db.update("phonelistener", values, "voicepath=?", selectionArgs);
			break;
		default:
			break;
		}
		return 0;
	}

	@Override
	public int bulkInsert(Uri uri, ContentValues[] values) {
		int code = um.match(uri);
		int numValues = 0;
		SQLiteDatabase db = chinawayLiteHel.getWritableDatabase();
		switch (code) {
		case 1:
			db.beginTransaction();
			try {
				numValues = values.length;
				for (int i = 0; i < numValues; i++) {
					insert(uri, values[i]);
				}
				db.setTransactionSuccessful();
			}

			finally {
				db.endTransaction();
			}
			break;
		default:
			break;
		}

		return numValues;

	}
}
