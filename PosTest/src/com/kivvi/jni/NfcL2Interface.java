package com.kivvi.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KIVVI_CONTACTLESS_CARD
 */

public class NfcL2Interface {
	static {
		System.loadLibrary("kivvi_nfc_l2");
	}

	/**
	 * Load NFC kernel
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int loadKernel();

	/**
	 * Unload NFC kernel
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int unloadKernel();

	/**
	 * Initialize the NFC kernel
	 * @param kernelMode : set "1"
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int emvKernelInit(int kernelMode);

	/**
	 * Set card event
	 * @param event
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int setCardEvent(int event);
	
	/**
	 * Get card event
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int getCardEvent();
	
	/**
	 * Initialize the contactless reader
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int rfReaderInit();

	/**
	 * Release the contactless reader
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int rfReaderRelease();

	/**
	 * Active the contactless card
	 * @param timeout : time out(ms)
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int rfReaderActiveCard(int timeout);

	/**
	 * Prepare the transaction
	 * @param amount : transaction amount
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int tradePrepare(long amount);

	/**
	 * Process the transaction
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int tradeProcess();
	
	/**
	 * Trade end
	 * @param status
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int tradeEnd(int status);

	/**
	 * Read transaction record
	 * @param tradeList
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int emvReadLog(byte[] tradeList);

	/**
	 * Download parameters
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int downloadParam();

	/**
	 * Get tag value
	 * @param tag : emv tag
	 * @param tagValue : tag value
	 * @return > 0 : emv tag length; <= 0 : fail
	 */
	public native static int getTagValue(short tag, byte[] tagValue);

	/**
	 * Select the application
	 * @param readCardMode
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static int appSelect(char readCardMode);
	
	/**
	 * Qpboc application initialize
	 * @param state
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static short qpbocAppInit(byte[] state);
	
	/**
	 * Read application data
	 * @param mode
	 * @return >= 0 : success; < 0 : fail
	 */
	public native static short readAppData(char mode);
}
