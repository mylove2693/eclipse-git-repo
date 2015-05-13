package com.cynovo.sirius.database;

import com.cynovo.sirius.util.MyLog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * SQLite数据库生成函数
 * 
 */

public class DBHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "PaySDK.db"; // SQLite本地数据库名称
	public static final int DATABASE_VERSION = 1; // SQLite本地数据库版本号

	private static final String TABLE_RECORD = "CREATE TABLE IF NOT EXISTS `record` ("
			+ "`recordID` INTEGER PRIMARY KEY ,`tradeType` INTEGER, `amount` VARCHAR, "
			+ "`traceNo` VARCHAR, `originalTraceNo` VARCHAR, `requestDate` VARCHAR, "
			+ "`requestTime` VARCHAR, `ackDate` VARCHAR, `ackTime` VARCHAR, `ackNo` VARCHAR, "
			+ "`ackNoPrompt` VARCHAR, `operatorNo` VARCHAR, `cardNo` VARCHAR, `batchid` VARCHAR,"
			+ "`referenceNo` VARCHAR, `remark` VARCHAR, `tip` VARCHAR, `terminalCode` VARCHAR, "
			+ "`merchantCode` VARCHAR, `authorizationNo` VARCHAR, `clearingDate` VARCHAR, "
			+ "`issuerbankid` VARCHAR, `acqbank` VARCHAR, `reversalNo` VARCHAR)";

	public DBHelper(Context context, String name, int version) {
		super(context, name, null, version);
	}

	// 当本地没有此数据库时回调
	@Override
	public void onCreate(SQLiteDatabase db) {
		MyLog.d("SQLiteOpenHelper",
				"onCreate---------------------------------------------");

		db.beginTransaction();
		try {
			db.execSQL(TABLE_RECORD);
			for (int i = 0; i < BankErrorCode.tableList.length; i++) {
				db.execSQL(BankErrorCode.tableList[i]);
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}

	// 当本地数据库版本号不一样时回调
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		MyLog.d("SQLiteOpenHelper",
				"onUpgrade---------------------------------------------");
	}
}
