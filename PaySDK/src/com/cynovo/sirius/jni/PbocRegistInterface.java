package com.cynovo.sirius.jni;

public class PbocRegistInterface {
	static {
		System.loadLibrary("UnionpayCloudPos");
		System.loadLibrary("pbocregist");
	}

	public native static int open();

	public native static int close();

	public native static int checkEmv();

	public native static int poll();

	public native static int RegisterNotify();

	public native static int UnRegisterNotify();

	public native static int initEmvState(int state);
}
