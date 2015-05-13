package com.cynovo.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KOOLCLOUD_PINPAD
 * android.permission.CYNOVO_PIN_GET_PIN_BLOCK android.permission.CYNOVO_PIN_MAC
 * android.permission.CYNOVO_PIN_ENCRYPT_DATA
 * android.permission.CYNOVO_PIN_UPDATE_USER_KEY
 * android.permission.CYNOVO_PIN_UPDATE_MASTER_KEY
 */

public class PinPadInterface {

	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("cynovo_pinpad");
	}

	/* native methods as following */
	/**
	 * @defgroup pinpad PINPAD_API
	 * @{
	 */

	/**
	 * open the device
	 * 
	 * @return - value ==0 : success - value < 0 : error code, -1 : device
	 *         doesn't exist, -10 : unknown error
	 */
	public native static int open();

	/**
	 * close the device
	 * 
	 * @return - value ==0 : success - value < 0 : error code, -1 : parameter
	 *         mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int close();

	/**
	 * show text on the device
	 * 
	 * @param[in] nLineIndex : line index, from top to down
	 * @param[in] arryText : encoded string
	 * @param[in] nTextLength : length of string
	 * @param[in] nFlagSound : 0 : no voice prompt, 1 : voice prompt
	 * @return - value < 0 : error code, maybe, your display string is too long!
	 *         - value >= 0 : success (suggest 0)
	 */
	public native static int showText(int nLineIndex, byte arryText[],
			int nTextLength, int nFlagSound);

	/**
	 * set the maximal length of pin
	 * 
	 * @param[in] nLength : length >= 0 && length <= 0x0D
	 * @param[in] nFlag : 1 : maximal length, 0 : minimal length
	 * @return - value < 0 : error code - value >= 0 : success (suggest 0)
	 */
	public native static int selectKey(int nKeyType, int nMasterKeyID,
			int nUserKeyType, int nAlgorith);

	/**
	 * set the maximal length of pin
	 * 
	 * @param[in] nLength : length >= 0 && length <= 0x0D
	 * @param[in] nFlag : 1 : maximal length, 0 : minimal length
	 * @return - value < 0 : error code - value >= 0 : success (suggest 0)
	 */
	public native static int setPinLength(int nLength, int nFlag);

	/**
	 * encrypt string using user key (common encrypt method, for MAC, better to
	 * invoke calculateMac())
	 * 
	 * @param[in] arryPlainText : plain text
	 * @param[in] nTextLength : length of plain text
	 * @param[out] arryCipherTextBuffer : buffer for saving cipher text
	 * @return - value < 0 : error code - value >= 0 : success, length of cipher
	 *         text length
	 */
	public native static int encrypt(byte arryPlainText[], int nTextLength,
			byte arryCipherTextBuffer[]);

	/**
	 * calculate pin block
	 * 
	 * @param[in] arryASCIICardNumber : card number in ASCII format
	 * @param[in] nCardNumberLength : length of card number
	 * @param[out] arryPinBlockBuffer : buffer for saving pin block
	 * @param[in] nTimeout_MS : timeout waiting for user input in milliseconds,
	 *            if it is less than zero, then wait forever
	 * @param[in] nFlagSound : 0 : no voice prompt, 1 : voice prompt
	 * @return - value < 0 : error code - value >= 0 : success, length of pin
	 *         block
	 */
	public native static int calculatePinBlock(byte arryASCIICardNumber[],
			int nCardNumberLength, byte arryPinBlockBuffer[], int nTimeout_MS,
			int nFlagSound);

	/**
	 * calculate the MAC using current user key (real implementation is
	 * required)
	 * 
	 * @param[in] arryData : data
	 * @param[in] nDataLength : data length
	 * @param[in] nMACFlag : 0 : X99, 1 : ECB
	 * @param[out] arryMACOutBuffer : MAC data buffer
	 * @return - value < 0 : error code - value >= 0 : success, length of MAC
	 *         data buffer
	 */
	public native static int calculateMac(byte arryData[], int nDataLength,
			int nMACFlag, byte arryMACOutBuffer[]);

	/**
	 * update the user key
	 * 
	 * @param[in] nMasterKeyID : master key ID
	 * @param[in] nUserKeyID : user key ID
	 * @param[in] arryCipherNewUserKey : new user key in cipher text
	 * @param[in] nCipherNewUserKeyLength : length of new user key in cipher
	 *            text
	 * @return - value < 0 : error code - value >= 0 : success (suggest 0)
	 */
	public native static int updateUserKey(int nMasterKeyID, int nUserKeyID,
			byte arryCipherNewUserKey[], int nCipherNewUserKeyLength);

	/**
	 * update the master key
	 * 
	 * @param[in] nMasterKeyID : master key ID
	 * @param[in] arrayOldKey : old key
	 * @param[in] nOldKeyLength : length of old key
	 * @param[in] arrayNewKey : new key
	 * @param[in] nNewKeyLength : length of new key
	 * @return - value < 0 : error code - value >= 0 : success (suggest 0)
	 */
	public native static int updateMasterKey(int nMasterKeyID,
			byte arrayOldKey[], int nOldKeyLength, byte arrayNewKey[],
			int nNewKeyLength);
	/** @} */

}