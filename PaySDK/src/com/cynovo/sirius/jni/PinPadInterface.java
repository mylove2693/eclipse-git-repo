package com.cynovo.sirius.jni;

public class PinPadInterface {

	static {
		System.loadLibrary("UnionpayCloudPos");
		System.loadLibrary("pinpad");
	}

	public final static int KEY_TYPE_DUKPT = 0;
	public final static int KEY_TYPE_TDUKPT = 1;
	public final static int KEY_TYPE_MASTER = 2;
	public final static int KEY_TYPE_PUBLIC = 3;
	public final static int KEY_TYPE_FIX = 4;

	public static final int[] MASTER_KEY_ID = new int[] { 0x00, 0x01, 0x02,
			0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09 };
	public static final int[] USER_KEY_ID = new int[] { 0x00, 0x01 };

	public static final int ALGORITH_3DES = 1;
	public static final int ALGORITH_DES = 0;
	public final static int MAC_METHOD_X99 = 0;
	public final static int MAC_METHOD_ECB = 1;

	public native static int open();

	public native static int checkPinpad();

	public native static int scrclrLine(int nLineIndex);

	public native static int showText(int nLineIndex, byte arryText[],
			int nTextLength, int nFlagSound);

	public native static int selectKey(int nKeyType, int nMasterKeyID,
			int nUserKeyID, int nAlgorith);

	public native static int encryptString(byte arryPlainText[],
			int nTextLength, byte arryCipherTextBuffer[]);

	/**
	 * 等待输入密码
	 * 
	 * @param arryASCIICardNumber
	 * @param nCardNumberLength
	 * @param arryPinBlockBuffer
	 * @param nTimeout_MS
	 * @param nFlagSound
	 * @return
	 */
	public native static int calculatePinBlock(byte arryASCIICardNumber[],
			int nCardNumberLength, byte arryPinBlockBuffer[], int nTimeout_MS,
			int nFlagSound);

	public native static int calculateMac(byte arryData[], int nDataLength,
			int nMACFlag, byte arryMACOutBuffer[]);

	public native static int updateUserKey(int nMasterKeyID, int nUserKeyID,
			byte arryCipherNewUserKey[], int nCipherNewUserKeyLength,
			byte nMac[]);

	public native static int setPinLength(int nLength, int nFlag);

	public native static int updateMasterKey(int nMasterKeyID, byte pOldKey[],
			int nOldKeyLength, byte pNewKey[], int nNewKeyLength,
			byte nRandom[], byte nMac[]);

	public native static int close();
}
