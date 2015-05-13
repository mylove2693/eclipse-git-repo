package com.cynovo.sirius.parameter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.cynovo.sirius.PaySDK.PayMainActivity;
import com.cynovo.sirius.database.BankErrorCodeManager;
//import com.cynovo.sirius.jni.ISO8583Interface;
import com.cynovo.sirius.util.MyLog;
import com.kivvi.jni.ISO8583Interface;

/**
 * 支付成功后，返回的结果参数
 * 
 * @author Feel
 */
public class PayResultParameters {
	private String ReqTransType; // 交易类型
	private String ReqTransAmount; // 交易金额
	private String ReqTransDate; // 交易请求日期
	private String ReqTransTime; // 交易请求时间
	private String ReqTransOperator; // 操作员号
	private String RespTransDate; // 交易应答日期
	private String RespTransTime; // 交易应答时间
	private String RespCode; // 返回码
	private String RespDesc; // 返回码中文解释
	private String CardNo; // 卡号
	private String BatchNo; // 批次号
	private String ReferenceNo; // 参考号
	private String TraceNo; // 凭证号
	private String TerminalNo; // 终端号
	private String MerchantNo; // 商户号
	private String AuthorizationNo; // 授权码
	private String ClearDate; // 清算日期
	private String Issuer; // 发卡行
	private String Acquirer; // 收单行

	public String getReqTransType() {
		return ReqTransType;
	}

	public void setReqTransType(String reqTransType) {
		ReqTransType = reqTransType;
	}

	public String getReqTransAmount() {
		return ReqTransAmount;
	}

	public void setReqTransAmount(String reqTransAmount) {
		ReqTransAmount = reqTransAmount;
	}

	public String getReqTransDate() {
		return ReqTransDate;
	}

	public void setReqTransDate(String reqTransDate) {
		ReqTransDate = reqTransDate;
	}

	public String getReqTransTime() {
		return ReqTransTime;
	}

	public void setReqTransTime(String reqTransTime) {
		ReqTransTime = reqTransTime;
	}

	public String getReqTransOperator() {
		return ReqTransOperator;
	}

	public void setReqTransOperator(String reqTransOperator) {
		ReqTransOperator = reqTransOperator;
	}

	public String getRespTransDate() {
		return RespTransDate;
	}

	public void setRespTransDate(String respTransDate) {
		RespTransDate = respTransDate;
	}

	public String getRespTransTime() {
		return RespTransTime;
	}

	public void setRespTransTime(String respTransTime) {
		RespTransTime = respTransTime;
	}

	public String getRespCode() {
		return RespCode;
	}

	public void setRespCode(String respCode) {
		RespCode = respCode;
	}

	public String getRespDesc() {
		return RespDesc;
	}

	public void setRespDesc(String respDesc) {
		RespDesc = respDesc;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getBatchNo() {
		return BatchNo;
	}

	public void setBatchNo(String batchNo) {
		BatchNo = batchNo;
	}

	public String getReferenceNo() {
		return ReferenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		ReferenceNo = referenceNo;
	}

	public String getTraceNo() {
		return TraceNo;
	}

	public void setTraceNo(String traceNo) {
		TraceNo = traceNo;
	}

	public String getTerminalNo() {
		return TerminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		TerminalNo = terminalNo;
	}

	public String getMerchantNo() {
		return MerchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		MerchantNo = merchantNo;
	}

	public String getAuthorizationNo() {
		return AuthorizationNo;
	}

	public void setAuthorizationNo(String authorizationNo) {
		AuthorizationNo = authorizationNo;
	}

	public String getClearDate() {
		return ClearDate;
	}

	public void setClearDate(String clearDate) {
		ClearDate = clearDate;
	}

	public String getIssuer() {
		return Issuer;
	}

	public void setIssuer(String issuer) {
		Issuer = issuer;
	}

	public String getAcquirer() {
		return Acquirer;
	}

	public void setAcquirer(String acquirer) {
		Acquirer = acquirer;
	}

	public void setPayResultParameters(Context context) {
		
		PayResultParameters parameters = AllPayParameters.mPayResult;

		
		 String field44 = new String(ISO8583Interface.getAdditionalData());
		 MyLog.e("field44:"+field44); 
		 
		 String field55 = ISO8583Interface.getField55(new byte[55]) + "";
		 MyLog.e("field55:"+field55); 
		 
		 if(field44 != null && !field44.isEmpty() && field44.length() >= 11) {
			  parameters.setAcquirer(field44.substring(11).trim()); 
		 }
		 parameters.setAuthorizationNo(new String(ISO8583Interface.getAuthorizationNo()));
		 parameters.setBatchNo(new String(ISO8583Interface.getBatchNo()));
		 parameters.setCardNo(new String(ISO8583Interface.getAccountNo()));
		 parameters.setClearDate(new String(ISO8583Interface.getSettlementDate())); 
		 
		 if(field44 != null && !field44.isEmpty() && field44.length() >= 11) {
			  parameters.setIssuer(field44.substring(0, 11).trim()); 
		 }
		 
		 parameters.setMerchantNo(new String(ISO8583Interface.getMerchantCode()));
		 parameters.setReferenceNo(new String(ISO8583Interface.getRefenceNo()));
		 parameters.setReqTransAmount(new String(ISO8583Interface.getAmount()));
		 parameters.setReqTransDate(PayMainActivity.currentDate);
		 parameters.setReqTransOperator(PayMainActivity.operator);
		 parameters.setReqTransTime(PayMainActivity.currentTime);
		 parameters.setReqTransType(new String(ISO8583Interface.getMessageType())); 
		 
		 String ErrorCode = new String(ISO8583Interface.getAckNo());
		 parameters.setRespCode(ErrorCode);
		 parameters.setRespDesc(BankErrorCodeManager.getDesc(context, ErrorCode).getDisplay()); 
		 parameters.setRespTransDate(new String(ISO8583Interface.getDate())); 
		 parameters.setRespTransTime(new String(ISO8583Interface.getTime())); 
		 parameters.setTerminalNo(new String(ISO8583Interface.getTerminalCode()));
		 parameters.setTraceNo(new String(ISO8583Interface.getTraceNo()));
		
	}

	public JSONObject getPayResultJson() {
		PayResultParameters parameters = AllPayParameters.mPayResult;

		try {
			JSONObject json = new JSONObject();
			json.put("Acquirer", parameters.getAcquirer());
			json.put("AuthorizationNo", parameters.getAuthorizationNo());
			json.put("BatchNo", parameters.getBatchNo());
			json.put("CardNo", parameters.getCardNo());
			json.put("ClearDate", parameters.getClearDate());
			json.put("Issuer", parameters.getIssuer());
			json.put("MerchantNo", parameters.getMerchantNo());
			json.put("ReferenceNo", parameters.getReferenceNo());
			json.put("ReqTransAmount", parameters.getReqTransAmount());
			json.put("ReqTransDate", parameters.getReqTransDate());
			json.put("ReqTransOperator", parameters.getReqTransOperator());
			json.put("ReqTransTime", parameters.getReqTransTime());
			json.put("ReqTransType", parameters.getReqTransType());
			json.put("RespCode", parameters.getRespCode());
			json.put("RespDesc", parameters.getRespDesc());
			json.put("RespTransDate", parameters.getRespTransDate());
			json.put("RespTransTime", parameters.getRespTransTime());
			json.put("TerminalNo", parameters.getTerminalNo());
			json.put("TraceNo", parameters.getTraceNo());

			return json;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
}
