package com.cynovo.paysetting;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

public class PaySettingProvider extends ContentProvider {
	
	public static final String TAG = "PaySettingProvider";
	
	private SharedPreferences PaySettingPreference;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		PaySettingPreference = this.getContext().getSharedPreferences(PaySettingActivity.PREFERENCENAME, Context.MODE_PRIVATE);
		
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		String[] table = new String[]{"key","value"};
		
		MatrixCursor c = new MatrixCursor(table);
		
		c.addRow(new Object[]{"yinlian",PaySettingPreference.getString("yinlian", "false")});
		c.addRow(new Object[]{"yinlian",PaySettingPreference.getString("yinlian", "false")});
		c.addRow(new Object[]{"yinlian",PaySettingPreference.getString("yinlian", "false")});
		c.addRow(new Object[]{"yinlian",PaySettingPreference.getString("yinlian", "false")});
		
		return c;
		
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
