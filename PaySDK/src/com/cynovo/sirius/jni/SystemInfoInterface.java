package com.cynovo.sirius.jni;

public class SystemInfoInterface {

	static {
		System.loadLibrary("systeminfo");
		System.loadLibrary("systeminfo2");
	}

	public native static int open();

	public native static int close();

	public native static int getinfo(byte[] request, byte[] result);
}
