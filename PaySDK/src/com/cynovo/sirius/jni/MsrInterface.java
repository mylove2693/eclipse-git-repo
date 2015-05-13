package com.cynovo.sirius.jni;

public class MsrInterface {
	static {
		System.loadLibrary("UnionpayCloudPos");
		System.loadLibrary("msr");
	}

	public native static int open();

	public native static int close();

	public native static int poll(int nTimout);

	public native static int getTrackError(int nTrackIndex);

	public native static int getTrackDataLength(int nTrackIndex);

	public native static int getTrackData(int nTrackIndex, byte byteArry[],
			int nLength);

}
