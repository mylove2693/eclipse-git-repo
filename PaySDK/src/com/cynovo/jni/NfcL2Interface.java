package com.cynovo.jni;

import android.util.Log;

public class NfcL2Interface {
	static {
		System.loadLibrary("cynovo_nfc_l2");
		System.loadLibrary("NFCKernel");
		// System.loadLibrary("koolcloudPos");
	}

	public native static int loadKernel();

	public native static int unloadKernel();

	/**
	 * 
	 * @param kernelMode
	 *            0:auto 1:manual
	 * @return
	 */
	public native static int emvKernelInit(int kernelMode);

	public native static int setTradeType(short transType);

	public native static int rfReaderInit();

	public native static int rfReaderRelease();

	public native static int rfReaderActiveCard(int timeout);

	public native static int tradePrepare(long amount);

	public native static int tradeProcess();

	public native static int tradeEnd(int status);

	public native static int emvReadLog(byte[] tradeList);

	public native static int downloadParam();

	public native static int getTagValue(short tag, byte[] tagValue);

	public native static int setBusinessName(byte[] merchantName,
			int merchantNameLen);

	public static void cardEventOccure(int cardEventType) {
		// int cardType = getCardType();
		// EMVKernel.cardEventCallBack(cardType, cardEventType);
		EmvL2Event.setCardEvent(cardEventType);
	}

	// add thess interface for test
	public native static int appSelect(char readCardMode);

	public native static short qpbocAppInit(byte[] state);

	public native static short readAppData(char mode);

}
