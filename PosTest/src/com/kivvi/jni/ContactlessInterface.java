package com.kivvi.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 *  Permission explicit declaration android.permission.CYNOVO_SAFE_MODULE
 *  android.permission.CYNOVO_SAFE_MODULE_READONLY
 */

public class ContactlessInterface {

	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("kivvi_contactlesscard");
	}

	public static int CONTACTLESS_CARD_MODE_AUTO = 0;
	public static int CONTACTLESS_CARD_MODE_TYPE_A = 1;
	public static int CONTACTLESS_CARD_MODE_TYPE_B = 2;
	public static int CONTACTLESS_CARD_MODE_TYPE_C = 3;

	public static int RC500_COMMON_CMD_GET_READER_VERSION = 0x40;
	public static int RC500_COMMON_CMD_RF_CONTROL = 0x38;

	/* native methods as following */	
	
	
	/**
	  * @defgroup Contactless Contactless_API
	  * @{
	  */
	
	/**
	 * initialize the contactless card reader 
	 * @return 
	 *  - value >= 0 : correct handle
	 *  - value < 0 : error code, -1 : device doesn't exist, -10 : unknown error
	 */
	public native static int open();

	/**
	 * close the contactless card reader 
	 * @return 
	 *   - value >= 0 : success (suggest 0)
	 *   - value  < 0 : error code, - 1: parameter mismatch, - 2: I/O error, -10 : unknown error
	 */
	public native static int close();

	/**
	 * start searching the contactless card If you set the nCardMode is auto,
	 * reader will try to activate card in type A, type B and type successively;
	 * If you set the nCardMode is type A, type B, or type C, reader only try to
	 * activate card in the specified way.
	 * 
	 * @param : nCardMode : handle of this card reader possible value :
	 *        CONTACTLESS_CARD_MODE_AUTO = 0 CONTACTLESS_CARD_MODE_TYPE_A = 1;
	 *        CONTACTLESS_CARD_MODE_TYPE_B = 2; CONTACTLESS_CARD_MODE_TYPE_C =
	 *        3;
	 * @param nFlagSearchAll
	 *            0 : signal user if we find one card in the field 1 : signal
	 *            user only we find all card in the field
	 * @param nTimeout_MS
	 *            : time out in milliseconds. if nTimeout_MS is less then zero,
	 *            the searching process is infinite. 
	 * @return 
	 *   - value >= 0 : success (suggest 0) 
	 *   - value  < 0 : error code
	 */
	public native static int searchTargetBegin(int nCardMode,
			int nFlagSearchAll, int nTimeout_MS);

	/**
	 * stop the process of searching card.
	 * @return 
	 *    - value >= 0 : success (suggest 0) 
	 *    - value  < 0 : error code
	 */
	public native static int searchTargetEnd();

	/**
	 * attach the target before transmitting APDU command In this process, the
	 * target(card) is activated and return ATR
	 * 
	 * @param byteArrayATR
	 *            : ATR buffer, if you set it null, you can not get the data.
	 * @return 
	 *   - value >= 0 : success, return length of ATR;
	 *   - value  < 0 : error code
	 */
	public native static int attachTarget(byte byteArrayATR[]);

	/**
	 * detach the target. If you want to send APDU again, you should attach it.
	 * @return 
	 *   - value >= 0 : success (suggest 0)
	 *   - value  < 0 : error code
	 */
	public native static int detachTarget();

	/**
	 * transmit APDU command and get the response
	 * 
	 * @param byteArrayAPDU
	 *            : command of APDU
	 * @param nAPDULength
	 *            : length of command of APDU
	 * @param byteArrayResponse
	 *            : response of command of APDU 
	 * @return 
	 *   - value >= 0 : success (suggest 0) 
	 *   - value  < 0 : error code
	 */
	public native static int transmit(byte byteArrayAPDU[], int nAPDULength,
			byte byteArrayResponse[]);

	/**
	 * send control command.
	 * 
	 * @param nCmdID
	 *            : ID of command
	 * @param byteArrayCmdData
	 *            : data associated with command, if no data, you can set it
	 *            NULL
	 * @param nDataLength
	 *            : data length of command 
	 * @return 
	 *   - value >= 0 : success (suggest 0) 
	 *   - value  < 0 : error code
	 */
	public native static int sendControlCommand(int nCmdID,
			byte byteArrayCmdData[], int nDataLength);

	/**
	 * poll the event within period time.
	 * 
	 * @param nTimeout_MS
	 *            : time out in milliseconds. if nTimeout_MS is less then zero,
	 *            the event process is infinite.
	 * @param event
	 *            : event defined in ContactlessEvent.java 
	 * @return 
	 *  - value >= 0 :success (suggest 0) 
	 *  - value  < 0 : error code
	 */
	public native static int pollEvent(int nTimeout_MS, ContactlessEvent event);

	/**
	 * cancel the poll process invoked before. 
	 * @return 
	 *  - value >= 0 : success(suggest 0) 
	 *  - value  < 0 : error code
	 */
	public native static int cancelPoll();

	/**
	 * Verify pin only for MiFare one card
	 * 
	 * @param nSectorIndex
	 *            : sector index
	 * @param nPinType
	 *            == 0x0A : A type 
	 *            == 0x0B : B type
	 * @param strPin
	 *            : password of this pin
	 * @param nPinLength
	 *            : length of password 
	 * @return 
	 *  - value >= 0 : success (suggest 0)
	 *  - value  < 0 : error code
	 */
	public native static int verifyPinMifare(int nSectorIndex, int nPinType,
			byte[] strPin, int nPinLength);

	/**
	 * Read data only for MiFare one card
	 * 
	 * @param nSectorIndex
	 *            : sector index
	 * @param nBlockIndex
	 *            : block index
	 * @param pDataBuffer
	 *            : data buffer
	 * @param nDataBufferLength
	 *            : buffer length 
	 * @return 
	 *    - value >= 0 : data length 
	 *    - value  < 0 : error code
	 */
	public native static int readMifare(int nSectorIndex, int nBlockIndex,
			byte[] pDataBuffer, int nDataBufferLength);

	/**
	 * Write data only for MiFare one card
	 * 
	 * @param nSectorIndex
	 *            : sector index
	 * @param nBlockIndex
	 *            : block index
	 * @param pData
	 *            : data
	 * @param nDataLength
	 *            : data length 
	 * @return 
	 *    - value >= 0 : success (suggest 0) 
	 *    - value  < 0 : error code
	 */
	public native static int writeMifare(int nSectorIndex, int nBlockIndex,
			byte[] pData, int nDataLength);
	
	/** }@ */
	
}
