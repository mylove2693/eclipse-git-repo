package com.cynovo.sirius.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class LocalDatabase {

	protected static SQLiteDatabase db;
	protected static LocalDatabase localDatabase = null;

	protected LocalDatabase() {

	}

	public static LocalDatabase getInstance(Context context) {

		if (localDatabase == null)
			localDatabase = new LocalDatabase(context, DBHelper.DATABASE_NAME,
					DBHelper.DATABASE_VERSION);

		return localDatabase;
	}

	private LocalDatabase(Context context, String dbname, int version) {
		try {
			db = new DBHelper(context, dbname, version).getWritableDatabase();
		} catch (SQLiteException e) {
			db = new DBHelper(context, dbname, version).getReadableDatabase();
		}
	}

	protected void finalize() {
		try {
			super.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		// other finalization code...
		if (db != null)
			db.close();
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return db;
	}

}
