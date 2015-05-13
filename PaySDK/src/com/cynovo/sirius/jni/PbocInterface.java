package com.cynovo.sirius.jni;

public class PbocInterface {

	static {
		System.loadLibrary("EMVKernel");
		System.loadLibrary("pboc");
	}

	// --login -c
	// "cd /cygdrive/H/PAR7_ALL/Com.Cynovo.EmvTestTool && /cygdrive/i/android-ndk-r4-crystax/ndk-build"

	public native static int open();

	public native static int close();

	public native static int EmvKernelInit();

	public native static int QueryCardPresence();

	public native static int CardPowerOn();

	public native static int CardPowerOff();

	public native static int GetTradeListInit();

	public native static int GetTradeList(int iRecordNo, byte[] pucRecordInfo);

	public native static int BuildAppList(byte[] list);

	public native static int SelectApp(int a, int b, byte[] result);

	public native static int ReadAppRecord();

	public native static int OffLineDataAuth();

	public native static int ProcessRestrict();

	public native static int GetVerifyMethod();

	public native static int TerRiskManage();

	public native static int TerActionAnalyse();

	// public native static int OnLineManage(byte[] pDate,int[] pLen);
	public native static int SendOnlineMessage(byte[] pDate);

	// public native static int GetCheckSumValue(byte[] value);
	public native static int TradeEnd();

	public native static int DownloadParam(byte[] param);

	public native static int GetTagValue(int result, byte[] value);

	public native static int GetTagData(short tag, byte[] tagData);

	public native static int TransParamSetSum(byte[] sum);

	public native static int GetBalance(byte[] balace);

	public native static int GetCheckSumValue(byte[] pucCheckSum,
			byte[] pucCheckSumLen);

	public native static int SetTradeSum(byte[] pucTradeSum);

	public native static int AppInit();

	public native static int RecvOnlineMessage(byte[] pDate, int pLen);

	public native static int emv_aidparam_clear();

	public native static int emv_aidparam_add(byte[] AIDParam, int dataLength);

	public native static int emv_capkparam_clear();

	public native static int emv_capkparam_add(byte[] CAPKParam, int dataLength);

	public native static int OpenReader();

	public native static int CloseReader();

	public native static int IssuerScriptAuth();
	// public native static int RegisterNotify();
	// public native static int UnRegisterNotify();
	// public native static int poll();

}
