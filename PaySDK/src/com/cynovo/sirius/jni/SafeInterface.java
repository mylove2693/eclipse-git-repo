package com.cynovo.sirius.jni;

public class SafeInterface {
	static {
		System.loadLibrary("UnionpayCloudPos");
		System.loadLibrary("safemodule");
	}

	public native static int open();

	public native static int close();

	public native static int installCert(HsmObject hsmobject,
			byte pObjectData[], int nDataLength, int nDataType);

	public native static int reset();

	public native static int readCert(int nIndex, HsmObject hsmobject,
			byte pObjectData[], int nDataLength, int nDataType);

	public native static int safe_getVer(byte nVersion[]);

	public native static int safe_storeID(byte merchantId[], byte terminalId[]);

	public native static int safe_readID(byte merchantId[], byte terminalId[]);

	public native static int safe_storeAID(byte aidContex[], int recordLen,
			int recordID);

	public native static int safe_storeCAPK(byte capkContex[], int recordLen,
			int recordID);
}