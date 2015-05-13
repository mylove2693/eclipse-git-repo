package com.cynovo.sirius.jni;

public class PrinterInterface {
	static {
		System.loadLibrary("2printer");
	}

	public native static int open(byte[] status);

	public native static int close();

	public native static int begin();

	public native static int end();

	public native static int write(byte status, byte arryData[], int nDataLength);

	public native static int read(byte status, byte arryData[], int nDataLength);
}
