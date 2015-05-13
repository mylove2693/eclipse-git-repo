package com.cynovo.sirius.database;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.parameter.PayResultParameters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class PayRecordManager {
	public static void recordPayResult(Context context,
			PayResultParameters parameters) {
		// "INSERT INTO `ReturnCode` VALUES('OT','金额过小','B','金额过小','金额过小')";
		SQLiteDatabase msqlitedb = LocalDatabase.getInstance(context)
				.getSQLiteDatabase();
		String sql = "INSERT INTO `record` (`tradeType`, `amount`, "
				+ "`traceNo`, `originalTraceNo`, `requestDate`, "
				+ "`requestTime`, `ackDate`, `ackTime`, `ackNo`, "
				+ "`operatorNo`, `cardNo`, `batchid`,"
				+ "`referenceNo`, `terminalCode`, "
				+ "`merchantCode`, `authorizationNo`, `clearingDate`, "
				+ "`issuerbankid`, `acqbank`, `ackNoPrompt`) " + "VALUES "
				+ "('"
				+ parameters.getReqTransType()
				+ "','"
				+ parameters.getReqTransAmount()
				+ "','"
				+ parameters.getTraceNo()
				+ "','"
				+ PayMainActivity.sTraceNo
				+ "','"
				+ parameters.getReqTransDate()
				+ "','"
				+ parameters.getReqTransTime()
				+ "','"
				+ parameters.getRespTransDate()
				+ "','"
				+ parameters.getRespTransTime()
				+ "','"
				+ parameters.getRespCode()
				+ "','"
				+ parameters.getReqTransOperator()
				+ "','"
				+ parameters.getCardNo()
				+ "','"
				+ parameters.getBatchNo()
				+ "','"
				+ parameters.getReferenceNo()
				+ "','"
				+ parameters.getTerminalNo()
				+ "','"
				+ parameters.getMerchantNo()
				+ "','"
				+ parameters.getAuthorizationNo()
				+ "','"
				+ parameters.getClearDate()
				+ "','"
				+ parameters.getIssuer()
				+ "','"
				+ parameters.getAcquirer()
				+ "', '"
				+ parameters.getRespDesc() + "')";
		msqlitedb.execSQL(sql);
	}
}
