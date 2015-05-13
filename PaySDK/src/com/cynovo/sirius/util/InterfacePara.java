package com.cynovo.sirius.util;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class InterfacePara {

	public enum PaymentMode {
		CardPay, WeiXin, ZhiFuBao
	}

	public enum CardMode {
		UninLinkPay, YiBao, MisPos
	}

	public static String strAccountUnionLink;
	public static String strAccountWeixin;

	public PaymentMode paymode;
	public CardMode cardmode;

	public boolean ParseInput(String data) {
		String strAccount = "";
		String strPayMode = "";
		if (data == null || data.length() == 0) {
			return false;
		}

		try {
			JSONObject jsonObjectRet = new JSONObject(data);
			strAccount = jsonObjectRet.getString("ReqTransAmount");
			strPayMode = jsonObjectRet.getString("ReqPayMode");
			Log.e("debug", strAccount);
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e("debug", "Failed get Account");
			// return false;
		}

		// 01 刷卡, 02 微信, 03 支付宝, 04
		paymode = PaymentMode.CardPay;
		if (strPayMode.equals("01")) {
			paymode = PaymentMode.CardPay;
		}

		else if (strPayMode.equals("02")) {
			paymode = PaymentMode.WeiXin;
		}

		else if (strPayMode.equals("03")) {
			paymode = PaymentMode.ZhiFuBao;
		}

		if (strAccount.length() < 3) {
			return false;
		}

		String strTempAccount = strAccount.replaceAll("^(0+)", "");

		int location = strTempAccount.indexOf('.');
		if (location != -1) {
			String left = strTempAccount.substring(0, location);
			String right = strTempAccount.substring(location + 1,
					strTempAccount.length());
			strTempAccount = left + right;
		} else {
			return false;
		}

		int length = strTempAccount.length();
		for (int i = 0; i < length; i++) {
			char temp = strTempAccount.charAt(i);
			if (temp < '0' || temp > '9') {
				return false;
			}
		}

		strAccountWeixin = strTempAccount;
		Log.e("debug", "generate AccountWeixin");
		Log.e("debug", strAccountWeixin);

		String strZeor = "";
		if (length < 12) {
			int ZeroLen = 12 - length;
			for (int i = 0; i < ZeroLen; i++) {
				strZeor += "0";
			}
			strZeor += strTempAccount;
		}

		else {
			strZeor = strTempAccount.subSequence(length - 12, length)
					.toString();
		}

		strAccountUnionLink = strZeor;
		Log.e("debug", "generate AccountUnionPay");
		Log.e("debug", strAccountUnionLink);

		return true;
	}

}
