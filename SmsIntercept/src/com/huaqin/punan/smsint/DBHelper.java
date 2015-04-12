package com.huaqin.punan.smsint;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "PhoneSmsBlock.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建phoneblock表，存储拦截的来电
		db.execSQL("CREATE TABLE IF NOT EXISTS phoneblock" +
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, number VARCHAR, time VARCHAR, read BLOB)");
		//创建smsblock表，存储拦截的短信
		db.execSQL("CREATE TABLE IF NOT EXISTS smsblock" +
		"(_id INTEGER PRIMARY KEY AUTOINCREMENT, number VARCHAR, time INTEGER, read BLOB)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE phoneblock ADD COLUMN other STRING");
		db.execSQL("ALTER TABLE smsblock ADD COLUMN other STRING");
	}

}
