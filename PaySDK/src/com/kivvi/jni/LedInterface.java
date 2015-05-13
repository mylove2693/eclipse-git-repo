package com.kivvi.jni;

public class LedInterface {
	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("kivvi_led");
	}
	public final static int Camerled = 0x01;
	public final static int redled = 0x02;
	public final static int greenled = 0x03;
	public final static int rednfcled = 0x04;
	public final static int greennfcled = 0x05;
	public final static int bluenfcled = 0x06;
	public final static int yellownfcled = 0x07;

	public final static int ON = 0x01;
	public final static int OFF = 0x00;

	/* native methods as following */

	/**
	 * @defgroup led LED_API
	 * @{
	 */

	/**
	 * open the led driver manager - to avoid the resource conflict, please
	 * strictly follow the preferred process described in the front.
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code, -1 :
	 *         device doesn't exist, -10 : unknown error
	 * @note if no such led on the device, please also implement this API, value
	 *       return -1.
	 */
	public native static int open();

	/**
	 * set the led on or off
	 * 
	 * @param[in] mode : LED_OFF or LED_ON
	 * @param[in] led : CAMERA_LED or other led
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int set(int mode, int color);

	/**
	 * close the led driver manager
	 * 
	 * @return - value >= 0 : success (suggest 0) - value < 0 : error code
	 */
	public native static int close();
	/** }@ */
}
