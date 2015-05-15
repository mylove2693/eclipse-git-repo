package com.kivvi.jni;

public class SnInterface {
	static {
		/* Driver implementation, so file shall put under /system/lib */
		System.loadLibrary("kivvi_sn");
	}

	public native static int getSN(byte[] byteArry);

}
