package com.cynovo.sirius.bankcard;

import android.util.Log;

import com.cynovo.sirius.flow.Input.TradeType;
//import com.cynovo.sirius.jni.PbocInterface;
//import com.cynovo.sirius.jni.PbocRegistInterface;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.Utility;
import com.kivvi.jni.EmvL2Interface;
import com.kivvi.jni.ISO8583Interface;
import com.kivvi.jni.ISO8583Interface.ICDataClass;

public class BankEmvData {

	private BankIcData icdata = null;
	private static boolean isCardPowerOn = false;

	public static int open() {
		// modified by
		// wanhaiping-------------------------------------------->begin
		/*
		 * int ret = PbocInterface.OpenReader(); if (ret < 0) {
		 * MyLog.i("PbocInterface", " OpenReader: faile"); return ret; }
		 * 
		 * PbocRegistInterface.initEmvState(PbocInterface.QueryCardPresence());
		 * ret = PbocRegistInterface.RegisterNotify(); if (ret < 0) {
		 * MyLog.i("PbocRegistInterface",
		 * "PbocRegistInterface RegisterNotify: faile"); return ret; }
		 */
		int ret = EmvL2Interface.emvKernelInit();
		if (ret < 0) {
			MyLog.e("debug","EmvL2Interface "+ " emvKernelInit: faile");
			return ret;
		}

		ret = EmvL2Interface.openReader();
		if (ret < 0) {
			MyLog.e("debug","EmvL2Interface " + " OpenReader: faile");
			return ret;
		}

		// modified by
		// wanhaiping---------------------------------------------<end
		return 0;
	}

	public static void close() {
		emvPause();
		// modified by wanhaiping------------------------------------>begin
		/*
		 * int ret = PbocRegistInterface.UnRegisterNotify(); if (ret < 0) {
		 * MyLog.i("PbocInterface", " UnRegisterNotify: faile"); }
		 * 
		 * ret = PbocInterface.CloseReader(); if (ret < 0) {
		 * MyLog.i("PbocInterface", "CloseReader failed"); return; }
		 */

		EmvL2Interface.closeReader();

		// modified by wanhaiping------------------------------------<end
	}

	public boolean QueryEmvPresence() {
		MyLog.d("PosService", "emv polling....");

		/*
		 * int retval = PbocRegistInterface.poll();
		 * MyLog.i("PbocInterface poll: ", String.valueOf(retval)); if (retval >
		 * 0) { return true; } return false;
		 */

		int ret = EmvL2Interface.queryCardPresence();
		ret = EmvL2Interface.getCardEvent();

		if(ret == 0){
			return true;
		}else{
			return false;
		}

	}

	/*
	 * public boolean IsCardIn() { try { Thread.sleep(300); } catch
	 * (InterruptedException e) { e.printStackTrace(); } int retval =
	 * PbocInterface.QueryCardPresence(); MyLog.i("PbocInterface poll: ",
	 * String.valueOf(retval)); if (retval > 0) { return true; } return false; }
	 */

	public BankIcData getData() {
		return icdata;
	}

	public void clearData() {
		icdata = null;
	}

	public int GetEMVData(String price, TradeType mode) {

		// deleted by wanhaiping-------------------------------->begin
		/*
		 * int res = PbocInterface.CardPowerOn(); if (res < 0) {
		 * MyLog.i("PbocInterface", "CardPowerOn failed"); return -1; }
		 */
		// deleted by wanhaiping--------------------------------<end

		isCardPowerOn = true;
		byte[] input_result = Utility.Str2Bcd(price);

		// modified by wanhaiping------------------------------->begin
		// res = PbocInterface.SetTradeSum(input_result);
		long sum = Long.parseLong(price);
		int res = EmvL2Interface.setTradeSum(sum);
		// modified by wanhaiping---------------------------------<end

		if (res < 0) {
			MyLog.i("PbocInterface", "SetTradeSum failed");
			return -1;
		}

		// ----------------------------------------------------->begin
		/*
		 * byte[] list = new byte[128]; int pLen =
		 * PbocInterface.BuildAppList(list); if (pLen <= 0) {
		 * MyLog.i("PbocInterface", "BuildAppList failed"); return -1; } int
		 * para1 = 1; int para2 = 0; byte[] app = new byte[8]; res =
		 * PbocInterface.SelectApp(para1, para2, app); if (res < 0) {
		 * MyLog.i("PbocInterface", "SelectApp failed"); return -1; } res =
		 * PbocInterface.ReadAppRecord(); if (res < 0) {
		 * MyLog.i("PbocInterface", "readAppRecord failed"); return -1; }
		 */

		res = EmvL2Interface.tradePrepare();
		if (res < 0) {
			MyLog.i("EmvL2Interface", "tradePrepare failed");
			return -1;
		}

		// -----------------------------------------------------<end

		// 如果是消费执行下面的，撤销和退货执行完下面的就退出
		if (mode != TradeType.Sale) {
			icdata = new BankIcData();
			icdata.setTrack2(getTrack2());
			ISO8583Interface.ICDataClass icDatac = new ISO8583Interface.ICDataClass();
			icDatac.PAN = getPAN();
			icDatac.cardSeqNo = getCardSeqNo();
			icDatac.dateOfExpired = getDataOfExpired();
			icdata.setIcdata(icDatac);
			return 0;
		}

		// modified by wanhaiping-------------------------------------->begin
		/*
		 * res = PbocInterface.OffLineDataAuth(); if (res < 0) {
		 * MyLog.i("PbocInterface", "OffLineDataAuth failed"); return -2; } res
		 * = PbocInterface.ProcessRestrict(); if (res < 0) {
		 * MyLog.i("PbocInterface", "ProcessRestrict failed"); return -2; }
		 * 
		 * res = PbocInterface.GetVerifyMethod(); if (res < 0) {
		 * MyLog.i("PbocInterface", "GetVerifyMethod failed"); return -2; }
		 * 
		 * res = PbocInterface.TerRiskManage(); if (res < 0) {
		 * MyLog.i("PbocInterface", "TerRiskManage failed"); return -2; }
		 * 
		 * res = PbocInterface.TerActionAnalyse(); if (res < 0) {
		 * MyLog.i("PbocInterface", "TerActionAnalyse failed"); return -2; }
		 */

		// modified by wanhaiping--------------------------------------<end

		icdata = new BankIcData();
		icdata.setTrack2(getTrack2());
		ISO8583Interface.ICDataClass icDatac = new ISO8583Interface.ICDataClass();

		icDatac.PAN = getPAN();
		icDatac.cardSeqNo = getCardSeqNo();
		icDatac.dateOfExpired = getDataOfExpired();
		icdata.setIcdata(icDatac);

		return 0;
	}

	// added fun by wanhaiping
	public int GetEMVData(String price, TradeType mode, int pinpadret) {

		// deleted by wanhaiping-------------------------------->begin
		/*
		 * int res = PbocInterface.CardPowerOn(); if (res < 0) {
		 * MyLog.i("PbocInterface", "CardPowerOn failed"); return -1; }
		 */
		// deleted by wanhaiping--------------------------------<end

		//isCardPowerOn = true;
		//byte[] input_result = Utility.Str2Bcd(price);

		// modified by wanhaiping------------------------------->begin
		// res = PbocInterface.SetTradeSum(input_result);
		long sum = Long.parseLong(price);
		int res = EmvL2Interface.setTradeSum(sum); //modified by punan

		// modified by wanhaiping---------------------------------<end
		
		if (res < 0) {
			MyLog.e("debug", "EmvL2Interface " + "SetTradeSum failed");
			return -1;
		}
		MyLog.e("debug", "EmvL2Interface " + "SetTradeSum success");

		
		res = EmvL2Interface.tradePrepare();
		
		if (res < 0) {
			MyLog.e("debug", "EmvL2Interface " + "tradePrepare failed");
			return -1;
		}
		MyLog.e("debug", "EmvL2Interface " + "tradePrepare success");
		
		// ----------------------------------------------------->begin
		/*
		 * byte[] list = new byte[128]; int pLen =
		 * PbocInterface.BuildAppList(list); if (pLen <= 0) {
		 * MyLog.i("PbocInterface", "BuildAppList failed"); return -1; } int
		 * para1 = 1; int para2 = 0; byte[] app = new byte[8]; res =
		 * PbocInterface.SelectApp(para1, para2, app); if (res < 0) {
		 * MyLog.i("PbocInterface", "SelectApp failed"); return -1; } res =
		 * PbocInterface.ReadAppRecord(); if (res < 0) {
		 * MyLog.i("PbocInterface", "readAppRecord failed"); return -1; }
		 */

		// deleted by wanhaiping------------------------------->begin
		/*
		 * res = EmvL2Interface.tradePrepare(); if (res < 0) {
		 * MyLog.i("EmvL2Interface", "tradePrepare failed"); return -1; }
		 */
		// deleted by wanhaiping-------------------------------<end

		// 如果是消费执行下面的，撤销和退货执行完下面的就退出
		if (mode != TradeType.Sale) {
			icdata = new BankIcData();
			icdata.setTrack2(getTrack2());
			ISO8583Interface.ICDataClass icDatac = new ISO8583Interface.ICDataClass();
			icDatac.PAN = getPAN();
			icDatac.cardSeqNo = getCardSeqNo();
			icDatac.dateOfExpired = getDataOfExpired();
			icdata.setIcdata(icDatac);
			return 0;
		}

		// modified by wanhaiping-------------------------------------->begin
		/*
		 * res = PbocInterface.OffLineDataAuth(); if (res < 0) {
		 * MyLog.i("PbocInterface", "OffLineDataAuth failed"); return -2; } res
		 * = PbocInterface.ProcessRestrict(); if (res < 0) {
		 * MyLog.i("PbocInterface", "ProcessRestrict failed"); return -2; }
		 * 
		 * res = PbocInterface.GetVerifyMethod();
		 * 
		 * if (res < 0) { MyLog.i("PbocInterface", "GetVerifyMethod failed");
		 * return -2; }
		 * 
		 * res = PbocInterface.TerRiskManage(); if (res < 0) {
		 * MyLog.i("PbocInterface", "TerRiskManage failed"); return -2; }
		 * 
		 * res = PbocInterface.TerActionAnalyse(); if (res < 0) {
		 * MyLog.i("PbocInterface", "TerActionAnalyse failed"); return -2; }
		 */

		// deleted by wanhaiping-------------------------------------->begin
		/*
		 * res = EmvL2Interface.tradeProcess(pinpadret); if (res < 0) {
		 * MyLog.i("EmvL2Interface", "tradeProcess failed"); return -2; }
		 */
		// deleted by wanhaiping--------------------------------------<end

		icdata = new BankIcData();
		icdata.setTrack2(getTrack2());

		ISO8583Interface.ICDataClass icDatac = new ISO8583Interface.ICDataClass();

		icDatac.PAN = getPAN();
		icDatac.cardSeqNo = getCardSeqNo();
		icDatac.dateOfExpired = getDataOfExpired();
		icdata.setIcdata(icDatac);

		return 0;
	}

	
	public int packF55Data() {
		byte[] pData = new byte[1024];
		int onlineDataLen = EmvL2Interface.getOnlineData(pData);
		
		if(icdata == null)
			return -1;
		icdata.getIcdata().f55 = pData;
		icdata.getIcdata().f55Length = onlineDataLen;
		
		Log.e("debug", "pData= "+new String(pData)+", onlineDataLen= "+onlineDataLen);
		
		return 0;
	}
	
	public int calculateF55() { // 原名sendOnlineMessage误导，并没有在线通信，仅仅是计算55域

		// modified ------------------------------------------------>begin
		byte[] pData = new byte[1024];
		byte[] tagdata = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		int templen;
		int pLen = 0;

		/*
		 * int pLen = PbocInterface.SendOnlineMessage(pData); if (pLen <= 0) {
		 * MyLog.i("PbocInterface", "SendOnlineMessage failed"); return -2; }
		 */

		int ret = EmvL2Interface.onlineRequest();
		if (ret < 0) {
			MyLog.e("EmvL2Interface onlineRequest failed");
		}

		templen = EmvL2Interface.getTagValue((short) 0x9F26, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F27, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F10, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F37, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F36, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x95, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9A, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9C, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F02, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x5F2A, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x82, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F1A, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F03, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		templen = EmvL2Interface.getTagValue((short) 0x9F33, tagdata);
		System.arraycopy(tagdata, 0, pData, pLen, templen);
		pLen += templen;

		// modified by
		// wanhaiping------------------------------------------------------<end

		String str = "";
		for (int i = 0; i < pLen; i++)
			str += String.format("%02x ", pData[i]);
		MyLog.i("ICDataClass pData ", str);
		if (icdata == null)
			return -1;

		icdata.getIcdata().f55 = pData;
		icdata.getIcdata().f55Length = pLen;

		return 0;
	}

	private static void emvPause() {

		byte[] f55;
		byte[] pData = new byte[1024];
		f55 = pData;

		// delete by wanhaiping------------------------------------>begin
		/*
		 * if (isCardPowerOn) { int ret = PbocInterface.CardPowerOff(); if (ret
		 * < 0) { MyLog.i("PbocInterface", "CardPowerOff failed"); return; }
		 * isCardPowerOn = false; MyLog.d("EMVPOS", "ret value :" + ret); }
		 */
		// delete by wanhaiping-------------------------------------<end
	}

	
	public int recvOnlineData(byte[] onlineData, int onlineDataLen, byte[] respCode, int respCodeLen) {
		int ret;
		
		ret = EmvL2Interface.onlineResponse(onlineData, onlineDataLen, respCode, respCodeLen);
		if (ret < 0) {
			System.out.println("recvOnlineMessage failed");
			return -1;
		}
		
		//交易结束
		ret = EmvL2Interface.tradeEnd();
		if (ret < 0) {
			System.out.println("tradeEnd failed");
			return -2;
		}
		
		return 0;
	}
	
	public static int emvReceive() {
		byte[] f55 = new byte[255];
		int f55Len = 0;

		// delele by
		// wanhaiping---------------------------------------------->begin
		f55Len = ISO8583Interface.getField55(f55);
		// delele by
		// wanhaiping-----------------------------------------------<end

		MyLog.i("debug","f5Len"+ String.valueOf(f55Len));

		MyLog.i("debug","ICDataClass pData "+
				"-----------------------------------------");
		String str = "";
		for (int i = 0; i < f55Len; i++)
			str += String.format("%02x ", f55[i]);
		MyLog.i("debug","ICDataClass pData= "+ str);
		MyLog.i("debug","ICDataClass pData "+
				"-----------------------------------------");

		// added by
		// wanhaiping---------------------------------------------------->begin
		byte[] f39 = new byte[255];
		int respCodeLen;
		f39 = ISO8583Interface.getAckNo();
		MyLog.i("debug","f39"+ String.valueOf(f39));
		// added by
		// wanhaiping----------------------------------------------------<end

		int ret = 0;
		if (f55Len >= 0) {
			// ret:-1 失败有脚本 1: 成功有脚本 -2: 失败无脚本 0:成功无脚本

			// modified by
			// wanhaiping------------------------------------------------>begin
			// ret = PbocInterface.RecvOnlineMessage(f55, f55Len);
			ret = EmvL2Interface.onlineResponse(f55, f55Len, f39, 2);
			// modified by
			// wanhaiping------------------------------------------------<end

			MyLog.i("ICDataClass pData ",
					"ret:-1 失败有脚本 1: 成功有脚本  -2: 失败无脚本  0:成功无脚本-------:" + ret);
			return ret;
		}
		return -2;
	}

	public static String getPAN() {
		byte ba[] = new byte[255];

		// modified by wanhaiping------------------------------------>begin
		// int pLen = PbocInterface.GetTagData((short) 0x5A, ba);
		int pLen = EmvL2Interface.getTagValue((short) 0x5A, ba);
		// modified by wanhaiping------------------------------------<end

		String pan = Utility.bcd2Str(ba, pLen);
		MyLog.i("debug" , "icData.PAN = "+ pan);
		if ((pan.endsWith("f") || pan.endsWith("F")) && pan.length() >= 13)
			return pan.substring(0, pan.length() - 1);

		return pan;
	}

	/**
	 * @return 用于冲正获取IC卡TAG信息
	 */
	public static ICDataClass getReverseIcF55() {
		ICDataClass icDatac = new ICDataClass();
		icDatac.f55Length = 0;
		icDatac.f55 = new byte[255];
		icDatac.PAN = getPAN();
		icDatac.cardSeqNo = getCardSeqNo();
		icDatac.dateOfExpired = getDataOfExpired();

		byte ba[] = new byte[255];
		int pLen = 0;

		// modified by
		// wanhaiping------------------------------------------------------------>begin
		// 终端验证结果
		/*
		 * pLen = PbocInterface.GetTagData((short) 0x95, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 发卡行脚本结果 pLen =
		 * PbocInterface.GetTagData((short) 0xDF31, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 应用交易计数器 pLen = PbocInterface.GetTagData((short) 0x9F36, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 接口设备序列号 pLen =
		 * PbocInterface.GetTagData((short) 0x9F1E, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 发卡行应用数据 pLen = PbocInterface.GetTagData((short) 0x9F10, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen;
		 */
//
//		pLen = EmvL2Interface.getTagValue((short) 0x95, ba);
//		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
//		icDatac.f55Length += pLen;
//		// 发卡行脚本结果
//		pLen = EmvL2Interface.getTagValue((short) 0xDF31, ba);
//		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
//		icDatac.f55Length += pLen;
		// 应用交易计数器
		
		byte[] taglen = new byte[]{(byte) 0x9f, (byte) 0x36, (byte) 0x02};
		
		pLen = EmvL2Interface.getTagValue((short) 0x9F36, ba);
		
		System.arraycopy(taglen, 0, icDatac.f55, 0, taglen.length);
		System.arraycopy(ba, 0, icDatac.f55, 3, pLen);
		icDatac.f55Length = taglen.length + pLen;
		
		Log.e("debug", "punan icDatac.f55 =====>>>"+new String(icDatac.f55));
		
//		// 接口设备序列号
//		pLen = EmvL2Interface.getTagValue((short) 0x9F1E, ba);
//		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
//		icDatac.f55Length += pLen;
//		// 发卡行应用数据
//		pLen = EmvL2Interface.getTagValue((short) 0x9F10, ba);
//		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
//		icDatac.f55Length += pLen;

		// modified by
		// wanhaiping--------------------------------------------------------------<end

		return icDatac;
	}

	/**
	 * @return ICDataClass 用于脚本通知获取IC卡TAG信息
	 */
	public static ICDataClass get62IcF55() {
		ICDataClass icDatac = new ICDataClass();
		icDatac.f55Length = 0;
		icDatac.f55 = new byte[255];
		icDatac.PAN = getPAN();
		icDatac.cardSeqNo = getCardSeqNo();
		icDatac.dateOfExpired = getDataOfExpired();

		byte ba[] = new byte[255];
		int pLen = 0;

		// modified by
		// wanhaiping--------------------------------------------------------->begin
		// 终端性能
		/*
		 * pLen = PbocInterface.GetTagData((short) 0x9F33, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 终端验证结果 pLen =
		 * PbocInterface.GetTagData((short) 0x95, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * //不可预知数 // pLen = PbocInterface.GetTagData((short)0x9F37, ba); //
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen); //
		 * icDatac.f55Length += pLen; // 接口设备序列号 pLen =
		 * PbocInterface.GetTagData((short) 0x9F1E, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 发卡行应用数据 pLen = PbocInterface.GetTagData((short) 0x9F10, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 应用密文 pLen =
		 * PbocInterface.GetTagData((short) 0x9F26, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 应用交易计数器 pLen = PbocInterface.GetTagData((short) 0x9F36, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 应用交互特征 pLen =
		 * PbocInterface.GetTagData((short) 0x82, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 发卡行脚本结果 pLen = PbocInterface.GetTagData((short) 0xDF31, ba);
		 * System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		 * icDatac.f55Length += pLen; // 交易国家代码 pLen =
		 * PbocInterface.GetTagData((short) 0x9F1A, ba); System.arraycopy(ba, 0,
		 * icDatac.f55, icDatac.f55Length, pLen); icDatac.f55Length += pLen; //
		 * 交易日期 pLen = PbocInterface.GetTagData((short) 0x9A, ba);
		 */

		pLen = EmvL2Interface.getTagValue((short) 0x9F33, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 终端验证结果
		pLen = EmvL2Interface.getTagValue((short) 0x95, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// //不可预知数
		// pLen = PbocInterface.GetTagData((short)0x9F37, ba);
		// System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		// icDatac.f55Length += pLen;
		// 接口设备序列号
		pLen = EmvL2Interface.getTagValue((short) 0x9F1E, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 发卡行应用数据
		pLen = EmvL2Interface.getTagValue((short) 0x9F10, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 应用密文
		pLen = EmvL2Interface.getTagValue((short) 0x9F26, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 应用交易计数器
		pLen = EmvL2Interface.getTagValue((short) 0x9F36, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 应用交互特征
		pLen = EmvL2Interface.getTagValue((short) 0x82, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 发卡行脚本结果
		pLen = EmvL2Interface.getTagValue((short) 0xDF31, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 交易国家代码
		pLen = EmvL2Interface.getTagValue((short) 0x9F1A, ba);
		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		// 交易日期
		pLen = EmvL2Interface.getTagValue((short) 0x9A, ba);

		// modified by
		// wanhaiping------------------------------------------------------<end

		System.arraycopy(ba, 0, icDatac.f55, icDatac.f55Length, pLen);
		icDatac.f55Length += pLen;
		return icDatac;
	}

	public static String getDataOfExpired() {
		byte ba[] = new byte[255];

		// modified by wanhaiping------------------------------------->begin
		// int pLen = PbocInterface.GetTagData((short) 0x5F24, ba);

		int pLen = EmvL2Interface.getTagValue((short) 0x5F24, ba);

		// modified by wanhaiping-------------------------------------<end

		for (int i = 0; i < pLen; i++) {
			MyLog.i("debug","b" + String.format("0x%02x", ba[i]));
		}
		String dateOfExpired = Utility.bcd2Str(ba, pLen);
		dateOfExpired = dateOfExpired.substring(0, dateOfExpired.length() - 2);
		MyLog.i("debug" , "icData.dateOfExpired" + dateOfExpired);
		return dateOfExpired;
	}

	public static String getCardSeqNo() {
		byte ba[] = new byte[255];

		// modified by wanhaiping------------------------------------->begin
		// int pLen = PbocInterface.GetTagData((short) 0x5F34, ba);

		int pLen = EmvL2Interface.getTagValue((short) 0x5F34, ba);

		// modified by wanhaiping-------------------------------------<end

		for (int i = 0; i < pLen; i++) {
			MyLog.i("debug","b===>>>"+ String.format("0x%02x", ba[i]));
		}
		String cardSeqNo = "0" + Utility.bcd2Str(ba, pLen);
		MyLog.i("debug","icData.cardSeqNo"+ cardSeqNo);
		return cardSeqNo;
	}

	public static String getTrack2() {
		byte ba[] = new byte[255];

		// modified by wanhaiping------------------------------------->begin
		// int pLen = PbocInterface.GetTagData((short) 0x57, ba);
		int pLen = EmvL2Interface.getTagValue((short) 0x57, ba);
		// modified by wanhaiping-------------------------------------<end

		MyLog.e("debug", "tag len =" +String.valueOf(pLen));
		
		if(pLen == 0)
			return null;
		
		String track2 = Utility.bcd2Str(ba, pLen);
		
		track2 = track2.substring(0, track2.length() - 1);
		MyLog.e("debug", "track2 =" +track2);
		
		return track2;
	}

	private static final int TAG_9F36 = 0x9F36;
	private static final int TAG_0091 = 0x0091;
	private static final int TAG_0071 = 0x0071;
	private static final int TAG_0072 = 0x0072;

	int iOnlineRecv(byte[] pucRecvData, int uiRecvDataLen) {
		int uiPos = 0;
		int uiTag;
		int uiTempLen = 0;

		while (uiPos < uiRecvDataLen) {
			uiTag = pucRecvData[uiPos];
			if (uiTag == 0 || uiTag == 0xFF) {
				uiPos++;
				continue;
			}

			if ((uiTag & 0x1F) == 0x1F) // 双字节TAG
			{
				uiPos++;
				uiTag = (uiTag << 8) + pucRecvData[uiPos];
			}

			switch (uiTag) {
			// 应用交易计数器
			case TAG_9F36:
				uiPos++;
				uiTempLen = pucRecvData[uiPos];
				uiPos++;
				uiPos += uiTempLen;
				break;
			// 发卡行认证数据
			case TAG_0091:
				uiPos++;
				uiTempLen = pucRecvData[uiPos];
				// MyLogI(MyLog_TAG, "0091 Len: %d", uiTempLen);
				uiPos++;
				// memcpy(ptrOnlineManageData->aucIssuerData, pucRecvData +
				// uiPos, uiTempLen);
				// ptrOnlineManageData->ucIssuerDataLen = uiTempLen;

				uiPos += uiTempLen;
				break;

			// 发卡行脚本1
			case TAG_0071:
				uiPos++;
				uiTempLen = pucRecvData[uiPos];
				uiPos++;
				// memcpy(ptrOnlineManageData->aucScript, pucRecvData + uiPos,
				// uiTempLen);
				// ptrOnlineManageData->ucScriptLen = uiTempLen;
				uiPos += uiTempLen;

				break;

			// 发卡行脚本2
			case TAG_0072:
				uiPos++;
				uiTempLen = pucRecvData[uiPos];
				uiPos++;
				// memcpy(ptrOnlineManageData->aucScript, pucRecvData + uiPos,
				// uiTempLen);
				// ptrOnlineManageData->ucScriptLen = uiTempLen;
				uiPos += uiTempLen;
				break;
			default:
				uiPos++;
				break;
			}
		}
		return 0;
	}
}
