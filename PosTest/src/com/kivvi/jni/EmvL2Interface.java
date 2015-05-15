package com.kivvi.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KIVVI_SMARTCARD
 */

public class EmvL2Interface {
	static {
		System.loadLibrary("kivvi_emv_l2");
	}
	
	/**
	 * Initialize the EMV kernel
	 * @param mode : set 1
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int emvKernelInit();
	
	/**
	 * Open the EMV reader
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int openReader();
	
	/**
	 * Close the EMV reader
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int closeReader();
	
	/**
	 * Query whether the card is inserted
	 * @return 1 : card presence; 0 : card not presence
	 */
	public native static int queryCardPresence();
	
	/**
	 * Get card holder verify method
	 * @param tag9F61Data
	 * @param tag9F61Len
	 * @param printMethod
	 * @return >= 0 : success; < 0 : fail
	 */
	//public native static int getVerifyMethod(byte[] tag9F61Data, byte[] tag9F61Len, byte[] printMethod);
	
	/**
	 * Trade Prepare
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int tradePrepare();
	
	/**
	 * Trade Process
	 * @param pinRes
	 * 			1: PIN Try Limit exceeded
	 *			2: PIN entry required and PIN pad not present or not working
	 *			3: PIN entry required, PIN pad present, but PIN was not entered
	 *			4: Online PIN entered
	 *			5: Bypass PIN
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int tradeProcess(int pinRes);
	
	/**
	 * Trade end
	 * @return >= 0 : success; <0 : fail
	 */
	public native static int tradeEnd();
	
	/**
	 * Set trade sum
	 * @param tradeSum
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int setTradeSum(long tradeSum);
	
	/**
	 * Set card event
	 * @param cardEvent
	 * @return
	 */
	public native static int setCardEvent(int cardEvent);
	
	/**
	 * Get card event
	 * @return
	 */
	public native static int getCardEvent();
	
	/**
	 * Get tag value
	 * @param tag
	 * @param tagValue
	 * @return > 0 : tag length; <= 0 : fail
	 */
	public native static int getTagValue(short tag, byte[] tagValue);
	
	/**
	 * Get online data
	 * @param onlineData : online data
	 * @return >= 0 : success; <0 : fail
	 */
	public native static int getOnlineData(byte[] onlineData);
	
	/**
	 * Online request
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int onlineRequest();
	
	/**
	 * Online response
	 * @param f55Data : field 55 data
	 * @param f55DataLen : field 55 data length
	 * @param respCode : response code from field 39
	 * @param respCodeLen : response code length
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int onlineResponse(byte[] f55Data, int f55DataLen, byte[] respCode, int respCodeLen);
	
	/**
	 * Add aid parameter
	 * @param aidParam : application identify parameter
	 * @param len : application identify parameter length
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int addAidParam(byte[] aidParam, int len);
	
	/**
	 * Delete aid parameter
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int delAidParam();
	
	/**
	 * Add capk parameter
	 * @param capkParam : capk parameter
	 * @param len : capk parameter length
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int addCapkParam(byte[] capkParam, int len);
	
	/**
	 * Delete capk parameter
	 * @return >= 0 : success; < 0 :fail
	 */
	public native static int delCapkParam();
}
