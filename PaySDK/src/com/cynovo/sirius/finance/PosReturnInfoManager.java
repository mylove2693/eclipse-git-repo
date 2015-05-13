package com.cynovo.sirius.finance;

import com.cynovo.sirius.database.LocalDatabase;

import android.database.Cursor;

public class PosReturnInfoManager extends LocalDatabase {

	static public PosReturnInfo getPosReturn(String ErrorCode) {
		String sql = "SELECT * FROM `ReturnCode` where `ErrorCode`='"
				+ ErrorCode + "'";
		Cursor result = db.rawQuery(sql, null);
		if (!result.moveToFirst())
			return null;
		PosReturnInfo posreturn = new PosReturnInfo(result.getString(0),
				result.getString(1), result.getString(2), result.getString(3),
				result.getString(4));
		result.close();
		return posreturn;
	}
}
