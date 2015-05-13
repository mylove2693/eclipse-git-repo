package com.cynovo.jni;

/** STRONGLY RECOMMENDED: to implement resource control in Native Layer to avoid resource conflict between 
 *  two Java applications simultaneously invoke this device functionality. 
 */

/**
 * Permission explicit declaration android.permission.KOOLCLOUD_MSR
 */

public class MsrInterface {

	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("cynovo_msr");
	}

	/* native methods as following */

	/**
	 * @defgroup msr MSR_API
	 * @{
	 */

	/**
	 * load msr interface
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int loadMsr();

	/**
	 * unload msr interface
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int unloadMsr();

	/**
	 * open msr device
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int open();

	/**
	 * close msr device
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int close();

	/**
	 * start a thread to read msr data
	 * 
	 * @return - value = 0 : success - value < 0 : creat thread fail
	 */
	public native static int startReadDataThread();

	/**
	 * get track error
	 * 
	 * @param nTrackIndex
	 *            [in]: track index[0,1,2] 0 : 1st track, 1 : 2nd track, 2 : 3rd
	 *            track
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code, -101
	 *         : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int getTrackError(int nTrackIndex);

	/**
	 * get length of track data
	 * 
	 * @param nTrackIndex
	 *            [in]: track index[0,1,2] 0 : 1st track, 1 : 2nd track, 2 : 3rd
	 *            track
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code, -101
	 *         : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int getTrackDataLength(int nTrackIndex);

	/**
	 * get track data.
	 * 
	 * @param nTrackIndex
	 *            [in] : track index[0,1,2] 0 : 1st track, 1 : 2nd track, 2 :
	 *            3rd track
	 * @param byteArry
	 *            [out] : track data
	 * @param nLength
	 *            [in] : length of track data
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code, -101
	 *         : input error, -102 : I/O error, -100 : unknown error
	 */
	public native static int getTrackData(int nTrackIndex, byte[] byteArry,
			int nLength);

	/**
	 * get the msr event.
	 * 
	 * @return - value = -1 : no msr data - value = 0 : msr data event
	 */
	public native static int getEvent();

	/**
	 * set the msr event.
	 */
	public native static void setEvent(int type);

	/** @} */

}