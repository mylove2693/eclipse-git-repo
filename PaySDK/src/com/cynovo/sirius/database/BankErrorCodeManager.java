package com.cynovo.sirius.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cynovo.sirius.finance.PosReturnInfo;

public class BankErrorCodeManager {
	public static PosReturnInfo getDesc(Context context, String ErrorCode) {
		SQLiteDatabase msqlitedb = LocalDatabase.getInstance(context)
				.getSQLiteDatabase();
		String sql = "SELECT * FROM `ReturnCode` where `ErrorCode`='"
				+ ErrorCode + "'";
		Cursor result = msqlitedb.rawQuery(sql, null);
		if (!result.moveToFirst())
			return null;
		PosReturnInfo posreturn = new PosReturnInfo(result.getString(0),
				result.getString(1), result.getString(2), result.getString(3),
				result.getString(4));
		result.close();
		return posreturn;
	}
}
