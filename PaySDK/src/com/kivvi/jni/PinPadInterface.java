package com.kivvi.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KIVVI_PINPAD
 * android.permission.KIVVI_PIN_GET_PIN_BLOCK
 * android.permission.KIVVI_PIN_MAC
 * android.permission.KIVVI_PIN_ENCRYPT_DATA
 * android.permission.KIVVI_PIN_UPDATE_USER_KEY
 * android.permission.KIVVI_PIN_UPDATE_MASTER_KEY
 */

public class PinPadInterface {

	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("kivvi_pinpad");
	}

	/**
	 * key code reference definition, better to define in high-tier class public
	 * final static int KEY_TYPE_DUKPT = 0; public final static int
	 * KEY_TYPE_TDUKPT = 1; public final static int KEY_TYPE_MASTER = 2; public
	 * final static int KEY_TYPE_PUBLIC = 3; public final static int
	 * KEY_TYPE_FIX = 4; public final static int MAC_METHOD_X99 = 0; public
	 * final static int MAC_METHOD_ECB = 1;
	 */

	/* native methods as following */

	/**
	 * open the device 
	 * @return value ==0 : success < 0 : error code, -1 : device
	 * doesn't exist, -10 : unknown error
	 */
	public native static int open();

	/**
	 * close the device 
	 * @return value ==0 : success < 0 : error code, -1 :
	 * parameter mismatch, -2 : I/O error, -10 : unknown error
	 */
	public native static int close();

	/**
	 * show text on the device
	 * 
	 * @param nLineIndex
	 *            : line index, from top to down
	 * @param arryText
	 *            : encoded string
	 * @param nTextLength
	 *            : length of string
	 * @param nFlagSound
	 *            : 0 : no voice prompt, 1 : voice prompt 
	 * @return value < 0 :
	 *            error code, maybe, your display string is too long! >= 0 :
	 *            success (suggest 0)
	 */
	public native static int showText(int nLineIndex, byte arryText[],
			int nTextLength, int nFlagSound);

	/**
	 * select master key and user key (usually, one device has only one master
	 * key, do not need consider multiple master keys)
	 * 
	 * @param nKeyType
	 *            : 0 : dukpt, 1: Tdukpt, 2 : master key, 3 : public key, 4 :
	 *            fix key
	 * @param nMasterKeyID
	 *            : master key ID , [0x00, ..., 0x09], make sense only when
	 *            nKeyType is master-session pair,
	 * @param nUserKeyType
	 *            : user key ID, [0x00, 0x01], 0: pin key, 1: encrypt key
	 * @param nAlgorith
	 *            : 1 : 3DES, 0 : DES 
	 * @return value < 0 : error code >= 0 :
	 *            success (suggest 0)
	 */
	public native static int selectKey(int nKeyType, int nMasterKeyID,
			int nUserKeyType, int nAlgorith);

	/**
	 * set the maximal length of pin
	 * 
	 * @param nLength
	 *            : length >= 0 && length <= 0x0D
	 * @param nFlag
	 *            : 1 : maximal length, 0 : minimal length 
	 * @return value < 0 :
	 *            error code >= 0 : success (suggest 0)
	 */
	public native static int setPinLength(int nLength, int nFlag);

	/**
	 * encrypt string using user key (common encrypt method, for MAC, better to
	 * invoke calculateMac())
	 * 
	 * @param arryPlainText
	 *            : plain text
	 * @param nTextLength
	 *            : length of plain text
	 * @param arryCipherTextBuffer
	 *            : buffer for saving cipher text 
	 * @return value < 0 : error code
	 *            >= 0 : success, length of cipher text length
	 */
	public native static int encrypt(byte arryPlainText[], int nTextLength,
			byte arryCipherTextBuffer[]);

	/**
	 * calculate pin block
	 * 
	 * @param arryASCIICardNumber
	 *            : card number in ASCII format
	 * @param nCardNumberLength
	 *            : length of card number
	 * @param arryPinBlockBuffer
	 *            : buffer for saving pin block
	 * @param nTimeout_MS
	 *            : timeout waiting for user input in milliseconds, if it is
	 *            less than zero, then wait forever
	 * @param nFlagSound
	 *            : 0 : no voice prompt, 1 : voice prompt
	 * @return value < 0 : error code >= 0 : success, length of pin block
	 */
	public native static int calculatePinBlock(byte arryASCIICardNumber[],
			int nCardNumberLength, byte arryPinBlockBuffer[], int nTimeout_MS,
			int nFlagSound);

	/**
	 * calculate the MAC using current user key (real implementation is
	 * required)
	 * 
	 * @param arryData
	 *            : data
	 * @param nDataLength
	 *            : data length
	 * @param nMACFlag
	 *            : 0 : X99, 1 : ECB
	 * @param arryMACOutBuffer
	 *            : MAC data buffer 
	 * @return value < 0 : error code >= 0 :
	 *            success, length of MAC data buffer
	 */
	public native static int calculateMac(byte arryData[], int nDataLength,
			int nMACFlag, byte arryMACOutBuffer[]);

	/**
	 * update the user key
	 * 
	 * @param nMasterKeyID
	 *            : master key ID
	 * @param nUserKeyID
	 *            : user key ID
	 * @param arryCipherNewUserKey
	 *            : new user key in cipher text
	 * @param nCipherNewUserKeyLength
	 *            : length of new user key in cipher text 
	 * @return value < 0 :
	 *            error code >= 0 : success (suggest 0)
	 */
	public native static int updateUserKey(int nMasterKeyID, int nUserKeyID,
			byte arryCipherNewUserKey[], int nCipherNewUserKeyLength);

	/**
	 * update the master key
	 * 
	 * @param nMasterKeyID
	 *            : master key ID
	 * @param arrayOldKey
	 *            : old key
	 * @param nOldKeyLength
	 *            : length of old key
	 * @param arrayNewKey
	 *            : new key
	 * @param nNewKeyLength
	 *            : length of new key 
	 * @return value < 0 : error code >= 0 :
	 *            success (suggest 0)
	 */
	public native static int updateMasterKey(int nMasterKeyID,
			byte arrayOldKey[], int nOldKeyLength, byte arrayNewKey[],
			int nNewKeyLength);

}
