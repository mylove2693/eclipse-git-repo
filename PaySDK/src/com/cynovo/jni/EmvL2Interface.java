package com.cynovo.jni;

import android.util.Log;

public class EmvL2Interface {

	/*
	 * static { System.loadLibrary("cynovo_emv_l2"); }
	 * 
	 * public native static int loadKernel();
	 * 
	 * public native static int unloadKernel();
	 * 
	 * 
	 * public native static int emvKernelInit(int kernelMode);
	 * 
	 * public native static int openReader();
	 * 
	 * public native static int closeReader();
	 * 
	 * public native static int queryCardPresence();
	 * 
	 * public native static int cardPowerOn();
	 * 
	 * public native static int cardPowerOff();
	 * 
	 * public native static int selectApp(int mode);
	 * 
	 * public native static int appInit();
	 * 
	 * public native static int readAppData();
	 * 
	 * public native static int offLineDataAuth();
	 * 
	 * public native static int processRestrict();
	 * 
	 * public native static int getVerifyMethod(byte[] tag9F61Data, byte[]
	 * tag9F61Len, byte[] printMethod);
	 * 
	 * public native static int terRiskManage();
	 * 
	 * public native static int terActionAnalyse();
	 * 
	 * 
	 * public native static int onlineManage(int cardType, byte[] PAN, int
	 * panLen, int amount);
	 * 
	 * public native static int tradeEnd();
	 * 
	 * public native static int downloadParam();
	 * 
	 * public native static int getCheckSumValue(byte[] checkSum);
	 * 
	 * //public native static int setTradeSum(int tradeSum); public native
	 * static int setTradeSum(long tradeSum); //public native static int
	 * setTradeSum(double tradeSum);
	 * 
	 * public native static int setTradeType(short transType);
	 * 
	 * public native static int emvReadLog(byte[] tradeList);
	 * 
	 * public native static void postSem(char pressKey, char seqNum);
	 * 
	 * public native static int getADFTag0084(byte[] tag0084Value);
	 * 
	 * public native static void cardholderConfirm(char pressKey);
	 * 
	 * public native static int getTagValue(short tag, byte[] tagValue);
	 * 
	 * public native static int setBusinessName(byte[] merchantName, int
	 * merchantNameLen);
	 * 
	 * public static void cardEventOccure(int cardEventType) { //
	 * Log.i("jni.EMVKernelInterface", // int cardType = getCardType(); //
	 * EMVKernel.cardEventCallBack(cardType, cardEventType);
	 * EmvL2Event.setCardEvent(cardEventType);
	 * //BaseInterface.led_on(BaseInterface.COLOR_YELLOW); }
	 */

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Load the EMV kernel
	 * 
	 * @return >=0 : succ; < 0 : fail
	 */
	public native static int loadKernel();

	/**
	 * Unload the EMV kernel
	 * 
	 * @return >=0 : succ; < 0 : fail
	 */
	public native static int unloadKernel();

	/**
	 * Initialize the EMV kernel
	 * 
	 * @param mode
	 *            : set 1
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int emvKernelInit(int mode);

	/**
	 * Open the EMV reader
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int openReader();

	/**
	 * Close the EMV reader
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int closeReader();

	/**
	 * Query whether the card is inserted
	 * 
	 * @return 1 : card presence; 0 : card not presence
	 */
	public native static int queryCardPresence();

	/**
	 * Power on the card
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int cardPowerOn();

	/**
	 * Power off the card
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int cardPowerOff();

	/**
	 * Select the application in the smart card
	 * 
	 * @param mode
	 *            : set 0
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int selectApp(int mode);

	/**
	 * Initialize the application
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int appInit();

	/**
	 * Read the application data
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int readAppData();

	/**
	 * Offline data authentication
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int offLineDataAuth();

	/**
	 * Process restrict
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int processRestrict();

	/**
	 * Get user input pin result
	 * 
	 * @param pinRes
	 *            1: PIN Try Limit exceeded 2: PIN entry required and PIN pad
	 *            not present or not working 3: PIN entry required, PIN pad
	 *            present, but PIN was not entered 4: Online PIN entered 5:
	 *            Bypass PIN
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int getInputPinResult(int pinRes);

	/**
	 * Get card holder verify method
	 * 
	 * @param tag9F61Data
	 * @param tag9F61Len
	 * @param printMethod
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int getVerifyMethod(byte[] tag9F61Data,
			byte[] tag9F61Len, byte[] printMethod);

	/**
	 * Terminal risk manage
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int terRiskManage();

	/**
	 * Terminal action analysis
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int terActionAnalyse();

	/**
	 * Trade Prepare
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int tradePrepare();

	/**
	 * Trade Process
	 * 
	 * @param pinRes
	 *            1: PIN Try Limit exceeded 2: PIN entry required and PIN pad
	 *            not present or not working 3: PIN entry required, PIN pad
	 *            present, but PIN was not entered 4: Online PIN entered 5:
	 *            Bypass PIN
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int tradeProcess(int pinRes);

	/**
	 * Trade end
	 * 
	 * @return >= 0 : succ; <0 : fail
	 */
	public native static int tradeEnd();

	/**
	 * Download parameter
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int downloadParam();

	/**
	 * Get checksum value
	 * 
	 * @param checkSum
	 *            value
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int getCheckSumValue(byte[] checkSum);

	/**
	 * Set trade sum
	 * 
	 * @param tradeSum
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int setTradeSum(long tradeSum);

	/**
	 * Set trade type
	 * 
	 * @param transType
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int setTradeType(short transType);

	/**
	 * Read record
	 * 
	 * @param trade
	 *            record
	 * @return >= 0 : succ; < 0 : fail
	 */
	// public native static int emvReadLog(byte[] tradeRecord);

	/**
	 * Card holder confirm
	 * 
	 * @param pressKey
	 */
	// public native static void cardholderConfirm(char pressKey);

	/**
	 * Get tag value
	 * 
	 * @param tag
	 * @param tagValue
	 * @return > 0 : tag length; <= 0 : fail
	 */
	public native static int getTagValue(short tag, byte[] tagValue);

	/**
	 * Get online data
	 * 
	 * @param onlineData
	 *            : online data
	 * @return >= 0 : succ; <0 : fail
	 */
	public native static int getOnlineData(byte[] onlineData);

	/**
	 * Send online message
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int sendOnlineMessage();

	/**
	 * Receive online message
	 * 
	 * @param f55Data
	 *            : field 55 data
	 * @param f55DataLen
	 *            : field 55 data length
	 * @param respCode
	 *            : response code from field 39
	 * @param respCodeLen
	 *            : response code length
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int recvOnlineMessage(byte[] f55Data, int f55DataLen,
			byte[] respCode, int respCodeLen);

	/**
	 * Add aid parameter
	 * 
	 * @param aidParam
	 *            : application identify parameter
	 * @param len
	 *            : application identify parameter length
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int addAidParam(byte[] aidParam, int len);

	/**
	 * Delete aid parameter
	 * 
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int delAidParam();

	/**
	 * Add capk parameter
	 * 
	 * @param capkParam
	 *            : capk parameter
	 * @param len
	 *            : capk parameter length
	 * @return >= 0 : succ; < 0 : fail
	 */
	public native static int addCapkParam(byte[] capkParam, int len);

	/**
	 * Delete capk parameter
	 * 
	 * @return >= 0 : succ; < 0 :fail
	 */
	public native static int delCapkParam();

	/**
	 * Callback function, this function will be called by jni, user should not
	 * call it
	 * 
	 * @param cardEventType
	 */
	public static void cardEventOccure(int cardEventType) {
		EmvL2Event.setCardEvent(cardEventType);
	}

}
