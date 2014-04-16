package com.huaqin.punan.data;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String DATABASE_NAME = "test.db";
	private static final int DATABASE_VERSION = 1;
	
	public DBHelper(Context context) {
		
		//CursorFactory use Default
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	//Create the Database
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXITS person" + 
					"_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, " +
					"age INTEGER, info TEXT");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("ALTER TABLE person ADD COLUMN other STRING");  
	}

}
